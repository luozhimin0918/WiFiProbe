package com.ums.wifiprobe.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

public class MeasureUtil {

  private static final String TAG = "MeasureUtil";

  public static int[] getScreenSize(Context activity) {
    // 获取屏幕密度（方法2）
    DisplayMetrics dm = activity.getResources().getDisplayMetrics();
    Log.d(TAG, "activity" + activity);
    float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
    int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
    float xdpi = dm.xdpi;
    float ydpi = dm.ydpi;

    int screenWidth = dm.widthPixels; // 屏幕宽（像素，如：480px）
    int screenHeight = dm.heightPixels; // 屏幕高（像素，如：800px）
    int[] screens = {screenWidth, screenHeight};
    Log.d(TAG, "density=" + density+ "  densityDPI=" + densityDPI+ "  xdpi=" + xdpi+ "  ydpi=" + ydpi+ "  screenWidth="+screenWidth+ "  screenHeight="+screenHeight );
    return screens;
  }
  public static float getScreenDensity(Context context){
    DisplayMetrics dm = context.getResources().getDisplayMetrics();
    return dm.density;
  }

}
