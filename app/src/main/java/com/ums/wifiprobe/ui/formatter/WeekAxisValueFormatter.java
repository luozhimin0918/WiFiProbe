package com.ums.wifiprobe.ui.formatter;

import android.util.Log;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by philipp on 02/06/16.
 */
public class WeekAxisValueFormatter implements IAxisValueFormatter {

    protected String[] mWeekDays;

    private BarLineChartBase<?> chart;

    public WeekAxisValueFormatter(String[] weeks) {
        this.mWeekDays = weeks;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        int days = (int) value;
        if (days < mWeekDays.length) {
            if (days == 0) {
                return mWeekDays[0];
            } else if (days == mWeekDays.length - 1) {
                return mWeekDays[mWeekDays.length - 1];
            } else if (days < mWeekDays.length - 1) {
                return mWeekDays[days];
            }
        }
        return "";
    }


}
