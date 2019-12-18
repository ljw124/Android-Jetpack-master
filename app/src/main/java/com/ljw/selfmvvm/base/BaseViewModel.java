package com.ljw.selfmvvm.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.trello.rxlifecycle2.LifecycleTransformer;
import java.util.ArrayList;

/**
 * Create by Ljw on 2019/12/12 16:53
 *  继承AndroidViewModel，是因为里面要用context时候直接可以getApplication()
 */
public abstract class BaseViewModel<T extends BaseModel> extends AndroidViewModel {

    private T model;
    private ArrayList<String> onNetTags;

    public BaseViewModel(@NonNull Application application) {
        super(application);

        createModel();
        onNetTags = new ArrayList<>();
        model.setOnNetTags(onNetTags);
    }

    private void createModel() {
        if (model == null){
            model = (T) new BaseModelImpl();
        }
    }

    public T getModel(){
        return model;
    }

    public void setLifecycleTransformer(LifecycleTransformer lifecycleTransformer){
        if (null != model){
            model.setLifecycleTransformer(lifecycleTransformer);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
