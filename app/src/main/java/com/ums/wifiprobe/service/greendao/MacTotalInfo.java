package com.ums.wifiprobe.service.greendao;

import com.google.gson.Gson;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.DaoException;

import java.util.List;

/**
 * Created by chenzhy on 2017/10/30.
 */

@Entity
public class MacTotalInfo {

    //统计信息和详细信息区分开来 统计信息的scale上多加个s 如months weeks days
    //周和月需要加上年份

    //example scale="months" date="2017-09-17" scaleValue="11" 表示2017年11月份统计数据
    //example scale="month" date="2017-09-17" scaleValue="11" 表示2017年11月份统计数据 存储在List中 每月1号  2017-11
            //exapmle scale="day" date="2017-09-1" scaleValue="2017-09-17" 当月内每天数据

    //example scale="weeks" date="2017-09-17" scaleValue="38" 表示这周内的统计数据 第38周
    //exapmle scale="week" date="2017-09-17" scaleValue="38" 这周内的的数据  存储在list中 getTasks  2017-38
        //exapmle scale="day" date="2017-09-17" scaleValue="周日" 这周内每日的数据

    //exapmle scale="days" date="2017-09-17" scaleValue="周日" 周日当天数据
    //example scale="day" date="2017-09-17" scaleValue="周日" 周日分时的数据 存储在list中
        //example scale="hour" date="2017-09-17" scaleValue="0" 周日0时的数据


    @Id
    Long id;

    @Index
    String scale;
    String scaleValue;
    String date;//查询日期 //2017-9-25//当前查询日期 若为月则为该月的1号 若为周则为该周的数据且存储为该周日以方便查询 若为日和时则为该日
    @ToMany(referencedJoinProperty = "owenerId")
    List<RssiInfo> rssiInfos;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 62060404)
    private transient MacTotalInfoDao myDao;
    @Generated(hash = 2111495463)
    public MacTotalInfo(Long id, String scale, String scaleValue, String date) {
        this.id = id;
        this.scale = scale;
        this.scaleValue = scaleValue;
        this.date = date;
    }
    @Generated(hash = 116448332)
    public MacTotalInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getScale() {
        return this.scale;
    }
    public void setScale(String scale) {
        this.scale = scale;
    }
    public String getScaleValue() {
        return this.scaleValue;
    }
    public void setScaleValue(String scaleValue) {
        this.scaleValue = scaleValue;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1924475491)
    public List<RssiInfo> getRssiInfos() {
        if (rssiInfos == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RssiInfoDao targetDao = daoSession.getRssiInfoDao();
            List<RssiInfo> rssiInfosNew = targetDao._queryMacTotalInfo_RssiInfos(id);
            synchronized (this) {
                if (rssiInfos == null) {
                    rssiInfos = rssiInfosNew;
                }
            }
        }
        return rssiInfos;
    }
    public List<RssiInfo> getRssiInfoList() {
        if (daoSession != null) {
           return getRssiInfos();
        }
        return rssiInfos;
    }
    public void setRssiInfos(List<RssiInfo> list){
        rssiInfos = list;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 688875355)
    public synchronized void resetRssiInfos() {
        rssiInfos = null;
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
@Generated(hash = 320514537)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getMacTotalInfoDao() : null;
}
  

}
