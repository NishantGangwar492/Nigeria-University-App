package com.iotait.schoolapp.database.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.iotait.schoolapp.database.dao.MyDao;
import com.iotait.schoolapp.database.tables.ArticleNotification;

// Main database
@Database(entities = {
        ArticleNotification.class
},version = 3,exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    public abstract MyDao myDao();
}
