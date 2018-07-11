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

public class CommonDialog2 extends Dialog implements View.OnClickListener{
    private TextView contentTxt;
    private TextView submitTxt;
    private TextView cancelTxt;

    private Context mContext;
    private String content;
    private OnDialogCloseListener listener;
    private String positiveName;
    private String negativeName;

    public CommonDialog2(Context context) {
        super(context, R.style.commondialog);
        this.mContext = context;
    }
    public CommonDialog2(Context context, String content) {
        super(context, R.style.commondialog);
        this.mContext = context;
        this.content = content;
    }

    public CommonDialog2(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public CommonDialog2(Context context, int themeResId, String content, OnDialogCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
    }

    protected CommonDialog2(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }


    public CommonDialog2 setContent(String content){
        this.content = content;
        return this;
    }

    public CommonDialog2 setPositiveButton(String name){
        this.positiveName = name;
        return this;
    }

    public CommonDialog2 setNegativeButton(String name){
        this.negativeName = name;
        return this;
    }
    public void setOnCloseListener(OnDialogCloseListener listener){
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_common2);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){
        contentTxt = (TextView)findViewById(R.id.content);
        submitTxt = (TextView)findViewById(R.id.submit);
        submitTxt.setOnClickListener(this);
        cancelTxt = (TextView)findViewById(R.id.cancel);
        cancelTxt.setOnClickListener(this);


        if(!TextUtils.isEmpty(positiveName)){
            submitTxt.setText(positiveName);
        }

        if(!TextUtils.isEmpty(negativeName)){
            cancelTxt.setText(negativeName);
        }
        if(content!=null){
            contentTxt.setText(content);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                this.dismiss();
                if(listener != null){
                    listener.onClick(this, false);
                }
                break;
            case R.id.submit:
                this.dismiss();
                if(listener != null){
                    listener.onClick(this, true);
                }
                break;
        }
    }


}
