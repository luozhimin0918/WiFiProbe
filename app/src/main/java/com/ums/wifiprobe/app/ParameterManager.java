package com.ums.wifiprobe.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public abstract  class ParameterManager {

	private final static String TAG = "ParameterManager";

	/**
	 * 获取sharedPreference
	 * 
	 * @return
	 */
	protected abstract SharedPreferences getSharedPreferences();

	protected abstract Editor getEditor();

	/**
	 * 设置boolean型数据
	 * 
	 * @param key
	 * @param value
	 */
	public void setBooleanValue(String key, boolean value) {
		if (getEditor() != null) {
			getEditor().putBoolean(key, value);
		} else {
			Log.d(TAG, "=======setBooleanValue getEditor() = null");
		}
	}
	public void saveBooleanValue(String key, boolean value){
		setBooleanValue(key, value);
		syncValue();
	}

	public boolean getBooleanValue(String key, boolean defaultValue) {
		try {
			boolean value;
			if (getSharedPreferences() != null) {
				if (getSharedPreferences().contains(key)) {
					value = getSharedPreferences()
							.getBoolean(key, defaultValue);
				} else {
					value = defaultValue;
					setBooleanValue(key, defaultValue);
					syncValue();
				}
			} else {
				value = defaultValue;
			}
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	/**
	 * 添加或修改字段
	 * 
	 * @param key
	 * @param value
	 */
	public void setStringValue(String key, String value) {
		if (getEditor() != null) {
			getEditor().putString(key, value);
		} else {
			Log.d(TAG, "=======setStringValue getEditor() = null");
		}
	}

	/**
	 * 获取key字段字符串
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getStringValue(String key, String defaultValue) {

		try {
			String value;
			if (getSharedPreferences() != null) {
				if (getSharedPreferences().contains(key)) {
					value = getSharedPreferences().getString(key, defaultValue);
				} else {
					value = defaultValue;
					setStringValue(key, defaultValue);
					syncValue();
				}
			} else {
				value = defaultValue;
			}
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	public int getIntValue(String key, int defaultValue) {
		try {
			int value;
			if (getSharedPreferences() != null) {
				if (getSharedPreferences().contains(key)) {
					value = getSharedPreferences().getInt(key, defaultValue);
				} else {
					value = defaultValue;
					setIntValue(key, defaultValue);
					syncValue();
				}
			} else {
				value = defaultValue;
			}
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	public void setIntValue(String key, int value) {
		if (getEditor() != null) {
			getEditor().putInt(key, value);
		} else {
			Log.d(TAG, "=======setIntValue getEditor() = null");
		}
	}

	/**
	 * 获取double类型数据，把String类型转double类型
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public double getDoubleValue(String key, double defaultValue) {
		try {
			double value;
			if (getSharedPreferences() != null) {
				if (getSharedPreferences().contains(key)) {
					float fValue = getSharedPreferences().getFloat(key,
							(float) defaultValue);
					value = Double.parseDouble(String.valueOf(fValue));
				} else {
					value = (float) defaultValue;
					setDoubleValue(key, defaultValue);
					syncValue();
				}
			} else {
				value = (float) defaultValue;
			}
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	/**
	 * 把double类型数据以String类型形式保存
	 * 
	 * @param key
	 * @param value
	 */
	public void setDoubleValue(String key, double value) {
		if (getEditor() != null) {
			getEditor().putFloat(key, (float) value);
		} else {
			Log.d(TAG, "=======setDoubleValue getEditor() = null");
		}
	}

	public void removeValue(String key) {
		if (getEditor() != null) {
			getEditor().remove(key);
		}
	}

	public void removeStringValue(String key) {
		removeValue(key);
	}

	/**
	 * 同步到数据库
	 */
	public void syncValue() {
		if (getEditor() != null) {
			getEditor().commit();
		}
	}

	
	/**
	 * 获取应用版本信息
	 * @param context
	 * @return
	 */
	public static String getAppversion(Context context) {
		// 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
			String version = packInfo.versionName;
	        return version;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return "";
	}
	
	/**
	 * 获取版本号
	 * @param context
	 * @return
	 */
	public static int getAppVersionCode(Context context) {
		// 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
	        return packInfo.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return 0;
	}
	
}