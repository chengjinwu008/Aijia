package com.cjq.aijia.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by CJQ on 2015/11/16.
 */
public class ToastUtil {
    private static String oldMsg;
    protected static Toast toastNormal = null;
    private static long oneTime = 0;
    private static long twoTime = 0;

    public static void showToast(Context context, String s) {
        if (toastNormal == null) {
            toastNormal = Toast.makeText(context, s, Toast.LENGTH_SHORT);
            toastNormal.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toastNormal.show();
                }
            } else {
                oldMsg = s;
                toastNormal.setText(s);
                toastNormal.show();
            }
        }
        oneTime = twoTime;
    }


    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }
}
