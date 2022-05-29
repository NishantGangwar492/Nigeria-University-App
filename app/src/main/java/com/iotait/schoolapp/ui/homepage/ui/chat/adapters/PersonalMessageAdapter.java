package com.iotait.schoolapp.ui.homepage.ui.chat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.databinding.ItemPersonalBinding;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.ui.homepage.ui.chat.ChatWindowActivity;
import com.iotait.schoolapp.ui.homepage.ui.chat.common.ChatRelatedCommonFunction;
import com.iotait.schoolapp.ui.homepage.ui.chat.constant.Constant;
import com.iotait.schoolapp.ui.homepage.ui.chat.models.ChatUser;
import com.iotait.schoolapp.utils.PublicFunctions;

import java.util.List;

public class PersonalMessageAdapter  extends RecyclerView.Adapter<PersonalMessageAdapter.ViewHolder> {
    Context context;
    List<ChatUser> recentChatUser;
    ChatRelatedCommonFunction chatRelatedCommonFunction;
    PublicFunctions pf = new PublicFunctions();

    public PersonalMessageAdapter(Context context, List<ChatUser> recentChatUser) {
        this.context = context;
        this.recentChatUser = recentChatUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPersonalBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_personal, parent, false);

        return new ViewHolder(binding);
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatUser user = recentChatUser.get(position);

        String[] array = user.getLastMessageTime().split(" ");
        // holder.binding.lastMMessageTime.setText(array[1]);
        String finaltime = pf.getTimeandDate(user.getLastMessageTime());

//        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
//        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
//        String displayValue = "";
//        try {
//            Date date = dateFormatter.parse(finaltime);
//            displayValue = timeFormatter.format(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        holder.binding.lastMMessageTime.setText(finaltime);
        holder.binding.name.setText(user.getName());
        UIHelper.setPersonImage(holder.binding.profileImage, user.getProfile());
        //  holder.binding.lastMessage.setText(user.getLastMessage());
        lastMessage(holder.binding.lastMessage, AppController.getFirebaseHelper().getFirebaseAuth().getUid(), user.getUserId());
        unreadChatCount(holder.binding.unseenMessageCount, user.getUserId(), AppController.getFirebaseHelper().getFirebaseAuth().getUid());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatWindowActivity.class);
                Constant.chatUser = user;
                context.startActivity(intent);
            }
        });
    }

    private void lastMessage(TextView lastMessage, String uid, String userId) {

        db.collection("RecentUser").document("CurrentUser").collection(uid).document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.get("lastMessage") != null)
                        lastMessage.setText(documentSnapshot.get("lastMessage").toString());
                }

            }
        });
//        db.collection("RecentUser").document("CurrentUser").collection(uid).document(userId).get().
//                addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                if (e != null) {
//                    Log.w("TAG", "listen:error", e);
//                    return;
//                }
//
//                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
//                    switch (dc.getType()) {
//                        case ADDED:
//                            ChatUser chatUser = new ChatUser();
//                            if (dc.getDocument().getData().get("name") != null)
//                                chatUser.setName(dc.getDocument().getData().get("name").toString());
//                            if (dc.getDocument().getData().get("profile") != null)
//                                chatUser.setProfile(dc.getDocument().getData().get("profile").toString());
//                            if (dc.getDocument().getData().get("lastMessage") != null)
//                                chatUser.setLastMessage(dc.getDocument().getData().get("lastMessage").toString());
//                            if (dc.getDocument().getData().get("lastMessageTime") != null)
//                                chatUser.setLastMessageTime(dc.getDocument().getData().get("lastMessageTime").toString());
//                            if (dc.getDocument().getData().get("userId") != null)
//                                chatUser.setUserId(dc.getDocument().getData().get("userId").toString());
//
//
//
//
//                            // getIsUserPremiumOrNot(dc.getDocument().getData().get("userId").toString(), chatUser);
//                    }
//                }
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return recentChatUser.size();
    }

    int countMessage = 0;
    FirebaseFirestore fs = FirebaseFirestore.getInstance();

    public void unreadChatCount(TextView textView, String otherUserId, String myId) {
        fs.collection("Message").document(myId + otherUserId).collection(myId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                countMessage = 0;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (document.getData().get("ReadStatus") != null && document.getData().get("ReadStatus").equals("unread")) {
                        textView.setVisibility(View.VISIBLE);
                        countMessage += 1;
                        textView.setText(String.valueOf(countMessage));
                    }
                }
                if (countMessage == 0) {
                    textView.setVisibility(View.GONE);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemPersonalBinding binding;
        public ViewHolder(@NonNull ItemPersonalBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
        }
    }
}
