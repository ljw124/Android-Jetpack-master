package com.ljw.selfmvvm.bean.basebean;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * Create by Ljw on 2019/12/12 14:03
 * 定义网络请求状态、业务处理接口
 */
public class Resource<T> {
    //状态  这里有多个状态 0表示加载中；1表示成功；2表示联网失败；3表示接口虽然走通，但走的失败（如：关注失败）
    private static final int LOADING = 0;
    private static final int SUCCESS = 1;
    private static final int ERROR = 2;
    private static final int FAIL = 3;
    private static final int PROGRESS = 4;//注意只有下载文件和上传图片时才会有

    private int state;

    private String errorMsg;
    private T data;
    private Throwable error;

    //这里和文件和进度有关了
    private int precent;//文件下载百分比
    private long total;//文件总大小

    public Resource(int state, T data, String errorMsg){
        this.state = state;
        this.data = data;
        this.errorMsg = errorMsg;
    }

    public Resource(int state, Throwable error) {
        this.state = state;
        this.error = error;
    }

    public Resource(int state, int precent, long total) {
        this.state = state;
        this.precent = precent;
        this.total = total;
    }

    //加载中
    public static <T> Resource<T> loading(String showMsg){
        return new Resource<>(LOADING, null, showMsg);
    }

    //请求成功
    public static <T> Resource<T> success(T data){
        return new Resource<>(SUCCESS, data, null);
    }

    //根据返回的数据判断是否请求成功
    public static <T> Resource<T> response(ResponModel<T> data){
        if (null != data){
            if (data.isSuccess()){
                return new Resource<>(SUCCESS, data.getData(), null);
            } else {
                return new Resource<>(FAIL, null, data.getErrorMsg());
            }
        } else {
            return new Resource<>(ERROR, null, null);
        }
    }

    //失败
    public static <T> Resource<T> failure(String msg) {
        return new Resource<>(ERROR, null, msg);
    }

    //错误
    public static <T> Resource<T> error(Throwable t) {
        return new Resource<>(ERROR, t);
    }

    //进度
    public static <T> Resource<T> progress(int precent, long total) {
        return new Resource<>(PROGRESS, precent, total);
    }

    //供外部调用的入口
    public void handler(OnHandleCallback<T> callback){
        switch (state) {
            case LOADING:
                callback.onLoading(errorMsg);
                break;
            case SUCCESS:
                callback.onSuccess(data);
                break;
            case FAIL:
                callback.onFailure(errorMsg);
                break;
            case ERROR:
                callback.onError(error);
                break;
            case PROGRESS:
                callback.onProgress(precent,total);
                break;
        }
        if (state != LOADING) {
            callback.onCompleted();
        }
    }

    //供外部调用的入口
    public void handler(OnHandleCallback<T> callback, SmartRefreshLayout smartRefreshLayout){
        switch (state) {
            case LOADING:
                callback.onLoading(errorMsg);
                break;
            case SUCCESS:
                callback.onSuccess(data);
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadMore();
                break;
            case FAIL:
                callback.onFailure(errorMsg);
                smartRefreshLayout.finishRefresh(false);
                smartRefreshLayout.finishLoadMore(false);
                break;
            case ERROR:
                callback.onError(error);
                smartRefreshLayout.finishRefresh(false);
                smartRefreshLayout.finishLoadMore(false);
                break;
            case PROGRESS:
                callback.onProgress(precent,total);
                break;
        }
        if (state != LOADING) {
            callback.onCompleted();
        }
    }

    //定义处理业务的接口
    public interface OnHandleCallback<T> {
        void onLoading(String showMessage);

        void onSuccess(T data);

        void onFailure(String msg);

        void onError(Throwable error);

        void onCompleted();

        void onProgress(int precent, long total);
    }
}
