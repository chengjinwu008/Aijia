package com.cjq.aijia.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.cjq.aijia.entity.EventShutDown;
import com.cjq.aijia.util.MD5;
import com.ypy.eventbus.EventBus;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.listener.CloudCodeListener;

public class CheckService extends Service {
    private boolean flagX=true;

    public CheckService() {
    }

    @Override
    public void onDestroy() {
        flagX=false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        final AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
        new Thread(){
            @Override
            public void run() {
                final boolean[] flag = {true};

                while (flagX){

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(flag[0]){
                        flag[0] =false;
                        ace.callEndpoint(CheckService.this, "check", null,
                                new CloudCodeListener() {
                                    @Override
                                    public void onSuccess(Object object) {

                                        flag[0] = true;

                                        String code = (String) object;
                                        Calendar calendar = Calendar.getInstance(Locale.CHINA);
                                        String s = "chenjinqiang" + calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
                                        String ncode = MD5.getMD5(s.getBytes());

                                        if (!ncode.equals(code)) {
                                            EventBus.getDefault().post(new EventShutDown());
                                        }
                                    }

                                    @Override
                                    public void onFailure(int code, String msg) {
                                        flag[0] = true;
//                                EventBus.getDefault().post(new EventShutDown());
                                    }
                                });
                    }

                }
            }
        }.start();

        return super.onStartCommand(intent, flags, startId);
    }

    class MyBinder extends Binder{

    }
}
