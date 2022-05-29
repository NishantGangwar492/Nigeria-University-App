package com.iotait.schoolapp.database.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.iotait.schoolapp.database.tables.ArticleNotification;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class MyDao_Impl implements MyDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ArticleNotification> __insertionAdapterOfArticleNotification;

  private final SharedSQLiteStatement __preparedStmtOfDelteArticle;

  public MyDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfArticleNotification = new EntityInsertionAdapter<ArticleNotification>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `articlenotification` (`ID`,`articleid`,`title`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ArticleNotification value) {
        stmt.bindLong(1, value.getID());
        if (value.getArticleId() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getArticleId());
        }
        if (value.getArticleTitle() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getArticleTitle());
        }
      }
    };
    this.__preparedStmtOfDelteArticle = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM articlenotification WHERE articleid = ?";
        return _query;
      }
    };
  }

  @Override
  public void addNotification(final ArticleNotification articleNotification) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfArticleNotification.insert(articleNotification);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int delteArticle(final String articleid) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDelteArticle.acquire();
    int _argIndex = 1;
    if (articleid == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, articleid);
    }
    __db.beginTransaction();
    try {
      final int _result = _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
      __preparedStmtOfDelteArticle.release(_stmt);
    }
  }

  @Override
  public ArticleNotification getArticleItem(final String articleid) {
    final String _sql = "select * from articlenotification WHERE articleid=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (articleid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, articleid);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfID = CursorUtil.getColumnIndexOrThrow(_cursor, "ID");
      final int _cursorIndexOfArticleId = CursorUtil.getColumnIndexOrThrow(_cursor, "articleid");
      final int _cursorIndexOfArticleTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final ArticleNotification _result;
      if(_cursor.moveToFirst()) {
        _result = new ArticleNotification();
        final int _tmpID;
        _tmpID = _cursor.getInt(_cursorIndexOfID);
        _result.setID(_tmpID);
        final String _tmpArticleId;
        _tmpArticleId = _cursor.getString(_cursorIndexOfArticleId);
        _result.setArticleId(_tmpArticleId);
        final String _tmpArticleTitle;
        _tmpArticleTitle = _cursor.getString(_cursorIndexOfArticleTitle);
        _result.setArticleTitle(_tmpArticleTitle);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getCount() {
    final String _sql = "SELECT COUNT(ID) FROM articlenotification";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ArticleNotification> getArticles() {
    final String _sql = "select * from articlenotification";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfID = CursorUtil.getColumnIndexOrThrow(_cursor, "ID");
      final int _cursorIndexOfArticleId = CursorUtil.getColumnIndexOrThrow(_cursor, "articleid");
      final int _cursorIndexOfArticleTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final List<ArticleNotification> _result = new ArrayList<ArticleNotification>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ArticleNotification _item;
        _item = new ArticleNotification();
        final int _tmpID;
        _tmpID = _cursor.getInt(_cursorIndexOfID);
        _item.setID(_tmpID);
        final String _tmpArticleId;
        _tmpArticleId = _cursor.getString(_cursorIndexOfArticleId);
        _item.setArticleId(_tmpArticleId);
        final String _tmpArticleTitle;
        _tmpArticleTitle = _cursor.getString(_cursorIndexOfArticleTitle);
        _item.setArticleTitle(_tmpArticleTitle);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
