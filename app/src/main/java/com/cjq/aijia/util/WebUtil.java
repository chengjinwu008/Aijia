package com.cjq.aijia.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cjq.aijia.CommonData;
import com.cjq.aijia.R;
import com.cjq.aijia.entity.EventLogin;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.marshalchen.common.commonUtils.urlUtils.HttpUtilsAsync;
import com.ypy.eventbus.EventBus;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * 接口工具
 * Created by CJQ on 2015/11/16.
 */
public class WebUtil {

    /**
     * 请求登录接口
     *
     * @param userName         用户名
     * @param password         密码
     * @param dealAfterSuccess 异步处理执行体
     */
    public static void requestLogin(final Context context, String userName, String password, final Runnable dealAfterSuccess) throws JSONException {
        RequestParams params = new RequestParams();

        JSONObject paramObj = new JSONObject();
        JSONObject dataObj = new JSONObject();
        dataObj.put("userName", userName);
        dataObj.put("password", password);
        paramObj.put("code", "0002");
        paramObj.put("data", dataObj);
        params.put("opjson", paramObj.toString());
        HttpUtilsAsync.post(CommonData.LOGIN_URL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // 准备开始请求登录
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    JSONObject res = new JSONObject(new String(response));

                    if ("0000".equals(res.getString("code"))) {
                        JSONObject data = res.getJSONObject("data");

                        SaveTool.save(context, "key", data.getString("key"));
                        SaveTool.save(context, "userId", data.getString("userId"));
                        EventBus.getDefault().post(new EventLogin());
                        if(dealAfterSuccess!=null)
                        dealAfterSuccess.run();
                    } else {
                        ToastUtil.showToast(context, res.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    /**
     * 请求短信接口
     *
     * @param context          上下文
     * @param s                手机号
     * @param dealAfterSuccess 异步处理执行体
     * @throws JSONException
     */
    public static void requestShotMsg(final Context context, String s, final Runnable dealAfterSuccess) throws JSONException {
        if (!checkMobile(s)) {
            ToastUtil.showToast(context, "请输入正确的手机号");
        } else {
            RequestParams params = new RequestParams();

            JSONObject paramObj = new JSONObject();
            JSONObject dataObj = new JSONObject();
            dataObj.put("phoneNumber", s);
            paramObj.put("code", "0001");
            paramObj.put("data", dataObj);
            params.put("opjson", paramObj.toString());

            HttpUtilsAsync.post(CommonData.SHORT_MSG_URL, params, new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    // 准备开始请求短信
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    // 请求登录成功返回数据
                    System.out.println(new String(response));
                    try {
                        JSONObject res = new JSONObject(new String(response));

                        if ("0000".equals(res.getString("code"))) {
                            ToastUtil.showToast(context, context.getString(R.string.request_msg_hint));
                            if(dealAfterSuccess!=null)
                            dealAfterSuccess.run();
                        } else {
                            ToastUtil.showToast(context, res.getString("msg"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
        return !("".equals(s) || !s.matches("^1[3|4|5|8|7][0-9]\\d{8}$"));
    }

    /**
     * 检查网络连接
     *
     * @param manager 连接管理器
     * @return true：网络通畅|false：网络没有连接
     */
    public static boolean checkNetWork(ConnectivityManager manager) {
        NetworkInfo network = manager.getActiveNetworkInfo();
        if (network == null) {
            return false;
        } else if (!network.isAvailable() || !network.isConnected() || network.isFailover()) {
            return false;
        }
        return true;
    }

    /**
     * 注册的第一步
     *
     * @param context
     * @param userName         用户名
     * @param mobile           手机号
     * @param verify           验证码
     * @param dealAfterSuccess 后期处理
     * @throws JSONException
     */
    public static void checkInfo(final Context context, String userName, String mobile, String verify, final Runnable dealAfterSuccess) throws JSONException {
        RequestParams params = new RequestParams();

        JSONObject paramObj = new JSONObject();
        JSONObject dataObj = new JSONObject();
        dataObj.put("phoneNumber", mobile);
        dataObj.put("vCode", verify);
        dataObj.put("userName", userName);
        paramObj.put("code", "0013");
        paramObj.put("data", dataObj);
        params.put("opjson", paramObj.toString());

        HttpUtilsAsync.post(CommonData.CHECK_INFO_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject res = new JSONObject(new String(responseBody));

                    if ("0000".equals(res.getString("code"))) {
                        if(dealAfterSuccess!=null)
                        dealAfterSuccess.run();
                    } else {
                        ToastUtil.showToast(context, res.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    /**
     * 注册请求接口
     *
     * @param context
     * @param userName         用户名
     * @param mobile           手机号
     * @param verify           验证码
     * @param password         密码
     * @param dealAfterSuccess 后期处理
     * @throws JSONException
     */
    public static void requestRegister(final Context context, String userName, String mobile, String verify, String password, final Runnable dealAfterSuccess) throws JSONException {
        RequestParams params = new RequestParams();

        JSONObject paramObj = new JSONObject();
        JSONObject dataObj = new JSONObject();
        dataObj.put("phoneNumber", mobile);
        dataObj.put("vCode", verify);
        dataObj.put("userName", userName);
        dataObj.put("password", password);
        paramObj.put("code", "0011");
        paramObj.put("data", dataObj);
        params.put("opjson", paramObj.toString());

        HttpUtilsAsync.post(CommonData.CHECK_INFO_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject res = new JSONObject(new String(responseBody));

                    if ("0000".equals(res.getString("code"))) {
                        JSONObject data = res.getJSONObject("data");

                        SaveTool.save(context, "key", data.getString("key"));
                        SaveTool.save(context, "userId", data.getString("userId"));
                        EventBus.getDefault().post(new EventLogin());
                        if(dealAfterSuccess!=null)
                        dealAfterSuccess.run();
                    } else {
                        ToastUtil.showToast(context, res.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    /**
     * 找回密码第一步
     * @param context
     * @param mobile 手机号
     * @param verify 验证码
     * @param dealAfterSuccess 后期
     * @throws JSONException
     */
    public static void findCheckMobile(final Context context,  String mobile, String verify, final Runnable dealAfterSuccess) throws JSONException {
        RequestParams params = new RequestParams();

        JSONObject paramObj = new JSONObject();
        JSONObject dataObj = new JSONObject();
        dataObj.put("phoneNumber", mobile);
        dataObj.put("vCode", verify);
        paramObj.put("code", "0007");
        paramObj.put("data", dataObj);
        params.put("opjson", paramObj.toString());

        HttpUtilsAsync.post(CommonData.FIND_PASSWORD_CHECK_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject res = new JSONObject(new String(responseBody));

                    if ("0000".equals(res.getString("code"))) {
                        if(dealAfterSuccess!=null)
                        dealAfterSuccess.run();
                    } else {
                        ToastUtil.showToast(context, res.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    /**
     * 找回密码第二步
     * @param context
     * @param mobile 手机号
     * @param verify 验证码
     * @param password 密码
     * @param dealAfterSuccess 后期
     * @throws JSONException
     */
    public static void requestFindPassword(final Context context,  String mobile, String verify,String password, final Runnable dealAfterSuccess) throws JSONException {
        RequestParams params = new RequestParams();

        JSONObject paramObj = new JSONObject();
        JSONObject dataObj = new JSONObject();
        dataObj.put("phoneNumber", mobile);
        dataObj.put("vCode", verify);
        dataObj.put("password",password);
        paramObj.put("code", "0012");
        paramObj.put("data", dataObj);
        params.put("opjson", paramObj.toString());

        HttpUtilsAsync.post(CommonData.FIND_PASSWORD_CHECK_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject res = new JSONObject(new String(responseBody));

                    if ("0000".equals(res.getString("code"))) {
                        if(dealAfterSuccess!=null)
                        dealAfterSuccess.run();
                    } else {
                        ToastUtil.showToast(context, res.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
