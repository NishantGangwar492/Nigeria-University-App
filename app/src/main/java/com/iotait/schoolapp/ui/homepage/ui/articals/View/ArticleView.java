package com.iotait.schoolapp.ui.homepage.ui.articals.View;

import com.iotait.schoolapp.ui.homepage.ui.articals.articlemodel.ArticleModel;

import java.util.List;

public interface ArticleView {


    public void onArticleLike(ArticleModel articleId);

    public void onArticleDislike(ArticleModel articleId);


    public void onArticleReadmoreClick(String articleId, List<String> peoplesDislike, List<String> peoplesLike,
                                       String writerImage, String article, String articlePublishedDate, String writerDesignation,
                                       String writerName, String articleTitle, int like, int dislike);


}
