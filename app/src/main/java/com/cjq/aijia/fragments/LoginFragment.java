package com.cjq.aijia.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cjq.aijia.R;
import com.cjq.aijia.activity.FindPasswordActivity;
import com.cjq.aijia.activity.RegisterActivity;
import com.cjq.aijia.entity.EventLogin;
import com.cjq.aijia.util.WebUtil;
import com.ypy.eventbus.EventBus;

import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by CJQ on 2015/11/13.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private static Fragment INSTANCE;
    @InjectView(R.id.login_user_name)
    EditText userNameText;
    @InjectView(R.id.login_password)
    EditText passwordText;
    @InjectView(R.id.login_btn)
    View loginBtn;
    @InjectView(R.id.register_btn)
    View registerBtn;
    @InjectView(R.id.forget_btn)
    View forgetBtn;

    public static Fragment getInstance() {
        if (INSTANCE == null)
            INSTANCE = new LoginFragment();
        return INSTANCE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        ButterKnife.inject(this,view);

        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        forgetBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:
                doLogin();
                break;
            case R.id.register_btn:
                showRegister();
                break;
            case R.id.forget_btn:
                showFinder();
                break;
        }
    }

    //显示找回密码页面
    private void showFinder() {
        Intent intent = new Intent(getActivity(), FindPasswordActivity.class);
        startActivity(intent);
    }

    //跳转到注册页面
    private void showRegister() {
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        startActivity(intent);
    }

    //登录
    private void doLogin() {
        String userName =  userNameText.getText().toString();
        String password = passwordText.getText().toString();

        try {
            WebUtil.requestLogin(getActivity(),userName, password, new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new EventLogin());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
