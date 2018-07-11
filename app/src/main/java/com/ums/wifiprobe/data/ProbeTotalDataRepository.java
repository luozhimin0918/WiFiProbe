package com.ums.wifiprobe.data;

import android.util.Log;

import com.ums.wifiprobe.data.database.LocalProbeTotalDataResource;
import com.ums.wifiprobe.service.greendao.MacTotalInfo;
import com.ums.wifiprobe.utils.TimeUtils;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chenzhy on 2017/9/19.
 */

public class ProbeTotalDataRepository implements DataResource<MacTotalInfo> {
    //先从缓存读，若没有，则从数据统计表中读，若还没有，则从原始表中读，读完存至数据统计表，存至缓存，当天的数据不存
    private static ProbeTotalDataRepository instance = null;
    private LocalProbeTotalDataResource localProbeDataManager;
    private Map<String, MacTotalInfo> mCachedProbeInfos;
    private Map<String, List<MacTotalInfo>> mCachedProbeInfoList;

    private Map<String,String> queryingTaskMap = new ConcurrentHashMap<>();//防止上层代码多次调用查询同一数据，防止并发错误，导致数据库重复插入
    private Map<String,String> queryingTasksMap = new ConcurrentHashMap<>();//防止上层代码多次调用查询同一数据，防止并发错误，导致数据库重复插入

    private ProbeTotalDataRepository() {
        this.localProbeDataManager = new LocalProbeTotalDataResource();
    }

    //string cun scale|scalevalue|date
    public static ProbeTotalDataRepository getInstance() {
        if (instance == null) {
            instance = new ProbeTotalDataRepository();
        }
        return instance;
    }

    @Override
    public void getTask(final String scaleValue, final String scale, final String date, final GetTaskCallback<MacTotalInfo> callback) {
        Log.e("WiFiProbe-Data","getTask--ProbeTotalData-- scaleValue="+scaleValue+" scale="+scale+" date="+date);
        final String key = scale + "|" + scaleValue + "|" + date;
        if(queryingTaskMap.containsKey(key)){
            Log.e("WiFiProbe-Data","thisTask is getting ,can't get again !");
            return;
        }else{
            queryingTaskMap.put(key,key);
        }
        if (mCachedProbeInfos == null) {
            mCachedProbeInfos = new LinkedHashMap<>();
        } else {
            if (mCachedProbeInfos.containsKey(key)) {
                queryingTaskMap.remove(key);
                if (callback != null)
                    callback.OnTaskLoaded(mCachedProbeInfos.get(key));
                return;
            }
        }
        localProbeDataManager.getTask(scaleValue, scale, date, new GetTaskCallback<MacTotalInfo>() {

            @Override
            public void OnTaskLoaded(MacTotalInfo info) {
                queryingTaskMap.remove(key);
                if (callback != null)
                    callback.OnTaskLoaded(info);
                String[] ss = scaleValue.split("-");
                String tempScaleValue ="";
                if(scale.equals("month")||scale.equals("months")||scale.equals("week")||scale.equals("weeks")){
                    tempScaleValue = ss[1];
                }
                boolean isCurOrAfterYear = TimeUtils.getYear(TimeUtils.getTimeMillions(date))>=TimeUtils.getYear(System.currentTimeMillis())?true:false;

                if ((scale.equals("month")||scale.equals("months")) && isCurOrAfterYear&&Integer.valueOf(tempScaleValue) >= TimeUtils.getMonthsOfYear(TimeUtils.getDate(System.currentTimeMillis()))) {
                    //当周内统计数据不需要缓存
                    return;
                }

                if ((scale.equals("week")||scale.equals("weeks")) && isCurOrAfterYear&&Integer.valueOf(tempScaleValue) >= TimeUtils.getWeeksOfYear(TimeUtils.getDate(System.currentTimeMillis()))) {
                    //当周内统计数据不需要缓存
                    return;
                }
                if ((scale.equals("day")||scale.equals("days")) && isCurOrAfterYear&&TimeUtils.getTimeMillions(date)>=TimeUtils.getTimeMillions(TimeUtils.getDate(System.currentTimeMillis()))) {
                    //当天内统计数据不需要缓存
                    return;
                }
                if (scale.equals("hour") && isCurOrAfterYear&&Integer.valueOf(scaleValue)>= TimeUtils.getHour(System.currentTimeMillis())) {
                    //当小时内统计数据不需要缓存
                    return;
                }
                mCachedProbeInfos.put(key, info);

            }

            @Override
            public void onDataNotAvaliable() {
                queryingTaskMap.remove(key);
                if (callback != null)
                    callback.onDataNotAvaliable();
            }
        });
    }

