package com.ums.wifiprobe.net;

import com.google.gson.Gson;

/**
 * Created by chenzhy on 2017/9/16.
 */

public class BaseNetEntity {
    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
