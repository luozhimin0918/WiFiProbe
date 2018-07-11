package com.ums.wifiprobe.service.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import com.google.gson.Gson;
import com.ums.wifiprobe.service.probeentity.ProbeInfoEntityWrap;

/**
 * Created by chenzhy on 2017/10/24.
 */

@Entity
public class MacProbeInfo {
    @Id(autoincrement = true)
    private Long id;
    @Index
    private String createDate;//2017-09-27
    @Index
    private int createHour;//2
    private String mac;
    @ToOne(joinProperty = "mac")
    MacStatisticsInfo macStatisticsInfo;
    private String brandMac;
    @ToOne(joinProperty = "brandMac")
    MacBrandInfo macBrandInfo;

    private int rssi;
    private long createTime;
    private long lastTime;
    private long leaveTime;
    private long durationTime;
    private int leaveHour;//3
    private int probeTimes;//某时间段内探测次数
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1874669615)
    private transient MacProbeInfoDao myDao;
    @Generated(hash = 893075329)
    private transient String macStatisticsInfo__resolvedKey;
    @Generated(hash = 1100620847)
    private transient String macBrandInfo__resolvedKey;



    public MacProbeInfo(ProbeInfoEntityWrap wrap) {
        this.createDate = wrap.getCreateDate();
        this.createHour = wrap.getCreateHour();
        this.mac = wrap.getMac();
        this.createTime = wrap.getCreateTime();
        this.lastTime = wrap.getLastTime();
        this.rssi =Integer.valueOf(wrap.getRssi()) ;
        this.leaveTime = wrap.getLeaveTime();
        this.leaveHour = wrap.getLeaveHour();
        this.probeTimes = wrap.getProbeTimes();
        this.durationTime = wrap.getDurationTime();
        this.brandMac = this.mac.substring(0, 8).replace(":", "").toUpperCase();

    }


    @Generated(hash = 1552641403)
    public MacProbeInfo(Long id, String createDate, int createHour, String mac,
            String brandMac, int rssi, long createTime, long lastTime,
            long leaveTime, long durationTime, int leaveHour, int probeTimes) {
        this.id = id;
        this.createDate = createDate;
        this.createHour = createHour;
        this.mac = mac;
        this.brandMac = brandMac;
        this.rssi = rssi;
        this.createTime = createTime;
        this.lastTime = lastTime;
        this.leaveTime = leaveTime;
        this.durationTime = durationTime;
        this.leaveHour = leaveHour;
        this.probeTimes = probeTimes;
    }


    @Generated(hash = 2067212651)
    public MacProbeInfo() {
    }




    //------ Entity is detached from DAO context------//坑待填，手动查询的话
    public MacBrandInfo getMacBrandInfos() {

        if (daoSession != null) {
            return getMacBrandInfo();
        }
        if (macBrandInfo != null) {
            return macBrandInfo;
        }
        MacBrandInfo info = new MacBrandInfo();
        info.setBrandMac(brandMac);
        info.setBrandName("其他品牌");
        return info;
    }

    public MacStatisticsInfo getMacStatisticsInfos() {
        if (daoSession != null) {
            return getMacStatisticsInfo();
        }
        return macStatisticsInfo;
    }
    //------ Entity is detached from DAO context------//坑待填，手动查询的话


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getCreateDate() {
        return this.createDate;
    }


    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }


    public int getCreateHour() {
        return this.createHour;
    }


    public void setCreateHour(int createHour) {
        this.createHour = createHour;
    }


    public String getMac() {
        return this.mac;
    }


    public void setMac(String mac) {
        this.mac = mac;
    }


    public String getBrandMac() {
        return this.brandMac;
    }


    public void setBrandMac(String brandMac) {
        this.brandMac = brandMac;
    }


    public int getRssi() {
        return this.rssi;
    }


    public void setRssi(int rssi) {
        this.rssi = rssi;
    }


    public long getCreateTime() {
        return this.createTime;
    }


    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


    public long getLastTime() {
        return this.lastTime;
    }


    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }


    public long getLeaveTime() {
        return this.leaveTime;
    }


    public void setLeaveTime(long leaveTime) {
        this.leaveTime = leaveTime;
    }


    public long getDurationTime() {
        return this.durationTime;
    }


    public void setDurationTime(long durationTime) {
        this.durationTime = durationTime;
    }


    public int getLeaveHour() {
        return this.leaveHour;
    }


    public void setLeaveHour(int leaveHour) {
        this.leaveHour = leaveHour;
    }


    public int getProbeTimes() {
        return this.probeTimes;
    }


    public void setProbeTimes(int probeTimes) {
        this.probeTimes = probeTimes;
    }


    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1274883192)
    public MacStatisticsInfo getMacStatisticsInfo() {
        String __key = this.mac;
        if (macStatisticsInfo__resolvedKey == null
                || macStatisticsInfo__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MacStatisticsInfoDao targetDao = daoSession.getMacStatisticsInfoDao();
            MacStatisticsInfo macStatisticsInfoNew = targetDao.load(__key);
            synchronized (this) {
                macStatisticsInfo = macStatisticsInfoNew;
                macStatisticsInfo__resolvedKey = __key;
            }
        }
        return macStatisticsInfo;
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 100012321)
    public void setMacStatisticsInfo(MacStatisticsInfo macStatisticsInfo) {
        synchronized (this) {
            this.macStatisticsInfo = macStatisticsInfo;
            mac = macStatisticsInfo == null ? null : macStatisticsInfo.getMac();
            macStatisticsInfo__resolvedKey = mac;
        }
    }


    /** To-one relationship, resolved on first access. */
    @Generated(hash = 35943514)
    public MacBrandInfo getMacBrandInfo() {
        String __key = this.brandMac;
        if (macBrandInfo__resolvedKey == null
                || macBrandInfo__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MacBrandInfoDao targetDao = daoSession.getMacBrandInfoDao();
            MacBrandInfo macBrandInfoNew = targetDao.load(__key);
            synchronized (this) {
                macBrandInfo = macBrandInfoNew;
                macBrandInfo__resolvedKey = __key;
            }
        }
        return macBrandInfo;
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 127306149)
    public void setMacBrandInfo(MacBrandInfo macBrandInfo) {
        synchronized (this) {
            this.macBrandInfo = macBrandInfo;
            brandMac = macBrandInfo == null ? null : macBrandInfo.getBrandMac();
            macBrandInfo__resolvedKey = brandMac;
        }
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2025727087)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMacProbeInfoDao() : null;
    }

}
