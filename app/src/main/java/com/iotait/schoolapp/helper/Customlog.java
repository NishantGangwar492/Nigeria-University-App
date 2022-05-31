package com.iotait.schoolapp.helper;

import android.util.Log;

public class Customlog {
    public static void showlogD(String tag, String message) {
        Log.d(tag, message);
    }

    public static void showlogE(String tag, String message) {
        Log.e(tag, message);
    }
}
