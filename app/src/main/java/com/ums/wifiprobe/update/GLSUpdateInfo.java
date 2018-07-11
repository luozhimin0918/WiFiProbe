package com.ums.wifiprobe.update;

import android.content.Context;
import android.text.TextUtils;

import com.ums.wifiprobe.CommonStants;
import com.ums.wifiprobe.ui.customview.DownloadDialog;
import com.ums.wifiprobe.ui.customview.MultiDownloadDialog;
import com.ums.wifiprobe.ui.customview.OnDialogCloseListener;

/**
 * Created by chenzhy on 2018/1/16.
 */

public class GLSUpdateInfo extends AppUpdateInfo {

    public GLSUpdateInfo() {
        this.appName = "服务";
        this.appKey = CommonStants.APPKEY_SELF;
        this.packageName = "com.gelian.device";
    }

    @Override
    public void showDownloadDialog(Context context, String title, OnDialogCloseListener listener) {
        dialog = new MultiDownloadDialog(context);
        ((MultiDownloadDialog) dialog).setTitle(title);
        ((MultiDownloadDialog) dialog).setMulTitle("客群画像");
        dialog.setCancelable(false);
        ((MultiDownloadDialog) dialog).setOnCloseListener(listener);
        dialog.show();
    }

    public void updateMultiTitle(String multiTitle){
        if(!TextUtils.isEmpty(multiTitle)){
            ((MultiDownloadDialog) dialog).updateMultiTitle(multiTitle);
        }
    }
    public void updateMultiButton(String buttonName){
        if(!TextUtils.isEmpty(buttonName)){
            ((MultiDownloadDialog) dialog).updateMultiButton(buttonName);
        }
    }

    @Override
    public void updateDownloadProgress(String state, int progress) {
        if(dialog==null){
            return;
        }

        ((MultiDownloadDialog) dialog).setDownloadState(state);
        if (progress >= 0)
            ((MultiDownloadDialog) dialog).updateProgress(progress);

    }

    @Override
    public void hideDownloadDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = null;
    }
}
