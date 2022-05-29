package com.iotait.schoolapp.helper;

import android.app.Activity;
import android.app.ProgressDialog;

// Progress bar helper class
public class ProgressHelper {
    // Declare necessary variable
    private Activity activity;
    private ProgressDialog progress;

    // Constructor of this class
    public ProgressHelper(Activity activity) {
        this.activity = activity;
        this.progress = new ProgressDialog(activity);
    }

    // set progess message
    public void setProgres(String message) {
        progress.setMessage(message);
        progress.setCancelable(false);
    }

    // Showing the progress
    public void showProgress() {
        if (progress != null)
            progress.show();
    }

    // Dismiss the progress
    public void dismissProgress() {
        if (progress != null)
            progress.dismiss();
    }

}
