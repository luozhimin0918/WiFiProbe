package com.ums.wifiprobe.ui.presenter;

import android.util.Log;

import com.appinterface.update.AppJson;
import com.appinterface.update.ReturnCode;
import com.appinterface.update.UpdateHelper;
import com.appinterface.update.UpdateListener;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.ums.wifiprobe.CommonStants;
import com.ums.wifiprobe.app.ThreadPoolProxyFactory;
import com.ums.wifiprobe.app.WPApplication;
import com.ums.wifiprobe.data.BrandTotalDataRepository;
import com.ums.wifiprobe.data.DataResource;
import com.ums.wifiprobe.data.ProbeTotalDataRepository;
import com.ums.wifiprobe.data.database.helper.ProbeInfoDaoHelper;
import com.ums.wifiprobe.service.ProbeServiceConstant;
import com.ums.wifiprobe.service.greendao.BrandTotalInfo;
import com.ums.wifiprobe.service.greendao.MacTotalInfo;
import com.ums.wifiprobe.service.greendao.RssiInfo;
import com.ums.wifiprobe.ui.CustomerInfoShowContract;
import com.ums.wifiprobe.ui.activity.CustomerInfoActivity;
import com.ums.wifiprobe.update.AppUpdateInfo;
import com.ums.wifiprobe.update.SelpUpdateInfo;
import com.ums.wifiprobe.utils.NetWorkUtils;
import com.ums.wifiprobe.utils.TimeUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenzhy on 2017/9/20.
 */

public class CustomerInfoPresenter implements CustomerInfoShowContract.Presenter, CustomerInfoShowContract.View {
    private final static String TAG = "WiFiProbe-Presenter";
    private CustomerInfoShowContract.View view;
    private boolean isFirst = true;

    //使用同步锁工具，待昨日今日数据都加载完成之后，同步显示

