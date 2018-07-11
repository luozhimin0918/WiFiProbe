package com.ums.wifiprobe.service;

import android.os.Handler;
import android.util.Log;
import com.ums.wifiprobe.app.ThreadPoolProxyFactory;
import com.ums.wifiprobe.app.WPApplication;
import com.ums.wifiprobe.service.greendao.MacStatisticsInfo;
import com.ums.wifiprobe.service.greendao.MacStatisticsInfoDao;
import com.ums.wifiprobe.service.probeentity.ProbeInfoEntityWrap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chenzhy on 2017/9/14.
 */

public abstract class BaseProbeDataManager {

    private static final String TAG = "WiFiprobe-Datamanager";

    public static final int PROBE_INTERVAL_TIME = 60000;//60秒检测1次
    public static final int LEAVE_INTERVAL_TIME = PROBE_INTERVAL_TIME * 1;//1分钟判断1次,wifi设备是否已离开

    public static ConcurrentHashMap<String, ProbeInfoEntityWrap> localMap = new ConcurrentHashMap();//当前已探测的设备mac集合，用于前端展示

    Handler handler = new Handler();

    ProbeWiFiReceiveThread receiveThread;
    ProbeWiFiModifyThread modifyThread;


    class ProbeWiFiReceiveThread extends Thread {
        ArrayBlockingQueue<ProbeInfoEntityWrap> sQueue = new ArrayBlockingQueue<ProbeInfoEntityWrap>(2048);

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    ProbeInfoEntityWrap info = sQueue.poll();
                    if (info != null) {
                        synchronized (getClass()) {
                            saveData(info);
                        }
                    }
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    interrupt();
                    sQueue.clear();
                    sQueue = null;
                }
            }
        }

        public synchronized void addQueue(ProbeInfoEntityWrap wifiInfo) {
            if(sQueue.size()>1000){
                Log.d(TAG,"data size is too large："+sQueue.size()+" sub data had too much data！");//上层处理速度慢或底层数据量太多
            }
            sQueue.add(wifiInfo);
        }
        public synchronized void clearQueue(){
            sQueue.clear();
        }
    }

    public void clearCache(){
        receiveThread.clearQueue();
    }

    class ProbeWiFiModifyThread extends Thread {

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    sleep(LEAVE_INTERVAL_TIME);
                    refreshData();
                } catch (InterruptedException e) {
                    interrupt();
                }
            }
        }
    }

    public void addProbeInfo(ProbeInfoEntityWrap info) {
        if (receiveThread != null && !receiveThread.isInterrupted()) {
            receiveThread.addQueue(info);
        }
    }

    public void startProbe() {
        receiveThread = new ProbeWiFiReceiveThread();
        modifyThread = new ProbeWiFiModifyThread();
        receiveThread.start();
        modifyThread.start();
        handler.postDelayed(uploadRunnable, ProbeServiceConstant.uploadInterval);//半分钟定位次
    }


    public void closeProbe() {
        if (receiveThread != null) {
            receiveThread.interrupt();
            receiveThread = null;
            modifyThread.interrupt();
            modifyThread = null;
        }
        handler.removeCallbacks(uploadRunnable);
    }

    Runnable uploadRunnable = new Runnable() {
        @Override
        public void run() {
            uploadToServer(1000);//设置为1000条，单词上传，后期改为可设置
            handler.postDelayed(this, ProbeServiceConstant.uploadInterval);
        }
    };


    //-------数据过滤策略----------//
    protected void saveData(ProbeInfoEntityWrap probeInfoWrap) {
        String mac = probeInfoWrap.getMac();
        if (localMap.containsKey(mac)) {
            ProbeInfoEntityWrap wrap = localMap.get(mac);
            if (Integer.valueOf(wrap.getRssi()) < Integer.valueOf(probeInfoWrap.getRssi())) {
                wrap.setRssi(probeInfoWrap.getRssi());
            }
            wrap.addProbeTimes();//再次探测到，新增探测次数
            long lastTime = wrap.getLastTime();
            wrap.setLastTime(System.currentTimeMillis());

            if ((probeInfoWrap.getLastTime() - lastTime) <= 2000) {
                //过滤掉无用数据
                return;
            }

            newProbeInfoArrived(wrap);

//            if (lastTime == wrap.getCreateTime()) {
//                //第二次扫描到，新插入数据库
////                Log.d("TAG", "update and insert mac:" + wrap.getMac() + " --createTime:" + wrap.getCreateTime());
//                newProbeInfoArrived(wrap);
//            } else {
//                //之前已扫描到，更新数据库
////                Log.d("TAG", "update  mac:" + probeInfoWrap.getMac() + " --createTime:" + wrap.getCreateTime());
//                probeInfoUpdated(wrap);//2次间隔时间大于1秒才算,,,先查询再修改
//
//            }


        } else {
//            Log.d("TAG", "add new mac:" + probeInfoWrap.getMac() + " --createTime:" + probeInfoWrap.getCreateTime());
            localMap.put(probeInfoWrap.getMac(), probeInfoWrap);
            MacStatisticsInfo macStatisticsInfo = WPApplication.gMacStatisticsInfoDao.queryBuilder().where(MacStatisticsInfoDao.Properties.Mac.eq(probeInfoWrap.getMac())).unique();
            if(macStatisticsInfo==null){
                macStatisticsInfo = new MacStatisticsInfo();
                macStatisticsInfo.setMac(probeInfoWrap.getMac());
                macStatisticsInfo.setProbeTimes(1);
                macStatisticsInfo.setCreateTimes(probeInfoWrap.getCreateTime());
            }else{
                macStatisticsInfo.setProbeTimes(macStatisticsInfo.getProbeTimes()+1);
            }
            final MacStatisticsInfo info = macStatisticsInfo;
            synchronized (WPApplication.gDaoSession){
                ThreadPoolProxyFactory.getUpdateThreadPoolProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        WPApplication.gMacStatisticsInfoDao.insertOrReplace(info);
                    }
                });
            }
            }


    }

    ;

    protected void refreshData() {
        long curTime = System.currentTimeMillis();
        Iterator<Map.Entry<String, ProbeInfoEntityWrap>> iterator = localMap.entrySet().iterator();
        Set<ProbeInfoEntityWrap> waitDeleteList = new HashSet<>();
        while (iterator.hasNext()) {
            Map.Entry<String, ProbeInfoEntityWrap> entry = iterator.next();
            if (curTime - entry.getValue().getLastTime() > ProbeServiceConstant.leaveInterval) {
                entry.getValue().setLeaveTime(curTime);
                waitDeleteList.add(entry.getValue());
                iterator.remove();//删除该元素
            }
        }
        probeInfoLeaved(waitDeleteList);
    }


    //-------最终数据处理策略-------//
    abstract void newProbeInfoArrived(ProbeInfoEntityWrap t);

    abstract void probeInfoUpdated(ProbeInfoEntityWrap t);

    abstract void probeInfoLeaved(Collection<ProbeInfoEntityWrap> t);//批量删除，并更新数据库，如果仅扫描到1次，则插入，否则更新

    abstract void uploadToServer(int number);
}
