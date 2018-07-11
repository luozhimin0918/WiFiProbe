package com.ums.wifiprobe.service.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenzhy on 2017/10/31.
 */

@Entity
public class BrandTotalInfo {


    //example scale="months" date="2017-09-17" scaleValue="11" 表示2017年11月份品牌信息

    //example scale="weeks" date="2017-09-17" scaleValue="38" 表示这周内的品牌信息 第38周

    //example scale="days" date="2017-09-17" scaleValue="2017-09-17" 该日的各品牌信息 存储在list中


    @Id(autoincrement = true)
    private Long id;
    @Index
    private String brandName;
    private int brandNumber;

    private String scale;
    private String scaleValue;
    private String date;//查询日期 //2017-9-25//当前查询日期 若为月则为该月的1号 若为周则为该周的数据且存储为该周日以方便查询 若为日和时则为该日
    @Generated(hash = 1780604650)
    public BrandTotalInfo(Long id, String brandName, int brandNumber, String scale,
            String scaleValue, String date) {
        this.id = id;
        this.brandName = brandName;
        this.brandNumber = brandNumber;
        this.scale = scale;
        this.scaleValue = scaleValue;
        this.date = date;
    }
    @Generated(hash = 197332532)
    public BrandTotalInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBrandName() {
        return this.brandName;
    }
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
    public int getBrandNumber() {
        return this.brandNumber;
    }
    public void setBrandNumber(int brandNumber) {
        this.brandNumber = brandNumber;
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
   
}
