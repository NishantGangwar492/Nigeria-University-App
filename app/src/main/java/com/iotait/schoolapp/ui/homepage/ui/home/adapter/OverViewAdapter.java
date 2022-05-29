package com.iotait.schoolapp.ui.homepage.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.iotait.schoolapp.R;
import com.iotait.schoolapp.databinding.ItemOverviewBinding;
import com.iotait.schoolapp.ui.homepage.ui.home.view.OverViewHomeView;

public class OverViewAdapter extends RecyclerView.Adapter<OverViewAdapter.ViewHolder> {

    Context context;
    String[] name;
    int[] images;
    View root;
    OverViewHomeView overViewHomeView;

    public OverViewAdapter(Context context, String[] name, int[] images, View root, OverViewHomeView overViewHomeView) {
        this.context = context;
        this.name = name;
        this.images = images;
        this.root = root;
        this.overViewHomeView = overViewHomeView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOverviewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_overview, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.binding.textoverview.setText(name[position]);
        holder.binding.imageoverview.setImageResource(images[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (position == 0) {
                    overViewHomeView.onQuestionClick();

                }
                if (position == 1) {
                    overViewHomeView.onUnnNewsClick();

                }
                if (position == 2) {
                    overViewHomeView.onArticlesClick();

                }
                if (position == 3) {
                    overViewHomeView.onTakeExamClick();

                }
                if (position == 4) {
                    overViewHomeView.onSchoolAnthemClick();

                }
                if (position == 5) {

                    overViewHomeView.onSyllabusClick();

                }
                if (position == 6) {

                    overViewHomeView.onWeeklyContestClick();

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemOverviewBinding binding;

        public ViewHolder(@NonNull ItemOverviewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
