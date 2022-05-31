package com.iotait.schoolapp.ui.homepage.ui.chat.common;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.notification.APIService;
import com.iotait.schoolapp.notification.Data;
import com.iotait.schoolapp.notification.MyResponse;
import com.iotait.schoolapp.notification.Sender;
import com.iotait.schoolapp.ui.homepage.ui.chat.constant.Constant;
import com.iotait.schoolapp.ui.homepage.ui.chat.interfaces.ChatLListener;
import com.iotait.schoolapp.ui.homepage.ui.chat.models.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRelatedCommonFunction {

    private FirebaseFirestore fs = FirebaseFirestore.getInstance();
    Context context;
    APIService apiService;

    public ChatRelatedCommonFunction(Context context, APIService apiService) {
        this.context = context;
        this.apiService = apiService;
    }

    public HashMap getMessageMap(Message msg) {

        HashMap smsMap = new HashMap();
        smsMap.put("MessageId", msg.MessageId);
        smsMap.put("SentTime", msg.SentTime);
        smsMap.put("Message", msg.Msg);
        smsMap.put("MsgType", msg.MsgType);
        smsMap.put("SenderName", msg.SenderName);
        smsMap.put("SenderId", msg.SenderId);
        smsMap.put("Profile", msg.Profile);
        smsMap.put("Status", msg.Status);
        smsMap.put("URLS", msg.URLS);
        smsMap.put("ReadStatus", msg.ReadStatus);
        return smsMap;
    }

    public void sentMessage(Message msg, String myId, String otherUserId, ChatLListener handler) {

        msg.Status = "Sent";
        fs.collection("Message").document(myId + otherUserId).collection(myId).document(msg.MessageId)
                .set(getMessageMap(msg)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Log.e("TAG", "onSuccess: send success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "onFailure: can not sent " + e.getMessage());
            }
        });

        fs.collection("Message").document(otherUserId + myId).collection(otherUserId).document(msg.MessageId)
                .set(getMessageMap(msg)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                handler.onMessageSendSuccess(msg);
                getUserNotificationSendStatus(otherUserId, msg, myId, msg.MsgType);
                if (msg.MsgType.equalsIgnoreCase("Media")) {
                    handler.onImageSendSuccess();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "onFailure: can not sent " + e.getMessage());
            }
        });

    }

    public void sendMessageNotification(Message sms, String myId, String otherId, String msgType) {
        if (otherId.equals("appadmin")) {
            FirebaseDatabase.getInstance().getReference("Tokens").child("Admins").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String token = "";
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            token = dsp.child("token").getValue(String.class);
                            Log.e("TAG", "onDataChange: admin token" + token);
                            if (msgType.equalsIgnoreCase("Media")) {

                                sendNotif(token, myId, otherId, sms.SenderName, Constant.currentUserProfile, "Media was sent", "individual");
                            } else {
                                sendNotif(token, myId, otherId, sms.SenderName, Constant.currentUserProfile, sms.Msg, "individual");
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            AppController.getFirebaseHelper().getTokens().child("Users").child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String token = "";
                        if (dataSnapshot.child("token").getValue() != null)
                            token = dataSnapshot.child("token").getValue(String.class);
                        if (msgType.equalsIgnoreCase("Media")) {

                            sendNotif(token, myId, otherId, sms.SenderName, Constant.currentUserProfile, "Media was sent", "individual");
                        } else {
                            sendNotif(token, myId, otherId, sms.SenderName, Constant.currentUserProfile, sms.Msg, "individual");
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void sendNotif(String token, String myId, String otherId, String myName, String senderProfile, String message,
                           String messageType) {
        Data data = new Data(myId, otherId, myName, senderProfile, R.mipmap.ic_launcher_round, message,
                "New Message", "SMS", messageType);
        Sender sender1 = new Sender(data, token);
        apiService.sendNotification(sender1).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Log.e("TAG", "unsuccess: ");
                    } else {
                        Log.e("TAG", "success: ");
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Log.e("TAG", "failur: " + t.getMessage());
            }
        });
    }


    public void createRecentChat(String myId, String otherId, Message msg) {

        fs.collection("RecentUser").document("CurrentUser").collection(myId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            List<String> Uid = new ArrayList<>();
                            for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                                switch (dc.getType()) {
                                    case ADDED:
                                        Uid.add(dc.getDocument().getId());
                                }
                            }
                            if (Uid.contains(otherId)) {
                                updateRecent(myId, otherId, msg, msg.MsgType);
                            } else {
                                createNewRecent(myId, otherId, msg, msg.MsgType);
                            }
                        } else {
                            createNewRecent(myId, otherId, msg, msg.MsgType);
                        }
                    }
                });

    }

    private void createNewRecent(String myId, String otherId, Message msg, String msgType) {

        if (msgType.equalsIgnoreCase("Media")) {
            fs.collection("RecentUser").document("CurrentUser").collection(myId).document(otherId)
                    .set(getRecentMap(Constant.chatUser.getUserId(), Constant.chatUser.getName(), Constant.chatUser.getProfile(),
                            "Media", msg.SentTime)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e("TAG", "success ");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("TAG", "onFailure" + e.getMessage());
                }
            });

            fs.collection("RecentUser").document("CurrentUser").collection(otherId).document(myId)
                    .set(getRecentMap(myId, Constant.currentUserName, Constant.currentUserProfile,
                            "Media", msg.SentTime)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e("TAG", "success ");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("TAG", "onFailure: can not sent " + e.getMessage());
                }
            });

        } else {
            fs.collection("RecentUser").document("CurrentUser").collection(myId).document(otherId)
                    .set(getRecentMap(Constant.chatUser.getUserId(), Constant.chatUser.getName(), Constant.chatUser.getProfile(),
                            msg.Msg, msg.SentTime)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e("TAG", "success ");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("TAG", "onFailure" + e.getMessage());
                }
            });

            fs.collection("RecentUser").document("CurrentUser").collection(otherId).document(myId)
                    .set(getRecentMap(myId, Constant.currentUserName, Constant.currentUserProfile,
                            msg.Msg, msg.SentTime)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e("TAG", "success ");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("TAG", "onFailure: can not sent " + e.getMessage());
                }
            });

        }
    }

    private void updateRecent(String myId, String otherUserId, Message msg, String msgType) {
        if (msgType.equalsIgnoreCase("Media")) {
            fs.collection("RecentUser").document("CurrentUser").collection(myId).document(otherUserId)
                    .update(getRecentMap(Constant.chatUser.getUserId(), Constant.chatUser.getName(), Constant.chatUser.getProfile(),
                            "Media", msg.SentTime)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e("TAG", "success recent2: can not sent ");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("TAG", "onFailure: can not sent " + e.getMessage());
                }
            });

            fs.collection("RecentUser").document("CurrentUser").collection(otherUserId).document(myId)
                    .update(getRecentMap(myId, Constant.currentUserName, Constant.currentUserProfile,
                            "Media", msg.SentTime)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e("TAG", "success recent2: can not sent ");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("TAG", "onFailure: can not sent " + e.getMessage());
                }
            });
        } else {
            fs.collection("RecentUser").document("CurrentUser").collection(myId).document(otherUserId)
                    .update(getRecentMap(Constant.chatUser.getUserId(), Constant.chatUser.getName(), Constant.chatUser.getProfile(),
                            msg.Msg, msg.SentTime)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e("TAG", "success recent2: can not sent ");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("TAG", "onFailure: can not sent " + e.getMessage());
                }
            });

            fs.collection("RecentUser").document("CurrentUser").collection(otherUserId).document(myId)
                    .update(getRecentMap(myId, Constant.currentUserName, Constant.currentUserProfile,
                            msg.Msg, msg.SentTime)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e("TAG", "success recent2: can not sent ");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("TAG", "onFailure: can not sent " + e.getMessage());
                }
            });
        }
    }

    private HashMap getRecentMap(String userId, String name, String profile, String lastMessage, String lastMessageTime) {
        HashMap recent = new HashMap<>();
        recent.put("userId", userId);
        recent.put("name", name);
        recent.put("profile", profile);
        recent.put("lastMessage", lastMessage);
        recent.put("lastMessageTime", lastMessageTime);
        return recent;
    }

    public void senRoomMessages(final Message msg, final String roomId, final String myId, final ChatLListener handler) {

        fs.collection("RoomMessages").document("AllMessage").collection(roomId).document()
                .set(getMessageMap(msg)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                checkAvailableCurrentUser(myId, msg.Msg, msg.SenderName, msg.MsgType);
                //sendGroupNotification(myId, msg.Msg, msg.SenderName, msg.MsgType);

                handler.onMessageSendSuccessforGroup(msg);
                if (msg.MsgType.equalsIgnoreCase("Media")) {
                    handler.onImageSendSuccessforGroup();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void sendGroupNotification(String myId, String sms, String myName, String msgType) {
        for (int i = 0; i < Constant.publicRooms.getRoomMembers().size(); i++) {
            getGroupUserNotificationSendStatus(Constant.publicRooms.getRoomMembers().get(i), myId, sms, myName, msgType);
        }
    }

    private void setGroupNotificationFinal(String myId, String sms, String myName, String msgType, String userId){
        if (userId.equals("appadmin")) {
            FirebaseDatabase.getInstance().getReference("Tokens").child("Admins").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String token = "";
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            token = dsp.child("token").getValue(String.class);

                            if (msgType.equalsIgnoreCase("Media")) {
                                sendNotif(token, myId, Constant.publicRooms.getRoomId(), Constant.publicRooms.getRoomName(),
                                        Constant.publicRooms.getRoomCoverImage(), "Media was sent", "group");
                            } else {
                                sendNotif(token, myId, Constant.publicRooms.getRoomId(), Constant.publicRooms.getRoomName(),
                                        Constant.publicRooms.getRoomCoverImage(), sms, "group");
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            if (!myId.equals(userId)) {
                AppController.getFirebaseHelper().getTokens().child("Users").child(userId).
                        addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String token = "";
                                    if (dataSnapshot.child("token").getValue() != null)
                                        token = dataSnapshot.child("token").getValue(String.class);

                                    if (msgType.equalsIgnoreCase("Media")) {
                                        sendNotif(token, myId, Constant.publicRooms.getRoomId(), Constant.publicRooms.getRoomName(),
                                                Constant.publicRooms.getRoomCoverImage(), "Media was sent", "group");
                                    } else {
                                        sendNotif(token, myId, Constant.publicRooms.getRoomId(), Constant.publicRooms.getRoomName(),
                                                Constant.publicRooms.getRoomCoverImage(), sms, "group");
                                    }
                                           }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        }
    }

    private void getUserNotificationSendStatus(String otherUserId, Message msg, String myId, String msgType) {
        FirebaseFirestore.getInstance().collection("ChatUser").document(otherUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().get("premiumType") != null && task.getResult().get("premiumType").toString().equalsIgnoreCase("p2")){
                    if (task.getResult().get("notificationStatus") != null &&
                            task.getResult().get("notificationStatus").toString().equalsIgnoreCase("Offline")){
                        sendMessageNotification(msg, myId, otherUserId, msgType);
                    }
                }
            }
        });
    }

    private void getGroupUserNotificationSendStatus(String userId, String myId, String sms, String myName, String msgType){
        FirebaseFirestore.getInstance().collection("ChatUser").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().get("premiumType") != null && task.getResult().get("premiumType").toString().equalsIgnoreCase("p2")){
                    if (task.getResult().get("notificationStatus") != null && task.getResult().get("notificationStatus").toString().equalsIgnoreCase("Offline"))
                        setGroupNotificationFinal(myId, sms, myName, msgType, userId);
                }
            }
        });
    }

    private void checkAvailableCurrentUser(String myId, String sms, String myName, String msgType){
        fs.collection("PublicRoom").document(Constant.publicRooms.getRoomId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                List<String> availableMember = (List<String>) task.getResult().get("Members");
                Log.e("TAG", "onComplete: "+ availableMember.size());
                for (int i = 0; i < availableMember.size(); i++) {
                    getGroupUserNotificationSendStatus(availableMember.get(i), myId, sms, myName, msgType);
                }
            }
        });
    }
}
