package com.ums.wifiprobe.app;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.ums.wifiprobe.service.greendao.BrandTotalInfoDao;
import com.ums.wifiprobe.service.greendao.MacProbeInfo;
import com.ums.wifiprobe.service.greendao.MacProbeInfoDao;
import com.ums.wifiprobe.service.greendao.MacStatisticsInfoDao;
import com.ums.wifiprobe.service.greendao.MacTotalInfoDao;
import com.ums.wifiprobe.service.greendao.RssiInfoDao;
import com.ums.wifiprobe.utils.TimeUtils;

import java.util.List;

/**
 * Created by chenzhy on 2017/9/19.
 */

public class DataBaseInitWorkTask extends AsyncTask {

    @Override
    protected Object doInBackground(Object[] params) {


        //清理mac详情表数据
        if(WPApplication.gMacProbeInfoDao!=null){
        long macCount = WPApplication.gMacProbeInfoDao.queryBuilder().count();
        if(macCount>300000){
            WPApplication.gDaoSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    WPApplication.gMacProbeInfoDao.queryBuilder().orderAsc(MacProbeInfoDao.Properties.Id).limit(150000).buildDelete().executeDeleteWithoutDetachingEntities();

                }
            });
        }}

        //清理mac首次出现统计表详情表数据
        if(WPApplication.gMacStatisticsInfoDao!=null){
        long statisticsCount = WPApplication.gMacStatisticsInfoDao.queryBuilder().count();
        if (statisticsCount > 200000) {
            //删除只出现1次的mac地址，极有可能是随机MAC
            WPApplication.gDaoSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    WPApplication.gMacStatisticsInfoDao.queryBuilder().where(MacStatisticsInfoDao.Properties.ProbeTimes.lt(2)).orderAsc(MacStatisticsInfoDao.Properties.CreateTimes).limit(100000).buildDelete().executeDeleteWithoutDetachingEntities();
                }
            });
        }}

        //清理mac数据统计表
        if(WPApplication.gMacTotalInfoDao!=null){
        long macTotalInfoCount = WPApplication.gMacTotalInfoDao.queryBuilder().count();
        if(macTotalInfoCount>200000){
            WPApplication.gDaoSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    WPApplication.gMacTotalInfoDao.queryBuilder().orderAsc(MacTotalInfoDao.Properties.Id).limit(100000).buildDelete().executeDeleteWithoutDetachingEntities();
                }
            });
        }}

        //清理品牌统计表
        if(WPApplication.gBrandTotalInfoDao!=null){
        long brandTotalInfoCount = WPApplication.gBrandTotalInfoDao.queryBuilder().count();
        if(brandTotalInfoCount>200000){
            WPApplication.gDaoSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    WPApplication.gBrandTotalInfoDao.queryBuilder().orderAsc(BrandTotalInfoDao.Properties.Id).limit(100000).buildDelete().executeDeleteWithoutDetachingEntities();
                }
            });
        }}

        //清理远中近统计表
        if(WPApplication.gRssiInfoDao!=null){
        long rssiInfoCount = WPApplication.gRssiInfoDao.count();
        if(rssiInfoCount>200000){
            WPApplication.gDaoSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    WPApplication.gRssiInfoDao.queryBuilder().orderAsc(RssiInfoDao.Properties.Rssi_id).limit(100000).buildDelete().executeDeleteWithoutDetachingEntities();
                }
            });
        }}

        GlobalValueManager.getInstance().setCheckDatabase(System.currentTimeMillis(), true);

        return null;
    }


}
