package com.iotait.schoolapp.ui.homepage.ui.chat.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.databinding.ItemPublicroomBinding;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.ui.homepage.ui.chat.ChatWindowActivity;
import com.iotait.schoolapp.ui.homepage.ui.chat.ChatWindowGroupActivity;
import com.iotait.schoolapp.ui.homepage.ui.chat.constant.Constant;
import com.iotait.schoolapp.ui.homepage.ui.chat.interfaces.ChatListListener;
import com.iotait.schoolapp.ui.homepage.ui.chat.models.PublicRooms;

import java.util.List;

public class PublicRoomAdapter extends RecyclerView.Adapter<PublicRoomAdapter.ViewHolder> {

    Context context;
    List<PublicRooms> rooms;
    ChatListListener listListener;

    public PublicRoomAdapter(Context context, List<PublicRooms> rooms, ChatListListener listListener) {
        this.context = context;
        this.rooms = rooms;
        this.listListener = listListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPublicroomBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_publicroom,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PublicRooms publicRooms = rooms.get(position);

        holder.binding.publicname.setText(publicRooms.getRoomName());
        UIHelper.setPersonImage(holder.binding.publicimage, publicRooms.getRoomCoverImage());
        holder.binding.totalCount.setText(publicRooms.getRoomMembers().size() + " People");

        if (publicRooms.getRoomMembers().contains(AppController.getFirebaseHelper().getFirebaseAuth().getUid())){
            holder.binding.joinnow.setText("Joined");
        } else {
            holder.binding.joinnow.setText("Join Now");
        }

        holder.binding.joinnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.binding.joinnow.getText().toString().equalsIgnoreCase("Join Now"))
                    listListener.onRoomJoinClick(publicRooms.getRoomId(), publicRooms.getRoomMembers());
                else
                    Toast.makeText(context, "You are already a member of this room", Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (publicRooms.getRoomMembers().contains(AppController.getFirebaseHelper().getFirebaseAuth().getUid())){
                    Intent intent = new Intent(context, ChatWindowGroupActivity.class);
                    Constant.publicRooms = publicRooms;
                    intent.putExtra("ROOM_ID", publicRooms.getRoomId());
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "You are not a member of this Room", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemPublicroomBinding binding;
        public ViewHolder(@NonNull ItemPublicroomBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
        }
    }
}
