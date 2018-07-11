package com.ums.wifiprobe.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
/**
 * Created by carl_liu on 2017/11/15.
 */
public class CheckApkExist {
    private static String facebookPkgName = "com.facebook.katana";

    public static boolean checkApkExist(Context context, String packageName){
        if (TextUtils.isEmpty(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);

            return true;
        } catch (PackageManager.NameNotFoundException e) {

            return false;
        }
    }

    public static boolean checkFacebookExist(Context context){
        return checkApkExist(context, facebookPkgName);
    }
    // 剩余的可以自行扩展，下边会给出一些常用的包名
}





