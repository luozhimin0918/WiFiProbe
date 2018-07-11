package com.ums.shdep.reward;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by chenzhy on 2017/9/28.
 */

public class ThankBossDialog extends Dialog implements View.OnClickListener {
    private ImageView ivThanks;
    private Button btnThanks;
    private OnDialogCloseListener listener;
    private Context mContext;

    public ThankBossDialog(Context context,OnDialogCloseListener listener) {
        super(context, R.style.ThankbossDialog);
        mContext = context;
        this.listener = listener;
    }

    public ThankBossDialog(Context context, String content) {
        super(context, R.style.ThankbossDialog);
        mContext = context;
    }

    public ThankBossDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        mContext = context;
    }

    public ThankBossDialog(Context context, int themeResId, String content, OnDialogCloseListener listener) {
        super(context, themeResId);
        mContext = context;
        this.listener = listener;
    }

    protected ThankBossDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }




    public void setOnCloseListener(OnDialogCloseListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_thankboss);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        ivThanks = (ImageView) findViewById(R.id.reward_dialog_iv_thankboss);
        btnThanks = (Button) findViewById(R.id.reward_dialog_btn_thankboss);
        btnThanks.setOnClickListener(this);
       }

    @Override
    public void show() {
        super.show();
//        Glide.with(mContext).load(R.drawable.littleman).diskCacheStrategy(DiskCacheStrategy.ALL).into((ImageView) findViewById(R.id.reward_dialog_iv_thankboss));
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.reward_dialog_btn_thankboss) {
            this.dismiss();
            mContext = null;
            if (listener != null) {
                listener.onDialogClosed();
            }

        }
    }

    interface OnDialogCloseListener {
        void onDialogClosed();
    }


}
