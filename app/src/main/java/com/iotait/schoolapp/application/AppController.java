package com.iotait.schoolapp.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.facebook.FacebookSdk;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.FirebaseDatabase;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.database.db.MyDatabase;
import com.iotait.schoolapp.helper.FirebaseHelper;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import co.paystack.android.PaystackSdk;


public class AppController extends Application {
    private static AppController mInstance;
    private static FirebaseHelper firebaseHelper;
    private static MyDatabase myDatabase;
    String PremiumType = "";

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize sdkn
        PaystackSdk.initialize(this);
        AudienceNetworkAds.initialize(this);


        FacebookSdk.setClientToken("fb718532098925550");
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        PremiumType = pref.getString("premiumType", null);
        boolean isPremium = pref.getBoolean("isPremium", false);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();


//        if (isPremium) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
//        } else {
//            FirebaseDatabase.getInstance().setPersistenceEnabled(false);
//        }

        mInstance = this;
        firebaseHelper=new FirebaseHelper();

        myDatabase = Room.
                databaseBuilder(getApplicationContext(), MyDatabase.class, getResources().getString(R.string.db_name))
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

    }
    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static synchronized FirebaseHelper getFirebaseHelper() {
        return firebaseHelper;
    }

    public static synchronized MyDatabase getDatabase() {
        return myDatabase;
    }
}
