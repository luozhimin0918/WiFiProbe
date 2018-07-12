package com.ums.wifiprobe.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ums.wifiprobe.AppProtocolInfo;
import com.ums.wifiprobe.R;
import com.ums.wifiprobe.app.DataBaseInitWorkTask;
import com.ums.wifiprobe.app.GlobalValueManager;
import com.ums.wifiprobe.ui.customview.ControlScrollViewPager;
import com.ums.wifiprobe.ui.fragment.PassengerFlowDataFragment;
import com.ums.wifiprobe.ui.fragment.PassengerFlowTraFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by luozhimin on 2018/6/29.
 */

public class NewCustomerInfTaActivity extends NewBaseActivity implements View.OnClickListener,OnTabSelectListener {


    @BindView(R.id.head_setting)
    ImageView headSetting;
    @BindView(R.id.h5_app_name)
    TextView h5AppName;
    @BindView(R.id.head_advanced)
    LinearLayout headAdvanced;
    @BindView(R.id.head_part)
    LinearLayout headPart;
    @BindView(R.id.tab_main)
    SlidingTabLayout tabMain;
    @BindView(R.id.view_pager)
    ControlScrollViewPager viewPager;
    private List<Fragment> list_fragment;                                //定义要装fragment的列表
    private List<String> list_title;                                     //tab名称列表
    private PassengerFlowTraFragment passengerFlowTraFragment;
    private PassengerFlowDataFragment passengerFlowDataFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!GlobalValueManager.getInstance().isAgreeProtocol()) {
            Intent intent = new Intent(getBaseContext(), SplashActivity.class);
            Bundle bundle = new Bundle();
            try {
                bundle.putSerializable("appinfo", new AppProtocolInfo(getString(R.string.app_name), getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName, "protocol.txt",R.mipmap.appicon));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void initView() {
        passengerFlowTraFragment=new PassengerFlowTraFragment();
        passengerFlowDataFragment=new PassengerFlowDataFragment();
        list_fragment=new ArrayList<Fragment>();
        list_fragment.add(passengerFlowDataFragment);

        list_fragment.add(passengerFlowTraFragment);
        //tab title List
        list_title=new ArrayList<String>();
        list_title.add("  客流数据  ");
        list_title.add("客流交易数据");
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabMain.setViewPager(viewPager);
        tabMain.setOnTabSelectListener(this);
        viewPager.setCurrentItem(0);


    }

    @Override
    public void initData() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_customer_main;
    }

    @Override
    public void CacheClearComplete() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_setting:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
        }

    }

    @OnClick({R.id.head_setting, R.id.head_advanced})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_setting:
                break;
            case R.id.head_advanced:
                break;
        }
    }

    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public void onTabReselect(int position) {

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return list_fragment.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return list_title.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return list_fragment.get(position);
        }
    }



}
