package com.ums.wifiprobe.data;

import android.util.Log;

import com.ums.wifiprobe.data.database.LocalBrandTotalDataResource;
import com.ums.wifiprobe.data.database.LocalProbeTotalDataResource;
import com.ums.wifiprobe.service.greendao.BrandTotalInfo;
import com.ums.wifiprobe.service.greendao.MacTotalInfo;
import com.ums.wifiprobe.utils.TimeUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chenzhy on 2017/9/19.
 */

public class BrandTotalDataRepository implements DataResource<BrandTotalInfo> {
    //先从缓存读，若没有，则从数据统计表中读，若还没有，则从原始表中读，读完存至数据统计表，存至缓存，当天的数据不存
    private static BrandTotalDataRepository instance = null;
    private LocalBrandTotalDataResource localProbeDataManager;
    private Map<String, BrandTotalInfo> mCachedBrandInfos;
    private Map<String, List<BrandTotalInfo>> mCachedBrandInfoList;

    private Map<String,String> queryingTaskMap = new ConcurrentHashMap<>();//防止上层代码多次调用查询同一数据，防止并发错误，导致数据库重复插入
    private Map<String,String> queryingTasksMap = new ConcurrentHashMap<>();//防止上层代码多次调用查询同一数据，防止并发错误，导致数据库重复插入

    private BrandTotalDataRepository() {
        this.localProbeDataManager = new LocalBrandTotalDataResource();
    }

    //string cun scale|scalevalue|date
    public static BrandTotalDataRepository getInstance() {
        if (instance == null) {
            instance = new BrandTotalDataRepository();
        }
        return instance;
    }

    @Override
    public void getTask(final String scaleValue, final String scale, final String date, final GetTaskCallback<BrandTotalInfo> callback) {
        Log.e("BrandTotal-Data","getTask--BrandTotalData-- scaleValue="+scaleValue+" scale="+scale+" date="+date);
        final String key = scale + "|" + scaleValue + "|" + date;
        if(queryingTaskMap.containsKey(key)){
            Log.e("BrandTotal-Data","thisTask is getting ,can't get again !");
            return;
        }else{
            queryingTaskMap.put(key,key);
        }
        if (mCachedBrandInfos == null) {
            mCachedBrandInfos = new LinkedHashMap<>();
        } else {
            if (mCachedBrandInfos.containsKey(key)) {
                queryingTaskMap.remove(key);
                if (callback != null)
                    callback.OnTaskLoaded(mCachedBrandInfos.get(key));
                return;
            }
        }
        localProbeDataManager.getTask(scaleValue, scale, date, new GetTaskCallback<BrandTotalInfo>() {

            @Override
            public void OnTaskLoaded(BrandTotalInfo info) {
                queryingTaskMap.remove(key);
                if (callback != null)
                    callback.OnTaskLoaded(info);

                String[] ss = scaleValue.split("-");
                String tempScaleValue ="";
                if(scale.equals("month")||scale.equals("months")||scale.equals("week")||scale.equals("weeks")){
                    tempScaleValue = ss[1];
                }

                boolean isCurOrAfterYear = TimeUtils.getYear(TimeUtils.getTimeMillions(date))>=TimeUtils.getYear(System.currentTimeMillis())?true:false;
                if ((scale.equals("month")||scale.equals("months")) &&isCurOrAfterYear&& Integer.valueOf(tempScaleValue) >= TimeUtils.getMonthsOfYear(TimeUtils.getDate(System.currentTimeMillis()))) {
                    //当周内统计数据不需要缓存
                    return;
                }

                if ((scale.equals("week")||scale.equals("weeks")) && isCurOrAfterYear&& Integer.valueOf(tempScaleValue) >= TimeUtils.getWeeksOfYear(TimeUtils.getDate(System.currentTimeMillis()))) {
                    //当周内统计数据不需要缓存
                    return;
                }
                if ((scale.equals("day")||scale.equals("days")) &&isCurOrAfterYear&&  TimeUtils.getTimeMillions(date)>=TimeUtils.getTimeMillions(TimeUtils.getDate(System.currentTimeMillis()))) {
                    //当天内统计数据不需要缓存
                    return;
                }
                if (scale.equals("hour") &&isCurOrAfterYear&&  Integer.valueOf(scaleValue)>= TimeUtils.getHour(System.currentTimeMillis())) {
                    //当小时内统计数据不需要缓存
                    return;
                }
                mCachedBrandInfos.put(key, info);

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
    public void getTasks(final String scaleValue, final String scale, final String date, final LoadTasksCallback<BrandTotalInfo> callback) {

        Log.e("BrandTotal-Data","getTasks--BrandTotalData-- scaleValue="+scaleValue+" scale="+scale+" date="+date);
        final String key = scale + "|" + scaleValue + "|" + date;
        if(queryingTasksMap.containsKey(key)){
            Log.e("BrandTotal-Data","thisTasks is getting ,can't get again !");
            return;
        }else{
            queryingTasksMap.put(key,key);
        }
        if (mCachedBrandInfoList == null) {
            mCachedBrandInfoList = new LinkedHashMap<>();
        } else {
            if (mCachedBrandInfoList.containsKey(key)) {
                queryingTasksMap.remove(key);
                if (callback != null)
                    callback.onTasksLoaded(mCachedBrandInfoList.get(key));
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

                if ((scale.equals("month")||scale.equals("months")) &&isCurOrAfterYear&& Integer.valueOf(tempScaleValue) >= TimeUtils.getMonthsOfYear(TimeUtils.getDate(System.currentTimeMillis()))) {
                    //当周内统计数据不需要缓存
                    return;
                }

                if ((scale.equals("week")||scale.equals("weeks")) &&isCurOrAfterYear&&  Integer.valueOf(tempScaleValue) >= TimeUtils.getWeeksOfYear(TimeUtils.getDate(System.currentTimeMillis()))) {
                    //当周内统计数据不需要缓存
                    return;
                }
                if ((scale.equals("day")||scale.equals("days")) && isCurOrAfterYear&& TimeUtils.getTimeMillions(date)>=TimeUtils.getTimeMillions(TimeUtils.getDate(System.currentTimeMillis()))) {
                    //当天内统计数据不需要缓存
                    return;
                }

                mCachedBrandInfoList.put(key, list);
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
    public void saveTask(BrandTotalInfo info) {

    }

    @Override
    public void saveTasks(List<BrandTotalInfo> list) {

    }

    @Override
    public void clearTasks() {
        if(mCachedBrandInfoList!=null){
            mCachedBrandInfoList.clear();
        }
        if(mCachedBrandInfos!=null){
            mCachedBrandInfos.clear();
        }
    }


}
