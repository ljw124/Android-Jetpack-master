package com.ljw.selfmvvm.base;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.ljw.selfmvvm.bean.basebean.ParamsBuilder;
import com.ljw.selfmvvm.bean.basebean.Resource;
import com.ljw.selfmvvm.bean.basebean.ResponModel;
import com.ljw.selfmvvm.net.Interceptor.NetCacheInterceptor;
import com.ljw.selfmvvm.net.Interceptor.OfflineCacheInterceptor;
import com.ljw.selfmvvm.net.RetrofitApiService;
import com.ljw.selfmvvm.net.RetrofitManager;
import com.ljw.selfmvvm.net.download.DownFileUtils;
import com.trello.rxlifecycle2.LifecycleTransformer;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Create by Ljw on 2019/12/12 10:48
 * 这是model层的基类，处理数据的业务统一在这里处理
 */
public abstract class BaseModel {

    //解决RxJava可能存在的内存泄漏
    private LifecycleTransformer lifecycleTransformer;
    //请求到的数据，要防止同一url重复请求网络
    private ArrayList<String> onNetTags;

    //提供统一的网络请求入口
    public RetrofitApiService getApiService(){
        return RetrofitManager.getRetrofitManager().getRetrofitApiService();
    }

    //设置RxJava的生命周期
    public void setLifecycleTransformer(LifecycleTransformer lifecycleTransformer){
        this.lifecycleTransformer = lifecycleTransformer;
    }

    public void setOnNetTags(ArrayList<String> onNetTags){
        this.onNetTags = onNetTags;
    }

    //向外开发处理业务的入口
    public <T> MutableLiveData<T> observeGo(Observable observable, final MutableLiveData<T> liveData){
        return observeGo(observable, liveData, null);
    }

    public <T> MutableLiveData<T> observeGo(Observable observable, final MutableLiveData<T> liveData, ParamsBuilder paramsBuilder){
        if (null == paramsBuilder){
            paramsBuilder = ParamsBuilder.builder();
        }
        if (paramsBuilder.getRetryCount() > 0){
            return observeWithRetry(observable, liveData, paramsBuilder);
        } else {
            return observe(observable, liveData, paramsBuilder);
        }
    }

