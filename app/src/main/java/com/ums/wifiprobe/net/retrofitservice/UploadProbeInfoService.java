package com.ums.wifiprobe.net.retrofitservice;

import com.google.gson.GsonBuilder;
import com.ums.wifiprobe.net.ApiConstant;
import com.ums.wifiprobe.net.requestentity.ProbeInfoRequestEntity;
import com.ums.wifiprobe.net.responseentity.ProbeInfoResponseEntity;
import java.io.IOException;
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

public class UploadProbeInfoService  {
    private static final Retrofit sRetrofit = new Retrofit.Builder()
            .baseUrl(ApiConstant.UPLOAD_PROBEINFO_TO_SERVER)
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                    .setLenient()
                    .create()))
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(generateClient())
            .build();
    private static final UploadProbeInfoService.UploadService uploadService= sRetrofit.create(UploadService.class);

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
    public static Call<ProbeInfoResponseEntity> uploadProbeInfoToService(ProbeInfoRequestEntity requestEntity){
        Call<ProbeInfoResponseEntity> call = uploadService.uploadProbeInfo(requestEntity);
        return call;
    }



    public interface UploadService{
        @POST("upload")
        Call<ProbeInfoResponseEntity> uploadProbeInfo(@Body ProbeInfoRequestEntity entity);

    }


}
