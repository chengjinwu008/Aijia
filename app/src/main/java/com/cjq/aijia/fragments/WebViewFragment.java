package com.cjq.aijia.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cjq.aijia.CommonData;
import com.cjq.aijia.R;
import com.cjq.aijia.entity.EventWebChange;
import com.cjq.aijia.util.SaveTool;
import com.ypy.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by CJQ on 2015/11/12.
 */
public class WebViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static Fragment INSTANCE;

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
        webView.setWebChromeClient(new WebChromeClient() {

        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (refreshLayout.isRefreshing())
                    refreshLayout.setRefreshing(false);
            }
        });

        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.theme_color), getResources().getColor(R.color.colorAccent));
        refreshLayout.setOnRefreshListener(this);
        return view;
    }

    public void onEventMainThread(EventWebChange webChange) {
        dealURL(webChange.getUrl());
        webView.loadUrl(url);
    }

    @Override
    public void onRefresh() {
        dealURL(url);
        webView.loadUrl(url);
    }

    private void dealURL(String urlS) {
        try {
            url = urlS + SaveTool.getKey(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
