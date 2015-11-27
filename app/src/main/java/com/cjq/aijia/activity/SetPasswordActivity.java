package com.cjq.aijia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.cjq.aijia.R;
import com.cjq.aijia.util.ToastUtil;
import com.cjq.aijia.util.Validator;
import com.cjq.aijia.util.WebUtil;

import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SetPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    public final static String FLAG_REGISTER = "register";
    public final static String FLAG_RESET = "reset";
    public final static String FLAG_FIND = "find";

    @InjectView(R.id.set_password_new)
    EditText passwordNew;
    @InjectView(R.id.set_password_confirm)
    EditText passwordConfirm;
    @InjectView(R.id.set_password_close)
    View close;
    @InjectView(R.id.set_password_done)
    View done;
    private String userName;
    private String mobile;
    private String verify;
    private String flag;
    private boolean threadFlag=true;
    Handler handler = new Handler();

    @Override
    protected void onDestroy() {
        threadFlag = false;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        ButterKnife.inject(this);

        close.setOnClickListener(this);
        done.setOnClickListener(this);
        Intent intent = getIntent();

        userName = intent.getStringExtra("userName");
        mobile = intent.getStringExtra("mobile");
        verify = intent.getStringExtra("verify");
        flag = intent.getStringExtra("flag");

        if (flag == null || "".equals(flag)) {
            Log.e("SetPasswordActivity","要启动改活动必须传入相应的FLAG");
            finish();
        }

        new Thread(){
            @Override
            public void run() {
                while (threadFlag){
                    String pn =  passwordNew.getText().toString();
                    String pc =  passwordConfirm.getText().toString();
                    if(Validator.checkPassword(pn) && pn.equals(pc)){
                        if(!done.isEnabled()){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    done.setBackgroundResource(R.drawable.button1);
                                    done.setEnabled(true);
                                }
                            });
                        }
                    }else{
                        if(done.isEnabled()){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    done.setBackgroundResource(R.drawable.button3);
                                    done.setEnabled(false);
                                }
                            });
                        }
                    }

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                super.run();
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.set_password_close:
                finish();
                break;
            case R.id.set_password_done:

                switch (flag){
                    case FLAG_REGISTER:

                        try {
                            WebUtil.requestRegister(this, userName, mobile, verify, passwordNew.getText().toString(), new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    case FLAG_FIND:

                        try {
                            WebUtil.requestFindPassword(this, mobile, verify, passwordNew.getText().toString(), new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.showToast(SetPasswordActivity.this,"密码已经找回并设置到新的密码，请重新登录");
                                    finish();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                }

                break;
        }
    }
}
