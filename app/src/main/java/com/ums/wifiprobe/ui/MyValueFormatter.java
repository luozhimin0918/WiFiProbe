package com.ums.wifiprobe.ui;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by chenzhy on 2017/9/21.
 */

public class MyValueFormatter implements IAxisValueFormatter {
    private String[] mValues;

    public MyValueFormatter(String[] values) {
        mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mValues[(int) value % mValues.length];
    }
}
