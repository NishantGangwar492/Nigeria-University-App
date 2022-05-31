package com.iotait.schoolapp.ui.homepage.ui.unn_news.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.iotait.schoolapp.R;
import com.iotait.schoolapp.databinding.ItemUnnNewsBinding;
import com.iotait.schoolapp.ui.homepage.ui.home.model.Datum;
import com.iotait.schoolapp.ui.homepage.ui.home.view.HomeView;

import java.util.List;

public class UnnNewsAdapter extends RecyclerView.Adapter<UnnNewsAdapter.ViewHolder> {

    Context context;
    List<Datum> datumList;
    HomeView homeView;

    public UnnNewsAdapter(Context context, List<Datum> datumList, HomeView homeView) {
        this.context = context;
        this.datumList = datumList;
        this.homeView = homeView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemUnnNewsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_unn_news, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Datum datum = datumList.get(position);
        holder.binding.setNewsitem(datum);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.binding.textoverview.setText(Html.fromHtml(datum.getPostContent(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.binding.textoverview.setText(Html.fromHtml(datum.getPostContent()));
        }
        holder.binding.ReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeView.onNewsClick(datum);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datumList.size();
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

        ItemUnnNewsBinding binding;

        public ViewHolder(@NonNull ItemUnnNewsBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    //pagination
    public void addData(List<Datum> data) {
        for (Datum datum : data) {
            datumList.add(datum);
        }
        notifyDataSetChanged();
    }
}
