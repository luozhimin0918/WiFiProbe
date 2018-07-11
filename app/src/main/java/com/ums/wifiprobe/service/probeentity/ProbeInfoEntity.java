package com.ums.wifiprobe.service.probeentity;

/**
 * Created by chenzhy on 2017/9/14.
 */

public class ProbeInfoEntity {
    protected String mac;
    protected String rssi;
    protected long createTime;
    protected long leaveTime;

    public ProbeInfoEntity() {
    }

    public ProbeInfoEntity(String mac, String rssi, long createTime) {
        this.mac = mac;
        this.rssi = rssi;
        this.createTime = createTime;
    }

    public ProbeInfoEntity(String mac, String rssi, long createTime, long leaveTime) {
        this(mac,rssi,createTime);
        this.leaveTime = leaveTime;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(long leaveTime) {
        this.leaveTime = leaveTime;
    }
}
