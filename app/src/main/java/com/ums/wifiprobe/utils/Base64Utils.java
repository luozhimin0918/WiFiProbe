package com.ums.wifiprobe.utils;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

/**
 * Created by chenzhy on 2017/7/7.
 */

public class Base64Utils {
    public static String convert(byte[] s){
        byte[] value= s;
        return Base64.encodeToString(value,0).replace("\n","");
    }
    public static byte[] decode(String s){
        return Base64.decode(s.getBytes(StandardCharsets.UTF_8),0);
    }
}
