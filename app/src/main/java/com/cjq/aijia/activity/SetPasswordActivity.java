package com.cjq.aijia.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.cjq.aijia.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    @InjectView(R.id.set_password_new)
    EditText passwordNew;
    @InjectView(R.id.set_password_confirm)
    EditText passwordConfirm;
    @InjectView(R.id.set_password_close)
    View close;
    @InjectView(R.id.set_password_done)
    View done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        ButterKnife.inject(this);

        close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.set_password_close:
                finish();
                break;
            case R.id.set_password_done:
                // TODO: 2015/11/16 提交修改的密码
                break;
        }
    }
}
