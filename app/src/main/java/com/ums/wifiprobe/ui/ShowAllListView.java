package com.ums.wifiprobe.ui;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;

/**
 * Created by lihe on 2017/9/24.
 */

public class ShowAllListView extends ListView {
    public ShowAllListView(Context context) {
        super(context);
    }

    public ShowAllListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShowAllListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(1073741823,MeasureSpec.AT_MOST));
    }









}
