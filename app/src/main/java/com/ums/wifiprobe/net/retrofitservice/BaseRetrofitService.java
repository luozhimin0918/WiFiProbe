package com.ums.wifiprobe.net.retrofitservice;

import com.ums.wifiprobe.net.requestentity.BaseRequestEntity;
import com.ums.wifiprobe.net.responseentity.BaseResponseEntity;

import retrofit2.Retrofit;

/**
 * Created by chenzhy on 2017/9/16.
 */

public abstract class BaseRetrofitService<T1 extends BaseRequestEntity, T2 extends BaseResponseEntity> {
    abstract Retrofit getRetrofit();

//    public Call<T2> doActionWithServer(T2 t){
//
//    };

    interface BaseServiceInterface{};
}
