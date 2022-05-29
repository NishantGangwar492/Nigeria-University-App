package com.iotait.schoolapp.ui.homepage.ui.exam.fullexam;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.common.Common;
import com.iotait.schoolapp.databinding.FragmentFullExamBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.Customlog;
import com.iotait.schoolapp.helper.FragmentHelper;
import com.iotait.schoolapp.helper.NetworkHelper;
import com.iotait.schoolapp.listener.ImageClick;
import com.iotait.schoolapp.session.SessionManager;
import com.iotait.schoolapp.ui.homepage.ui.exam.adapter.QuestionAdapter;
import com.iotait.schoolapp.ui.homepage.ui.exam.models.SelectItem;
import com.iotait.schoolapp.ui.homepage.ui.question.details.models.QuestionItem;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;

import java.io.File;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FullExamFragment extends Fragment implements View.OnClickListener, ImageClick {

    private FragmentFullExamBinding fullExamBinding;
    private Bundle bundle;
    private List<SelectItem> selectItemList;
    private String pass_str = "";
    private String state = "";
    private long duration = 0;
    long min;
    long sec;

    private QuestionAdapter adapter;
    private List<QuestionItem> questionItems;
    private List<QuestionItem> questionItemsTemp;
    private List<QuestionItem> questionItemsTemp1;
    private List<QuestionItem> qlist;

    String PremiumType = "";
    boolean isPremium;
    String expireDate = "";
    Date date1 = null;
    Date date2 = null;
    private SessionManager sessionManager;
    private Dialog dialog;

    private static final String FORMAT = "%02d m %02d s";


    public FullExamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fullExamBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_full_exam, container, false);

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        PremiumType = pref.getString("premiumType", null);
        isPremium = pref.getBoolean("isPremium", false);
        expireDate = pref.getString("expire_date", null);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMM yyyy hh:mm a");
        String c_date = new SimpleDateFormat("E, dd MMM yyyy hh:mm a", Locale.getDefault()).format(new Date());

        if (expireDate != null && !expireDate.equals("")) {
            try {
                date1 = simpleDateFormat.parse(c_date);
                date2 = simpleDateFormat.parse(expireDate);


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return fullExamBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sessionManager = new SessionManager(getContext());
        fullExamBinding.includeToolbar.toolbarTitle.setText("FULL EXAM");
        ((AppCompatActivity) getActivity()).setSupportActionBar(fullExamBinding.includeToolbar.toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        fullExamBinding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                onCountdownFinish();
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(fullExamBinding.getRoot(), R.id.action_fullExamFragment_to_takeExamFragment, bundle);
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                onCountdownFinish();
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(fullExamBinding.getRoot(), R.id.action_fullExamFragment_to_takeExamFragment, bundle);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        Common.answerList=new ArrayList<>();
        bundle = getArguments();
        pass_str = bundle.getString("selectlist");
        state = bundle.getString("state");
        duration = bundle.getLong("duration");
        Gson gson = new Gson();
        Type type = new TypeToken<List<SelectItem>>() {
        }.getType();
        selectItemList = gson.fromJson(pass_str, type);
        for (SelectItem selectItem : selectItemList) {
            Customlog.showlogD("DATA_ITEM", selectItem.getSubject());
        }

        questionItems = new ArrayList<>();
        questionItemsTemp = new ArrayList<>();
        questionItemsTemp1 = new ArrayList<>();
        qlist = new ArrayList<>();
        adapter = new QuestionAdapter(getContext(), questionItems, this);
        adapter.setHasStableIds(true);
        fullExamBinding.rvquestions.setLayoutManager(new LinearLayoutManager(getContext()));
        fullExamBinding.rvquestions.setAdapter(adapter);

        getQuestionForFirstSubject1forp1(selectItemList.get(0).getSubject());
        fullExamBinding.btnSubmit.setOnClickListener(this);
    }
    private void getQuestionForFirstSubject1forp1(String subject1) {
        if (NetworkHelper.hasNetworAccess(getContext())) {

            fullExamBinding.spinKit.setVisibility(View.VISIBLE);
            fullExamBinding.textWarning.setVisibility(View.VISIBLE);
            AppController.getFirebaseHelper().getQuestions()
                    .orderByChild("subject").equalTo(subject1)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            questionItemsTemp1.clear();
                            questionItemsTemp.clear();
                            if (dataSnapshot.exists()) {
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

                                String explain_image="";
                                String optiona_image="";
                                String optionb_image="";
                                String optionc_image="";
                                String optiond_image="";
                                String question_image="";
                                String scenario_image="";

                                List<String> uid = new ArrayList<>();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    if (dataSnapshot1.child("answer").getValue() != null) {
                                        answer = dataSnapshot1.child("answer").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("explaination").getValue() != null) {
                                        explaination = dataSnapshot1.child("explaination").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("options").child("optionA").getValue() != null) {
                                        optionA = dataSnapshot1.child("options").child("optionA").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("options").child("optionB").getValue() != null) {
                                        optionB = dataSnapshot1.child("options").child("optionB").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("options").child("optionC").getValue() != null) {
                                        optionC = dataSnapshot1.child("options").child("optionC").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("options").child("optionD").getValue() != null) {
                                        optionD = dataSnapshot1.child("options").child("optionD").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("prepareExam").getValue(Boolean.class) != null) {
                                        prepareExam = dataSnapshot1.child("prepareExam").getValue(Boolean.class);
                                    }
                                    if (dataSnapshot1.child("question").getValue() != null) {
                                        question = dataSnapshot1.child("question").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("scenario").getValue() != null) {
                                        scenario = dataSnapshot1.child("scenario").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("studyQuestion").getValue(Boolean.class) != null) {
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
                                    }
                                    if (dataSnapshot1.child("optiona_image").getValue() != null) {
                                        optiona_image = dataSnapshot1.child("options").child("optiona_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("optionb_image").getValue() != null) {
                                        optionb_image = dataSnapshot1.child("options").child("optionb_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("optionc_image").getValue() != null) {
                                        optionc_image = dataSnapshot1.child("options").child("optionc_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("optiond_image").getValue() != null) {
                                        optiond_image = dataSnapshot1.child("options").child("optiond_image").getValue(String.class);
                                    }

                                    if (dataSnapshot1.child("question_image").getValue() != null) {
                                        question_image = dataSnapshot1.child("question_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("scenario_image").getValue() != null) {
                                        scenario_image = dataSnapshot1.child("scenario_image").getValue(String.class);
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


                                    if (!uid.contains(questionid)) {
                                        questionItemsTemp1.add(questionItem);
                                        uid.add(questionid);
                                    }

                                }
                                Log.d("TAG", "onDataChange: " + questionItemsTemp1.size());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            fullExamBinding.spinKit.setVisibility(View.GONE);
                            fullExamBinding.textWarning.setVisibility(View.GONE);
                            questionItems.clear();
                            adapter.notifyDataSetChanged();
                        }
                    });


            AppController.getFirebaseHelper().getWeeklycontestQuestions()
                    .orderByChild("subject").equalTo(subject1)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            qlist.clear();
                            if (dataSnapshot.exists()) {
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


                                List<String> uid = new ArrayList<>();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    if (dataSnapshot1.child("answer").getValue() != null) {
                                        answer = dataSnapshot1.child("answer").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("explaination").getValue() != null) {
                                        explaination = dataSnapshot1.child("explaination").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("options").child("optionA").getValue() != null) {
                                        optionA = dataSnapshot1.child("options").child("optionA").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("options").child("optionB").getValue() != null) {
                                        optionB = dataSnapshot1.child("options").child("optionB").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("options").child("optionC").getValue() != null) {
                                        optionC = dataSnapshot1.child("options").child("optionC").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("options").child("optionD").getValue() != null) {
                                        optionD = dataSnapshot1.child("options").child("optionD").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("prepareExam").getValue(Boolean.class) != null) {
                                        prepareExam = dataSnapshot1.child("prepareExam").getValue(Boolean.class);
                                    }
                                    if (dataSnapshot1.child("question").getValue() != null) {
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
                                    }
                                    if (dataSnapshot1.child("optiona_image").getValue() != null) {
                                        optiona_image = dataSnapshot1.child("options").child("optiona_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("optionb_image").getValue() != null) {
                                        optionb_image = dataSnapshot1.child("options").child("optionb_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("optionc_image").getValue() != null) {
                                        optionc_image = dataSnapshot1.child("options").child("optionc_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("optiond_image").getValue() != null) {
                                        optiond_image = dataSnapshot1.child("options").child("optiond_image").getValue(String.class);
                                    }

                                    if (dataSnapshot1.child("question_image").getValue() != null) {
                                        question_image = dataSnapshot1.child("question_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("scenario_image").getValue() != null) {
                                        scenario_image = dataSnapshot1.child("scenario_image").getValue(String.class);
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


                                    if (!uid.contains(questionid)) {
                                        questionItemsTemp1.add(questionItem);
                                        uid.add(questionid);
                                    }

                                }
                                Log.d("TAG", "onDataChange: " + questionItemsTemp1.size());
                                for (int i = 0; i < questionItemsTemp1.size(); i++) {
                                    Random randomizer = new Random();
                                    QuestionItem random = questionItemsTemp1.get(randomizer.nextInt(questionItemsTemp1.size()));
                                    qlist.add(random);
                                }
                                HashSet<QuestionItem> hashSet = new HashSet<QuestionItem>();
                                hashSet.addAll(qlist);
                                qlist.clear();
                                qlist.addAll(hashSet);
                                if (qlist.size() > 15) {
                                    for (int i = 0; i < 15; i++) {
                                        QuestionItem questionItem = qlist.get(i);
                                        questionItemsTemp.add(questionItem);
                                    }
                                } else {
                                    questionItemsTemp.addAll(qlist);
                                }
                                getQuestionForFirstSubject2(selectItemList.get(1).getSubject());

                            } else {
                                getQuestionForFirstSubject2(selectItemList.get(1).getSubject());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            fullExamBinding.spinKit.setVisibility(View.GONE);
                            fullExamBinding.textWarning.setVisibility(View.GONE);
                            questionItems.clear();
                            adapter.notifyDataSetChanged();
                        }
                    });


        } else {
            fullExamBinding.spinKit.setVisibility(View.VISIBLE);
            fullExamBinding.textWarning.setVisibility(View.VISIBLE);
            AppController.getFirebaseHelper().getQuestions()
                    .orderByChild("subject").equalTo(subject1)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            questionItemsTemp1.clear();
                            questionItemsTemp.clear();
                            if (dataSnapshot.exists()) {
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

                                List<String> uid = new ArrayList<>();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    if (dataSnapshot1.child("answer") != null) {
                                        answer = dataSnapshot1.child("answer").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("explaination").getValue() != null) {
                                        explaination = dataSnapshot1.child("explaination").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("options").child("optionA").getValue() != null) {
                                        optionA = dataSnapshot1.child("options").child("optionA").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("options").child("optionB").getValue() != null) {
                                        optionB = dataSnapshot1.child("options").child("optionB").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("options").child("optionC").getValue() != null) {
                                        optionC = dataSnapshot1.child("options").child("optionC").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("options").child("optionD").getValue() != null) {
                                        optionD = dataSnapshot1.child("options").child("optionD").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("prepareExam").getValue(Boolean.class) != null) {
                                        prepareExam = dataSnapshot1.child("prepareExam").getValue(Boolean.class);
                                    }
                                    if (dataSnapshot1.child("question").getValue() != null) {
                                        question = dataSnapshot1.child("question").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("scenario").getValue() != null) {
                                        scenario = dataSnapshot1.child("scenario").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("studyQuestion").getValue(Boolean.class) != null) {
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
                                    }
                                    if (dataSnapshot1.child("optiona_image").getValue() != null) {
                                        optiona_image = dataSnapshot1.child("options").child("optiona_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("optionb_image").getValue() != null) {
                                        optionb_image = dataSnapshot1.child("options").child("optionb_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("optionc_image").getValue() != null) {
                                        optionc_image = dataSnapshot1.child("options").child("optionc_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("optiond_image").getValue() != null) {
                                        optiond_image = dataSnapshot1.child("options").child("optiond_image").getValue(String.class);
                                    }

                                    if (dataSnapshot1.child("question_image").getValue() != null) {
                                        question_image = dataSnapshot1.child("question_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("scenario_image").getValue() != null) {
                                        scenario_image = dataSnapshot1.child("scenario_image").getValue(String.class);
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


                                    if (!uid.contains(questionid)) {
                                        questionItemsTemp1.add(questionItem);
                                        uid.add(questionid);
                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            fullExamBinding.spinKit.setVisibility(View.GONE);
                            fullExamBinding.textWarning.setVisibility(View.GONE);
                            questionItems.clear();
                            adapter.notifyDataSetChanged();
                        }
                    });

            AppController.getFirebaseHelper().getWeeklycontestQuestions()
                    .orderByChild("subject").equalTo(subject1)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            qlist.clear();
                            if (dataSnapshot.exists()) {
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


                                List<String> uid = new ArrayList<>();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    if (dataSnapshot1.child("answer").getValue() != null) {
                                        answer = dataSnapshot1.child("answer").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("explaination").getValue() != null) {
                                        explaination = dataSnapshot1.child("explaination").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("options").child("optionA").getValue() != null) {
                                        optionA = dataSnapshot1.child("options").child("optionA").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("options").child("optionB").getValue() != null) {
                                        optionB = dataSnapshot1.child("options").child("optionB").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("options").child("optionC").getValue() != null) {
                                        optionC = dataSnapshot1.child("options").child("optionC").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("options").child("optionD").getValue() != null) {
                                        optionD = dataSnapshot1.child("options").child("optionD").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("prepareExam").getValue(Boolean.class) != null) {
                                        prepareExam = dataSnapshot1.child("prepareExam").getValue(Boolean.class);
                                    }
                                    if (dataSnapshot1.child("question").getValue() != null) {
                                        question = dataSnapshot1.child("question").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("scenario").getValue() != null) {
                                        scenario = dataSnapshot1.child("scenario").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("studyQuestion").getValue(Boolean.class) != null) {
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
                                    }
                                    if (dataSnapshot1.child("optiona_image").getValue() != null) {
                                        optiona_image = dataSnapshot1.child("options").child("optiona_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("optionb_image").getValue() != null) {
                                        optionb_image = dataSnapshot1.child("options").child("optionb_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("optionc_image").getValue() != null) {
                                        optionc_image = dataSnapshot1.child("options").child("optionc_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("optiond_image").getValue() != null) {
                                        optiond_image = dataSnapshot1.child("options").child("optiond_image").getValue(String.class);
                                    }

                                    if (dataSnapshot1.child("question_image").getValue() != null) {
                                        question_image = dataSnapshot1.child("question_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("scenario_image").getValue() != null) {
                                        scenario_image = dataSnapshot1.child("scenario_image").getValue(String.class);
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


                                    if (!uid.contains(questionid)) {
                                        questionItemsTemp1.add(questionItem);
                                        uid.add(questionid);
                                    }

                                }

                                for (int i = 0; i < questionItemsTemp1.size(); i++) {
                                    Random randomizer = new Random();
                                    QuestionItem random = questionItemsTemp1.get(randomizer.nextInt(questionItemsTemp1.size()));
                                    qlist.add(random);
                                }
                                HashSet<QuestionItem> hashSet = new HashSet<QuestionItem>();
                                hashSet.addAll(qlist);
                                qlist.clear();
                                qlist.addAll(hashSet);
                                if (qlist.size() > 15) {
                                    for (int i = 0; i < 15; i++) {
                                        QuestionItem questionItem = qlist.get(i);
                                        questionItemsTemp.add(questionItem);
                                    }
                                } else {
                                    questionItemsTemp.addAll(qlist);
                                }
                                getQuestionForFirstSubject2(selectItemList.get(1).getSubject());

                            } else {
                                getQuestionForFirstSubject2(selectItemList.get(1).getSubject());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            fullExamBinding.spinKit.setVisibility(View.GONE);
                            fullExamBinding.textWarning.setVisibility(View.GONE);
                            questionItems.clear();
                            adapter.notifyDataSetChanged();
                        }
                    });
        }

    }

    private void getQuestionForFirstSubject2(String subject2) {
        AppController.getFirebaseHelper().getQuestions()
                .orderByChild("subject").equalTo(subject2)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        questionItemsTemp1.clear();
                        qlist.clear();
                        if (dataSnapshot.exists()) {
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

                            String explain_image="";
                            String optiona_image="";
                            String optionb_image="";
                            String optionc_image="";
                            String optiond_image="";
                            String question_image="";
                            String scenario_image="";

                            List<String> uid = new ArrayList<>();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                if (dataSnapshot1.child("answer").getValue() != null) {
                                    answer = dataSnapshot1.child("answer").getValue(String.class);
                                }
                                if (dataSnapshot1.child("explaination").getValue() != null) {
                                    explaination = dataSnapshot1.child("explaination").getValue(String.class);
                                }
                                if (dataSnapshot1.child("options").child("optionA").getValue() != null) {
                                    optionA = dataSnapshot1.child("options").child("optionA").getValue(String.class);
                                }
                                if (dataSnapshot1.child("options").child("optionB").getValue() != null) {
                                    optionB = dataSnapshot1.child("options").child("optionB").getValue(String.class);
                                }
                                if (dataSnapshot1.child("options").child("optionC").getValue() != null) {
                                    optionC = dataSnapshot1.child("options").child("optionC").getValue(String.class);
                                }
                                if (dataSnapshot1.child("options").child("optionD").getValue() != null) {
                                    optionD = dataSnapshot1.child("options").child("optionD").getValue(String.class);
                                }
                                if (dataSnapshot1.child("prepareExam").getValue(Boolean.class) != null) {
                                    prepareExam = dataSnapshot1.child("prepareExam").getValue(Boolean.class);
                                }
                                if (dataSnapshot1.child("question").getValue() != null) {
                                    question = dataSnapshot1.child("question").getValue(String.class);
                                }
                                if (dataSnapshot1.child("scenario").getValue() != null) {
                                    scenario = dataSnapshot1.child("scenario").getValue(String.class);
                                }
                                if (dataSnapshot1.child("studyQuestion").getValue(Boolean.class) != null) {
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
                                }
                                if (dataSnapshot1.child("optiona_image").getValue() != null) {
                                    optiona_image = dataSnapshot1.child("options").child("optiona_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optionb_image").getValue() != null) {
                                    optionb_image = dataSnapshot1.child("options").child("optionb_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optionc_image").getValue() != null) {
                                    optionc_image = dataSnapshot1.child("options").child("optionc_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optiond_image").getValue() != null) {
                                    optiond_image = dataSnapshot1.child("options").child("optiond_image").getValue(String.class);
                                }

                                if (dataSnapshot1.child("question_image").getValue() != null) {
                                    question_image = dataSnapshot1.child("question_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("scenario_image").getValue() != null) {
                                    scenario_image = dataSnapshot1.child("scenario_image").getValue(String.class);
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

                                if (!uid.contains(questionid)) {
                                    questionItemsTemp1.add(questionItem);
                                    uid.add(questionid);
                                }
                            }
                            Log.d("TAG", "onDataChange: " + questionItemsTemp1.size());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        AppController.getFirebaseHelper().getWeeklycontestQuestions()
                .orderByChild("subject").equalTo(subject2)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        qlist.clear();
                        if (dataSnapshot.exists()) {
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


                            List<String> uid = new ArrayList<>();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                if (dataSnapshot1.child("answer").getValue() != null) {
                                    answer = dataSnapshot1.child("answer").getValue(String.class);
                                }
                                if (dataSnapshot1.child("explaination").getValue() != null) {
                                    explaination = dataSnapshot1.child("explaination").getValue(String.class);
                                }
                                if (dataSnapshot1.child("options").child("optionA").getValue() != null) {
                                    optionA = dataSnapshot1.child("options").child("optionA").getValue(String.class);
                                }
                                if (dataSnapshot1.child("options").child("optionB").getValue() != null) {
                                    optionB = dataSnapshot1.child("options").child("optionB").getValue(String.class);
                                }
                                if (dataSnapshot1.child("options").child("optionC").getValue() != null) {
                                    optionC = dataSnapshot1.child("options").child("optionC").getValue(String.class);
                                }
                                if (dataSnapshot1.child("options").child("optionD").getValue() != null) {
                                    optionD = dataSnapshot1.child("options").child("optionD").getValue(String.class);
                                }
                                if (dataSnapshot1.child("prepareExam").getValue(Boolean.class) != null) {
                                    prepareExam = dataSnapshot1.child("prepareExam").getValue(Boolean.class);
                                }
                                if (dataSnapshot1.child("question").getValue() != null) {
                                    question = dataSnapshot1.child("question").getValue(String.class);
                                }
                                if (dataSnapshot1.child("scenario").getValue() != null) {
                                    scenario = dataSnapshot1.child("scenario").getValue(String.class);
                                }
                                if (dataSnapshot1.child("studyQuestion").getValue(Boolean.class) != null) {
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
                                }
                                if (dataSnapshot1.child("optiona_image").getValue() != null) {
                                    optiona_image = dataSnapshot1.child("options").child("optiona_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optionb_image").getValue() != null) {
                                    optionb_image = dataSnapshot1.child("options").child("optionb_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optionc_image").getValue() != null) {
                                    optionc_image = dataSnapshot1.child("options").child("optionc_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optiond_image").getValue() != null) {
                                    optiond_image = dataSnapshot1.child("options").child("optiond_image").getValue(String.class);
                                }

                                if (dataSnapshot1.child("question_image").getValue() != null) {
                                    question_image = dataSnapshot1.child("question_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("scenario_image").getValue() != null) {
                                    scenario_image = dataSnapshot1.child("scenario_image").getValue(String.class);
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


                                if (!uid.contains(questionid)) {
                                    questionItemsTemp1.add(questionItem);
                                    uid.add(questionid);
                                }

                            }

                            Log.d("TAG", "onDataChange: " + questionItemsTemp1.size());
                            for (int i = 0; i < questionItemsTemp1.size(); i++) {
                                Random randomizer = new Random();
                                QuestionItem random = questionItemsTemp1.get(randomizer.nextInt(questionItemsTemp1.size()));
                                qlist.add(random);
                            }
                            HashSet<QuestionItem> hashSet = new HashSet<QuestionItem>();
                            hashSet.addAll(qlist);
                            qlist.clear();
                            qlist.addAll(hashSet);
                            if (qlist.size() > 15) {
                                for (int i = 0; i < 15; i++) {
                                    QuestionItem questionItem = qlist.get(i);
                                    questionItemsTemp.add(questionItem);
                                }
                            } else {
                                questionItemsTemp.addAll(qlist);
                            }

                            getQuestionForFirstSubject3(selectItemList.get(2).getSubject());
                        } else {
                            getQuestionForFirstSubject3(selectItemList.get(2).getSubject());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        fullExamBinding.spinKit.setVisibility(View.GONE);
                        fullExamBinding.textWarning.setVisibility(View.GONE);
                        questionItems.clear();
                        adapter.notifyDataSetChanged();
                    }
                });


    }

    private void getQuestionForFirstSubject3(String subject3) {
        AppController.getFirebaseHelper().getQuestions()
                .orderByChild("subject").equalTo(subject3)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        questionItemsTemp1.clear();
                        qlist.clear();
                        if (dataSnapshot.exists()) {
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

                            String explain_image="";
                            String optiona_image="";
                            String optionb_image="";
                            String optionc_image="";
                            String optiond_image="";
                            String question_image="";
                            String scenario_image="";

                            List<String> uid = new ArrayList<>();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                if (dataSnapshot1.child("answer").getValue() != null) {
                                    answer = dataSnapshot1.child("answer").getValue(String.class);
                                }
                                if (dataSnapshot1.child("explaination").getValue() != null) {
                                    explaination = dataSnapshot1.child("explaination").getValue(String.class);
                                }
                                if (dataSnapshot1.child("options").child("optionA").getValue() != null) {
                                    optionA = dataSnapshot1.child("options").child("optionA").getValue(String.class);
                                }
                                if (dataSnapshot1.child("options").child("optionB").getValue() != null) {
                                    optionB = dataSnapshot1.child("options").child("optionB").getValue(String.class);
                                }
                                if (dataSnapshot1.child("options").child("optionC").getValue() != null) {
                                    optionC = dataSnapshot1.child("options").child("optionC").getValue(String.class);
                                }
                                if (dataSnapshot1.child("options").child("optionD").getValue() != null) {
                                    optionD = dataSnapshot1.child("options").child("optionD").getValue(String.class);
                                }
                                if (dataSnapshot1.child("prepareExam").getValue(Boolean.class) != null) {
                                    prepareExam = dataSnapshot1.child("prepareExam").getValue(Boolean.class);
                                }
                                if (dataSnapshot1.child("question").getValue() != null) {
                                    question = dataSnapshot1.child("question").getValue(String.class);
                                }
                                if (dataSnapshot1.child("scenario").getValue() != null) {
                                    scenario = dataSnapshot1.child("scenario").getValue(String.class);
                                }
                                if (dataSnapshot1.child("studyQuestion").getValue(Boolean.class) != null) {
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
                                }
                                if (dataSnapshot1.child("optiona_image").getValue() != null) {
                                    optiona_image = dataSnapshot1.child("options").child("optiona_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optionb_image").getValue() != null) {
                                    optionb_image = dataSnapshot1.child("options").child("optionb_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optionc_image").getValue() != null) {
                                    optionc_image = dataSnapshot1.child("options").child("optionc_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optiond_image").getValue() != null) {
                                    optiond_image = dataSnapshot1.child("options").child("optiond_image").getValue(String.class);
                                }

                                if (dataSnapshot1.child("question_image").getValue() != null) {
                                    question_image = dataSnapshot1.child("question_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("scenario_image").getValue() != null) {
                                    scenario_image = dataSnapshot1.child("scenario_image").getValue(String.class);
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

                                if (!uid.contains(questionid)) {
                                    questionItemsTemp1.add(questionItem);
                                    uid.add(questionid);
                                }
                            }
                            Log.d("TAG", "onDataChange: " + questionItemsTemp1.size());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        AppController.getFirebaseHelper().getWeeklycontestQuestions()
                .orderByChild("subject").equalTo(subject3)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        qlist.clear();
                        if (dataSnapshot.exists()) {
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


                            List<String> uid = new ArrayList<>();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                if (dataSnapshot1.child("answer").getValue() != null) {
                                    answer = dataSnapshot1.child("answer").getValue(String.class);
                                }
                                if (dataSnapshot1.child("explaination").getValue() != null) {
                                    explaination = dataSnapshot1.child("explaination").getValue(String.class);
                                }
                                if (dataSnapshot1.child("options").child("optionA").getValue() != null) {
                                    optionA = dataSnapshot1.child("options").child("optionA").getValue(String.class);
                                }
                                if (dataSnapshot1.child("options").child("optionB").getValue() != null) {
                                    optionB = dataSnapshot1.child("options").child("optionB").getValue(String.class);
                                }
                                if (dataSnapshot1.child("options").child("optionC").getValue() != null) {
                                    optionC = dataSnapshot1.child("options").child("optionC").getValue(String.class);
                                }
                                if (dataSnapshot1.child("options").child("optionD").getValue() != null) {
                                    optionD = dataSnapshot1.child("options").child("optionD").getValue(String.class);
                                }
                                if (dataSnapshot1.child("prepareExam").getValue(Boolean.class) != null) {
                                    prepareExam = dataSnapshot1.child("prepareExam").getValue(Boolean.class);
                                }
                                if (dataSnapshot1.child("question").getValue() != null) {
                                    question = dataSnapshot1.child("question").getValue(String.class);
                                }
                                if (dataSnapshot1.child("scenario").getValue() != null) {
                                    scenario = dataSnapshot1.child("scenario").getValue(String.class);
                                }
                                if (dataSnapshot1.child("studyQuestion").getValue(Boolean.class) != null) {
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
                                }
                                if (dataSnapshot1.child("optiona_image").getValue() != null) {
                                    optiona_image = dataSnapshot1.child("options").child("optiona_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optionb_image").getValue() != null) {
                                    optionb_image = dataSnapshot1.child("options").child("optionb_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optionc_image").getValue() != null) {
                                    optionc_image = dataSnapshot1.child("options").child("optionc_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optiond_image").getValue() != null) {
                                    optiond_image = dataSnapshot1.child("options").child("optiond_image").getValue(String.class);
                                }

                                if (dataSnapshot1.child("question_image").getValue() != null) {
                                    question_image = dataSnapshot1.child("question_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("scenario_image").getValue() != null) {
                                    scenario_image = dataSnapshot1.child("scenario_image").getValue(String.class);
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


                                if (!uid.contains(questionid)) {
                                    questionItemsTemp1.add(questionItem);
                                    uid.add(questionid);
                                }

                            }
                            Log.d("TAG", "onDataChange: " + questionItemsTemp1.size());
                            for (int i = 0; i < questionItemsTemp1.size(); i++) {
                                Random randomizer = new Random();
                                QuestionItem random = questionItemsTemp1.get(randomizer.nextInt(questionItemsTemp1.size()));
                                qlist.add(random);
                            }

                            HashSet<QuestionItem> hashSet = new HashSet<QuestionItem>();
                            hashSet.addAll(qlist);
                            qlist.clear();
                            qlist.addAll(hashSet);
                            if (qlist.size() > 15) {
                                for (int i = 0; i < 15; i++) {
                                    QuestionItem questionItem = qlist.get(i);
                                    questionItemsTemp.add(questionItem);
                                }
                            } else {
                                questionItemsTemp.addAll(qlist);
                            }


                            getQuestionForFirstSubject4(selectItemList.get(3).getSubject());
                        } else {
                            getQuestionForFirstSubject3(selectItemList.get(3).getSubject());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        fullExamBinding.spinKit.setVisibility(View.GONE);
                        fullExamBinding.textWarning.setVisibility(View.GONE);
                        questionItems.clear();
                        adapter.notifyDataSetChanged();
                    }
                });


    }

    private void getQuestionForFirstSubject4(String subject4) {
        AppController.getFirebaseHelper().getQuestions()
                .orderByChild("subject").equalTo(subject4)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        fullExamBinding.spinKit.setVisibility(View.GONE);
                        fullExamBinding.textWarning.setVisibility(View.GONE);
                        questionItemsTemp1.clear();
                        qlist.clear();
                        if (dataSnapshot.exists()) {
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

                            String explain_image="";
                            String optiona_image="";
                            String optionb_image="";
                            String optionc_image="";
                            String optiond_image="";
                            String question_image="";
                            String scenario_image="";

                            List<String> uid = new ArrayList<>();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                if (dataSnapshot1.child("answer").getValue() != null) {
                                    answer = dataSnapshot1.child("answer").getValue(String.class);
                                }
                                if (dataSnapshot1.child("explaination").getValue() != null) {
                                    explaination = dataSnapshot1.child("explaination").getValue(String.class);
                                }
                                if (dataSnapshot1.child("options").child("optionA").getValue() != null) {
                                    optionA = dataSnapshot1.child("options").child("optionA").getValue(String.class);
                                }
                                if (dataSnapshot1.child("options").child("optionB").getValue() != null) {
                                    optionB = dataSnapshot1.child("options").child("optionB").getValue(String.class);
                                }
                                if (dataSnapshot1.child("options").child("optionC").getValue() != null) {
                                    optionC = dataSnapshot1.child("options").child("optionC").getValue(String.class);
                                }
                                if (dataSnapshot1.child("options").child("optionD").getValue() != null) {
                                    optionD = dataSnapshot1.child("options").child("optionD").getValue(String.class);
                                }
                                if (dataSnapshot1.child("prepareExam").getValue(Boolean.class) != null) {
                                    prepareExam = dataSnapshot1.child("prepareExam").getValue(Boolean.class);
                                }
                                if (dataSnapshot1.child("question").getValue() != null) {
                                    question = dataSnapshot1.child("question").getValue(String.class);
                                }
                                if (dataSnapshot1.child("scenario").getValue() != null) {
                                    scenario = dataSnapshot1.child("scenario").getValue(String.class);
                                }
                                if (dataSnapshot1.child("studyQuestion").getValue(Boolean.class) != null) {
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
                                }
                                if (dataSnapshot1.child("optiona_image").getValue() != null) {
                                    optiona_image = dataSnapshot1.child("options").child("optiona_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optionb_image").getValue() != null) {
                                    optionb_image = dataSnapshot1.child("options").child("optionb_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optionc_image").getValue() != null) {
                                    optionc_image = dataSnapshot1.child("options").child("optionc_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optiond_image").getValue() != null) {
                                    optiond_image = dataSnapshot1.child("options").child("optiond_image").getValue(String.class);
                                }

                                if (dataSnapshot1.child("question_image").getValue() != null) {
                                    question_image = dataSnapshot1.child("question_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("scenario_image").getValue() != null) {
                                    scenario_image = dataSnapshot1.child("scenario_image").getValue(String.class);
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

                                if (!uid.contains(questionid)) {
                                    questionItemsTemp1.add(questionItem);
                                    uid.add(questionid);
                                }
                            }
                            Log.d("TAG", "onDataChange: " + questionItemsTemp1.size());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        AppController.getFirebaseHelper().getWeeklycontestQuestions()
                .orderByChild("subject").equalTo(subject4)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        qlist.clear();
                        if (dataSnapshot.exists()) {
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


                            List<String> uid = new ArrayList<>();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                if (dataSnapshot1.child("answer").getValue() != null) {
                                    answer = dataSnapshot1.child("answer").getValue(String.class);
                                }
                                if (dataSnapshot1.child("explaination").getValue() != null) {
                                    explaination = dataSnapshot1.child("explaination").getValue(String.class);
                                }
                                if (dataSnapshot1.child("options").child("optionA").getValue() != null) {
                                    optionA = dataSnapshot1.child("options").child("optionA").getValue(String.class);
                                }
                                if (dataSnapshot1.child("options").child("optionB").getValue() != null) {
                                    optionB = dataSnapshot1.child("options").child("optionB").getValue(String.class);
                                }
                                if (dataSnapshot1.child("options").child("optionC").getValue() != null) {
                                    optionC = dataSnapshot1.child("options").child("optionC").getValue(String.class);
                                }
                                if (dataSnapshot1.child("options").child("optionD").getValue() != null) {
                                    optionD = dataSnapshot1.child("options").child("optionD").getValue(String.class);
                                }
                                if (dataSnapshot1.child("prepareExam").getValue(Boolean.class) != null) {
                                    prepareExam = dataSnapshot1.child("prepareExam").getValue(Boolean.class);
                                }
                                if (dataSnapshot1.child("question").getValue() != null) {
                                    question = dataSnapshot1.child("question").getValue(String.class);
                                }
                                if (dataSnapshot1.child("scenario").getValue() != null) {
                                    scenario = dataSnapshot1.child("scenario").getValue(String.class);
                                }
                                if (dataSnapshot1.child("studyQuestion").getValue(Boolean.class) != null) {
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
                                }
                                if (dataSnapshot1.child("optiona_image").getValue() != null) {
                                    optiona_image = dataSnapshot1.child("options").child("optiona_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optionb_image").getValue() != null) {
                                    optionb_image = dataSnapshot1.child("options").child("optionb_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optionc_image").getValue() != null) {
                                    optionc_image = dataSnapshot1.child("options").child("optionc_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optiond_image").getValue() != null) {
                                    optiond_image = dataSnapshot1.child("options").child("optiond_image").getValue(String.class);
                                }

                                if (dataSnapshot1.child("question_image").getValue() != null) {
                                    question_image = dataSnapshot1.child("question_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("scenario_image").getValue() != null) {
                                    scenario_image = dataSnapshot1.child("scenario_image").getValue(String.class);
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


                                if (!uid.contains(questionid)) {
                                    questionItemsTemp1.add(questionItem);
                                    uid.add(questionid);
                                }

                            }
                            Log.d("TAG", "onDataChange: " + questionItemsTemp1.size());
                            for (int i = 0; i < questionItemsTemp1.size(); i++) {
                                Random randomizer = new Random();
                                QuestionItem random = questionItemsTemp1.get(randomizer.nextInt(questionItemsTemp1.size()));
                                qlist.add(random);
                            }
                            HashSet<QuestionItem> hashSet = new HashSet<QuestionItem>();
                            hashSet.addAll(qlist);
                            qlist.clear();
                            qlist.addAll(hashSet);
                            if (qlist.size() > 15) {
                                for (int i = 0; i < 15; i++) {
                                    QuestionItem questionItem = qlist.get(i);
                                    questionItemsTemp.add(questionItem);
                                }
                            } else {
                                questionItemsTemp.addAll(qlist);
                            }


                            questionItems.clear();
//                            questionItems.addAll(questionItemsTemp);
//                            fullExamBinding.btnSubmit.setVisibility(View.VISIBLE);
//                            fullExamBinding.txtTotalQuestion.setText("Total " + String.valueOf(questionItems.size()) + " Questions");
//                            adapter.notifyDataSetChanged();
//                            fullExamBinding.layoutTimer.setVisibility(View.VISIBLE);
//                            statrtTime();


                            if (questionItemsTemp.size() == 60) {
                                questionItems.addAll(questionItemsTemp);
                                questionItemsTemp.clear();
                                fullExamBinding.btnSubmit.setVisibility(View.VISIBLE);
                                fullExamBinding.txtTotalQuestion.setText("Total " + String.valueOf(questionItems.size()) + " Questions");
                                adapter.notifyDataSetChanged();
                                fullExamBinding.layoutTimer.setVisibility(View.VISIBLE);
                                statrtTime();
                            } else if (questionItemsTemp.size() > 60) {
                                for (int i = 0; i < 60; i++) {
                                    QuestionItem finalQuestionItem = questionItemsTemp.get(i);
                                    questionItems.add(finalQuestionItem);
                                }
                                questionItemsTemp.clear();
                                fullExamBinding.btnSubmit.setVisibility(View.VISIBLE);
                                fullExamBinding.txtTotalQuestion.setText("Total " + String.valueOf(questionItems.size()) + " Questions");
                                adapter.notifyDataSetChanged();
                                fullExamBinding.layoutTimer.setVisibility(View.VISIBLE);
                                statrtTime();
                            } else {
                                getQuestionForFirstSubject1forp1(selectItemList.get(0).getSubject());
                            }


                        } else {
                            questionItems.clear();
                            questionItems.addAll(questionItemsTemp);
                            adapter.notifyDataSetChanged();
                            statrtTime();
                            fullExamBinding.btnSubmit.setVisibility(View.VISIBLE);
                            fullExamBinding.txtTotalQuestion.setText("Total " + String.valueOf(questionItems.size()) + " Questions");
                            adapter.notifyDataSetChanged();
                            fullExamBinding.layoutTimer.setVisibility(View.VISIBLE);

                            if (questionItems.size() == 0) {
                                new CustomMessage(getActivity(), "No questions added");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    private CountDownTimer countDownTimer;
    private void statrtTime() {
        countDownTimer=new CountDownTimer(duration, 1000) { // 60 min the milli seconds here

            public void onTick(long millisUntilFinished) {

                fullExamBinding.txtRemainingTime.setText("" + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                min = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                sec = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));

            }

            public void onFinish() {
                fullExamBinding.txtRemainingTime.setText("Time is up!");
                onCountdownFinish();
                processAnswer();
            }
        }.start();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_submit) {
            if (Common.answerList.size() != 0) {
                showAlartDialog();
            } else {
                new CustomMessage(getActivity(), "Please select a answer");
            }
        }
    }

    public void showAlartDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.Dialogtheme);
        builder.setTitle("Submit  answer");
        builder.setMessage("Are you sure, you want to submit answer?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onCountdownFinish();
                processAnswer();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void processAnswer() {
        if (PremiumType.equals("p0")){
            sessionManager.setFullExamCount(sessionManager.getFullExamCount()+1);
            updateFullExamInfo(sessionManager.getFullExamCount());
        }
        long minute = 60 - min;
        long second = 60 - sec;
        // String minute= String.valueOf(min);
        // String Second=String.valueOf(sec);

        String TakeTime = "" + 0 + ":" + minute + ":" + second;

//        Date date = null;
//
//        DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
//        try {
//          date = sdf.parse(TakeTime);
//          Long time=date.getTime();
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        String str_questionlist = new Gson().toJson(questionItems);
        String list = new Gson().toJson(selectItemList);
        Bundle bundle = new Bundle();
        bundle.putString("questionlist", str_questionlist);
        bundle.putString("type", "full_exam");
        bundle.putInt("marks", (int) 400);
        bundle.putString("selectlist", list);
        bundle.putString("TakeTime", TakeTime);

        if (PremiumType.equals("p0")){
            showLeftExamDialog(bundle);
        } else {
            FragmentHelper.changeFragmet(fullExamBinding.getRoot(), R.id.action_fullExamFragment_to_examPreviewFragment, bundle);
        }
    }

    private void updateFullExamInfo(int fullExamCount) {
        HashMap<String, Object> user = new HashMap();
        user.put("fullexamcount", fullExamCount);
        AppController.getFirebaseHelper().getUsers().child(AppController.getFirebaseHelper().getFirebaseAuth().getUid()).updateChildren(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    private void showLeftExamDialog(Bundle bundle1){
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_full_exam_left_count);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        final Button btn_ok = dialog.findViewById(R.id.btn_ok);
        final TextView dialogText = dialog.findViewById(R.id.benefit_title);

        dialogText.setText("You have only " + (3-sessionManager.getFullExamCount()) + " free exam left. To get unlimited exam features. Please upgrade to premium.");

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                FragmentHelper.changeFragmet(fullExamBinding.getRoot(), R.id.action_fullExamFragment_to_examPreviewFragment, bundle1);
            }
        });
        dialog.show();
    }

    private boolean printDifference(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            if (date2.after(date1)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private List<String> localImageListOffline;
    private List<String> localImageList;

    @Override
    public void onImageClick(String link) {
        if (NetworkHelper.hasNetworAccess(getContext())) {
            showSingleImage(link);
        } else {
            showSingleImageOffline(link);
        }

    }

    private void showSingleImageOffline(String link) {
        localImageListOffline = new ArrayList<>();


        if (link != null) {
            String fileName1 = "";
            Pattern p = Pattern.compile("Question%2F(.+?)\\?alt");
            Matcher m = p.matcher(link);
            if (m.find()) {
                String spaceReuce = m.group(1).replace("%20", " ");
                fileName1 = spaceReuce;
            }
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myUNN_Post_Utme/images/", fileName1 + ".jpeg");

            Uri imageuri = Uri.fromFile(file);

            localImageListOffline.add(String.valueOf(file));
            new StfalconImageViewer.Builder<String>(getContext(), localImageListOffline, new ImageLoader<String>() {
                @Override
                public void loadImage(ImageView imageView, String imageUrl) {
                    Glide.with(getActivity()).load(imageUrl).into(imageView);
                }
            }).withStartPosition(0).withHiddenStatusBar(false).show();
        }
    }


    private void showSingleImage(String link) {
        localImageList=new ArrayList<>();
        localImageList.add(link);
        new StfalconImageViewer.Builder<String>(getContext(), localImageList, new ImageLoader<String>() {
            @Override
            public void loadImage(ImageView imageView, String imageUrl) {
                Glide.with(getActivity()).load(imageUrl).into(imageView);
            }
        }).withStartPosition(0).withHiddenStatusBar(false).show();
    }

//    @Override
//    public void onImageClick(String link) {
//        showSingleImage(link);
//    }
//    private List<String> localImageList;
//    private void showSingleImage(String link) {
//        localImageList=new ArrayList<>();
//        localImageList.add(link);
//        new StfalconImageViewer.Builder<String>(getContext(), localImageList, new ImageLoader<String>() {
//            @Override
//            public void loadImage(ImageView imageView, String imageUrl) {
//                Glide.with(getActivity()).load(imageUrl).into(imageView);
//            }
//        }).withStartPosition(0).withHiddenStatusBar(false).show();
//    }

    private void onCountdownFinish() {
        if (countDownTimer!=null){
            countDownTimer.cancel();
        }

    }
}
