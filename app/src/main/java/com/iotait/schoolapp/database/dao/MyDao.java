package com.iotait.schoolapp.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.iotait.schoolapp.database.tables.ArticleNotification;

import java.util.List;

// Local database class for room database
@Dao
public interface MyDao {
    //TODO Project
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addNotification(ArticleNotification articleNotification);

    @Query("select * from articlenotification WHERE articleid=:articleid")
    public ArticleNotification getArticleItem(String articleid);

    @Query("SELECT COUNT(ID) FROM articlenotification")
    int getCount();
  
    @Query("DELETE FROM articlenotification WHERE articleid = :articleid")
    public int delteArticle(String articleid);

    @Query("select * from articlenotification")
    public List<ArticleNotification> getArticles();
}
