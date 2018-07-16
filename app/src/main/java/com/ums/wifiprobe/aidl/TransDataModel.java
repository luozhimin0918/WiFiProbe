package com.ums.wifiprobe.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.ums.app.dataanalysis.aidl.DBDataProvider;
import com.ums.wifiprobe.utils.TimeUtils;

import java.text.ParseException;
import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;

public class TransDataModel {

    private static DBDataProvider manager;
    private static Context mContext;

    public TransDataModel(Context context) {
        this.mContext = context;
    }

    public static ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            manager = DBDataProvider.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public static void bind() {
        Intent intent = new Intent("com.ums.app.dataanalysis.service.DBDataService");
        intent.setPackage("com.ums.app.dataanalysis");
        mContext.bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    public static   List<Bundle>  get(String startDate,String endDate) {
        List<Bundle> data=null;
        try {
            try {
                long st= TimeUtils.dateToStamp(startDate);
                long en =TimeUtils.dateToStamp(endDate);
                data = manager.getDBData(st,en);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return data;
    }
    public static List<Bundle> gethour(String startDate,String endDate ){
        List<Bundle> data=null;
        try {
            try {
                long st= TimeUtils.dateToStamp(startDate);
                long en =TimeUtils.dateToStamp(endDate);
                data = manager.getDBDataEachHour(st,en);
                Log.d("TTrand",st+  "    "+ en );
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return data;
    }
}
