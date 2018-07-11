package com.ums.wifiprobe.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.ums.wifiprobe.utils.TimeUtils;

public final class GlobalValueManager extends ParameterManager {

    private final static String TAG = "GlobalValueManager";
    private SharedPreferences sharedPreferences = null;
    private Editor editor = null;
    private Application application;

    private GlobalValueManager() {
    }

    private static GlobalValueManager instance;

    public static GlobalValueManager getInstance() {

        if (instance == null) {
            synchronized (GlobalValueManager.class) {
                if (instance == null) {
                    instance = new GlobalValueManager();
                }
            }
        }
        return instance;
    }

    public void init(WPApplication application) {
        this.application = application;
        sharedPreferences = application.getSharedPreferences("GlobalValue", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public Application getApplication() {
        return application;
    }

    @Override
    protected SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    @Override
    protected Editor getEditor() {
        return editor;
    }

    public int getUploadIntervalTime() {
        return getIntValue("uploadInterval", 60000);
    }

    public void setUploadIntervalTime(int time) {
        setIntValue("uploadInterval", time);
        syncValue();
    }

    public int getLeaveIntervalTime() {
        return getIntValue("leaveInterval", 300000);
    }

    public void setLeaveIntervalTime(int time) {
        setIntValue("leaveInterval", time);
        syncValue();
    }


    //二次探测离开判断间隔
    public int getLeaveProbeAgainIntervalTime() {
        return getIntValue("leaveProbeAgainInterval", 1200000);
    }

    public void setLeaveProbeAgainIntervalTime(int time) {
        setIntValue("leaveProbeAgainInterval", time);
        syncValue();
    }

    //当设备移动超过一段距离时，自动提示请历史数据
    public int getLocationDistance() {
        return getIntValue("locationDistance", 50);
    }

    public void setLocationDistance(int distance) {
        setIntValue("locationDistance", distance);
        syncValue();
    }

    //wifi探针探测到的距离
    public int getCloseDistance() {
        return getIntValue("closeDistance", 10);
    }

    public void setCloseDistance(int distance) {
        setIntValue("closeDistance", distance);
        syncValue();
    }

    public int getMiddleDistance() {
        return getIntValue("middleDistance", 20);
    }

    public void setMiddleDistance(int distance) {
        setIntValue("middleDistance", distance);
        syncValue();
    }

    public int getfarDistance() {
        return getIntValue("farDistance", 30);
    }

    public void setfarDistance(int distance) {
        setIntValue("farDistance", distance);
        syncValue();
    }


    public boolean getAutoProbeValue() {
        return getBooleanValue("autoProbe", true);
    }

    public void setAutoProbeValue(boolean autoProbe) {
        setBooleanValue("autoProbe", autoProbe);
        syncValue();
    }

    public void setWifiProbeErrorInfo(String errorInfo) {
        setStringValue("wifiProbeErrorInfo", errorInfo);
        syncValue();
        if (isFirstOpenApp()) {
            setFirstOpenApp(false);
        }
    }

    public String getWifiProbeErrorInfo() {
        return getStringValue("wifiProbeErrorInfo", "检测到服务版本过低，暂不支持采集客流数据.请先升级服务");
    }


    public void setLatitude(String latitude) {
        setStringValue("latitude", latitude);
        syncValue();
    }

    public void setLongitude(String longitude) {
        setStringValue("longitude", longitude);
        syncValue();
    }

    public String getLatitude() {
        return getStringValue("latitude", "0.0");
    }

    public String getLongitude() {
        return getStringValue("longitude", "0.0");
    }


    //获取第一次检测的设备信息，app安装时轮询获取
    public boolean isFirstOpenApp() {
        return getBooleanValue("isFirstOpenApp", true);
    }

    public void setFirstOpenApp(boolean isFirst) {
        setBooleanValue("isFirstOpenApp", isFirst);
        syncValue();
    }

    //每天检查一次数据库，是否需要清理
    public boolean isCheckDatabase(long timeMillions) {
        String key = TimeUtils.getDate(timeMillions);
        return getBooleanValue(key, false);
    }

    public void setCheckDatabase(long timeMillions, boolean checked) {
        String key = TimeUtils.getDate(timeMillions);
        setBooleanValue(key, checked);
        syncValue();
    }

    public boolean isAgreeProtocol() {
        return getBooleanValue("agree", false);
    }

    public void setAgreeProtocol(boolean agree) {
        setBooleanValue("agree", agree);
        syncValue();
    }

    public boolean isRewardOpened(){
        return getBooleanValue("rewardOpened",false);
    }
    public void setRewardOpened(boolean isRewardOpened){
        setBooleanValue("rewardOpened",isRewardOpened);
        syncValue();
    }


    /**
     * 清除所有数据
     */
    public void clear() {
        getSharedPreferences().getAll().clear();
    }

}