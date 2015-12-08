package com.cjq.aijia;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.cjq.aijia.activity.LogoActivity;
import com.cjq.aijia.entity.BottomButton;
import com.cjq.aijia.entity.EventJumpIndex;
import com.cjq.aijia.entity.EventLogin;
import com.cjq.aijia.entity.EventMainRefresh;
import com.cjq.aijia.entity.EventNoNetChange;
import com.cjq.aijia.entity.EventWebChange;
import com.cjq.aijia.entity.EventWebRefresh;
import com.cjq.aijia.entity.EventWebViewBackgroundRefresh;
import com.cjq.aijia.fragments.LoginFragment;
import com.cjq.aijia.fragments.NoNetworkFragment;
import com.cjq.aijia.fragments.UserCenterFragment;
import com.cjq.aijia.fragments.WebViewFragment;
import com.cjq.aijia.reveiver.NetworkReceiver;
import com.cjq.aijia.service.CheckService;
import com.cjq.aijia.util.SaveTool;
import com.cjq.aijia.util.WebUtil;
import com.cjq.aijia.view.BottomBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.ypy.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.Bmob;

public class MainActivity extends BaseActivity implements BottomBar.OnButtonCheckedListener {
    @InjectView(R.id.main_bottom_bar)
    BottomBar bottomBar;
    private Intent intent;

    private Map<String, Fragment> fragments = new HashMap<>();

    private FragmentManager manager;
    private NetworkReceiver receiver;
    private String now_no_key = "web";
    private int now_no=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, LogoActivity.class));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        intent = new Intent(this, CheckService.class);
        receiver = new NetworkReceiver();

        if (savedInstanceState == null) {
            Bmob.initialize(this, "6e5ad2c79bb81e156aec8a5c38a09f05");
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
            startService(intent);
            //注册网络监控监听
            IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            registerReceiver(receiver, filter);
        }

        bottomBar.addButton(new BottomButton(R.drawable.shouye_dianji, R.drawable.shouye_weidianji, R.color.pure_white, R.color.pure_white, null));
        bottomBar.addButton(new BottomButton(R.drawable.feilei_dianji, R.drawable.feilei_weidianji, R.color.pure_white, R.color.pure_white, null));
        bottomBar.addButton(new BottomButton(R.drawable.gouwuche_dianji, R.drawable.gouwuche_weidianji, R.color.pure_white, R.color.pure_white, null));
        bottomBar.addButton(new BottomButton(R.drawable.wode_dianji, R.drawable.wode_weidianji, R.color.pure_white, R.color.pure_white, null));

        bottomBar.setCheckedListener(this);
        manager = getSupportFragmentManager();

        fragments.put("web", WebViewFragment.getInstance());
        fragments.put("uc", UserCenterFragment.getInstance());
        fragments.put("login", LoginFragment.getInstance());
        fragments.put("no_net", NoNetworkFragment.getInstance());

        changeFragment("web");
        bottomBar.changeColor(0);
    }

    @Override
    public void onButtonChecked(int No) {

    }

    @Override
    public void onButtonCheckedChanged(int No) {
        now_no=No;
        //处理不同的点击
        switch (No) {
            case 3:
                //跳转个人中心
                if (WebUtil.checkNetWork((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE))) {
                    try {
                        SaveTool.getUserId(this);
                        changeFragment("uc");
                    } catch (Exception e) {
                        e.printStackTrace();
                        changeFragment("login");
                    }
                } else {
                    changeFragment("no_net");
                }
                bottomBar.changeColor(No);
                break;
            default:
                if (WebUtil.checkNetWork((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE))) {
                    dealWebView(No);
                } else {
                    changeFragment("no_net");
                    bottomBar.changeColor(now_no);
                }
                break;
        }
    }

    private void changeFragment(String key) {
        now_no_key = key;
        FragmentTransaction transaction = manager.beginTransaction();
        if (!fragments.get(key).isAdded()) {
            transaction.add(R.id.main_content, fragments.get(key));
        }

        Set<String> keys = fragments.keySet();
        for (String s : keys) {
            if (!key.equals(s)) {
                Fragment temp = fragments.get(s);
                if (temp.isAdded() && !temp.isHidden())
                    transaction.hide(temp);
            }
        }
        if (fragments.get(key).isHidden())
            transaction.show(fragments.get(key));

        transaction.commitAllowingStateLoss();
    }

    //对webview分类处理
    private void dealWebView(int no) {
        if (no == 2)
            try {
                SaveTool.getUserId(this);
                if ("web".equals(now_no_key)) {
                    changeFragment("web");
                    bottomBar.changeColor(no);
                    EventBus.getDefault().post(new EventWebChange(no));
                } else
                    EventBus.getDefault().post(new EventWebViewBackgroundRefresh(no));
            } catch (Exception e) {
                bottomBar.changeColor(3);
                changeFragment("login");
            }
        else {
            if ("web".equals(now_no_key)) {
                changeFragment("web");
                bottomBar.changeColor(no);
                EventBus.getDefault().post(new EventWebChange(no));
            } else
                EventBus.getDefault().post(new EventWebViewBackgroundRefresh(no));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        stopService(intent);
        super.onDestroy();
    }

    public void onEventMainThread(EventMainRefresh e) {
        if (bottomBar.getButtonActivated() == 3)
            onButtonCheckedChanged(bottomBar.getButtonActivated());
        else
            EventBus.getDefault().post(new EventWebViewBackgroundRefresh(bottomBar.getButtonActivated()));
    }

    public void onEventMainThread(EventJumpIndex e) {
        onButtonCheckedChanged(e.getNum());
        bottomBar.changeColor(e.getNum());
    }

    public void onEventMainThread(EventNoNetChange e) {
        if(now_no!=3){
            changeFragment("no_net");
            bottomBar.changeColor(now_no);
        }
    }

    public void onEventMainThread(EventLogin e) {
        if (bottomBar.getButtonActivated() == 3) {
            onButtonCheckedChanged(bottomBar.getButtonActivated());
        }
    }

    public void onEventMainThread(EventWebRefresh e) {
        changeFragment("web");
        bottomBar.changeColor(now_no);
    }
}
