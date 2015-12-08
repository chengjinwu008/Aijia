package com.cjq.aijia.reveiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.cjq.aijia.entity.EventMainRefresh;
import com.cjq.aijia.entity.EventNoNetChange;
import com.cjq.aijia.util.WebUtil;
import com.ypy.eventbus.EventBus;

/**
 * Created by CJQ on 2015/12/2.
 */
public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!WebUtil.checkNetWork((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))) {
            EventBus.getDefault().post(new EventNoNetChange());
        }
    }
}
