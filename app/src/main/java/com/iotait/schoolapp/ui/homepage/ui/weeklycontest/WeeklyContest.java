package com.iotait.schoolapp.ui.homepage.ui.weeklycontest;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.databinding.FragmentWeeklyContestBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.FragmentHelper;
import com.iotait.schoolapp.helper.NetworkHelper;
import com.iotait.schoolapp.session.SessionManager;
import com.iotait.schoolapp.ui.homepage.ui.exam.models.SelectItem;
import com.iotait.schoolapp.ui.homepage.ui.weeklycontest.subjectadapter.RecyclerWeeklyContestSubjectAdapter;
import com.iotait.schoolapp.ui.homepage.ui.weeklycontest.view.WeeklyContestSelectionView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class WeeklyContest extends Fragment implements View.OnClickListener, WeeklyContestSelectionView {

    FragmentWeeklyContestBinding binding;

    private Dialog dialog;
    private RecyclerWeeklyContestSubjectAdapter recyclerExamSubjectAdapter;
    private SpinKitView spinKitView;
    private List<String> subjectList;
    private List<SelectItem> selectList;


    List<String> QuestionList;


    private String EVENT_DATE_TIME = "";
    private String EVENT_END_DATE_TIME = "";
    private String PresentDate = "";
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    long subjectQuestionCount;
    long TotalQuestionCount;


    boolean findUser = false;
    boolean checkExamStatus = false;

    public WeeklyContest() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weekly_contest, container, false);

        View root = binding.getRoot();

        QuestionList = new ArrayList<>();


        return root;
    }

    boolean contestStatus = false;

    @Override
    public void onResume() {
        super.onResume();


//        CheckParticipant();
        getWeeklyContestQuestionList();
        sessionManager = new SessionManager(getContext());
        binding.points.setText(String.valueOf(sessionManager.getTotalPoints()));
        postContestData();
    }

    private void CheckParticipant() {
        if (getActivity() != null) {
            if (NetworkHelper.hasNetworAccess(getActivity())) {
                AppController.getFirebaseHelper().getWeeklyContestParticipateStatus().child("ParticipantList").
                        addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    binding.spinKit.setVisibility(View.GONE);
                                    binding.tvfull.setClickable(true);
                                    for (DataSnapshot ps : dataSnapshot.getChildren()) {
                                        String key = ps.getKey();
                                        assert key != null;
                                        if (key.equals(Objects.requireNonNull(AppController.getFirebaseHelper().getFirebaseAuth().getCurrentUser()).getUid())) {
                                            findUser = true;
                                        } else {
                                            findUser = false;
                                        }
                                    }

                                    if (findUser) {
                                        new CustomMessage(getActivity(), "You Already Participated in the Exam");
                                    } else {

                                        if (PremiumType.equals("p0")) {
                                            if (sessionManager.getTotalPoints() >= 150) {
                                                showDialog();
                                            } else {
                                                showpointDialog();
                                            }
                                        } else {
                                            showDialog();
                                        }
                                    }

                                } else {
                                    if (!findUser) {
                                        binding.spinKit.setVisibility(View.GONE);
                                        binding.tvfull.setClickable(true);
                                        if (PremiumType.equals("p0")) {
                                            if (sessionManager.getTotalPoints() >= 150) {
                                                showDialog();
                                            } else {
                                                showpointDialog();
                                            }
                                        } else {
                                            showDialog();
                                        }
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                new CustomMessage(getActivity(), "" + databaseError.getMessage());
                            }
                        });
            } else {
                new CustomMessage(getActivity(), "Please Connect to the Internet!");
            }
        }
    }

    private void CheckStatus() {

        if (getActivity() != null) {
            if (NetworkHelper.hasNetworAccess(getActivity())) {

                AppController.getFirebaseHelper().getWeeklyContestLiveStatus().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            contestStatus = (boolean) dataSnapshot.child("live_status").getValue();

                        }
                        if (contestStatus) {
                            CheckParticipant();
                        } else {
                            binding.spinKit.setVisibility(View.GONE);
                            binding.tvfull.setClickable(true);
                            new CustomMessage(getActivity(), "Exam is not Live Yet");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        new CustomMessage(getActivity(), "" + databaseError.getMessage());
                    }
                });
            }
        } else {
            new CustomMessage(getActivity(), "No Internet. Please Connect to the Internet");
        }

    }

    Calendar cal = Calendar.getInstance();
    List<Date> disable = new ArrayList<>();

    private void postContestData() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String c_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        String startTime = "07:00:00";
        String EndTime = "22:00:00";

        Date date1 = null;
        Date date2 = null;
        try {
            date1 = simpleDateFormat.parse(c_date);
            date2 = simpleDateFormat.parse(c_date);


            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm:ss");
            String currentTime = simpleDateFormat1.format(date2);

            Date currentTimer = simpleDateFormat1.parse(currentTime);
            Date startTimer = simpleDateFormat1.parse(startTime);
            Date endTimer = simpleDateFormat1.parse(EndTime);


            Calendar c = Calendar.getInstance();
            c.setFirstDayOfWeek(Calendar.SATURDAY);
            c.setTime(date1);
            int currentday = c.get(Calendar.DAY_OF_WEEK);


            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");


            if (currentday == Calendar.SATURDAY && currentTimer.after(startTimer) && currentTimer.before(endTimer)) {
                //  EVENT_DATE_TIME = fmt.format(date1) + " 07:00:00";
                EVENT_END_DATE_TIME = fmt.format(date1) + " 21:59:00";
                PresentDate = c_date;
                binding.countdown.setText("Live");
                contestCloseTimer();
            } else {

                Calendar cal = Calendar.getInstance();
                cal.setFirstDayOfWeek(Calendar.SATURDAY);
                cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                cal.add(Calendar.WEEK_OF_MONTH, 1);
                String boos = simpleDateFormat.format(cal.getTime());
                Date bosk = simpleDateFormat.parse(boos);
                EVENT_DATE_TIME = fmt.format(bosk) + " 07:00:00";

                PresentDate = c_date;

                CountDown();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


    private void getWeeklyContestQuestionList() {
        AppController.getFirebaseHelper().getWeeklyQuestionList().child(AppController.getFirebaseHelper().getFirebaseAuth().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            QuestionList.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String key = snapshot.getKey();
                                AppController.getFirebaseHelper().getWeeklyQuestionList().child(AppController.getFirebaseHelper().getFirebaseAuth().getUid())
                                        .child(key).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {

                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                subjectQuestionCount = dataSnapshot.getChildrenCount();

                                                String questionId = ds.getKey();
                                                QuestionList.add(questionId);
                                            }


                                        } else {

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                deleteWeeklyContestQuestion(key);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void deleteWeeklyContestQuestion(String key) {
        Query query = AppController.getFirebaseHelper().getWeeklycontestQuestions().orderByChild("subject").equalTo(key);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    TotalQuestionCount = dataSnapshot.getChildrenCount();
                    if (subjectQuestionCount == TotalQuestionCount) {
                        AppController.getFirebaseHelper().getWeeklyQuestionList().child(AppController.getFirebaseHelper().getFirebaseAuth().getUid())
                                .child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                subjectQuestionCount = 0;
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    } else {
                        subjectQuestionCount = 0;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    SharedPreferences pref;
    private SessionManager sessionManager;
    private String PremiumType = "";
    private AdRequest adRequest;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




        binding.backbtn.setOnClickListener(this);
        binding.leaderboardfromweekly.setOnClickListener(this);
        binding.closeCountdown.setVisibility(View.GONE);
        binding.txtContestClose.setVisibility(View.GONE);

        adRequest = new AdRequest.Builder().build();
        loadAd();

        pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
        PremiumType = pref.getString("premiumType", null);


        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                if (closingTimer != null) {
                    closingTimer.cancel();
                }
                if (startTimer != null) {
                    startTimer.cancel();
                }

                FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_weekly_contest_to_nav_home, new Bundle());

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        if (PremiumType.equals("p0")) {
            binding.coinview.setVisibility(View.VISIBLE);
        } else {
            binding.coinview.setVisibility(View.GONE);
        }

        binding.tvfull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.spinKit.setVisibility(View.VISIBLE);
                binding.tvfull.setClickable(false);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        CheckStatus();
                    }
                }, 5000);


            }
        });


    }


    private CountDownTimer startTimer;

    private void CountDown() {
        if (closingTimer != null) {
            closingTimer.cancel();
        }
        binding.closeCountdown.setVisibility(View.GONE);
        binding.txtContestClose.setVisibility(View.GONE);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            Date event_date = dateFormat.parse(EVENT_DATE_TIME);
            Date present_date = dateFormat.parse(PresentDate);
            long diff = event_date.getTime() - present_date.getTime();

            startTimer = new CountDownTimer(diff, 1000) {
                public void onTick(long millisUntilFinished) {

                    long Days = millisUntilFinished / (24 * 60 * 60 * 1000);
                    long Hours = millisUntilFinished / (60 * 60 * 1000) % 24;
                    long Minutes = millisUntilFinished / (60 * 1000) % 60;
                    long Seconds = millisUntilFinished / 1000 % 60;
                    //
                    binding.countdown.setText(String.format("%02d", Days) + "d:\t" + String.format("%02d", Hours) + "h:\t"
                            + String.format("%02d", Minutes) + "m:\t" + String.format("%02d", Seconds) + "s");
                }

                public void onFinish() {

                    binding.countdown.setText("Live");
                    if (startTimer != null) {
                        startTimer.cancel();
                    }

                    contestCloseTimer();

                }
            }.start();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private CountDownTimer closingTimer;

    private void contestCloseTimer() {
        binding.closeCountdown.setVisibility(View.VISIBLE);
        binding.txtContestClose.setVisibility(View.VISIBLE);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            Date event_end_date = dateFormat.parse(EVENT_END_DATE_TIME);
            Date present_date = dateFormat.parse(PresentDate);
            long diff = event_end_date.getTime() - present_date.getTime();
            closingTimer = new CountDownTimer(diff, 1000) {
                public void onTick(long millisUntilFinished) {
                    long Hours = millisUntilFinished / (60 * 60 * 1000) % 24;
                    long Minutes = millisUntilFinished / (60 * 1000) % 60;
                    long Seconds = millisUntilFinished / 1000 % 60;
                    //
                    binding.closeCountdown.setText(String.format("%02d", Hours) + "h:\t"
                            + String.format("%02d", Minutes) + "m:\t" + String.format("%02d", Seconds) + "s");
                }

                public void onFinish() {
                    if (closingTimer != null) {
                        closingTimer.cancel();
                    }


                    binding.countdown.setText("Close");
                    binding.closeCountdown.setVisibility(View.GONE);
                    binding.txtContestClose.setVisibility(View.GONE);
                    postContestData();
                }
            }.start();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        Bundle bundle = new Bundle();
        switch (id) {

            case R.id.backbtn:
                FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_weekly_contest_to_nav_home, bundle);
                if (closingTimer != null) {
                    closingTimer.cancel();
                }
                if (startTimer != null) {
                    startTimer.cancel();
                }
                break;
            case R.id.leaderboardfromweekly:
                bundle.putString("type", "WeeklyContest");
                FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_weekly_contest_to_LeaderBoard, bundle);
                break;

        }
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

    private void showpointDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }

        if (getActivity() != null) {
            dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_point);
            dialog.setCanceledOnTouchOutside(true);

            Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
            Button btn_earn_points = dialog.findViewById(R.id.btn_ok);
            Button btn_upgrade_to_Premium = dialog.findViewById(R.id.btnToPremium);


            btn_earn_points.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    Bundle bundle = new Bundle();
                    FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_weekly_contest_to_getRewarded, bundle);
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            btn_upgrade_to_Premium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    Bundle bundle = new Bundle();
                    FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_weekly_contest_to_premiumProcessFragment, bundle);
                }
            });

            dialog.show();
        }
    }

    private RewardedAd rewardedAd;

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
    }

    private void showDialog() {


        if (dialog != null) {
            dialog.dismiss();
        }
        if (getActivity() != null) {
            dialog = new Dialog(getActivity());
            selectList = new ArrayList<>();
            subjectList = new ArrayList<>();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.select_exam_subject_view);
            dialog.setCanceledOnTouchOutside(false);

            RecyclerView recyclerView = dialog.findViewById(R.id.exam_subject_list);
            spinKitView = dialog.findViewById(R.id.spin_kit);


            getSubjectData();
            recyclerExamSubjectAdapter = new RecyclerWeeklyContestSubjectAdapter(getContext(), subjectList, WeeklyContest.this);
            recyclerExamSubjectAdapter.setHasStableIds(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(recyclerExamSubjectAdapter);

            final Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
            final Button btn_ok = dialog.findViewById(R.id.btn_ok);

            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectList != null && selectList.size() == 4) {
                        dialog.dismiss();
                        String list = new Gson().toJson(selectList);
                        String previousExamQuestionList = new Gson().toJson(QuestionList);
                        Bundle bundle = new Bundle();
                        bundle.putString("selectlist", list);
                        bundle.putLong("duration", 3600000);
                        bundle.putString("state", "full_exam");
                        bundle.putString("previousExamQuestionList", previousExamQuestionList);

                        if (closingTimer != null) {
                            closingTimer.cancel();
                        }
                        if (startTimer != null) {
                            startTimer.cancel();
                        }

                        if (PremiumType.equals("p0")) {
                            sessionManager.setCurrentPoints(sessionManager.getTotalPoints() - 150);

                            HashMap<String, Object> point = new HashMap<>();
                            point.put("point", String.valueOf(sessionManager.getTotalPoints()));
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


                        } else {

                        }


                        HashMap<String, Object> status = new HashMap<>();
                        status.put(AppController.getFirebaseHelper().getFirebaseAuth().getCurrentUser().getUid(), true);
                        AppController.getFirebaseHelper().getWeeklyContestParticipateStatus().child("ParticipantList").updateChildren(status)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_weekly_contest_to_takeContestExamFragment, bundle);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });


                    } else {
                        new CustomMessage(getActivity(), "Select [4] subjects");
                    }
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectList.clear();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    private void getSubjectData() {
        if (getActivity() != null) {
            if (NetworkHelper.hasNetworAccess(getActivity())) {
                if (spinKitView != null) {
                    spinKitView.setVisibility(View.VISIBLE);
                }
                AppController.getFirebaseHelper().getSubject().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        subjectList.clear();
                        if (spinKitView != null) {
                            spinKitView.setVisibility(View.GONE);
                        }
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String subject = dataSnapshot1.child("subject").getValue(String.class);
                                subjectList.add(subject);
                            }
                            recyclerExamSubjectAdapter.notifyDataSetChanged();
                        } else {
                            if (dialog != null) {
                                dialog.dismiss();
                                recyclerExamSubjectAdapter.notifyDataSetChanged();
                                new CustomMessage(getActivity(), "No subject available");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        if (spinKitView != null) {
                            spinKitView.setVisibility(View.GONE);
                        }
                        if (dialog != null) {
                            dialog.dismiss();
                            recyclerExamSubjectAdapter.notifyDataSetChanged();
                            new CustomMessage(getActivity(), "No subject available");
                        }

                    }
                });
            } else {
                new CustomMessage(getActivity(), "No internet available");
            }
        }
    }

    @Override
    public void onSubjectSelect(int pos, String subject) {
        SelectItem selectItem = new SelectItem();
        selectItem.setId(pos);
        selectItem.setSubject(subject);
        selectList.add(selectItem);
    }

    @Override
    public void onSubjectDeselect(int pos, String subject) {
        for (int i = 0; i < selectList.size(); i++) {
            SelectItem item = selectList.get(i);
            if (item.getId() == pos) {
                selectList.remove(i);
            }
        }
    }
}