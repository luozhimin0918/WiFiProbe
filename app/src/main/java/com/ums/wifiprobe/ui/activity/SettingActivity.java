package com.ums.wifiprobe.ui.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.ums.shdep.reward.RewardActivity;
import com.ums.shdep.reward.RewardActivity;
import com.ums.wifiprobe.R;
import com.ums.wifiprobe.app.GlobalValueManager;
import com.ums.wifiprobe.app.WPApplication;
import com.ums.wifiprobe.data.BrandTotalDataRepository;
import com.ums.wifiprobe.data.ProbeInfoDataRepository;
import com.ums.wifiprobe.data.ProbeTotalDataRepository;
import com.ums.wifiprobe.service.BaseProbeDataManager;
import com.ums.wifiprobe.service.ProbeService;
import com.ums.wifiprobe.service.ProbeServiceConstant;
import com.ums.wifiprobe.ui.customview.AlertDialog;
import com.ums.wifiprobe.ui.customview.CommonDialog;
import com.ums.wifiprobe.ui.customview.OnDialogCloseListener;
import com.ums.wifiprobe.ui.customview.SwitchButton;
import com.ums.wifiprobe.utils.ClearDataUtils;
import com.ums.wifiprobe.utils.DialogUtil;

import butterknife.BindView;
import cn.bingoogolapple.badgeview.BGABadgeLinearLayout;

