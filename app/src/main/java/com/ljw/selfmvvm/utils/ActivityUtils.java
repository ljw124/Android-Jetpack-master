package com.ljw.selfmvvm.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.ljw.selfmvvm.MyApplication;

/**
 * Create by Ljw on 2019/12/13 11:21
 */
public class ActivityUtils {

    private ActivityUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void startActivity(Context context, Class<? extends Activity> clz) {
        Intent intent = new Intent(context, clz);
        context.startActivity(intent);
    }

    public static void finishWithAnim(Activity activity, int enterAnim, int exitAnim) {
        activity.finish();
        activity.overridePendingTransition(enterAnim, exitAnim);
    }

    public static Intent build(Context context, Class<? extends Activity> clz){
        Intent intent = new Intent(context, clz);
        return intent;
    }

    public static void startHome(){
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getContext().startActivity(homeIntent);
    }
}
