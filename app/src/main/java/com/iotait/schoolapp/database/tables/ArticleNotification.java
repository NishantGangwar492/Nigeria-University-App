package com.iotait.schoolapp.database.tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Individual message table for holding individual message data
@Entity(tableName = "articlenotification" /*indices = @Index(value = {"sender","receiver"}, unique = true)*/)
public class ArticleNotification {
    @PrimaryKey(autoGenerate = true)
    private int ID;

    @ColumnInfo(name = "articleid")
    private String articleId;

    @ColumnInfo(name = "title")
    private String articleTitle;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }
}
