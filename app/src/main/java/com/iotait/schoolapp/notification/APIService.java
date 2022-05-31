package com.iotait.schoolapp.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    //for initializing api service into the app
     @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAADHSKcKQ:APA91bEP8-em7JxVGdfJcosXbKvku5UAuIcNL9ObGqAGP9j-5knmTP26KaeNcyWSlUxpx-5ZMgnOH5lj2N4VUmbf4ZWpXflXD_PxDJUpYXsJAdaRMd_8rlry3OeDDpGi5sxcGFbI_G-v"
    })
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender sender);
}
