package com.iotait.schoolapp.notification.chatnotif;

import android.util.Log;

import com.iotait.schoolapp.data.network.RetrofitClient;
import com.iotait.schoolapp.ui.homepage.ui.chat.models.Message;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendNotifications {


    public static void sendPushNotification(Message message, String token, String myId) {
        sendNotify(token,message,myId);
    }
    private static void sendNotify(String token,Message message,String myid){

        HashMap body = new HashMap();
        body.put("title",message.MessageId);
        body.put("body", message.MsgType);

        HashMap data = new HashMap();
        data.put("type","SMS");
        body.put("message", message.Msg);
        data.put("user",message.SenderId);
        body.put("title1",message.SenderName);
        //body.put("message", message.Msg);

        HashMap param = new HashMap();
        param.put("to",token);
        param.put("notification",body);
        param.put("data",data);

        RetrofitClient.sendNotification().send(param).enqueue(new Callback<HashMap>() {
            @Override
            public void onResponse(Call<HashMap> call, Response<HashMap> response) {
                Log.e("TAG", "onResponse: Notification send success");
            }

            @Override
            public void onFailure(Call<HashMap> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

}
