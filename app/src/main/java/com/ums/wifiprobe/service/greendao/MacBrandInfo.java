package com.ums.wifiprobe.service.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenzhy on 2017/10/24.
 */

@Entity
public class MacBrandInfo {
    @Id
    String brandMac;//103047
    @Index
    String brandName;//三星
    @Generated(hash = 1831371806)
    public MacBrandInfo(String brandMac, String brandName) {
        this.brandMac = brandMac;
        this.brandName = brandName;
    }
    @Generated(hash = 1871216049)
    public MacBrandInfo() {
    }
    public String getBrandMac() {
        return this.brandMac;
    }
    public void setBrandMac(String brandMac) {
        this.brandMac = brandMac;
    }
    public String getBrandName() {
        return this.brandName;
    }
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
