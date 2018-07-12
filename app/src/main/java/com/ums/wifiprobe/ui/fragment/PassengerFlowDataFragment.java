package com.ums.wifiprobe.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.ums.wifiprobe.AppProtocolInfo;
import com.ums.wifiprobe.R;
import com.ums.wifiprobe.app.DataBaseInitWorkTask;
import com.ums.wifiprobe.app.GlobalValueManager;
import com.ums.wifiprobe.service.ProbeService;
import com.ums.wifiprobe.service.greendao.MacTotalInfo;
import com.ums.wifiprobe.service.greendao.RssiInfo;
import com.ums.wifiprobe.ui.ComparisonInfo;
import com.ums.wifiprobe.ui.CustomerInfoShowContract;
import com.ums.wifiprobe.ui.ShowAllListView;
import com.ums.wifiprobe.ui.activity.CustomerInfoActivity;
import com.ums.wifiprobe.ui.activity.ProbeDetailActivity;
import com.ums.wifiprobe.ui.activity.SettingActivity;
import com.ums.wifiprobe.ui.activity.SplashActivity;
import com.ums.wifiprobe.ui.adapter.CustomerAdapter;
import com.ums.wifiprobe.ui.customview.MyLinearLayout;
import com.ums.wifiprobe.ui.customview.OnDialogCloseListener;
import com.ums.wifiprobe.ui.formatter.DayHoursAxisValueFormatter;
import com.ums.wifiprobe.ui.formatter.MonthAxisValueFormatter;
import com.ums.wifiprobe.ui.formatter.WeekAxisValueFormatter;
import com.ums.wifiprobe.ui.presenter.CustomerInfoPresenter;
import com.ums.wifiprobe.update.AppUpdateInfo;
import com.ums.wifiprobe.update.GLSUpdateInfo;
import com.ums.wifiprobe.update.GLUpdateInfo;
import com.ums.wifiprobe.utils.CheckApkExist;
import com.ums.wifiprobe.utils.DialogUtil;
import com.ums.wifiprobe.utils.LocalDisplay;
import com.ums.wifiprobe.utils.TimeUtils;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.os.Handler;


import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.badgeview.BGABadgeImageButton;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * Created by Administrator on 2015/11/30.
 */
public class PassengerFlowDataFragment extends Fragment implements View.OnClickListener, OnChartValueSelectedListener, CustomerInfoShowContract.View  {
    protected WeakReference<View> mRootView;
    private View view;
    String TAG="PassengerFlowData--Fragment";
    @BindView(R.id.tv_daydata)
    TextView tvDayData;
    @BindView(R.id.tv_weekdata)
    TextView tvWeekData;
    @BindView(R.id.tv_monthdata)
    TextView tvMonthData;

    @BindView(R.id.iv_dayline)
    ImageView ivDayline;
    @BindView(R.id.iv_weekline)
    ImageView ivWeekline;
    @BindView(R.id.iv_monthline)
    ImageView ivMonthline;

    @BindView(R.id.ibtn_predate)
    ImageButton ibtnPreData;
    @BindView(R.id.ibtn_nextdate)
    ImageButton ibtnNextDate;
    @BindView(R.id.tv_curdate)
    TextView tvCurDate;
    @BindView(R.id.tv_timeselected)
    TextView tvCurTime;
    @BindView(R.id.tv_valueselected)
    TextView tvCurValue;

    @BindView(R.id.tv_timeselected1)
    TextView tvCurTime1;
    @BindView(R.id.tv_customer_value)
    TextView tvCurValue1;

    @BindView(R.id.tv_customer_old)
    TextView tvCustomerOld;
    @BindView(R.id.tv_customer_new)
    TextView tvCustomerNew;

    @BindView(R.id.tv_distance_close)
    TextView tvDistanceClose;
    @BindView(R.id.tv_distance_middle)
    TextView tvDistanceMiddle;
    @BindView(R.id.tv_distance_far)
    TextView tvDistanceFar;
    @BindView(R.id.chart_bar)
    BarChart barChart;
    @BindView(R.id.chart_bar2)
    BarChart barChart2;
    @BindView(R.id.chart_pie)
    PieChart pieChart;
    @BindView(R.id.tv_number_customer)
    TextView tvCustomNumber;
    @BindView(R.id.tv_number_newcustomer)
    TextView tvNewCustomNumber;
    @BindView(R.id.tv_number_oldcustomer)
    TextView tvOldCustomNumber;

    @BindView(R.id.linearlayout_chartlegend)
    LinearLayout legendLayout;

    @BindView(R.id.linearlayout_customer)
    LinearLayout linearLayoutTotalCustomer;
    @BindView(R.id.linearlayout_oldcustomer)
    LinearLayout linearLayoutOldCustomer;
    @BindView(R.id.linearlayout_newcustomer)
    LinearLayout linearLayoutNewCustomer;

    @BindView(R.id.linearlayout_timepick)
    LinearLayout linearLayoutTimePicker;

    @BindView(R.id.linearlayout_fiveminiute)
    LinearLayout linearLayoutFiveminute;

    @BindView(R.id.tv_number_fiveminute)
    TextView tvFiveminute;

    @BindView(R.id.lv_comparison)
    ShowAllListView listView;



    private CustomerAdapter.TitleViewHolder title;
    private CustomerAdapter adapter;

