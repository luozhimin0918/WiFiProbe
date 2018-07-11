package com.ums.wifiprobe.utils;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.text.InputFilter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 输入法工具类
 *
 * @author CB 
 * @version 创建时间：2014年7月2日 下午5:40:02  
 *
 */
public class InputUtils {

	public static final int STATUS_OPEN = 1;
	public static final int STATUS_CLOSE = 0;

	/**
	 * 隐藏虚拟键盘
	 * @param v 视图对象
	 */
	public static void hideKeyboard(Window window) {
		// 判断隐藏软键盘是否弹出
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
	
	/**
	 * 隐藏虚拟键盘
	 */
	public static void hideKeyboard(Activity activity) {
		// 判断隐藏软键盘是否弹出
		LogUtil.i("softInputMode1:"+activity.getWindow().getAttributes().softInputMode);
		if (activity.getWindow().getAttributes().softInputMode ==
				WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
			// 隐藏软键盘
			activity
					.getWindow()
					.setSoftInputMode(
							WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		}
	}
	
	/**
	 * 隐藏虚拟键盘
	 * @param v 视图对象
	 */
	public static void hideKeyboard(View v){
		InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
		    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
		}
	}
    /**
     * 显示虚拟键盘
     * @param v 视图对象
     */
    public static void showKeyboard(View v){
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }
      
    /**
     * 强制显示或者关闭系统键盘
     * @param txtSearchKey 输入框
     * @param status 开关标识
     */
    public static void setKeyBoardStatus(final EditText editText,final int status){
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
			@Override
			public void run() {
			    InputMethodManager m = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			     if(status == STATUS_OPEN) {
			    	 m.showSoftInput(editText,InputMethodManager.SHOW_FORCED);
			     } else {
			         m.hideSoftInputFromWindow(editText.getWindowToken(), 0);
			     }
			 }
        }, 200);
    }
      
    /**
     * 通过定时器强制隐藏虚拟键盘
     * @param v
     */
    public static void timerHideKeyboard(final View v){
    	Timer timer = new Timer();
        timer.schedule(new TimerTask(){
	        @Override
	        public void run() {
	        	InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	        	if (imm.isActive()) {
	                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
	            }
	        }
        }, 10);
    }
    
    /**
     * 判断输入法是否显示
     * @param edittext 输入框
     * @return
     */
    public static boolean isShowKeyBoard(EditText editText){
    	boolean bool = false;
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
           bool = true; 
        }
        return bool;
    }
    
    /**
     * 不使用键盘
     * @param edittext
     * @return
     */
    public static void alwaysHideKeyBoard(EditText editText,int inputType){
    	if (editText != null) {
    		editText.setInputType(inputType);
    		editText.setTextIsSelectable(true);
    	}
    }
    
    /**
     * 设置最大值
     * @param editText
     * @param maxLength
     */
    public static void setMaxLength(EditText editText,int maxLength){
    	if (editText != null) {

			InputFilter[] filters = {new InputFilter.LengthFilter(maxLength)};
			editText.setFilters(filters);
    	}
    }
    
    /**
     * 隐藏系统输入法
     * @param editText 输入框
     */
	public static void hideSystemInput(EditText editText){
		 try {  
	            Class<EditText> cls = EditText.class;  
	            Method setShowSoftInputOnFocus;  
	            setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",  
	                    boolean.class);  
	            setShowSoftInputOnFocus.setAccessible(true);  
	            setShowSoftInputOnFocus.invoke(editText, false);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }
	}
}
