package com.iotait.schoolapp.ui.homepage.ui.navigation;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.facebook.ads.RewardedVideoAd;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iotait.schoolapp.BuildConfig;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.databinding.FragmentNavigationBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.Customlog;
import com.iotait.schoolapp.helper.FragmentHelper;
import com.iotait.schoolapp.helper.NetworkHelper;
import com.iotait.schoolapp.helper.PermissionHelper;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.session.SessionManager;
import com.iotait.schoolapp.ui.homepage.ui.chat.ChatListActivty;
import com.iotait.schoolapp.ui.homepage.ui.question.details.models.QuestionItem;
import com.iotait.schoolapp.ui.login.LoginActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;


public class Navigation extends Fragment implements View.OnClickListener {

    public FragmentNavigationBinding binding;
    private PermissionHelper permissionHelper;

    public static final int CAMERA_REQUEST_CODE = 10001;
    public static final int GALLERY_REQUEST_CODE = 20001;
    public static String FACEBOOK_URL = "https://www.facebook.com/groups/1529938033858394/";
    public static String FACEBOOK_PAGE_ID = "unnaspirantsandstudnets";
    public Uri image_uri;

    String PremiumType = "";
    int versionCode;
    ProgressDialog mDialog;


    //offline
    Dialog dialog;
    long count = 0;
    long finaCount = 0;
    long initialcount = 0;
    String expireDate = "";


    List<String> subjectname;
    List<String> subjectnameTemp;
    private List<String> subjectList;
    private List<QuestionItem> questionItems;
    private List<QuestionItem> questionItemstemp;

    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;
    private ProgressDialog progressDialog;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private SessionManager sessionManager;


    //

    public static Navigation instance;

    public static Navigation getInstance() {
        return instance;
    }

    private GoogleSignInClient mGoogleSignInClient;

    private String TAG="AD_NETWORK";

    private RewardedVideoAd rewardedVideoAd;

