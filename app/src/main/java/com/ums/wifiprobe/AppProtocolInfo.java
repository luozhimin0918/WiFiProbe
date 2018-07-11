package com.ums.wifiprobe;

import java.io.Serializable;

/**
 * Created by chenzhy on 2018/3/13.
 */

public class AppProtocolInfo implements Serializable{
    private String appName;
    private String appVersion;
    private String appProtocalFileName;
    private int appIcon;

    public AppProtocolInfo(String appName, String appVersion, String appProtocalFileName,int appIcon) {
        this.appName = appName;
        this.appVersion = appVersion;
        this.appProtocalFileName = appProtocalFileName;
        this.appIcon = appIcon;
    }

    public int getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(int appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppProtocalFileName() {
        return appProtocalFileName;
    }

    public void setAppProtocalFileName(String appProtocalFileName) {
        this.appProtocalFileName = appProtocalFileName;
    }
}
