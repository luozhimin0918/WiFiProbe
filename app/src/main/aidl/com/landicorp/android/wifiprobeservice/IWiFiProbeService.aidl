package com.landicorp.android.wifiprobeservice;
import com.landicorp.android.wifiprobeservice.ITaskCallback;
import com.landicorp.android.wifiprobeservice.ITaskStaCallback;
import com.landicorp.android.wifiprobeservice.ISwitchCallback;
interface IWiFiProbeService{
   boolean openWifiProbe();
   boolean closeWifiPorbe();
   void registerCallback(ITaskCallback cb);   
   void unregisterCallback(ITaskCallback cb); 
   void startGetProbeInfo();

   void openWifiStaProbe(ISwitchCallback icb);
   void closeWifiStaProbe(ISwitchCallback icb);
   void closeWifiStaProbeInfo(ISwitchCallback icb);
   void registerStaCallback(ITaskStaCallback cb);   
   void unregisterStaCallback(ITaskStaCallback cb); 
   void startGetStaProbeInfo();
   
   boolean wifiProbeSuspend(boolean enable);
}