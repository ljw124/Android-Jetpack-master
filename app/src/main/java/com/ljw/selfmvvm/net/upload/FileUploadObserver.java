package com.ljw.selfmvvm.net.upload;

import android.os.Handler;
import android.os.Looper;

import io.reactivex.observers.DefaultObserver;

/**
 * Create by Ljw on 2019/12/14 10:11
 */
public abstract class FileUploadObserver<T> extends DefaultObserver<T> {
    Handler mDelivery = new Handler(Looper.getMainLooper());

    @Override
    public void onNext(final T t) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                onUpLoadSuccess(t);
            }
        });

    }

    @Override
    public void onError(final Throwable e) {

        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                onUpLoadFail(e);
            }
        });
    }

    @Override
    public void onComplete() {

    }

    //监听进度的改变
    public void onProgressChange(final long bytesWritten, final long contentLength) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                onProgress((int) (bytesWritten * 100 / contentLength));
            }
        });

    }

    //上传成功的回调
    public abstract void onUpLoadSuccess(T t);

    //上传失败回调
    public abstract void onUpLoadFail(Throwable e);

    //上传进度回调
    public abstract void onProgress(int progress);

}

