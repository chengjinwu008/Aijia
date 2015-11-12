package com.cjq.aijia;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.cjq.aijia.entity.BottomButton;
import com.cjq.aijia.service.CheckService;
import com.cjq.aijia.view.BottomBar;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements BottomBar.OnButtonCheckedListener {
    @InjectView(R.id.main_bottom_bar)
    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        Intent intent = new Intent(this, CheckService.class);

        if(savedInstanceState==null)
        {
            bindService(intent, new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {

                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            },BIND_ADJUST_WITH_ACTIVITY);
        }

        bottomBar.addButton(new BottomButton(R.drawable.shouye_dianji, R.drawable.shouye_weidianji, R.color.pure_white, R.color.pure_white, null));
        bottomBar.addButton(new BottomButton(R.drawable.feilei_dianji, R.drawable.feilei_weidianji, R.color.pure_white, R.color.pure_white, null));
        bottomBar.addButton(new BottomButton(R.drawable.gouwuche_dianji, R.drawable.gouwuche_weidianji, R.color.pure_white, R.color.pure_white, null));
        bottomBar.addButton(new BottomButton(R.drawable.wode_dianji,R.drawable.wode_weidianji,R.color.pure_white,R.color.pure_white,null));

        bottomBar.setCheckedListener(this);
    }

    @Override
    public void onButtonChecked(int No) {

    }

    @Override
    public void onButtonCheckedNoChange(int No) {
        //处理不同的点击
        switch (No){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
