package com.ums.wifiprobe.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.ums.wifiprobe.R;
import com.ums.wifiprobe.ui.customview.MyMarkerViewNew;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Descriptions:柱状图管理类
 */

public class BarChartManager {
    private BarChart mBarChart;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;
    //颜色集合
    List<Integer> colors = new ArrayList<>();
    Context context ;
    public BarChartManager(BarChart barChart, Context context) {
        this.mBarChart = barChart;
        leftAxis = mBarChart.getAxisLeft();
        rightAxis = mBarChart.getAxisRight();
        xAxis = mBarChart.getXAxis();
        colors.add(Color.rgb(73, 169, 238));
        colors.add(Color.rgb(152, 216, 125));

        this.context=context;
        initBarChart();
    }

    /**
     * 初始化LineChart
     */
    private void initBarChart() {
        //背景颜色
        mBarChart.setBackgroundColor(Color.WHITE);
        //网格
        mBarChart.setDrawGridBackground(false);
        //背景阴影
        mBarChart.setDrawBarShadow(false);
        mBarChart.setHighlightFullBarEnabled(false);

        //显示边界
        mBarChart.setDrawBorders(false);
        //设置动画效果
        mBarChart.animateY(1000, Easing.EasingOption.Linear);
        mBarChart.animateX(1000, Easing.EasingOption.Linear);
        //图表的描述
        mBarChart.getDescription().setText("");
        mBarChart.setScaleXEnabled(true);
        mBarChart.setScaleYEnabled(false);


        //折线图例 标签 设置
        Legend legend = mBarChart.getLegend();
        legend.setEnabled(false);
        legend.setForm(Legend.LegendForm.SQUARE);//图示 标签的形状。   正方形
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setTypeface(Typeface.createFromAsset(this.context.getAssets(), "OpenSans-Light.ttf"));
        legend.setYOffset(10f);
        legend.setXOffset(0f);
        legend.setYEntrySpace(20f);
        legend.setTextSize(10f);
        legend.setFormSize(10);

        //XY轴的设置
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);//设置最小的区间，避免标签的迅速增多
        xAxis.setTypeface(Typeface.createFromAsset(this.context.getAssets(), "OpenSans-Light.ttf"));
        xAxis.setTextSize(9);
        xAxis.setDrawGridLines(false);//设置竖状的线是否显示
        xAxis.setCenterAxisLabels(true);//设置标签居中
        xAxis.setDrawAxisLine(false);
        // xAxis.setSpaceMax(10);
        // xAxis.setValueFormatter();

        leftAxis.setDrawGridLines(false);//设置横状的线是否显示
//        leftAxis.enableGridDashedLine(6f, 3f, 0);//虚线

        leftAxis.setAxisLineWidth(1f);
        leftAxis.setEnabled(true);
        leftAxis.setDrawAxisLine(false);// 是否绘制轴线
        leftAxis.setTypeface(Typeface.createFromAsset(this.context.getAssets(), "OpenSans-Light.ttf"));
        leftAxis.setTextSize(9);
//        leftAxis.setGridColor(0xacb3e5fc);
        //   leftAxis.setTextColor(0xb3e5fc);//设置左边Y轴文字的颜色
        //   leftAxis.setAxisLineColor(0xb3e5fc);//设置左边Y轴的颜色

        // rightAxis.setDrawGridLines(false);//设置横状的线是否显示
        rightAxis.setEnabled(false);//隐藏右边轴和数字

