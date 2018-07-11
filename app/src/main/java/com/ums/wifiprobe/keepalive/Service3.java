package com.ums.wifiprobe.keepalive;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class Service3 extends Service{

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
        Log.d("csf1","service3 start");
        stopService(new Intent(this,this.getClass()));
    }
}