/**
 * Created by chenzhy on 2017/11/5.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.head_back)
    ImageButton btnBack;
    @BindView(R.id.setting_et_movedistance)
    EditText editTextDistance;
    @BindView(R.id.setting_btn_open)
    Button btnSave;
    @BindView(R.id.switch_button)
    SwitchButton switchButton;
    @BindView(R.id.relativelayout_cleardata)
    RelativeLayout layoutCleardata;
    @BindView(R.id.setting_tv_movedistance)
    TextView tvMoveDistance;
    @BindView(R.id.relativelayout_whitelist)
    RelativeLayout layoutWhitelist;
    @BindView(R.id.setting_tv_version)
    TextView tvVersion;

    private BGABadgeLinearLayout bgaBadgeLinearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
        bgaBadgeLinearLayout = (BGABadgeLinearLayout) findViewById(R.id.linearlayout_reward);
        bgaBadgeLinearLayout.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        layoutCleardata.setOnClickListener(this);
        tvMoveDistance.setOnClickListener(this);
        layoutWhitelist.setOnClickListener(this);
        editTextDistance.setText(ProbeServiceConstant.locationDistance + "");

        editTextDistance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        switchButton.setChecked(ProbeServiceConstant.probeIsOpened);
        switchButton.setShadowEffect(true);//disable shadow effect
        switchButton.setEnabled(true);//disable button
        switchButton.setEnableEffect(false);//disable the switch animation
        switchButton.setOnCheckedChangeListener(checkedChangeListener);
        try {
            tvVersion.setText(getPackageManager().getPackageInfo(this.getPackageName(),0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
        }


    }

    SwitchButton.OnCheckedChangeListener checkedChangeListener = new SwitchButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(SwitchButton view, boolean isChecked) {
            if (view.isPressed())
                return;
            if (isChecked) {
                String errorMsg = GlobalValueManager.getInstance().getWifiProbeErrorInfo();
                if (!errorMsg.equals("ok")) {
                    String title = errorMsg.contains("系统")?"系统版本低":"服务版本低";
                    DialogUtil.showAlertDialog(SettingActivity.this,title, GlobalValueManager.getInstance().getWifiProbeErrorInfo(), onOpenProbeCloseListener, "确定");
                } else {
                    showLoadingDialog();
                }
            } else {
                DialogUtil.showDialog(SettingActivity.this, "探测器开关", "关闭探测器后将无法继续采集客流数据！是否关闭？", onCloseProbeCloseListener);
            }
        }
    };

    //加超时判断
    private void showLoadingDialog(){
//        sendBroadcast(new Intent(ProbeServiceConstant.RECEIVER_ACTION_OPEN_PROBE));
        Intent intent = new Intent(this, ProbeService.class);
        intent.putExtra("openOrCloseProbe","open");
        startService(intent);
        isOpeningProbe = true;
        DialogUtil.showLoadingDialog(SettingActivity.this);
        mHandler.removeCallbacks(openTimeOutRunnable);
        mHandler.postDelayed(openTimeOutRunnable,1000);

    }

    boolean isOpeningProbe = false;
    Handler mHandler = new Handler();
    int times = 0;//10s超时
    Runnable openTimeOutRunnable = new Runnable() {
        @Override
        public void run() {
            if(times>=10){
                isOpeningProbe = false;
                times=0;
                switchButton.setOnCheckedChangeListener(null);
                switchButton.setChecked(false);
                switchButton.setOnCheckedChangeListener(checkedChangeListener);
                DialogUtil.hideLoadingDialog();
                Toast.makeText(SettingActivity.this,"客流探测器开启失败",Toast.LENGTH_LONG).show();
            }else{
            if(isOpeningProbe){
                times++;
                mHandler.postDelayed(this,1000);
            }}
        }
    };



    OnDialogCloseListener onOpenProbeCloseListener = new OnDialogCloseListener() {
        @Override
        public void onClick(Dialog dialog,boolean confirm) {
            switchButton.setChecked(false);


        }
    };
    OnDialogCloseListener onCloseProbeCloseListener = new OnDialogCloseListener() {
        @Override
        public void onClick(Dialog dialog, boolean confirm) {

            if (confirm) {
                Intent intent = new Intent(SettingActivity.this, ProbeService.class);
                intent.putExtra("openOrCloseProbe","close");
                startService(intent);
                DialogUtil.showLoadingDialog(SettingActivity.this);
            } else {
                switchButton.setOnCheckedChangeListener(null);
                switchButton.setChecked(true);
                switchButton.setOnCheckedChangeListener(checkedChangeListener);
            }
        }
    };

    @Override
    public void initData() {
        registerBoradcastReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(openTimeOutRunnable);
        unregisterReceiver(probeSwitchBroadReceiver);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void CacheClearComplete() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(GlobalValueManager.getInstance().isRewardOpened()){
            bgaBadgeLinearLayout.hiddenBadge();
        }else{
            bgaBadgeLinearLayout.showCirclePointBadge();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back:

                onBackPressed();
                break;
            case R.id.relativelayout_cleardata:
                DialogUtil.showDialog(SettingActivity.this, "清除历史数据", "清除后将无法查看历史客流！是否清除？", new OnDialogCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            synchronized (WPApplication.gDaoSession) {
                                new ClearDataUtils(SettingActivity.this).clearDatabases();
                                BaseProbeDataManager.localMap.clear();
                                ProbeInfoDataRepository.getInstance().clearTasks();
                                ProbeTotalDataRepository.getInstance().clearTasks();
                                BrandTotalDataRepository.getInstance().clearTasks();
                                WPApplication.initDataBase();
                                sendBroadcast(new Intent(ProbeServiceConstant.RECEIVER_ACTION_CLEAR_QUEEN_DATA));
                                CacheClearComplete();
                            }
                            Toast.makeText(SettingActivity.this, "历史数据清除成功", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                break;
            case R.id.setting_btn_open:
                if (TextUtils.isEmpty(editTextDistance.getText())){
                    Toast.makeText(SettingActivity.this, "距离不能为空！", Toast.LENGTH_SHORT).show();
                    editTextDistance.setText(ProbeServiceConstant.locationDistance + "");
                }else {
                    if (!TextUtils.isEmpty(editTextDistance.getText()) && isNumeric(editTextDistance.getText().toString())) {
                        GlobalValueManager.getInstance().setLocationDistance(Integer.valueOf(editTextDistance.getText().toString()));
                        ProbeServiceConstant.locationDistance = GlobalValueManager.getInstance().getLocationDistance();
                        Toast.makeText(SettingActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SettingActivity.this, "距离必须为米整数！", Toast.LENGTH_SHORT).show();
                        editTextDistance.setText(GlobalValueManager.getInstance().getLocationDistance());
                    }
                }



                break;

            case R.id.setting_tv_movedistance:
                DialogUtil.showAlertDialog(this,"设备重置距离", "默认为50米。表示该设备被移动到50米之外，将会提示是否清除历史数据", null, "确定");
                break;

            case R.id.relativelayout_whitelist:
                startActivity(new Intent(SettingActivity.this, WhiteListActivity.class));
                break;

            case R.id.linearlayout_reward:
                GlobalValueManager.getInstance().setRewardOpened(true);
                startActivity(new Intent(this, RewardActivity.class));
                break;
        }
    }


    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private class ProbeSwitchBroadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ProbeServiceConstant.RECEIVER_ACTION_OPEN_PROBE_SUCCESS)) {
                DialogUtil.hideLoadingDialog();
                Log.d(TAG, "ProbeSwitchBroadReceiver open probe success");
                switchButton.setOnCheckedChangeListener(null);
                switchButton.setChecked(true);
                switchButton.setOnCheckedChangeListener(checkedChangeListener);
                times=0;
                isOpeningProbe = false;
                mHandler.removeCallbacks(openTimeOutRunnable);
                Toast.makeText(SettingActivity.this, "客流探测器已开启", Toast.LENGTH_LONG).show();
            } else if (intent.getAction().equals(ProbeServiceConstant.RECEIVER_ACTION_CLOSE_PROBE_SUCCESS)) {
                Log.d(TAG, "ProbeSwitchBroadReceiver close probe success");
                DialogUtil.hideLoadingDialog();
                switchButton.setOnCheckedChangeListener(null);
                switchButton.setChecked(false);
                switchButton.setOnCheckedChangeListener(checkedChangeListener);
                Toast.makeText(SettingActivity.this, "客流探测器已关闭", Toast.LENGTH_LONG).show();
            }else if (intent.getAction().equals(ProbeServiceConstant.RECEIVER_ACTION_OPEN_PROBE_FAILURE)) {
                mHandler.removeCallbacks(openTimeOutRunnable);
                isOpeningProbe = false;
                times=0;
                switchButton.setOnCheckedChangeListener(null);
                switchButton.setChecked(false);
                switchButton.setOnCheckedChangeListener(checkedChangeListener);
                DialogUtil.hideLoadingDialog();
                Toast.makeText(SettingActivity.this,"客流探测器开启失败",Toast.LENGTH_LONG).show();
            }
        }
    }

    private ProbeSwitchBroadReceiver probeSwitchBroadReceiver = new ProbeSwitchBroadReceiver();

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ProbeServiceConstant.RECEIVER_ACTION_OPEN_PROBE_SUCCESS);
        myIntentFilter.addAction(ProbeServiceConstant.RECEIVER_ACTION_CLOSE_PROBE_SUCCESS);
        myIntentFilter.addAction(ProbeServiceConstant.RECEIVER_ACTION_OPEN_PROBE_FAILURE);
        //注册广播
        registerReceiver(probeSwitchBroadReceiver, myIntentFilter);
    }

}
