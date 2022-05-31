package com.iotait.schoolapp.ui.homepage.ui.articals.articlemodel;

import java.util.List;

public class ArticleModel {

    String Article;
    String ArticleId;
    String ArticlePublishedDate;
    String ArticleTitle;
    int Dislike;
    int Like;
    String WriterDesignation;
    String WriterImage;
    String WriterName;
    List<String> peoplesLike;
    List<String> peoplesDislike;


    public ArticleModel() {
    }

    public ArticleModel(String article, String articleId, String articlePublishedDate, String articleTitle, int dislike, int like, String writerDesignation, String writerImage, String writerName, List<String> peoplesLike, List<String> peoplesDislike) {
        Article = article;
        ArticleId = articleId;
        ArticlePublishedDate = articlePublishedDate;
        ArticleTitle = articleTitle;
        Dislike = dislike;
        Like = like;
        WriterDesignation = writerDesignation;
        WriterImage = writerImage;
        WriterName = writerName;
        this.peoplesLike = peoplesLike;
        this.peoplesDislike = peoplesDislike;
    }

    public String getArticle() {
        return Article;
    }

    public void setArticle(String article) {
        Article = article;
    }

    public String getArticleId() {
        return ArticleId;
    }

    public void setArticleId(String articleId) {
        ArticleId = articleId;
    }

    public String getArticlePublishedDate() {
        return ArticlePublishedDate;
    }

    public void setArticlePublishedDate(String articlePublishedDate) {
        ArticlePublishedDate = articlePublishedDate;
    }

    public String getArticleTitle() {
        return ArticleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        ArticleTitle = articleTitle;
    }

    public int getDislike() {
        return Dislike;
    }

    public void setDislike(int dislike) {
        Dislike = dislike;
    }

    public int getLike() {
        return Like;
    }

    public void setLike(int like) {
        Like = like;
    }

    public String getWriterDesignation() {
        return WriterDesignation;
    }

    public void setWriterDesignation(String writerDesignation) {
        WriterDesignation = writerDesignation;
    }

    public String getWriterImage() {
        return WriterImage;
    }

    public void setWriterImage(String writerImage) {
        WriterImage = writerImage;
    }

    public String getWriterName() {
        return WriterName;
    }

    public void setWriterName(String writerName) {
        WriterName = writerName;
    }

    public List<String> getPeoplesLike() {
        return peoplesLike;
    }

    public void setPeoplesLike(List<String> peoplesLike) {
        this.peoplesLike = peoplesLike;
    }

    public List<String> getPeoplesDislike() {
        return peoplesDislike;
    }

    public void setPeoplesDislike(List<String> peoplesDislike) {
        this.peoplesDislike = peoplesDislike;
    }
}
