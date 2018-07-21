package com.ums.wifiprobe.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Message;
import android.util.Log;

import com.ums.wifiprobe.aidl.TransDataModel;
import com.ums.wifiprobe.app.ThreadPoolProxyFactory;
import com.ums.wifiprobe.data.DataResource;
import com.ums.wifiprobe.data.ProbeTotalDataRepository;
import com.ums.wifiprobe.eventbus.MessageEvent;
import com.ums.wifiprobe.service.greendao.MacTotalInfo;
import com.ums.wifiprobe.service.greendao.RssiInfo;
import com.ums.wifiprobe.utils.TimeUtils;
import com.ums.wifiprobe.utils.coupterUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Luozhimin on 2018-07-21.7:00
 */
    /**
     * IntentService的使用
     * IntentService是Service的子类，也需要在xml中注册
     * 它有自定义的子线程的方法
     * 这里主要需要解决的问题是资源文件得到后怎么把数据传递给UI线程的Activity
     */

    public class MyIntentService extends IntentService {
        TransDataModel mTransDataModel;
        private String curDate = TimeUtils.getDate(System.currentTimeMillis());

        List<Double> Month30KeliuList = new ArrayList<>();//本月30天的客流量
        List<Double> LastMonth30KeliuList = new ArrayList<>();//上个月30天的客流量

        List<String> monthDateStrList = new ArrayList<>();//本月30天的日期字符串list
        List<String> LastmonthDateStrList = new ArrayList<>();//上个月30天的日期字符串list

        List<Float> LastMonth30Keliudangjie = new ArrayList<>();
        List<Float> Month30Keliudangjie = new ArrayList<>();
        /**
         * 通过构造方法，传入子线程的名字
         * 但是这里必须要创建一个无参的构造方法
         */
        public MyIntentService() {
            super("myService");
            Log.d("MyIntentSer", "MyIntentService()");
        }

        /**
         * 这是在子线程中的执行操作
         */
        @Override
        protected void onHandleIntent(Intent intent) {
            Log.d("MyIntentSer", "子线程开始工作");
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
//                                                    sendEmptyMessage(212);




                                                    List<Double> benMonthJiaoyiList = new ArrayList<>();
                                                    Log.d("benMonthJiaoyiList", Month30KeliuList.size() + "   " + monthDateStrList.size() + ">>>>  ");
                                                    for (String ds : monthDateStrList) {
                                                        Double transAmout = coupterUtil.toDayAmountZong(mTransDataModel, ds);
                                                        benMonthJiaoyiList.add(transAmout);
                                                        Log.d("benMonthJiaoyiList", transAmout + "");
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
                                                            Log.d("Month30Keliudangjie", "");
                                                        }
                                                    }
                                                    for (Float f : Month30Keliudangjie) {
                                                        Log.d("Month30KeliudajieTwwwww", f + "");
                                                    }


                                                    List<Double> LastMonthJiaoyiList = new ArrayList<>();
                                                    Log.d("LastMonthJiaoyiList", LastMonth30KeliuList.size() + "   " + LastmonthDateStrList.size() + ">>>>  ");
                                                    for (String ds : LastmonthDateStrList) {
                                                        Double transAmout = coupterUtil.toDayAmountZong(mTransDataModel, ds);
                                                        LastMonthJiaoyiList.add(transAmout);
                                                        Log.d("LastMonthJiaoyiList", transAmout + "");
                                                    }


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
                                                            Log.d("LastMonth30Keliudangjie", "");
                                                        }
                                                    }
                                                    for (Float f : LastMonth30Keliudangjie) {
                                                        Log.d("LastMonthdangjieTwwwww", f + "");
                                                    }

                                                    EventBus.getDefault().post(new MessageEvent(Month30Keliudangjie, LastMonth30Keliudangjie));


                                                }
                                            }

                                            @Override
                                            public void onDataNotAvaliable() {

                                            }
                                        });
                                    }
                                });




                            }
                        }

                        @Override
                        public void onDataNotAvaliable() {
                            Log.d("Month30KeliuList", "onDataNotAvaliable");
                        }
                    });
                }
            });


        }

        @Override
        public void onCreate() {
            super.onCreate();
            mTransDataModel = new TransDataModel(getApplicationContext());
            mTransDataModel.bind();
            Log.d("MyIntentSer", "onCreate");
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
           getApplicationContext().unbindService(mTransDataModel.mConnection);

            Log.d("MyIntentSer", "onDestroy");
        }
    }
