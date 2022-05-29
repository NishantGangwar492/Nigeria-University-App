package com.iotait.schoolapp.ui.homepage.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.databinding.ActivityChatWindowBinding;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.notification.APIService;
import com.iotait.schoolapp.notification.Client;
import com.iotait.schoolapp.ui.homepage.ui.chat.adapters.ChatAdapter;
import com.iotait.schoolapp.ui.homepage.ui.chat.common.ChatRelatedCommonFunction;
import com.iotait.schoolapp.ui.homepage.ui.chat.constant.Constant;
import com.iotait.schoolapp.ui.homepage.ui.chat.interfaces.ChatLListener;
import com.iotait.schoolapp.ui.homepage.ui.chat.models.ChatUser;
import com.iotait.schoolapp.ui.homepage.ui.chat.models.Message;
import com.iotait.schoolapp.utils.PublicFunctions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ChatWindowActivity extends AppCompatActivity implements ChatLListener {
    ActivityChatWindowBinding binding;
    ChatAdapter adapter;
    ChatRelatedCommonFunction chatRelatedCommonFunction;
    FirebaseFirestore fs = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;
    private static final int PICK_FILE_CONTENT = 1002;
    LinearLayoutManager layoutManager;
    ArrayList<Message> messages = new ArrayList<>();
    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_window);
        //chatWindowActivity=this;

        apiService = Client.getClient(getResources().getString(R.string.fcmlink)).create(APIService.class);
        chatRelatedCommonFunction = new ChatRelatedCommonFunction(this, apiService);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        layoutManager = new LinearLayoutManager(ChatWindowActivity.this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);

        adapter = new ChatAdapter(this, messages, false);
        adapter.setHasStableIds(true);

        //  binding.rvchats.setNestedScrollingEnabled(false);
        binding.rvchats.setLayoutManager(layoutManager);
        binding.rvchats.setHasFixedSize(true);
        binding.rvchats.setAdapter(adapter);


        setSupportActionBar(binding.toolbar.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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

//        binding.filePickIv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openFiles();
//            }
//        });

        //loadMessages(AppController.getFirebaseHelper().getFirebaseAuth().getUid(), Constant.chatUser.getUserId());
        //loadMessageNew(AppController.getFirebaseHelper().getFirebaseAuth().getUid(), Constant.chatUser.getUserId());
        if (getIntent().getStringExtra("senderId")!=null && getIntent().getStringExtra("receiver") !=null){
            loadMessageNew(getIntent().getStringExtra("receiver"), getIntent().getStringExtra("senderId"));
            ChatUser chatUser = new ChatUser();
            chatUser.setUserId(getIntent().getStringExtra("senderId"));
            chatUser.setName(getIntent().getStringExtra("senderName"));
            chatUser.setProfile(getIntent().getStringExtra("senderProfile"));
            Log.e("TAG,", "onCreate: " + getIntent().getStringExtra("senderName"));
            Constant.chatUser = chatUser;
            UIHelper.setPersonImage(binding.otherUserProfile, Constant.chatUser.getProfile());
            binding.otherUserName.setText(Constant.chatUser.getName());
        } else {
            loadMessageNew(AppController.getFirebaseHelper().getFirebaseAuth().getUid(), Constant.chatUser.getUserId());
            UIHelper.setPersonImage(binding.otherUserProfile, Constant.chatUser.getProfile());
            binding.otherUserName.setText(Constant.chatUser.getName());
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

    private void sendTextMessage(String message){
        String myId =  AppController.getFirebaseHelper().getFirebaseAuth().getUid();
        Message textSMS = new Message(UUID.randomUUID().toString(), PublicFunctions.LTU(), message, "Text", Constant.currentUserName,
               myId, "Sent", new ArrayList<String>(), Constant.currentUserProfile, "unread");

        chatRelatedCommonFunction.sentMessage(textSMS, AppController.getFirebaseHelper().getFirebaseAuth().getUid(), Constant.chatUser.getUserId(), this);
    }

    private void loadMessages(String myId, String otherId){

        fs.collection("Message").document(myId+otherId).collection(myId).orderBy("SentTime", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {

                    ArrayList<DocumentSnapshot> documentSnapshots = (ArrayList<DocumentSnapshot>) queryDocumentSnapshots.getDocuments();
                    messages.clear();
                    for (int i = 0 ; i<documentSnapshots.size() ; i++){
                        HashMap currentRecent = (HashMap) documentSnapshots.get(i).getData();
                        messages.add(getMessageFromMap(currentRecent));
                        Log.e("TAG", "onSuccess: " + documentSnapshots.size());
                    }
                    adapter.updateValues(messages, binding.rvchats);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void loadMessageNew(String myId, String otherId) {

        fs.collection("Message").document(myId + otherId).collection(myId).orderBy("SentTime")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@com.google.firebase.database.annotations.Nullable QuerySnapshot snapshots,
                                        @com.google.firebase.database.annotations.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
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
//                .addSnapshotListener(this,
//                new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//
//                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
//                    List<DocumentChange> documentSnapshots = queryDocumentSnapshots.getDocumentChanges();
//                    for (int i = 0 ; i<documentSnapshots.size() ; i++){
//                        HashMap currentSMS = (HashMap) documentSnapshots.get(i).getDocument().getData();
//                        Message currentMsg = getMessageFromMap(currentSMS);
//                        messages.add(currentMsg);
//                    }
//                    int scrollPosition = messages.size();
//                    adapter.notifyDataSetChanged();
//                    binding.rvchats.smoothScrollToPosition(scrollPosition);
//                }
//            }
//        });
    }



    private Message getMessageFromMap(HashMap data){
        Message msg = new Message((String)data.get("MessageId"),(String)data.get("SentTime"),(String) data.get("Message"),
                (String) data.get("MsgType"),(String) data.get("SenderName"),(String) data.get("SenderId"), (String) data.get("Status"),
                (List<String>) data.get("URLS"),(String) data.get("Profile"),(String) data.get("ReadStatus"));
        return msg;
    }

    private void openPicker(){

        Log.e("TAG", "openPicker: picker opened");
        ArrayList<Image> images = new ArrayList<>();
        ImagePicker imagePicker = ImagePicker.create(ChatWindowActivity.this);
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

    //for open application type file
    private void openFiles() {
        Intent intent = new Intent();
        intent.setType("application/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, "Select File(s)"), PICK_FILE_CONTENT);
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

    private void getMediaMessageForImmediateView(){
        String myId =  AppController.getFirebaseHelper().getFirebaseAuth().getUid();
        Message textSMS = new Message(UUID.randomUUID().toString(), PublicFunctions.LTU(), " ", "Media", Constant.currentUserName,
                myId, "Sent", mediaUrl, Constant.currentUserProfile,"");
        messages.add(textSMS);
        adapter.notifyDataSetChanged();
        binding.rvchats.smoothScrollToPosition(messages.size());
    }

    private void sendMediaMessage(){
        String myId =  AppController.getFirebaseHelper().getFirebaseAuth().getUid();
        Message textSMS = new Message(UUID.randomUUID().toString(), PublicFunctions.LTU(), " ", "Media", Constant.currentUserName,
                myId, "Sent", mediaUrl, Constant.currentUserProfile,"unread");
        chatRelatedCommonFunction.sentMessage(textSMS, AppController.getFirebaseHelper().getFirebaseAuth().getUid(), Constant.chatUser.getUserId(), this);
    }

    protected void hideSoftKeyboard(EditText input) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }


    @Override
    public void onImageSendSuccess() {
        messages.clear();
        loadMessageNew(AppController.getFirebaseHelper().getFirebaseAuth().getUid(), Constant.chatUser.getUserId());
    }

    @Override
    public void onMessageSendSuccess(Message msg) {
        //messages.clear();
        //  loadMessageNew(AppController.getFirebaseHelper().getFirebaseAuth().getUid(), Constant.chatUser.getUserId());
        chatRelatedCommonFunction.createRecentChat(AppController.getFirebaseHelper().getFirebaseAuth().getUid(), Constant.chatUser.getUserId(), msg);
    }

    @Override
    public void onImageSendSuccessforGroup() {

    }

    @Override
    public void onMessageSendSuccessforGroup(Message msg) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ChatWindowActivity.this, ChatListActivty.class));
    }
}
