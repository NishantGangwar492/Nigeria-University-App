package com.iotait.schoolapp.ui.homepage.ui.home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.common.Common;
import com.iotait.schoolapp.database.tables.ArticleNotification;
import com.iotait.schoolapp.databinding.FragmentHomeBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.Customlog;
import com.iotait.schoolapp.helper.FragmentHelper;
import com.iotait.schoolapp.helper.NetworkHelper;
import com.iotait.schoolapp.helper.ShareHelper;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.notification.Token;
import com.iotait.schoolapp.session.SessionManager;
import com.iotait.schoolapp.ui.homepage.ui.articals.articlemodel.ArticleModel;
import com.iotait.schoolapp.ui.homepage.ui.chat.ChatListActivty;
import com.iotait.schoolapp.ui.homepage.ui.detailsnews.DetailsNewsWebView;
import com.iotait.schoolapp.ui.homepage.ui.home.adapter.LatestNewsAdapter;
import com.iotait.schoolapp.ui.homepage.ui.home.adapter.OverViewAdapter;
import com.iotait.schoolapp.ui.homepage.ui.home.adapter.SliderAdapter;
import com.iotait.schoolapp.ui.homepage.ui.home.model.Datum;
import com.iotait.schoolapp.ui.homepage.ui.home.model.LatestNewsResponse;
import com.iotait.schoolapp.ui.homepage.ui.home.model.SliderItem;
import com.iotait.schoolapp.ui.homepage.ui.home.view.HomeView;
import com.iotait.schoolapp.ui.homepage.ui.home.view.OverViewHomeView;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment implements BaseSliderView.OnSliderClickListener, View.OnClickListener, HomeView,
        OverViewHomeView {


    FragmentHomeBinding fragmentHomeBinding;
    Bundle bundle;

    BottomSheetDialog dialog;
    private String premium_type="";

    Dialog phoneInputDialog;

    String[] name = {"Study Questions", "UNN News", "Articles", "Take Exam", "School Anthem", "Syllabus", "Weekly Contest"};

    int[] images = {R.drawable.questioncolor, R.drawable.unnnewscolor, R.drawable.articlecolor, R.drawable.takeexamcolor,
            R.drawable.schoolanthemcolor, R.drawable.syllabuscolor, R.drawable.weeklycontest};
    LatestNewsAdapter adapter;
    private List<Datum> datumList;

    OverViewAdapter overViewAdapter;
    private HomeViewModel homeViewModel;
    private List<Datum> tempList;


    private int pagenumber=1;
    private int pageLimit=20;
    private int totalPage=0;
    private List<SliderItem> sliderItems;
    private SliderAdapter sliderAdapter;

    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;
    private ProgressDialog progressDialog;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private String TAG="AD_NETWORK";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        checkPhoneNumber();
        bundle = new Bundle();

        View root = fragmentHomeBinding.getRoot();

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                getActivity().finishAffinity();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);


        fragmentHomeBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.equals(premium_type, "p2")) {
                    startActivity(new Intent(getContext(), ChatListActivty.class));
                } else if (TextUtils.equals(premium_type, "p1")) {
                    new CustomMessage(getActivity(), "Chat is't available, please update your package");
                } else if (TextUtils.equals(premium_type, "p0")) {
                    new CustomMessage(getActivity(), "Please update to premium package to enable chat");
                    FragmentHelper.changeFragmet(fragmentHomeBinding.getRoot(), R.id.action_nav_home_to_premiumProcessFragment, bundle);
                }
            }
        });


        fragmentHomeBinding.menuBtn.setOnClickListener(this);

        return root;
    }

    private void checkPhoneNumber() {
        @SuppressLint("HardwareIds")
        String android_id = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if (NetworkHelper.hasNetworAccess(getContext())) {

            AppController.getFirebaseHelper().getUsers().child(AppController.getFirebaseHelper()
                    .getFirebaseAuth().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        String phone = "";
                        boolean isPremium = false;
                        String deviceId = "";


                        if (dataSnapshot.child("phone").getValue(String.class) != null) {
                            phone = dataSnapshot.child("phone").getValue(String.class);
//                            ShowPhoneNumberInputDialog();
                        }
                        if ((Boolean) dataSnapshot.child("isPremimum").getValue() != null) {
                            isPremium = (Boolean) dataSnapshot.child("isPremimum").getValue();
                        }
                        if (dataSnapshot.child("device_id").getValue() != null) {
                            deviceId = dataSnapshot.child("device_id").getValue().toString();
                        }
                        if (isPremium) {
                            if (deviceId.equals("")) {
                                HashMap<String, Object> device_id = new HashMap<>();
                                device_id.put("device_id", android_id);
                                AppController.getFirebaseHelper().getUsers().child(AppController.getFirebaseHelper()
                                        .getFirebaseAuth().getCurrentUser().getUid()).updateChildren(device_id).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                });

                            }
                        }


                        if (phone.equals("")) {
                            ShowPhoneNumberInputDialog();
                        } else {
                            if (phoneInputDialog != null) {
                                phoneInputDialog.dismiss();
                            }
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void ShowPhoneNumberInputDialog() {
        if (phoneInputDialog != null) {
            phoneInputDialog.dismiss();
        }

        if (getActivity()!=null){
            phoneInputDialog = new Dialog(getActivity());
            phoneInputDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            phoneInputDialog.setContentView(R.layout.phone_number_request_dialog);
            phoneInputDialog.setCanceledOnTouchOutside(false);
            phoneInputDialog.setCancelable(false);

            EditText phone = phoneInputDialog.findViewById(R.id.edit_phonedialog);

            CountryCodePicker ccp = phoneInputDialog.findViewById(R.id.ccpdialog);

            Button upload = phoneInputDialog.findViewById(R.id.upload);

            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(phone.getText().toString())) {

                        HashMap<String, Object> user = new HashMap<>();

                        user.put("phone", "+" + ccp.getSelectedCountryCode() + phone.getText().toString());
                        user.put("premium_type", "p0");
                        user.put("isPremimum", false);
                        user.put("countrycode", "+" + ccp.getSelectedCountryCode());
                        user.put("point", "0");
                        user.put("date", Common.c_date);

                        AppController.getFirebaseHelper().getUsers().child(AppController.getFirebaseHelper()
                                .getFirebaseAuth().getCurrentUser().getUid()).updateChildren(user).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if (phoneInputDialog != null) {
                                    phoneInputDialog.dismiss();
                                }

                                new CustomMessage(getActivity(), "Successfully Updated Phone Number. You are ready to go. Good Luck!");
                            }
                        });
                    } else {
                        phone.setError("Please Input phone number");
                    }
                }
            });
            phoneInputDialog.show();
        }
    }

    private SessionManager sessionManager;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        tempList = new ArrayList<>();

        sessionManager = new SessionManager(getContext());


        pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
        editor = pref.edit();

        datumList=new ArrayList<>();
        adapter = new LatestNewsAdapter(getContext(), datumList, this);
        adapter.setHasStableIds(true);
        fragmentHomeBinding.latestnewsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentHomeBinding.latestnewsRecycler.setAdapter(adapter);

        homeViewModel.setLatestNewsResponseMutableLiveData(getContext(),pagenumber,pageLimit,this);
        homeViewModel.getLatestNewsResponseMutableLiveData().observe(getViewLifecycleOwner(), new Observer<LatestNewsResponse>() {
            @Override
            public void onChanged(LatestNewsResponse latestNewsResponse) {
                pagenumber=latestNewsResponse.getNextPage();
                totalPage=latestNewsResponse.getTotalPage();
                tempList.clear();
                tempList.addAll(latestNewsResponse.getData());
                adapter.addData(tempList);

            }
        });

        fragmentHomeBinding.latestnewsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItem = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                boolean endHasbeenReched = lastVisibleItem + 1 >= totalItem;
                if (totalItem > 0 && endHasbeenReched) {
                    if (pagenumber!=-1 && pagenumber <= totalPage) {
                        homeViewModel.setLatestNewsResponseMutableLiveData(getContext(),pagenumber, pageLimit, HomeFragment.this);
                    } else {
                        new CustomMessage(getActivity(),"No item available");
                    }
                }
            }
        });


        overViewAdapter = new OverViewAdapter(getContext(), name, images, fragmentHomeBinding.getRoot(), HomeFragment.this);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        fragmentHomeBinding.recyclerOverView.setLayoutManager(layoutManager);
        fragmentHomeBinding.recyclerOverView.setNestedScrollingEnabled(false);
        fragmentHomeBinding.recyclerOverView.setHasFixedSize(true);
        fragmentHomeBinding.recyclerOverView.setAdapter(overViewAdapter);

        sliderItems = new ArrayList<>();
        sliderAdapter = new SliderAdapter(getContext(), sliderItems);
        fragmentHomeBinding.sliderMainPage.setSliderAdapter(sliderAdapter);

        fragmentHomeBinding.sliderMainPage.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        fragmentHomeBinding.sliderMainPage.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        fragmentHomeBinding.sliderMainPage.setAutoCycleDirection(fragmentHomeBinding.sliderMainPage.AUTO_CYCLE_DIRECTION_RIGHT);
        fragmentHomeBinding.sliderMainPage.setIndicatorSelectedColor(Color.WHITE);
        fragmentHomeBinding.sliderMainPage.setIndicatorUnselectedColor(Color.GRAY);
        fragmentHomeBinding.sliderMainPage.setScrollTimeInSec(4);
        fragmentHomeBinding.sliderMainPage.startAutoCycle();
        getSliderData();
        fragmentHomeBinding.share.setOnClickListener(this::onClick);
        fragmentHomeBinding.ringbell.setOnClickListener(this::onClick);

        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId(getString(R.string.interstitialad_id));
        adRequest = new AdRequest.Builder().build();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Ad loading, please wait.");
        progressDialog.setCancelable(false);
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        Bundle bundle = new Bundle();
        switch (id) {
            case R.id.menu_btn:
                FragmentHelper.changeFragmet(fragmentHomeBinding.getRoot(), R.id.action_nav_home_to_navigation, bundle);
                break;
            case R.id.share:
                ShareHelper.shareLink(getContext());
                break;
                case R.id.ringbell:
                    if (TextUtils.equals(premium_type,"p0")){
                        adHandler(R.id.action_nav_home_to_notificationFragment, bundle);
                    }
                    else {
                        changeFragment(R.id.action_nav_home_to_notificationFragment, bundle);
                    }
                break;
        }
    }
    private com.facebook.ads.InterstitialAd interstitialAdFacebook;
    private void adHandler(int action, Bundle bundle){

        mInterstitialAd.loadAd(adRequest);
        progressDialog.show();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                mInterstitialAd.show();
                progressDialog.dismiss();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                changeFragment(action,bundle);
                progressDialog.dismiss();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                changeFragment(action,bundle);
            }
        });

