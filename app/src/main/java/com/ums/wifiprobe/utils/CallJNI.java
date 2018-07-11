package com.ums.wifiprobe.utils;

/**
 * 调用JNI
 * Created by wangf on 2016/12/16.
 */

public class CallJNI {

    public native int stringFromJNI();

    static {
        System.loadLibrary("hello-jni");
    }

    public int doCall(){
        return stringFromJNI();
    }
}
