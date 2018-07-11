package com.ums.wifiprobe.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ums.AppHelper;
import com.ums.wifiprobe.CommonStants;
import com.ums.wifiprobe.app.GlobalValueManager;
import com.ums.wifiprobe.app.ThreadPoolProxyFactory;
import com.ums.wifiprobe.app.WPApplication;
import com.ums.wifiprobe.net.requestentity.ProbeInfoRequestEntity;
//import com.ums.wifiprobe.service.greendao.DaoProbeInfoDao;
import com.ums.wifiprobe.service.greendao.MacProbeInfo;
import com.ums.wifiprobe.service.greendao.MacProbeInfoDao;
import com.ums.wifiprobe.service.probeentity.ProbeInfoEntityWrap;
import com.ums.wifiprobe.utils.LocationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by chenzhy on 2017/9/14.
 */

public class LocalProbeDataManager extends BaseProbeDataManager {

    private static final String TAG = "WiFiprobe-Datamanager";

    @Override
    synchronized void newProbeInfoArrived(ProbeInfoEntityWrap probeInfoWrap) {
        long lastIntervalTime = System.currentTimeMillis() - ProbeServiceConstant.probeAgainInterval;
        boolean isNew;
        List<MacProbeInfo> list = WPApplication.gMacProbeInfoDao.queryBuilder().where(MacProbeInfoDao.Properties.Mac.eq(probeInfoWrap.getMac()), MacProbeInfoDao.Properties.LastTime.gt(lastIntervalTime)).list();
        if(list.size()>1){
            Log.d("LocalProbeDataManager","dataRepeat:"+new Gson().toJson(list.get(0)));
        }

        MacProbeInfo macProbeInfo = WPApplication.gMacProbeInfoDao.queryBuilder().where(MacProbeInfoDao.Properties.Mac.eq(probeInfoWrap.getMac()), MacProbeInfoDao.Properties.LastTime.gt(lastIntervalTime)).unique();
        if (macProbeInfo == null) {
            isNew = true;
            macProbeInfo = new MacProbeInfo(probeInfoWrap);
        } else {
            isNew = false;
            macProbeInfo.setLeaveTime(0);
            macProbeInfo.setLastTime(probeInfoWrap.getLastTime());
            macProbeInfo.setLeaveHour(0);
            macProbeInfo.setProbeTimes(macProbeInfo.getProbeTimes() + probeInfoWrap.getProbeTimes());
            macProbeInfo.setDurationTime(macProbeInfo.getLastTime() - macProbeInfo.getCreateTime());
            probeInfoWrap.setCreateTime(macProbeInfo.getCreateTime());
            probeInfoWrap.setCreateHour(macProbeInfo.getCreateHour());
            probeInfoWrap.setDurationTime(macProbeInfo.getDurationTime());
            probeInfoWrap.setProbeTimes(0);
        }

        final MacProbeInfo finalMacProbeInfo = macProbeInfo;
        final boolean tempNew = isNew;
        synchronized (WPApplication.gDaoSession) {
            ThreadPoolProxyFactory.getUpdateThreadPoolProxy().execute(new Runnable() {
                @Override
                public void run() {
                    if (tempNew) {
//                        Log.d(TAG, "probeInfo Insert：" + new Gson().toJson(finalMacProbeInfo));

                        WPApplication.gMacProbeInfoDao.insert(finalMacProbeInfo);
                    } else {
//                        Log.d(TAG, "probeInfo Update：" + new Gson().toJson(finalMacProbeInfo));
                        WPApplication.gMacProbeInfoDao.update(finalMacProbeInfo);
                    }
                }
            });
        }
    }


    @Override
    void probeInfoUpdated(ProbeInfoEntityWrap probeInfoWrap) {

    }


