package com.cjq.aijia.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjq.aijia.R;
import com.cjq.aijia.entity.EventMainRefresh;
import com.ypy.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by CJQ on 2015/11/16.
 */
public class NoNetworkFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.no_net_refresh)
    SwipeRefreshLayout refreshLayout;
    Handler handler = new Handler();

    private static Fragment INSTANCE;

    public static Fragment getInstance() {
        if (INSTANCE == null)
            INSTANCE = new NoNetworkFragment();
        return INSTANCE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.no_network, container, false);
        ButterKnife.inject(this, view);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.theme_color), getResources().getColor(R.color.colorAccent));
        return view;
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
                EventBus.getDefault().post(new EventMainRefresh());
            }
        }, 500);
    }
}
