package com.iotait.schoolapp.data.network;

import com.iotait.schoolapp.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpProvider {
    private static final String APPLICATION_CONTENT_TYPE = "application/json;charset=utf-8";
    static OkHttpClient getNotiHttpClient(){

        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        getNotiUpHttpBuilder(httpBuilder);
        return httpBuilder.build();
    }
    private static void getNotiUpHttpBuilder(OkHttpClient.Builder httpBuilder) {
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpBuilder.addInterceptor(logging);
        }
        httpBuilder.connectTimeout(45, TimeUnit.SECONDS);
        httpBuilder.readTimeout(45, TimeUnit.SECONDS);
        httpBuilder.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder accept = original.newBuilder()
                    .header("Authorization","key=AAAADHSKcKQ:APA91bEP8-em7JxVGdfJcosXbKvku5UAuIcNL9ObGqAGP9j-5knmTP26KaeNcyWSlUxpx-5ZMgnOH5lj2N4VUmbf4ZWpXflXD_PxDJUpYXsJAdaRMd_8rlry3OeDDpGi5sxcGFbI_G-v")
                    .header("Content-Type", APPLICATION_CONTENT_TYPE);
            Request.Builder requestBuilder = accept
                    .method(original.method(), original.body());
            Request request = requestBuilder.build();
            return chain.proceed(request);
        });
    }
}
