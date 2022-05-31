package com.iotait.schoolapp.ui.homepage.ui.chat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.databinding.ActivityChatWindowGroupBinding;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.notification.APIService;
import com.iotait.schoolapp.notification.Client;
import com.iotait.schoolapp.ui.homepage.ui.chat.adapters.ChatAdapter;
import com.iotait.schoolapp.ui.homepage.ui.chat.common.ChatRelatedCommonFunction;
import com.iotait.schoolapp.ui.homepage.ui.chat.constant.Constant;
import com.iotait.schoolapp.ui.homepage.ui.chat.interfaces.ChatLListener;
import com.iotait.schoolapp.ui.homepage.ui.chat.models.Message;
import com.iotait.schoolapp.ui.homepage.ui.chat.models.PublicRooms;
import com.iotait.schoolapp.utils.PublicFunctions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ChatWindowGroupActivity extends AppCompatActivity implements ChatLListener {
    ActivityChatWindowGroupBinding binding;
    ChatAdapter adapter;
    ChatRelatedCommonFunction chatRelatedCommonFunction;
    FirebaseFirestore fs = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;
    private static final int PICK_FILE_CONTENT = 1002;
    LinearLayoutManager layoutManager;
    ArrayList<Message> messages = new ArrayList<>();
    String groupId = "";
    APIService apiService;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_window_group);
        //new CommentKeyBoardFix(this);

        apiService = Client.getClient(getResources().getString(R.string.fcmlink)).create(APIService.class);
        chatRelatedCommonFunction = new ChatRelatedCommonFunction(this, apiService);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setSupportActionBar(binding.toolbar.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (getIntent().getStringExtra("ROOM_ID") != null)
            groupId = getIntent().getStringExtra("ROOM_ID");

        layoutManager = new LinearLayoutManager(ChatWindowGroupActivity.this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        adapter = new ChatAdapter(this, messages, true);
        adapter.setHasStableIds(true);
        binding.rvchats.setLayoutManager(layoutManager);
        binding.rvchats.setHasFixedSize(true);
        binding.rvchats.setAdapter(adapter);


        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextMessage(binding.smsWriteET.getText().toString());
                binding.smsWriteET.getText().clear();
            }
        });

        binding.imagePickIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPicker();
            }
        });

        //loadMessages(groupId);
        if (getIntent().getStringExtra("senderId")!=null && getIntent().getStringExtra("receiver") !=null){
            groupId = getIntent().getStringExtra("receiver");
            getSingleGroupInformation(groupId);
            loadMessages(groupId);

            UIHelper.setPersonImage(binding.otherUserProfile, getIntent().getStringExtra("senderProfile"));
            binding.otherUserName.setText(getIntent().getStringExtra("senderName"));
        } else {
            loadMessages(groupId);
            if (Constant.publicRooms.getRoomCoverImage()!=null)
                UIHelper.setPersonImage(binding.otherUserProfile, Constant.publicRooms.getRoomCoverImage());
            binding.otherUserName.setText(Constant.publicRooms.getRoomName());
        }

        binding.rvchats.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    binding.rvchats.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.rvchats.smoothScrollToPosition(
                                    messages.size());
                        }
                    }, 100);
                }
            }
        });
    }

    private void getSingleGroupInformation(String groupId){
        FirebaseFirestore.getInstance().collection("PublicRoom").document(groupId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        PublicRooms rooms = new PublicRooms();
                        rooms.setRoomName(document.getData().get("RoomName").toString());
                        rooms.setRoomCoverImage(document.getData().get("CoverImage").toString());
                        rooms.setRoomId(document.getId());
                        rooms.setRoomMembers((List<String>) document.get("Members"));
                        rooms.setModerators((List<String>) document.get("Moderators"));
                        Constant.publicRooms = rooms;
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    private void sendTextMessage(String message){
        String myId = AppController.getFirebaseHelper().getFirebaseAuth().getUid();
        Message textSMS = new Message(UUID.randomUUID().toString(), PublicFunctions.LTU(), message, "Text", Constant.currentUserName,
                myId, "Sent", new ArrayList<String>(), Constant.currentUserProfile, "");
        chatRelatedCommonFunction.senRoomMessages(textSMS, groupId, AppController.getFirebaseHelper().getFirebaseAuth().getUid(), this);
    }

    private void loadMessages(String roomId){

        fs.collection("RoomMessages").document("AllMessage").collection(roomId).orderBy("SentTime")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@com.google.firebase.database.annotations.Nullable QuerySnapshot snapshots,
                                        @com.google.firebase.database.annotations.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    HashMap currentSMS = (HashMap) dc.getDocument().getData();
                                    Message currentMsg = getMessageFromMap(currentSMS);
                                    messages.add(currentMsg);
                            }
                        }
                        int scrollPosition = messages.size();
                        adapter.notifyDataSetChanged();
                        binding.rvchats.smoothScrollToPosition(scrollPosition);
                    }
                });
    }

    private Message getMessageFromMap(HashMap data){
        Message msg = new Message((String)data.get("MessageId"),(String)data.get("SentTime"),(String) data.get("Message"),
                (String) data.get("MsgType"),(String) data.get("SenderName"),(String) data.get("SenderId"), (String) data.get("Status"),
                (List<String>) data.get("URLS"),(String) data.get("Profile"),(String) data.get("ReadTime"));
        return msg;
    }

    private void openPicker(){

        Log.e("TAG", "openPicker: picker opened");
        ArrayList<Image> images = new ArrayList<>();
        ImagePicker imagePicker = ImagePicker.create(ChatWindowGroupActivity.this);
        imagePicker.language("en")
                .theme(R.style.ImagePickerTheme)
                .folderMode(false)
                .toolbarArrowColor(Color.RED)
                .toolbarFolderTitle("Folder")
                .toolbarImageTitle("Tap to select")
                .origin(images)
                .showCamera(true)
                .returnMode(ReturnMode.ALL)
                .single()
                .includeVideo(false)
                .imageDirectory("Camera")
                .imageFullDirectory(Environment.getExternalStorageDirectory().getPath())
                .toolbarDoneButtonText("DONE")
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image img = ImagePicker.getImages(data).get(0);
            Uri selectedMediaUri = Uri.fromFile(new File(img.getPath()));
            if(PublicFunctions.isImage(selectedMediaUri.getPath())){
                uploadImageToStorage(img.getPath(), false);
                mediaUrl.clear();
                mediaUrl.add(img.getPath());
                getMediaMessageForImmediateView();
            }else{
                uploadImageToStorage(img.getPath(), true);
                mediaUrl.clear();
                mediaUrl.add(img.getPath());
                getMediaMessageForImmediateView();
            }
        }
    }

    List<String> mediaUrl = new ArrayList<>();
    private void uploadImageToStorage(String path, boolean isVideo){
        Uri file = Uri.fromFile(new File(path));
        storageRef = storage.getReference();
        UploadTask uploadTask;
        StorageReference uploadRef;

        if (isVideo){
            uploadRef = storageRef.child("IndividualMessage").child("videos").child(""+file.getLastPathSegment());
            uploadTask = uploadRef.putFile(file);
        } else {
            uploadRef = storageRef.child("IndividualMessage").child("images").child(""+file.getLastPathSegment());
            uploadTask = uploadRef.putFile(file);
        }

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return uploadRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    mediaUrl.clear();
                    mediaUrl.add(String.valueOf(downloadUri));
                    sendMediaMessage();
                }
            }
        });
    }

    private void updateStatus(String status){
        if (AppController.getFirebaseHelper().getFirebaseAuth().getCurrentUser()!=null){
            FirebaseFirestore.getInstance().collection("ChatUser").document(AppController.getFirebaseHelper().getFirebaseAuth().getUid())
                    .update("notificationStatus", status)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateStatus("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateStatus("Offline");
    }

    private void getMediaMessageForImmediateView(){
        String myId =  AppController.getFirebaseHelper().getFirebaseAuth().getUid();
        Message textSMS = new Message(UUID.randomUUID().toString(), PublicFunctions.LTU(), " ", "Media", Constant.currentUserName,
                myId, "Sent", mediaUrl, Constant.currentUserProfile,"");
        messages.add(textSMS);
        adapter.notifyDataSetChanged();
        binding.rvchats.smoothScrollToPosition(messages.size());

    }

    private void sendMediaMessage(){
        String myId = AppController.getFirebaseHelper().getFirebaseAuth().getUid();
        Message textSMS = new Message(UUID.randomUUID().toString(), PublicFunctions.LTU(), " ", "Media", Constant.currentUserName,
                myId, "Sent", mediaUrl, Constant.currentUserProfile, "");
        chatRelatedCommonFunction.senRoomMessages(textSMS, groupId, AppController.getFirebaseHelper().getFirebaseAuth().getUid(), this);
    }

    protected void hideSoftKeyboard(EditText input) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }


    @Override
    public void onImageSendSuccess() {

    }

    @Override
    public void onMessageSendSuccess(Message msg) {
        //chatRelatedCommonFunction.createRecentChat(AppController.getFirebaseHelper().getFirebaseAuth().getUid(), Constant.chatUser.getUserId(), msg);
    }

    @Override
    public void onImageSendSuccessforGroup() {
        messages.clear();
        loadMessages(groupId);
    }

    @Override
    public void onMessageSendSuccessforGroup(Message msg) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu1, menu);
        if (getIntent().getStringExtra("senderId") != null) {
            Log.e("TAG", "onCreateOptionsMenu: problem");
        } else {
            if (!Constant.publicRooms.getModerators().contains(AppController.getFirebaseHelper().getFirebaseAuth().getUid())) {
                MenuItem item = menu.findItem(R.id.view_participants);
                item.setVisible(false);
            }
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.view_participants:
                addAndViewPerticipants();
                return true;
            case R.id.leave_group:

                if (dialog != null) {
                    dialog.dismiss();
                }
                dialog = new Dialog(ChatWindowGroupActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.conformation_dialoge);
                dialog.setCanceledOnTouchOutside(true);
                Button yes = dialog.findViewById(R.id.yes);
                Button no = dialog.findViewById(R.id.no);

                TextView textView = dialog.findViewById(R.id.txtPersonName);
                TextView textconform = dialog.findViewById(R.id.textconform);
                ImageView imageconform = dialog.findViewById(R.id.imageconform);
                textconform.setVisibility(View.VISIBLE);
                imageconform.setVisibility(View.GONE);
                textconform.setText("Leave Group");
                textView.setText("Do You Want leave the Group ?");

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog != null)
                            dialog.dismiss();
                        removeUser();
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog != null)
                            dialog.dismiss();
                    }
                });

                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void addAndViewPerticipants(){
        checkAvailableCurrentUser();
    }

    private void removeUser(){
        Constant.publicRooms.getRoomMembers().remove(AppController.getFirebaseHelper().getFirebaseAuth().getUid());
        List<String> allmoderator = new ArrayList<>();
        allmoderator.addAll(Constant.publicRooms.getRoomMembers());

        fs.collection("PublicRoom").document(Constant.publicRooms.getRoomId())
                .update("Members", allmoderator)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ChatWindowGroupActivity.this, "User Removed Success", Toast.LENGTH_SHORT).show();
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

    private void checkAvailableCurrentUser(){
        fs.collection("PublicRoom").document(Constant.publicRooms.getRoomId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().get("Moderators") != null){
                    List<String> availableModerators = (List<String>) task.getResult().get("Moderators");
                    if (availableModerators.contains(AppController.getFirebaseHelper().getFirebaseAuth().getUid())){
                        Intent intent = new Intent(ChatWindowGroupActivity.this, GroupAdminActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ChatWindowGroupActivity.this, "You are removed from moderators", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChatWindowGroupActivity.this, "No moderator available", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ChatWindowGroupActivity.this, ChatListActivty.class));
    }
}
