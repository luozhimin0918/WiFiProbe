package com.ums.wifiprobe.utils;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
/***
 * 快速点击事件类<br>
 * 设置一个最大点击数，若短时间内点击数达到最大值时处理一种事件，否则按单击情况处理另一种事件
 * @author LiuChenShuo
 *
 */
public class FastClickEvent implements Callback{

	/**两次点击的时间间隔，默认为260毫秒*/
	private int clickGap = 260;
	
	private int clickMax;
	private Handler handler;
	private int lastCount = 1;
	private int count = 0;
	private boolean firstClick = true;
	
	private ClickTimeOut clickTimeOut;
	private ClickEnough clickEnough;
	
	public FastClickEvent(int clickMax) {
		this.clickMax = clickMax;
		handler = new Handler(this);
	}

	public void setClickGap(int clickGap) {
		this.clickGap = clickGap;
	}

	public void onClick(){
		onClick(0);
	}
	/**
	 * @param what 将作为ClickTimeOut,ClickEnough的onClick(int what)的参数
	 */
	public void onClick(int what){
		count++;
		
		if (firstClick) {
			checkFastClick(what);
			firstClick = false;
		}
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		if (count > lastCount && count < clickMax) {
			checkFastClick(msg.what);
			lastCount = count;
		} else if (count == lastCount && count < clickMax) {
			if(clickTimeOut != null){
				clickTimeOut.onClick(msg.what);
			}
			resset();
		} else if (count >= clickMax) {
			if(clickEnough != null){
				clickEnough.onClick(msg.what);
			}
			resset();
		}

		return true;
	}
	
	private void checkFastClick(int what){
		handler.sendEmptyMessageDelayed(what, clickGap);
	}
	
	private void resset() {
		lastCount = 1;
		count = 0;
		firstClick = true;
	}
	
	public void setClickTimeOut(ClickTimeOut clickTimeOut) {
		this.clickTimeOut = clickTimeOut;
	}

	public void setClickEnough(ClickEnough clickEnough) {
		this.clickEnough = clickEnough;
	}

	/***
	 * 点击数不足最大值时，事件的处理方式
	 */
	public interface ClickTimeOut{
		void onClick(int what);
	}

	/***
	 * 点击数达到最大值时，事件的处理方式
	 */
	public interface ClickEnough{
		void onClick(int what);
	}
}
