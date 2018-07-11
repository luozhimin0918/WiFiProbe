package com.ums.wifiprobe.net.requestentity;

import java.util.List;

/**
 * Created by chenzhy on 2017/9/14.
 */

public class ProbeInfoRequestEntity extends BaseRequestEntity{
   private BaseInfo BaseInfo;
    private List<String> device_list;

    public BaseInfo getBaseInfo() {
        return BaseInfo;
    }

    public void setBaseInfo(BaseInfo baseInfo) {
        BaseInfo = baseInfo;
    }

    public List<String> getDevice_list() {
        return device_list;
    }

    public void setDevice_list(List<String> device_list) {
        this.device_list = device_list;
    }

    public class BaseInfo{
        String SN;
        String Longitude;
        String Latitude;
        public BaseInfo(String sn,String longitude,String latitude){
            this.SN = sn;
            this.Latitude = latitude;
            this.Longitude = longitude;
        }

        public String getSN() {
            return SN;
        }

        public void setSN(String SN) {
            this.SN = SN;
        }

        public String getLongitude() {
            return Longitude;
        }

        public void setLongitude(String longitude) {
            Longitude = longitude;
        }

        public String getLatitude() {
            return Latitude;
        }

        public void setLatitude(String latitude) {
            Latitude = latitude;
        }
    }
}
