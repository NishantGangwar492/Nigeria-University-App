package com.iotait.schoolapp.ui.homepage.ui.leaderboard.alltime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.databinding.ItemLeaderBoardBinding;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.ui.homepage.ui.leaderboard.model.LeaderBoardModel;
import com.iotait.schoolapp.ui.homepage.ui.leaderboard.model.userDetailsModel;
import com.iotait.schoolapp.ui.homepage.ui.leaderboard.view.LeaderBoardView;

import java.text.SimpleDateFormat;
import java.util.List;

public class LeaderBoardAdapterForAllTime extends RecyclerView.Adapter<LeaderBoardAdapterForAllTime.ViewHolder> {

    Context context;
    List<LeaderBoardModel> leaderBoardModelList;
    LeaderBoardView leaderBoardView;

    public LeaderBoardAdapterForAllTime(Context context, List<LeaderBoardModel> leaderBoardModelList, LeaderBoardView leaderBoardView) {
        this.context = context;
        this.leaderBoardModelList = leaderBoardModelList;
        this.leaderBoardView = leaderBoardView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLeaderBoardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_leader_board, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LeaderBoardModel leaderBoardModel = leaderBoardModelList.get(position);
        if (position <= 2) {
            holder.binding.item.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.leaderboardwhite));

        } else {
            holder.binding.item.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.leaderboard));
        }

        holder.binding.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaderBoardView.OnLeaderBoardItemClickAllTime(leaderBoardModel);
            }
        });
        int pos = position + 1;
        holder.binding.position.setText("" + pos+" ");
        holder.binding.score.setText(String.valueOf(leaderBoardModel.getScore())+"/400");
        UIHelper.leaderName(holder.binding.name,leaderBoardModel.getUid());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        String date = simpleDateFormat.format(leaderBoardModel.getTimeTake());
        holder.binding.takeTime.setText("" + date + " min");


    }

    @Override
    public int getItemCount() {
        return leaderBoardModelList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemLeaderBoardBinding binding;

        public ViewHolder(@NonNull ItemLeaderBoardBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
