package com.ums.wifiprobe.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ums.wifiprobe.AppProtocolInfo;
import com.ums.wifiprobe.CommonStants;
import com.ums.wifiprobe.R;
import com.ums.wifiprobe.app.GlobalValueManager;

/**
 * Created by chenzhy on 2018/3/6.
 */

public class SplashActivity extends Activity implements View.OnClickListener {
    private TextView tvAppName, tvVersionName, tvProtocol1, tvProtocol2;
    private ImageView ivAppIcon;
    private CheckBox cbAgree;
    private Button btnAgree;
    private AppProtocolInfo appProtocolInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        appProtocolInfo = (AppProtocolInfo) getIntent().getExtras().getSerializable("appinfo");
        tvAppName = (TextView) findViewById(R.id.splash_tv_appname);
        tvVersionName = (TextView) findViewById(R.id.splash_tv_versionname);
        tvProtocol1 = (TextView) findViewById(R.id.splash_tv_protocol1);
        tvProtocol2 = (TextView) findViewById(R.id.splash_tv_protocol2);
        ivAppIcon = (ImageView) findViewById(R.id.splash_iv_appicon);
        SpannableString protocolStr = new SpannableString("《使用许可协议》");
        protocolStr.setSpan(new UnderlineSpan(), 0, protocolStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvProtocol1.setText(protocolStr);
        tvProtocol2.setText(protocolStr);
        cbAgree = (CheckBox) findViewById(R.id.splash_cb_agree);
        btnAgree = (Button) findViewById(R.id.splash_btn_agree);
        tvProtocol1.setOnClickListener(this);
        tvProtocol2.setOnClickListener(this);
        btnAgree.setOnClickListener(this);
        cbAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnAgree.setClickable(true);
                } else {
                    btnAgree.setClickable(false);
                }
            }
        });
        tvAppName.setText(appProtocolInfo.getAppName());
        tvVersionName.setText(appProtocolInfo.getAppVersion());
        if(appProtocolInfo.getAppIcon()!=0){
            ivAppIcon.setImageResource(appProtocolInfo.getAppIcon());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.splash_tv_protocol1:
            case R.id.splash_tv_protocol2:
                Intent intent = new Intent(this, ProtocolActivity.class);
                intent.putExtra("protocol", appProtocolInfo.getAppProtocalFileName());
                startActivity(intent);
                break;
            case R.id.splash_btn_agree:
                if (cbAgree.isChecked()) {
                    if (appProtocolInfo.getAppName().equals(getString(R.string.app_name))) {
                        GlobalValueManager.getInstance().setAgreeProtocol(true);
                        startActivity(new Intent(getBaseContext(), CustomerInfoActivity.class));
                    } else {
                        //店知了
                        setResult(-1);//-1同意协议 -2拒绝协议
                    }
                    finish();
                } else {
                    Toast.makeText(this, "请先同意使用许可协议", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(!appProtocolInfo.getAppName().equals(getString(R.string.app_name))){
            setResult(-2);//拒绝协议
        }
        super.onBackPressed();
    }
}
