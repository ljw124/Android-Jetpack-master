package com.ljw.selfmvvm;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import com.ljw.selfmvvm.bean.UserBean;
import com.ljw.selfmvvm.customview.x5webview.X5InitService;
import com.ljw.selfmvvm.utils.PreferenceUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

/**
 * Create by Ljw on 2019/12/11 17:32
 */
public class MyApplication extends Application {

    private static MyApplication context;
    private static UserBean loginUser; //用户登录信息

    //static 代码段可以防止内存泄露，初始化刷新、加载更多的样式
    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((mContext, layout) -> {
//            layout.setPrimaryColorsId(R.color.white, R.color.status_background);//全局设置主题颜色
            return new ClassicsHeader(mContext).setPrimaryColorId(R.color.white).setAccentColorId(R.color.status_background);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator((mContext, layout)->{
            return new ClassicsFooter(mContext).setPrimaryColorId(R.color.white).setAccentColorId(R.color.status_background);
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static UserBean getLoginUser(){
        if (loginUser == null){
            loginUser = PreferenceUtil.getByClass("user", UserBean.class);
        }
        return loginUser;
    }

    public static void updateUser(UserBean user){
        PreferenceUtil.putByClass("user", user);
        loginUser = user;
    }

    public static void loginOut(){
        loginUser = null;
        PreferenceUtil.clearByClass(UserBean.class);
    }

    public static Context getContext(){
        return context;
    }

    private void initX5web() {
        Intent intent = new Intent(this, X5InitService.class);
        startService(intent);
    }
}
