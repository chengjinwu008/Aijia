package com.cjq.aijia.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.cjq.aijia.R;
import com.cjq.aijia.entity.EventJumpIndex;
import com.cjq.aijia.util.JsInterface;
import com.cjq.aijia.util.SaveTool;
import com.ypy.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CommonWebViewActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.common_title)
    TextView title;
    @InjectView(R.id.common_web)
    WebView web;
    @InjectView(R.id.common_close)
    View close;
    @InjectView(R.id.common_refresh)
    SwipeRefreshLayout refreshLayout;

    public static final String EXTRA_URL = "url";
    public static final String EXTRA_TITLE = "title";
    private String url;
    private boolean flag;

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_webview);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        String urlS = intent.getStringExtra(EXTRA_URL);
        final String titleText = intent.getStringExtra(EXTRA_TITLE);
//        refreshLayout.setEnabled(true);
        dealURL(urlS);

        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        web.addJavascriptInterface(new JsInterface() {
            @JavascriptInterface
            public void login_request() {
                SaveTool.clear(CommonWebViewActivity.this);
                EventBus.getDefault().post(new EventJumpIndex().setNum(3));
                finish();
            }
        }, "app");
        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                if ("".equals(titleText)) {
                    CommonWebViewActivity.this.title.setText(title);
                }
                super.onReceivedTitle(view, title);
            }
        });

        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (refreshLayout.isRefreshing())
                    refreshLayout.setRefreshing(false);
//                if(flag){
//                    refreshLayout.setVisibility(View.GONE);
//                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                refreshLayout.setVisibility(View.VISIBLE);
//                flag=false;
                view.stopLoading();
                view.loadUrl("file:///android_asset/net_err_hint.html");
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                view.stopLoading();
                view.loadUrl("file:///android_asset/net_err_hint.html");
                super.onReceivedHttpError(view, request, errorResponse);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                view.stopLoading();
                view.loadUrl("file:///android_asset/net_err_hint.html");
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                view.stopLoading();
                view.loadUrl("file:///android_asset/net_err_hint.html");
                super.onReceivedError(view, request, error);
            }
        });

        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.theme_color), getResources().getColor(R.color.colorAccent));
        refreshLayout.setOnRefreshListener(this);

        title.setText(titleText);

        web.loadUrl(url);
        close.setOnClickListener(this);
    }

    private final void dealURL(String urlS) {
        try {
            if (urlS.contains("?")) {
                if (!urlS.contains("key")) {
                    url = urlS + "&key=" + SaveTool.getKey(this);
                } else {
                    url = urlS.replaceFirst("key=\\w+&", "key=" + SaveTool.getKey(this) + "&");
                }
            } else {
                url = urlS + "?key=" + SaveTool.getKey(this);
            }
        } catch (Exception e) {
            url = urlS;
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_close:
                finish();
                break;
        }
    }

    @Override
    public final void onRefresh() {
//        dealURL(url);
//        web.loadUrl(url);
        web.reload();
//        flag=true;
    }

    public final static void startCommonWeb(Context context, String title, String url) {
        Intent intent = new Intent(context, CommonWebViewActivity.class);
        intent.putExtra(CommonWebViewActivity.EXTRA_TITLE, title);
        intent.putExtra(CommonWebViewActivity.EXTRA_URL, url);
        context.startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (web.canGoBack()) {
                web.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
