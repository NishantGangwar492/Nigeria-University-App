package com.iotait.schoolapp.ui.homepage.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.databinding.ActivityChatListBinding;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.ui.homepage.HomeNavigationActivity;
import com.iotait.schoolapp.ui.homepage.ui.chat.adapters.ActivenowAdapter;
import com.iotait.schoolapp.ui.homepage.ui.chat.adapters.PersonalMessageAdapter;
import com.iotait.schoolapp.ui.homepage.ui.chat.adapters.PublicRoomAdapter;
import com.iotait.schoolapp.ui.homepage.ui.chat.constant.Constant;
import com.iotait.schoolapp.ui.homepage.ui.chat.interfaces.ChatListListener;
import com.iotait.schoolapp.ui.homepage.ui.chat.models.ChatUser;
import com.iotait.schoolapp.ui.homepage.ui.chat.models.PublicRooms;

import java.util.ArrayList;
import java.util.List;

public class ChatListActivty extends AppCompatActivity implements ChatListListener {

    ActivityChatListBinding binding;
    ActivenowAdapter activenowAdapter;
    PublicRoomAdapter publicRoomAdapter;
    PersonalMessageAdapter personalMessageAdapter;
    FirebaseFirestore db;
    List<ChatUser> availableChatUser;
    List<ChatUser> availableChatUserCopy;
    List<ChatUser> recentChatUser;
    List<PublicRooms> allPublicRooms;
    List<String> premiumUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_list);
        //setContentView();

        db = FirebaseFirestore.getInstance();
        availableChatUser = new ArrayList<>();
        availableChatUserCopy = new ArrayList<>();
        recentChatUser = new ArrayList<>();
        allPublicRooms = new ArrayList<>();
        premiumUserId = new ArrayList<>();

        activenowAdapter = new ActivenowAdapter(this, availableChatUser);
        binding.rvactive.setAdapter(activenowAdapter);
        binding.rvactive.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        getAllPublicRoomsRealtimes();
        publicRoomAdapter = new PublicRoomAdapter(this, allPublicRooms, this);
        binding.rvpublicroom.setAdapter(publicRoomAdapter);
        binding.rvpublicroom.setLayoutManager(new LinearLayoutManager(this));


        personalMessageAdapter = new PersonalMessageAdapter(this, recentChatUser);
        binding.rvpersonalmsz.setAdapter(personalMessageAdapter);
        binding.rvpersonalmsz.setLayoutManager(new LinearLayoutManager(this));


        getIsUserPremiumOrNot();
        getRecentChatUserNew(AppController.getFirebaseHelper().getFirebaseAuth().getUid());


        getAllChatUser();

        getAdminUser();

        binding.adminUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatListActivty.this, ChatWindowActivity.class);
                Constant.chatUser = adminUser;
                startActivity(intent);
            }
        });
        searchChatUser();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    ChatUser adminUser = new ChatUser();

    private void getAdminUser() {
        adminUser.setUserId("appadmin");
        adminUser.setName("Admin");
        adminUser.setStatus("Offline");
        adminUser.setProfile("");
    }

    private void getChatUsers() {

        db.collection("ChatUser").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ChatUser chatUser = new ChatUser();
                        if (!document.getId().toString().equals(AppController.getFirebaseHelper().getFirebaseAuth().getUid())){
                            chatUser.setName(document.getData().get("name").toString());
                            chatUser.setProfile(document.getData().get("profile").toString());
                            chatUser.setStatus(document.getData().get("status").toString());
                            chatUser.setUserId(document.getId().toString());
                            availableChatUser.add(chatUser);
                        }
                    }
                    activenowAdapter.notifyDataSetChanged();
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void getAllChatUser(){
        db.collection("ChatUser").whereEqualTo("premiumType", "p2")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@com.google.firebase.database.annotations.Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    ChatUser chatUser = new ChatUser();
                                    if (!dc.getDocument().getId().toString().equals(AppController.getFirebaseHelper().getFirebaseAuth().getUid())){
                                            chatUser.setName(dc.getDocument().getData().get("name").toString());
                                            chatUser.setProfile(dc.getDocument().getData().get("profile").toString());
                                            chatUser.setStatus(dc.getDocument().getData().get("status").toString());
                                            chatUser.setUserId(dc.getDocument().getId().toString());
                                            chatUser.setPremiumType(dc.getDocument().get("premiumType").toString());
                                            availableChatUser.add(chatUser);
                                            availableChatUserCopy.add(chatUser);
                                    } else {
                                        binding.chatusername.setText(dc.getDocument().getData().get("name").toString());
                                        UIHelper.setPersonImage(binding.chatusericon, dc.getDocument().getData().get("profile").toString());
                                    }
                            }
                        }
                        activenowAdapter.notifyDataSetChanged();
                        activenowAdapter.setSearchUser(availableChatUserCopy);
                    }
                });
    }

    private void getRecentChatUser(String myId){
        db.collection("RecentUser").document("CurrentUser").collection(myId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ChatUser chatUser = new ChatUser();
                        chatUser.setName(document.getData().get("name").toString());
                        chatUser.setProfile(document.getData().get("profile").toString());
                        chatUser.setLastMessage(document.getData().get("lastMessage").toString());
                        chatUser.setLastMessageTime(document.getData().get("lastMessageTime").toString());
                        chatUser.setUserId(document.getData().get("userId").toString());
                        recentChatUser.add(chatUser);
                    }
                    personalMessageAdapter.notifyDataSetChanged();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "onFailure: failed");
            }
        });
    }

    private void getIsUserPremiumOrNot() {
        premiumUserId.clear();

        db.collection("ChatUser").whereEqualTo("premiumType", "p2")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@com.google.firebase.database.annotations.Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    premiumUserId.add(dc.getDocument().getId());
                            }
                        }

                    }
                });
    }

    private void getRecentChatUserNew(String myId) {
        db.collection("RecentUser").document("CurrentUser").collection(myId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "listen:error ", e);
                    return;
                }

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            ChatUser chatUser = new ChatUser();
                            if (dc.getDocument().getData().get("name") != null)
                                chatUser.setName(dc.getDocument().getData().get("name").toString());
                            if (dc.getDocument().getData().get("profile") != null)
                                chatUser.setProfile(dc.getDocument().getData().get("profile").toString());
                            if (dc.getDocument().getData().get("lastMessage") != null)
                                chatUser.setLastMessage(dc.getDocument().getData().get("lastMessage").toString());
                            if (dc.getDocument().getData().get("lastMessageTime") != null)
                                chatUser.setLastMessageTime(dc.getDocument().getData().get("lastMessageTime").toString());
                            if (dc.getDocument().getData().get("userId") != null)
                                chatUser.setUserId(dc.getDocument().getData().get("userId").toString());

                            if (premiumUserId.contains(dc.getDocument().getData().get("userId").toString())) {
                                recentChatUser.add(chatUser);
                            }


                            // getIsUserPremiumOrNot(dc.getDocument().getData().get("userId").toString(), chatUser);
                    }
                }

                personalMessageAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getAllPublicRoomsRealtimes() {
        db.collection("PublicRoom")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@com.google.firebase.database.annotations.Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    PublicRooms rooms = new PublicRooms();
                                    rooms.setRoomName(dc.getDocument().getData().get("RoomName").toString());
                                    rooms.setRoomCoverImage(dc.getDocument().getData().get("CoverImage").toString());
                                    rooms.setRoomId(dc.getDocument().getId());
                                    rooms.setRoomMembers((List<String>) dc.getDocument().get("Members"));
                                    rooms.setModerators((List<String>) dc.getDocument().get("Moderators"));
                                    allPublicRooms.add(rooms);
                            }
                        }
                        publicRoomAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onRoomJoinClick(String roomId, List<String> member) {
        member.add(AppController.getFirebaseHelper().getFirebaseAuth().getUid());
        db.collection("PublicRoom").document(roomId)
                .update("Members", member)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ChatListActivty.this, "You are now member of this room", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error updating room members", e);
                    }
                });

    }

    private void searchChatUser(){
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                activenowAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                activenowAdapter.filter(newText);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ChatListActivty.this, HomeNavigationActivity.class));
    }
}
