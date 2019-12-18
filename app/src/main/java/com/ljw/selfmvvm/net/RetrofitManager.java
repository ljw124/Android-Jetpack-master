package com.ljw.selfmvvm.net;

import android.os.Environment;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.ljw.selfmvvm.common.SystemConst;
import com.ljw.selfmvvm.net.Interceptor.HttpLogInterceptor;
import com.ljw.selfmvvm.net.Interceptor.NetCacheInterceptor;
import com.ljw.selfmvvm.net.Interceptor.OfflineCacheInterceptor;
import com.ljw.selfmvvm.utils.ContextUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by Ljw on 2019/12/12 11:45
 * Retrofit管理类
 */
public class RetrofitManager {

    private static RetrofitManager retrofitManager;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    private PersistentCookieJar cookieJar;
    private RetrofitApiService retrofitApiService;

    private RetrofitManager(){
        cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(ContextUtils.getApp()));
        //如果后端没有提供退出登录接口，还可以通过以下主动清理
//        cookieJar.clear();
//        cookieJar.clearSession();
        initOkHttpClient();
        initRetrofit();
    }

    public static RetrofitManager getRetrofitManager(){
        if (retrofitManager == null){
            synchronized (RetrofitManager.class){
                if (retrofitManager == null){
                    retrofitManager = new RetrofitManager();
                }
            }
        }
        return retrofitManager;
    }

    //初始化OkHttp，并设置拦截器
    private void initOkHttpClient() {
        okHttpClient = new OkHttpClient.Builder()
                //设置缓存文件路径，和文件大小
                .cache(new Cache(new File(Environment.getExternalStorageDirectory() + "/okhttp_cache/"), 50 * 1024 * 1024))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                //记录请求日志
                .addInterceptor(new HttpLogInterceptor())
                //设置在线和离线缓存
                .addInterceptor(OfflineCacheInterceptor.getInstance())
                .addNetworkInterceptor(NetCacheInterceptor.getInstance())
                .cookieJar(cookieJar)
                .build();
    }

    //初始化Retrofit，并通过Retrofit创建提供给外部访问的接口
    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(SystemConst.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        retrofitApiService = retrofit.create(RetrofitApiService.class);
    }

    //向外提供统一的网络请求接口
    public static RetrofitApiService getRetrofitApiService(){
        if (retrofitManager == null){
            retrofitManager = new RetrofitManager();
        }
        return retrofitManager.retrofitApiService;
    }
}
