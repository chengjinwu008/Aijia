package com.cjq.aijia.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.cjq.aijia.R;
import com.cjq.aijia.util.TimerRunnable;
import com.cjq.aijia.util.WebUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

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
    Handler handler = new Handler();

    boolean threadFlag = true;

    private final String ag = "同意爱家用户协议";
    private final String ag_url = "http://www.baidu.com";

    @Override
    protected void onDestroy() {

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_request_verify:
                WebUtil.requestShotMsg(this, mobile.getText().toString(), new TimerRunnable(requestVerify, threadFlag, handler));
                break;
            case R.id.register_next_step:
                // TODO: 2015/11/16 验证成功是否需要在线验证
                break;
            case R.id.register_close:
                finish();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            //失去焦点判断3个输入框的状态
            checkInputStatus();
        }
    }

    private void checkInputStatus() {
        String username = userName.getText().toString();
        String mob = mobile.getText().toString();
        String ver = verify.getText().toString();

        if (check(username, mob, ver)) {
            nextStep.setBackgroundDrawable(getResources().getDrawable(R.drawable.button1));
            nextStep.setEnabled(true);
        } else {
            nextStep.setBackgroundDrawable(getResources().getDrawable(R.drawable.button3));
            nextStep.setEnabled(false);
        }
    }

    //验证规则
    private boolean check(String username, String mob, String ver) {
        if ("".equals(username)) {
            return false;
        }
        if ("".equals(mob)) {
            return false;
        }
        if ("".equals(ver)) {
            return false;
        }

        return true;
    }
}