    public Navigation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        questionItems = new ArrayList<>();
        questionItemstemp = new ArrayList<>();

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_navigation, container, false);
        View root = binding.getRoot();

        binding.back.setOnClickListener(this);
        binding.schoolAnthem.setOnClickListener(this);
        binding.questions.setOnClickListener(this);
        binding.unnNews.setOnClickListener(this);
        binding.articals.setOnClickListener(this);
        binding.takeExam.setOnClickListener(this);
        binding.syllabus.setOnClickListener(this);
        binding.chatBot.setOnClickListener(this);
        binding.facebook.setOnClickListener(this);
        binding.aboutApp.setOnClickListener(this);
        binding.rateUs.setOnClickListener(this);
        binding.ContactUs.setOnClickListener(this);
        binding.userProfilePicture.setOnClickListener(this);
        binding.goPremium.setOnClickListener(this);
        binding.LeaderBoard.setOnClickListener(this);
        binding.offline.setOnClickListener(this);
        binding.signout.setOnClickListener(this);
        binding.weeklyContest.setOnClickListener(this);
        binding.unnFaq.setOnClickListener(this);


        versionCode = BuildConfig.VERSION_CODE;
        binding.versioncode.setText("Version " + versionCode);


        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //For google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //creating google sign in client
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);


        instance = this;
        permissionHelper = new PermissionHelper(getContext());
        sessionManager = new SessionManager(getContext());


        //offline
        subjectname = new ArrayList<>();
        subjectnameTemp = new ArrayList<>();
        subjectList = new ArrayList<>();
        //


        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {

                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_navigation_to_nav_home, bundle);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId(getString(R.string.interstitialad_id));
        adRequest = new AdRequest.Builder().build();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Ad loading, please wait.");
        progressDialog.setCancelable(false);

        loadAd();
        binding.btnEarnPoint.setOnClickListener(this::onClick);
    }

    private void loadAd() {
        this.rewardedAd = new RewardedAd(getContext(), getResources().getString(R.string.rewardad_id));
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
                Log.e(TAG, "onRewardedAdFailedToLoad: " + errorCode);
            }
        };
        rewardedAd.loadAd(adRequest, adLoadCallback);
    }


    @Override
    public void onResume() {
        super.onResume();

        AppController.getFirebaseHelper().getUsers().keepSynced(true);

        pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
        editor = pref.edit();

        PremiumType = pref.getString("premiumType", null);
        boolean isPremium = pref.getBoolean("isPremium", false);
        expireDate = pref.getString("expire_date", null);


        getUserDetails();


        int point = sessionManager.getTotalPoints();


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMM yyyy hh:mm a");
        String c_date = new SimpleDateFormat("E, dd MMM yyyy hh:mm a", Locale.getDefault()).format(new Date());
        String cd_date = new SimpleDateFormat("E, dd MMM yyyy", Locale.getDefault()).format(new Date());
        Date date1 = null;
        Date date2 = null;


        if (expireDate != null && !expireDate.equals("")) {
            try {
                date1 = simpleDateFormat.parse(c_date);
                date2 = simpleDateFormat.parse(expireDate);


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (isPremium && printDifference(date1, date2)) {
            binding.offline.setVisibility(View.VISIBLE);
        } else {
            binding.offline.setVisibility(View.GONE);
        }

        if (PremiumType.equals("p1")) {
            binding.packageId.setText("Basic");
            binding.packageId.setVisibility(View.VISIBLE);
            binding.btnEarnPoint.setVisibility(View.GONE);
        } else if (PremiumType.equals("p2")) {
            binding.packageId.setText("Pro");
            binding.packageId.setVisibility(View.VISIBLE);
            binding.btnEarnPoint.setVisibility(View.GONE);
        } else {
            binding.packageId.setVisibility(View.VISIBLE);
            binding.btnEarnPoint.setVisibility(View.VISIBLE);
        }


        if (NetworkHelper.hasNetworAccess(getContext())) {
            count = 0;
            findAllfile();
        }


    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void findAllfile() {
        AppController.getFirebaseHelper().getQuestions()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            String key = snapshot.getKey();


                            AppController.getFirebaseHelper().getQuestions().child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                    questionItems.clear();


                                    if (dataSnapshot1.hasChild("explain_image") | dataSnapshot1.hasChild("optiona_image")
                                            | dataSnapshot1.hasChild("optionb_image") | dataSnapshot1.hasChild("optionc_image")
                                            | dataSnapshot1.hasChild("optiond_image") | dataSnapshot1.hasChild("scenario_image")
                                            | dataSnapshot1.hasChild("question_image")) {


                                        String answer = "";
                                        String explaination = "";
                                        String optionA = "";
                                        String optionB = "";
                                        String optionC = "";
                                        String optionD = "";
                                        boolean prepareExam = false;
                                        String question = "";
                                        String scenario = "";
                                        boolean studyQuestion = false;
                                        String subject = "";
                                        String year = "";
                                        String questionid = "";

                                        String explain_image = "";
                                        String optiona_image = "";
                                        String optionb_image = "";
                                        String optionc_image = "";
                                        String optiond_image = "";
                                        String question_image = "";
                                        String scenario_image = "";


                                        if (dataSnapshot1.child("answer") != null) {
                                            answer = dataSnapshot1.child("answer").getValue(String.class);
                                        }
                                        if (dataSnapshot1.child("explaination") != null) {
                                            explaination = dataSnapshot1.child("explaination").getValue(String.class);
                                        }
                                        if (dataSnapshot1.child("options").child("optionA") != null) {
                                            optionA = dataSnapshot1.child("options").child("optionA").getValue(String.class);
                                        }
                                        if (dataSnapshot1.child("options").child("optionB") != null) {
                                            optionB = dataSnapshot1.child("options").child("optionB").getValue(String.class);
                                        }
                                        if (dataSnapshot1.child("options").child("optionC") != null) {
                                            optionC = dataSnapshot1.child("options").child("optionC").getValue(String.class);
                                        }
                                        if (dataSnapshot1.child("options").child("optionD") != null) {
                                            optionD = dataSnapshot1.child("options").child("optionD").getValue(String.class);
                                        }
                                        if (dataSnapshot1.child("prepareExam").getValue() != null) {
                                            prepareExam = dataSnapshot1.child("prepareExam").getValue(Boolean.class);
                                        }
                                        if (dataSnapshot1.child("question") != null) {
                                            question = dataSnapshot1.child("question").getValue(String.class);
                                        }
                                        if (dataSnapshot1.child("scenario").getValue() != null) {
                                            scenario = dataSnapshot1.child("scenario").getValue(String.class);
                                        }
                                        if (dataSnapshot1.child("studyQuestion").getValue() != null) {
                                            studyQuestion = dataSnapshot1.child("studyQuestion").getValue(Boolean.class);
                                        }
                                        if (dataSnapshot1.child("subject").getValue() != null) {
                                            subject = dataSnapshot1.child("subject").getValue(String.class);
                                        }
                                        if (dataSnapshot1.child("year").getValue() != null) {
                                            year = dataSnapshot1.child("year").getValue(String.class);
                                        }


                                        if (dataSnapshot1.child("explain_image").getValue() != null) {
                                            explain_image = dataSnapshot1.child("explain_image").getValue(String.class);
                                            count++;


                                        }
                                        if (dataSnapshot1.child("optiona_image").getValue() != null) {
                                            optiona_image = dataSnapshot1.child("options").child("optiona_image").getValue(String.class);
                                            count++;

                                        }
                                        if (dataSnapshot1.child("optionb_image").getValue() != null) {
                                            optionb_image = dataSnapshot1.child("options").child("optionb_image").getValue(String.class);
                                            count++;

                                        }
                                        if (dataSnapshot1.child("optionc_image").getValue() != null) {
                                            optionc_image = dataSnapshot1.child("options").child("optionc_image").getValue(String.class);
                                            count++;

                                        }
                                        if (dataSnapshot1.child("optiond_image").getValue() != null) {
                                            optiond_image = dataSnapshot1.child("options").child("optiond_image").getValue(String.class);
                                            count++;

                                        }

                                        if (dataSnapshot1.child("question_image").getValue() != null) {
                                            question_image = dataSnapshot1.child("question_image").getValue(String.class);
                                            count++;

                                        }
                                        if (dataSnapshot1.child("scenario_image").getValue() != null) {
                                            scenario_image = dataSnapshot1.child("scenario_image").getValue(String.class);
                                            count++;

                                        }


                                        questionid = dataSnapshot1.getKey();
                                        QuestionItem questionItem = new QuestionItem();
                                        questionItem.setAnswer(answer);
                                        questionItem.setExplaination(explaination);
                                        questionItem.setOptionA(optionA);
                                        questionItem.setOptionB(optionB);
                                        questionItem.setOptionC(optionC);
                                        questionItem.setOptionD(optionD);
                                        questionItem.setPrepareExam(prepareExam);
                                        questionItem.setQuestion(question);
                                        questionItem.setScenario(scenario);
                                        questionItem.setStudyQuestion(studyQuestion);
                                        questionItem.setSubject(subject);
                                        questionItem.setYear(year);
                                        questionItem.setQuestionId(questionid);

                                        questionItem.setExplain_image(explain_image);
                                        questionItem.setOptiona_image(optiona_image);
                                        questionItem.setOptionb_image(optionb_image);
                                        questionItem.setOptionc_image(optionc_image);
                                        questionItem.setOptiond_image(optiond_image);
                                        questionItem.setQuestion_image(question_image);
                                        questionItem.setScenario_image(scenario_image);


                                        questionItems.add(questionItem);


                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }


                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private RewardedAd rewardedAd;

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        int id = v.getId();
        switch (id) {
            case R.id.back:
                FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_navigation_to_nav_home, bundle);
                break;

            case R.id.schoolAnthem:
                if (TextUtils.equals(PremiumType, "p0")) {
                    adHandler(R.id.action_navigation_to_schoolAnthem, bundle);
                } else {
                    changeFragment(R.id.action_navigation_to_schoolAnthem, bundle);
                }
                break;

            case R.id.questions:
                if (TextUtils.equals(PremiumType, "p0")) {
                    adHandler(R.id.action_navigation_to_questionFragment, bundle);
                } else {
                    changeFragment(R.id.action_navigation_to_questionFragment, bundle);
                }
                break;

            case R.id.unnNews:

                if (TextUtils.equals(PremiumType, "p0")) {
                    adHandler(R.id.action_navigation_to_unnNews, bundle);
                } else {
                    changeFragment(R.id.action_navigation_to_unnNews, bundle);
                }
                break;
            case R.id.articals:
                if (TextUtils.equals(PremiumType, "p0")) {
                    adHandler(R.id.action_navigation_to_articals, bundle);
                } else {
                    changeFragment(R.id.action_navigation_to_articals, bundle);
                }
                break;
            case R.id.takeExam:
                if (TextUtils.equals(PremiumType, "p0")) {
                    adHandler(R.id.action_navigation_to_takeExamFragment, bundle);
                } else {
                    changeFragment(R.id.action_navigation_to_takeExamFragment, bundle);
                }
                break;
            case R.id.syllabus:
                if (TextUtils.equals(PremiumType, "p0")) {
                    adHandler(R.id.action_navigation_to_syllabus, bundle);
                } else {
                    changeFragment(R.id.action_navigation_to_syllabus, bundle);
                }
                break;
            case R.id.chatBot:
                if (TextUtils.equals(PremiumType, "p2")) {
                    startActivity(new Intent(getContext(), ChatListActivty.class));
                } else if (TextUtils.equals(PremiumType, "p1")) {
                    new CustomMessage(getActivity(), "Chat is't available, please update your package");
                } else if (TextUtils.equals(PremiumType, "p0")) {
                    new CustomMessage(getActivity(), "Please update to premium package to enable chat");
                    FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_navigation_to_premiumProcessFragment, bundle);
                }
                break;
            case R.id.facebook:
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(getContext());
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
                break;
            case R.id.aboutApp:

                if (TextUtils.equals(PremiumType, "p0")) {
                    adHandler(R.id.action_navigation_to_aboutApp, bundle);
                } else {
                    changeFragment(R.id.action_navigation_to_aboutApp, bundle);
                }
                break;
            case R.id.rateUs:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.iotait.schoolapp")));
                break;
            case R.id.ContactUs:

                FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_navigation_to_conntactusdetails, bundle);
                break;
            case R.id.userProfilePicture:
                showImageImportDialog();
                break;
            case R.id.go_premium:
                FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_navigation_to_premiumProcessFragment, bundle);
                break;
            case R.id.LeaderBoard:
                bundle.putString("type", "FromNavigation");
                FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_navigation_to_LeaderBoard, bundle);
                break;
            case R.id.offline:
                offlineAll();
                break;
            case R.id.signout:
                signout();
                break;
            case R.id.weekly_contest:
                FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_navigation_to_weekly_contest, bundle);
                break;
            case R.id.btn_earn_point:
                String c_date = new SimpleDateFormat("E, dd MMM yyyy", Locale.getDefault()).format(new Date());
                String s_date = sessionManager.getCurrentDate();
                int daily_reward = sessionManager.getDaylyReward();
                if (TextUtils.equals(c_date, s_date) && daily_reward < 5) {
                    loadRewardAd();
                } else {
                    new CustomMessage(getActivity(), "You have earned maximum points for today. Continue tomorrow.");
                }
                break;
            case R.id.unn_faq:
                FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_navigation_to_unnFaqFragment, bundle);
                break;
        }
    }

    private void loadRewardAd() {

        if (this.rewardedAd.isLoaded()) {
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.

                }

                @Override
                public void onRewardedAdClosed() {
                    // Ad closed.

                    loadAd();
                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    // User earned reward.
                    Log.d("USER_DATA", "" + reward.getAmount());
                    sessionManager.setDaylyReward(sessionManager.getDaylyReward() + 1);
                    sessionManager.setCurrentPoints(sessionManager.getTotalPoints() + 10);
                    HashMap<String, Object> point = new HashMap<>();
                    point.put("point", String.valueOf(sessionManager.getTotalPoints()));
                    point.put("rewardCount", String.valueOf(sessionManager.getDaylyReward()));
                    AppController.getFirebaseHelper().getUsers().child(AppController.getFirebaseHelper()
                            .getFirebaseAuth().getCurrentUser().getUid()).updateChildren(point).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            new CustomMessage(getActivity(), "" + e.getMessage());
                        }
                    });

                }

                @Override
                public void onRewardedAdFailedToShow(int errorCode) {
                    // Ad failed to display.

                    loadAd();
                }
            };
            rewardedAd.show(getActivity(), adCallback);
        } else {
            loadAd();
            new CustomMessage(getActivity(), "Ad not loaded please try again later,");
        }


