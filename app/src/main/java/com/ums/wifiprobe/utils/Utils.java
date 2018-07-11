package com.ums.wifiprobe.utils;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.util.TypedValue;

/**
 * Created by chenzhy on 2017/8/22.
 */

public class Utils {
    public static void wakeScreen(Context context) {
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        kl.disableKeyguard(); // 解锁
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        // 点亮屏幕
        PowerManager.WakeLock pm_wl = pm.newWakeLock(
                PowerManager.ACQUIRE_CAUSES_WAKEUP
                        | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        pm_wl.acquire();
        pm_wl.release();//发出命令
    }
    public final static boolean isScreenOn(Context c) {
        PowerManager powermanager;
        powermanager = (PowerManager) c.getSystemService(Context.POWER_SERVICE);
        return powermanager.isScreenOn();
    }
    public static int dp2px(Context c,int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                c.getResources().getDisplayMetrics());
    }
}
