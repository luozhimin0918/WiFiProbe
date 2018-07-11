package com.ums.wifiprobe.ui.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ums.wifiprobe.R;

/**
 * Created by chenzhy on 2017/9/28.
 */

public class AlertDialogWhiteList extends Dialog implements View.OnClickListener{
    private TextView contentTxt;
    private TextView submitTxt;

    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;

    public AlertDialogWhiteList(Context context) {
        super(context, R.style.commondialog);
        this.mContext = context;
    }
    public AlertDialogWhiteList(Context context, String content) {
        super(context, R.style.commondialog);
        this.mContext = context;
        this.content = content;
    }

    public AlertDialogWhiteList(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public AlertDialogWhiteList(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
    }

    protected AlertDialogWhiteList(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public AlertDialogWhiteList setButtonName(String name){
        submitTxt.setText(name);
        return this;
    }


    public AlertDialogWhiteList setContent(String content){
        this.content = content;
        return this;
    }

    public AlertDialogWhiteList setPositiveButton(String name){
        this.positiveName = name;
        return this;
    }


    public void setOnCloseListener(OnCloseListener listener){
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_alert_whitelist);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){
        contentTxt = (TextView)findViewById(R.id.content);
        submitTxt = (TextView)findViewById(R.id.submit);
        submitTxt.setOnClickListener(this);

        contentTxt.setText(content);
        if(!TextUtils.isEmpty(positiveName)){
            submitTxt.setText(positiveName);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                this.dismiss();
                if(listener != null){
                    listener.onClick(this);
                }
                break;
        }
    }

    public interface OnCloseListener{
        void onClick(Dialog dialog);
    }
}
