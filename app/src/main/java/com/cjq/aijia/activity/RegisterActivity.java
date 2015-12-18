package com.cjq.aijia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.cjq.aijia.BaseActivity;
import com.cjq.aijia.R;
import com.cjq.aijia.util.FlashAnimationUtil;
import com.cjq.aijia.util.TimerRunnable;
import com.cjq.aijia.util.Validator;
import com.cjq.aijia.util.WebUtil;

import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

    @InjectView(R.id.register_ag)
    CheckBox registerAg;
    @InjectView(R.id.register_user_name)
    EditText userName;
    @InjectView(R.id.register_mobile)
    EditText mobile;
    @InjectView(R.id.register_verify)
    EditText verify;
    @InjectView(R.id.register_request_verify)
    TextView requestVerify;
    @InjectView(R.id.register_next_step)
    View nextStep;
    @InjectView(R.id.register_close)
    View close;
    @InjectView(R.id.register_divider_one)
    View dividerOne;
    @InjectView(R.id.register_divider_two)
    View dividerTwo;
    @InjectView(R.id.register_divider_three)
    View dividerThree;

    Handler handler = new Handler();

    boolean threadFlag = true;

    private final String ag = "同意爱家用户协议";
    private final String ag_url = "http://www.baidu.com";

    @Override
    protected void onDestroy() {
//        FlashAnimationUtil.endAll();
        threadFlag = false;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
        SpannableString agg = new SpannableString(ag);
        URLSpan urlSpan = new URLSpan(ag_url);
        agg.setSpan(urlSpan, 2, ag.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        registerAg.setText(agg);
        registerAg.setMovementMethod(LinkMovementMethod.getInstance());

        requestVerify.setOnClickListener(this);
        nextStep.setOnClickListener(this);
        close.setOnClickListener(this);

        userName.setOnFocusChangeListener(this);
        mobile.setOnFocusChangeListener(this);
        verify.setOnFocusChangeListener(this);

        new Thread() {
            @Override
            public void run() {
                while (threadFlag) {
                    String userNameText = userName.getText().toString();
                    String mobileText = mobile.getText().toString();
                    String verifyText = verify.getText().toString();
                    if (Validator.checkName(userNameText)&& Validator.checkMobile(mobileText) && Validator.checkVerify(verifyText) && registerAg.isChecked()) {
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
        switch (v.getId()) {
            case R.id.register_request_verify:
                try {
                    WebUtil.requestShotMsg(this, mobile.getText().toString(), new TimerRunnable(requestVerify, threadFlag, handler));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.register_next_step:
                try {
                    WebUtil.checkInfo(this, userName.getText().toString(), mobile.getText().toString(), verify.getText().toString(), new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(RegisterActivity.this,SetPasswordActivity.class);
                            intent.putExtra("userName",userName.getText().toString());
                            intent.putExtra("mobile",mobile.getText().toString());
                            intent.putExtra("verify", verify.getText().toString());
                            intent.putExtra("flag",SetPasswordActivity.FLAG_REGISTER);
//                            intent.putExtra("userName",userName.getText().toString());

                            startActivity(intent);
                            finish();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.register_close:
                finish();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.register_user_name:
                if (!hasFocus) {
                    //验证
                    String userNameText = userName.getText().toString();
                    if (Validator.checkName(userNameText)) {
                        FlashAnimationUtil.changeColorSmooth(dividerOne, getResources().getColor(R.color.right));
                    } else {
                        FlashAnimationUtil.changeColorSmooth(dividerOne, getResources().getColor(R.color.err));
                    }
                }else{
                    FlashAnimationUtil.changeColorSmooth(dividerOne, getResources().getColor(R.color.text_hint));
                }
                break;
            case R.id.register_mobile:
                if (!hasFocus) {
                    String mobileText = mobile.getText().toString();
                    if (Validator.checkMobile(mobileText)) {
                        FlashAnimationUtil.changeColorSmooth(dividerTwo, getResources().getColor(R.color.right));
                    } else {
                        FlashAnimationUtil.changeColorSmooth(dividerTwo, getResources().getColor(R.color.err));
                    }
                }else{
                    FlashAnimationUtil.changeColorSmooth(dividerTwo, getResources().getColor(R.color.text_hint));
                }
                break;
            case R.id.register_verify:
                if (!hasFocus) {
                    String verifyText = verify.getText().toString();
                    if (Validator.checkVerify(verifyText)) {
                        FlashAnimationUtil.changeColorSmooth(dividerThree, getResources().getColor(R.color.right));
                    } else {
                        FlashAnimationUtil.changeColorSmooth(dividerThree, getResources().getColor(R.color.err));
                    }
                }else{
                    FlashAnimationUtil.changeColorSmooth(dividerThree, getResources().getColor(R.color.text_hint));
                }
                break;
        }
    }

}
