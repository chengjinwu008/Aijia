package com.cjq.aijia.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class CheckService extends Service {
    public CheckService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        return super.onStartCommand(intent, flags, startId);
    }

    class MyBinder extends Binder{

    }
}
