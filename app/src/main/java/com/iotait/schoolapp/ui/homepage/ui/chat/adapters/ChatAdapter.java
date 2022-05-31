package com.iotait.schoolapp.ui.homepage.ui.chat.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.databinding.InSmsMediaBinding;
import com.iotait.schoolapp.databinding.InSmsTextBinding;
import com.iotait.schoolapp.databinding.OutSmsMediaBinding;
import com.iotait.schoolapp.databinding.OutSmsTextBinding;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.ui.homepage.ui.chat.FullPhotoViewActivity;
import com.iotait.schoolapp.ui.homepage.ui.chat.constant.Constant;
import com.iotait.schoolapp.ui.homepage.ui.chat.models.Message;
import com.iotait.schoolapp.utils.PublicFunctions;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {
    Context context;

    public static final int IN_Text  = 0;
    public static final int IN_Media = 1;
    public static final int OUT_Text  = 2;
    public static final int OUT_Media = 3;

    private List<Message> mValues = new ArrayList<>();
    PublicFunctions pf = new PublicFunctions();
    boolean isGroup;

    public ChatAdapter(Context context, List<Message> mValues, boolean isGroup) {
        this.context = context;
        this.mValues = mValues;
        this.isGroup = isGroup;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case IN_Media:
                InSmsMediaBinding inSmsMediaBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.in_sms_media, parent, false);
                IncomingMediaView inSmsMediaView  = new IncomingMediaView(inSmsMediaBinding);
                return inSmsMediaView;
            case OUT_Media:
                OutSmsMediaBinding outSmsMediaBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.out_sms_media, parent, false);
                OutgoingMediaView outgoingMediaView  = new OutgoingMediaView(outSmsMediaBinding);
                return outgoingMediaView;
            case IN_Text:
                InSmsTextBinding textBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.in_sms_text, parent, false);
                IncomingTextView incomingTextView = new IncomingTextView(textBinding);
                return incomingTextView;
            default:
                OutSmsTextBinding outTextBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.out_sms_text, parent, false);
                OutgoingTextView outgoingTextView = new OutgoingTextView(outTextBinding);
                return outgoingTextView;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message msg = mValues.get(position);
        int value = getItemViewType(position);
        boolean isLast =  position == 0;

        switch (value){
            case IN_Text:
                IncomingTextView inText = (IncomingTextView) holder;
                inText.inSmsTextBinding.leftMessage.setText(msg.Msg);
                inText.inSmsTextBinding.messageTime.setText(pf.getTimeandDate(msg.SentTime));
                if (isGroup){
                    inText.inSmsTextBinding.messageSenderName.setVisibility(View.VISIBLE);
                    inText.inSmsTextBinding.messageSenderName.setText(msg.SenderName);
                }
                if (msg.ReadStatus != null && msg.ReadStatus.equals("unread")){
                    updateReadStatus(msg.MessageId, AppController.getFirebaseHelper().getFirebaseAuth().getUid(), Constant.chatUser.getUserId());
                }
                return;
            case IN_Media:
                IncomingMediaView incomingMediaView = (IncomingMediaView) holder;
                if (isGroup){
                    incomingMediaView.inSmsMediaBinding.messageTime.setText(msg.SenderName + "    " + pf.getTimeandDate(msg.SentTime));
                } else {
                    incomingMediaView.inSmsMediaBinding.messageTime.setText(pf.getTimeandDate(msg.SentTime));
                }
                if (PublicFunctions.isVideo(msg.URLS.get(0))){
                    incomingMediaView.inSmsMediaBinding.playBtn.setVisibility(View.VISIBLE);
                } else {
                    incomingMediaView.inSmsMediaBinding.playBtn.setVisibility(View.GONE);
                }
                UIHelper.setImage(incomingMediaView.inSmsMediaBinding.mediaImg, msg.URLS.get(0));
                incomingMediaView.inSmsMediaBinding.mediaImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, FullPhotoViewActivity.class);
                        intent.putExtra("URL_KEY", msg.URLS.get(0));
                        context.startActivity(intent);
                    }
                });
                if (msg.ReadStatus != null && msg.ReadStatus.equals("unread")){
                    updateReadStatus(msg.MessageId, AppController.getFirebaseHelper().getFirebaseAuth().getUid(), Constant.chatUser.getUserId());
                }
                return;
            case OUT_Media:
                OutgoingMediaView outgoingMediaView = (OutgoingMediaView) holder;
                outgoingMediaView.outSmsMediaBinding.messageTime.setText(pf.getTimeandDate(msg.SentTime));
                if (PublicFunctions.isVideo(msg.URLS.get(0))){
                    outgoingMediaView.outSmsMediaBinding.playBtn.setVisibility(View.VISIBLE);
                } else {
                    outgoingMediaView.outSmsMediaBinding.playBtn.setVisibility(View.GONE);
                }
                UIHelper.setImage(outgoingMediaView.outSmsMediaBinding.mediaImg, msg.URLS.get(0));
                String s = msg.URLS.get(0).trim().toLowerCase();
                boolean isWeb = s.startsWith("http://") || s.startsWith("https://");
                if (isWeb){
                outgoingMediaView.outSmsMediaBinding.progressView.setVisibility(View.GONE);
                } else {
                    outgoingMediaView.outSmsMediaBinding.progressView.setVisibility(View.VISIBLE);
                }

                outgoingMediaView.outSmsMediaBinding.mediaImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, FullPhotoViewActivity.class);
                        intent.putExtra("URL_KEY", msg.URLS.get(0));
                        context.startActivity(intent);
                    }
                });
                if (msg.ReadStatus != null && msg.ReadStatus.equals("unread")){
                    updateReadStatus(msg.MessageId, AppController.getFirebaseHelper().getFirebaseAuth().getUid(), Constant.chatUser.getUserId());
                }

                return;
            default:
                OutgoingTextView ouText = (OutgoingTextView) holder;
                ouText.outSmsTextBinding.yourMeessage.setText(msg.Msg);
                ouText.outSmsTextBinding.messageTime.setText(pf.getTimeandDate(msg.SentTime));
                if (msg.ReadStatus != null && msg.ReadStatus.equals("unread")){
                    updateReadStatus(msg.MessageId, AppController.getFirebaseHelper().getFirebaseAuth().getUid(), Constant.chatUser.getUserId());
                }
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public int getItemViewType(int position) {

        Message msg = mValues.get(position);
        if (msg.MsgType.equalsIgnoreCase("Text")){
            if (msg.SenderId.equalsIgnoreCase(AppController.getFirebaseHelper().getFirebaseAuth().getUid())){
                return OUT_Text;
            }else{
                return IN_Text;
            }
        } else{
            if (msg.SenderId.equalsIgnoreCase(AppController.getFirebaseHelper().getFirebaseAuth().getUid())){
                return OUT_Media;
            }else{
                return IN_Media;
            }
        }
    }

    public void updateValues(ArrayList<Message> msg, RecyclerView recyclerView){
        mValues.clear();
        mValues.addAll(msg);
        notifyDataSetChanged();
        //recyclerView.smoothScrollToPosition(msg.size()-2);
    }

    FirebaseFirestore fs = FirebaseFirestore.getInstance();
    public void updateReadStatus(String messageId, String myId, String otherId){
        fs.collection("Message").document(myId+otherId).collection(myId)
                .document(messageId).update("ReadStatus", "read").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.e("TAG", "onComplete: status updated" );
            }
        });
    }

    static class IncomingMediaView extends RecyclerView.ViewHolder {
        InSmsMediaBinding inSmsMediaBinding;
        public IncomingMediaView(InSmsMediaBinding view) {
            super(view.getRoot());
            inSmsMediaBinding = view;
        }
    }

    static class OutgoingMediaView extends RecyclerView.ViewHolder {
        OutSmsMediaBinding outSmsMediaBinding;
        public OutgoingMediaView(OutSmsMediaBinding view) {
            super(view.getRoot());
            outSmsMediaBinding = view;
        }
    }

    static class OutgoingTextView extends RecyclerView.ViewHolder {
        OutSmsTextBinding outSmsTextBinding;
        public OutgoingTextView(OutSmsTextBinding view) {
            super(view.getRoot());
            outSmsTextBinding = view;
        }
    }

    static class IncomingTextView extends RecyclerView.ViewHolder {
        InSmsTextBinding inSmsTextBinding;

        public IncomingTextView(InSmsTextBinding view) {
            super(view.getRoot());
            inSmsTextBinding = view;
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }
}
