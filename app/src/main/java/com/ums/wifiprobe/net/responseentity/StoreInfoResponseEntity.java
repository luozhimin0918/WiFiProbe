package com.ums.wifiprobe.net.responseentity;

import java.util.List;

/**
 * Created by chenzhy on 2017/9/16.
 */

public class StoreInfoResponseEntity extends BaseResponseEntity {
    private String Type;
    private String Scale;
    private String Date_time;
    private List<String> Device_List;

    public StoreInfoResponseEntity() {
    }

    public StoreInfoResponseEntity(String type, String scale, String date_time) {
        Type = type;
        Scale = scale;
        Date_time = date_time;
    }

    public StoreInfoResponseEntity(String type, String scale, String date_time, List<String> device_List) {
        Type = type;
        Scale = scale;
        Date_time = date_time;
        Device_List = device_List;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getScale() {
        return Scale;
    }

    public void setScale(String scale) {
        Scale = scale;
    }

    public String getDate_time() {
        return Date_time;
    }

    public void setDate_time(String date_time) {
        Date_time = date_time;
    }

    public List<String> getDevice_List() {
        return Device_List;
    }

    public void setDevice_List(List<String> device_List) {
        Device_List = device_List;
    }
}