    //业务统一在这里处理--不会重连
    private <T> MutableLiveData<T> observe(Observable observable, final MutableLiveData<T> liveData, ParamsBuilder paramsBuilder){
        if (null == paramsBuilder){
            paramsBuilder = new ParamsBuilder();
        }
        String loadingMessage = paramsBuilder.getLoadingMessage();
        int offlineCacheTime = paramsBuilder.getOfflineCacheTime();
        int onlineCacheTime = paramsBuilder.getOnlineCacheTime();
        String oneTag = paramsBuilder.getOneTag();
        boolean showDialog = paramsBuilder.isShowDialog();

        if (onlineCacheTime > 0) {
            setOnlineCacheTime(onlineCacheTime);
        }
        if (offlineCacheTime > 0) {
            setOfflineCacheTime(offlineCacheTime);
        }
        if (!TextUtils.isEmpty(oneTag)){
            if (onNetTags.contains(oneTag)){
                return liveData;
            }
        }
        observable.subscribeOn(Schedulers.io()) //指定Observable自身在哪个调度器上执行
                .doOnSubscribe(disposable -> { //doOnSubscribe是事件被订阅之前(也就是事件源发起之前)会调用的方法，一般用于修改、添加或者删除事件源的数据流
                    if (!TextUtils.isEmpty(oneTag)){
                        onNetTags.add(oneTag);
                    }
                    if (showDialog){
                        liveData.postValue((T) Resource.loading(loadingMessage));
                    }
                })
                .doFinally(()->{ //产生的Observable终止之后会被调用，无论是正常还是异常终止
                    if (!TextUtils.isEmpty(oneTag)) {
                        onNetTags.remove(oneTag);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) //指定一个观察者在哪个调度器上观察这个Observable
                .compose(lifecycleTransformer)
                .subscribe(o ->{ //订阅
                    liveData.postValue((T)Resource.response((ResponModel<Object>)o));
                }, throwable -> {
                    liveData.postValue((T)Resource.error((Throwable) throwable));
                });

        return liveData;
    }

    //业务统一在这里处理--会重连
    private <T> MutableLiveData<T> observeWithRetry(Observable observable, MutableLiveData<T> liveData, ParamsBuilder paramsBuilder){
        if (null == paramsBuilder){
            paramsBuilder = ParamsBuilder.builder();
        }
        String loadingMessage = paramsBuilder.getLoadingMessage();
        int offlineCacheTime = paramsBuilder.getOfflineCacheTime();
        int onlineCacheTime = paramsBuilder.getOnlineCacheTime();
        String oneTag = paramsBuilder.getOneTag();
        boolean showDialog = paramsBuilder.isShowDialog();

        if (onlineCacheTime > 0) {
            setOnlineCacheTime(onlineCacheTime);
        }
        if (offlineCacheTime > 0) {
            setOfflineCacheTime(offlineCacheTime);
        }
        if (!TextUtils.isEmpty(oneTag)) {
            if (onNetTags.contains(oneTag)) {
                return liveData;
            }
        }

        int retryCount = paramsBuilder.getRetryCount();
        final int[] currentCount = {0};
        observable.subscribeOn(Schedulers.io())
                .retryWhen(throwable -> {
                    if (currentCount[0] <= retryCount){
                        currentCount[0] ++;
                        return Observable.just(1).delay(5000, TimeUnit.MILLISECONDS);
                    } else {
                        return Observable.error(new Throwable("连接超时"));
                    }
                })
                .doOnSubscribe(disposable -> {
                    if (!TextUtils.isEmpty(oneTag)) {
                        onNetTags.add(oneTag);
                    }
                    if (showDialog) {
                        liveData.postValue((T) Resource.loading(loadingMessage));
                    }
                })
                .doFinally(() -> {
                    if (!TextUtils.isEmpty(oneTag)) {
                        onNetTags.remove(oneTag);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer)
                .subscribe(o ->{
                    liveData.postValue((T)Resource.response((ResponModel<Object>) o));
                }, throwable -> {
                    liveData.postValue((T)Resource.error((Throwable) throwable));
                });

        return liveData;
    }

    //设置在线网络缓存
    private void setOnlineCacheTime(int time) {
        NetCacheInterceptor.getInstance().setOnlineTime(time);
    }

    //设置离线网络缓存
    private void setOfflineCacheTime(int time) {
        OfflineCacheInterceptor.getInstance().setOfflineCacheTime(time);
    }

    //正常下载(重新从0开始下载)
    private <T> MutableLiveData<T> downLoadFile(Observable observable, final MutableLiveData<T> liveData, final String destDir, final String fileName) {
        return downLoadFile(observable, liveData, destDir, fileName, 0);
    }

    //断点下载，如果下载到一半，可从一半开始下载
    private <T> MutableLiveData<T> downLoadFile(Observable observable, final MutableLiveData<T> liveData, final String destDir, final String fileName, long currentLength) {
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(requestBody -> {
                    if (currentLength == 0) {
                        return DownFileUtils.saveFile((ResponseBody) requestBody, destDir, fileName, liveData);
                    } else {
                        return DownFileUtils.saveFile((ResponseBody) requestBody, destDir, fileName, currentLength, liveData);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(file -> {
                    liveData.postValue((T) Resource.success(file));
                }, throwable -> {
                    liveData.postValue((T) Resource.error((Throwable) throwable));
                });
        return liveData;
    }


    //上传文件只有2个参数，showDialog：是否显示dialog；loadmessage：showDialog显示的文字
    private <T> MutableLiveData<T> upLoadFile(Observable observable, MutableLiveData<T> liveData) {
        return upLoadFile(observable, liveData, null);
    }

    //上传文件
    private <T> MutableLiveData<T> upLoadFile(Observable observable, MutableLiveData<T> liveData, ParamsBuilder paramsBuilder) {

        if (paramsBuilder == null) {
            paramsBuilder = paramsBuilder.builder();
        }
        boolean showDialog = paramsBuilder.isShowDialog();
        String loadingMessage = paramsBuilder.getLoadingMessage();

        observable.subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (showDialog) {
                        liveData.postValue((T) Resource.loading(loadingMessage));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                //防止RxJava内存泄漏
                .subscribe(o -> {
                    liveData.postValue((T) Resource.success("成功了"));
                }, throwable -> {
                    liveData.postValue((T) Resource.error((Throwable) throwable));
                });

        return liveData;
    }
}
