package com.cjq.aijia.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cjq.aijia.R;
import com.cjq.aijia.util.TimerRunnable;
import com.cjq.aijia.util.WebUtil;

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.find_password_request_verify:
                WebUtil.requestShotMsg(this, mobile.getText().toString(),new TimerRunnable(requestVerify,threadFlag,handler));
                break;
            case R.id.find_password_next_step:
                // TODO: 2015/11/16 验证规则
                break;
            case R.id.find_password_close:
                finish();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            checkInputStatus();
        }
    }

    private void checkInputStatus() {
        String mob = mobile.getText().toString();
        String ver = verify.getText().toString();

        if (check( mob, ver)) {
            nextStep.setBackgroundDrawable(getResources().getDrawable(R.drawable.button1));
            nextStep.setEnabled(true);
        } else {
            nextStep.setBackgroundDrawable(getResources().getDrawable(R.drawable.button3));
            nextStep.setEnabled(false);
        }
    }

    //验证规则
    private boolean check( String mob, String ver) {
        if ("".equals(mob)) {
            return false;
        }
        if ("".equals(ver)) {
            return false;
        }

        return true;
    }
}
