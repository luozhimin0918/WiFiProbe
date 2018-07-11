package com.ums.wifiprobe.ui.formatter;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;

/**
 * Created by philipp on 02/06/16.
 */
public class DayHoursAxisValueFormatter implements IAxisValueFormatter {

    protected String[] mDayHours;

    public DayHoursAxisValueFormatter(String[] weeks) {
        this.mDayHours = weeks;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        int days = (int) value;
        if (days < mDayHours.length) {
            if (days == 0) {
                return mDayHours[0];
            } else if (days == mDayHours.length - 1) {
                return mDayHours[mDayHours.length - 1];
            } else if (days % 4 == 0) {
                return mDayHours[days];
            }
        }
        return "";
    }


}
