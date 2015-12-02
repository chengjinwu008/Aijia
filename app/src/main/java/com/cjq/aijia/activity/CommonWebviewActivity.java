package com.cjq.aijia.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.cjq.aijia.R;
import com.cjq.aijia.util.SaveTool;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_webview);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        String urlS = intent.getStringExtra(EXTRA_URL);
        String titleText = intent.getStringExtra(EXTRA_TITLE);

        dealURL(urlS);

        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        web.setWebChromeClient(new WebChromeClient() {

        });

        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (refreshLayout.isRefreshing())
                    refreshLayout.setRefreshing(false);
            }
        });

        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.theme_color), getResources().getColor(R.color.colorAccent));
        refreshLayout.setOnRefreshListener(this);

        title.setText(titleText);

        web.loadUrl(url);
        close.setOnClickListener(this);
    }

    private void dealURL(String urlS) {
        try {
            if(urlS.contains("?")){
                if(!urlS.contains("key")){
                    url = urlS+"&key="+SaveTool.getKey(this);
                }else{
                    url= urlS.replaceFirst("key=\\w+&","key="+SaveTool.getKey(this)+"&");
                }
            }else{
                url = urlS+"?key="+SaveTool.getKey(this);
            }
        } catch (Exception e) {
            url = urlS;
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.common_close:
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
        dealURL(url);
        web.loadUrl(url);
    }

    public static void startCommonWeb(Context context, String title, String url) {
        Intent intent = new Intent(context, CommonWebViewActivity.class);
        intent.putExtra(CommonWebViewActivity.EXTRA_TITLE, title);
        intent.putExtra(CommonWebViewActivity.EXTRA_URL, url);
        context.startActivity(intent);
    }
}
