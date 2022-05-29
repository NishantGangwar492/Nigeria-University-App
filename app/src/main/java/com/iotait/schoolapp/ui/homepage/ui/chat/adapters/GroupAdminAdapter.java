package com.iotait.schoolapp.ui.homepage.ui.chat.adapters;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.iotait.schoolapp.R;
import com.iotait.schoolapp.databinding.ItemMakeadminBinding;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.ui.homepage.ui.chat.constant.Constant;
import com.iotait.schoolapp.ui.homepage.ui.chat.interfaces.AddPerticipantsListener;
import com.iotait.schoolapp.ui.homepage.ui.chat.models.ModeratorDetails;

import java.util.List;

public class GroupAdminAdapter extends RecyclerView.Adapter<GroupAdminAdapter.ViewHolder> {

    Context context;
    List<ModeratorDetails> moderatorDetailsList;
    AddPerticipantsListener listener;

    public GroupAdminAdapter(Context context, List<ModeratorDetails> moderatorDetailsList, AddPerticipantsListener listener) {
        this.context = context;
        this.moderatorDetailsList = moderatorDetailsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMakeadminBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_makeadmin,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModeratorDetails moderatorDetails = moderatorDetailsList.get(position);

        UIHelper.setPersonImage(holder.binding.publicimage, moderatorDetails.getProfile());
        holder.binding.publicname.setText(moderatorDetails.getName());
        holder.binding.emailTv.setText(moderatorDetails.getEmail());
        holder.binding.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.removeParticipantListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moderatorDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemMakeadminBinding binding;
        public ViewHolder(@NonNull ItemMakeadminBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
        }
    }

}
