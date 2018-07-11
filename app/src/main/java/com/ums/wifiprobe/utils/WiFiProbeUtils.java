package com.ums.wifiprobe.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WiFiProbeUtils { 
	 private static String TAG = "WiFiProbeUtils";
	public static  final int MAX_RSSI = -20;
	public static final float TEMP =75; 
	public static String calculateDistance(double rssi){
		double distance =0.0;
		if(rssi>0){
			return -1+"";
		}else if(rssi>=-20){
			rssi = 20;
		}else{}
		distance = Math.pow(10, -((rssi-MAX_RSSI)/TEMP));
		return String.format("%.2f", distance);
	}
}
