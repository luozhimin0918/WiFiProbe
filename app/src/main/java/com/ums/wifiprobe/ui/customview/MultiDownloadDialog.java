package com.ums.wifiprobe.ui.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ums.wifiprobe.R;
import com.ums.wifiprobe.ui.customview.numberprogressbar.NumberProgressBar;

/**
 * Created by chenzhy on 2017/9/28.
 */

public class MultiDownloadDialog extends Dialog implements View.OnClickListener {
    private TextView titleTxt;
    private TextView multiTitleTxt;
    private TextView hideTxt;
    private NumberProgressBar progressBar;

    private Context mContext;
    private String title;
    private String mulTitle;
    private OnDialogCloseListener listener;
    private String positiveName;

    public MultiDownloadDialog(Context context) {
        super(context, R.style.commondialog);
        this.mContext = context;
    }

    public MultiDownloadDialog(Context context, String title) {
        super(context, R.style.commondialog);
        this.mContext = context;
        this.title = title;
    }

    public MultiDownloadDialog(Context context, int themeResId, String title) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
    }

    public MultiDownloadDialog(Context context, int themeResId, String title, OnDialogCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.listener = listener;
    }

    protected MultiDownloadDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }


    public MultiDownloadDialog setTitle(String title) {
        this.title = title;
        return this;
    }
    public MultiDownloadDialog setMulTitle(String mulTitle){
        this.mulTitle = mulTitle;
        return this;
    }

    public MultiDownloadDialog setPositiveButton(String name) {
        this.positiveName = name;
        return this;
    }


    public void setOnCloseListener(OnDialogCloseListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_multidownload);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public void setDownloadState(String title) {
        titleTxt.setText(title);
    }

    public void updateProgress(int i) {
        progressBar.setProgress(i);
    }

    public void updateMultiTitle(String mulTitle){
        multiTitleTxt.setText(mulTitle);
    }

    public void updateMultiButton(String buttonName){
        hideTxt.setText(buttonName);
    }

    private void initView() {
        titleTxt = (TextView) findViewById(R.id.title);
        multiTitleTxt = (TextView) findViewById(R.id.multititle);
        hideTxt = (TextView) findViewById(R.id.hide);
        hideTxt.setOnClickListener(this);
        progressBar = (NumberProgressBar) findViewById(R.id.numberbar);
        titleTxt.setText(title);
        multiTitleTxt.setText(mulTitle);
        if (!TextUtils.isEmpty(positiveName)) {
            hideTxt.setText(positiveName);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hide:
                this.dismiss();
                if (listener != null) {
                    listener.onClick(this, true);
                }
        }
    }


}
