package com.cjq.aijia.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cjq.aijia.CommonData;
import com.cjq.aijia.R;
import com.cjq.aijia.adapter.SettingAdapter;
import com.cjq.aijia.entity.EventJumpIndex;
import com.cjq.aijia.entity.SettingItem;
import com.cjq.aijia.util.WebUtil;
import com.ypy.eventbus.EventBus;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SettingActivity extends AppCompatActivity implements SettingAdapter.ItemClickedInterface, View.OnClickListener {

    @InjectView(R.id.setting_recycler)
    RecyclerView recyclerView;
    @InjectView(R.id.setting_close)
    View close;

    private int versionCode;
    private String versionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        try {
            PackageInfo info =  getPackageManager().getPackageInfo(getPackageName(),0);

            versionCode =  info.versionCode;
            versionName = info.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        ButterKnife.inject(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<SettingItem> menuItems = new ArrayList<>();
        menuItems.add(new SettingItem("地址管理","",true));
        menuItems.add(new SettingItem("会员卡","",true));
//        menuItems.add(new SettingItem("优惠券","",true));
        menuItems.add(new SettingItem("修改密码","",true));
        menuItems.add(new SettingItem("当前版本",versionName));
        menuItems.add(new SettingItem(SettingItem.TYPE.BUTTON,"退出登录"));

        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setColor(getResources().getColor(R.color.common_background));
        paint.setAntiAlias(true);

        recyclerView.setAdapter(new SettingAdapter(menuItems).setItemClickedInterface(this));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).paint(paint).build());
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        close.setOnClickListener(this);
    }

    @Override
    public void onItemClicked(int position, Object entity, View childView) {
        switch (position){
            case 5:
                try {
                    WebUtil.requestLogout(this, new Runnable() {
                        @Override
                        public void run() {
                            EventBus.getDefault().post(new EventJumpIndex().setNum(0));
                            finish();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 0:
                CommonWebViewActivity.startCommonWeb(this,"收货地址", CommonData.ADDRESS_LIST_URL);
                break;
            case 1:
                CommonWebViewActivity.startCommonWeb(this,"会员卡", CommonData.MEMBER_CARD);
                break;
            case 2:
                String mobileNumber = getIntent().getStringExtra("mobile");
                if(mobileNumber!=null && !"".equals(mobileNumber)){
                    Intent intent = new Intent(this,FindPasswordActivity.class);
                    intent.putExtra("mobile",mobileNumber);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_close:
                finish();
                break;
        }
    }
}
