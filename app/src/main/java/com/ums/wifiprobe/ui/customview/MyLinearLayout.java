package com.ums.wifiprobe.ui.customview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ums.wifiprobe.R;

/**
 * Created by chenzhy on 2017/10/30.
 */

public class MyLinearLayout extends LinearLayout {
    View mView;
    ImageView imageView;
    TextView textView1;
    ProgressBar bar;
    TextView textVeiw2;

    public MyLinearLayout(Context context) {
        super(context);
        initView(context);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    private void initView(Context context){
        LayoutInflater mInflater = LayoutInflater.from(context);
        mView = mInflater.inflate(R.layout.layout_brandinfo, null);
        addView(mView);
        imageView = (ImageView) mView.findViewById(R.id.layout_brandicon);
        textView1 = (TextView) mView.findViewById(R.id.layout_brandname);
        bar = (ProgressBar) mView.findViewById(R.id.layout_brandprogressbar);
        textVeiw2 = (TextView) mView.findViewById(R.id.layout_brandpercent);
    }

    public void updateState(int color,String brandName,int progress,String percent){
        GradientDrawable drawable1 = (GradientDrawable) imageView.getBackground();
        drawable1.setColor(color);
        textView1.setText(brandName);
        textVeiw2.setText(percent);

        GradientDrawable p=new GradientDrawable();
        p.setCornerRadius(16);
        p.setColor(color);
        ClipDrawable progressDrawable = new ClipDrawable(p, Gravity.LEFT, ClipDrawable.HORIZONTAL);
        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.parseColor("#f4f4f4"));
        background.setCornerRadius(16);
        LayerDrawable pd=new LayerDrawable(new Drawable[]{background,progressDrawable});
        bar.setProgressDrawable(pd);
        bar.setProgress(progress-1);
        bar.setProgress(progress);

    }


}