    @BindView(R.id.rotate_header_linerlayout_frame)
    PtrClassicFrameLayout mPtrFrame;

    BGABadgeImageButton bgaBadgeImageButton;

    private final static String[] hours = new String[]{"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
    private final static String[] weekDays = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private final static String[] monthDays = new String[]{"1日", "2日", "3日", "4日", "5日", "6日", "7日", "8日", "9日", "10日", "11日", "12日", "13日", "14日", "15日", "16日", "17日", "18日", "19日", "20日", "21日", "22日", "23日", "24日", "25日", "26日", "27日", "28日", "29日", "30日", "31日"};

    private String curType = "day";//当前时间点类型 day/week/month
    private String curDate = TimeUtils.getDate(System.currentTimeMillis());
    private int curWeek = TimeUtils.getWeeksOfYear(curDate);
    private int curMonth = TimeUtils.getMonthsOfYear(curDate);
    private int curMonthYear = TimeUtils.getYear(System.currentTimeMillis());//月数据的年份
    private int curWeekYear = TimeUtils.getYear(System.currentTimeMillis());//周数据的年份
    private CustomerInfoPresenter presenter;

    private Entry dayEntry1, dayEntry2, weekEntry1, weekEntry2, monthEntry1, monthEntry2;

    private Handler mHandler;
    private Runnable firstOpenRunnable;
    private int times;//向服务查询次数
    private DatePickDialog datePicker;
    boolean isFirst = true;
    Context mContext;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null || mRootView.get() == null) {
            view = inflater.inflate(R.layout.fragment_customer, null);
            mRootView = new WeakReference<View>(view);
            mContext=getContext();
            ButterKnife.bind(this,view);
            checkDatabase();
            initView();
            initData();
        } else {
            ViewGroup parent = (ViewGroup) mRootView.get().getParent();
            if (parent != null) {
                parent.removeView(mRootView.get());
            }
        }
        return mRootView.get();

    }
    private DataBaseInitWorkTask mDataBaseInitWorkTask;
    private void checkDatabase() {
        if (!GlobalValueManager.getInstance().isCheckDatabase(System.currentTimeMillis())) {
            mDataBaseInitWorkTask = new DataBaseInitWorkTask();
            mDataBaseInitWorkTask.execute();
        }
    }

    private void initView() {
        adapter = new CustomerAdapter(mContext, null);
        title = new CustomerAdapter.TitleViewHolder(listView.getContext());
        addTitle("day");
        listView.setAdapter(adapter);

        mHandler = new Handler();
        firstOpenRunnable = new Runnable() {
            @Override
            public void run() {
                if (GlobalValueManager.getInstance().isFirstOpenApp() && times <= 20) {
                    mHandler.postDelayed(this, 500);
                    times++;
                } else {
                    times = 0;
                    hideLoadingDialog();
                    checkCompatibility();
                }
            }
        };

        //判断本APP是否第一次打开,第一次打开的时候，由于服务未曾运行过，故要先等服务检查完设备信息后，才能更新界面信息
        boolean isFirstOpen = GlobalValueManager.getInstance().isFirstOpenApp();
        Log.d(TAG, "is App first open :" + isFirstOpen);
        if (isFirstOpen) {
            showLoadingDialog();
            Intent mIntent = new Intent(mContext, ProbeService.class);
            mContext.startService(mIntent);
            mHandler.postDelayed(firstOpenRunnable, 500);
        } else {
            checkCompatibility();
        }

        tvDayData.setOnClickListener(this);
        tvWeekData.setOnClickListener(this);
        tvMonthData.setOnClickListener(this);
        ibtnPreData.setOnClickListener(this);
        ibtnNextDate.setOnClickListener(this);
        linearLayoutTotalCustomer.setOnClickListener(this);
        linearLayoutOldCustomer.setOnClickListener(this);
        linearLayoutNewCustomer.setOnClickListener(this);
        linearLayoutFiveminute.setOnClickListener(this);
        linearLayoutTimePicker.setOnClickListener(this);
        tvCurDate.setText(curDate);
        initPtrFrame();
        initChartView();
    }

    private void initData() {
        if (presenter == null) {
            presenter = new CustomerInfoPresenter(this);
            presenter.start();
        }
        initChartData();
        presenter.updateDayChartData(curDate);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_setting:
                Intent intent = new Intent(mContext, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_daydata:
                presenter.updateDayChartData(curDate);
                break;
            case R.id.tv_weekdata:
                loadWeekDate(curWeekYear, curWeek);
                break;
            case R.id.tv_monthdata:
                loadMonthDate(curMonthYear, curMonth);
                break;
            case R.id.ibtn_predate:

                Calendar calendar = Calendar.getInstance();
                String nowDate;
                switch (curType) {
                    case "day":
                        calendar.setTimeInMillis(TimeUtils.getTimeMillions(curDate));
                        calendar.add(Calendar.DATE, -1);
                        nowDate = TimeUtils.getDate(calendar.getTimeInMillis());
                        presenter.updateDayChartData(nowDate);
                        break;
                    case "week":
                        calendar.set(Calendar.YEAR, curWeekYear);
                        if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                            calendar.set(Calendar.WEEK_OF_YEAR, curWeek - 1);
                        } else {
                            calendar.set(Calendar.WEEK_OF_YEAR, curWeek);
                        }
                        calendar.add(Calendar.WEEK_OF_YEAR, -1);
//                        curYear = calendar.get(Calendar.YEAR);
                        nowDate = TimeUtils.getDate(calendar.getTimeInMillis());
                        presenter.updateWeekChartData(nowDate);
                        break;
                    case "month":
                        calendar.set(Calendar.YEAR, curMonthYear);
                        if (curMonth == 2) {
                            calendar.set(Calendar.MONTH, 0);
                        } else {
                            calendar.set(Calendar.MONTH, curMonth - 1);
                            calendar.add(Calendar.MONTH, -1);
                        }
                        calendar.set(Calendar.DAY_OF_MONTH, 1);
                        nowDate = TimeUtils.getDate(calendar.getTimeInMillis());
                        presenter.updateMonthChartData(nowDate);
                        break;
                }
                break;
            case R.id.ibtn_nextdate:
                Calendar calendar1 = Calendar.getInstance();
                String nowDate1;
                switch (curType) {
                    case "day":

                        calendar1.setTimeInMillis(TimeUtils.getTimeMillions(curDate));
                        calendar1.add(Calendar.DATE, 1);
                        nowDate1 = TimeUtils.getDate(calendar1.getTimeInMillis());
                        presenter.updateDayChartData(nowDate1);
                        break;
                    case "week":
                        calendar1.set(Calendar.YEAR, curWeekYear);
                        if (calendar1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                            calendar1.add(Calendar.DATE, -6);//如果是周日，则按周一算
                        }
                        calendar1.set(Calendar.WEEK_OF_YEAR, curWeek - 1);
                        calendar1.add(Calendar.WEEK_OF_YEAR, 1);
                        //如果是最后一周

                        nowDate1 = TimeUtils.getDate(calendar1.getTimeInMillis());
                        presenter.updateWeekChartData(nowDate1);
                        break;
                    case "month":
                        calendar1.set(Calendar.YEAR, curMonthYear);
                        if (curMonth == 2) {
                            calendar1.set(Calendar.MONTH, 2);
                        } else {
                            calendar1.set(Calendar.MONTH, curMonth - 1);
                            calendar1.add(Calendar.MONTH, 1);
                        }
                        calendar1.set(Calendar.DAY_OF_MONTH, 1);
                        nowDate1 = TimeUtils.getDate(calendar1.getTimeInMillis());
                        Log.e(TAG, "nowDate1=" + nowDate1);
                        presenter.updateMonthChartData(nowDate1);
                        break;
                }
                break;
            case R.id.linearlayout_customer:
                Intent intent1 = new Intent(mContext, ProbeDetailActivity.class);
                intent1.putExtra("date", curDate);
                intent1.putExtra("type", "客流量");
                startActivity(intent1);
                break;
            case R.id.linearlayout_newcustomer:
                Intent intent2 = new Intent(mContext, ProbeDetailActivity.class);
                intent2.putExtra("date", curDate);
                intent2.putExtra("type", "新客");
                startActivity(intent2);
                break;
            case R.id.linearlayout_oldcustomer:
                Intent intent3 = new Intent(mContext, ProbeDetailActivity.class);
                intent3.putExtra("date", curDate);
                intent3.putExtra("type", "老客");
                startActivity(intent3);
                break;
            case R.id.linearlayout_fiveminiute:
                Intent intent4 = new Intent(mContext, ProbeDetailActivity.class);
                intent4.putExtra("date", curDate);
                intent4.putExtra("type", "5分钟内客流");
                startActivity(intent4);
                break;
            case R.id.linearlayout_timepick:
                showDatePicker();
                break;
            case R.id.head_advanced:
                if (CheckApkExist.checkApkExist(mContext, "com.gelian.shopun") && CheckApkExist.checkApkExist(mContext, "com.gelian.device")) {
                    Intent intent6 = new Intent();
                    intent6.setClassName("com.gelian.shopun", "com.gelian.shopun.activity.ActivitySplash");
                    PackageManager packageManager = mContext.getPackageManager();
                    if (packageManager.resolveActivity(intent6, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                        startActivity(intent6);
                    } else {

                    }

                } else {
                    //提醒从应用市场下载安装
                    Intent protocolIntent = new Intent(mContext, SplashActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("appinfo", new AppProtocolInfo("客群画像", "", "protocol_dzl.txt",0));
                    protocolIntent.putExtras(bundle);
                    startActivityForResult(protocolIntent, 2001);
                }
                break;
        }
    }
    private void addTitle(String type) {

        if (listView.getHeaderViewsCount() > 0) {
            listView.removeHeaderView(title.getRootView());
        }
        switch (type) {
            case "day":
                title.setDate(new ComparisonInfo(getResources().getString(R.string.detaillist_baseunit_hour), getResources().getString(R.string.detaillist_customernumber_cur_day), getResources().getString(R.string.detaillist_customernumber_last_day), getResources().getString(R.string.detaillist_customernumber_comparison)));
                break;
            case "week":
                title.setDate(new ComparisonInfo(getResources().getString(R.string.detaillist_baseunit_date), getResources().getString(R.string.detaillist_customernumber_cur_week), getResources().getString(R.string.detaillist_customernumber_last_week), getResources().getString(R.string.detaillist_customernumber_comparison)));
                break;
            case "month":
                title.setDate(new ComparisonInfo(getResources().getString(R.string.detaillist_baseunit_date), getResources().getString(R.string.detaillist_customernumber_cur_month), getResources().getString(R.string.detaillist_customernumber_last_month), getResources().getString(R.string.detaillist_customernumber_comparison)));
                break;
        }

        listView.addHeaderView(title.getRootView());
    }

    private void initPtrFrame() {
        // header
        final MaterialHeader header = new MaterialHeader(mContext);
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, LocalDisplay.dp2px(15), 0, LocalDisplay.dp2px(10));
        header.setPtrFrameLayout(mPtrFrame);

        mPtrFrame.setLoadingMinTime(1000);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        mPtrFrame.setDurationToClose(100);//一定要设置
        mPtrFrame.setPinContent(false);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                switch (curType) {
                    case "day":
                        presenter.updateDayChartData(curDate);
                        break;
                    case "week":
                        loadWeekDate(curWeekYear, curWeek);
                        break;
                    case "month":
                        loadMonthDate(curMonthYear, curMonth);
                        break;
                }
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return true;
            }
        });
    }
    private void initChartView() {
        initBarChartView(barChart);
        initBarChartView(barChart2);
        initPieChartView();
    }
    private void initChartData() {
        setBarChartData(barChart, null, null);
        setBarChartData(barChart2, null, null);
        setPieChartData(null);
    }
    private void initBarChartView(BarChart barChart) {
        if (barChart.equals(this.barChart)) {
            barChart.setOnChartValueSelectedListener(this);
        } else {
            barChart.setOnChartValueSelectedListener(barChart2Listener);
        }
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setScaleEnabled(false);
        barChart.getDescription().setEnabled(false);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        barChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false);

        barChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);
        IAxisValueFormatter xAxisFormatter = new DayHoursAxisValueFormatter(hours);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Light.ttf"));
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(24);
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setTextColor(Color.parseColor("#727272"));
//        xAxis.setTextSize(18f);

