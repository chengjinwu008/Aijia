package com.cjq.aijia.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by CJQ on 2015/11/12.
 */
public class SaveTool {

    @SuppressLint("CommitPrefEdits")
    public static void save(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if(key==null || "".equals(key)){
            editor.clear();
            editor.commit();
            return;
        }
        if (value==null || "".equals(value))
        editor.remove(key);
        else
        editor.putString(key, String.valueOf(value));
        editor.commit();
    }

    public static String getKey(Context context) throws Exception {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String res = preferences.getString("key", "");
        if(!"".equals(res))
            return res;
        throw new Exception("没有登录");
    }

    public static String getUserId(Context context) throws Exception {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String res = preferences.getString("user_id","");
        if(!"".equals(res))
            return res;
        throw new Exception("没有登录");
    }
}
