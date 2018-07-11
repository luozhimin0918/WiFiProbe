package com.ums.wifiprobe.app;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import com.marswin89.marsdaemon.DaemonApplication;
import com.marswin89.marsdaemon.DaemonConfigurations;
import com.ums.wifiprobe.data.database.helper.MacBrandXmlPusher;
import com.ums.wifiprobe.keepalive.Receiver1;
import com.ums.wifiprobe.keepalive.Receiver2;
import com.ums.wifiprobe.keepalive.Service2;
import com.ums.wifiprobe.keepalive.Service3;
import com.ums.wifiprobe.service.ProbeService;
import com.ums.wifiprobe.service.greendao.BrandTotalInfoDao;
import com.ums.wifiprobe.service.greendao.DaoMaster;
import com.ums.wifiprobe.service.greendao.DaoSession;
import com.ums.wifiprobe.service.greendao.MacBrandInfo;
import com.ums.wifiprobe.service.greendao.MacBrandInfoDao;
import com.ums.wifiprobe.service.greendao.MacProbeInfoDao;
import com.ums.wifiprobe.service.greendao.MacStatisticsInfoDao;
import com.ums.wifiprobe.service.greendao.MacTotalInfoDao;
import com.ums.wifiprobe.service.greendao.MacWhiteListDao;
import com.ums.wifiprobe.service.greendao.RssiInfoDao;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by chenzhy on 2017/9/13.
 */

public class WPApplication extends DaemonApplication {
    public static DaoSession gDaoSession;
    public static MacBrandInfoDao gMacBrandInfoDao;
    public static MacStatisticsInfoDao gMacStatisticsInfoDao;
    public static MacProbeInfoDao gMacProbeInfoDao;
    public static MacTotalInfoDao gMacTotalInfoDao;
    public static BrandTotalInfoDao gBrandTotalInfoDao;
    public static RssiInfoDao gRssiInfoDao;
    public static MacWhiteListDao gMacWhiteListDao;
    public static WPApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("csf1", "APP onCreate");
        instance = this;
        GlobalValueManager.getInstance().init(this);
        if (Build.VERSION.SDK_INT <= 19 && GlobalValueManager.getInstance().isFirstOpenApp()) {
            startService(new Intent(this, Service3.class));//保活用 android4.4版本会出问题，故临时启动一个主线程service，app第一次启动即可
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                initDataBase();
                GlobalValueManager.getInstance().setLeaveIntervalTime(300000);
                GlobalValueManager.getInstance().setUploadIntervalTime(600000);//先这样吧，等设置界面出来再取消
            }
        }.start();
    }

    public static void initDataBase() {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(instance, "probe-db", null);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        gDaoSession = daoMaster.newSession();

        gMacBrandInfoDao = gDaoSession.getMacBrandInfoDao();
        gMacStatisticsInfoDao = gDaoSession.getMacStatisticsInfoDao();
        gMacProbeInfoDao = gDaoSession.getMacProbeInfoDao();
        gMacTotalInfoDao = gDaoSession.getMacTotalInfoDao();
        gBrandTotalInfoDao = gDaoSession.getBrandTotalInfoDao();
        gRssiInfoDao = gDaoSession.getRssiInfoDao();
        gMacWhiteListDao = gDaoSession.getMacWhiteListDao();
        MacWhiteListDao.createTable(gDaoSession.getDatabase(), true);//先建表把，后期会做数据库升级处理
        //清理15日前 ProbeInfo表数据
        //将ProbeInfo表数据，从昨日开始将统计数据存入统计表中，逐日存入，直至原始表无数据，或统计表已存在
        initMacBrandDataBase();
        new DataBaseInitWorkTask().execute();

    }

    public static void initMacBrandDataBase() {
        long count = gMacBrandInfoDao.queryBuilder().count();
        if (count < 1000) {
            //初始值为1500左右，若小于1000，则判断为数据库被清除
            try {
                InputStream inputStream = instance.getAssets().open("macoui.xml");
                List<MacBrandInfo> list = MacBrandXmlPusher.getParams(inputStream);
                gMacBrandInfoDao.insertInTx(list);
            } catch (IOException e) {
                Log.d("TAG", e.toString());
            } catch (Throwable throwable) {
                Log.d("TAG", throwable.toString());
            }
        }
    }

    public static WPApplication getInstance() {
        return instance;
    }


    public interface LocationChangedListener {
        void onLocationChanged(double distance);
    }

    public static void onLocationChanged(double distance) {
        if (locationChangedListener != null) {
            locationChangedListener.onLocationChanged(distance);
        }
    }

    public static void setLocationListener(LocationChangedListener listener) {
        locationChangedListener = listener;
    }

    static LocationChangedListener locationChangedListener;


    /**
     * you can override this method instead of {@link android.app.Application attachBaseContext}
     *
     * @param base
     */
    @Override
    public void attachBaseContextByDaemon(Context base) {
        super.attachBaseContextByDaemon(base);
    }


    /**
     * give the configuration to lib in this callback
     *
     * @return
     */
    @Override
    protected DaemonConfigurations getDaemonConfigurations() {
        DaemonConfigurations.DaemonConfiguration configuration1 = new DaemonConfigurations.DaemonConfiguration(
                "com.ums.wifiprobe:process2",
                ProbeService.class.getCanonicalName(),
                Receiver1.class.getCanonicalName());

        DaemonConfigurations.DaemonConfiguration configuration2 = new DaemonConfigurations.DaemonConfiguration(
                "com.ums.wifiprobe:process1",
                Service2.class.getCanonicalName(),
                Receiver2.class.getCanonicalName());

        DaemonConfigurations.DaemonListener listener = new WPApplication.MyDaemonListener();
        //return new DaemonConfigurations(configuration1, configuration2);//listener can be null
        if(Build.VERSION.SDK_INT>23){
            return null;//屏蔽android 7.0系统
        }
        return new DaemonConfigurations(configuration1, configuration2, listener);
    }


    class MyDaemonListener implements DaemonConfigurations.DaemonListener {
        @Override
        public void onPersistentStart(Context context) {
        }

        @Override
        public void onDaemonAssistantStart(Context context) {
        }

        @Override
        public void onWatchDaemonDaed() {
        }
    }

}