    public CustomerInfoPresenter(CustomerInfoShowContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    public void setView(CustomerInfoShowContract.View view) {
        this.view = view;
        if (view != null) {
            view.setPresenter(this);
        }
    }

    @Override
    public void start() {
        //检查更新
//        if (NetWorkUtils.isWifiConnected(activity)) {
//            checkUpdate();
//        }
    }

    @Override
    public void end() {
        this.view = null;
    }

    public void stop() {
        //需要将查询线程池shutdown?
    }

    @Override
    public void updateDayChartData(final String date) {
        Log.d(TAG, "start update dayChartData");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取BarChart数据
                showLoadingDialog();
                final List<MacTotalInfo> curCustomerList = new ArrayList<>();
                final List<MacTotalInfo> preCustomerList = new ArrayList<>();
                final CountDownLatch latch = new CountDownLatch(4);
                ThreadPoolProxyFactory.getQueryThreadPoolProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        ProbeTotalDataRepository.getInstance().getTasks(TimeUtils.getWeekDate(date), "day", date, new DataResource.LoadTasksCallback<MacTotalInfo>() {

                            @Override
                            public void onTasksLoaded(List<MacTotalInfo> list) {
                                List<BarEntry> barEntries = new ArrayList<BarEntry>();
                                List<BarEntry> barEntries1 = new ArrayList<>();
                                for (int i = 0; i < list.size(); i++) {
                                    float[] values = new float[3];
                                    float[] values2 = new float[2];
                                    List<RssiInfo> rssiInfos = list.get(i).getRssiInfos();
                                    for (RssiInfo info : rssiInfos) {
                                        if (info.getIsDistinct() && info.getMinRssi() == ProbeServiceConstant.maxRssi && info.getMaxRssi() == 0) {
                                            //近
                                            values[0] += info.getTotaNumber();
                                        }
                                        if (info.getIsDistinct() && info.getMinRssi() == ProbeServiceConstant.middleRssi && ProbeServiceConstant.maxRssi == info.getMaxRssi()) {
                                            //中
                                            values[1] += info.getTotaNumber();
                                        }
                                        if (info.getIsDistinct() && info.getMinRssi() == ProbeServiceConstant.minRssi && ProbeServiceConstant.middleRssi == info.getMaxRssi()) {
                                            //远
                                            values[2] += info.getTotaNumber();
                                        }
                                        if (info.getIsDistinct() && info.getMinRssi() == -1000 && info.getMaxRssi() == 0) {
                                            if (info.getIsOld()) {
                                                values2[1] += info.getTotaNumber();
                                            } else {
                                                values2[0] += info.getTotaNumber();
                                            }
                                        }

                                    }
                                    barEntries1.add(new BarEntry(i, values2));
                                    barEntries.add(new BarEntry(i, values));

                                }
                                curCustomerList.addAll(list);
                                latch.countDown();
                                updateBarChartView(date, "day", barEntries);
                                updateCustomerBarChartView(date, "day", barEntries1);
                                Log.d(TAG, "barChart/customerBarChart data load success !");
                            }

                            @Override
                            public void onDataNotAvaliable() {
                                latch.countDown();
                                Log.d(TAG, "barChart/customerBarChart data load failure !");
                            }
                        });
                    }
                });

                ThreadPoolProxyFactory.getQueryThreadPoolProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        ProbeTotalDataRepository.getInstance().getTasks(TimeUtils.getWeekDate(date), "day", TimeUtils.getDefineDayAgoDate(TimeUtils.getTimeMillions(date), 1), new DataResource.LoadTasksCallback<MacTotalInfo>() {

                            @Override
                            public void onTasksLoaded(List<MacTotalInfo> list) {
                                preCustomerList.addAll(list);
                                latch.countDown();
                                Log.d(TAG, "pre customerBarChart data load success !");
                            }

                            @Override
                            public void onDataNotAvaliable() {
                                latch.countDown();
                                Log.d(TAG, "pre customerBarChart data load failure !");
                            }
                        });
                    }
                });
                //获取PieChart数据


                ThreadPoolProxyFactory.getQueryThreadPoolProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        BrandTotalDataRepository.getInstance().getTasks(date, "days", date, new DataResource.LoadTasksCallback<BrandTotalInfo>() {
                            @Override
                            public void onTasksLoaded(List<BrandTotalInfo> list) {
                                if (list != null && list.size() > 0) {
                                    float totalValue = 0;
                                    float otherValue = 0;
                                    Collections.sort(list, new Comparator<BrandTotalInfo>() {
                                        @Override
                                        public int compare(BrandTotalInfo o1, BrandTotalInfo o2) {
                                            if (o1.getBrandNumber() > o2.getBrandNumber()) {
                                                return -1;
                                            }
                                            if (o1.getBrandNumber() == o2.getBrandNumber()) {
                                                return 0;
                                            }
                                            return 1;
                                        }
                                    });
                                    for (int i = 0; i < list.size(); i++) {
                                        totalValue += list.get(i).getBrandNumber();
                                    }
                                    if (list.size() > 7) {
                                        int size = list.size() - 7;
                                        for (int i = 0; i < size; i++) {
                                            otherValue += list.get(list.size() - 1).getBrandNumber();
                                            list.remove(list.size() - 1);
                                        }
                                    }
                                    List<PieEntry> pieEntries = new ArrayList<PieEntry>();
                                    for (int i = 0; i < list.size(); i++) {
                                        if (list.get(i).getBrandName().equals("其他品牌")) {
                                            pieEntries.add(new PieEntry(list.get(i).getBrandNumber() + otherValue, list.get(i).getBrandName()));
                                        } else {
                                            pieEntries.add(new PieEntry(list.get(i).getBrandNumber(), list.get(i).getBrandName()));
                                        }
                                    }
                                    updatePieChartView(date, "day", pieEntries, totalValue);
                                }
                                latch.countDown();
                                Log.d(TAG, "PieChart data load success !");
                            }

                            @Override
                            public void onDataNotAvaliable() {
                                updatePieChartView(date, "day", null, 0);
                                latch.countDown();
                                Log.d(TAG, "PieChart data load failure !");
                            }
                        });
                    }
                });

                //获取ListView数据
                //获取Total数据-----同barData
                ThreadPoolProxyFactory.getQueryThreadPoolProxy().execute(new Runnable() {
                    @Override
                    public void run() {

                        ProbeTotalDataRepository.getInstance().getTask(date, "days", date, new DataResource.GetTaskCallback<MacTotalInfo>() {
                            @Override
                            public void OnTaskLoaded(MacTotalInfo info) {

                                int count = ProbeInfoDaoHelper.getDayCustomerCount(date, 300000l);
                                updateTotalView(date, "day", info, count);
                                latch.countDown();
                                Log.d(TAG, "totalView data load success !");
                            }

                            @Override
                            public void onDataNotAvaliable() {
                                latch.countDown();
                                Log.d(TAG, "totalView data load failure !");
                            }
                        });
                    }
                });
                try {
                    latch.await(10, TimeUnit.SECONDS);
                    updateDetailListView(date, "day", preCustomerList, curCustomerList);
                    Log.d(TAG, "day all data load complete !");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    latch.countDown();
                    latch.countDown();
                    latch.countDown();
                    latch.countDown();
                } finally {
                    hideLoadingDialog();
                    if (isFirst) {
                        if (NetWorkUtils.isWifiConnected(WPApplication.getInstance())) {
                            checkUpdate(new SelpUpdateInfo());
                        }
                        isFirst = false;
                    }
                }

            }
        }).start();

    }

    @Override
    public void updateWeekChartData(final String date) {
        //获取BarChart数据
        Log.d(TAG, "start update weekChartData");
        new Thread(new Runnable() {

            @Override
            public void run() {
                showLoadingDialog();
                final List<MacTotalInfo> curCustomerList = new ArrayList<>();
                final List<MacTotalInfo> preCustomerList = new ArrayList<>();
                final CountDownLatch latch = new CountDownLatch(4);
                ThreadPoolProxyFactory.getQueryThreadPoolProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        String scaleValue = TimeUtils.getYear(TimeUtils.getTimeMillions(date)) + "-" + TimeUtils.getWeeksOfYear(date);
                        ProbeTotalDataRepository.getInstance().getTasks(scaleValue, "week", date, new DataResource.LoadTasksCallback<MacTotalInfo>() {

                            @Override
                            public void onTasksLoaded(List<MacTotalInfo> list) {
//                        包括远中近的数据,需要查询3次，%3=0 近 =1 中 =2 远 总数据通过getTask具体查询
//                        0存放总数据，后1 2 3依次存放远中近数据,,,,暂且这么规定
                                List<BarEntry> barEntries = new ArrayList<BarEntry>();
                                List<BarEntry> barEntries1 = new ArrayList<>();
                                for (int i = 0; i < list.size(); i++) {
                                    float[] values = new float[3];
                                    float[] values2 = new float[2];
                                    List<RssiInfo> rssiInfos = list.get(i).getRssiInfos();
                                    for (RssiInfo info : rssiInfos) {
                                        if (info.getIsDistinct() && info.getMinRssi() == ProbeServiceConstant.maxRssi && info.getMaxRssi() == 0) {
                                            //近
                                            values[0] += info.getTotaNumber();
                                        }
                                        if (info.getIsDistinct() && info.getMinRssi() == ProbeServiceConstant.middleRssi && ProbeServiceConstant.maxRssi == info.getMaxRssi()) {
                                            //中
                                            values[1] += info.getTotaNumber();
                                        }
                                        if (info.getIsDistinct() && info.getMinRssi() == ProbeServiceConstant.minRssi && ProbeServiceConstant.middleRssi == info.getMaxRssi()) {
                                            //远
                                            values[2] += info.getTotaNumber();
                                        }
                                        if (info.getIsDistinct() && info.getMinRssi() == -1000 && info.getMaxRssi() == 0) {
                                            if (info.getIsOld()) {
                                                values2[1] += info.getTotaNumber();
                                            } else {
                                                values2[0] += info.getTotaNumber();
                                            }
                                        }

                                    }
                                    barEntries1.add(new BarEntry(i, values2));
                                    barEntries.add(new BarEntry(i, values));

                                }
                                curCustomerList.addAll(list);
                                latch.countDown();
                                updateBarChartView(date, "week", barEntries);
                                updateCustomerBarChartView(date, "week", barEntries1);
                                Log.d(TAG, "barChart/customerBarChart data load success !");
                            }

                            @Override
                            public void onDataNotAvaliable() {
                                latch.countDown();
                                Log.d(TAG, "barChart/customerBarChart data load failure !");
                            }
                        });
                    }
                });

                ThreadPoolProxyFactory.getQueryThreadPoolProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        String scaleValue = TimeUtils.getYear(TimeUtils.getTimeMillions(TimeUtils.getSundayDate(date, -1))) + "-" + (TimeUtils.getWeeksOfYear(date) - 1);
                        ProbeTotalDataRepository.getInstance().getTasks(scaleValue, "week", TimeUtils.getSundayDate(date, -1), new DataResource.LoadTasksCallback<MacTotalInfo>() {

                            @Override
                            public void onTasksLoaded(List<MacTotalInfo> list) {
                                preCustomerList.addAll(list);
                                latch.countDown();
                                Log.d(TAG, "pre customerBarChart data load success !");
                            }

                            @Override
                            public void onDataNotAvaliable() {
                                latch.countDown();
                                Log.d(TAG, "pre customerBarChart data load failure !");
                            }
                        });
                    }
                });

                //获取PieChart数据
                ThreadPoolProxyFactory.getQueryThreadPoolProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        String scaleValue = TimeUtils.getYear(TimeUtils.getTimeMillions(date)) + "-" + TimeUtils.getWeeksOfYear(date);
                        BrandTotalDataRepository.getInstance().getTasks(scaleValue, "weeks", date, new DataResource.LoadTasksCallback<BrandTotalInfo>() {
                            @Override
                            public void onTasksLoaded(List<BrandTotalInfo> list) {
                                if (list != null && list.size() > 0) {
                                    float totalValue = 0;
                                    float otherValue = 0;
                                    Collections.sort(list, new Comparator<BrandTotalInfo>() {
                                        @Override
                                        public int compare(BrandTotalInfo o1, BrandTotalInfo o2) {
                                            if (o1.getBrandNumber() > o2.getBrandNumber()) {
                                                return -1;
                                            }
                                            if (o1.getBrandNumber() == o2.getBrandNumber()) {
                                                return 0;
                                            }
                                            return 1;
                                        }
                                    });
                                    for (int i = 0; i < list.size(); i++) {
                                        totalValue += list.get(i).getBrandNumber();
                                    }
                                    if (list.size() > 7) {
                                        int size = list.size() - 7;
                                        for (int i = 0; i < size; i++) {
                                            otherValue += list.get(list.size() - 1).getBrandNumber();
                                            list.remove(list.size() - 1);
                                        }
                                    }
                                    List<PieEntry> pieEntries = new ArrayList<PieEntry>();
                                    for (int i = 0; i < list.size(); i++) {
                                        if (list.get(i).getBrandName().equals("其他品牌")) {
                                            pieEntries.add(new PieEntry(list.get(i).getBrandNumber() + otherValue, list.get(i).getBrandName()));
                                        } else {
                                            pieEntries.add(new PieEntry(list.get(i).getBrandNumber(), list.get(i).getBrandName()));
                                        }
                                    }
                                    latch.countDown();
                                    updatePieChartView(date, "week", pieEntries, totalValue);
                                    Log.d(TAG, "totalView data load success !");
                                }
                            }

                            @Override
                            public void onDataNotAvaliable() {
                                latch.countDown();
                                updatePieChartView(date, "week", null, 0);
                                Log.d(TAG, "totalView data load failure !");
                            }
                        });
                    }
                });
                //获取ListView数据
                //获取Total数据-----同barData
                ThreadPoolProxyFactory.getQueryThreadPoolProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        String scaleValue = TimeUtils.getYear(TimeUtils.getTimeMillions(date)) + "-" + TimeUtils.getWeeksOfYear(date);
                        ProbeTotalDataRepository.getInstance().getTask(scaleValue, "weeks", date, new DataResource.GetTaskCallback<MacTotalInfo>() {
                            @Override
                            public void OnTaskLoaded(MacTotalInfo info) {
                                latch.countDown();
                                updateTotalView(date, "week", info, 0);
                            }

                            @Override
                            public void onDataNotAvaliable() {
                                latch.countDown();
                            }
                        });
                    }
                });
                try {
                    latch.await(10, TimeUnit.SECONDS);
                    updateDetailListView(date, "week", preCustomerList, curCustomerList);
                    Log.d(TAG, "week all data load complete !");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    latch.countDown();
                    latch.countDown();
                    latch.countDown();
                    latch.countDown();
                } finally {
                    hideLoadingDialog();
                }
            }
        }).start();


    }

    @Override
    public void updateMonthChartData(final String date) {
        Log.d(TAG, "start update monthChartData");
        new Thread(new Runnable() {
            @Override
            public void run() {
                showLoadingDialog();
                final List<MacTotalInfo> curCustomerList = new ArrayList<>();
                final List<MacTotalInfo> preCustomerList = new ArrayList<>();
                final CountDownLatch latch = new CountDownLatch(4);
                //获取BarChart数据
                ThreadPoolProxyFactory.getQueryThreadPoolProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        String scaleValue = TimeUtils.getYear(TimeUtils.getTimeMillions(date)) + "-" + TimeUtils.getMonthsOfYear(date);
                        ProbeTotalDataRepository.getInstance().getTasks(scaleValue, "month", date, new DataResource.LoadTasksCallback<MacTotalInfo>() {

                            @Override
                            public void onTasksLoaded(List<MacTotalInfo> list) {
//                        包括远中近的数据,需要查询3次，%3=0 近 =1 中 =2 远 总数据通过getTask具体查询
//                        0存放总数据，后1 2 3依次存放远中近数据,,,,暂且这么规定
                                List<BarEntry> barEntries = new ArrayList<BarEntry>();
                                List<BarEntry> barEntries1 = new ArrayList<>();
                                for (int i = 0; i < list.size(); i++) {
                                    float[] values = new float[3];
                                    float[] values2 = new float[2];
                                    List<RssiInfo> rssiInfos = list.get(i).getRssiInfos();
                                    for (RssiInfo info : rssiInfos) {
                                        if (info.getIsDistinct() && info.getMinRssi() == ProbeServiceConstant.maxRssi && info.getMaxRssi() == 0) {
                                            //近
                                            values[0] += info.getTotaNumber();
                                        }
                                        if (info.getIsDistinct() && info.getMinRssi() == ProbeServiceConstant.middleRssi && ProbeServiceConstant.maxRssi == info.getMaxRssi()) {
                                            //中
                                            values[1] += info.getTotaNumber();
                                        }
                                        if (info.getIsDistinct() && info.getMinRssi() == ProbeServiceConstant.minRssi && ProbeServiceConstant.middleRssi == info.getMaxRssi()) {
                                            //远
                                            values[2] += info.getTotaNumber();
                                        }
                                        if (info.getIsDistinct() && info.getMinRssi() == -1000 && info.getMaxRssi() == 0) {
                                            if (info.getIsOld()) {
                                                values2[1] += info.getTotaNumber();
                                            } else {
                                                values2[0] += info.getTotaNumber();
                                            }
                                        }

                                    }
                                    barEntries1.add(new BarEntry(i, values2));
                                    barEntries.add(new BarEntry(i, values));
                                }
                                curCustomerList.addAll(list);
                                latch.countDown();
                                updateBarChartView(date, "month", barEntries);
                                updateCustomerBarChartView(date, "month", barEntries1);
                                Log.d(TAG, "barChart/customerBarChart data load success !");
                            }

                            @Override
                            public void onDataNotAvaliable() {
                                latch.countDown();
                                Log.d(TAG, "barChart/customerBarChart data load failure !");
                            }
                        });
                    }
                });

                ThreadPoolProxyFactory.getQueryThreadPoolProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        int lastMonth = TimeUtils.getDefineMonthAgo(date, -1);
                        String lastMonthData = TimeUtils.getDefineMonthAgoDate(date, -1);
                        String scaleValue = TimeUtils.getYear(TimeUtils.getTimeMillions(lastMonthData)) + "-" + lastMonth;
                        ProbeTotalDataRepository.getInstance().getTasks(scaleValue, "month", lastMonthData, new DataResource.LoadTasksCallback<MacTotalInfo>() {

                            @Override
                            public void onTasksLoaded(List<MacTotalInfo> list) {
                                preCustomerList.addAll(list);
                                latch.countDown();
                                Log.d(TAG, "pre customerBarChart data load success !");
                            }

                            @Override
                            public void onDataNotAvaliable() {
                                latch.countDown();
                                Log.d(TAG, "pre customerBarChart data load success !");
                            }
                        });
                    }
                });

                //获取PieChart数据
                ThreadPoolProxyFactory.getQueryThreadPoolProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        String scaleValue = TimeUtils.getYear(TimeUtils.getTimeMillions(date)) + "-" + TimeUtils.getMonthsOfYear(date);
                        BrandTotalDataRepository.getInstance().getTasks(scaleValue, "months", date, new DataResource.LoadTasksCallback<BrandTotalInfo>() {
                            @Override
                            public void onTasksLoaded(List<BrandTotalInfo> list) {
                                if (list != null && list.size() > 0) {
                                    float totalValue = 0;
                                    float otherValue = 0;
                                    Collections.sort(list, new Comparator<BrandTotalInfo>() {
                                        @Override
                                        public int compare(BrandTotalInfo o1, BrandTotalInfo o2) {
                                            if (o1.getBrandNumber() > o2.getBrandNumber()) {
                                                return -1;
                                            }
                                            if (o1.getBrandNumber() == o2.getBrandNumber()) {
                                                return 0;
                                            }
                                            return 1;
                                        }
                                    });
                                    for (int i = 0; i < list.size(); i++) {
                                        totalValue += list.get(i).getBrandNumber();
                                    }
                                    if (list.size() > 7) {
                                        int size = list.size() - 7;
                                        for (int i = 0; i < size; i++) {
                                            otherValue += list.get(list.size() - 1).getBrandNumber();
                                            list.remove(list.size() - 1);
                                        }
                                    }
                                    List<PieEntry> pieEntries = new ArrayList<PieEntry>();
                                    for (int i = 0; i < list.size(); i++) {
                                        if (list.get(i).getBrandName().equals("其他品牌")) {
                                            pieEntries.add(new PieEntry(list.get(i).getBrandNumber() + otherValue, list.get(i).getBrandName()));
                                        } else {
                                            pieEntries.add(new PieEntry(list.get(i).getBrandNumber(), list.get(i).getBrandName()));
                                        }
                                    }
                                    latch.countDown();
                                    updatePieChartView(date, "month", pieEntries, totalValue);
                                    Log.d(TAG, "totalView data load success !");
                                }
                            }

                            @Override
                            public void onDataNotAvaliable() {
                                latch.countDown();
                                updatePieChartView(date, "month", null, 0);
                                Log.d(TAG, "totalView data load failure !");
                            }
                        });
                    }
                });
                //获取ListView数据
                //获取Total数据-----同barData
                ThreadPoolProxyFactory.getQueryThreadPoolProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        String scaleValue = TimeUtils.getYear(TimeUtils.getTimeMillions(date)) + "-" + TimeUtils.getMonthsOfYear(date);
                        ProbeTotalDataRepository.getInstance().getTask(scaleValue, "months", date, new DataResource.GetTaskCallback<MacTotalInfo>() {
                            @Override
                            public void OnTaskLoaded(MacTotalInfo info) {
                                latch.countDown();
                                updateTotalView(date, "month", info, 0);
                            }

                            @Override
                            public void onDataNotAvaliable() {
                                latch.countDown();
                            }
                        });
                    }
                });
                try {
                    latch.await(10, TimeUnit.SECONDS);
                    updateDetailListView(date, "month", preCustomerList, curCustomerList);
                    Log.d(TAG, "month all data load complete !");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    latch.countDown();
                    latch.countDown();
                    latch.countDown();
                    latch.countDown();
                } finally {
                    hideLoadingDialog();
                }
            }
        }).start();


    }

    @Override
    public void checkUpdate(final AppUpdateInfo info) {
        Log.e(TAG, "start checkAppUpdate ");
        UpdateHelper.checkUpdate(WPApplication.getInstance(), info.getPackageName(), new UpdateListener() {
            @Override
            public void onCheckUpdateResult(int i, AppJson appJson) {
                Log.e(TAG, "onCheckUpdateResult " + i);
                super.onCheckUpdateResult(i, appJson);
                if (i == ReturnCode.CODE_GET_UPDATE) {
                    Log.e(TAG, "start download !");
                    if (info.getPackageName().equals("com.gelian.shopun")) {
                        startUpdate(info, true);
                    } else {
                        showConfirmDialog("版本检查", "检查到当前有更新版本，是否现在更新！", info);
                    }

                } else if (i == ReturnCode.CODE_FAILED_UNINSTALLED) {
                    startUpdate(info, true);//未安装，直接下载安装
                } else {
                    if (info.getPackageName().equals("com.gelian.shopun")) {
                        //未检查到店知了有更新下载
                        showConfirmDialog("版本检查", "应用市场中未检查到有店知了的更新！", info);
                    }
                }
            }

            @Override
            public void onProgress(int i) {
                super.onProgress(i);
            }

            @Override
            public void onStateChanged(int i) {
                super.onStateChanged(i);
            }
        });
    }

    @Override
    public void startUpdate(final AppUpdateInfo info, final boolean showDialog) {
        if (showDialog) {
            showDownloadDialog("下载中", info);
        }
        UpdateHelper.download(WPApplication.getInstance(), info.getAppKey(), info.getPackageName(), true, new UpdateListener() {
            @Override
            public void onCheckUpdateResult(int i, AppJson appJson) {
                super.onCheckUpdateResult(i, appJson);
            }

            @Override
            public void onProgress(int i) {
                super.onProgress(i);
                Log.e(TAG, "download progress  " + i);
                updateDownloadProgress("下载中...", i, info);
            }

            @Override
            public void onStateChanged(int i) {
                super.onStateChanged(i);
                Log.e(TAG, "onStateChanged  " + i);
                switch (i) {
                    case 1:
                        updateDownloadProgress("等待下载", -1, info);
                        break;
                    case 2:
                        updateDownloadProgress("正在下载", 0, info);
                        break;
                    case 3:
                        updateDownloadProgress("正在暂停下载", -1, info);
                        break;
                    case 4:
                        updateDownloadProgress("下载已暂停", -1, info);
                        break;
                    case 5:
                        updateDownloadProgress("下载完成", -1, info);
                        break;
                    case 6:
                        updateDownloadProgress("下载失败", -1, info);
                        break;
                    case 7:
                        updateDownloadProgress("正在取消下载", -1, info);
                        break;
                    case 8:
                        updateDownloadProgress("下载已取消", -1, info);
                        break;
                    case 9:
                        updateDownloadProgress("正在安装", -1, info);
                        break;
                    case 10:
                        updateDownloadProgress("等待安装", -1, info);
                        break;
                    case 11:
                        updateDownloadProgress("安装完成", -1, info);
                        break;
                    case 12:
                        updateDownloadProgress("安装失败", -1, info);
                        break;
                    case 13:
                        updateDownloadProgress("安装已取消", -1, info);
                        break;
                }
            }
        });
    }

    @Override
    public void updateBarChartView(String date, String type, List<BarEntry> list) {
        if (view != null) {
            view.updateBarChartView(date, type, list);
        }
    }

    @Override
    public void updateCustomerBarChartView(String date, String type, List<BarEntry> list) {
        if (view != null) {
            view.updateCustomerBarChartView(date, type, list);
        }
    }

    @Override
    public void updatePieChartView(String date, String type, List<PieEntry> list, float totalValue) {
        if (view != null) {
            view.updatePieChartView(date, type, list, totalValue);
        }
    }

    @Override
    public void updateTotalView(String date, String type, MacTotalInfo info, int fiveMinuteCounts) {
        if (view != null) {
            view.updateTotalView(date, type, info, fiveMinuteCounts);
        }
    }

    @Override
    public void updateDetailListView(String date, String type, List<MacTotalInfo> last, List<MacTotalInfo> now) {
        if (view != null) {
            view.updateDetailListView(date, type, last, now);
        }
    }

    @Override
    public void showLoadingDialog() {
        if (view != null) {
            view.showLoadingDialog();
        }
    }

    @Override
    public void hideLoadingDialog() {
        if (view != null) {
            view.hideLoadingDialog();
        }
    }

    @Override
    public void showConfirmDialog(String title, String content, AppUpdateInfo info) {
        if (view != null) {
            view.showConfirmDialog(title, content, info);
        }
    }

    @Override
    public void showDownloadDialog(String title, AppUpdateInfo info) {
        if (view != null) {
            view.showDownloadDialog(title, info);
        }
    }

    @Override
    public void updateDownloadProgress(String title, int i, AppUpdateInfo info) {
        if (view != null) {
            view.updateDownloadProgress(title, i, info);
        }
    }

    @Override
    public void hideDownloadDialog() {
        if (view != null) {
            view.hideDownloadDialog();
        }
    }

    @Override
    public void setPresenter(CustomerInfoShowContract.Presenter presenter) {

    }
}
