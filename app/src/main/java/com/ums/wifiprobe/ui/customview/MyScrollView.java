package com.ums.wifiprobe.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by chenzhy on 2017/10/13.
 */

public class MyScrollView extends ScrollView {

    float lastYPosition = 0;


    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastYPosition = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float yPosition = ev.getY();
                if(!canScrollVertically(-1)&&canScrollVertically(1)&&(yPosition-lastYPosition)>0){
                   requestDisallowInterceptTouchEvent(true);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


}
