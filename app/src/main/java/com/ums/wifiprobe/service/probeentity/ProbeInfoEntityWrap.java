package com.ums.wifiprobe.service.probeentity;

import com.google.gson.Gson;
import com.ums.wifiprobe.utils.TimeUtils;

/**
 * Created by chenzhy on 2017/9/14.
 */

public class ProbeInfoEntityWrap extends ProbeInfoEntity {
    private long lastTime;
    private String createDate;
    private int createHour;
    private int leaveHour;
    private int probeTimes;//总探测次数
    private long durationTime;/////shenmeqingkuang



    public ProbeInfoEntityWrap(String mac, String rssi, long createTime) {
        super(mac, rssi, createTime);
        this.createDate = TimeUtils.getDate(createTime);
        this.createHour = TimeUtils.getHour(createTime);
        this.probeTimes++;
        this.createTime = createTime;
        this.lastTime = createTime;
    }

    public long getDurationTime() {
        return durationTime;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
        if(lastTime>=createTime)
        this.durationTime = this.lastTime-this.createTime;

    }

    public void setDurationTime(long durationTime) {
        this.durationTime = durationTime;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getCreateHour() {
        return createHour;
    }

    public void setCreateHour(int createHour) {
        this.createHour = createHour;
    }

    public int getLeaveHour() {
        return leaveHour;
    }

    public void setLeaveHour(int leaveHour) {
        this.leaveHour = leaveHour;
    }

    public int getProbeTimes() {
        return probeTimes;
    }

    public void setProbeTimes(int probeTimes) {
        this.probeTimes = probeTimes;
    }
    public void addProbeTimes(){
        probeTimes++;
    }
    public void setLeaveTime(long leaveTime){
        super.setLeaveTime(leaveTime);
        this.leaveHour = TimeUtils.getHour(leaveTime);
        if(leaveTime>=createTime&&leaveTime>=lastTime){
            this.durationTime = leaveTime-createTime;
        }
    }

}
