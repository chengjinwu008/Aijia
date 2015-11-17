package com.cjq.aijia.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import com.cjq.aijia.CommonData;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.marshalchen.common.commonUtils.urlUtils.HttpUtilsAsync;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

/**
 * Created by CJQ on 2015/11/16.
 */
public class WebUtil {

    public static void requestLogin(String userName,String password, final Runnable dealAfterSuccess){
        RequestParams params = new RequestParams();
        params.put("userName",userName);
        params.put("password",password);
        HttpUtilsAsync.post(CommonData.LOGIN_URL,params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // 准备开始请求登录
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // 请求登录成功返回数据
                dealAfterSuccess.run();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // 请求登录服务器错误
            }

            @Override
            public void onRetry(int retryNo) {
                // 重试请求登录
            }
        });
    }

    public static void requestShotMsg(Context context,String s,final Runnable dealAfterSuccess) throws JSONException {
        if(!checkMobile(s)){
            ToastUtil.showToast(context,"请输入正确的手机号");
        }else{
            RequestParams params = new RequestParams();

            JSONObject paramObj=new JSONObject();
            JSONObject dataObj=new JSONObject();
            dataObj.put("phoneNumber",s);
            paramObj.put("code","0001");
            paramObj.put("data",dataObj);
            params.put("opjson",paramObj.toString());

            HttpUtilsAsync.post(CommonData.SHORTMSG_URL,params, new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    // 准备开始请求短信
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    // 请求登录成功返回数据
                    dealAfterSuccess.run();
                    System.out.println(new String(response));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    // 请求登录服务器错误
                }

                @Override
                public void onRetry(int retryNo) {
                    // 重试请求登录
                }
            });
        }
    }

    private static boolean checkMobile(String s) {
        return !("".equals(s) || !s.matches("^1[3|4|5|8][0-9]\\d{4,8}$"));
    }

    public static boolean checkNetWork(ConnectivityManager manager){
        NetworkInfo network = manager.getActiveNetworkInfo();
        if(network==null){
            return false;
        }else if(!network.isAvailable() ||!network.isConnected()||network.isFailover() ){
            return false;
        }
        return true;
    }
}
