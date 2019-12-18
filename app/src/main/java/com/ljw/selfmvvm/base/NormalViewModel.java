package com.ljw.selfmvvm.base;

import android.app.Application;

import androidx.annotation.NonNull;

/**
 * Create by Ljw on 2019/12/13 11:03
 * 不需要用ViewModel的,用此类代替
 */
public class NormalViewModel extends BaseViewModel{

    public NormalViewModel(@NonNull Application application) {
        super(application);
    }
}
