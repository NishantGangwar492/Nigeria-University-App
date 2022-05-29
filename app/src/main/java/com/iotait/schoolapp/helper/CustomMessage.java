package com.iotait.schoolapp.helper;

import android.app.Activity;

import com.google.android.material.snackbar.Snackbar;

public class CustomMessage {
    Activity activity;
    String message;

    public CustomMessage(Activity activity, String message) {
        this.activity = activity;
        this.message = message;
        showTost(message);
    }

    public void showTost(String message) {
        if (activity!=null){
            Snackbar snack = Snackbar.make(
                    (activity.findViewById(android.R.id.content)),
                    message + "", Snackbar.LENGTH_LONG);
            snack.setActionTextColor(activity.getResources().getColor(android.R.color.holo_red_light));

            snack.show();
        }
    }
}