//        xAxis.setYOffset(10f);
//        xAxis.setXOffset(10f);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Light.ttf"));
        leftAxis.setLabelCount(6, true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.enableGridDashedLine(10f, 10f, 0f);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);


        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        l.setEnabled(false);
    }
    //检查兼容性------还需检查服务的升级/安装
    private void checkCompatibility() {
        String errorMsg = GlobalValueManager.getInstance().getWifiProbeErrorInfo();
        Log.d(TAG, "checkCompatibility :" + errorMsg);
        if (!errorMsg.equals("ok")) {
            final String title = errorMsg.contains("系统") ? "系统版本低" : "服务版本低";
            DialogUtil.showAlertDialog(mContext, title, GlobalValueManager.getInstance().getWifiProbeErrorInfo(), new OnDialogCloseListener() {
                @Override
                public void onClick(Dialog dialog, boolean confirm) {
                    getActivity().finish();
                }
            }, null);
        } else {
            mPtrFrame.autoRefresh();
        }

    }

    private void initPieChartView() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawCenterText(true);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(false);
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        pieChart.setCenterTextTypeface(Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Light.ttf"));
        pieChart.setCenterTextColor(Color.parseColor("#9f9f9f"));
        pieChart.setCenterTextSize(20f);
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setEnabled(false);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTypeface(Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Regular.ttf"));
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setDrawEntryLabels(false);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 2001:
                if(resultCode==-1){
                    //同意
                    AppUpdateInfo info;
                    if (!CheckApkExist.checkApkExist(mContext, "com.gelian.device")) {
                        info = new GLSUpdateInfo();
                        if (CheckApkExist.checkApkExist(mContext, "com.gelian.shopun")) {
                            info.setAppNumber(1);
                        } else {
                            info.setAppNumber(2);
                        }
                    } else {
                        info = new GLUpdateInfo();
                        info.setAppNumber(1);
                    }
                    info.setDownloadIndex(1);
                    presenter.startUpdate(info, true);//先下载服务，下载并安装完服务之后再下载店知了，安装成功后直接打开
                }else if(resultCode==-2){
                    //拒绝
                }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDataBaseInitWorkTask = null;
        presenter.stop();
        presenter.end();
        presenter.setView(null);
        presenter = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!GlobalValueManager.getInstance().getWifiProbeErrorInfo().equals("ok")) {
            return;
        }
        if (!isFirst) {
            switch (curType) {
                case "day":
                    presenter.updateDayChartData(curDate);
                    break;
                case "week":
                    loadWeekDate(curWeekYear, curWeek);
                    break;
                case "month":
                    loadMonthDate(curMonthYear, curMonth);
                    break;
            }
        } else {
            isFirst = false;
        }
    }

    private void loadWeekDate(int curYear, int curWeek) {
        Log.e(TAG, "start load weekData " + curYear + "-" + curWeek);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, curYear);
        if (c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            c.set(Calendar.WEEK_OF_YEAR, curWeek - 1);
        } else {
            c.set(Calendar.WEEK_OF_YEAR, curWeek);
        }
        presenter.updateWeekChartData(TimeUtils.getDate(c.getTimeInMillis()));
    }

    private void loadMonthDate(int curYear, int curMonth) {
        Log.e(TAG, "start load MonthDate " + curYear + "-" + curMonth);
        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.YEAR, curYear);
        c1.set(Calendar.MONTH, curMonth - 1);//必须这样
        presenter.updateMonthChartData(TimeUtils.getDate(c1.getTimeInMillis()));
    }
    public void showDatePicker() {
//        if (datePicker == null) {
        datePicker = new DatePickDialog(mContext);
        //设置上下年分限制
        datePicker.setYearLimt(5);
        //设置标题
        datePicker.setTitle("选择时间");
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
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String tempDate = format.format(date);
                switch (curType) {
                    case "day":
                        presenter.updateDayChartData(tempDate);
                        break;
                    case "week":
                        presenter.updateWeekChartData(tempDate);
                        break;
                    case "month":
                        presenter.updateMonthChartData(tempDate);
                        break;
                }
            }
        });
