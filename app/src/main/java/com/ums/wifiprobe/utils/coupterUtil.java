package com.ums.wifiprobe.utils;

import android.os.Bundle;
import android.util.Log;

import com.ums.wifiprobe.aidl.TransDataModel;

import java.util.List;

/**
 * Created by Luozhimin on 2018-07-17.8:45
 */
public class coupterUtil {
    public static Double toDayAmountZong(TransDataModel mTransDataModel,String startDate){
        Double moneyZong;
        List<Bundle> dd = mTransDataModel.get(startDate + " 00:00:00", startDate + " 23:59:00");
        moneyZong = 0d;
        for (Bundle d : dd) {
            moneyZong += Double.parseDouble(d.getString("transAmount"));
            Log.d("toDayAmountZong",d.getString("transAmount")+"" );
        }
        return moneyZong;
    }
}
