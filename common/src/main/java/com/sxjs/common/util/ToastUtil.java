package com.sxjs.common.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @Auther: xp
 * @Date: 2019/9/13 21:53
 * @Description:
 */
public class ToastUtil {

    private static Toast mToast;

    /**
     * Toast快速切换
     */
    public static void showToast(Context context, String msg, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, duration);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
    /**
     * Toast快速切换
     */
    public static void showToast(Context mContext,String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }



    /**
     * 立即取消Toast
     */
    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}
