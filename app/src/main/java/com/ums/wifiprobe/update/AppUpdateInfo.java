package com.ums.wifiprobe.update;

import android.app.Dialog;
import android.content.Context;

import com.ums.wifiprobe.ui.customview.OnDialogCloseListener;

/**
 * Created by chenzhy on 2018/1/16.
 */

public abstract class AppUpdateInfo {
    protected String packageName;
    protected String appName;
    protected String appKey;
    protected int appNumber=1;
    protected int downloadIndex=1;
    protected Dialog dialog;

    public int getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(int appNumber) {
        this.appNumber = appNumber;
    }

    public int getDownloadIndex() {
        return downloadIndex;
    }

    public void setDownloadIndex(int downloadIndex) {
        this.downloadIndex = downloadIndex;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public abstract void showDownloadDialog(Context context, String title,  OnDialogCloseListener listener);
    public abstract void updateDownloadProgress(String state,int progress);
    public abstract void hideDownloadDialog();
}
