package com.cjq.aijia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.cjq.aijia.entity.BottomButton;
import com.cjq.aijia.fragments.UserCenterFragment;
import com.cjq.aijia.fragments.WebViewFragment;
import com.cjq.aijia.service.CheckService;
import com.cjq.aijia.view.BottomBar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.Bmob;

public class MainActivity extends BaseActivity implements BottomBar.OnButtonCheckedListener {
    @InjectView(R.id.main_bottom_bar)
    BottomBar bottomBar;
    private Intent intent;
    private Fragment uc_fragment;
    private FragmentManager manager;
    private Fragment web_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        intent = new Intent(this, CheckService.class);

        if(savedInstanceState==null)
        {
            Bmob.initialize(this, "6e5ad2c79bb81e156aec8a5c38a09f05");
            startService(intent);
        }

        bottomBar.addButton(new BottomButton(R.drawable.shouye_dianji, R.drawable.shouye_weidianji, R.color.pure_white, R.color.pure_white, null));
        bottomBar.addButton(new BottomButton(R.drawable.feilei_dianji, R.drawable.feilei_weidianji, R.color.pure_white, R.color.pure_white, null));
        bottomBar.addButton(new BottomButton(R.drawable.gouwuche_dianji, R.drawable.gouwuche_weidianji, R.color.pure_white, R.color.pure_white, null));
        bottomBar.addButton(new BottomButton(R.drawable.wode_dianji,R.drawable.wode_weidianji,R.color.pure_white,R.color.pure_white,null));

        bottomBar.setCheckedListener(this);

        uc_fragment = UserCenterFragment.getInstance();
        web_fragment = WebViewFragment.getInstance();
        manager  = getSupportFragmentManager();

        manager.beginTransaction().add(R.id.main_content,web_fragment).commit();
    }

    @Override
    public void onButtonChecked(int No) {

    }

    @Override
    public void onButtonCheckedNoChange(int No) {
        //处理不同的点击
        switch (No){
            case 3:
                //跳转个人中心
                FragmentTransaction transaction = manager.beginTransaction();
                if(!uc_fragment.isAdded()) {
                    transaction.add(R.id.main_content,uc_fragment);
                }

                if(web_fragment.isAdded() && !web_fragment.isHidden())
                    transaction.hide(web_fragment);

                transaction.show(uc_fragment).commit();

                break;
            default:
                FragmentTransaction trans = manager.beginTransaction();
                if(!web_fragment.isAdded()) {
                    trans.add(R.id.main_content,web_fragment);
                }

                if(uc_fragment.isAdded() && !uc_fragment.isHidden())
                    trans.hide(uc_fragment);

                trans.show(web_fragment).commit();

                dealWebView(No);
                break;
        }
    }

    //对webview分类处理
    private void dealWebView(int no) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        stopService(intent);
        super.onDestroy();
    }
}
