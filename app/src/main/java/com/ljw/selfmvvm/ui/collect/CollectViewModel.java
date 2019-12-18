package com.ljw.selfmvvm.ui.collect;

import android.app.Application;

import com.ljw.selfmvvm.base.BaseModelImpl;
import com.ljw.selfmvvm.base.BaseViewModel;
import com.ljw.selfmvvm.bean.HomeCollectionBean;
import com.ljw.selfmvvm.bean.basebean.ParamsBuilder;
import com.ljw.selfmvvm.bean.basebean.Resource;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

/**
 * Create by Ljw on 2019/12/14 14:34
 */
public class CollectViewModel extends BaseViewModel<BaseModelImpl> {

    public CollectViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Resource<HomeCollectionBean>> getCollectArticles(int curPage, ParamsBuilder paramsBuilder) {
        return getModel().getCollectArticles(curPage, paramsBuilder);
    }

    public LiveData<Resource<String>> unCollectByMe(int id, int originId) {
        return getModel().unCollectByMe(id, originId);
    }

}
