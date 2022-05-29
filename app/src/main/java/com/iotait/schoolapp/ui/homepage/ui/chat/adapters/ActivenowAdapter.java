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

import com.bumptech.glide.Glide;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.databinding.ItemActivenowBinding;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.ui.homepage.ui.chat.ChatWindowActivity;
import com.iotait.schoolapp.ui.homepage.ui.chat.constant.Constant;
import com.iotait.schoolapp.ui.homepage.ui.chat.models.ChatUser;

import java.util.ArrayList;
import java.util.List;

public class ActivenowAdapter extends RecyclerView.Adapter<ActivenowAdapter.ViewHolder> {

    Context context;
    List<ChatUser> availableChatUser;
    List<ChatUser> searchUser;

    public ActivenowAdapter(Context context, List<ChatUser> availableChatUser) {
        this.context = context;
        this.availableChatUser = availableChatUser;
    }

    public void setSearchUser(List<ChatUser> searchUser){
        this.searchUser = searchUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemActivenowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_activenow,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ChatUser chatUser = availableChatUser.get(position);
        UIHelper.setPersonImage(holder.binding.circleImageView, chatUser.getProfile());
        holder.binding.userNameTv.setText(chatUser.getName());

        if (!chatUser.getStatus().equalsIgnoreCase("Online")){
            holder.binding.activeImage.setImageResource(R.drawable.ic_assh_dot);
        } else {
            holder.binding.activeImage.setImageResource(R.drawable.ic_green_dot_vector);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatWindowActivity.class);
                Constant.chatUser = chatUser;
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return availableChatUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemActivenowBinding binding;
        public ViewHolder(@NonNull ItemActivenowBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
        }
    }

    public void filter(String text) {
        availableChatUser.clear();
        if(text.isEmpty()){
            availableChatUser.addAll(searchUser);
        } else{
            text = text.toLowerCase();
            for(ChatUser item: searchUser){
                if(item.getName().toLowerCase().contains(text)){
                    availableChatUser.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
