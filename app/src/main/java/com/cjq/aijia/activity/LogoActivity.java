package com.cjq.aijia.activity;

import android.os.Bundle;
import android.os.Handler;

import com.cjq.aijia.BaseActivity;
import com.cjq.aijia.R;

/**
 * Created by CJQ on 2015/11/18.
 */
public class LogoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },2000);
    }
}
