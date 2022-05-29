package com.iotait.schoolapp.ui.homepage.ui.weeklycontest.getreward;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.common.Common;
import com.iotait.schoolapp.databinding.FragmentGetRewardBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.FragmentHelper;
import com.iotait.schoolapp.session.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class GetRewardFragment extends Fragment implements View.OnClickListener {


    SharedPreferences pref;
    FragmentGetRewardBinding binding;
    private SessionManager sessionManager;
    private String PremiumType = "";
    private AdRequest adRequest;
    private RewardedAd rewardedAd;
    private RewardedVideoAd rewardedVideoAd;
    private String TAG="AD_NETWORK";


    public GetRewardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_get_reward, container, false);

        View root = binding.getRoot();
        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        binding.earnMorePoints.setOnClickListener(this);

        adRequest = new AdRequest.Builder().build();
        loadAd();
        sessionManager = new SessionManager(getContext());

        binding.includeToolbar.toolbarTitle.setText("MY COIN");
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.includeToolbar.toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_getRewarded_to_weekly_contest, new Bundle());
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {

                FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_getRewarded_to_weekly_contest, new Bundle());
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);


    }


    @Override
    public void onResume() {
        super.onResume();
        sessionManager = new SessionManager(getContext());
        binding.pointsinmycoin.setText(String.valueOf(sessionManager.getTotalPoints()));
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

            }
        };
        rewardedAd.loadAd(adRequest, adLoadCallback);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.earnMorePoints:
                String c_date = new SimpleDateFormat("E, dd MMM yyyy", Locale.getDefault()).format(new Date());
                String s_date = sessionManager.getCurrentDate();
                int daily_reward = sessionManager.getDaylyReward();
                if (TextUtils.equals(c_date, s_date) && daily_reward < 5) {
                    loadRewardAd();
                } else {
                    new CustomMessage(getActivity(), "You have earned maximum points for today. Continue tomorrow.");
                }
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

            new CustomMessage(getActivity(), "Ad not loaded please try again later,");
        }

//        if (Common.reawerType.toLowerCase().equals("google")) {
//            if (this.rewardedAd.isLoaded()) {
//
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
//
//                        sessionManager.setDaylyReward(sessionManager.getDaylyReward() + 1);
//                        sessionManager.setCurrentPoints(sessionManager.getTotalPoints() + 10);
//
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
//                        loadAd();
//                    }
//                };
//                rewardedAd.show(getActivity(), adCallback);
//            } else {
//
//                new CustomMessage(getActivity(), "Ad not loaded please try again later,");
//            }
//        }
//        else if(Common.reawerType.toLowerCase().equals("facebook")){
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
}