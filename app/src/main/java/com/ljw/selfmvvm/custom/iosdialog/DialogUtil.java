package com.ljw.selfmvvm.custom.iosdialog;

import android.app.Activity;
import android.view.View;

/**
 * Create by Ljw on 2019/12/12 11:09
 */
public final class DialogUtil {

    private DialogUtil() {
    }

    //需要no的点击监听，那么再加个接口。实现。
    public interface DialogAlertListener {
        void yes() ;
    }

    /*
    * 仿ios dialog
    * */
    public static void alertIosDialog(Activity act, String message, String confirmMessage, String cancleMessage, final DialogAlertListener listener) {
        IosAlertDialog dialog = new IosAlertDialog(act).builder();
        dialog.setMsg(message);
        dialog.setConfirmMsg(confirmMessage);
        dialog.setConcleMsg(cancleMessage);
        dialog.setConfirmButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.yes();
            }
        });
//        dialog.setNegativeButton("取消", new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//            }
//        });
        if (act != null && !act.isFinishing()) {
            dialog.show();
        }
    }

}
