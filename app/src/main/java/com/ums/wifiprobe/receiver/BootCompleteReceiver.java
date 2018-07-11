package com.ums.wifiprobe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ums.wifiprobe.app.GlobalValueManager;
import com.ums.wifiprobe.service.ProbeService;
import com.ums.wifiprobe.service.ProbeServiceConstant;

import java.util.Map;

/**
 * Created by chenzhy on 2017/9/13.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    private static final String TAG = "WiFiProbe-Receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            //开机启动wifi探针服务
            Log.d(TAG, "boot complete wifiprobe !");
            if (GlobalValueManager.getInstance().isAgreeProtocol()) {
                context.startService(new Intent(context, ProbeService.class));
            }
        } else if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED) || intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED) || intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            if (packageName == null) {
                return;
            }
            if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED) && packageName.equals("com.ums.upos.uapi")) {
                //获取最新设备支持信息
                Log.d(TAG, "u service replace ,notify wifiprobeservice !");
                GlobalValueManager.getInstance().setFirstOpenApp(true);//需排除无关升级，若两版本皆支持探针，则还是需要重启下探针服务，若探针程序已经在运行，则重新绑定下服务
                context.sendBroadcast(new Intent(ProbeServiceConstant.RECEIVER_ACTION_SERVICE_REPLACED));
            }

        }
    }


}