//        }
        Date date = new Date();
        switch (curType) {
            case "day":
                date = TimeUtils.getDate(0, 0, 2, curDate);
                break;
            case "week":
                date = TimeUtils.getDate(curWeekYear, curWeek, 1, curDate);
                break;
            case "month":
                date = TimeUtils.getDate(curMonthYear, curMonth, 0, curDate);
                break;
        }
        datePicker.setStartDate(date);
        Log.e("TTTTT", "setStartDate=" + date.toString());
        datePicker.show();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        // 设置x轴的LimitLine，index是从0开始的
        LimitLine xLimitLine = new LimitLine(e.getX(), "");
        xLimitLine.setLineColor(ColorTemplate.rgb("#4caf50"));
        xLimitLine.setLineWidth(2f);
        XAxis xAxis = barChart.getXAxis();
        xAxis.removeAllLimitLines();
        xAxis.addLimitLine(xLimitLine);

        if (h != null) {
            int index = (int) e.getX();

            BarEntry barEntry = ((BarDataSet) barChart.getData().getDataSetByIndex(0)).getValues().get(index);
            tvDistanceClose.setText(((int) barEntry.getYVals()[0]) + "");
            tvDistanceMiddle.setText(((int) barEntry.getYVals()[1]) + "");
            tvDistanceFar.setText(((int) barEntry.getYVals()[2]) + "");
            switch (curType) {
                case "day":
                    tvCurTime.setText(hours[index]);
                    dayEntry1 = e;
                    break;
                case "week":
                    tvCurTime.setText(weekDays[index]);
                    weekEntry1 = e;
                    break;
                case "month":
                    tvCurTime.setText(monthDays[index]);
                    monthEntry1 = e;
                    break;
            }

            tvCurValue.setText(((int) barEntry.getYVals()[0]) + ((int) barEntry.getYVals()[1]) + ((int) barEntry.getYVals()[2]) + "人次");
        }
    }

    @Override
    public void onNothingSelected() {

    }

    private OnChartValueSelectedListener barChart2Listener = new OnChartValueSelectedListener() {
        @Override
        public void onValueSelected(Entry e, Highlight h) {
            onValueSelected2(e, h);
        }

        @Override
        public void onNothingSelected() {

        }
    };

    private void onValueSelected2(Entry e, Highlight h) {
        if (e == null)
            return;
        // 设置x轴的LimitLine，index是从0开始的
        LimitLine xLimitLine = new LimitLine(e.getX(), "");
        xLimitLine.setLineColor(ColorTemplate.rgb("#90e1f6"));
        xLimitLine.setLineWidth(2f);
        XAxis xAxis = barChart2.getXAxis();
        xAxis.removeAllLimitLines();
        xAxis.addLimitLine(xLimitLine);

        if (h != null) {
            int index = (int) e.getX();
            BarEntry barEntry = ((BarDataSet) barChart2.getData().getDataSetByIndex(0)).getValues().get(index);
            tvCustomerNew.setText(((int) barEntry.getYVals()[0]) + "");
            tvCustomerOld.setText(((int) barEntry.getYVals()[1]) + "");
            switch (curType) {
                case "day":
                    tvCurTime1.setText(hours[index]);
                    dayEntry2 = e;
                    break;
                case "week":
                    tvCurTime1.setText(weekDays[index]);
                    weekEntry2 = e;
                    break;
                case "month":
                    tvCurTime1.setText(monthDays[index]);
                    monthEntry2 = e;
                    break;
            }
            tvCurValue1.setText(((int) barEntry.getYVals()[0]) + ((int) barEntry.getYVals()[1]) + "人次");
        }
    }

    private void setBarChartData(BarChart barChart, String type, ArrayList<BarEntry> yVals) {
        if (yVals == null)
            return;
        curType = type;
        updateBarView(barChart, type, yVals);
        BarDataSet set1;
        if (barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals);
            set1.setDrawValues(false);
            set1.setHighLightAlpha(0);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            if (barChart.equals(barChart2)) {
                set1 = new BarDataSet(yVals, "新老客详细信息");
                set1.setColors(new int[]{ColorTemplate.rgb("#90e1f6"), ColorTemplate.rgb("#ffda6a")});
                set1.setStackLabels(new String[]{"新", "老"});
            } else {
                set1 = new BarDataSet(yVals, "客流量详细信息");
                set1.setColors(new int[]{ColorTemplate.rgb("#4caf50"), ColorTemplate.rgb("#b7dfb9"), ColorTemplate.rgb("#dbefdc")});
                set1.setStackLabels(new String[]{"近", "中", "远"});
            }
            set1.setHighLightAlpha(0);
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            set1.setDrawValues(false);
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setValueTextColor(Color.WHITE);
            barChart.setData(data);
        }

        Entry entry1 = dayEntry1;
        Entry entry2 = dayEntry2;

        if (barChart.equals(this.barChart)) {

            switch (type) {
                case "day":
                    entry1 = dayEntry1 != null ? dayEntry1 : new Entry(TimeUtils.getHour(System.currentTimeMillis()), 0);
                    break;
                case "week":
                    entry1 = weekEntry1 != null ? weekEntry1 : new Entry(TimeUtils.getWeekDay(System.currentTimeMillis()), 0);
                    break;
                case "month":
                    int curDay = TimeUtils.getMonthDay(System.currentTimeMillis());
                    int size = set1.getValues().size();
                    int curIndex = curDay >= size ? size - 1 : curDay;
                    if (monthEntry1 == null) {
                        monthEntry1 = new Entry(curIndex, 0);
                    } else {
                        monthEntry1.setX(curIndex);
                    }
                    entry1 = monthEntry1;
                    break;
            }

            onValueSelected(entry1, new Highlight(entry1.getX(), 0, 0));
        } else {
            switch (type) {
                case "day":
                    entry2 = dayEntry2 != null ? dayEntry2 : new Entry(TimeUtils.getHour(System.currentTimeMillis()), 0);
                    ;
                    break;
                case "week":
                    entry2 = weekEntry2 != null ? weekEntry2 : new Entry(TimeUtils.getWeekDay(System.currentTimeMillis()), 0);
                    ;
                    break;
                case "month":
                    int curDay = TimeUtils.getMonthDay(System.currentTimeMillis());
                    int size = set1.getValues().size();
                    int curIndex = curDay >= size ? size - 1 : curDay;
                    if (monthEntry2 == null) {
                        monthEntry2 = new Entry(curIndex, 0);
                    } else {
                        monthEntry2.setX(curIndex);
                    }
                    entry2 = monthEntry2;
                    ;
                    break;
            }
            onValueSelected2(entry2, new Highlight(entry2.getX(), 0, 0));

        }

        barChart.setFitBars(true);
        barChart.invalidate();
    }
    private void setPieChartData(ArrayList<PieEntry> entries) {
        if (entries == null) {
            entries = new ArrayList<>();
        }
        if (pieChart.getData() != null && pieChart.getData().getDataSet() != null && entries.size() > 0) {
            ((PieDataSet) pieChart.getData().getDataSet()).setValues(entries);
            pieChart.getData().notifyDataChanged();
            pieChart.notifyDataSetChanged();
        } else {
            PieDataSet dataSet = new PieDataSet(entries, "brandInfos");
            dataSet.setDrawIcons(false);
            dataSet.setSliceSpace(3f);
            dataSet.setIconsOffset(new MPPointF(0, 40));
            dataSet.setSelectionShift(5f);
            dataSet.setDrawValues(false);

            ArrayList<Integer> colors = new ArrayList<Integer>();
            colors.add(this.getResources().getColor(R.color.macbrand_no1));
            colors.add(this.getResources().getColor(R.color.macbrand_no2));
            colors.add(this.getResources().getColor(R.color.macbrand_no3));
            colors.add(this.getResources().getColor(R.color.macbrand_no4));
            colors.add(this.getResources().getColor(R.color.macbrand_no5));
            colors.add(this.getResources().getColor(R.color.macbrand_no6));
            colors.add(this.getResources().getColor(R.color.macbrand_no7));
            dataSet.setColors(colors);
            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.WHITE);
            data.setValueTypeface(Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Light.ttf"));
            pieChart.setData(data);
            pieChart.highlightValues(null);
        }
        if (pieChart.getData().getDataSet().getEntryCount() > 0) {
            pieChart.setCenterText("TOP" + (pieChart.getData().getDataSet().getEntryCount() - 1));
        } else {
            pieChart.setCenterText("TOP" + (pieChart.getData().getDataSet().getEntryCount()));
        }
        pieChart.invalidate();
        updatePieChartLegend();
    }
    private String getComparisonValue(int num1, int num2) {
        if (num1 == 0 && num2 == 0) {
            return String.valueOf(0);
        }
        if (num2 == 0) {
            return String.valueOf(1);
        }
        if (num1 == 0) {
            return String.valueOf(-1);
        }
        return String.valueOf((num1 - num2) / (num2 * 1.0));
    }
    public void updatePieChartLegend() {
        IPieDataSet dataSet = pieChart.getData().getDataSet();
        legendLayout.removeAllViews();
        float totalValue = 0;
        for (int i = 0; i < dataSet.getEntryCount(); i++) {
            totalValue += dataSet.getEntryForIndex(i).getY();
        }
        DecimalFormat df1 = new DecimalFormat("0.00%");
        for (int i = 0; i < dataSet.getEntryCount(); i++) {
            PieEntry entry = dataSet.getEntryForIndex(i);
            String brandName = entry.getLabel();
            LegendEntry[] legends = pieChart.getLegend().getEntries();
            int color = legends[i].formColor;
            float value = entry.getY();
            int progress = (int) (value * 100f / totalValue);
            String showValue = df1.format(value / totalValue);
            MyLinearLayout layout = new MyLinearLayout(mContext);
            if (!brandName.equals("其他品牌")) {
                legendLayout.addView(layout);
                layout.updateState(color, brandName, progress, showValue);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = 40;
                layout.setLayoutParams(params);
            }
        }
    }

    private void updateBarView(BarChart barChart, String type, ArrayList<BarEntry> yVals1) {
        IAxisValueFormatter xAxisFormatter = new DayHoursAxisValueFormatter(hours);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setLabelCount(24);
        if (type != null)
            switch (type) {
                case "day":
                    break;
                case "week":
                    xAxisFormatter = new WeekAxisValueFormatter(weekDays);
                    xAxis.setLabelCount(7);
                    break;
                case "month":
                    int length = yVals1.size() > 31 ? 31 : yVals1.size();
                    String[] days = new String[length];
                    for (int i = 0; i < length; i++) {
                        days[i] = monthDays[i];
                    }
                    xAxisFormatter = new MonthAxisValueFormatter(days);
                    xAxis.setLabelCount(length);
                    break;
                default:
                    break;
            }
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(xAxisFormatter);
    }
    @Override
    public void updateBarChartView(String date, final String type, final List<BarEntry> list) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setBarChartData(barChart, type, (ArrayList<BarEntry>) list);
            }
        });
    }

    @Override
    public void updateCustomerBarChartView(String date, final String type, final List<BarEntry> list) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setBarChartData(barChart2, type, (ArrayList<BarEntry>) list);
            }
        });
    }

    @Override
    public void updatePieChartView(String date, String type, final List<PieEntry> list, float totalValue) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setPieChartData((ArrayList<PieEntry>) list);
            }
        });
    }

    @Override
    public void updateTotalView(final String date, final String type, final MacTotalInfo info, final int fiveMinuteCounts) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                int customerNumber = 0;
                int newCustomerNumber = 0;
                int oldCustomerNumber = 0;
                List<RssiInfo> rssiInfos = info.getRssiInfos();
                if (rssiInfos != null && rssiInfos.size() > 0) {
                    for (RssiInfo info : rssiInfos) {
                        if (info.getMinRssi() == -1000 && info.getMaxRssi() == 0 && info.getIsDistinct()) {
                            if (info.getIsOld()) {
                                oldCustomerNumber += info.getTotaNumber();
                            } else {
                                newCustomerNumber += info.getTotaNumber();
                            }
                            customerNumber += info.getTotaNumber();
                        }
                    }
                }

                tvCustomNumber.setText(customerNumber + "");
                tvNewCustomNumber.setText(newCustomerNumber + "");
                tvOldCustomNumber.setText(oldCustomerNumber + "");
                tvFiveminute.setText("5分钟内客流   " + fiveMinuteCounts);
                curType = type;

                switch (curType) {
                    case "day":
                        tvCurDate.setText(date);
                        curDate = date;
                        tvDayData.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        tvWeekData.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        tvMonthData.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        ivDayline.setVisibility(View.VISIBLE);
                        ivWeekline.setVisibility(View.INVISIBLE);
                        ivMonthline.setVisibility(View.INVISIBLE);
                        if (date.equals(TimeUtils.getDate(System.currentTimeMillis()))) {
                            linearLayoutFiveminute.setVisibility(View.VISIBLE);
                        } else {
                            linearLayoutFiveminute.setVisibility(View.GONE);
                        }

                        break;
                    case "week":
                        long timeMillions = TimeUtils.getTimeMillions(date);
                        curWeekYear = TimeUtils.getYear(timeMillions);
                        curWeek = TimeUtils.getWeeksOfYear(date);
                        tvCurDate.setText(curWeekYear + "年第" + curWeek + "周");
                        tvDayData.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        tvWeekData.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        tvMonthData.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        ivDayline.setVisibility(View.INVISIBLE);
                        ivWeekline.setVisibility(View.VISIBLE);
                        ivMonthline.setVisibility(View.INVISIBLE);
                        linearLayoutFiveminute.setVisibility(View.GONE);
                        break;
                    case "month":
                        long timeMillions1 = TimeUtils.getTimeMillions(date);
                        Log.e(TAG, "return date:" + date);
                        curMonthYear = TimeUtils.getYear(timeMillions1);
                        curMonth = TimeUtils.getMonthsOfYear(date);
                        Log.e(TAG, "return month:" + curMonth);
                        tvCurDate.setText(curMonthYear + "年" + curMonth + "月");
                        tvDayData.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        tvWeekData.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        tvMonthData.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        ivDayline.setVisibility(View.INVISIBLE);
                        ivWeekline.setVisibility(View.INVISIBLE);
                        ivMonthline.setVisibility(View.VISIBLE);
                        linearLayoutFiveminute.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    @Override
    public void updateDetailListView(String date, final String type, final List<MacTotalInfo> last, final List<MacTotalInfo> now) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.refreshComplete();
                if (last == null || now == null)
                    return;
                List<ComparisonInfo> list = new ArrayList<>();
                int size = last.size() >= now.size() ? last.size() : now.size();
                for (int i = 0; i < size; i++) {
                    int curValue = 0;
                    if (i < now.size()) {
                        List<RssiInfo> nowRssiInfos = now.get(i).getRssiInfos();

                        if (nowRssiInfos != null && nowRssiInfos.size() > 0) {
                            for (RssiInfo info : nowRssiInfos) {
                                if (info.getMinRssi() == -1000 && info.getMaxRssi() == 0 && info.getIsDistinct()) {
                                    curValue += info.getTotaNumber();
                                }
                            }
                        }
                    }
                    int lastValue = 0;
                    if (i < last.size()) {
                        List<RssiInfo> lastRssiInfos = last.get(i).getRssiInfos();

                        if (lastRssiInfos != null && lastRssiInfos.size() > 0) {
                            for (RssiInfo info : lastRssiInfos) {
                                if (info.getMinRssi() == -1000 && info.getMaxRssi() == 0 && info.getIsDistinct()) {
                                    lastValue += info.getTotaNumber();
                                }
                            }
                        }
                    }
                    String percentValue = "-";

                    percentValue = getComparisonValue(curValue, lastValue);

                    String scaleValue = "";
                    if (type.equals("month")) {
                        scaleValue = monthDays[i];
                    } else if (type.equals("day")) {
                        if (now.size() >= last.size()) {
                            scaleValue = now.get(i).getScaleValue() + "时";
                        } else {
                            scaleValue = last.get(i).getScaleValue() + "时";
                        }
                    } else {
                        if (now.size() >= last.size()) {
                            scaleValue = now.get(i).getScaleValue();
                        } else {
                            scaleValue = last.get(i).getScaleValue();
                        }
                    }
                    list.add(new ComparisonInfo(scaleValue, curValue + "", lastValue + "", percentValue));
                }
                switch (type) {
                    case "day":
                        title.setDate(new ComparisonInfo("小时", "今日客流量", "昨日客流量", "涨幅"));
                        break;
                    case "week":
                        title.setDate(new ComparisonInfo("日期", "本周客流量", "上周客流量", "涨幅"));
                        break;
                    case "month":
                        title.setDate(new ComparisonInfo("日期", "本月客流量", "上月客流量", "涨幅"));
                        break;
                }

                adapter.setData(list);


            }
        });
    }

    @Override
    public void showLoadingDialog() {
        if (mPtrFrame.isRefreshing())
            return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogUtil.showLoadingDialog(mContext);
            }
        });

    }

    @Override
    public void hideLoadingDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogUtil.hideLoadingDialog();
            }
        });
    }

    @Override
    public void showConfirmDialog(final String title, final String content, final AppUpdateInfo info) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (info.getPackageName().equals("com.ums.wifiprobe")) {
                    DialogUtil.showDialog(mContext, title, content, new OnDialogCloseListener() {
                        @Override
                        public void onClick(Dialog dialog, boolean confirm) {
                            if (confirm) {
                                presenter.startUpdate(info, true);
                            }
                        }
                    });
                } else {
                    //店知了未有更新
                    Toast.makeText(mContext, "应用市场未检查到" + info.getAppName() + "更新", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void showDownloadDialog(final String title, final AppUpdateInfo info) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                info.showDownloadDialog(mContext, info.getAppName() + title, new OnDialogCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        info.hideDownloadDialog();
                    }
                });
            }
        });
    }

    @Override
    public void updateDownloadProgress(final String title, final int i, final AppUpdateInfo info) {
        Log.e("TTTTT", "updateDialog=" + title);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (title.equals("安装完成")) {
                    //直接打开刚安装完成的应用
                    info.updateDownloadProgress(info.getAppName() + title, i);
                    int flag = -1;
                    if (CheckApkExist.checkApkExist(mContext, info.getPackageName())) {
                        if (info.getPackageName().equals("com.gelian.shopun")) {
                            info.hideDownloadDialog();
                            flag = 1;
                        } else if (info.getPackageName().equals("com.gelian.device")) {
                            if (CheckApkExist.checkApkExist(mContext, "com.gelian.shopun")) {
                                info.hideDownloadDialog();
                                flag = 1;
                            } else {
                                info.setAppName("应用");
                                info.setPackageName("com.gelian.shopun");
                                info.setDownloadIndex(2);
                                presenter.startUpdate(info, false);
                            }
                        } else {
                            info.hideDownloadDialog();
                        }
                    }
                    if (flag == 1) {
                        //打开店知了
                        Intent intent = new Intent();
                        intent.setClassName("com.gelian.shopun", "com.gelian.shopun.activity.ActivitySplash");
                        PackageManager packageManager = mContext.getPackageManager();
                        if (packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                            startActivity(intent);
                        } else {

                        }
                    }
                } else {
                    info.updateDownloadProgress(info.getAppName() + title, i);
                    if (title.equals("正在下载")) {
                        if (info instanceof GLSUpdateInfo) {
                            ((GLSUpdateInfo) info).updateMultiTitle("客群画像(" + info.getDownloadIndex() + "/" + info.getAppNumber() + ")");
                        } else if (info instanceof GLUpdateInfo) {
                            ((GLUpdateInfo) info).updateMultiTitle("客群画像(" + info.getDownloadIndex() + "/" + info.getAppNumber() + ")");
                        }
                    } else if (title.equals("下载失败") || title.equals("下载已取消") || title.equals("安装失败")) {
                        if (info instanceof GLSUpdateInfo) {
                            ((GLSUpdateInfo) info).updateMultiButton("取消");
                        } else if (info instanceof GLUpdateInfo) {
                            ((GLUpdateInfo) info).updateMultiButton("取消");
                        }
                    }
                }
            }
        });
    }

    @Override
    public void hideDownloadDialog() {

    }

    @Override
    public void setPresenter(CustomerInfoShowContract.Presenter presenter) {

    }
}