//        if (Common.reawerType.toLowerCase().equals("google")){
//            if (this.rewardedAd.isLoaded()) {
//                RewardedAdCallback adCallback = new RewardedAdCallback() {
//                    @Override
//                    public void onRewardedAdOpened() {
//                        // Ad opened.
//
//                    }
//
//                    @Override
//                    public void onRewardedAdClosed() {
//                        // Ad closed.
//
//                        loadAd();
//                    }
//
//                    @Override
//                    public void onUserEarnedReward(@NonNull RewardItem reward) {
//                        // User earned reward.
//                        Log.d("USER_DATA", "" + reward.getAmount());
//                        sessionManager.setDaylyReward(sessionManager.getDaylyReward() + 1);
//                        sessionManager.setCurrentPoints(sessionManager.getTotalPoints() + 10);
//                        HashMap<String, Object> point = new HashMap<>();
//                        point.put("point", String.valueOf(sessionManager.getTotalPoints()));
//                        point.put("rewardCount", String.valueOf(sessionManager.getDaylyReward()));
//                        AppController.getFirebaseHelper().getUsers().child(AppController.getFirebaseHelper()
//                                .getFirebaseAuth().getCurrentUser().getUid()).updateChildren(point).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                new CustomMessage(getActivity(), "" + e.getMessage());
//                            }
//                        });
//
//                    }
//
//                    @Override
//                    public void onRewardedAdFailedToShow(int errorCode) {
//                        // Ad failed to display.
//
//                        loadAd();
//                    }
//                };
//                rewardedAd.show(getActivity(), adCallback);
//            } else {
//
//                new CustomMessage(getActivity(), "Ad not loaded please try again later,");
//            }
//        }else if(Common.reawerType.toLowerCase().equals("facebook")){
//            rewardedVideoAd = new RewardedVideoAd(getActivity(), getResources().getString(R.string.fb_rewardad_id));
//            rewardedVideoAd.setAdListener(new RewardedVideoAdListener() {
//                @Override
//                public void onError(Ad ad, AdError error) {
//                    // Rewarded video ad failed to load
//                    Log.e(TAG, "Rewarded video ad failed to load: " + error.getErrorMessage());
//                    new CustomMessage(getActivity(), "Ad not loaded please try again later,");
//                }
//
//                @Override
//                public void onAdLoaded(Ad ad) {
//                    // Rewarded video ad is loaded and ready to be displayed
//                    Log.d(TAG, "Rewarded video ad is loaded and ready to be displayed!");
//                    rewardedVideoAd.show();
//                }
//
//                @Override
//                public void onAdClicked(Ad ad) {
//                    // Rewarded video ad clicked
//                    Log.d(TAG, "Rewarded video ad clicked!");
//                }
//
//                @Override
//                public void onLoggingImpression(Ad ad) {
//                    // Rewarded Video ad impression - the event will fire when the
//                    // video starts playing
//                    Log.d(TAG, "Rewarded video ad impression logged!");
//                }
//
//                @Override
//                public void onRewardedVideoCompleted() {
//                    // Rewarded Video View Complete - the video has been played to the end.
//                    // You can use this event to initialize your reward
//                    Log.d(TAG, "Rewarded video completed!");
//
//                    // Call method to give reward
//                    // giveReward();
//                    sessionManager.setDaylyReward(sessionManager.getDaylyReward() + 1);
//                    sessionManager.setCurrentPoints(sessionManager.getTotalPoints() + 10);
//                    HashMap<String, Object> point = new HashMap<>();
//                    point.put("point", String.valueOf(sessionManager.getTotalPoints()));
//                    point.put("rewardCount", String.valueOf(sessionManager.getDaylyReward()));
//                    AppController.getFirebaseHelper().getUsers().child(AppController.getFirebaseHelper()
//                            .getFirebaseAuth().getCurrentUser().getUid()).updateChildren(point).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            new CustomMessage(getActivity(), "" + e.getMessage());
//                        }
//                    });
//                }
//
//                @Override
//                public void onRewardedVideoClosed() {
//                    // The Rewarded Video ad was closed - this can occur during the video
//                    // by closing the app, or closing the end card.
//                    Log.d(TAG, "Rewarded video ad closed!");
//                }
//            });
//            rewardedVideoAd.loadAd();
//        }

    }

    private com.facebook.ads.InterstitialAd interstitialAdFacebook;
    private void adHandler(int action, Bundle bundle) {

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
                changeFragment(action, bundle);
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
                changeFragment(action, bundle);
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
//                    changeFragment(action, bundle);
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
//                    changeFragment(action, bundle);
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
        FragmentHelper.changeFragmet(binding.getRoot(), action, bundle);
    }

    private void signout() {
        AppController.getFirebaseHelper().getFirebaseAuth().signOut();
        AppController.getFirebaseHelper().getFirebaseAuth().signOut();
        mGoogleSignInClient.signOut();
        LoginManager.getInstance().logOut();
        sessionManager.clearData();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

    private void offlineAll() {


        if (NetworkHelper.hasNetworAccess(getContext())) {

            if (!permissionHelper.checkPermission())
                permissionHelper.requestPermission();
            else {

                createDefDirectory();
                if (dialog != null) {
                    dialog.dismiss();
                }
                dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.conformation_dialoge);
                dialog.setCanceledOnTouchOutside(true);
                Button yes = dialog.findViewById(R.id.yes);
                Button no = dialog.findViewById(R.id.no);

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog != null)
                            dialog.dismiss();
                    }
                });
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog = new ProgressDialog(getContext());
                        mDialog.setMessage("Saving Data! Do not turn off Internet");

                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.show();

                        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                            @Override
                            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                if (keyCode == KeyEvent.KEYCODE_BACK) {
                                    if (mDialog.isShowing()) {
                                        Toast.makeText(getContext(), "Offline Process is running!! Please keep patience, We almost there!!", Toast.LENGTH_SHORT).show();
                                        //your logic here for back button pressed event
                                    }
                                    return true;
                                }
                                return false;
                            }
                        });

                        AppController.getFirebaseHelper().getFullExam().keepSynced(true);
                        AppController.getFirebaseHelper().getSubject().keepSynced(true);
                        AppController.getFirebaseHelper().getSyllabus().keepSynced(true);
                        AppController.getFirebaseHelper().getCustomExam().keepSynced(true);
                        AppController.getFirebaseHelper().getQuestions().keepSynced(true);
                        AppController.getFirebaseHelper().getYears().keepSynced(true);
                        AppController.getFirebaseHelper().getUsers().keepSynced(true);

                        offlineSyllabus();
                        offlineSubject();
                        offlineQuestion();
                        getUserDetails();


                        if (dialog != null)
                            dialog.dismiss();


                    }
                });

                dialog.show();
            }

        } else {
            Toast.makeText(getContext(), "you have to turn on your internet", Toast.LENGTH_SHORT).show();
        }

    }

    private void offlineQuestion() {
        if (NetworkHelper.hasNetworAccess(getContext())) {


            AppController.getFirebaseHelper().getQuestions()
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            long countChildren = dataSnapshot.getChildrenCount();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                String key = snapshot.getKey();


                                AppController.getFirebaseHelper().getQuestions().child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                        questionItems.clear();


                                        if (dataSnapshot1.hasChild("explain_image") | dataSnapshot1.hasChild("optiona_image")
                                                | dataSnapshot1.hasChild("optionb_image") | dataSnapshot1.hasChild("optionc_image")
                                                | dataSnapshot1.hasChild("optiond_image") | dataSnapshot1.hasChild("scenario_image")
                                                | dataSnapshot1.hasChild("question_image")) {


                                            //    initialcount = initialcount+count;


                                            String answer = "";
                                            String explaination = "";
                                            String optionA = "";
                                            String optionB = "";
                                            String optionC = "";
                                            String optionD = "";
                                            boolean prepareExam = false;
                                            String question = "";
                                            String scenario = "";
                                            boolean studyQuestion = false;
                                            String subject = "";
                                            String year = "";
                                            String questionid = "";

                                            String explain_image = "";
                                            String optiona_image = "";
                                            String optionb_image = "";
                                            String optionc_image = "";
                                            String optiond_image = "";
                                            String question_image = "";
                                            String scenario_image = "";


                                            if (dataSnapshot1.child("answer") != null) {
                                                answer = dataSnapshot1.child("answer").getValue(String.class);
                                            }
                                            if (dataSnapshot1.child("explaination") != null) {
                                                explaination = dataSnapshot1.child("explaination").getValue(String.class);
                                            }
                                            if (dataSnapshot1.child("options").child("optionA") != null) {
                                                optionA = dataSnapshot1.child("options").child("optionA").getValue(String.class);
                                            }
                                            if (dataSnapshot1.child("options").child("optionB") != null) {
                                                optionB = dataSnapshot1.child("options").child("optionB").getValue(String.class);
                                            }
                                            if (dataSnapshot1.child("options").child("optionC") != null) {
                                                optionC = dataSnapshot1.child("options").child("optionC").getValue(String.class);
                                            }
                                            if (dataSnapshot1.child("options").child("optionD") != null) {
                                                optionD = dataSnapshot1.child("options").child("optionD").getValue(String.class);
                                            }
                                            if (dataSnapshot1.child("prepareExam").getValue() != null) {
                                                prepareExam = dataSnapshot1.child("prepareExam").getValue(Boolean.class);
                                            }
                                            if (dataSnapshot1.child("question") != null) {
                                                question = dataSnapshot1.child("question").getValue(String.class);
                                            }
                                            if (dataSnapshot1.child("scenario").getValue() != null) {
                                                scenario = dataSnapshot1.child("scenario").getValue(String.class);
                                            }
                                            if (dataSnapshot1.child("studyQuestion").getValue() != null) {
                                                studyQuestion = dataSnapshot1.child("studyQuestion").getValue(Boolean.class);
                                            }
                                            if (dataSnapshot1.child("subject").getValue() != null) {
                                                subject = dataSnapshot1.child("subject").getValue(String.class);
                                            }
                                            if (dataSnapshot1.child("year").getValue() != null) {
                                                year = dataSnapshot1.child("year").getValue(String.class);
                                            }


                                            if (dataSnapshot1.child("explain_image").getValue() != null) {
                                                explain_image = dataSnapshot1.child("explain_image").getValue(String.class);

                                                downloadImage(explain_image);

                                            }
                                            if (dataSnapshot1.child("optiona_image").getValue() != null) {
                                                optiona_image = dataSnapshot1.child("options").child("optiona_image").getValue(String.class);

                                                downloadImage(optiona_image);
                                            }
                                            if (dataSnapshot1.child("optionb_image").getValue() != null) {
                                                optionb_image = dataSnapshot1.child("options").child("optionb_image").getValue(String.class);

                                                downloadImage(optionb_image);
                                            }
                                            if (dataSnapshot1.child("optionc_image").getValue() != null) {
                                                optionc_image = dataSnapshot1.child("options").child("optionc_image").getValue(String.class);

                                                downloadImage(optionc_image);
                                            }
                                            if (dataSnapshot1.child("optiond_image").getValue() != null) {
                                                optiond_image = dataSnapshot1.child("options").child("optiond_image").getValue(String.class);

                                                downloadImage(optiond_image);
                                            }

                                            if (dataSnapshot1.child("question_image").getValue() != null) {
                                                question_image = dataSnapshot1.child("question_image").getValue(String.class);

                                                downloadImage(question_image);
                                            }
                                            if (dataSnapshot1.child("scenario_image").getValue() != null) {
                                                scenario_image = dataSnapshot1.child("scenario_image").getValue(String.class);

                                                downloadImage(scenario_image);
                                            }


                                            questionid = dataSnapshot1.getKey();
                                            QuestionItem questionItem = new QuestionItem();
                                            questionItem.setAnswer(answer);
                                            questionItem.setExplaination(explaination);
                                            questionItem.setOptionA(optionA);
                                            questionItem.setOptionB(optionB);
                                            questionItem.setOptionC(optionC);
                                            questionItem.setOptionD(optionD);
                                            questionItem.setPrepareExam(prepareExam);
                                            questionItem.setQuestion(question);
                                            questionItem.setScenario(scenario);
                                            questionItem.setStudyQuestion(studyQuestion);
                                            questionItem.setSubject(subject);
                                            questionItem.setYear(year);
                                            questionItem.setQuestionId(questionid);

                                            questionItem.setExplain_image(explain_image);
                                            questionItem.setOptiona_image(optiona_image);
                                            questionItem.setOptionb_image(optionb_image);
                                            questionItem.setOptionc_image(optionc_image);
                                            questionItem.setOptiond_image(optiond_image);
                                            questionItem.setQuestion_image(question_image);
                                            questionItem.setScenario_image(scenario_image);


                                            questionItems.add(questionItem);


                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }


                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


        }
    }

    private void downloadImage(String explain_image) {

        checkFolder(explain_image, "images/", "imagelist");
    }

    private void checkFolder(String link, String folderName, String type) {

        if (!folderName.equals("/myUNN_Post_Utme/")) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myUNN_Post_Utme/" + folderName;
            File dir = new File(path);
            boolean isDirectoryCreated = dir.exists();
            if (!isDirectoryCreated) {
                isDirectoryCreated = dir.mkdir();
            }
            if (isDirectoryCreated) {
                downloadData(link, path, type);

            }
        } else {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + folderName;
            File dir = new File(path);
            boolean isDirectoryCreated = dir.exists();
            if (!isDirectoryCreated) {
                isDirectoryCreated = dir.mkdir();
            }
            if (isDirectoryCreated) {
                downloadData(link, path, type);
            }
        }

    }

    // main Directory creating method
    public void createDefDirectory() {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myUNN_Post_Utme/";
        File dir = new File(path);
        boolean isDirectoryCreated = dir.exists();
        if (!isDirectoryCreated) {
            isDirectoryCreated = dir.mkdir();
        }
        if (isDirectoryCreated) {
            // do something


        }
    }

    private void downloadData(String link, String fileSavePath, String type) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(link);
        final long THIRTY_MEGABYTE = 1024 * 1024 * 25;


        httpsReference.getBytes(THIRTY_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                File filename;

                String fileName1 = "";


                switch (type) {
                    case "image":
                    case "imagelist": {
                        Pattern p = Pattern.compile("Question%2F(.+?)\\?alt");
                        Matcher m = p.matcher(link);
                        if (m.find()) {
                            String spaceReuce = m.group(1).replace("%20", " ");
                            fileName1 = spaceReuce;
                        }
                        filename = new File(fileSavePath, fileName1 + ".jpeg");
                        saveFile(filename, bytes, type);

                        break;
                    }

                    default:
                        filename = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + fileSavePath, "notFound");
                        break;
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors

                Toast.makeText(getContext(), "" + exception, Toast.LENGTH_SHORT).show();

            }

        });
    }

    private void saveFile(File filename, byte[] bytes, String type) {


        finaCount++;


        try {
            OutputStream output = new FileOutputStream(filename);
            output.write(bytes);

            // Toast.makeText(getContext(), "save", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            // Toast.makeText(getContext(), "not save", Toast.LENGTH_SHORT).show();
        }


        if (finaCount >= count) {
            //stop progress bar here


            mDialog.dismiss();
            //  Toast.makeText(getContext(), "final count" + finaCount + " count children" + count, Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(), "App Offline SuccessFull", Toast.LENGTH_SHORT).show();


            ProgressDialog pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Initializing... ");

            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
            final Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    pDialog.dismiss();
                    Toast.makeText(getContext(), "Initializing successful ! you Ready to Go !! Enjoy ", Toast.LENGTH_SHORT).show();
                }
            }, 40000);
        }


    }

    private void offlineSyllabus() {
        AppController.getFirebaseHelper().getSyllabus().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subjectnameTemp.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = snapshot.getKey();
                        subjectnameTemp.add(key);
                    }

                    subjectname.clear();
                    subjectname.addAll(subjectnameTemp);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void offlineSubject() {
        AppController.getFirebaseHelper().getSubject().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subjectList.clear();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String subject = dataSnapshot1.child("subject").getValue(String.class);
                        subjectList.add(subject);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });

    }

    private String getFacebookPageURL(Context context) {
        try {
            // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://web.facebook.com/groups/unnaspirantsandstudnets/"));
            // startActivity(intent);

            PackageManager packageManager = context.getPackageManager();
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (Exception e) {
            return FACEBOOK_URL;
        }
    }

    private void showImageImportDialog() {
        String[] items = {" Camera", " Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Select Image");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    if (permissionHelper.checkPermission()) {
                        captureImage();
                    } else {
                        permissionHelper.requestPermission();
                    }
                }
                if (i == 1) {
                    if (permissionHelper.checkPermission()) {
                        pickImage();
                    } else {
                        permissionHelper.requestPermission();
                    }
                }
            }
        });
        dialog.create().show();
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    private void captureImage() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NewPicture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image to text");
        image_uri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE) {
                binding.userProfilePicture.setImageURI(data.getData());
                uploadImageToFirebase(data.getData());
            }
            if (requestCode == CAMERA_REQUEST_CODE) {
                binding.userProfilePicture.setImageURI(image_uri);
                uploadImageToFirebase(image_uri);
            }
        }
    }


    private StorageReference ref;
    private UploadTask uploadTask;

    public void uploadImageToFirebase(Uri uri) {

        final String userkey = AppController.getFirebaseHelper().getFirebaseAuth().getUid();
        ref = FirebaseStorage.getInstance().getReference().child("ProfileImages/" +
                userkey);

        final ProgressDialog mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Profile image uploading...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setIndeterminate(false);
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setMax(100);
        mDialog.show();

        uploadTask = ref.putFile(uri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("photo", uri.toString());
                        AppController.getFirebaseHelper().getUsers().child(userkey).updateChildren(hashMap);
                        new CustomMessage(getActivity(), "Profile Updated Successfully");
                        getUserDetails();
                        mDialog.dismiss();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                new CustomMessage(getActivity(), "" + e.getMessage());
                mDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = 0;
                if (taskSnapshot.getBytesTransferred() != 0) {
                    progress = 100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                }
                mDialog.setProgress((int) progress);
            }
        });
    }


    private void getUserDetails() {

        String cd_date = new SimpleDateFormat("E, dd MMM yyyy", Locale.getDefault()).format(new Date());
        binding.spinKit.setVisibility(View.VISIBLE);
        AppController.getFirebaseHelper().getUsers().child(AppController.getFirebaseHelper()
                .getFirebaseAuth().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                binding.spinKit.setVisibility(View.GONE);
                if (dataSnapshot.exists()) {
                    String email = "";
                    String personname = "";
                    String photo = "";
                    String point = "0";
                    String rewardCount = "0";
                    String pointsDate = cd_date;
                    if (dataSnapshot.child("email").getValue() != null)
                        email = dataSnapshot.child("email").getValue(String.class);
                    if (dataSnapshot.child("personname").getValue() != null)
                        personname = dataSnapshot.child("personname").getValue(String.class);
                    if (dataSnapshot.child("photo").getValue() != null)
                        photo = dataSnapshot.child("photo").getValue(String.class);
                    if (dataSnapshot.child("point").getValue() != null)
                        point = dataSnapshot.child("point").getValue(String.class);
                    if (dataSnapshot.child("pointsDate").getValue() != null)
                        pointsDate = dataSnapshot.child("pointsDate").getValue(String.class);
                    if (dataSnapshot.child("rewardCount").getValue() != null)
                        rewardCount = dataSnapshot.child("rewardCount").getValue(String.class);

                    binding.userName.setText(personname);
                    binding.userEmail.setText(email);
                    UIHelper.setPersonImage(binding.userProfilePicture, photo);

                    sessionManager.setCurrentPoints(Integer.parseInt(point));
                    sessionManager.setCurrentDate(pointsDate);
                    sessionManager.setDaylyReward(Integer.parseInt(rewardCount));


                    if (dataSnapshot.child("premium_type").getValue(String.class) != null && (Boolean) dataSnapshot.child("isPremimum").getValue() &&
                            dataSnapshot.child("expire_date").getValue(String.class) != null) {
                        String premiumType = dataSnapshot.child("premium_type").getValue(String.class);
                        String Expire_date = dataSnapshot.child("expire_date").getValue(String.class);
                        boolean ispremium = (Boolean) dataSnapshot.child("isPremimum").getValue();


                        editor.putString("premiumType", premiumType);
                        editor.putBoolean("isPremium", ispremium);
                        editor.putString("expire_date", Expire_date);
                        editor.commit();

                        Customlog.showlogD("PACKAGE_TYPE", "1" + PremiumType);

                    } else {
                        binding.packageId.setText(point + " Pts");
                        editor.putString("premiumType", "p0");
                        editor.putBoolean("isPremium", false);
                        editor.putString("expire_date", null);
                        editor.commit();

                        Customlog.showlogD("PACKAGE_TYPE", "2" + PremiumType);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                binding.spinKit.setVisibility(View.GONE);
            }
        });
    }


    private boolean printDifference(Date startDate, Date endDate) {
        if (startDate != null && endDate != null) {
            if (endDate.after(startDate)) {


                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }
    public void openWhatsapp(View view) {
        String url = "https://wa.link/pmm0lz";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

}
