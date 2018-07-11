package com.ums.wifiprobe.service.greendao;

import com.google.gson.Gson;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenzhy on 2017/11/2.
 */


//若取全部值，则min和max取-1000,0 大于某个临界值 则如为>-20 小于某个临界值 则为<-100   最小值默认取-1000 好识别 最大值默认取0
@Entity
public class RssiInfo {
    @Id(autoincrement = true)
    private Long rssi_id;
    private int minRssi;
    private int maxRssi;
    private int totaNumber;
    private boolean isOld;//true 老顾客 false 新顾客
    private boolean isDistinct;//true 表示去重, false 表示不去重
    private long owenerId;
    @Generated(hash = 1005920743)
    public RssiInfo(Long rssi_id, int minRssi, int maxRssi, int totaNumber, boolean isOld,
            boolean isDistinct, long owenerId) {
        this.rssi_id = rssi_id;
        this.minRssi = minRssi;
        this.maxRssi = maxRssi;
        this.totaNumber = totaNumber;
        this.isOld = isOld;
        this.isDistinct = isDistinct;
        this.owenerId = owenerId;
    }
    public RssiInfo(int minRssi, int maxRssi, int totaNumber,boolean isOld,boolean isDistinct) {
        this.minRssi = minRssi;
        this.maxRssi = maxRssi;
        this.totaNumber = totaNumber;
        this.isOld = isOld;
        this.isDistinct = isDistinct;
    }

    @Generated(hash = 217625894)
    public RssiInfo() {
    }
    public Long getRssi_id() {
        return this.rssi_id;
    }
    public void setRssi_id(Long rssi_id) {
        this.rssi_id = rssi_id;
    }
    public int getMinRssi() {
        return this.minRssi;
    }
    public void setMinRssi(int minRssi) {
        this.minRssi = minRssi;
    }
    public int getMaxRssi() {
        return this.maxRssi;
    }
    public void setMaxRssi(int maxRssi) {
        this.maxRssi = maxRssi;
    }
    public int getTotaNumber() {
        return this.totaNumber;
    }
    public void setTotaNumber(int totaNumber) {
        this.totaNumber = totaNumber;
    }
    public long getOwenerId() {
        return this.owenerId;
    }
    public void setOwenerId(long owenerId) {
        this.owenerId = owenerId;
    }
    public boolean getIsOld() {
        return this.isOld;
    }
    public void setIsOld(boolean isOld) {
        this.isOld = isOld;
    }
    public boolean getIsDistinct() {
        return this.isDistinct;
    }
    public void setIsDistinct(boolean isDistinct) {
        this.isDistinct = isDistinct;
    }



}
