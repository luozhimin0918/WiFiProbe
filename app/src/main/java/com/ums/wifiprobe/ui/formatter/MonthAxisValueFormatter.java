package com.ums.wifiprobe.ui.formatter;

import android.util.Log;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by philipp on 02/06/16.
 */
public class MonthAxisValueFormatter implements IAxisValueFormatter
{

    protected String[] mMonthDays ;

    public MonthAxisValueFormatter(String[] weeks) {
        this.mMonthDays = weeks;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        int days = (int) value;
        if(days<mMonthDays.length){
        if(days==0){
            return mMonthDays[0];
        }else if(days==mMonthDays.length-1){
            return mMonthDays[mMonthDays.length-1];
        }else if((days+1)%5==0){
            return mMonthDays[days];
        }}
        return "";
    }


}
