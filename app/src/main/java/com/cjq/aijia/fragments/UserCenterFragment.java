package com.cjq.aijia.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cjq.aijia.CommonData;
import com.cjq.aijia.R;
import com.cjq.aijia.activity.CommonWebViewActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class UserCenterFragment extends Fragment implements View.OnClickListener {

    private static Fragment INSTANCE;

    public static Fragment getInstance() {
        if (INSTANCE == null)
            INSTANCE = new UserCenterFragment();
        return INSTANCE;
    }

    @InjectView(R.id.user_center_config)
    View config;
    @InjectView(R.id.user_center_profile)
    ImageView profile;
    @InjectView(R.id.user_center_points_shopping_mall)
    View pointsShoppingMall;
    @InjectView(R.id.user_center_interactive_area)
    View interactiveArea;
    @InjectView(R.id.user_center_my_collection)
    View myCollection;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_center,container,false);
        ButterKnife.inject(this,view);

        pointsShoppingMall.setOnClickListener(this);
        interactiveArea.setOnClickListener(this);
        myCollection.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_center_points_shopping_mall:
                startCommonWeb("积分商城",CommonData.POINT_SHOPPING_MALL_URL);
                break;
            case R.id.user_center_interactive_area:
                startCommonWeb("互动社区",CommonData.INTERACTIVE_AREA_URL);
                break;
            case R.id.user_center_my_collection:
                startCommonWeb("我的收藏",CommonData.MY_COLLECTION_URL);
                break;
        }
    }

    private void startCommonWeb(String title,String url) {
        Intent intent = new Intent(getActivity(), CommonWebViewActivity.class);
        intent.putExtra(CommonWebViewActivity.EXTRA_TITLE,title);
        intent.putExtra(CommonWebViewActivity.EXTRA_URL,url);
        startActivity(intent);
    }
}
