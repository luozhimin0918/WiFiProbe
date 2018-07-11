package com.ums.wifiprobe.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by chenzhy on 2017/11/12.
 */

public class WiFiStateBroadReceiver extends BroadcastReceiver {

    private static final String TAG = "WiFiProbe-wifiState";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED")){
            //获取当前的wifi状态int类型数据
            if(!intent.hasExtra(WifiManager.EXTRA_WIFI_STATE)){
                return;
            }
            int mWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            Log.d(TAG,"wifi state had changed !"+mWifiState+" isOpingProbe :"+ProbeServiceConstant.probeIsOpening);
            if(ProbeServiceConstant.probeIsOpening){
                return;
            }
            switch (mWifiState ) {
                case WifiManager.WIFI_STATE_ENABLED:
                    Log.d(TAG,"wifi state had changed to enabled !");
                    if(ProbeServiceConstant.probeIsOpenedLaststate&&!ProbeServiceConstant.probeIsOpened){
                        Log.d(TAG,"need to open probe !");
                        context.sendBroadcast(new Intent(ProbeServiceConstant.RECEIVER_ACTION_OPEN_PROBE));
                    }
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    Log.d(TAG,"wifi state had changed to disabled !should close probe completely");
                    ProbeServiceConstant.probeIsOpenedLaststate = ProbeServiceConstant.probeIsOpened;
                    context.sendBroadcast(new Intent(ProbeServiceConstant.RECEIVER_ACTION_CLOSE_PROBE_COMPLETE));
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    break;
            }
        }
    }
}
