package com.iotait.schoolapp.ui.homepage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.common.Common;
import com.iotait.schoolapp.databinding.ActivityHomeNavigationBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.Customlog;
import com.iotait.schoolapp.helper.NetworkHelper;
import com.iotait.schoolapp.helper.ProgressHelper;
import com.iotait.schoolapp.session.SessionManager;
import com.iotait.schoolapp.ui.homepage.ui.chat.constant.Constant;
import com.iotait.schoolapp.ui.homepage.ui.navigation.models.UserItem;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.iotait.schoolapp.ui.homepage.ui.navigation.Navigation.CAMERA_REQUEST_CODE;
import static com.iotait.schoolapp.ui.homepage.ui.navigation.Navigation.GALLERY_REQUEST_CODE;

public class HomeNavigationActivity extends AppCompatActivity {

    public ActivityHomeNavigationBinding homeNavigationBinding;
    private AppBarConfiguration mAppBarConfiguration;
    FirebaseFirestore db;
    private AdRequest adRequest;
    private String premium_type = "";
    public static HomeNavigationActivity instance;
    SharedPreferences pref;
    private ProgressHelper progressHelper;
    private AdView adView;
    private boolean isFirstLaunch=false;
    private SessionManager sessionManager;

    public static HomeNavigationActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AdSettings.addTestDevice("578228e5-2da7-401e-9d01-57aefac432b6");
        List<String> testDeviceIds = Arrays.asList("3161B82912D3D3F2DC14A2F883F4C585");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);

        instance = this;
        progressHelper = new ProgressHelper(this);
        progressHelper.setProgres("Loading...");
        homeNavigationBinding = DataBindingUtil.setContentView(this, R.layout.activity_home_navigation);
        db = FirebaseFirestore.getInstance();
        addUserFullExamCount();
        getPersonData();
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.unnNews)
                .build();
        adRequest = new AdRequest.Builder().build();
        sessionManager = new SessionManager(this);
        addUserVersion();

        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "your.package",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void getCurrentState() {
        if (NetworkHelper.hasNetworAccess(this)) {
            progressHelper.showProgress();
            AppController.getFirebaseHelper().getAdState().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressHelper.dismissProgress();
                    if (dataSnapshot.exists()) {
                        Common.baneerType = dataSnapshot.child("banner").getValue(String.class);
                        Common.interstitalType = dataSnapshot.child("interstitial").getValue(String.class);
                        Common.reawerType = dataSnapshot.child("reawar").getValue(String.class);
                        isFirstLaunch=true;
                        if (Common.baneerType.toLowerCase().equals("google"))
                            loadGoogleBannerAd();
                        else if (Common.baneerType.toLowerCase().equals("facebook"))
                            loadFacebookBanner();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressHelper.dismissProgress();
                    new CustomMessage(HomeNavigationActivity.this, databaseError.getMessage());
                }
            });
        } else {
            new CustomMessage(HomeNavigationActivity.this, "No internet connection");
        }
    }

    private void loadFacebookBanner() {
        adView = new AdView(HomeNavigationActivity.this, getResources().getString(R.string.fb_banner_id), AdSize.BANNER_HEIGHT_50);
        adView.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Customlog.showlogD("AD_ERROR", adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Ad loaded callback
                homeNavigationBinding.admobBanner.bannerContainer.addView(adView);
                homeNavigationBinding.admobBanner.adlayout.setVisibility(View.GONE);
                homeNavigationBinding.admobBanner.adView.setVisibility(View.GONE);
                homeNavigationBinding.admobBanner.bannerContainer.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }
        });
        adView.loadAd();
    }

    private void loadGoogleBannerAd() {

        homeNavigationBinding.admobBanner.adView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                homeNavigationBinding.admobBanner.adlayout.setVisibility(View.VISIBLE);
                homeNavigationBinding.admobBanner.adView.setVisibility(View.VISIBLE);
                homeNavigationBinding.admobBanner.bannerContainer.setVisibility(View.GONE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Customlog.showlogD("AD_ERROR", "" + errorCode);
            }

            @Override
            public void onAdOpened() {

            }

            @Override
            public void onAdClicked() {

            }

            @Override
            public void onAdLeftApplication() {

            }

            @Override
            public void onAdClosed() {

            }
        });
        homeNavigationBinding.admobBanner.adView.loadAd(adRequest);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
        updateStatus("Offline");
    }

    @Override
    protected void onResume() {
        super.onResume();
        pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        premium_type = pref.getString("premiumType", "");
        Customlog.showlogD("CURRENT_STATE", premium_type);
        homeNavigationBinding.admobBanner.adlayout.setVisibility(View.GONE);
        if (TextUtils.equals(premium_type, "p0")) {
                loadGoogleBannerAd();
        }
    }

    private void addUserToCloudFirestore(String uId, String name, String profilrUrl, String status) {
        Map<String, Object> member = new HashMap<>();
        member.put("user_id", uId);
        member.put("name", name);
        member.put("profile", profilrUrl);
        member.put("status", status);
        member.put("notificationStatus", "Offline");
        member.put("premiumType", pref.getString("premiumType", ""));

        db.collection("ChatUser").document(uId)
                .set(member)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                    }
                });

        updateStatus("Online");
    }

    private void updateStatus(String status) {
        Map<String, Object> statusUpdate = new HashMap<>();
        statusUpdate.put("status", status);
        statusUpdate.put("premiumType", pref.getString("premiumType", ""));
        if (AppController.getFirebaseHelper().getFirebaseAuth().getCurrentUser() != null) {
            db.collection("ChatUser").document(AppController.getFirebaseHelper().getFirebaseAuth().getUid())
                    .update("status", status)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    });
        }
    }

    private void getPersonData() {
        AppController.getFirebaseHelper().getUsers().child(AppController.getFirebaseHelper().getFirebaseAuth().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String countrycode = "";
                    String date = "";
                    String email = "";
                    boolean isblock = false;
                    String password = "";
                    String personname = "";
                    String phone = "";
                    String username = "";
                    String photo = "";
                    String uid = "";
                    int fullExamCount=0;
                    if (dataSnapshot.child("countrycode").getValue() != null)
                        countrycode = dataSnapshot.child("countrycode").getValue(String.class);
                    if (dataSnapshot.child("date").getValue() != null)
                        date = dataSnapshot.child("date").getValue(String.class);
                    if (dataSnapshot.child("email").getValue() != null)
                        email = dataSnapshot.child("email").getValue(String.class);
                    if (dataSnapshot.child("isblock").getValue() != null)
                        isblock = dataSnapshot.child("isblock").getValue(Boolean.class);
                    if (dataSnapshot.child("password").getValue() != null)
                        password = dataSnapshot.child("password").getValue(String.class);
                    if (dataSnapshot.child("personname").getValue() != null)
                        personname = dataSnapshot.child("personname").getValue(String.class);
                    if (dataSnapshot.child("phone").getValue() != null)
                        phone = dataSnapshot.child("phone").getValue(String.class);
                    if (dataSnapshot.child("username").getValue() != null)
                        username = dataSnapshot.child("username").getValue(String.class);
                    if (dataSnapshot.child("photo").getValue() != null)
                        photo = dataSnapshot.child("photo").getValue(String.class);
                    if (dataSnapshot.child("fullexamcount").getValue() != null)
                        fullExamCount = dataSnapshot.child("fullexamcount").getValue(Integer.class);
                    uid = dataSnapshot.getKey();

                    sessionManager.setFullExamCount(fullExamCount);
                    addUserToCloudFirestore(AppController.getFirebaseHelper().getFirebaseAuth().getUid(), personname, photo, "Online");
                    Constant.currentUserName = personname;
                    Constant.currentUserProfile = photo;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addUserFullExamCount(){
        AppController.getFirebaseHelper().getUsers().child(AppController.getFirebaseHelper().getFirebaseAuth().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild("fullexamcount")){
                    HashMap<String, Object> user = new HashMap();
                    user.put("fullexamcount", 0);
                    AppController.getFirebaseHelper().getUsers().child(AppController.getFirebaseHelper().getFirebaseAuth().getUid()).updateChildren(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addUserVersion(){
        AppController.getFirebaseHelper().getUsers().child(AppController.getFirebaseHelper().getFirebaseAuth().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Object> user = new HashMap();
                user.put("appversion", getCurrentAppVersion());
                AppController.getFirebaseHelper().getUsers().child(AppController.getFirebaseHelper().getFirebaseAuth().getUid()).updateChildren(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String getCurrentAppVersion(){
        String version="";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.e("TAG", "getCurrentAppVersion: " + version );
        return version;
    }

}
