package com.ums.wifiprobe.service.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenzhy on 2017/10/24.
 */


@Entity
public class MacStatisticsInfo {
    @Id
    String mac;
    long createTimes;//第一次探测到的时间点
    int probeTimes;
    @Generated(hash = 886829932)
    public MacStatisticsInfo(String mac, long createTimes, int probeTimes) {
        this.mac = mac;
        this.createTimes = createTimes;
        this.probeTimes = probeTimes;
    }
    @Generated(hash = 798703411)
    public MacStatisticsInfo() {
    }
    public String getMac() {
        return this.mac;
    }
    public void setMac(String mac) {
        this.mac = mac;
    }
    public long getCreateTimes() {
        return this.createTimes;
    }
    public void setCreateTimes(long createTimes) {
        this.createTimes = createTimes;
    }
    public int getProbeTimes() {
        return this.probeTimes;
    }
    public void setProbeTimes(int probeTimes) {
        this.probeTimes = probeTimes;
    }
}
