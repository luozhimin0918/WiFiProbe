package com.ums.wifiprobe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.landicorp.android.wifiprobeservice.IWiFiProbeService;
import com.ums.upos.sdk.SDKResult;
import com.ums.upos.sdk.exception.CallServiceException;
import com.ums.upos.sdk.exception.SdkException;
import com.ums.upos.sdk.system.BaseSystemManager;
import com.ums.upos.sdk.system.ModuleEnum;
import com.ums.upos.sdk.system.OnServiceStatusListener;
import com.ums.upos.sdk.wiFiProbe.OnSwitchListenerImp;
import com.ums.upos.sdk.wiFiProbe.OnTaskStaListenerImp;
import com.ums.upos.sdk.wiFiProbe.WiFiProbeManager;
import com.ums.upos.uapi.engine.DeathRecipientListener;
import com.ums.wifiprobe.R;
import com.ums.wifiprobe.app.GlobalValueManager;
import com.ums.wifiprobe.app.WPApplication;
import com.ums.wifiprobe.keepalive.Service3;
import com.ums.wifiprobe.service.greendao.MacProbeInfoDao;
import com.ums.wifiprobe.service.greendao.MacWhiteListDao;
import com.ums.wifiprobe.service.probeentity.ProbeInfoEntity;
import com.ums.wifiprobe.service.probeentity.ProbeInfoEntityWrap;
import com.ums.wifiprobe.utils.CheckApkExist;
import com.ums.wifiprobe.utils.TimeUtils;
import com.ums.wifiprobeapp.IProbeInfoCallback;
import com.ums.wifiprobeapp.IProbeService;
import com.ums.wifiprobeapp.IProbeStateCallback;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;


/**
 * Created by chenzhy on 2017/9/13.
 */

public class ProbeService extends Service {

    String TAG = "WiFiProbe-ProbeService";
    private BaseProbeDataManager baseProbeDataManager;

    static WiFiProbeManager wiFiProbeManager;

    private final static Handler mHandler = new Handler();

