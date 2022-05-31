package com.iotait.schoolapp.ui.homepage.ui.exam.custom.exam_details;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.common.Common;
import com.iotait.schoolapp.databinding.FragmentCustomExamDetailsBinding;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomExamDetailsFragment extends Fragment implements View.OnClickListener, ImageClick {

    private FragmentCustomExamDetailsBinding customExamDetailsBinding;
    private Bundle bundle;
    private String selectedYear = "";
    private String subject = "";
//    private int numberofquestion = 0;
    private long duration = 0;

    private QuestionAdapter adapter;
    private List<QuestionItem> questionItems;
    private List<QuestionItem> questionItemsTemp;


    private static final String FORMAT = "%02d h %02d m %02d s";

    public CustomExamDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        customExamDetailsBinding= DataBindingUtil.inflate(inflater, R.layout.fragment_custom_exam_details,container,false);
        return customExamDetailsBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        customExamDetailsBinding.includeToolbar.toolbarTitle.setText("CUSTOM EXAM");
        ((AppCompatActivity) getActivity()).setSupportActionBar(customExamDetailsBinding.includeToolbar.toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        customExamDetailsBinding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCountdownFinish();
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(customExamDetailsBinding.getRoot(), R.id.action_customExamDetailsFragment_to_customExamFragment, bundle);
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                onCountdownFinish();
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(customExamDetailsBinding.getRoot(), R.id.action_customExamDetailsFragment_to_customExamFragment, bundle);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);


        Common.answerList=new ArrayList<>();
        bundle = getArguments();
        selectedYear = bundle.getString("year");
        subject = bundle.getString("subject");
//        numberofquestion = Integer.parseInt(bundle.getString("numberofquestion"));
        duration = bundle.getLong("duration");
        Customlog.showlogD("EXAM_DETAILS", ""+selectedYear);

        questionItems = new ArrayList<>();
        questionItemsTemp = new ArrayList<>();
        adapter = new QuestionAdapter(getContext(), questionItems, this);
        adapter.setHasStableIds(true);
        customExamDetailsBinding.rvquestions.setLayoutManager(new LinearLayoutManager(getContext()));
        customExamDetailsBinding.rvquestions.setAdapter(adapter);

        AppController.getFirebaseHelper().getQuestions()
                .orderByChild("subject").equalTo(subject).keepSynced(true);
        getSelectedQuestion(subject);

        customExamDetailsBinding.btnSubmit.setOnClickListener(this);
    }

    private void getSelectedQuestion(String subject) {
        if (NetworkHelper.hasNetworAccess(getContext())) {
            customExamDetailsBinding.spinKit.setVisibility(View.VISIBLE);
            AppController.getFirebaseHelper().getQuestions()
                    .orderByChild("subject").equalTo(subject)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            customExamDetailsBinding.spinKit.setVisibility(View.GONE);
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
                                    if (dataSnapshot1.child("prepareExam") != null) {
                                        prepareExam = dataSnapshot1.child("prepareExam").getValue(Boolean.class);
                                    }
                                    if (dataSnapshot1.child("question") != null) {
                                        question = dataSnapshot1.child("question").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("scenario") != null) {
                                        scenario = dataSnapshot1.child("scenario").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("studyQuestion") != null) {
                                        studyQuestion = dataSnapshot1.child("studyQuestion").getValue(Boolean.class);
                                    }
                                    if (dataSnapshot1.child("subject") != null) {
                                        subject = dataSnapshot1.child("subject").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("year") != null) {
                                        year = dataSnapshot1.child("year").getValue(String.class);
                                    }

                                    if (dataSnapshot1.child("explain_image")!=null){
                                        explain_image=dataSnapshot1.child("explain_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("optiona_image")!=null){
                                        optiona_image=dataSnapshot1.child("options").child("optiona_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("optionb_image")!=null){
                                        optionb_image=dataSnapshot1.child("options").child("optionb_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("optionc_image")!=null){
                                        optionc_image=dataSnapshot1.child("options").child("optionc_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("optiond_image")!=null){
                                        optiond_image=dataSnapshot1.child("options").child("optiond_image").getValue(String.class);
                                    }

                                    if (dataSnapshot1.child("question_image")!=null){
                                        question_image=dataSnapshot1.child("question_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("scenario_image")!=null){
                                        scenario_image=dataSnapshot1.child("scenario_image").getValue(String.class);
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
                                    if (questionItem.getYear().equals(selectedYear))
                                        questionItemsTemp.add(questionItem);
                                }
                                questionItems.clear();
                                questionItems.addAll(questionItemsTemp);
                                adapter.notifyDataSetChanged();
                                customExamDetailsBinding.txtTotalQuestion.setText("Total " + String.valueOf(questionItems.size()) + " Questions");
                                customExamDetailsBinding.layoutTimer.setVisibility(View.VISIBLE);
                                customExamDetailsBinding.btnSubmit.setVisibility(View.VISIBLE);
                                statrtTime();
                            } else {
                                new CustomMessage(getActivity(), "No question available");
                                questionItems.clear();
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        } else {
            customExamDetailsBinding.spinKit.setVisibility(View.VISIBLE);
            AppController.getFirebaseHelper().getQuestions()
                    .orderByChild("subject").equalTo(subject)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            customExamDetailsBinding.spinKit.setVisibility(View.GONE);
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
                                    if (dataSnapshot1.child("prepareExam") != null) {
                                        prepareExam = dataSnapshot1.child("prepareExam").getValue(Boolean.class);
                                    }
                                    if (dataSnapshot1.child("question") != null) {
                                        question = dataSnapshot1.child("question").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("scenario") != null) {
                                        scenario = dataSnapshot1.child("scenario").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("studyQuestion") != null) {
                                        studyQuestion = dataSnapshot1.child("studyQuestion").getValue(Boolean.class);
                                    }
                                    if (dataSnapshot1.child("subject") != null) {
                                        subject = dataSnapshot1.child("subject").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("year") != null) {
                                        year = dataSnapshot1.child("year").getValue(String.class);
                                    }

                                    if (dataSnapshot1.child("explain_image")!=null){
                                        explain_image=dataSnapshot1.child("explain_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("optiona_image")!=null){
                                        optiona_image=dataSnapshot1.child("options").child("optiona_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("optionb_image")!=null){
                                        optionb_image=dataSnapshot1.child("options").child("optionb_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("optionc_image")!=null){
                                        optionc_image=dataSnapshot1.child("options").child("optionc_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("optiond_image")!=null){
                                        optiond_image=dataSnapshot1.child("options").child("optiond_image").getValue(String.class);
                                    }

                                    if (dataSnapshot1.child("question_image")!=null){
                                        question_image=dataSnapshot1.child("question_image").getValue(String.class);
                                    }
                                    if (dataSnapshot1.child("scenario_image")!=null){
                                        scenario_image=dataSnapshot1.child("scenario_image").getValue(String.class);
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

                                    if (questionItem.getYear().equals(selectedYear))
                                        questionItemsTemp.add(questionItem);
                                }
                                questionItems.clear();
                                questionItems.addAll(questionItemsTemp);

                                adapter.notifyDataSetChanged();
                                customExamDetailsBinding.txtTotalQuestion.setText("Total " + String.valueOf(questionItems.size()) + " Questions");
                                customExamDetailsBinding.layoutTimer.setVisibility(View.VISIBLE);
                                customExamDetailsBinding.btnSubmit.setVisibility(View.VISIBLE);
                                statrtTime();
                            } else {
                                new CustomMessage(getActivity(), "No questions added");
                                questionItems.clear();
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
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
                customExamDetailsBinding.txtRemainingTime.setText(""+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }
            public void onFinish() {
                customExamDetailsBinding.txtRemainingTime.setText("Time is up!");
                onCountdownFinish();
                processAnswer();
            }
        }.start();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_submit:
                if (Common.answerList.size()!=0){
                    showAlartDialog();
                }else {
                    new CustomMessage(getActivity(),"Please select a answer");
                }
                break;
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
    private List<SelectItem> selectItems=new ArrayList<>();
    private void processAnswer() {
        onCountdownFinish();
        double res=questionItems.size()*1;
        SelectItem selectItem=new SelectItem();
        selectItem.setId(0);
        selectItem.setSubject(subject);
        selectItems.add(selectItem);
        String str_questionlist = new Gson().toJson(questionItems);
        Bundle bundle = new Bundle();
        bundle.putString("questionlist", str_questionlist);
        bundle.putString("type", "custom_exam");
        bundle.putString("selectlist", new Gson().toJson(selectItems));
        bundle.putInt("marks", (int) res);
        FragmentHelper.changeFragmet(customExamDetailsBinding.getRoot(), R.id.action_customExamDetailsFragment_to_examPreviewFragment, bundle);
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
