package com.marswin89.marsdaemon.strategy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import com.marswin89.marsdaemon.DaemonConfigurations;
import com.marswin89.marsdaemon.IDaemonStrategy;
import com.marswin89.marsdaemon.nativ.NativeDaemonAPI21;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * the strategy in android API 23.
 * 
 * @author Mars
 *
 */
public class DaemonStrategy24 implements IDaemonStrategy{
	private final static String INDICATOR_DIR_NAME 					= "indicators";
	private final static String INDICATOR_PERSISTENT_FILENAME 		= "indicator_p";
	private final static String INDICATOR_DAEMON_ASSISTANT_FILENAME = "indicator_d";
	private final static String OBSERVER_PERSISTENT_FILENAME		= "observer_p";
	private final static String OBSERVER_DAEMON_ASSISTANT_FILENAME	= "observer_d";
	
	private IBinder 				mRemote;
	private Parcel					mBroadcastData;
	private DaemonConfigurations 	mConfigs;

	@Override
	public boolean onInitialization(Context context) {
		return false;
	}

	@Override
	public void onPersistentCreate(final Context context, DaemonConfigurations configs) {

	}

	@Override
	public void onDaemonAssistantCreate(final Context context, DaemonConfigurations configs) {

	}
	
	
	@Override
	public void onDaemonDead() {

	}
	
	
	private void initAmsBinder(){

	}
	
	
	@SuppressLint("Recycle")// when process dead, we should save time to restart and kill self, don`t take a waste of time to recycle
	private void initBroadcastParcel(Context context, String broadcastName){

	}
	
	
	private boolean sendBroadcastByAmsBinder(){
	return false;
	}
	
	
	
	private boolean initIndicatorFiles(Context context){

			return false;
	}
	
	private void createNewFile(File dirFile, String fileName) throws IOException{
		File file = new File(dirFile, fileName);
		if(!file.exists()){
			file.createNewFile();
		}
	}
}
