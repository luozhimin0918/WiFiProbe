package com.ums.wifiprobe.ui.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ums.wifiprobe.R;
import com.ums.wifiprobe.ui.customview.numberprogressbar.NumberProgressBar;

/**
 * Created by chenzhy on 2017/9/28.
 */

public class DownloadDialog extends Dialog implements View.OnClickListener {
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;
    private NumberProgressBar progressBar;

    private Context mContext;
    private String title;
    private OnDialogCloseListener listener;
    private String positiveName;

    public DownloadDialog(Context context) {
        super(context, R.style.commondialog);
        this.mContext = context;
    }

    public DownloadDialog(Context context, String title) {
        super(context, R.style.commondialog);
        this.mContext = context;
        this.title = title;
    }

    public DownloadDialog(Context context, int themeResId, String title) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
    }

    public DownloadDialog(Context context, int themeResId, String title, OnDialogCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.title = title;
        this.listener = listener;
    }

    protected DownloadDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }


    public DownloadDialog setContent(String title) {
        this.title = title;
        return this;
    }

    public DownloadDialog setPositiveButton(String name) {
        this.positiveName = name;
        return this;
    }


    public void setOnCloseListener(OnDialogCloseListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_download);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public void setDownloadState(String title) {
        titleTxt.setText(title);
    }

    public void updateProgress(int i) {
        progressBar.setProgress(i);
    }

    private void initView() {
        titleTxt = (TextView) findViewById(R.id.title);
        submitTxt = (TextView) findViewById(R.id.submit);
        submitTxt.setOnClickListener(this);
        progressBar = (NumberProgressBar) findViewById(R.id.numberbar);
        cancelTxt = (TextView) findViewById(R.id.cancel);
        cancelTxt.setOnClickListener(this);
        titleTxt.setText(title);
        if (!TextUtils.isEmpty(positiveName)) {
            submitTxt.setText(positiveName);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                this.dismiss();
                if (listener != null) {
                    listener.onClick(this, true);
                }
            case R.id.cancel:
                this.dismiss();
                if (listener != null) {
                    listener.onClick(this, false);
                }
                break;
        }
    }


}
