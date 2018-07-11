package com.ums.wifiprobe.data;

import com.ums.wifiprobe.data.database.LocalProbeInfoDataResource;
import com.ums.wifiprobe.service.greendao.MacBrandInfo;
import com.ums.wifiprobe.service.greendao.MacProbeInfo;
import com.ums.wifiprobe.utils.TimeUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenzhy on 2017/9/23.
 */

public class ProbeInfoDataRepository implements DataResource<MacProbeInfo> {

    private Map<String, List<MacProbeInfo>> mCachedProbeInfoList;

    public static ProbeInfoDataRepository instance = null ;
    private LocalProbeInfoDataResource localProbeInfoDataResource;

    private ProbeInfoDataRepository(){
        localProbeInfoDataResource = new LocalProbeInfoDataResource();
    }

    public static ProbeInfoDataRepository getInstance(){
        if(instance == null){
            instance = new ProbeInfoDataRepository();
        }
        return instance;
    }
    @Override
    public void getTask(String scaleValue, String scale, String time, GetTaskCallback callback) {

    }

    @Override
    public void getTasks(final String scaleValue,final  String scale,final  String date,final  LoadTasksCallback callback) {
        if(mCachedProbeInfoList==null){
            mCachedProbeInfoList = new LinkedHashMap<>();
        }
        String tempDate = date;
        final String key = scaleValue+"|"+scale+"|"+tempDate;
        if(mCachedProbeInfoList.containsKey(key)){
            callback.onTasksLoaded(mCachedProbeInfoList.get(key));
            return;
        }
        localProbeInfoDataResource.getTasks(scaleValue, scale, date, new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List list) {
                callback.onTasksLoaded(list);
                if(date.equals(TimeUtils.getDate(System.currentTimeMillis()))){
                    return;//当天数据不需要缓存
                }
                mCachedProbeInfoList.put(key,list);
            }

            @Override
            public void onDataNotAvaliable() {

            }
        });
    }

    @Override
    public void saveTask(MacProbeInfo info) {

    }

    @Override
    public void saveTasks(List<MacProbeInfo> list) {

    }

    @Override
    public void clearTasks() {
        if(mCachedProbeInfoList!=null){
            mCachedProbeInfoList.clear();
        }
    }
}
