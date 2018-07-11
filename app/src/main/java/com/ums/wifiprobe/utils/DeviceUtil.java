package com.ums.wifiprobe.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Window;

public class DeviceUtil {

	private DeviceUtil() {// 确保不被实例化
	}

	/**
	 * dp 转化为 px
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dp2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * px 转化为 dp
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取设备宽度（px）
	 * 
	 * @param context
	 * @return
	 */
	public static int deviceWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * 获取设备高度（px）
	 * 
	 * @param context
	 * @return
	 */
	public static int deviceHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 * 获取状态栏高度
	 * 
	 * @author ZhengWx
	 * @date 2015年4月18日 上午10:47:39
	 * @param context
	 * @return
	 * @since 1.0
	 */
	public static int getStatusHeight(Context context) {
		int statusHeight = 0;
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height")
					.get(object).toString());
			statusHeight = context.getResources().getDimensionPixelSize(height);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return statusHeight;
	}

	/**
	 * 判断是否有SD卡（内置&外置）
	 * 
	 * @author ZhengWx
	 * @date 2015年2月7日 上午10:58:27
	 * @return
	 * @since 1.0
	 */
	public static boolean isExistSDCard() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取手机存储根目录
	 * 
	 * @author ZhengWx
	 * @date 2015年2月7日 上午10:58:33
	 * @return
	 * @since 1.0
	 */
	public static String getRootFilePath(Context context) {
		if (isExistSDCard()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		} else {
			return context.getFilesDir().getAbsolutePath();
		}
	}

	/**
	 * 获取手机存储根目录
	 * 
	 * @author ZhengWx
	 * @date 2015年2月7日 上午10:58:33
	 * @return
	 * @since 1.0
	 */
	public static String getRootFilePath() {
		if (isExistSDCard()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		} else {
			return Environment.getDataDirectory().getAbsolutePath();
		}
	}

	/**
	 * 判断目录是否存在，没有则创建
	 * 
	 * @author ZhengWx
	 * @date 2015年2月7日 下午4:35:44
	 * @param path
	 * @since 1.0
	 */
	public static void isDirectory(String path) {
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
	}

	/**
	 * 获取assets某个文件夹下的所有文件
	 * 
	 * @author ZhengWx
	 * @date 2015年6月2日 下午2:38:03
	 * @param context
	 * @param fileName
	 * @return
	 * @since 1.0
	 */
	public static String[] getAssetsFile(Context context, String fileName) {
		String[] files = null;
		try {
			files = context.getAssets().list(fileName);
		} catch (IOException e1) {
			LogUtil.e(e1);
		}

		if (files == null) {
			files = new String[] {};
		}
		LogUtil.i(fileName + ":" + Arrays.toString(files));

		return files;
	}

	/**
	 * 将assets文件夹中的文件复制到当前应用目录中
	 * 
	 * @author ZhengWx
	 * @date 2015年6月23日 下午1:43:10
	 * @param context
	 * @param assetsFilePath
	 *            assets文件夹中的文件，如：update/01
	 * @param outerFileName
	 *            复制后的文件名
	 * @return
	 * @since 1.0
	 */
	public static String assetsFile2Outer(Context context,
			String assetsFilePath, String outerFileName) {
		if (TextUtils.isEmpty(outerFileName)) {
			outerFileName = "outerTempFile";
		}
		try {
			InputStream inputStream = context.getAssets().open(assetsFilePath);
			byte[] tempDate = new byte[inputStream.available()];
			inputStream.read(tempDate);
			inputStream.close();
			context.deleteFile(outerFileName);
			context.openFileOutput(outerFileName, 0).write(tempDate);
			return context.getFileStreamPath(outerFileName).getAbsolutePath();
		} catch (Exception e) {
			LogUtil.e(e);
		}

		return "";
	}

	/**
	 * 将assets文件夹中的文件复制到指定目录下
	 * @param context
	 * @param assetDir
	 * @param dir
	 */
	public static void copyFromAssets(Context context, String assetDir, String dir) {

		String[] files;
		try {
			// 获得Assets一共有几多文件
			files = context.getResources().getAssets().list(assetDir);
		} catch (IOException e1) {
			return;
		}
		File mWorkingPath = new File(dir);
		// 如果文件路径不存在
		if (!mWorkingPath.exists()) {
			// 创建文件夹
			if (!mWorkingPath.mkdirs()) {
				// 文件夹创建不成功时调用
			}
		}

		for (int i = 0; i < files.length; i++) {
			try {
				// 获得每个文件的名字
				String fileName = files[i];
				// 根据路径判断是文件夹还是文件
				if (!fileName.contains(".")) {
					if (0 == assetDir.length()) {
						copyFromAssets(context, fileName, dir + fileName + "/");
					} else {
						copyFromAssets(context, assetDir + "/" + fileName, dir + "/"
								+ fileName + "/");
					}
					continue;
				}
				File outFile = new File(mWorkingPath, fileName);
				if (outFile.exists())
					outFile.delete();
				InputStream in = null;
				if (0 != assetDir.length())
					in = context.getAssets().open(assetDir + "/" + fileName);
				else
					in = context.getAssets().open(fileName);
				OutputStream out = new FileOutputStream(outFile);

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				in.close();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 当前是否有网络
	 * 
	 * @author ZhengWx
	 * @date 2015年7月1日 下午6:06:31
	 * @param context
	 * @return
	 * @since 1.0
	 */
	public static boolean isNetworkAvailable(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 返回版本名字(versionName)
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		String versionName = "";
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			versionName = packInfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}

	/**
	 * 返回版本号（versionCode）
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionCode(Context context) {
		String versionCode = "";
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			versionCode = String.valueOf(packInfo.versionCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionCode;
	}
	
	/**
	 * 获取设备的唯一标识，deviceId
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = tm.getDeviceId();
		if (deviceId == null) {
			return "-";
		} else {
			return deviceId;
		}
	}

	/**
	 * 获取手机品牌
	 * 
	 * @return
	 */
	public static String getPhoneBrand() {
		return android.os.Build.BRAND;
	}

	/**
	 * 获取手机型号
	 * 
	 * @return
	 */
	public static String getPhoneModel() {
		return android.os.Build.MODEL;
	}

	/**
	 * 获取手机Android API等级（22、23 ...）
	 * 
	 * @return
	 */
	public static int getBuildLevel() {
		return android.os.Build.VERSION.SDK_INT;
	}

	/**
	 * 获取手机Android 版本（4.4、5.0、5.1 ...）
	 * 
	 * @return
	 */
	public static String getBuildVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 获取AndroidManifest.xml里 <meta-data>的值
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static String getMetaData(Context context, String name) {
		String value = null;
		try {
			ApplicationInfo appInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			value = appInfo.metaData.getString(name);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 检测是否安装了指定的应用
	 * 
	 * @param context
	 * @param packageName
	 *            应用的包名
	 * @return
	 */
	public static boolean isApkAvilible(Context context, String packageName) {
		final PackageManager packageManager = context.getPackageManager();
		// 获取所有已安装程序的包信息
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		for (int i = 0; i < pinfo.size(); i++) {
			if (pinfo.get(i).packageName.equals(packageName))
				return true;
		}
		return false;
	}

	/**
	 * 开启其它应用
	 * 
	 * @param context
	 * @param packageName
	 */
	public static void openOtherApp(Context context, String packageName) {
		PackageManager packageManager = context.getPackageManager();
		Intent intent = packageManager.getLaunchIntentForPackage(packageName);
		if (intent != null) {
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(intent);
		}
	}

	public static void disableHomeAndMenu(Window win) {
		// 屏蔽菜单键
		win.addFlags(5);
		// 屏蔽HOME
		win.addFlags(3);
	}
	
	private static ConnectivityManager connectivityManager;
	
    private static NetworkInfo getNetworkInfo(Context context){
		if(connectivityManager==null){
			connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		return connectivityManager.getActiveNetworkInfo();
	}
    
	/**
	 * 判断是否是wifi方式联网
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnect(Context context){
		NetworkInfo info = getNetworkInfo(context);
		if (info!=null&&info.getType() == ConnectivityManager.TYPE_WIFI&&info.isConnected()) {
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * 判断当前设备是否是HD版
	 * @param context
	 * @return
	 */
	public static boolean isHD(Context context){
		Configuration newConfig = context.getResources().getConfiguration();
		DisplayMetrics metric = context.getResources().getDisplayMetrics();
		boolean land = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
//		int width = metric.widthPixels;
//		int height = metric.heightPixels;
//		int densityDpi = metric.densityDpi;
//		System.out.println("land="+land+",width="+width+",height="+height+",densityDpi="+densityDpi);
//		if(land && width == 1280 && height == 800 && densityDpi == 160){
//			System.out.println("isIM81:true");
//			return true;
//		} else{
//			System.out.println("isIM81:false");
//			return false;
//		}
		return land;
	}
}