    @Override
    void probeInfoLeaved(final Collection<ProbeInfoEntityWrap> probeInfoWrapList) {

        long lastIntervalTime = System.currentTimeMillis() - ProbeServiceConstant.probeAgainInterval;
        long lastLeaveTime = System.currentTimeMillis() - ProbeServiceConstant.leaveInterval - BaseProbeDataManager.LEAVE_INTERVAL_TIME;
        //待优化，线程池
        List<MacProbeInfo> probeInfoList1 = new ArrayList<>();
        List<MacProbeInfo> probeInfoList2 = new ArrayList<>();
        for (ProbeInfoEntityWrap wrap : probeInfoWrapList) {

            MacProbeInfo macProbeInfo = WPApplication.gMacProbeInfoDao.queryBuilder().where(MacProbeInfoDao.Properties.Mac.eq(wrap.getMac()), MacProbeInfoDao.Properties.LastTime.gt(lastIntervalTime)).unique();

            if (macProbeInfo != null) {
                macProbeInfo.setRssi(Integer.valueOf(wrap.getRssi()));
                macProbeInfo.setLastTime(wrap.getLastTime());
                macProbeInfo.setProbeTimes(macProbeInfo.getProbeTimes() + wrap.getProbeTimes());
                macProbeInfo.setLeaveTime(wrap.getLeaveTime());
                macProbeInfo.setLeaveHour(wrap.getLeaveHour());
                macProbeInfo.setDurationTime(macProbeInfo.getLastTime() - macProbeInfo.getCreateTime());
                probeInfoList1.add(macProbeInfo);
//                Log.d(TAG, "probeInfoLeaved Update：" + new Gson().toJson(macProbeInfo));
            } else {
                macProbeInfo = new MacProbeInfo(wrap);
                probeInfoList2.add(macProbeInfo);
//                Log.d(TAG, "probeInfoLeaved Insert：" + new Gson().toJson(macProbeInfo));
            }
        }
        synchronized (WPApplication.gDaoSession) {
            if (probeInfoList1.size() > 0) {
                final List<MacProbeInfo> finalProbeInfoList = probeInfoList1;
                ThreadPoolProxyFactory.getUpdateThreadPoolProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        WPApplication.gMacProbeInfoDao.updateInTx(finalProbeInfoList);
//                        for (int i = 0; i < finalProbeInfoList.size(); i++) {
//                            Log.d("LocalProbeDataManager", "probeInfoLeaved update：" + finalProbeInfoList.get(i).toString());
//                        }
                    }
                });
            }
            if (probeInfoList2.size() > 0) {
                final List<MacProbeInfo> finalProbeInfoList = probeInfoList2;
                ThreadPoolProxyFactory.getUpdateThreadPoolProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        WPApplication.gMacProbeInfoDao.insertInTx(finalProbeInfoList);
//                        for (int i = 0; i < finalProbeInfoList.size(); i++) {
//                            Log.d("LocalProbeDataManager", "probeInfoLeaved insert：" + finalProbeInfoList.get(i).toString());
//                        }
                    }
                });
            }
        }
    }


    @Override
    void uploadToServer(int number) {
//        packageUploadList(number);
        //组装JSON
//        ProbeInfoRequestEntity entity = packageUploadList(number);
        //retrofit上传
        //上传成功后删除数据库中的数据
        try {
            String json = AppHelper.getBaseSysInfo(WPApplication.getInstance());
//            Log.d("TAG","JSON--"+json);
            if (json == null)
                return;
            JSONObject jsonObject = new JSONObject(json);
            JSONObject tempJsonObject = jsonObject.getJSONObject("Location");
            String latitude = tempJsonObject.getString("Latitude");
            String longitude = tempJsonObject.getString("Longitude");
//            Log.d("TAG","Latitude="+latitude+" Longitude="+longitude);
            if (latitude != null && longitude != null && Float.valueOf(latitude) > 0 && Float.valueOf(longitude) > 0) {

                if (ProbeServiceConstant.longitude.equals("0.0") || ProbeServiceConstant.latitude.equals("0.0")) {
                    ProbeServiceConstant.longitude = longitude;//第一次成功定位
                    ProbeServiceConstant.latitude = latitude;
                    GlobalValueManager.getInstance().setLatitude(latitude);
                    GlobalValueManager.getInstance().setLongitude(longitude);
                } else {
                    double distance = LocationUtils.Distance(Double.valueOf(longitude), Double.valueOf(latitude), Double.valueOf(ProbeServiceConstant.longitude), Double.valueOf(ProbeServiceConstant.latitude));

                    Log.d(TAG, "The distance to lasttime :)" + distance);
                    if (distance > ProbeServiceConstant.locationDistance || distance < -1 * ProbeServiceConstant.locationDistance) {
                        //位置变动，重新定位
                        ProbeServiceConstant.longitude = longitude;
                        ProbeServiceConstant.latitude = latitude;
                        GlobalValueManager.getInstance().setLatitude(latitude);
                        GlobalValueManager.getInstance().setLongitude(longitude);
                    }
                    WPApplication.onLocationChanged(distance);
                }

            }
        } catch (JSONException e) {
            Log.d(TAG, "get location failure :" + e.toString());
            return;
        }


    }
//    //组装JSON
//    private ProbeInfoRequestEntity packageUploadList(int number){
//        List<ProbeInfo> list = WPApplication.gProbeInfoDao.queryBuilder().where(DaoProbeInfoDao.Properties.LeaveTime.notEq("0")).limit(number).list();
//        if(list==null||list.size()<1)
//            return null;
//        ProbeInfoRequestEntity uploadEntity = new ProbeInfoRequestEntity();
//        ProbeInfoRequestEntity.BaseInfo baseInfo;
////        try {
////            JSONObject jsonObject = new JSONObject(AppHelper.getBaseSysInfo(WPApplication.getInstance()));
////            latitude = jsonObject.getString("Latitude");
////            longitude = jsonObject.getString("Longitude");
////        } catch (JSONException e) {
////            e.printStackTrace();
//            //用之前的经纬度
////            if(latitude==null||longitude==null)
////                return null;
////        }
//        latitude = "11.11111";
//        longitude = "22.22222";
//        baseInfo = uploadEntity.new BaseInfo(CommonStants.SN,latitude,longitude);
//        List<String> deviceList = new ArrayList<>();
//        for(ProbeInfo info:list){
//            StringBuilder sb = new StringBuilder();
//            sb.append(info.getMac()).append("|").append(info.getRssi()).append("|").append(info.getCreateTime()).append("|").append(info.getLeaveTime());
//            deviceList.add(sb.toString());
//        }
//        uploadEntity.setBaseInfo(baseInfo);
//        uploadEntity.setDevice_list(deviceList);
//        Log.d("LocalProbeDataManager",uploadEntity.toJson());
//        return uploadEntity;
//    }
}
