package com.ljw.selfmvvm.base;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.JsonSyntaxException;
import com.ljw.selfmvvm.R;
import com.ljw.selfmvvm.bean.basebean.Resource;
import com.ljw.selfmvvm.customview.CustomProgress;
import com.ljw.selfmvvm.utils.ToastUtils;
import com.ljw.selfmvvm.utils.networks.NetWorkUtils;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * Create by Ljw on 2019/12/12 17:21
 */
public abstract class BaseActivity<VM extends BaseViewModel, VDB extends ViewDataBinding> extends RxFragmentActivity implements View.OnClickListener {

    /**
     * 下面三个抽象方法用于子类实现
     */
    //获取当前activity布局文件
    protected abstract int getContentViewId();
    //处理逻辑业务
    protected abstract void processLogic();
    //所有监听放这里
    protected abstract void setListener();

    protected VDB binding;
    protected VM mViewModel;

    private CustomProgress dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, getContentViewId());
        binding.setLifecycleOwner(this);
        createViewModel();
        processLogic();
        setListener();
    }

    public void createViewModel(){
        if (mViewModel == null){
            Class modelClass;
            Type type = getClass().getGenericSuperclass(); //获取当前对象的直接超类的Type，使用该方法可以获取到泛型T的具体类型
            //ParameterizedType 参数化类型，具有<>符号的变量是参数化类型
            if (type instanceof ParameterizedType){
                //获取指定的泛型参数
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            mViewModel = (VM) ViewModelProviders.of(this).get(modelClass);
            mViewModel.setLifecycleTransformer(bindToLifecycle());
        }
    }

    public Context getContext(){
        return this;
    }

    public abstract class OnCallBack<T> implements Resource.OnHandleCallback<T> {
        @Override
        public void onLoading(String showMessage) {
            if (dialog == null){
                dialog = CustomProgress.show(getContext(), "", true, null);
            }
            if (!TextUtils.isEmpty(showMessage)){
                dialog.setMessage(showMessage);
            }
            if (!dialog.isShowing()){
                dialog.show();
            }
        }

        //成功后的业务处理都不一样，放到子类去实现便可
        @Override
        public void onSuccess(T data) {

        }

        @Override
        public void onFailure(String msg) {
            ToastUtils.showToast(msg);
        }

        @Override
        public void onError(Throwable throwable) {
            if (!NetWorkUtils.isNetworkConnected(getContext())) {
                ToastUtils.showToast(getContext().getResources().getString(R.string.result_network_error));
                return;
            }
            if (throwable instanceof ConnectException) {
                ToastUtils.showToast(getContext().getResources().getString(R.string.result_server_error));
            } else if (throwable instanceof SocketTimeoutException) {
                ToastUtils.showToast(getContext().getResources().getString(R.string.result_server_timeout));
            } else if (throwable instanceof JsonSyntaxException) {
                ToastUtils.showToast(getContext().getResources().getString(R.string.result_parse_error));
            } else {
                ToastUtils.showToast(getContext().getResources().getString(R.string.result_empty_error));
            }
        }

        @Override
        public void onCompleted() {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        @Override
        public void onProgress(int precent, long total) {

        }
    }

    //快速获取textView 或 EditText上文字内容
    public String getStringByUI(View view){
        if (view instanceof EditText){
            return ((EditText) view).getText().toString().trim();
        } else if (view instanceof TextView){
            return ((TextView) view).getText().toString().trim();
        }
        return "";
    }
}
