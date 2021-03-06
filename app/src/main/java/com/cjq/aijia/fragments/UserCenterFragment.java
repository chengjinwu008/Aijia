package com.cjq.aijia.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cjq.aijia.CommonData;
import com.cjq.aijia.R;
import com.cjq.aijia.activity.CommonWebViewActivity;
import com.cjq.aijia.activity.SettingActivity;
import com.cjq.aijia.domain.User;
import com.cjq.aijia.entity.EventLogin;
import com.cjq.aijia.entity.UserInfo;
import com.cjq.aijia.util.SaveTool;
import com.cjq.aijia.util.UserInfoDealer;
import com.cjq.aijia.util.WebUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.ypy.eventbus.EventBus;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class UserCenterFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

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
    @InjectView(R.id.user_center_username)
    TextView userName;
    @InjectView(R.id.user_center_points)
    TextView userPoints;
    @InjectView(R.id.user_center_refresh)
    SwipeRefreshLayout refreshLayout;
    @InjectView(R.id.user_center_my_orders)
    View allOrders;
    @InjectView(R.id.user_center_waiting_payment)
    View waitingPayment;
    @InjectView(R.id.user_center_waiting_sending)
    View waitingSending;
    @InjectView(R.id.user_center_waiting_receiving)
    View waitingReceiving;
    @InjectView(R.id.user_center_charge_online)
    View chargeOnline;
    @InjectView(R.id.user_center_my_charge_history)
    View chargeHistory;
    @InjectView(R.id.user_center_done)
    View done;
    @InjectView(R.id.user_center_my_tracks)
    View myTracks;
    @InjectView(R.id.user_center_after_sell)
    View afterSell;

    private String mobile;
    private boolean isFetching=false;

    public void onEventMainThread(EventLogin e) {
        requestUserInfo();
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_center, container, false);
        ButterKnife.inject(this, view);

        EventBus.getDefault().register(this);

        pointsShoppingMall.setOnClickListener(this);
        interactiveArea.setOnClickListener(this);
        myCollection.setOnClickListener(this);
        config.setOnClickListener(this);
        allOrders.setOnClickListener(this);
        waitingPayment.setOnClickListener(this);
        waitingSending.setOnClickListener(this);
        waitingReceiving.setOnClickListener(this);
        done.setOnClickListener(this);
        myTracks.setOnClickListener(this);
        chargeHistory.setOnClickListener(this);
        chargeOnline.setOnClickListener(this);
        afterSell.setOnClickListener(this);

        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.theme_color), getResources().getColor(R.color.colorAccent));
        refreshLayout.setOnRefreshListener(this);
        requestUserInfo();
        return view;
    }

    private User getUserInfoLocal() {
        List<User> users = null;
        try {
            users = User.find(User.class, "user_id = ?", SaveTool.getUserId(getActivity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        final User user = users != null && users.size() != 0 ? users.get(0) : null;

        if (user != null)
            dealUserInfo(user);
        return user;
    }

    private void requestUserInfo() {
        getUserInfoOnline(getUserInfoLocal());
    }

    private void getUserInfoOnline(final User user) {
        try {
            WebUtil.requestUserInfo(getActivity(), new UserInfoDealer() {
                @Override
                public void dealWithInfo(UserInfo info) {
                    dealUserInfo(info);
                    if (user != null) {
                        user.setUserName(info.getUserName());
                        user.setMobile(info.getPhoneNumber());
                        user.setPoints(info.getUserPoints());
                        user.setProfile(info.getUserAvatar());
                        user.save();
                    } else {
                        try {
                            User userN = new User(info.getUserName(), info.getPhoneNumber(), info.getUserPoints(), info.getUserAvatar());
                            userN.setUserId(SaveTool.getUserId(getActivity()));
                            userN.save();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dealUserInfo(UserInfo info) {
        ImageLoader.getInstance().displayImage(info.getUserAvatar(), profile);
        userName.setText(info.getUserName());
        userPoints.setText("积分：" + info.getUserPoints() + "积分");
        mobile = info.getPhoneNumber();
        if (refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
    }

    private void dealUserInfo(User info) {
        if(!ImageLoader.getInstance().isInited()){
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        }
        ImageLoader.getInstance().displayImage(info.getProfile(), profile);
        userName.setText(info.getUserName());
        userPoints.setText("积分：" + info.getPoints() + "积分");
        mobile = info.getMobile();
        if (refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_center_points_shopping_mall:
                CommonWebViewActivity.startCommonWeb(getActivity(),"我的代金券", CommonData.MY_VOUCHER);
                break;
            case R.id.user_center_interactive_area:
                CommonWebViewActivity.startCommonWeb(getActivity(),"互动社区", CommonData.INTERACTIVE_AREA_URL);
                break;
            case R.id.user_center_my_collection:
                CommonWebViewActivity.startCommonWeb(getActivity(),"我的收藏", CommonData.MY_COLLECTION_URL);
                break;
            case R.id.user_center_my_orders:
                CommonWebViewActivity.startCommonWeb(getActivity(),"订单", CommonData.ALL_ORDER_LIST);
                break;
            case R.id.user_center_waiting_payment:
                CommonWebViewActivity.startCommonWeb(getActivity(),"订单", CommonData.ORDER_WAITING_PAYMENT);
                break;
            case R.id.user_center_waiting_sending:
                CommonWebViewActivity.startCommonWeb(getActivity(),"订单", CommonData.ORDER_WAITING_SENDING);
                break;
            case R.id.user_center_waiting_receiving:
                CommonWebViewActivity.startCommonWeb(getActivity(),"订单", CommonData.ORDER_WAITING_RECEIVING);
                break;
            case R.id.user_center_done:
                CommonWebViewActivity.startCommonWeb(getActivity(),"订单", CommonData.ORDER_DONE);
                break;
            case R.id.user_center_my_tracks:
                CommonWebViewActivity.startCommonWeb(getActivity(),"我的足迹", CommonData.HISTORY);
                break;
            case R.id.user_center_my_charge_history:
                CommonWebViewActivity.startCommonWeb(getActivity(),"充值记录",CommonData.CHARGE_HISTORY);
                break;
            case R.id.user_center_charge_online:
                CommonWebViewActivity.startCommonWeb(getActivity(),"在线充值",CommonData.CHARGE_ONLINE);
                break;
            case R.id.user_center_after_sell:
                CommonWebViewActivity.startCommonWeb(getActivity(),"我的售后",CommonData.AFTER_SELL);
                break;
            case R.id.user_center_config:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        try {
            requestUserInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
