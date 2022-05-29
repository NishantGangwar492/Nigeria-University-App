package com.iotait.schoolapp.ui.homepage.ui.notification.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.databinding.ItemArticalsBinding;
import com.iotait.schoolapp.databinding.ItemArticalsNotificationBinding;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.ui.homepage.ui.articals.View.ArticleView;
import com.iotait.schoolapp.ui.homepage.ui.articals.articlemodel.ArticleModel;

import java.util.List;

public class NotificationArticalsAdapter extends RecyclerView.Adapter<NotificationArticalsAdapter.ViewHolder> {

    Context context;
    List<ArticleModel> articleModelList;
    ArticleView articleView;

    public NotificationArticalsAdapter(Context context, List<ArticleModel> articleModelList, ArticleView articleView) {
        this.context = context;
        this.articleModelList = articleModelList;
        this.articleView = articleView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemArticalsNotificationBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_articals_notification, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final ArticleModel articleModel = articleModelList.get(position);
        holder.binding.setArticle(articleModel);
        holder.binding.txtReadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articleView.onArticleReadmoreClick(articleModel.getArticleId(), articleModel.getPeoplesDislike()
                        , articleModel.getPeoplesLike(), articleModel.getWriterImage(), articleModel.getArticle(), articleModel.getArticlePublishedDate()
                        , articleModel.getWriterDesignation(), articleModel.getWriterName(), articleModel.getArticleTitle(),
                        articleModel.getLike(), articleModel.getDislike());
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemArticalsNotificationBinding binding;

        public ViewHolder(@NonNull ItemArticalsNotificationBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
