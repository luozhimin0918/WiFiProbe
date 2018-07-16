package com.ums.wifiprobe.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ums.wifiprobe.R;
import com.ums.wifiprobe.aidl.TransDataModel;
import com.ums.wifiprobe.eventbus.MessageEvent;
import com.ums.wifiprobe.ui.activity.RevisedTurnoverActivity;
import com.ums.wifiprobe.ui.customview.DoubleDatePickerDialog;
import com.ums.wifiprobe.ui.customview.EasyDialog;
import com.ums.wifiprobe.utils.BarChartManager;
import com.ums.wifiprobe.utils.TimeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2015/11/30.
 */
public class PassengerFlowTraFragment extends Fragment implements OnChartValueSelectedListener {
    protected WeakReference<View> mRootView;
    @BindView(R.id.chart_bar_mulp)
    BarChart chartBarMulp;
    Unbinder unbinder;
    @BindView(R.id.chang_Tari_button)
    Button changTariButton;
    @BindView(R.id.easy_mess_ke_num)
    LinearLayout easyMessKeNum;
    @BindView(R.id.question_mark_Keliu_num)
    ImageView question_mark_Keliu_num;
    @BindView(R.id.radioToday)
    RadioButton radioToday;
    @BindView(R.id.radioWeek)
    RadioButton radioWeek;
    @BindView(R.id.radioMonth)
    RadioButton radioMonth;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.xiuzhenTariMoney)
    TextView xiuzhenTariMoney;
    @BindView(R.id.xiuzhenBili)
    TextView xiuzhenBili;
    @BindView(R.id.shanpuMianji)
    TextView shanpuMianji;
    @BindView(R.id.tariMoneyZong)
    TextView tariMoneyZong;
    @BindView(R.id.keliuNum)
    TextView keliuNum;
    @BindView(R.id.shanPuPinxiao)
    TextView shanPuPinxiao;
    @BindView(R.id.keliuPrice)
    TextView keliuPrice;
    @BindView(R.id.xiuzhenKeliuPrice)
    TextView xiuzhenKeliuPrice;
    @BindView(R.id.dateDoubleSelect)
    TextView dateDoubleSelect;
    @BindView(R.id.dateDoubleSelectEnd)
    TextView dateDoubleSelectEnd;

    private View view;
    private final static String[] weekDays = new String[]{"12-01", "12-02", "12-03", "12-04", "12-05", "12-06", "12-07"};

    float moneyZong = 0f;//交易金额
    int keliuNumInt = 0;//客流数量
    String xiuzhenNumStr = "";//修正交易金额
    String xiuzhenBulieStr = "";//修正比例
    String zhandiMianji = "";//占地面积
    Context mContext;
    TransDataModel mTransDataModel;
    private DatePickDialog datePicker;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null || mRootView.get() == null) {
            view = inflater.inflate(R.layout.frament_pass_tra_main, null);
            mRootView = new WeakReference<View>(view);
            mContext = getContext();
            ButterKnife.bind(this, view);
            EventBus.getDefault().register(this);
            initData();
            initChart();
        } else {
            ViewGroup parent = (ViewGroup) mRootView.get().getParent();
            if (parent != null) {
                parent.removeView(mRootView.get());
            }
        }
        unbinder = ButterKnife.bind(this, mRootView.get());
        return mRootView.get();

    }


    private void initData() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                // 选中状态改变时被触发
                switch (checkedId) {
                    case R.id.radioToday:
                        handler.sendEmptyMessage(0);
                        break;
                    case R.id.radioWeek:
                        handler.sendEmptyMessage(1);
                        break;
                    case R.id.radioMonth:
                        handler.sendEmptyMessage(2);
                        break;
                }
            }
        });
        mTransDataModel = new TransDataModel(getContext());
        mTransDataModel.bind();
        handler.sendEmptyMessageDelayed(55, 1000);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        startDate=endDate=sdf.format(new Date());
        dateDoubleSelect.setText(startDate);
        dateDoubleSelectEnd.setText(endDate);
        dateDoubleSelect.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();
            @Override
            public void onClick(View view) {
                showDatePickerStart();
//                Log.d("Frag", TimeUtils.getLastTimeInterval(textString));
              /*  // 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
                new DoubleDatePickerDialog(getContext(), 0, new DoubleDatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                          int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear,
                                          int endDayOfMonth) {
                        String textString = String.format("%d-%d-%d", startYear,
                                startMonthOfYear + 1, startDayOfMonth, endYear, endMonthOfYear + 1, endDayOfMonth);
                        dateDoubleSelect.setText(textString);

                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), false).show();*/
            }
        });
        dateDoubleSelectEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerEnd();
            }
        });
    }

    private void initChart() {
        chartBarMulp.setOnChartValueSelectedListener(this);
        BarChartManager barChartManager2 = new BarChartManager(chartBarMulp, getContext());

        //设置x轴的数据
        List<String> xValues0 = new ArrayList<>();
        xValues0.add("12-01");
        xValues0.add("");
        xValues0.add("12-03");
        xValues0.add("");
        xValues0.add("12-05");
        xValues0.add("");
        xValues0.add("12-07");
        xValues0.add("");
        xValues0.add("12-06");
        xValues0.add("");
        xValues0.add("12-05");
        xValues0.add("");
        xValues0.add("12-07");


        //设置y轴的数据()
        List<List<Float>> yValues = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            List<Float> yValue = new ArrayList<>();
            for (int j = 0; j < xValues0.size(); j++) {
                yValue.add((float) (Math.random() * 8) + 2);
            }
            yValues.add(yValue);
        }


        //线的名字集合
        List<String> names = new ArrayList<>();
        names.add("本周数据");
        names.add("上周数据");
        //创建多条柱状的图表
        barChartManager2.showBarChart(xValues0, yValues, names);


       /* chartBarMulp.setOnChartValueSelectedListener(this);
        chartBarMulp.getDescription().setEnabled(false);

        chartBarMulp.setDrawBorders(false);

        // scaling can now only be done on x- and y-axis separately
        chartBarMulp.setPinchZoom(false);
        chartBarMulp.setScaleEnabled(false);
        chartBarMulp.setDragEnabled(true);
        chartBarMulp.setDrawBarShadow(false);

        chartBarMulp.setDrawGridBackground(false);
        //设置动画效果
        chartBarMulp.animateY(500, Easing.EasingOption.Linear);
        chartBarMulp.animateX(500, Easing.EasingOption.Linear);
        Legend l = chartBarMulp.getLegend();
        l.setForm(Legend.LegendForm.SQUARE);//图示 标签的形状。   正方形
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Light.ttf"));
        l.setYOffset(10f);
        l.setXOffset(0f);
        l.setYEntrySpace(20f);
        l.setTextSize(10f);
        l.setFormSize(12);

        XAxis xAxis = chartBarMulp.getXAxis();
        xAxis.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Light.ttf"));
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);// 是否绘制轴线
        xAxis.setDrawGridLines(false);//设置竖状的线是否显示
        xAxis.setAxisMinimum(0f);
        YAxis leftAxis = chartBarMulp.getAxisLeft();
        leftAxis.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Light.ttf"));
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setDrawAxisLine(false);// 是否绘制轴线
        chartBarMulp.getAxisRight().setEnabled(false);*/



        /* int groupCount = 7;
        int startYear = 1980;
        int endYear = startYear + groupCount;
        float groupSpace = 0.08f;
        float barSpace = 0.03f; // x4 DataSet
        float barWidth = 0.4f; // x4 DataSet

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        float randomMultiplier = 6 * 10f;
        for (int i = startYear; i < endYear; i++) {
            yVals1.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
            yVals2.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
        }

        BarDataSet set1, set2, set3, set4;

        if (chartBarMulp.getData() != null && chartBarMulp.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) chartBarMulp.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) chartBarMulp.getData().getDataSetByIndex(1);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            chartBarMulp.getData().notifyDataChanged();
            chartBarMulp.notifyDataSetChanged();

        } else {
            // create 4 DataSets
            set1 = new BarDataSet(yVals1, "本周数据");
            set1.setColor(Color.rgb(73, 169, 238));
            set2 = new BarDataSet(yVals2, "上周数据");
            set2.setColor(Color.rgb(152, 216, 125));

            BarData data = new BarData(set1, set2);
            data.setValueFormatter(new LargeValueFormatter());
            data.setValueTypeface(Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Light.ttf"));

            chartBarMulp.setData(data);
        }
        chartBarMulp.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        chartBarMulp.getXAxis().setAxisMinimum(startYear);
        chartBarMulp.getBarData().setDrawValues(false);//是dataSet的属性，设置是否在图上显示出当前点（柱状图）的值

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        chartBarMulp.getXAxis().setAxisMaximum(startYear + chartBarMulp.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        chartBarMulp.groupBars(startYear, groupSpace, barSpace);
        chartBarMulp.invalidate();*/
    }

    String startDate;
    String endDate;
    SimpleDateFormat format;
    public void showDatePickerStart() {
        format = new SimpleDateFormat("yyyy-MM-dd");
        datePicker = new DatePickDialog(mContext);
        //设置上下年分限制
        datePicker.setYearLimt(1);
        //设置标题
        datePicker.setTitle("开始时间");
        //设置类型
        datePicker.setType(DateType.TYPE_YMD);
        //设置消息体的显示格式，日期格式
        datePicker.setMessageFormat("yyyy-MM-dd");
        //设置选择回调
        datePicker.setOnChangeLisener(null);
        //设置点击确定按钮回调
        datePicker.setOnSureLisener(new OnSureLisener() {
            @Override
            public void onSure(final Date date) {

                if(TimeUtils.isDateOneBigger(date)){
                    Toast.makeText(getActivity(),"日期不能大于今天",Toast.LENGTH_SHORT).show();
                }else if(TimeUtils.isDateOneBiggerTwo(format.format(date),endDate)){
                    Toast.makeText(getActivity(),"日期不能大于结束时间",Toast.LENGTH_SHORT).show();
                }else{
                    startDate = format.format(date);
                    dateDoubleSelect.setText(startDate);
                    Log.e("TTTTT", "StartSetOnSureLisener=" +startDate);
                }

            }
        });
        if(startDate!=null){
            try {
                datePicker.setStartDate(format.parse(startDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.e("TTTTT", "StartSetStartDate=" + startDate.toString());
        }else{
            datePicker.setStartDate(new Date());
        }


        datePicker.show();
    }
    public void showDatePickerEnd() {
        format = new SimpleDateFormat("yyyy-MM-dd");
        datePicker = new DatePickDialog(mContext);
        //设置上下年分限制
        datePicker.setYearLimt(1);
        //设置标题
        datePicker.setTitle("结束时间");
        //设置类型
        datePicker.setType(DateType.TYPE_YMD);
        //设置消息体的显示格式，日期格式
        datePicker.setMessageFormat("yyyy-MM-dd");
        //设置选择回调
        datePicker.setOnChangeLisener(null);
        //设置点击确定按钮回调
        datePicker.setOnSureLisener(new OnSureLisener() {
            @Override
            public void onSure(final Date date) {
                if(TimeUtils.isDateOneBigger(date)){
                    Toast.makeText(getActivity(),"日期不能大于今天",Toast.LENGTH_SHORT).show();
                }else if(TimeUtils.isDateOneBiggerTwo(startDate,format.format(date))){
                    Toast.makeText(getActivity(),"日期不能小于开始时间",Toast.LENGTH_SHORT).show();
                }else{
                    endDate = format.format(date);
                    dateDoubleSelectEnd.setText(endDate);
                    Log.e("TTTTT", "EndSetOnSureLisener=" +endDate);
                }

            }
        });
        if(endDate!=null){
            try {
                datePicker.setStartDate(format.parse(endDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.e("TTTTT", "EndSetStartDate=" + endDate.toString());
        }else{
            datePicker.setStartDate(new Date());
        }


        datePicker.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        getContext().unbindService(mTransDataModel.mConnection);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void BackGroundEvent(MessageEvent messageEvent) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (messageEvent != null) {
            if (messageEvent.getEditQuery() != null && !messageEvent.getEditQuery().equals("")) {
                xiuzhenTariMoney.setText(messageEvent.getEditQuery());
                xiuzhenNumStr = messageEvent.getEditQuery();
                float editQuertInt = Float.parseFloat(messageEvent.getEditQuery());
                xiuzhenKeliuPrice.setText(editQuertInt / keliuNumInt + "");
            }
            if (messageEvent.getEditQueryBilie() != null && !messageEvent.getEditQueryBilie().equals("")) {
                xiuzhenBili.setText(messageEvent.getEditQueryBilie());
                xiuzhenBulieStr = messageEvent.getEditQueryBilie();
            }
            if (messageEvent.getEditQueryMianji() != null && !messageEvent.getEditQueryMianji().equals("")) {
                shanpuMianji.setText(messageEvent.getEditQueryMianji());
                zhandiMianji = messageEvent.getEditQueryMianji();
                //商铺坪效
                int mianji = Integer.parseInt(messageEvent.getEditQueryMianji());
                shanPuPinxiao.setText((moneyZong / mianji) + "");

            }


        }


    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        // 设置x轴的LimitLine，index是从0开始的
        LimitLine xLimitLine = new LimitLine(e.getX(), "" + (int) e.getX());
        xLimitLine.setLineColor(ColorTemplate.rgb("#000000"));
        xLimitLine.setLineWidth(1f);
        XAxis xAxis = chartBarMulp.getXAxis();
        xAxis.removeAllLimitLines();
        xAxis.addLimitLine(xLimitLine);


    }

    @Override
    public void onNothingSelected() {

    }

    @OnClick({R.id.chang_Tari_button, R.id.easy_mess_ke_num})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.chang_Tari_button:
                Intent intent = new Intent(getContext(), RevisedTurnoverActivity.class);
                intent.putExtra("xiuzhenNumStr", xiuzhenNumStr);
                intent.putExtra("moneyZong", moneyZong);
                intent.putExtra("xiuzhenBulieStr", xiuzhenBulieStr);
                intent.putExtra("zhandiMianji", zhandiMianji);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_anim3, R.anim.activity_out1);
                break;
            case R.id.easy_mess_ke_num:
                new EasyDialog(getContext())
                        .setLayoutResourceId(R.layout.layout_tip_text)
                        .setBackgroundColor(getContext().getResources().getColor(R.color.background_color_black))
                        .setLocationByAttachedView(question_mark_Keliu_num)
                        .setGravity(EasyDialog.GRAVITY_BOTTOM)
                        .setAnimationAlphaShow(600, 0.0f, 1.0f)
                        .setAnimationAlphaDismiss(600, 1.0f, 0.0f)
                        .setTouchOutsideDismiss(true)
                        .setMatchParent(false)
                        .setMarginLeftAndRight(24, 24)
                        .setOutsideColor(getContext().getResources().getColor(R.color.outside_color_trans))
                        .show();
                break;
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(getContext(), "今日", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(getContext(), "本周", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getContext(), "本月", Toast.LENGTH_SHORT).show();
                    break;
                case 55:
                    List<Bundle> dd = mTransDataModel.get();

                    moneyZong = 0f;
                    for (Bundle d : dd) {
                        moneyZong += Float.parseFloat(d.getString("transAmount"));
                        Log.d("ppp", moneyZong + "   " + d.getString("transName") + "  " + d.getInt("transCount") + "  " + d.getString("transAmount"));
                    }
                    tariMoneyZong.setText(moneyZong + "");
                    //客流单价
                    keliuNumInt = Integer.parseInt(keliuNum.getText().toString());

                    keliuPrice.setText((moneyZong / keliuNumInt) + "");
                    xiuzhenBulieStr = xiuzhenBili.getText().toString();
                    xiuzhenNumStr = xiuzhenTariMoney.getText().toString();
                    zhandiMianji = shanpuMianji.getText().toString();
                    break;
            }
        }
    };
}
