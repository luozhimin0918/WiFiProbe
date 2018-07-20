package com.ums.wifiprobe.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ums.wifiprobe.R;
import com.ums.wifiprobe.ui.customview.CommonDialog;
import com.ums.wifiprobe.ui.customview.CommonDialog2;
import com.ums.wifiprobe.ui.customview.CommonDialog3;
import com.ums.wifiprobe.ui.customview.DownloadDialog;
import com.ums.wifiprobe.ui.customview.OnDialogCloseListener;

public class DialogUtil {

	private static ProgressDialog mProgressDialog;
	private static AlertDialog.Builder builder;
	private static Dialog loadingDialong;

	private DialogUtil() {
	}

	public static void showProgressDialog(Context context) {
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setTitle("初始化ing");
		mProgressDialog.setMessage("正在初始化中请稍候...");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(false);

		// 屏蔽菜单键 屏蔽HOME
		DeviceUtil.disableHomeAndMenu(mProgressDialog.getWindow());
		mProgressDialog.show();
	}

	public static void hideProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

	public static void showAppStoreDialog(final Context context,
			final DialogInterface.OnClickListener okListener) {
		builder = new AlertDialog.Builder(context);
		builder.setTitle("应用市场未安装！");
		builder.setPositiveButton("安装", okListener);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		});
		AlertDialog alertDialog = builder.create();
		
		// 屏蔽菜单键 屏蔽HOME
		DeviceUtil.disableHomeAndMenu(alertDialog.getWindow());		
		alertDialog.show();
	}

	/**
	 * 得到自定义的progressDialog
	 * @param context
	 * @param
	 * @return
	 */
	public static Dialog createLoadingDialog(Context context) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_layout);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.imageview_loading);
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.animation_loading);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		return loadingDialog;

	}
	public static void showLoadingDialog(Context context){
		hideLoadingDialog();
		loadingDialong = createLoadingDialog(context);
		loadingDialong.show();
	}
	public static void hideLoadingDialog(){
		if(loadingDialong!=null){
			loadingDialong.dismiss();
			loadingDialong=null;
		}
	}
	public static boolean isLoadingDialogShowing(){
		if(loadingDialong!=null&&loadingDialong.isShowing()){
			return true;
		}
		return false;
	}



	public static void showDialog(Context context, String title, String msg , OnDialogCloseListener listener) {
		CommonDialog dialog = new CommonDialog(context);
		dialog.setCancelable(false);
		dialog.setTitle(title).setContent(msg);
		dialog.setOnCloseListener(listener);
		dialog.show();
	}

	public static void showAlertDialog(Context context,String title, String msg , OnDialogCloseListener listener,String name){
		com.ums.wifiprobe.ui.customview.AlertDialog dialog = new com.ums.wifiprobe.ui.customview.AlertDialog(context);
		dialog.setContent(msg);
		if(!TextUtils.isEmpty(title)){
		dialog.setTitle(title);}
		dialog.setOnCloseListener(listener);
		dialog.setCancelable(true);
		if(!TextUtils.isEmpty(name)){
			dialog.setPositiveButton(name);

		}
		dialog.show();
	}

	public static void showAlertDialogWhiteList(Context context,String title, String msg , com.ums.wifiprobe.ui.customview.AlertDialogWhiteList.OnCloseListener listener,String name){
		com.ums.wifiprobe.ui.customview.AlertDialogWhiteList dialog = new com.ums.wifiprobe.ui.customview.AlertDialogWhiteList(context);
		dialog.setContent(msg);
		if(TextUtils.isEmpty(title)){
			dialog.setTitle(title);
		}
		dialog.setOnCloseListener(listener);
		if(!TextUtils.isEmpty(name)){
			dialog.setPositiveButton(name);

		}
		dialog.show();
	}

	private static DownloadDialog downloadDialog;
	public static void showDownloadDialog(Context context,String title, OnDialogCloseListener onCloseListener){
		downloadDialog = new DownloadDialog(context);
		downloadDialog.setTitle(title);
        downloadDialog.setCancelable(false);
		downloadDialog.setOnCloseListener(onCloseListener);
		downloadDialog.show();
	}
	public static void updateDownloadProgress(int progress){
		if(downloadDialog!=null)
		downloadDialog.updateProgress(progress);
	}
	public static void setDownloadState(String title){
		if(downloadDialog!=null){
			downloadDialog.setDownloadState(title);
		}
	}
	public static void hideDownloadDialog(){
		if(downloadDialog!=null){
			downloadDialog.dismiss();
			downloadDialog = null;
		}
	}

	private static CommonDialog2 commonDialog2;
	public static void showCommonDialog2(Context context,String content,OnDialogCloseListener listener){
		commonDialog2 = new CommonDialog2(context);
		commonDialog2.setContent(content);
		commonDialog2.setCancelable(true);
		commonDialog2.setOnCloseListener(listener);
		commonDialog2.show();
	}
	private static CommonDialog3 commonDialog3;
	public static void showCommonDialog3(Context context,String content,OnDialogCloseListener listener){
		commonDialog3 = new CommonDialog3(context);
		commonDialog3.setContent(content);
		commonDialog3.setCancelable(true);
		commonDialog3.setOnCloseListener(listener);
		commonDialog3.show();
	}
	public static void hideCommonDialog2(){
		if(commonDialog2!=null){
			commonDialog2.dismiss();
			commonDialog2=null;
		}
	}





}
