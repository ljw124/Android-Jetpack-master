package com.ljw.selfmvvm.utils;

import android.content.Context;

import com.ljw.selfmvvm.MyApplication;

/**
 * Create by Ljw on 2019/12/11 17:44
 */
public class ContextUtils {
    private ContextUtils() {
        throw new UnsupportedOperationException("you can't instantiate me...");
    }

    public static Context getApp() {
        return MyApplication.getContext();
    }
}
