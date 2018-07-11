package com.ums.wifiprobe.data.database;

import android.text.TextUtils;
import android.util.Log;

import com.ums.wifiprobe.CommonStants;
import com.ums.wifiprobe.app.GlobalValueManager;
import com.ums.wifiprobe.app.WPApplication;
import com.ums.wifiprobe.data.DataResource;
import com.ums.wifiprobe.data.database.helper.ProbeInfoDaoHelper;
import com.ums.wifiprobe.service.BaseProbeDataManager;
import com.ums.wifiprobe.service.greendao.MacBrandInfo;
import com.ums.wifiprobe.service.greendao.MacProbeInfo;
import com.ums.wifiprobe.service.probeentity.ProbeInfoEntityWrap;
import com.ums.wifiprobe.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzhy on 2017/9/23.
 */

public class LocalProbeInfoDataResource implements DataResource<MacProbeInfo> {
    //日 客流量/新顾客/老顾客/当前店内顾客
    //后续需做去重处理，以及统计次数

    @Override
    public void getTask(String scaleValue, String scale, String date, GetTaskCallback callback) {

    }

    //scale  22-100 从offset22开始查询100条数据
    @Override
    public void getTasks(String scaleValue, String scale, String date, LoadTasksCallback callback) {
        String offset="0";
        String count = "100";
        if (!TextUtils.isEmpty(scale)) {
            String[] params = scale.split("-");
            offset = params[0];
            count = params[1];
        }
        switch (scaleValue) {
            case "客流量":
                List<MacProbeInfo> list1 = ProbeInfoDaoHelper.getDayCustomerList(offset,count,date);
                if (list1 != null || list1.size() >= 0) {
                    callback.onTasksLoaded(list1);
                } else {
                    callback.onDataNotAvaliable();
                }
                break;
            case "新客":
                List<MacProbeInfo> list2= ProbeInfoDaoHelper.getDayCustomerList(false,offset,count,date);
                if (list2 != null || list2.size() >= 0) {
                    callback.onTasksLoaded(list2);
                } else {
                    callback.onDataNotAvaliable();
                }
                break;
            case "老客":
                List<MacProbeInfo> list3= ProbeInfoDaoHelper.getDayCustomerList(true,offset,count,date);
                if (list3 != null || list3.size() >= 0) {
                    callback.onTasksLoaded(list3);
                } else {
                    callback.onDataNotAvaliable();
                }
                break;
            case "5分钟内客流":
                List<MacProbeInfo> list4= ProbeInfoDaoHelper.getDayCustomerList(offset,count,date,300000l);
                if (list4 != null || list4.size() >= 0) {
                    callback.onTasksLoaded(list4);
                } else {
                    callback.onDataNotAvaliable();
                }
                break;

        }
    }

    @Override
    public void saveTask(MacProbeInfo info) {

    }

    @Override
    public void saveTasks(List<MacProbeInfo> list) {

    }

    @Override
    public void clearTasks() {

    }
}
