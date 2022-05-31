package com.iotait.schoolapp.ui.homepage.ui.articals.articalsadapter;

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
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.ui.homepage.ui.articals.View.ArticleView;
import com.iotait.schoolapp.ui.homepage.ui.articals.articlemodel.ArticleModel;

import java.util.ArrayList;
import java.util.List;

public class ArticalsAdapter extends RecyclerView.Adapter<ArticalsAdapter.ViewHolder> {

    Context context;
    List<ArticleModel> articleModelList;
    ArticleView articleView;

    public ArticalsAdapter(Context context, List<ArticleModel> articleModelList, ArticleView articleView) {
        this.context = context;
        this.articleModelList = articleModelList;
        this.articleView = articleView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemArticalsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_articals, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final ArticleModel articleModel = articleModelList.get(position);
        holder.binding.articlemain.setText(articleModel.getArticle());
        holder.binding.dislike.setText(String.valueOf(articleModel.getDislike()));
        holder.binding.like.setText(String.valueOf(articleModel.getLike()));
        holder.binding.pubDate.setText(articleModel.getArticlePublishedDate());
        holder.binding.title.setText(articleModel.getArticleTitle());
        holder.binding.writername.setText(articleModel.getWriterName());
        holder.binding.writerDesignation.setText(articleModel.getWriterDesignation());
        UIHelper.setArticleImage(context, holder.binding.articleprofile, articleModel.getWriterImage());

        for (String uid : articleModel.getPeoplesLike()) {
            if (uid.equals(AppController.getFirebaseHelper().getFirebaseAuth().getUid())) {
                holder.binding.imagelike.setImageResource(R.drawable.likeclick);
                break;
            } else {
                holder.binding.imagelike.setImageResource(R.drawable.likewhite);
            }
        }
        if (articleModel.getPeoplesLike().size() == 0) {
            holder.binding.imagelike.setImageResource(R.drawable.likewhite);
        }
        for (String uid : articleModel.getPeoplesDislike()) {
            if (uid.equals(AppController.getFirebaseHelper().getFirebaseAuth().getUid())) {
                holder.binding.imagedislike.setImageResource(R.drawable.dislikeclick);
                break;
            } else {
                holder.binding.imagedislike.setImageResource(R.drawable.dislikewhite);
            }
        }

        if (articleModel.getPeoplesDislike().size() == 0) {
            holder.binding.imagedislike.setImageResource(R.drawable.dislikewhite);
        }

        holder.binding.imagelike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articleView.onArticleLike(articleModel);
            }
        });

        holder.binding.imagedislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articleView.onArticleDislike(articleModel);
            }
        });

        holder.binding.readmore.setOnClickListener(new View.OnClickListener() {
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

    public void filterList(ArrayList<ArticleModel> filterdNames) {
        this.articleModelList = filterdNames;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemArticalsBinding binding;
        public ViewHolder(@NonNull ItemArticalsBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
