package com.ums.wifiprobe.keepalive;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * DO NOT do anything in this Service!<br/>
 *
 * Created by Mars on 12/24/15.
 */
public class Service2 extends Service{

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("csf1","service2 start");
//        try {
//            Thread.sleep(10000);
//            String str = null;
//            str.length();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
