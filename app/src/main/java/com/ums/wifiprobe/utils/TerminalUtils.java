package com.ums.wifiprobe.utils;

import android.util.Log;

/**
 * Created by chenzhy on 2017/6/16.
 */

public class TerminalUtils {
    public static String getSN(){
        return android.os.Build.SERIAL;
    }
    public static String getMD5Code(){
                        String md5Code ="";
                try{
                    //根据SN号算出校验号
                    md5Code = MD5.encoderByMd5(getSN());
                    md5Code = md5Code.substring(3,9);
                }catch(Throwable t){
                    Log.d("TerminalUtil",t.toString());
                }
        return md5Code;
    }

}
