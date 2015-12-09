package com.cjq.aijia.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cjq.aijia.CommonData;
import com.cjq.aijia.R;
import com.cjq.aijia.entity.EventJumpIndex;
import com.cjq.aijia.entity.EventNoNetChange;
import com.cjq.aijia.entity.EventWebChange;
import com.cjq.aijia.entity.EventWebRefresh;
import com.cjq.aijia.entity.EventWebViewBackgroundRefresh;
import com.cjq.aijia.util.LoginInterface;
import com.cjq.aijia.util.SaveTool;
import com.ypy.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by CJQ on 2015/11/12.
 */
public class WebViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static Fragment INSTANCE;
    private boolean loadBackground;
    private String url_origin=null;

    public static Fragment getInstance() {
        if (INSTANCE == null)
            INSTANCE = new WebViewFragment();
        return INSTANCE;
    }

    @InjectView(R.id.main_web)
    WebView webView;
    @InjectView(R.id.web_refresh)
    SwipeRefreshLayout refreshLayout;
    String url = CommonData.INDEX_URL;

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web, container, false);
        EventBus.getDefault().register(this);
        ButterKnife.inject(this, view);
        dealURL(url);
        webView.loadUrl(url);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new LoginInterface() {
            @Override
            public void login_request() {
                SaveTool.clear(getActivity());
                EventBus.getDefault().post(new EventJumpIndex().setNum(3));
            }
        }, "app");
        webView.setWebChromeClient(new WebChromeClient() {

        });

        webView.setWebViewClient(new WebViewClient() {
                                     @Override
                                     public void onPageFinished(WebView view, String url) {
                                         super.onPageFinished(view, url);
                                         if (refreshLayout.isRefreshing())
                                             refreshLayout.setRefreshing(false);
                                         if(loadBackground){
                                             loadBackground =false;
                                             EventBus.getDefault().post(new EventWebRefresh());
                                         }
                                     }

                                     @Override
                                     public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                                         super.onReceivedError(view, request, error);
                                         EventBus.getDefault().post(new EventNoNetChange());
                                     }

                                     @Override
                                     public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                                         super.onReceivedHttpError(view, request, errorResponse);
                                         EventBus.getDefault().post(new EventNoNetChange());
                                     }

                                     @Override
                                     public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                                         super.onReceivedHttpAuthRequest(view, handler, host, realm);
                                     }

                                     @Override
                                     public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                         super.onReceivedError(view, errorCode, description, failingUrl);
                                         if(!loadBackground)
                                         EventBus.getDefault().post(new EventNoNetChange());
                                         else{
                                             loadBackground =false;
                                             EventBus.getDefault().post(new EventNoNetChange());
                                         }

                                     }

                                     @Override
                                     public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                         super.onPageStarted(view, url, favicon);
                                     }
                                 }
        );

        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.theme_color), getResources().getColor(R.color.colorAccent));
        refreshLayout.setOnRefreshListener(this);
        return view;
    }

    public void onEventMainThread(EventWebChange webChange) {
        if(!webChange.getUrl().equals(url_origin)){
            url_origin = webChange.getUrl();
            dealURL(webChange.getUrl());
            webView.stopLoading();
            webView.loadUrl(url);
        }
    }

    public void onEventMainThread(EventWebViewBackgroundRefresh e) {
        if(!loadBackground){
            url_origin = e.getUrl();
            dealURL(e.getUrl());
            loadBackground = true;
            webView.loadUrl(url);
        }else{
            if(!e.getUrl().equals(url_origin)){
                url_origin = e.getUrl();
                dealURL(e.getUrl());
                webView.stopLoading();
                webView.loadUrl(url);
            }
        }
    }

    @Override
    public void onRefresh() {
        dealURL(url);
        webView.loadUrl(url);
    }

    private final void dealURL(String urlS) {
        try {
            if (urlS.contains("?")) {
                if (!urlS.contains("key")) {
                    url = urlS + "&key=" + SaveTool.getKey(getActivity());
                } else {
                    url = urlS.replaceFirst("key=\\w+&", "key=" + SaveTool.getKey(getActivity()) + "&");
                }
            } else {
                url = urlS + "?key=" + SaveTool.getKey(getActivity());
            }
        } catch (Exception e) {
            url = urlS;
            e.printStackTrace();
        }
    }
}
