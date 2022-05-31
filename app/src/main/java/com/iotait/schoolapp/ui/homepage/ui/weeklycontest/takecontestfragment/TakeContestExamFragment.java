package com.iotait.schoolapp.ui.homepage.ui.weeklycontest.takecontestfragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.common.Common;
import com.iotait.schoolapp.databinding.FragmentTakeContestExamBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.Customlog;
import com.iotait.schoolapp.helper.FragmentHelper;
import com.iotait.schoolapp.helper.NetworkHelper;
import com.iotait.schoolapp.listener.ImageClick;
import com.iotait.schoolapp.ui.homepage.ui.exam.adapter.QuestionAdapter;
import com.iotait.schoolapp.ui.homepage.ui.exam.models.SelectItem;
import com.iotait.schoolapp.ui.homepage.ui.question.details.models.QuestionItem;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TakeContestExamFragment extends Fragment implements ImageClick, View.OnClickListener {

    FragmentTakeContestExamBinding binding;

    private static final String FORMAT = "%02d m %02d s";
    long min;
    long sec;
    private Bundle bundle;
    private List<SelectItem> selectItemList;
    private List<String> previousExamQuestionList;
    private String pass_str = "";
    private String questionlist;
    private String state = "";
    private long duration = 0;
    private CountDownTimer countDownTimer;
    private QuestionAdapter adapter;
    private List<QuestionItem> questionItems;
    private List<QuestionItem> questionItemsTemp;
    private List<QuestionItem> questionItemsTemp1;
    private List<QuestionItem> qlist;
    private List<String> localImageListOffline;
    private List<String> localImageList;

    public TakeContestExamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_take_contest_exam, container, false);

        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.includeToolbar.toolbarTitle.setText("Weekly Contest EXAM");
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.includeToolbar.toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowDialogforBackfromExamPaper();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                ShowDialogforBackfromExamPaper();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);


        Common.answerList = new ArrayList<>();
        bundle = getArguments();
        pass_str = bundle.getString("selectlist");
        state = bundle.getString("state");
        duration = bundle.getLong("duration");
        questionlist = bundle.getString("previousExamQuestionList");
        Gson gson = new Gson();
        Type type = new TypeToken<List<SelectItem>>() {
        }.getType();
        selectItemList = gson.fromJson(pass_str, type);
        for (SelectItem selectItem : selectItemList) {
            Customlog.showlogD("DATA_ITEM", selectItem.getSubject());
        }
        Type type2 = new TypeToken<List<String>>() {
        }.getType();
        previousExamQuestionList = gson.fromJson(questionlist, type2);

        questionItems = new ArrayList<>();
        questionItemsTemp = new ArrayList<>();
        questionItemsTemp1 = new ArrayList<>();
        qlist = new ArrayList<>();

        adapter = new QuestionAdapter(getContext(), questionItems, this);
        adapter.setHasStableIds(true);
        binding.rvquestions.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvquestions.setAdapter(adapter);


        getQuestionForFirstSubject1(selectItemList.get(0).getSubject());


        binding.btnSubmit.setOnClickListener(this);
    }


    private void getQuestionForFirstSubject1(String subject1) {
        if (NetworkHelper.hasNetworAccess(getContext())) {
            binding.spinKit.setVisibility(View.VISIBLE);
            AppController.getFirebaseHelper().getWeeklycontestQuestions()
                    .orderByChild("subject").equalTo(subject1)
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

                                    if (dataSnapshot1.child("question") != null) {
                                        question = dataSnapshot1.child("question").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("scenario") != null) {
                                        scenario = dataSnapshot1.child("scenario").getValue(String.class);
                                    }

                                    if (dataSnapshot1.child("subject") != null) {
                                        subject = dataSnapshot1.child("subject").getValue(String.class);
                                    }

                                    if (dataSnapshot1.child("explain_image") != null) {
                                        explain_image = dataSnapshot1.child("explain_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("optiona_image") != null) {
                                        optiona_image = dataSnapshot1.child("options").child("optiona_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("optionb_image") != null) {
                                        optionb_image = dataSnapshot1.child("options").child("optionb_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("optionc_image") != null) {
                                        optionc_image = dataSnapshot1.child("options").child("optionc_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("optiond_image") != null) {
                                        optiond_image = dataSnapshot1.child("options").child("optiond_image").getValue(String.class);
                                    }

                                    if (dataSnapshot1.child("question_image") != null) {
                                        question_image = dataSnapshot1.child("question_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("scenario_image") != null) {
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

                                    //String finalQuestionid = questionid;

                                    if (!previousExamQuestionList.contains(questionid) && !uid.contains(questionid)) {
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
                            binding.spinKit.setVisibility(View.GONE);
                            questionItems.clear();
                            adapter.notifyDataSetChanged();
                        }
                    });
        } else {

        }
    }

    private void getQuestionForFirstSubject2(String subject2) {
        AppController.getFirebaseHelper().getWeeklycontestQuestions()
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

                                if (dataSnapshot1.child("question") != null) {
                                    question = dataSnapshot1.child("question").getValue(String.class);
                                }
                                if (dataSnapshot1.child("scenario") != null) {
                                    scenario = dataSnapshot1.child("scenario").getValue(String.class);
                                }

                                if (dataSnapshot1.child("subject") != null) {
                                    subject = dataSnapshot1.child("subject").getValue(String.class);
                                }

                                if (dataSnapshot1.child("explain_image") != null) {
                                    explain_image = dataSnapshot1.child("explain_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optiona_image") != null) {
                                    optiona_image = dataSnapshot1.child("options").child("optiona_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optionb_image") != null) {
                                    optionb_image = dataSnapshot1.child("options").child("optionb_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optionc_image") != null) {
                                    optionc_image = dataSnapshot1.child("options").child("optionc_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optiond_image") != null) {
                                    optiond_image = dataSnapshot1.child("options").child("optiond_image").getValue(String.class);
                                }

                                if (dataSnapshot1.child("question_image") != null) {
                                    question_image = dataSnapshot1.child("question_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("scenario_image") != null) {
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


                                if (!previousExamQuestionList.contains(questionid) && !uid.contains(questionid)) {
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

                            getQuestionForFirstSubject3(selectItemList.get(2).getSubject());
                        } else {
                            getQuestionForFirstSubject3(selectItemList.get(2).getSubject());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getQuestionForFirstSubject3(String subject3) {
        AppController.getFirebaseHelper().getWeeklycontestQuestions()
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

                                if (dataSnapshot1.child("question") != null) {
                                    question = dataSnapshot1.child("question").getValue(String.class);
                                }
                                if (dataSnapshot1.child("scenario") != null) {
                                    scenario = dataSnapshot1.child("scenario").getValue(String.class);
                                }

                                if (dataSnapshot1.child("subject") != null) {
                                    subject = dataSnapshot1.child("subject").getValue(String.class);
                                }

                                if (dataSnapshot1.child("explain_image") != null) {
                                    explain_image = dataSnapshot1.child("explain_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optiona_image") != null) {
                                    optiona_image = dataSnapshot1.child("options").child("optiona_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optionb_image") != null) {
                                    optionb_image = dataSnapshot1.child("options").child("optionb_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optionc_image") != null) {
                                    optionc_image = dataSnapshot1.child("options").child("optionc_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optiond_image") != null) {
                                    optiond_image = dataSnapshot1.child("options").child("optiond_image").getValue(String.class);
                                }

                                if (dataSnapshot1.child("question_image") != null) {
                                    question_image = dataSnapshot1.child("question_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("scenario_image") != null) {
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

                                if (!previousExamQuestionList.contains(questionid) && !uid.contains(questionid)) {
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


                            getQuestionForFirstSubject4(selectItemList.get(3).getSubject());
                        } else {
                            getQuestionForFirstSubject4(selectItemList.get(3).getSubject());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getQuestionForFirstSubject4(String subject4) {
        AppController.getFirebaseHelper().getWeeklycontestQuestions()
                .orderByChild("subject").equalTo(subject4)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        binding.spinKit.setVisibility(View.GONE);
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

                                if (dataSnapshot1.child("question") != null) {
                                    question = dataSnapshot1.child("question").getValue(String.class);
                                }
                                if (dataSnapshot1.child("scenario") != null) {
                                    scenario = dataSnapshot1.child("scenario").getValue(String.class);
                                }

                                if (dataSnapshot1.child("subject") != null) {
                                    subject = dataSnapshot1.child("subject").getValue(String.class);
                                }

                                if (dataSnapshot1.child("explain_image") != null) {
                                    explain_image = dataSnapshot1.child("explain_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optiona_image") != null) {
                                    optiona_image = dataSnapshot1.child("options").child("optiona_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optionb_image") != null) {
                                    optionb_image = dataSnapshot1.child("options").child("optionb_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optionc_image") != null) {
                                    optionc_image = dataSnapshot1.child("options").child("optionc_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("optiond_image") != null) {
                                    optiond_image = dataSnapshot1.child("options").child("optiond_image").getValue(String.class);
                                }

                                if (dataSnapshot1.child("question_image") != null) {
                                    question_image = dataSnapshot1.child("question_image").getValue(String.class);
                                }
                                if (dataSnapshot1.child("scenario_image") != null) {
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

                                if (!previousExamQuestionList.contains(questionid) && !uid.contains(questionid)) {
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
                            questionItems.clear();
                            if (questionItemsTemp.size() == 60) {
                                questionItems.addAll(questionItemsTemp);
                                questionItemsTemp.clear();
                                binding.btnSubmit.setVisibility(View.VISIBLE);
                                binding.txtTotalQuestion.setText("Total " + String.valueOf(questionItems.size()) + " Questions");
                                adapter.notifyDataSetChanged();
                                binding.layoutTimer.setVisibility(View.VISIBLE);
                                statrtTime();
                            } else if (questionItemsTemp.size() > 60) {
                                for (int i = 0; i < 60; i++) {
                                    QuestionItem finalQuestionItem = questionItemsTemp.get(i);
                                    questionItems.add(finalQuestionItem);
                                }
                                questionItemsTemp.clear();
                                binding.btnSubmit.setVisibility(View.VISIBLE);
                                binding.txtTotalQuestion.setText("Total " + String.valueOf(questionItems.size()) + " Questions");
                                adapter.notifyDataSetChanged();
                                binding.layoutTimer.setVisibility(View.VISIBLE);
                                statrtTime();
                            } else {
                                getQuestionForFirstSubject1(selectItemList.get(0).getSubject());
                            }

                        } else {
                            questionItems.clear();
                            questionItems.clear();
                            questionItems.clear();
                            questionItems.addAll(questionItemsTemp);
                            adapter.notifyDataSetChanged();
                            statrtTime();
                            binding.btnSubmit.setVisibility(View.VISIBLE);
                            binding.txtTotalQuestion.setText("Total " + String.valueOf(questionItems.size()) + " Questions");
                            adapter.notifyDataSetChanged();
                            binding.layoutTimer.setVisibility(View.VISIBLE);

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

    private void statrtTime() {
        countDownTimer = new CountDownTimer(duration, 1000) { // 60 min the milli seconds here

            public void onTick(long millisUntilFinished) {

                binding.txtRemainingTime.setText("" + String.format(FORMAT,
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
                binding.txtRemainingTime.setText("Time is up!");
                onCountdownFinish();
                processAnswer();
            }
        }.start();
    }

    private void processAnswer() {
        long minute = 60 - min;
        long second = 60 - sec;


        String TakeTime = "" + 0 + ":" + minute + ":" + second;


        contestQuestionListToDatabase(questionItems);

        String str_questionlist = new Gson().toJson(questionItems);
        String list = new Gson().toJson(selectItemList);
        Bundle bundle = new Bundle();
        bundle.putString("questionlist", str_questionlist);
        bundle.putString("type", "WeeklyContest");
        bundle.putInt("marks", (int) 400);
        bundle.putString("selectlist", list);
        bundle.putString("TakeTime", TakeTime);

        FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_takeContestExamFragment_to_examPreviewFragment, bundle);
    }

    private void contestQuestionListToDatabase(List<QuestionItem> questionItems) {


        for (int i = 0; i < questionItems.size(); i++) {
            HashMap<String, Object> hashMap = new HashMap<>();
            String subjectChildKey = questionItems.get(i).getSubject();
            hashMap.put(questionItems.get(i).getQuestionId(), true);

            AppController.getFirebaseHelper().getWeeklyQuestionList().child(AppController.getFirebaseHelper().getFirebaseAuth().getUid()).child(subjectChildKey).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Common.answerList = new ArrayList<>();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }


    }

    private void onCountdownFinish() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void onImageClick(String link) {
        if (NetworkHelper.hasNetworAccess(getContext())) {
            showSingleImage(link);
        } else {
            new CustomMessage(getActivity(), "No Internet Connections");
        }
    }

    private void showSingleImage(String link) {
        localImageList = new ArrayList<>();
        localImageList.add(link);
        new StfalconImageViewer.Builder<String>(getContext(), localImageList, new ImageLoader<String>() {
            @Override
            public void loadImage(ImageView imageView, String imageUrl) {
                Glide.with(getActivity()).load(imageUrl).into(imageView);
            }
        }).withStartPosition(0).withHiddenStatusBar(false).show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_submit:
                if (Common.answerList.size() != 0) {
                    showAlartDialog();
                } else {
                    new CustomMessage(getActivity(), "Please select a answer");
                }

                break;
        }
    }

    private void showAlartDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Dialogtheme);
        builder.setTitle("Submit  answer");
        builder.setMessage("Are you sure, you want to submit answer?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onCountdownFinish();
                processAnswer();
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

    private void ShowDialogforBackfromExamPaper() {


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Dialogtheme);
        builder.setTitle("Quit Contest");
        builder.setMessage("Are you sure, you want to Quit In the Contest?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (questionItems != null) {
                    contestQuestionListToDatabase(questionItems);
                }
                onCountdownFinish();
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_takeContestExamFragment_to_nav_home, bundle);
                // processAnswer();
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
}