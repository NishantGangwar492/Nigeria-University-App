package com.iotait.schoolapp.helper;

import android.content.Context;
import android.content.Intent;

import com.iotait.schoolapp.BuildConfig;
import com.iotait.schoolapp.R;


public class ShareHelper {
    public static void shareLink(Context mContext) {
        final String appPackageName = BuildConfig.APPLICATION_ID;
        final String appName = mContext.getString(R.string.app_name);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, appName);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "MyUNN Post Utme App gives you access to UNN Post-utme past questions, latest news and more for FREE. Download MyUNN Post-Utme App from play store via..>https://play.google.com/store/apps/details?id=com.iotait.schoolapp" );
        mContext.startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
}
