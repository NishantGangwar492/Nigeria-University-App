package com.iotait.schoolapp.data.network;


import com.iotait.schoolapp.ui.homepage.ui.home.model.LatestNewsResponse;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    //latest news
    @GET("news")
    Call<LatestNewsResponse> getNews(@Query("page") int page, @Query("limit") int limit);

    //unn news
    @GET("news/unn")
    Call<LatestNewsResponse> getUNNNews(@Query("page") int page, @Query("limit") int limit);

    @POST("send")
    Call<HashMap> send(@Body HashMap body);
}
