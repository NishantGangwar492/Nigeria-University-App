package com.iotait.schoolapp.ui.homepage.ui.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.databinding.ActivityChatWindowGroupBinding;
import com.iotait.schoolapp.databinding.ActivityGroupAdminBinding;
import com.iotait.schoolapp.ui.homepage.ui.chat.adapters.GroupAdminAdapter;
import com.iotait.schoolapp.ui.homepage.ui.chat.constant.Constant;
import com.iotait.schoolapp.ui.homepage.ui.chat.interfaces.AddPerticipantsListener;
import com.iotait.schoolapp.ui.homepage.ui.chat.models.ModeratorDetails;

import java.util.ArrayList;
import java.util.List;

public class GroupAdminActivity extends AppCompatActivity implements AddPerticipantsListener {

    ActivityGroupAdminBinding binding;
    GroupAdminAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_admin);

        binding.toolbar.toolbar.setTitle("Participants");
        setSupportActionBar(binding.toolbar.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        adapter = new GroupAdminAdapter(this, membersList, this);
        binding.adminRcv.setLayoutManager(new LinearLayoutManager(this));
        binding.adminRcv.setAdapter(adapter);

        getAllRoomAdmin();
        String string = Constant.publicRooms.getRoomMembers().size()+" Participants";
        binding.textView6.setText(string);

    }

    private List<ModeratorDetails> membersList = new ArrayList<>();
    private void getAllRoomAdmin(){
        membersList.clear();
        for(int i = 0; i< Constant.publicRooms.getRoomMembers().size(); i++){
            FirebaseDatabase.getInstance().getReference("Users").child(Constant.publicRooms.getRoomMembers().get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String personname = "";
                        String username = "";
                        String photo = "";
                        String uid = "";
                        String email="";

                        if (dataSnapshot.child("personname").getValue() != null)
                            personname = dataSnapshot.child("personname").getValue(String.class);
                        if (dataSnapshot.child("username").getValue() != null)
                            username = dataSnapshot.child("username").getValue(String.class);
                        if (dataSnapshot.child("photo").getValue() != null)
                            photo = dataSnapshot.child("photo").getValue(String.class);
                        if (dataSnapshot.child("email").getValue() !=null)
                            email = dataSnapshot.child("email").getValue(String.class);
                        uid = dataSnapshot.getKey();
                        ModeratorDetails moderatorDetails = new ModeratorDetails();
                        moderatorDetails.setName(personname);
                        moderatorDetails.setProfile(photo);
                        moderatorDetails.setUsername(username);
                        moderatorDetails.setUserId(uid);
                        moderatorDetails.setEmail(email);
                        if (!uid.equals(AppController.getFirebaseHelper().getFirebaseAuth().getUid()))
                            membersList.add(moderatorDetails);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void removeParticipantListener(int position) {
        Constant.publicRooms.getRoomMembers().remove(membersList.get(position).getUserId());
        List<String> allmoderator = new ArrayList<>();
        allmoderator.addAll(Constant.publicRooms.getRoomMembers());

        db.collection("PublicRoom").document(Constant.publicRooms.getRoomId())
                .update("Members", allmoderator)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(GroupAdminActivity.this, "User Removed Success", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error updating document", e);
                    }
                });
    }
}
