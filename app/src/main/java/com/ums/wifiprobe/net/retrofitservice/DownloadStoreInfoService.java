package com.ums.wifiprobe.net.retrofitservice;

import com.google.gson.GsonBuilder;
import com.ums.wifiprobe.net.ApiConstant;
import com.ums.wifiprobe.net.requestentity.ProbeInfoRequestEntity;
import com.ums.wifiprobe.net.requestentity.StoreInfoRequestEntity;
import com.ums.wifiprobe.net.responseentity.ProbeInfoResponseEntity;
import com.ums.wifiprobe.net.responseentity.StoreInfoResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by chenzhy on 2017/9/16.
 */

public class DownloadStoreInfoService {
    private static List<Call> callList = new ArrayList<>();
    private static final Retrofit sRetrofit = new Retrofit.Builder()
            .baseUrl(ApiConstant.GET_STOREINFO_FROM_SERVER)
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                    .setLenient()
                    .create()))
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(generateClient())
            .build();
    private static final DownloadStoreInfoService.DownloadService downloadService= sRetrofit.create(DownloadService.class);

    private static OkHttpClient generateClient() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                Request request = builder.addHeader("Content-type", "application/json")
                        .build();
                return chain.proceed(request);
            }
        }).build();
        return client;
    }
    public static Call<StoreInfoResponseEntity> downloadStoreInfoToService(StoreInfoRequestEntity requestEntity){
        Call<StoreInfoResponseEntity> call = downloadService.downloadStoreInfo(requestEntity);
        callList.add(call);
        return call;
    }

public static void cancelCalls(){
    for(Call call:callList){
        call.cancel();
    }
}
public static void removeCall(Call call){
    callList.remove(call);
}

    public interface DownloadService{
        @POST("upload")
        Call<StoreInfoResponseEntity> downloadStoreInfo(@Body StoreInfoRequestEntity entity);

    }


}
