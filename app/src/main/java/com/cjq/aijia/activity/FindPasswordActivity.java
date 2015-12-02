package com.cjq.aijia.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cjq.aijia.R;
import com.cjq.aijia.util.FlashAnimationUtil;
import com.cjq.aijia.util.TimerRunnable;
import com.cjq.aijia.util.Validator;
import com.cjq.aijia.util.WebUtil;

import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class FindPasswordActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    @InjectView(R.id.find_password_request_verify)
    TextView requestVerify;
    @InjectView(R.id.find_password_mobile)
    EditText mobile;
    @InjectView(R.id.find_password_verify)
    EditText verify;
    @InjectView(R.id.find_password_next_step)
    View nextStep;
    @InjectView(R.id.find_password_close)
    View close;
    @InjectView(R.id.find_password_divider_two)
    View dividerTwo;
    @InjectView(R.id.find_password_divider_one)
    View dividerOne;

    private boolean threadFlag=true;
    private Handler handler = new Handler();

    @Override
    protected void onDestroy() {
        threadFlag=false;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        ButterKnife.inject(this);

        requestVerify.setOnClickListener(this);
        nextStep.setOnClickListener(this);
        close.setOnClickListener(this);

        mobile.setOnFocusChangeListener(this);
        verify.setOnFocusChangeListener(this);

        String mobileNum = getIntent().getStringExtra("mobile");

        if(mobileNum!=null && !"".equals(mobileNum)){
            mobile.setText(mobileNum);
            mobile.setEnabled(false);
            dividerOne.setVisibility(View.INVISIBLE);
        }

        new Thread() {
            @Override
            public void run() {
                while (threadFlag) {
                    String mobileText = mobile.getText().toString();
                    String verifyText = verify.getText().toString();
                    if (Validator.checkMobile(mobileText) && Validator.checkVerify(verifyText)) {
                        if (!nextStep.isEnabled()) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    nextStep.setEnabled(true);
                                    nextStep.setBackgroundResource(R.drawable.button1);
                                }
                            });
                        }
                    } else {
                        if (nextStep.isEnabled()) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    nextStep.setEnabled(false);
                                    nextStep.setBackgroundResource(R.drawable.button3);
                                }
                            });
                        }
                    }

                    try {
                        sleep(500);
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
        switch (v.getId()){
            case R.id.find_password_request_verify:
                try {
                    WebUtil.requestShotMsg(this, mobile.getText().toString(),new TimerRunnable(requestVerify,threadFlag,handler));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.find_password_next_step:
                try {
                    WebUtil.findCheckMobile(this, mobile.getText().toString(), verify.getText().toString(), new Runnable() {
                        @Override
                        public void run() {
                            Intent intent =new Intent(FindPasswordActivity.this,SetPasswordActivity.class);
                            intent.putExtra("mobile",mobile.getText().toString());
                            intent.putExtra("verify",verify.getText().toString());
                            intent.putExtra("flag",SetPasswordActivity.FLAG_FIND);
                            startActivity(intent);
                            finish();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.find_password_close:
                finish();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.find_password_mobile:
                if (!hasFocus) {
                    String mobileText = mobile.getText().toString();
                    if (Validator.checkMobile(mobileText)) {
                        FlashAnimationUtil.changeColorSmooth(dividerOne, getResources().getColor(R.color.right));
                    } else {
                        FlashAnimationUtil.changeColorSmooth(dividerOne, getResources().getColor(R.color.err));
                    }
                }else{
                    FlashAnimationUtil.changeColorSmooth(dividerOne, getResources().getColor(R.color.text_hint));
                }
                break;
            case R.id.find_password_verify:
                if (!hasFocus) {
                    String verifyText = verify.getText().toString();
                    if (Validator.checkVerify(verifyText)) {
                        FlashAnimationUtil.changeColorSmooth(dividerTwo, getResources().getColor(R.color.right));
                    } else {
                        FlashAnimationUtil.changeColorSmooth(dividerTwo, getResources().getColor(R.color.err));
                    }
                }else{
                    FlashAnimationUtil.changeColorSmooth(dividerTwo, getResources().getColor(R.color.text_hint));
                }
                break;
        }
    }
}
