package com.ums.wifiprobe.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.ums.wifiprobe.app.WPApplication;
import com.ums.wifiprobe.data.BrandTotalDataRepository;
import com.ums.wifiprobe.data.ProbeInfoDataRepository;
import com.ums.wifiprobe.data.ProbeTotalDataRepository;
import com.ums.wifiprobe.service.BaseProbeDataManager;
import com.ums.wifiprobe.service.ProbeServiceConstant;
import com.ums.wifiprobe.ui.customview.OnDialogCloseListener;
import com.ums.wifiprobe.utils.ClearDataUtils;
import com.ums.wifiprobe.utils.DialogUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by chenzhy on 2017/9/21.
 */

public abstract class NewBaseActivity extends AppCompatActivity implements WPApplication.LocationChangedListener {

    protected static final String TAG = "WiFiProbe-Activity";
    protected Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(getLayoutId());
//        Intent intent = new Intent(this, ProbeService.class);
//        startService(intent);
        unbinder = ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public abstract void initView();

    public abstract void initData();

    public abstract int getLayoutId();

    @Override
    protected void onPause() {
        super.onPause();
        WPApplication.setLocationListener(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        WPApplication.setLocationListener(this);
    }

    public abstract void CacheClearComplete();

    @Override
    public void onLocationChanged(final double distance) {
        Log.d(TAG,"两次之间的距离=" + distance );

        if (distance > ProbeServiceConstant.locationDistance || distance < -1 * ProbeServiceConstant.locationDistance) {
            DialogUtil.showDialog(NewBaseActivity.this, "历史数据清除", "距上次探测距离已超过" + ProbeServiceConstant.locationDistance + "米，是否清除上次数据？", new OnDialogCloseListener() {
                @Override
                public void onClick(Dialog dialog, boolean confirm) {
                    if (confirm) {

                        synchronized (WPApplication.gDaoSession) {
                            new ClearDataUtils(NewBaseActivity.this).clearDatabases();
                            BaseProbeDataManager.localMap.clear();
                            ProbeInfoDataRepository.getInstance().clearTasks();
                            ProbeTotalDataRepository.getInstance().clearTasks();
                            BrandTotalDataRepository.getInstance().clearTasks();
                            WPApplication.initDataBase();
                            sendBroadcast(new Intent(ProbeServiceConstant.RECEIVER_ACTION_CLEAR_QUEEN_DATA));
                            CacheClearComplete();
                        }
                        Toast.makeText(NewBaseActivity.this, "历史数据清除成功", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