//        if (Common.interstitalType.toLowerCase().equals("google")){
//            mInterstitialAd.loadAd(adRequest);
//            progressDialog.show();
//            mInterstitialAd.setAdListener(new AdListener() {
//                @Override
//                public void onAdLoaded() {
//                    // Code to be executed when an ad finishes loading.
//                    mInterstitialAd.show();
//                    progressDialog.dismiss();
//                }
//
//                @Override
//                public void onAdFailedToLoad(int errorCode) {
//                    // Code to be executed when an ad request fails.
//                    changeFragment(action,bundle);
//                    progressDialog.dismiss();
//                }
//
//                @Override
//                public void onAdOpened() {
//                    // Code to be executed when the ad is displayed.
//                }
//
//                @Override
//                public void onAdClicked() {
//                    // Code to be executed when the user clicks on an ad.
//                }
//
//                @Override
//                public void onAdLeftApplication() {
//                    // Code to be executed when the user has left the app.
//                }
//
//                @Override
//                public void onAdClosed() {
//                    // Code to be executed when the interstitial ad is closed.
//                    changeFragment(action,bundle);
//                }
//            });
//        }
//        else if (Common.interstitalType.toLowerCase().equals("facebook")){
//            interstitialAdFacebook = new com.facebook.ads.InterstitialAd(getActivity(), getResources().getString(R.string.fb_interstitialad_id));
//            progressDialog.show();
//            interstitialAdFacebook.setAdListener(new InterstitialAdListener() {
//                @Override
//                public void onInterstitialDisplayed(Ad ad) {
//                    // Interstitial ad displayed callback
//                }
//
//                @Override
//                public void onInterstitialDismissed(Ad ad) {
//                    // Interstitial dismissed callback
//                    Log.e(TAG, "Interstitial ad dismissed.");
//                    changeFragment(action,bundle);
//                }
//
//                @Override
//                public void onError(Ad ad, AdError adError) {
//                    // Ad error callback
//                    Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
//                    progressDialog.dismiss();
//                    changeFragment(action,bundle);
//                }
//
//                @Override
//                public void onAdLoaded(Ad ad) {
//                    // Interstitial ad is loaded and ready to be displayed
//                    Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
//                    // Show the ad
//                    interstitialAdFacebook.show();
//                    progressDialog.dismiss();
//                }
//
//                @Override
//                public void onAdClicked(Ad ad) {
//                    // Ad clicked callback
//                    Log.d(TAG, "Interstitial ad clicked!");
//                }
//
//                @Override
//                public void onLoggingImpression(Ad ad) {
//                    // Ad impression logged callback
//                    Log.d(TAG, "Interstitial ad impression logged!");
//                }
//            });
//
//            // For auto play video ads, it's recommended to load the ad
//            // at least 30 seconds before it is shown
//            interstitialAdFacebook.loadAd();
//        }

    }

    private void changeFragment(int action, Bundle bundle) {
        FragmentHelper.changeFragmet(fragmentHomeBinding.getRoot(), action, bundle);
    }

    public void onNewsLoading() {
        fragmentHomeBinding.spinKit.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNewsLoadFinish() {
        fragmentHomeBinding.spinKit.setVisibility(View.GONE);
    }

    @Override
    public void onInternetError() {
        //   new CustomMessage(getActivity(), getContext().getResources().getString(R.string.internet_error));
    }

    @Override
    public void onDataLoadError() {
        new CustomMessage(getActivity(), getContext().getResources().getString(R.string.server_error_messae));
    }

    @Override
    public void onNewsClick(Datum datum) {
        Bundle bundle = new Bundle();
        bundle.putString("link", datum.getGuid());
        UIHelper.changeActivty(getContext(), DetailsNewsWebView.class, bundle);
    }
    private void getSliderData() {
        if (NetworkHelper.hasNetworAccess(getContext())) {
            AppController.getFirebaseHelper().getSliders().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    sliderItems.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String key = dataSnapshot1.getKey();
                            String slider_image = dataSnapshot1.child("image").getValue(String.class);
                            SliderItem sliderItem = new SliderItem();
                            sliderItem.setSlider_id(key);
                            sliderItem.setSliderimage(slider_image);
                            sliderItems.add(sliderItem);
                        }
                        Collections.reverse(sliderItems);
                        sliderAdapter.notifyDataSetChanged();
                    } else {
                        sliderItems.clear();
                        sliderAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    sliderItems.clear();
                    sliderAdapter.notifyDataSetChanged();
                    Customlog.showlogD(TAG, "onCancelled: deleted file");
                }
            });
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    new CustomMessage(getActivity(),"No Internet connection");
//                    String PremiumType = "";
//                    String expireDate = "";
                    SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    // PremiumType = pref.getString("premiumType", null);
                    boolean isPremium = pref.getBoolean("isPremium", false);
                    // expireDate = pref.getString("expire_date", null);

                    if (isPremium) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    } else {
                        ShowPremiumDialog();
                    }

                }
            }, 100);
        }
    }

    private void ShowPremiumDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog_premium, null);
        dialog = new BottomSheetDialog(getContext(), R.style.SheetDialog);
        dialog.setContentView(dialogView);
        Button start = dialogView.findViewById(R.id.start);

        dialog.show();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                FragmentHelper.changeFragmet(fragmentHomeBinding.getRoot(), R.id.action_nav_home_to_premiumProcessFragment, bundle);


            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppController.getFirebaseHelper().getFirebaseAuth().getCurrentUser()!=null){
            getCurrentToken();
        }
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        premium_type = pref.getString("premiumType", "");
        setDateAndRewardCount();
        getArticle();
        getUserDetails();

    }

    private void setDateAndRewardCount() {

        String cd_date = new SimpleDateFormat("E, dd MMM yyyy", Locale.getDefault()).format(new Date());
        AppController.getFirebaseHelper().getUsers().child(AppController.getFirebaseHelper()
                .getFirebaseAuth().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String date = "";
                    String rewardCount = "0";
                    if (dataSnapshot.child("pointsDate").getValue() != null)
                        date = dataSnapshot.child("pointsDate").getValue(String.class);
                    if (dataSnapshot.child("rewardCount").getValue() != null)
                        rewardCount = dataSnapshot.child("rewardCount").getValue(String.class);

                    if (!TextUtils.equals(cd_date, date)) {
                        HashMap<String, Object> point = new HashMap<>();
                        point.put("pointsDate", cd_date);
                        point.put("rewardCount", "0");
                        AppController.getFirebaseHelper().getUsers().child(AppController.getFirebaseHelper().getFirebaseAuth()
                                .getCurrentUser().getUid()).updateChildren(point).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                new CustomMessage(getActivity(), "" + e.getMessage());
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                sessionManager.setCurrentDate(cd_date);
                                sessionManager.setDaylyReward(0);
                            }
                        });

                    } else {
                        sessionManager.setCurrentDate(date);
                        sessionManager.setDaylyReward(Integer.parseInt(rewardCount));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private List<ArticleModel> articleModels = new ArrayList<>();

    private void getArticle() {
        if (NetworkHelper.hasNetworAccess(getContext())) {
            AppController.getFirebaseHelper().getArticles().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        articleModels.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            String Article = "";
                            String ArticleId = "";
                            String ArticlePublishedDate = "";
                            String ArticleTitle = "";
                            int Dislike = 0;
                            int Like = 0;
                            String WriterDesignation = "";
                            String WriterImage = "";
                            String WriterName = "";
                            List<String> peoplesLike = new ArrayList<>();
                            List<String> peoplesDislike = new ArrayList<>();
                            if (snapshot.child("Article").getValue() != null)
                                Article = snapshot.child("Article").getValue(String.class);
                            if (snapshot.child("ArticleId").getValue() != null)
                                ArticleId = snapshot.child("ArticleId").getValue(String.class);
                            if (snapshot.child("ArticlePublishedDate").getValue() != null)
                                ArticlePublishedDate = snapshot.child("ArticlePublishedDate").getValue(String.class);
                            if (snapshot.child("ArticleTitle").getValue() != null)
                                ArticleTitle = snapshot.child("ArticleTitle").getValue(String.class);
                            if (snapshot.child("Like").getValue() != null)
                                Like = snapshot.child("Like").getValue(Integer.class);
                            if (snapshot.child("Dislike").getValue() != null)
                                Dislike = snapshot.child("Dislike").getValue(Integer.class);
                            if (snapshot.child("WriterDesignation").getValue() != null)
                                WriterDesignation = snapshot.child("WriterDesignation").getValue(String.class);

                            if (snapshot.child("WriterImage").getValue() != null)
                                WriterImage = snapshot.child("WriterImage").getValue(String.class);
                            if (snapshot.child("WriterName").getValue() != null)
                                WriterName = snapshot.child("WriterName").getValue(String.class);


                            if (snapshot.child("peoplesLike").getChildrenCount() > 0) {
                                for (DataSnapshot people : snapshot.child("peoplesLike").getChildren()) {
                                    String personid = people.child("personid").getValue(String.class);
                                    peoplesLike.add(personid);
                                }
                            }

                            if (snapshot.child("peoplesDislike").getChildrenCount() > 0) {
                                for (DataSnapshot people : snapshot.child("peoplesDislike").getChildren()) {
                                    String personid = people.child("personid").getValue(String.class);
                                    peoplesDislike.add(personid);
                                }
                            }


                            ArticleModel articleModel = new ArticleModel();
                            articleModel.setArticle(Article);
                            articleModel.setArticleId(ArticleId);
                            articleModel.setArticleTitle(ArticleTitle);
                            articleModel.setArticlePublishedDate(ArticlePublishedDate);
                            articleModel.setLike(Like);
                            articleModel.setDislike(Dislike);
                            articleModel.setWriterDesignation(WriterDesignation);
                            articleModel.setWriterName(WriterName);
                            articleModel.setWriterImage(WriterImage);
                            articleModel.setPeoplesLike(peoplesLike);
                            articleModel.setPeoplesDislike(peoplesDislike);

                            for (ArticleNotification articleNotification:AppController.getDatabase().myDao().getArticles()){
                                if (articleNotification.getArticleId().equals(articleModel.getArticleId())){
                                    articleModels.add(articleModel);
                                }
                            }
                        }
                        fragmentHomeBinding.badge.setNumber(articleModels.size());
                        if (articleModels.size()==0){
                            for (ArticleNotification articleNotification:AppController.getDatabase().myDao().getArticles()){
                                AppController.getDatabase().myDao().delteArticle(articleNotification.getArticleId());
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        else {
            new CustomMessage(getActivity(), "No internet connection");
        }

    }

    private void getUserDetails() {

        if (NetworkHelper.hasNetworAccess(getContext())) {

            AppController.getFirebaseHelper().getUsers().child(AppController.getFirebaseHelper()
                    .getFirebaseAuth().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        if (dataSnapshot.child("premium_type").getValue(String.class) != null && (Boolean) dataSnapshot.child("isPremimum").getValue() &&
                                dataSnapshot.child("expire_date").getValue(String.class) != null) {
                            String premiumType = dataSnapshot.child("premium_type").getValue(String.class);
                            String Expire_date = dataSnapshot.child("expire_date").getValue(String.class);
                            boolean ispremium = (Boolean) dataSnapshot.child("isPremimum").getValue();
                            Customlog.showlogD("PACKAGE_TYPE", "Home " + AppController.getFirebaseHelper()
                                    .getFirebaseAuth().getCurrentUser().getUid());


                            editor.putString("premiumType", premiumType);
                            editor.putBoolean("isPremium", ispremium);
                            editor.putString("expire_date", Expire_date);
                            editor.commit();


                        } else {
                            editor.putString("premiumType", "p0");
                            editor.putBoolean("isPremium", false);
                            editor.putString("expire_date", null);
                            editor.commit();
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }



    //getting user token for active or not
    public void getCurrentToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Customlog.showlogD("REG_TOKEN", task.getException().toString());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Customlog.showlogD("REG_TOKEN", token);
                        updateToken(token);
                    }
                });


    }

    //for token update in firebase database
    private void updateToken(String refreshtoken) {
        Token token = new Token(refreshtoken);
        AppController.getFirebaseHelper().getTokens().child("Users").child(AppController.getFirebaseHelper().getFirebaseAuth().getUid()).setValue(token);
    }

    @Override
    public void onQuestionClick() {
        if (TextUtils.equals(premium_type,"p0")){
            adHandler(R.id.action_nav_home_to_questionFragment, bundle);
        }
        else {
            changeFragment(R.id.action_nav_home_to_questionFragment, bundle);
        }
    }

    @Override
    public void onUnnNewsClick() {
        if (TextUtils.equals(premium_type,"p0")){
            adHandler(R.id.action_nav_home_to_unnNews, bundle);
        }
        else {
            changeFragment(R.id.action_nav_home_to_unnNews, bundle);
        }
    }

    @Override
    public void onArticlesClick() {
        if (TextUtils.equals(premium_type,"p0")){
            adHandler(R.id.action_nav_home_to_articals, bundle);
        }
        else {
            changeFragment(R.id.action_nav_home_to_articals, bundle);
        }
    }

    @Override
    public void onTakeExamClick() {
        if (TextUtils.equals(premium_type,"p0")){
            adHandler(R.id.action_nav_home_to_takeExamFragment, bundle);
        }
        else {
            changeFragment(R.id.action_nav_home_to_takeExamFragment, bundle);
        }
    }

    @Override
    public void onSchoolAnthemClick() {
        if (TextUtils.equals(premium_type,"p0")){
            adHandler(R.id.action_nav_home_to_schoolAnthem, bundle);
        }
        else {
            changeFragment(R.id.action_nav_home_to_schoolAnthem, bundle);
        }
    }

    @Override
    public void onSyllabusClick() {
        if (TextUtils.equals(premium_type, "p0")) {
            adHandler(R.id.action_nav_home_to_syllabus, bundle);
        } else {
            changeFragment(R.id.action_nav_home_to_syllabus, bundle);
        }
    }

    @Override
    public void onWeeklyContestClick() {
        changeFragment(R.id.action_nav_home_to_weekly_contest, bundle);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}