    private Timer timer;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initProbeParams();
        Log.d(TAG, "onServiceCreate");
        registerBroadReceiver();
    }

    private void initProbeParams() {
        ProbeServiceConstant.uploadInterval = GlobalValueManager.getInstance().getUploadIntervalTime();
        ProbeServiceConstant.leaveInterval = GlobalValueManager.getInstance().getLeaveIntervalTime();
        ProbeServiceConstant.autoProbe = GlobalValueManager.getInstance().getAutoProbeValue();
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onServiceDestroy!");
        if (wiFiProbeManager != null) {
            try {
                wiFiProbeManager.unregisterStaCallback(onTaskStaListenerImp1);
                wiFiProbeManager.closeWifiStaProbeInfo(onCloseSwitchListenerImp);
            } catch (SdkException e) {
                e.printStackTrace();
            }
        }
        if (probeConfigBroadcastReceiver != null) {
            unregisterReceiver(probeConfigBroadcastReceiver);
            probeConfigBroadcastReceiver = null;
        }
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent==null||!GlobalValueManager.getInstance().isAgreeProtocol()){
            return super.onStartCommand(intent, START_NOT_STICKY, startId);
        }
        Log.d(TAG, "onServiceStart=");
        if (intent.hasExtra("isServiceUpdate")) {
            String isServiceUpdate = intent.getStringExtra("isServiceUpdate");
            Log.d(TAG, isServiceUpdate);
            ProbeServiceConstant.probeIsOpened = false;
            updateWiFiProbeState();
            try {
                BaseSystemManager.getInstance().unBindDeviceService();
                wiFiProbeManager = null;//重新绑定服务,若之前不支持，新版本服务支持，则会重新读取
                startBindService();
            } catch (SdkException e) {
//            e.printStackTrace();
            }
        } else if (intent.hasExtra("openOrCloseProbe")) {
            String openOrCloseProbe = intent.getStringExtra("openOrCloseProbe");
            Log.d(TAG, "openOrCloseProbe ;" + openOrCloseProbe);
            if (openOrCloseProbe.equals("open")) {
                Log.d(TAG, "start probe !");
                startBindService();
            } else if (openOrCloseProbe.equals("close")) {
                if (wiFiProbeManager != null) {
                    try {
                        Log.d(TAG, "close probeinfo !");
                        wiFiProbeManager.unregisterStaCallback(onTaskStaListenerImp1);
                        wiFiProbeManager.closeWifiStaProbeInfo(onCloseSwitchListenerImp);
                        BaseSystemManager.getInstance().unBindDeviceService();
                    } catch (SdkException e) {
                        Log.d(TAG, "close probe action exception :" + e);
                    }
                }
            }

        } else {
            if (wiFiProbeManager == null&&!ProbeServiceConstant.probeIsOpening) {
                startBindService();
            }
        }
        return super.onStartCommand(intent, START_NOT_STICKY, startId);
    }

    private void startBindService() {
        try {
            Log.d(TAG, "start bindDeviceService");
            BaseSystemManager.getInstance().bindDeviceService(this, null, "85265473", new OnServiceStatusListener() {
                @Override
                public void onStatus(int arg0) {
                    if (arg0 != SDKResult.LOGIN_FAIL) {
                        Log.d(TAG, "bindDeviceService success ");
                        try {

                            final Map<ModuleEnum, String> map = BaseSystemManager.getInstance().getDeviceInfo();
                            if (map == null) {
                                //服务版本老
                                GlobalValueManager.getInstance().setWifiProbeErrorInfo(getResources().getString(R.string.function_support_uservice_unsupport));
                                Log.d(TAG, getResources().getString(R.string.function_support_uservice_unsupport));
                            } else {
                                if (map.containsKey(ModuleEnum.IS_SUPPORT_WIFIPROBE)) {
                                    String wifiprobe = map.get(ModuleEnum.IS_SUPPORT_WIFIPROBE);

                                    if (wifiprobe.equals("1")) {
                                        //支持
                                        Log.d(TAG, "support wifiProbe");
                                        GlobalValueManager.getInstance().setWifiProbeErrorInfo("ok");
                                        startProbe();
                                        if (baseProbeDataManager == null) {
                                            baseProbeDataManager = new LocalProbeDataManager();
                                            baseProbeDataManager.startProbe();
                                        }
                                        BaseSystemManager.getInstance().setDeathRecipientListener(new DeathRecipientListener() {
                                            @Override
                                            public void binderDied() {
                                                Log.d(TAG, "u service had died....");
                                                if (ProbeServiceConstant.probeIsOpened && CheckApkExist.checkApkExist(ProbeService.this, "com.ums.upos.uapi")) {
                                                    Log.d(TAG, "try to reopen wifiprobe!");
                                                    ProbeServiceConstant.probeIsOpenedLaststate = true;
                                                    ProbeServiceConstant.probeIsOpened = false;
                                                    updateWiFiProbeState();
                                                    startBindService();
                                                }
                                            }
                                        });


                                    } else {
                                        //服务是新的，但是不支持探针
                                        GlobalValueManager.getInstance().setWifiProbeErrorInfo(getResources().getString(R.string.function_support_device_unsupport));
                                        Log.d(TAG, getResources().getString(R.string.function_support_device_unsupport));
                                    }
                                } else {
                                    //服务版本老
                                    GlobalValueManager.getInstance().setWifiProbeErrorInfo(getResources().getString(R.string.function_support_uservice_unsupport));
                                    Log.d(TAG, getResources().getString(R.string.function_support_uservice_unsupport));
                                }
                            }

                        } catch (SdkException e) {
                            sendBroadcast(new Intent(ProbeServiceConstant.RECEIVER_ACTION_OPEN_PROBE_FAILURE));
                        } catch (CallServiceException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d(TAG, "bind service failure errorcode :" + arg0);//服务绑定失败，
                        GlobalValueManager.getInstance().setWifiProbeErrorInfo("ok");
                    }
                }
            });
        } catch (SdkException e) {
            Log.d(TAG, "bind service failure SdkException :" + e.toString());
            GlobalValueManager.getInstance().setWifiProbeErrorInfo("ok");
        }

    }

    private OnSwitchListenerImp onOpenSwitchListenerImp = new OnSwitchListenerImp() {
        @Override
        public void swich(final int arg0, final String arg1) {
            if (arg0 == SDKResult.WiFiProbe_Open_Succeed) {
                Log.d(TAG, "open wifiprobe succeed !");
                try {
                    if (timer != null) {
                        timer.cancel();
                    }
                    wiFiProbeManager.startGetStaProbeInfo();
                    wiFiProbeManager.registerStaCallback(onTaskStaListenerImp1);
                    ProbeServiceConstant.probeIsOpened = true;
                    updateWiFiProbeState();
                    ProbeServiceConstant.probeIsOpening = false;
                    sendBroadcast(new Intent(ProbeServiceConstant.RECEIVER_ACTION_OPEN_PROBE_SUCCESS));
                } catch (SdkException e) {
                    Log.d(TAG, "open wifiProbeFailure SdkException:" + e);
                }
            } else {
                ProbeServiceConstant.probeIsOpened = false;
                sendBroadcast(new Intent(ProbeServiceConstant.RECEIVER_ACTION_OPEN_PROBE_FAILURE));
                Log.d(TAG, "open wifiProbeFailure errorcode:" + arg0);
            }
        }


    };

    private void startProbe() {
        Log.d(TAG, "start open wifiprobe !");
        wiFiProbeManager = null;
        wiFiProbeManager = new WiFiProbeManager();
        ProbeServiceConstant.probeIsOpening = true;
        try {
            wiFiProbeManager.openWifiStaProbe(onOpenSwitchListenerImp);
            if (timer == null) {
                timer = new Timer();
                timer.schedule(timerTask, 10000);
            }
        } catch (Exception e) {
            Log.d(TAG, "startProbe :" + e);
//            e.printStackTrace();
        }


    }

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (!ProbeServiceConstant.probeIsOpened) {
                startProbe();
            }
        }
    };


    private OnSwitchListenerImp onCloseSwitchListenerImp = new OnSwitchListenerImp() {
        @Override
        public void swich(final int arg0, final String arg1) {
            Log.d(TAG, "closeWifiStaProbe arg0:" + arg0);
            if (arg0 == SDKResult.WiFiProbe_Stop_Succeed) {
                ProbeServiceConstant.probeIsOpened = false;
                updateWiFiProbeState();
                wiFiProbeManager = null;
                sendBroadcast(new Intent(ProbeServiceConstant.RECEIVER_ACTION_CLOSE_PROBE_SUCCESS));
                Log.d(TAG, "stop wifiprobe succeeded");
            } else if (arg0 == SDKResult.WiFiProbe_Close_Succeed) {
                ProbeServiceConstant.probeIsOpenedLaststate = ProbeServiceConstant.probeIsOpened;
                ProbeServiceConstant.probeIsOpened = false;
                updateWiFiProbeState();
                wiFiProbeManager = null;
                sendBroadcast(new Intent(ProbeServiceConstant.RECEIVER_ACTION_CLOSE_PROBE_SUCCESS));
                Log.d(TAG, "close wifiprobe succeeded");
            }

        }
    };

    private OnTaskStaListenerImp onTaskStaListenerImp1 = new OnTaskStaListenerImp() {
        @Override
        public void getWiFiProbeOfSta(String mac, String rssi, long time) {
            if (tempEntity == null) {
                tempEntity = new ProbeInfoEntity();
            } else {
                if (mac.equals(tempEntity.getMac()) && (time - tempEntity.getCreateTime() <= 2000)) {
                    tempEntity.setCreateTime(time);
                    tempEntity.setMac(mac);
                    return;//过滤
                }
            }
            tempEntity.setCreateTime(time);
            tempEntity.setMac(mac);

            int i = Integer.parseInt(tempEntity.getMac().substring(1, 2), 16);
            if (i % 4 > 0) {
                return;
            }
            String rssi1 = rssi.replace(" ", "");
            updateWiFiProbeInfo(mac,rssi,time);
            if (WPApplication.gMacWhiteListDao.queryBuilder().where(MacWhiteListDao.Properties.Mac.eq(mac.toUpperCase())).unique() != (null)) {
                return;//白名单
            }
            if (baseProbeDataManager != null) {
//                Log.d(TAG,"New Mac Approved :"+mac);
                baseProbeDataManager.addProbeInfo(new ProbeInfoEntityWrap(mac, rssi1, time));
            }


        }
    };


    private ProbeInfoEntity tempEntity = null;


    private void registerBroadReceiver() {
        if(probeConfigBroadcastReceiver==null){
            probeConfigBroadcastReceiver = new ProbeConfigBroadcastReceiver();
            IntentFilter myIntentFilter = new IntentFilter();
            myIntentFilter.addAction(ProbeServiceConstant.RECEIVER_ACTION_UPDATE_UPLOADINTERVALTIME);
            myIntentFilter.addAction(ProbeServiceConstant.RECEIVER_ACTION_LEAVE_INTERVALTIME);
            myIntentFilter.addAction(ProbeServiceConstant.RECEIVER_ACTION_LEAVE_PROBEAGAININTERVALTIME);
            myIntentFilter.addAction(ProbeServiceConstant.RECEIVER_ACTION_DISTANCE_LOCATION);
            myIntentFilter.addAction(ProbeServiceConstant.RECEIVER_ACTION_DISTANCE_CLOSE);
            myIntentFilter.addAction(ProbeServiceConstant.RECEIVER_ACTION_DISTANCE_MIDDLE);

            myIntentFilter.addAction(ProbeServiceConstant.RECEIVER_ACTION_DISTANCE_FAR);
            myIntentFilter.addAction(ProbeServiceConstant.RECEIVER_ACTION_UPDATE_AUTOPROBE);
            myIntentFilter.addAction(ProbeServiceConstant.RECEIVER_ACTION_OPEN_PROBE);
            myIntentFilter.addAction(ProbeServiceConstant.RECEIVER_ACTION_CLOSE_PROBE);
            myIntentFilter.addAction(ProbeServiceConstant.RECEIVER_ACTION_CLOSE_PROBE_COMPLETE);
            myIntentFilter.addAction(ProbeServiceConstant.RECEIVER_ACTION_CLEAR_QUEEN_DATA);
            myIntentFilter.addAction(ProbeServiceConstant.RECEIVER_ACTION_SERVICE_REPLACED);

            registerReceiver(probeConfigBroadcastReceiver, myIntentFilter);
        }

    }

    private ProbeConfigBroadcastReceiver probeConfigBroadcastReceiver ;


    class ProbeConfigBroadcastReceiver extends BroadcastReceiver {
        public ProbeConfigBroadcastReceiver() {
            super();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ProbeServiceConstant.RECEIVER_ACTION_UPDATE_UPLOADINTERVALTIME:
                    ProbeServiceConstant.uploadInterval = GlobalValueManager.getInstance().getUploadIntervalTime();
                    //处理相关事情
                    break;
                case ProbeServiceConstant.RECEIVER_ACTION_LEAVE_INTERVALTIME:
                    ProbeServiceConstant.leaveInterval = GlobalValueManager.getInstance().getLeaveIntervalTime();
                    //处理相关事情
                    break;
                case ProbeServiceConstant.RECEIVER_ACTION_LEAVE_PROBEAGAININTERVALTIME:
                    ProbeServiceConstant.probeAgainInterval = GlobalValueManager.getInstance().getLeaveProbeAgainIntervalTime();
                    //处理相关事情
                    break;
                case ProbeServiceConstant.RECEIVER_ACTION_DISTANCE_LOCATION:
                    ProbeServiceConstant.locationDistance = GlobalValueManager.getInstance().getLocationDistance();
                    //处理相关事情
                    break;
                case ProbeServiceConstant.RECEIVER_ACTION_DISTANCE_CLOSE:
                    ProbeServiceConstant.closeDistance = GlobalValueManager.getInstance().getCloseDistance();
                    //处理相关事情
                    break;
                case ProbeServiceConstant.RECEIVER_ACTION_DISTANCE_MIDDLE:
                    ProbeServiceConstant.middleDistance = GlobalValueManager.getInstance().getMiddleDistance();
                    //处理相关事情
                    break;
                case ProbeServiceConstant.RECEIVER_ACTION_DISTANCE_FAR:
                    ProbeServiceConstant.farDistance = GlobalValueManager.getInstance().getfarDistance();
                    //处理相关事情
                    break;
                case ProbeServiceConstant.RECEIVER_ACTION_UPDATE_AUTOPROBE:
                    ProbeServiceConstant.autoProbe = GlobalValueManager.getInstance().getAutoProbeValue();
                    //处理相关事情
                    break;
                case ProbeServiceConstant.RECEIVER_ACTION_OPEN_PROBE:
                    Log.d(TAG, "receive open probe action !");
                    startBindService();
                    break;
                case ProbeServiceConstant.RECEIVER_ACTION_CLOSE_PROBE:
                    Log.d(TAG, "receive close probe action !");
                    if (wiFiProbeManager != null) {
                        try {
                            Log.d(TAG, "close probeinfo !");
                            wiFiProbeManager.unregisterStaCallback(onTaskStaListenerImp1);
                            wiFiProbeManager.closeWifiStaProbeInfo(onCloseSwitchListenerImp);
                            BaseSystemManager.getInstance().unBindDeviceService();
                        } catch (SdkException e) {
                            Log.d(TAG, "close probe action exception :" + e);
                        }
                    }
                    break;
                case ProbeServiceConstant.RECEIVER_ACTION_CLOSE_PROBE_COMPLETE:
                    Log.d(TAG, "receive close probe complete action !");
                    if (wiFiProbeManager != null) {

                        try {
                            Log.d(TAG, "close probe complete!");
                            wiFiProbeManager.closeWifiStaProbe(onCloseSwitchListenerImp);
                            BaseSystemManager.getInstance().unBindDeviceService();
                        } catch (SdkException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
                case ProbeServiceConstant.RECEIVER_ACTION_CLEAR_QUEEN_DATA:
                    if (baseProbeDataManager != null) {
                        baseProbeDataManager.clearCache();
                    }
                    break;
                case ProbeServiceConstant.RECEIVER_ACTION_SERVICE_REPLACED:
                    Log.d(TAG, "receive u service replaced action " + ProbeServiceConstant.probeIsOpened);
                    if (ProbeServiceConstant.probeIsOpenedLaststate) {
                        ProbeServiceConstant.probeIsOpenedLaststate = true;
                        startBindService();
                    }
                    ProbeServiceConstant.probeIsOpened = false;
                    updateWiFiProbeState();
                    break;
            }
        }
    }


    private static final RemoteCallbackList<IProbeInfoCallback> mProbeInfoCallbacks = new RemoteCallbackList<IProbeInfoCallback>();
    private static final RemoteCallbackList<IProbeStateCallback> mProbeStateCallbacks = new RemoteCallbackList<IProbeStateCallback>();
    private final IProbeService.Stub  mBinder = new  com.ums.wifiprobeapp.IProbeService.Stub(){

        @Override
        public void startGetProbeState(IProbeStateCallback cb) throws RemoteException {
            if(cb!=null){
                if(cb!=null){
                    Log.d(TAG,"registerProbeStateCallback "+cb.toString());
                    mProbeStateCallbacks.register(cb);
                    updateWiFiProbeState();
                }
            }
        }

        @Override
        public void stopGetProbeState(IProbeStateCallback cb) throws RemoteException {
            if(cb!=null){
                if(cb!=null){
                    Log.d(TAG,"unregisterProbeStateCallback "+cb.toString());
                    mProbeStateCallbacks.unregister(cb);
                }
            }
        }

        @Override
        public void startGetProbeInfo(IProbeInfoCallback cb) throws RemoteException {
            if(cb!=null){
                Log.d(TAG,"registerProbeInfoCallback "+cb.toString());
                mProbeInfoCallbacks.register(cb);
            }

        }

        @Override
        public void stopGetProbeInfo(IProbeInfoCallback cb) throws RemoteException {
            if(cb!=null){
                Log.d(TAG,"unregisterProbeInfoCallback "+cb.toString());
                mProbeInfoCallbacks.unregister(cb);
            }
        }

    };

    private void updateWiFiProbeInfo(String mac,String rssi,long time){
        synchronized (ProbeService.class) {
            final int N = mProbeInfoCallbacks.beginBroadcast();
            try {
                for (int i = 0; i < N; i++) {
                    mProbeInfoCallbacks.getBroadcastItem(i).getWiFiProbeInfo(mac,rssi,time);
                }
            } catch (RemoteException e) {
                // The RemoteCallbackList will take care of removing
                // the dead object for us.
                Log.e(TAG, "RemoteException is happend: " + e.getMessage());
            } finally {
                mProbeInfoCallbacks.finishBroadcast();
            }

        }
    }
    private void updateWiFiProbeState(){
        synchronized (ProbeService.class) {
            final int N = mProbeStateCallbacks.beginBroadcast();
            try {
                for (int i = 0; i < N; i++) {
                    mProbeStateCallbacks.getBroadcastItem(i).updateProbeState(ProbeServiceConstant.probeIsOpened?1:0);
                }
            } catch (RemoteException e) {
                // The RemoteCallbackList will take care of removing
                // the dead object for us.
                Log.e(TAG, "RemoteException is happend: " + e.getMessage());
            } finally {
                mProbeStateCallbacks.finishBroadcast();
            }

        }
    }
}


