package com.cjq.aijia.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    @InjectView(R.id.main_web)
    WebView webView;

    private static WebViewFragment INSTANCE;

    public static WebViewFragment getInstance(){
        if(INSTANCE ==null)
            INSTANCE = new WebViewFragment();
        return INSTANCE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web,container,false);
        ButterKnife.inject(this,view);
        webView.loadUrl(CommonData.INDEX_URL);
        return view;
    }
}
