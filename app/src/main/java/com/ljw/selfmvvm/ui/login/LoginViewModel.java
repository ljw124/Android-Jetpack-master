package com.ljw.selfmvvm.ui.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import com.ljw.selfmvvm.base.BaseModelImpl;
import com.ljw.selfmvvm.base.BaseViewModel;
import com.ljw.selfmvvm.bean.UserBean;
import com.ljw.selfmvvm.bean.basebean.ParamsBuilder;
import com.ljw.selfmvvm.bean.basebean.Resource;

import java.util.HashMap;

/**
 * Create by Ljw on 2019/12/16 17:43
 */
public class LoginViewModel extends BaseViewModel<BaseModelImpl> {

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Resource<UserBean>> login(HashMap<String, Object> map, ParamsBuilder paramsBuilder) {
        return getModel().login(map, paramsBuilder);
    }
}
