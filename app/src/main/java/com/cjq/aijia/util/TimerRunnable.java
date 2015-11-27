package com.cjq.aijia.util;

import android.os.Handler;
import android.widget.TextView;

/**
 * Created by CJQ on 2015/11/16.
 */
public class TimerRunnable implements Runnable {

    private TextView requestVerify;
    private boolean threadFlag;
    private Handler handler;

    public TimerRunnable(TextView requestVerify, boolean threadFlag, Handler handler) {
        this.requestVerify = requestVerify;
        this.threadFlag = threadFlag;
        this.handler = handler;
    }

    @Override
    public void run() {
        requestVerify.setEnabled(false);
        //进入倒计时
        new Thread() {
            @Override
            public void run() {
                int time = 60;
                while (time >= 1 && threadFlag) {
                    final int finalTime = time;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            requestVerify.setText("再次发送（" + finalTime + "）");
                        }
                    });

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    time--;
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        requestVerify.setText("发送验证码");
                        requestVerify.setEnabled(true);
                    }
                });
                super.run();
            }
        }.start();
    }
}
