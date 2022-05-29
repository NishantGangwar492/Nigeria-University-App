package com.iotait.schoolapp.session;

import android.content.Context;
import android.content.SharedPreferences;

import static com.iotait.schoolapp.session.SessionHelper.CURRENT_POINTS;
import static com.iotait.schoolapp.session.SessionHelper.DAILY_REWARD;
import static com.iotait.schoolapp.session.SessionHelper.DATE;
import static com.iotait.schoolapp.session.SessionHelper.FULL_EXAM_COUNT;
import static com.iotait.schoolapp.session.SessionHelper.PREF_NAME;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void setCurrentPoints(int points){
        editor.putInt(CURRENT_POINTS, points);
        editor.apply();
    }

    public void setDaylyReward(int reward){
        editor.putInt(DAILY_REWARD, reward);
        editor.apply();

    }
    public void setCurrentDate(String c_date){
        editor.putString(DATE, c_date);
        editor.apply();
    }

    public int getTotalPoints(){
        return sharedPreferences.getInt(CURRENT_POINTS, 0);
    }

    public int getDaylyReward(){
        return sharedPreferences.getInt(DAILY_REWARD, 0);
    }

    public String getCurrentDate(){
        return sharedPreferences.getString(DATE, "");
    }

    public void clearData(){
        editor.clear();
        editor.commit();
    }

    public void setFullExamCount(int num){
        editor.putInt(FULL_EXAM_COUNT, num);
        editor.apply();
    }

    public int getFullExamCount(){
        return sharedPreferences.getInt(FULL_EXAM_COUNT, 0);
    }

}
