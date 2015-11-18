package com.cjq.aijia.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.cjq.aijia.CommonData;
import com.cjq.aijia.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by CJQ on 2015/11/12.
 */
public class WebViewFragment extends Fragment{

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
    String url=CommonData.INDEX_URL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web,container,false);
        ButterKnife.inject(this, view);
        webView.loadUrl(url);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.theme_color),getResources().getColor(R.color.colorAccent));
        return view;
    }
}
