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
import com.ums.wifiprobe.app.ThreadPoolProxyFactory;
import com.ums.wifiprobe.data.DataResource;
import com.ums.wifiprobe.data.ProbeTotalDataRepository;
import com.ums.wifiprobe.eventbus.MessageEvent;
import com.ums.wifiprobe.service.greendao.MacTotalInfo;
import com.ums.wifiprobe.service.greendao.RssiInfo;
import com.ums.wifiprobe.ui.activity.RevisedTurnoverActivity;
import com.ums.wifiprobe.ui.customview.EasyDialog;
import com.ums.wifiprobe.utils.BarChartManager;
import com.ums.wifiprobe.utils.TimeUtils;
import com.ums.wifiprobe.utils.coupterUtil;

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
    @BindView(R.id.jiaoyiTongBiImage)
    ImageView jiaoyiTongBiImage;
    @BindView(R.id.jiaoyiTongBiText)
    TextView jiaoyiTongBiText;
    @BindView(R.id.keliuBiImage)
    ImageView keliuBiImage;
    @BindView(R.id.keliuBiText)
    TextView keliuBiText;
    @BindView(R.id.dangjiaBiImage)
    ImageView dangjiaBiImage;
    @BindView(R.id.dangjiaBiText)
    TextView dangjiaBiText;
    @BindView(R.id.chartLineTextBen)
    TextView chartLineTextBen;
    @BindView(R.id.chartLineTextShang)
    TextView chartLineTextShang;

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
    int keliuBi = 0;//客流数量百分比
    List<Integer> keliuWeekInt = new ArrayList<>();//上一周的每一天的客流数量
    private String curDate = TimeUtils.getDate(System.currentTimeMillis());
    private boolean isFistCreat=true;//是否是第一次onCreateView
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
//            initChart();
        } else {
            ViewGroup parent = (ViewGroup) mRootView.get().getParent();
            if (parent != null) {
                parent.removeView(mRootView.get());
            }
        }
        unbinder = ButterKnife.bind(this, mRootView.get());
        return mRootView.get();

    }

    @Override
    public void onResume() {
        super.onResume();
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
        startDate = endDate = sdf.format(new Date());
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

    private void initChart(List<Float> todaylist, List<Float> beforList, String type) {
        chartBarMulp.setOnChartValueSelectedListener(this);
        BarChartManager barChartManager2 = new BarChartManager(chartBarMulp, getContext());
        List<String> xValues0 = new ArrayList<>();
        //线的名字集合
        List<String> names = new ArrayList<>();
        switch (type) {
            case "day":
                chartLineTextBen.setText("今天数据");
                chartLineTextShang.setText("昨天数据");
                //设置x轴的数据

                xValues0.add("00:00");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("04:00");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("08:00");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("12:00");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("16:00");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("20:00");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("23:00");


                names.add("今天数据");
                names.add("昨天数据");

                break;
            case "week":
                chartLineTextBen.setText("本周数据");
                chartLineTextShang.setText("上周数据");
                //设置x轴的数据

                xValues0.add("周一");
                xValues0.add("周二");
                xValues0.add("周三");
                xValues0.add("周四");
                xValues0.add("周五");
                xValues0.add("周六");
                xValues0.add("周日");


                names.add("本周数据");
                names.add("上周数据");

                break;
            case "month":
                chartLineTextBen.setText("本月数据");
                chartLineTextShang.setText("上月数据");
                //设置x轴的数据

                xValues0.add("1日");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("5日");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("10日");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("15日");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("20日");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("25日");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("");
                xValues0.add("30日");



                names.add("本月数据");
                names.add("上月数据");
                break;
        }


        //设置y轴的数据()
        List<List<Float>> yValues = new ArrayList<>();
        yValues.add(todaylist);
        yValues.add(beforList);


        //创建多条柱状的图表
        barChartManager2.showBarChart(xValues0, yValues, names);
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

                if (TimeUtils.isDateOneBigger(date)) {
                    Toast.makeText(getActivity(), "日期不能大于今天", Toast.LENGTH_SHORT).show();
                } else if (TimeUtils.isDateOneBiggerTwo(format.format(date), endDate)) {
                    Toast.makeText(getActivity(), "日期不能大于结束时间", Toast.LENGTH_SHORT).show();
                } else {
                    startDate = format.format(date);
                    dateDoubleSelect.setText(startDate);
                    Log.e("TTTTT", "StartSetOnSureLisener=" + startDate);
                    handler.sendEmptyMessageDelayed(55, 0);
                }

            }
        });
        if (startDate != null) {
            try {
                datePicker.setStartDate(format.parse(startDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.e("TTTTT", "StartSetStartDate=" + startDate.toString());
        } else {
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
                if (TimeUtils.isDateOneBigger(date)) {
                    Toast.makeText(getActivity(), "日期不能大于今天", Toast.LENGTH_SHORT).show();
                } else if (TimeUtils.isDateOneBiggerTwo(startDate, format.format(date))) {
                    Toast.makeText(getActivity(), "日期不能小于开始时间", Toast.LENGTH_SHORT).show();
                } else {
                    endDate = format.format(date);
                    dateDoubleSelectEnd.setText(endDate);
                    Log.e("TTTTT", "EndSetOnSureLisener=" + endDate);
                }

            }
        });
        if (endDate != null) {
            try {
                datePicker.setStartDate(format.parse(endDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.e("TTTTT", "EndSetStartDate=" + endDate.toString());
        } else {
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
                if (keliuNumInt != 0) {
                    xiuzhenKeliuPrice.setText(editQuertInt / keliuNumInt + "");
                }

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
                if (mianji != 0) {
                    shanPuPinxiao.setText((moneyZong / mianji) + "");
                }


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


    final List<Double> DaYhour24KeliuList = new ArrayList<>();//今天一天24小时的客流量
    final List<Double> Lasthour24KeliuList = new ArrayList<>();//昨天一天24小时的客流量

    final List<Double> Week7KeliuList = new ArrayList<>();//本周7天的客流量
    final List<Double> LastWeek7KeliuList = new ArrayList<>();//上周7天的客流量

    final List<Double> Month30KeliuList = new ArrayList<>();//本月30天的客流量
    final List<Double> LastMonth30KeliuList = new ArrayList<>();//上个月30天的客流量

    final List<String> monthDateStrList=new ArrayList<>();//本月30天的日期字符串list
    final List<String> LastmonthDateStrList=new ArrayList<>();//上个月30天的日期字符串list


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
//                    initChart();
                    //获取ListView数据
                    //获取Total数据-----同barData
                    ThreadPoolProxyFactory.getQueryThreadPoolProxy().execute(new Runnable() {
                        @Override
                        public void run() {
                            ProbeTotalDataRepository.getInstance().getTasks(TimeUtils.getWeekDate(curDate), "day", curDate, new DataResource.LoadTasksCallback<MacTotalInfo>() {

                                @Override
                                public void onTasksLoaded(List<MacTotalInfo> list) {
                                    if (list != null && list.size() > 0) {
                                        for (int i = 0; i < list.size(); i++) {
                                            int curValue = 0;

                                            List<RssiInfo> nowRssiInfos = list.get(i).getRssiInfos();

                                            if (nowRssiInfos != null && nowRssiInfos.size() > 0) {
                                                for (RssiInfo info : nowRssiInfos) {
                                                    if (info.getMinRssi() == -1000 && info.getMaxRssi() == 0 && info.getIsDistinct()) {
                                                        curValue += info.getTotaNumber();
                                                    }
                                                }
                                            }
                                            DaYhour24KeliuList.add(Double.parseDouble(curValue + ""));
                                            Log.d("DaYhour24KeliuList", list.get(i).getScaleValue() + "   " + curValue + "  " + "  " + list.get(i).getDate());
                                        }
                                    }
                                }

                                @Override
                                public void onDataNotAvaliable() {

                                }
                            });
                        }
                    });

                    ThreadPoolProxyFactory.getQueryThreadPoolProxy().execute(new Runnable() {
                        @Override
                        public void run() {
                            ProbeTotalDataRepository.getInstance().getTasks(TimeUtils.getWeekDate(curDate), "day", TimeUtils.getDefineDayAgoDate(TimeUtils.getTimeMillions(curDate), 1), new DataResource.LoadTasksCallback<MacTotalInfo>() {

                                @Override
                                public void onTasksLoaded(List<MacTotalInfo> list) {
                                    if (list != null && list.size() > 0) {
                                        for (int i = 0; i < list.size(); i++) {
                                            int curValue = 0;

                                            List<RssiInfo> nowRssiInfos = list.get(i).getRssiInfos();

                                            if (nowRssiInfos != null && nowRssiInfos.size() > 0) {
                                                for (RssiInfo info : nowRssiInfos) {
                                                    if (info.getMinRssi() == -1000 && info.getMaxRssi() == 0 && info.getIsDistinct()) {
                                                        curValue += info.getTotaNumber();
                                                    }
                                                }
                                            }
                                            Lasthour24KeliuList.add(Double.parseDouble(curValue + ""));
                                            Log.d("Lasthour24KeliuList", list.get(i).getScaleValue() + "   " + curValue + "  " + "  " + list.get(i).getDate());
                                        }
                                    }
                                }

                                @Override
                                public void onDataNotAvaliable() {

                                }
                            });
                        }
                    });

                    List<Float> Today24Keliudangjie = new ArrayList<>();
                    List<Bundle> hourList = mTransDataModel.gethour(curDate + " 00:00:00", curDate + " 23:59:00");
                    if (hourList != null && DaYhour24KeliuList != null) {
                        for (int h = 0; h < hourList.size(); h++) {
                            Double todayAmount = Double.parseDouble(hourList.get(h).getString("transAmount"));

                            if (DaYhour24KeliuList.get(h) != 0) {
                                Double dd = todayAmount / DaYhour24KeliuList.get(h);
                                Log.d("totttt", " >>>>" + dd + " " + DaYhour24KeliuList.get(h));
                                Today24Keliudangjie.add(Float.parseFloat(dd + ""));

                            } else {
                                Today24Keliudangjie.add(0f);
                                Log.d("totttt", " >>>>" + 0.0 + " ");
                            }
                            Log.d("hourList", hourList.get(h).getString("transAmount") + " " + TimeUtils.stampToDate(hourList.get(h).getLong("start_time") + "") + "  " + TimeUtils.stampToDate(hourList.get(h).getLong("end_time") + ""));
                        }
                    }
                    for (Float f : Today24Keliudangjie) {
                        Log.d("hourListTwwwww", f + "");
                    }
                    List<Float> Beday24Keliudangjie = new ArrayList<>();
                    String dayBeforeStr = TimeUtils.getBeforDay(curDate);
                    List<Bundle> hourBeforList = mTransDataModel.gethour(dayBeforeStr + " 00:00:00", dayBeforeStr + " 23:59:00");
                    if (hourBeforList != null && Lasthour24KeliuList != null) {
                        for (int l = 0; l < hourBeforList.size(); l++) {
                            Double todayAmount = Double.parseDouble(hourBeforList.get(l).getString("transAmount"));

                            if (Lasthour24KeliuList.get(l) != 0) {
                                Double dd = todayAmount / Lasthour24KeliuList.get(l);
                                Log.d("toLLLLLLL", " >>>>" + dd + " " + Lasthour24KeliuList.get(l));
                                Beday24Keliudangjie.add(Float.parseFloat(dd + ""));

                            } else {
                                Beday24Keliudangjie.add(0f);
                                Log.d("toLLLLLLL", " >>>>" + 0.0 + " ");
                            }
                            Log.d("hourBeforList", hourBeforList.get(l).getString("transAmount") + " " + TimeUtils.stampToDate(hourBeforList.get(l).getLong("start_time") + "") + "  " + TimeUtils.stampToDate(hourBeforList.get(l).getLong("end_time") + ""));
                        }

                    }
                    for (Float f : Beday24Keliudangjie) {
                        Log.d("hourBeforListTwwwww", f + "");
                    }
                    initChart(Today24Keliudangjie, Beday24Keliudangjie, "day");

                    break;
                case 1:

                    ThreadPoolProxyFactory.getQueryThreadPoolProxy().execute(new Runnable() {
                        @Override
                        public void run() {
                            String scaleValue = TimeUtils.getYear(TimeUtils.getTimeMillions(curDate)) + "-" + TimeUtils.getWeeksOfYear(curDate);
                            ProbeTotalDataRepository.getInstance().getTasks(scaleValue, "week", curDate, new DataResource.LoadTasksCallback<MacTotalInfo>() {

                                @Override
                                public void onTasksLoaded(List<MacTotalInfo> list) {
                                    if (list != null && list.size() > 0) {
                                        for (int i = 0; i < list.size(); i++) {
                                            int curValue = 0;

                                            List<RssiInfo> nowRssiInfos = list.get(i).getRssiInfos();

                                            if (nowRssiInfos != null && nowRssiInfos.size() > 0) {
                                                for (RssiInfo info : nowRssiInfos) {
                                                    if (info.getMinRssi() == -1000 && info.getMaxRssi() == 0 && info.getIsDistinct()) {
                                                        curValue += info.getTotaNumber();
                                                    }
                                                }
                                            }
                                            Week7KeliuList.add(Double.parseDouble(curValue + ""));
                                            Log.d("Week7KeliuList", list.get(i).getScaleValue() + "   " + curValue + "  " + "  " + list.get(i).getDate());
                                        }
                                    }
                                }

                                @Override
                                public void onDataNotAvaliable() {

                                }
                            });
                        }
                    });

                    ThreadPoolProxyFactory.getQueryThreadPoolProxy().execute(new Runnable() {
                        @Override
                        public void run() {
                            String scaleValue = TimeUtils.getYear(TimeUtils.getTimeMillions(TimeUtils.getSundayDate(curDate, -1))) + "-" + (TimeUtils.getWeeksOfYear(curDate) - 1);
                            ProbeTotalDataRepository.getInstance().getTasks(scaleValue, "week", TimeUtils.getSundayDate(curDate, -1), new DataResource.LoadTasksCallback<MacTotalInfo>() {

                                @Override
                                public void onTasksLoaded(List<MacTotalInfo> list) {
                                    if (list != null && list.size() > 0) {
                                        for (int i = 0; i < list.size(); i++) {
                                            int curValue = 0;

                                            List<RssiInfo> nowRssiInfos = list.get(i).getRssiInfos();

                                            if (nowRssiInfos != null && nowRssiInfos.size() > 0) {
                                                for (RssiInfo info : nowRssiInfos) {
                                                    if (info.getMinRssi() == -1000 && info.getMaxRssi() == 0 && info.getIsDistinct()) {
                                                        curValue += info.getTotaNumber();
                                                    }
                                                }
                                            }
                                            LastWeek7KeliuList.add(Double.parseDouble(curValue + ""));
                                            Log.d("LastWeek7KeliuList", list.get(i).getScaleValue() + "   " + curValue + "  " + "  " + list.get(i).getDate());
                                        }
                                    }
                                }

                                @Override
                                public void onDataNotAvaliable() {

                                }
                            });
                        }
                    });
                    List<String >  dste = TimeUtils.getTimeIntervallList(curDate);
                    List<Double> benWeekJiaoyiList =new ArrayList<>();
                    for(String  ds:dste){
                        benWeekJiaoyiList.add(coupterUtil.toDayAmountZong(mTransDataModel,ds));
                        Log.d("benWeekJiaoyiList", coupterUtil.toDayAmountZong(mTransDataModel,ds)+ "  ");
                    }
                  List<Float> Week7Keliudangjie = new ArrayList<>();


                    if (benWeekJiaoyiList != null && Week7KeliuList != null) {
                        for (int h = 0; h < benWeekJiaoyiList.size(); h++) {


                            if (Week7KeliuList.get(h) != 0) {
                                Double dd = benWeekJiaoyiList.get(h) / Week7KeliuList.get(h);
                                Log.d("wwwttttt", " >>>>" + dd + " " + Week7KeliuList.get(h));
                                Week7Keliudangjie.add(Float.parseFloat(dd + ""));

                            } else {
                                Week7Keliudangjie.add(0f);
                                Log.d("wwwttttt", " >>>>" + 0.0 + " ");
                            }
                            Log.d("benWeekJiaoyiList","");
                        }
                    }
                    for (Float f : Week7Keliudangjie) {
                        Log.d("benWeekJiaoyiListTwwwww", f + "");
                    }

                    List<String >  lastDste = TimeUtils.getLastWeekIntervalArray(curDate);
                    List<Double> LastWeekJiaoyiList =new ArrayList<>();
                    for(String  ds:lastDste){
                        LastWeekJiaoyiList.add(coupterUtil.toDayAmountZong(mTransDataModel,ds));
                        Log.d("LastWeekJiaoyiList", coupterUtil.toDayAmountZong(mTransDataModel,ds)+ "  ");
                    }
                    List<Float> LastWeek7Keliudangjie = new ArrayList<>();


                    if (LastWeekJiaoyiList != null && LastWeek7KeliuList != null) {
                        for (int h = 0; h < LastWeekJiaoyiList.size(); h++) {


                            if (LastWeek7KeliuList.get(h) != 0) {
                                Double dd = LastWeekJiaoyiList.get(h) / LastWeek7KeliuList.get(h);
                                Log.d("wwwllllll", " >>>>" + dd + " " + LastWeek7KeliuList.get(h));
                                LastWeek7Keliudangjie.add(Float.parseFloat(dd + ""));

                            } else {
                                LastWeek7Keliudangjie.add(0f);
                                Log.d("wwwllllll", " >>>>" + 0.0 + " ");
                            }
                            Log.d("LastWeekJiaoyiList","");
                        }
                    }
                    for (Float f : LastWeek7Keliudangjie) {
                        Log.d("LastWeek7Kelijiewwwww", f + "");
                    }
                    initChart(Week7Keliudangjie, LastWeek7Keliudangjie, "week");
                    break;
                case 2:
                    ThreadPoolProxyFactory.getQueryThreadPoolProxy().execute(new Runnable() {
                        @Override
                        public void run() {
                            String scaleValue = TimeUtils.getYear(TimeUtils.getTimeMillions(curDate)) + "-" + TimeUtils.getMonthsOfYear(curDate);
                            ProbeTotalDataRepository.getInstance().getTasks(scaleValue, "month", curDate, new DataResource.LoadTasksCallback<MacTotalInfo>() {

                                @Override
                                public void onTasksLoaded(List<MacTotalInfo> list) {
                                    if (list != null && list.size() > 0) {
                                        for (int i = 0; i < list.size(); i++) {
                                            int curValue = 0;

                                            List<RssiInfo> nowRssiInfos = list.get(i).getRssiInfos();

                                            if (nowRssiInfos != null && nowRssiInfos.size() > 0) {
                                                for (RssiInfo info : nowRssiInfos) {
                                                    if (info.getMinRssi() == -1000 && info.getMaxRssi() == 0 && info.getIsDistinct()) {
                                                        curValue += info.getTotaNumber();
                                                    }
                                                }
                                            }
                                            Month30KeliuList.add(Double.parseDouble(curValue + ""));
                                            monthDateStrList.add(list.get(i).getScaleValue());
                                            Log.d("Month30KeliuList", list.get(i).getScaleValue() + "   " + curValue + "  " + "  " + list.get(i).getDate());
                                        }
                                        sendEmptyMessage(211);
                                    }
                                }

                                @Override
                                public void onDataNotAvaliable() {
                                    Log.d("Month30KeliuList","onDataNotAvaliable");
                                }
                            });
                        }
                    });



                    break;
                case 211:
                    ThreadPoolProxyFactory.getQueryThreadPoolProxy().execute(new Runnable() {
                        @Override
                        public void run() {
                            int lastMonth = TimeUtils.getDefineMonthAgo(curDate, -1);
                            String lastMonthData = TimeUtils.getDefineMonthAgoDate(curDate, -1);
                            String scaleValue = TimeUtils.getYear(TimeUtils.getTimeMillions(lastMonthData)) + "-" + lastMonth;
                            ProbeTotalDataRepository.getInstance().getTasks(scaleValue, "month", lastMonthData, new DataResource.LoadTasksCallback<MacTotalInfo>() {

                                @Override
                                public void onTasksLoaded(List<MacTotalInfo> list) {
                                    if (list != null && list.size() > 0) {
                                        for (int i = 0; i < list.size(); i++) {
                                            int curValue = 0;

                                            List<RssiInfo> nowRssiInfos = list.get(i).getRssiInfos();

                                            if (nowRssiInfos != null && nowRssiInfos.size() > 0) {
                                                for (RssiInfo info : nowRssiInfos) {
                                                    if (info.getMinRssi() == -1000 && info.getMaxRssi() == 0 && info.getIsDistinct()) {
                                                        curValue += info.getTotaNumber();
                                                    }
                                                }
                                            }
                                            LastMonth30KeliuList.add(Double.parseDouble(curValue + ""));
                                            LastmonthDateStrList.add(list.get(i).getScaleValue());
                                            Log.d("LastMonth30KeliuList", list.get(i).getScaleValue() + "   " + curValue + "  " + "  " + list.get(i).getDate());
                                        }
                                        sendEmptyMessage(212);
                                    }
                                }

                                @Override
                                public void onDataNotAvaliable() {

                                }
                            });
                        }
                    });

                    break;
                case 212:
                    List<Double> benMonthJiaoyiList =new ArrayList<>();
                    Log.d("benMonthJiaoyiList", Month30KeliuList.size()+"   "+monthDateStrList.size()+ ">>>>  ");
                    for(String  ds:monthDateStrList){
                        Double  transAmout= coupterUtil.toDayAmountZong(mTransDataModel,ds);
                        benMonthJiaoyiList.add(transAmout);
                    Log.d("benMonthJiaoyiList", transAmout+"");
                    }
                   List<Float> Month30Keliudangjie = new ArrayList<>();


                    if (benMonthJiaoyiList != null && Month30KeliuList != null) {
                        for (int h = 0; h < benMonthJiaoyiList.size(); h++) {


                            if (Month30KeliuList.get(h) != 0) {
                                Double dd = benMonthJiaoyiList.get(h) / Month30KeliuList.get(h);
                                Log.d("wwwMMMM", " >>>>" + dd + " " + Month30KeliuList.get(h));
                                Month30Keliudangjie.add(Float.parseFloat(dd + ""));

                            } else {
                                Month30Keliudangjie.add(0f);
                                Log.d("wwwMMMM", " >>>>" + 0.0 + " ");
                            }
                            Log.d("Month30Keliudangjie","");
                        }
                    }
                    for (Float f : Month30Keliudangjie) {
                        Log.d("Month30KeliudajieTwwwww", f + "");
                    }


                    List<Double> LastMonthJiaoyiList =new ArrayList<>();
                    Log.d("LastMonthJiaoyiList", LastMonth30KeliuList.size()+"   "+LastmonthDateStrList.size()+ ">>>>  ");
                    for(String  ds:LastmonthDateStrList){
                        Double  transAmout= coupterUtil.toDayAmountZong(mTransDataModel,ds);
                        LastMonthJiaoyiList.add(transAmout);
                        Log.d("LastMonthJiaoyiList", transAmout+"");
                    }
                    List<Float> LastMonth30Keliudangjie = new ArrayList<>();


                    if (LastMonthJiaoyiList != null && LastMonth30KeliuList != null) {
                        for (int h = 0; h < LastMonthJiaoyiList.size(); h++) {


                            if (LastMonth30KeliuList.get(h) != 0) {
                                Double dd = LastMonthJiaoyiList.get(h) / LastMonth30KeliuList.get(h);
                                Log.d("wwwMttttt", " >>>>" + dd + " " + LastMonth30KeliuList.get(h));
                                LastMonth30Keliudangjie.add(Float.parseFloat(dd + ""));

                            } else {
                                LastMonth30Keliudangjie.add(0f);
                                Log.d("LastMonth30Keliudangjie", " >>>>" + 0.0 + " ");
                            }
                            Log.d("LastMonth30Keliudangjie","");
                        }
                    }
                    for (Float f : LastMonth30Keliudangjie) {
                        Log.d("LastMonthdangjieTwwwww", f + "");
                    }

                    initChart(Month30Keliudangjie, LastMonth30Keliudangjie, "month");
                    break;
                case 55:
                    List<Bundle> dd = mTransDataModel.get(startDate + " 00:00:00", startDate + " 23:59:00");
                    moneyZong = 0f;
                    for (Bundle d : dd) {
                        moneyZong += Float.parseFloat(d.getString("transAmount"));
                        Log.d("ppp", d.getString("transAmount"));
                    }
                    String startAndEnd = TimeUtils.getLastTimeInterval(startDate);
                    String[] ss = startAndEnd.split(",");
                    Log.d("www", ss[0] + "  " + ss[1]);
                    List<Bundle> dayWeek = mTransDataModel.get(ss[0] + " 00:00:00", ss[1] + " 23:59:00");
                    float weekZong = 0f;
                    for (Bundle d : dayWeek) {
                        weekZong += Float.parseFloat(d.getString("transAmount"));
                        Log.d("www", d.getString("transAmount"));
                    }

                    float weekZhi = weekZong / 7;

                    float baidfiWeek = 0f;
                    if (weekZhi != 0) {
                        baidfiWeek = (moneyZong / weekZhi - 1) * 100;
                    } else {
                        baidfiWeek = moneyZong;//当上周总交易额为零，取当前交易额
                    }

                    Log.d("www", weekZong + "" + baidfiWeek + "%");
                    if (baidfiWeek > 0) {
                        jiaoyiTongBiImage.setBackgroundResource(R.mipmap.icon_arr_above);
                        jiaoyiTongBiText.setTextColor(getResources().getColor(R.color.smalNumTextGree));
                        jiaoyiTongBiText.setText(baidfiWeek + "%");

                    } else {
                        jiaoyiTongBiImage.setBackgroundResource(R.mipmap.icon_arr_bottom);
                        jiaoyiTongBiText.setTextColor(getResources().getColor(R.color.smalNumTextRed));
                        jiaoyiTongBiText.setText(baidfiWeek + "%");
                    }
                    tariMoneyZong.setText(moneyZong + "");

                    xiuzhenBulieStr = xiuzhenBili.getText().toString();
                    xiuzhenNumStr = xiuzhenTariMoney.getText().toString();
                    zhandiMianji = shanpuMianji.getText().toString();

                    ThreadPoolProxyFactory.getQueryThreadPoolProxy().execute(new Runnable() {
                        @Override
                        public void run() {

                            ProbeTotalDataRepository.getInstance().getTask(startDate, "days", startDate, new DataResource.GetTaskCallback<MacTotalInfo>() {
                                @Override
                                public void OnTaskLoaded(MacTotalInfo infoRe) {
                                    int customerNumber = 0;
                                    List<RssiInfo> rssiInfos = infoRe.getRssiInfos();
                                    if (rssiInfos != null && rssiInfos.size() > 0) {
                                        for (RssiInfo info : rssiInfos) {
                                            if (info.getMinRssi() == -1000 && info.getMaxRssi() == 0 && info.getIsDistinct()) {

                                                customerNumber += info.getTotaNumber();
                                            }
                                        }
                                    }
                                    keliuNumInt = customerNumber;
                                    Log.d("DayNowKeliu", "一天的客流量" + customerNumber + "");
                                    sendEmptyMessage(77);
                                }

                                @Override
                                public void onDataNotAvaliable() {

                                    Log.d("DayNowKeliu", "totalView data load failure !");
                                }
                            });
                        }
                    });


                    break;
                case 66:
                    //客流单价
                    keliuNum.setText(keliuNumInt + "");
                    if (keliuNumInt != 0) {
                        keliuPrice.setText((moneyZong / keliuNumInt) + "");
                    } else {
                        keliuPrice.setText("0");
                    }

                    if (keliuBi > 0) {
                        keliuBiImage.setBackgroundResource(R.mipmap.icon_arr_above);
                        keliuBiText.setTextColor(getResources().getColor(R.color.smalNumTextGree));
                        keliuBiText.setText(keliuBi + "%");
                    } else {
                        keliuBiImage.setBackgroundResource(R.mipmap.icon_arr_bottom);
                        keliuBiText.setTextColor(getResources().getColor(R.color.smalNumTextRed));
                        keliuBiText.setText(keliuBi + "%");

                    }
                    List<String> weekListStr = TimeUtils.getLastWeekIntervalArray(startDate);
                    List<Double> weektranMoney = new ArrayList<>();//上周每一天的交易额
                    for (String ww : weekListStr) {
                        weektranMoney.add(coupterUtil.toDayAmountZong(mTransDataModel, ww));
                        Log.d("WeekStr", ww + "  " + coupterUtil.toDayAmountZong(mTransDataModel, ww));
                    }
                    for (Integer ii : keliuWeekInt) {
                        Log.d("WeekStrInt", ii + "  ");
                    }
                    List<Double> keliuPriceWeek = new ArrayList<>();//上周每一天的客流单价
                    Double keliuZongWeek = 0.0;
                    for (int w = 0; w < 7; w++) {
                        if (keliuWeekInt.get(w) != 0) {
                            keliuPriceWeek.add(weektranMoney.get(w) / keliuWeekInt.get(w));
                            keliuZongWeek += weektranMoney.get(w) / keliuWeekInt.get(w);
                            Log.d("WeekStrPrice", weektranMoney.get(w) / keliuWeekInt.get(w) + "  ");
                        } else {
                            keliuPriceWeek.add(0.00);
                            keliuZongWeek += 0.00;
                        }


                    }
                    Double kePriceBili = 0.0;
                    if (keliuNumInt != 0 && (keliuZongWeek / 7) != 0) {
                        Log.d("WeekStrPricezong", (moneyZong / keliuNumInt) + "   " + (keliuZongWeek / 7) + "");
                        kePriceBili = ((moneyZong / keliuNumInt) / (keliuZongWeek / 7) - 1) * 100;

                    } else {
                        kePriceBili = -100d;
                    }
                    if (kePriceBili > 0) {
                        dangjiaBiImage.setBackgroundResource(R.mipmap.icon_arr_above);
                        dangjiaBiText.setTextColor(getResources().getColor(R.color.smalNumTextGree));
                        dangjiaBiText.setText(kePriceBili + "%");
                    } else {
                        dangjiaBiImage.setBackgroundResource(R.mipmap.icon_arr_bottom);
                        dangjiaBiText.setTextColor(getResources().getColor(R.color.smalNumTextRed));
                        dangjiaBiText.setText(kePriceBili + "%");

                    }
                    Log.d("WeekStrPricezong", kePriceBili + "");

                    Log.d("WeekStrPricezong", keliuZongWeek + "");


                    if(isFistCreat){
                        handler.sendEmptyMessage(0);//客流交易趋势图
                        isFistCreat=false;
                    }


                    break;
                case 77:
                    ThreadPoolProxyFactory.getQueryThreadPoolProxy().execute(new Runnable() {
                        @Override
                        public void run() {
                            String scaleValue = TimeUtils.getYear(TimeUtils.getTimeMillions(TimeUtils.getSundayDate(startDate, -1))) + "-" + (TimeUtils.getWeeksOfYear(startDate) - 1);
                            ProbeTotalDataRepository.getInstance().getTasks(scaleValue, "week", TimeUtils.getSundayDate(startDate, -1), new DataResource.LoadTasksCallback<MacTotalInfo>() {

                                @Override
                                public void onTasksLoaded(List<MacTotalInfo> list) {
                                    int curValue = 0;
                                    keliuWeekInt.clear();
                                    for (int i = 0; i < list.size(); i++) {
                                        List<RssiInfo> nowRssiInfos = list.get(i).getRssiInfos();
                                        int onDayKeliNu = 0;
                                        if (nowRssiInfos != null && nowRssiInfos.size() > 0) {
                                            for (RssiInfo info : nowRssiInfos) {
                                                if (info.getMinRssi() == -1000 && info.getMaxRssi() == 0 && info.getIsDistinct()) {
                                                    curValue += info.getTotaNumber();
                                                    onDayKeliNu += info.getTotaNumber();
                                                }
                                            }
                                        }
                                        keliuWeekInt.add(onDayKeliNu);
                                    }
                                    if (curValue != 0) {
                                        keliuBi = (keliuNumInt / (curValue / 7) - 1) * 100;
                                    } else {
                                        keliuBi = keliuNumInt;//当上周客流总和为零时，取客流数量为百分比
                                    }


                                    Log.d("WeekLast", list.size() + " >>>" + curValue + "  ");
                                    sendEmptyMessage(66);
                                }

                                @Override
                                public void onDataNotAvaliable() {

                                    Log.d("WeekLast", "pre customerBarChart data load failure !");
                                }
                            });
                        }
                    });
                    break;
            }
        }
    };
}
