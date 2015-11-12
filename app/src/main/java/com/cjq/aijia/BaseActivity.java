package com.cjq.aijia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cjq.aijia.entity.EventShutDown;
import com.ypy.eventbus.EventBus;

public class BaseActivity extends AppCompatActivity {

    public void onEventMainThread(EventShutDown ev){
        finish();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }
}