    //scale day week
    @Override
    public void getTasks(final String scaleValue, final String scale, final String date, final LoadTasksCallback<MacTotalInfo> callback) {
        Log.e("WiFiProbe-Data","getTasks--ProbeTotalData-- scaleValue="+scaleValue+" scale="+scale+" date="+date);
        final String key = scale + "|" + scaleValue + "|" + date;
        if(queryingTasksMap.containsKey(key)){
            Log.e("WiFiProbe-Data","thisTasks is getting ,can't get again !");
            return;
        }else{
            queryingTasksMap.put(key,key);
        }
        if (mCachedProbeInfoList == null) {
            mCachedProbeInfoList = new LinkedHashMap<>();
        } else {
            if (mCachedProbeInfoList.containsKey(key)) {
                Log.e("WiFiProbe-Data","ProbeTotalData-- scaleValue="+scaleValue+" scale="+scale+" date="+date+" load data from cache");
                queryingTasksMap.remove(key);
                if (callback != null)
                    callback.onTasksLoaded(mCachedProbeInfoList.get(key));
                return;
            }
        }
        localProbeDataManager.getTasks(scaleValue, scale, date, new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List list) {
                queryingTasksMap.remove(key);
                if (callback != null)
                    callback.onTasksLoaded(list);

                String[] ss = scaleValue.split("-");
                String tempScaleValue ="";
                if(scale.equals("month")||scale.equals("months")||scale.equals("week")||scale.equals("weeks")){
                    tempScaleValue = ss[1];
                }

                boolean isCurOrAfterYear = TimeUtils.getYear(TimeUtils.getTimeMillions(date))>=TimeUtils.getYear(System.currentTimeMillis())?true:false;
                if ((scale.equals("month")||scale.equals("months")) && isCurOrAfterYear&&Integer.valueOf(tempScaleValue) >= TimeUtils.getMonthsOfYear(TimeUtils.getDate(System.currentTimeMillis()))) {
                    //当月内统计数据不需要缓存
                    return;
                }

                if ((scale.equals("week")||scale.equals("weeks")) &&isCurOrAfterYear&& Integer.valueOf(tempScaleValue) >= TimeUtils.getWeeksOfYear(TimeUtils.getDate(System.currentTimeMillis()))) {
                    //当周内统计数据不需要缓存
                    return;
                }
                if ((scale.equals("day")||scale.equals("days")) && isCurOrAfterYear&&TimeUtils.getTimeMillions(date)>=TimeUtils.getTimeMillions(TimeUtils.getDate(System.currentTimeMillis()))) {
                    //当天内统计数据不需要缓存
                    return;
                }

                mCachedProbeInfoList.put(key, list);
            }

            @Override
            public void onDataNotAvaliable() {
                queryingTasksMap.remove(key);
                if (callback != null)
                    callback.onDataNotAvaliable();
            }
        });
    }

    @Override
    public void saveTask(MacTotalInfo info) {

    }

    @Override
    public void saveTasks(List<MacTotalInfo> list) {

    }

    @Override
    public void clearTasks() {
        if(mCachedProbeInfoList!=null){
            mCachedProbeInfoList.clear();
        }
        if(mCachedProbeInfos!=null){
            mCachedProbeInfos.clear();
        }
    }


}