        //保证Y轴从0开始，不然会上移一点
        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);
        mBarChart.setDoubleTapToZoomEnabled(false); // 设置为false以禁止通过在其上双击缩放图表。

        // mBarChart.setBorderWidth(15);//设置边界宽度
    }

    /**
     * 展示柱状图(一条)
     *
     * @param xAxisValues
     * @param yAxisValues
     * @param label
     * @param color
     */
    public void showBarChart(List<Float> xAxisValues, List<Float> yAxisValues, String label, int color) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < xAxisValues.size(); i++) {
            entries.add(new BarEntry(xAxisValues.get(i), yAxisValues.get(i)));
        }
        // 每一个BarDataSet代表一类柱状图
        BarDataSet barDataSet = new BarDataSet(entries, label);

        barDataSet.setColor(color);
        barDataSet.setValueTextSize(9f);
        barDataSet.setFormLineWidth(1f);
        barDataSet.setFormSize(15.f);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet);
        BarData data = new BarData(dataSets);
        //设置X轴的刻度数
        xAxis.setLabelCount(xAxisValues.size()-1 , false);

        mBarChart.setData(data);
    }

    /**
     * 展示柱状图(多条)
     *
     * @param xAxisValues
     * @param yAxisValues
     * @param labels
     * @param colours
     */
    public void showBarChart(List<Float> xAxisValues, List<List<Float>> yAxisValues, List<String> labels, List<Integer> colours) {
        BarData data = new BarData();
        // xAxis.setValueFormatter(new StringAxisValueFormatter(xAxisValues));
        for (int i = 0; i < yAxisValues.size(); i++) {
            ArrayList<BarEntry> entries = new ArrayList<>();
            for (int j = 0; j < yAxisValues.get(i).size(); j++) {

                entries.add(new BarEntry(xAxisValues.get(i), yAxisValues.get(i).get(j)));
            }
            BarDataSet barDataSet = new BarDataSet(entries, labels.get(i));

            barDataSet.setColor(colours.get(i));
            barDataSet.setValueTextColor(colours.get(i));
            barDataSet.setValueTextSize(10f);
            barDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
            data.addDataSet(barDataSet);
        }
        int amount = yAxisValues.size();

        float groupSpace = 0.12f; //柱状图组之间的间距
        float barSpace = (float) ((1 - 0.12) / amount / 10); // x4 DataSet
        float barWidth = (float) ((1 - 0.12) / amount / 10 * 9); // x4 DataSet

        // (0.2 + 0.02) * 4 + 0.12 = 1.00 -> 组成1个"group"的宽度
        xAxis.setLabelCount(xAxisValues.size() - 1, false);
        data.setBarWidth(barWidth);


        data.groupBars(0, groupSpace, barSpace);
        mBarChart.setData(data);
    }

    public void showBarChart(List<String> xValues, List<List<Float>> yValuesList, List<String> labels){

        mBarChart.getXAxis().setValueFormatter(new StringAxisValueFormatter(xValues));
        BarData data = new BarData();
        for (int i = 0; i < yValuesList.size(); i++) {
            ArrayList<BarEntry> entries = new ArrayList<>();
            for (int j = 0; j < yValuesList.get(i).size(); j++) {
                entries.add(new BarEntry(i, yValuesList.get(i).get(j)));
            }
            // y轴的数据集合
            BarDataSet barDataSet = new BarDataSet(entries, labels.get(i));
            barDataSet.setColor(colors.get(i));
            barDataSet.setValueTextColor(colors.get(i));
            barDataSet.setValueTextSize(10f);
            data.addDataSet(barDataSet);
        }
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

                DecimalFormat mFormat;
//                mFormat = new DecimalFormat("###,###,##0.00"); // use one decimal
                mFormat= new DecimalFormat("#.###"); //结果： 3.14
                return mFormat.format(value) ;
            }

        });
        int amount = yValuesList.size();

        float groupSpace = 0.62f; //柱状图组之间的间距
        float barSpace = (float) ((1 - 0.12) / amount / 10); // x4 DataSet
        float barWidth = 0.15f; // x4 DataSet

        // (0.2 + 0.02) * 4 + 0.12 = 1.00 -> interval per "group"
        mBarChart.getXAxis().setLabelCount(xValues.size()-1, false);
        data.setBarWidth(barWidth);
        xAxis.setAxisMaximum(xValues.size());
        xAxis.setAxisMinimum(0);
        data.groupBars(0, groupSpace, barSpace);
        MyMarkerViewNew mv = new MyMarkerViewNew(context, R.layout.custom_marker_view_new);
        mv.setChartView(mBarChart); // For bounds control
        mBarChart.setMarker(mv); // Set the marker to the chart
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.setData(data);
        mBarChart.getBarData().setDrawValues(false);//是dataSet的属性，设置是否在图上显示出当前点（柱状图）的值
        mBarChart.getData().notifyDataChanged();
        mBarChart.notifyDataSetChanged();
    }
    protected BarData getBarData(List<String> xValues, List<List<Float>> yValuesList) {
        mBarChart.getXAxis().setValueFormatter(new StringAxisValueFormatter(xValues));
        BarData data = new BarData();
        for (int i = 0; i < yValuesList.size(); i++) {
            ArrayList<BarEntry> entries = new ArrayList<>();
            for (int j = 0; j < yValuesList.get(i).size(); j++) {
                entries.add(new BarEntry(i, yValuesList.get(i).get(j)));
            }
            // y轴的数据集合
            BarDataSet barDataSet = new BarDataSet(entries, xValues.get(i));
            barDataSet.setColor(colors.get(i));
            barDataSet.setValueTextColor(colors.get(i));
            barDataSet.setValueTextSize(10f);
            data.addDataSet(barDataSet);
        }
        int amount = yValuesList.size();

        float groupSpace = 0.12f; //柱状图组之间的间距
        float barSpace = (float) ((1 - 0.12) / amount / 10); // x4 DataSet
        float barWidth = (float) ((1 - 0.12) / amount / 10 * 9); // x4 DataSet

        // (0.2 + 0.02) * 4 + 0.08 = 1.00 -> interval per "group"
        mBarChart.getXAxis().setLabelCount(xValues.size()-1, false);
        data.setBarWidth(barWidth);

        data.groupBars(0, groupSpace, barSpace);
        return data;
    }
    /**
     * 设置Y轴值
     *
     * @param max
     * @param min
     * @param labelCount
     */
    public void setYAxis(float max, float min, int labelCount) {
        if (max < min) {
            return;
        }
        leftAxis.setAxisMaximum(max);
        leftAxis.setAxisMinimum(min);
        leftAxis.setLabelCount(labelCount, false);

        rightAxis.setAxisMaximum(max);
        rightAxis.setAxisMinimum(min);
        rightAxis.setLabelCount(labelCount, false);
        mBarChart.invalidate();
    }

    /**
     * 设置X轴的值
     *
     * @param max
     * @param min
     * @param labelCount
     */
    public void setXAxis(float max, float min, int labelCount) {
        xAxis.setAxisMaximum(max);
        xAxis.setAxisMinimum(min);
        xAxis.setLabelCount(labelCount, false);

        mBarChart.invalidate();
    }

    /**
     * 设置高限制线
     *
     * @param high
     * @param name
     */
    public void setHightLimitLine(float high, String name, int color) {
        if (name == null) {
            name = "高限制线";
        }
        LimitLine hightLimit = new LimitLine(high, name);
        hightLimit.setLineWidth(4f);
        hightLimit.setTextSize(10f);
        hightLimit.setLineColor(color);
        hightLimit.setTextColor(color);
        leftAxis.addLimitLine(hightLimit);
        mBarChart.invalidate();
    }

    /**
     * 设置低限制线
     *
     * @param low
     * @param name
     */
    public void setLowLimitLine(int low, String name) {
        if (name == null) {
            name = "低限制线";
        }
        LimitLine hightLimit = new LimitLine(low, name);
        hightLimit.setLineWidth(4f);
        hightLimit.setTextSize(10f);
        leftAxis.addLimitLine(hightLimit);
        mBarChart.invalidate();
    }

    /**
     * 设置描述信息
     *
     * @param str
     */
    public void setDescription(String str) {
        Description description = new Description();
        description.setText(str);
        mBarChart.setDescription(description);
        mBarChart.invalidate();
    }
}