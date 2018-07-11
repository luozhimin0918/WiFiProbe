package com.ums.wifiprobe.service.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenzhy on 2017/11/10.
 */

@Entity
public class MacWhiteList  {
    @Id(autoincrement = true)
    Long id;
    @Unique
    String mac;
    @Generated(hash = 137813743)
    public MacWhiteList(Long id, String mac) {
        this.id = id;
        this.mac = mac;
    }
    @Generated(hash = 928217645)
    public MacWhiteList() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getMac() {
        return this.mac;
    }
    public void setMac(String mac) {
        this.mac = mac;
    }
}
