package com.iotait.schoolapp.ui.homepage.ui.question.details;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.databinding.FragmentQuestionDetailsBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.FragmentHelper;
import com.iotait.schoolapp.helper.NetworkHelper;
import com.iotait.schoolapp.listener.ImageClick;
import com.iotait.schoolapp.ui.homepage.ui.question.details.adapters.QuestionAdapter;
import com.iotait.schoolapp.ui.homepage.ui.question.details.models.QuestionItem;
import com.iotait.schoolapp.ui.homepage.ui.question.view.AddQuestionView;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionDetailsFragment extends Fragment implements AddQuestionView, ImageClick {

    private FragmentQuestionDetailsBinding questionDetailsBinding;
    private Bundle bundle;
    private String Selected_year="";
    private String selected_subject="";
    private List<QuestionItem> questionItems;
    private List<QuestionItem> questionFilterlist;
    private QuestionAdapter questionAdapter;
    String PremiumType = "";
    boolean isPremium;
    String expireDate = "";


    public QuestionDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        questionDetailsBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_question_details, container,false);
        return questionDetailsBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        questionItems=new ArrayList<>();
        questionFilterlist = new ArrayList<>();
        bundle = getArguments();
        Selected_year=bundle.getString("year");
        selected_subject=bundle.getString("subject");
        questionDetailsBinding.selectedSubject.setText(selected_subject);


        questionDetailsBinding.includeToolbar.toolbarTitle.setText("STUDY QUESTIONS");
        ((AppCompatActivity) getActivity()).setSupportActionBar(questionDetailsBinding.includeToolbar.toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        questionDetailsBinding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(questionDetailsBinding.getRoot(), R.id.action_questionDetailsFragment_to_questionFragment, bundle);
            }
        });

        questionAdapter=new QuestionAdapter(getContext(), questionItems, this, this);
        questionDetailsBinding.recyclerQuestions.setLayoutManager(new LinearLayoutManager(getContext()));
        questionDetailsBinding.recyclerQuestions.setHasFixedSize(true);
        questionDetailsBinding.recyclerQuestions.setNestedScrollingEnabled(false);
        questionDetailsBinding.recyclerQuestions.setAdapter(questionAdapter);


        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        PremiumType = pref.getString("premiumType", null);
        isPremium = pref.getBoolean("isPremium", false);
        expireDate = pref.getString("expire_date", null);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMM yyyy hh:mm a");
        String c_date = new SimpleDateFormat("E, dd MMM yyyy hh:mm a", Locale.getDefault()).format(new Date());
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
            if (PremiumType.equals("p1")) {
                AppController.getFirebaseHelper().getQuestions().keepSynced(true);

            }
            if (PremiumType.equals("p0")) {

                AppController.getFirebaseHelper().getQuestions().keepSynced(false);
            }
            if (PremiumType.equals("p2")) {
                AppController.getFirebaseHelper().getQuestions().keepSynced(true);
            }
        } else {
            AppController.getFirebaseHelper().getQuestions().keepSynced(false);
        }

        getQuestionList();
        if (questionFilterlist != null) {
            searchQuestion(questionFilterlist);
        }
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

    private void getQuestionList() {
        if (NetworkHelper.hasNetworAccess(getContext())){
            questionDetailsBinding.spinKit.setVisibility(View.VISIBLE);
            AppController.getFirebaseHelper().getQuestions()
                    .orderByChild("subject").equalTo(selected_subject)
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    questionDetailsBinding.spinKit.setVisibility(View.GONE);
                    questionItems.clear();
                    questionFilterlist.clear();
                    if (dataSnapshot.exists()){
                        String answer="";
                        String explaination = "";
                        String optionA="";
                        String optionB="";
                        String optionC="";
                        String optionD="";
                        boolean prepareExam=false;
                        String question="";
                        String scenario="";
                        boolean studyQuestion=false;
                        String subject="";
                        String year="";
                        String questionid="";

                        String explain_image="";
                        String optiona_image="";
                        String optionb_image="";
                        String optionc_image="";
                        String optiond_image="";
                        String question_image="";
                        String scenario_image="";

                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            if (dataSnapshot1.child("answer")!=null){
                                answer=dataSnapshot1.child("answer").getValue(String.class);
                            }
                            if (dataSnapshot1.child("explaination")!=null){
                                explaination=dataSnapshot1.child("explaination").getValue(String.class);
                            }
                            if (dataSnapshot1.child("options").child("optionA")!=null){
                                optionA=dataSnapshot1.child("options").child("optionA").getValue(String.class);
                            }
                            if (dataSnapshot1.child("options").child("optionB")!=null){
                                optionB=dataSnapshot1.child("options").child("optionB").getValue(String.class);
                            }
                            if (dataSnapshot1.child("options").child("optionC")!=null){
                                optionC=dataSnapshot1.child("options").child("optionC").getValue(String.class);
                            }
                            if (dataSnapshot1.child("options").child("optionD")!=null){
                                optionD=dataSnapshot1.child("options").child("optionD").getValue(String.class);
                            }
                            if (dataSnapshot1.child("prepareExam")!=null){
                                prepareExam=dataSnapshot1.child("prepareExam").getValue(Boolean.class);
                            }
                            if (dataSnapshot1.child("question")!=null){
                                question=dataSnapshot1.child("question").getValue(String.class);
                            }
                            if (dataSnapshot1.child("scenario")!=null){
                                scenario=dataSnapshot1.child("scenario").getValue(String.class);
                            }
                            if (dataSnapshot1.child("studyQuestion")!=null){
                                studyQuestion=dataSnapshot1.child("studyQuestion").getValue(Boolean.class);
                            }
                            if (dataSnapshot1.child("subject")!=null){
                                subject=dataSnapshot1.child("subject").getValue(String.class);
                            }
                            if (dataSnapshot1.child("year")!=null){
                                year=dataSnapshot1.child("year").getValue(String.class);
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

                            questionid=dataSnapshot1.getKey();
                            QuestionItem questionItem=new QuestionItem();
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

                            if (questionItem.getYear().equals(Selected_year)){
                                questionItems.add(questionItem);
                                questionFilterlist.add(questionItem);
                            }
                        }
                        if (questionItems.size()==0){
                            new CustomMessage(getActivity(),"No questions are available");
                        }
                        questionAdapter.notifyDataSetChanged();
                    }
                    else {
                        new CustomMessage(getActivity(),"No questions are available");
                        questionAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    questionDetailsBinding.spinKit.setVisibility(View.GONE);
                    questionItems.clear();
                    questionAdapter.notifyDataSetChanged();
                }
            });
        }
        else {

            questionDetailsBinding.spinKit.setVisibility(View.VISIBLE);
            AppController.getFirebaseHelper().getQuestions()
                    .orderByChild("subject").equalTo(selected_subject)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            questionDetailsBinding.spinKit.setVisibility(View.GONE);
                            questionItems.clear();
                            questionFilterlist.clear();
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

                                    if (questionItem.getYear().equals(Selected_year)) {
                                        questionItems.add(questionItem);
                                        questionFilterlist.add(questionItem);
                                    }
                                }
                                if (questionItems.size() == 0) {
                                    new CustomMessage(getActivity(), "No questions are available");
                                }
                                questionAdapter.notifyDataSetChanged();
                            } else {
                                new CustomMessage(getActivity(), "No questions are available");
                                questionAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            questionDetailsBinding.spinKit.setVisibility(View.GONE);
                            questionItems.clear();
                            questionAdapter.notifyDataSetChanged();
                        }
                    });
            new CustomMessage(getActivity(),"No internet connection");
        }
    }

    private void searchQuestion(final List<QuestionItem> searchlist) {

        questionDetailsBinding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<QuestionItem> filteredList = new ArrayList<>();
                if (query.isEmpty()) {
                    filteredList = searchlist;
                } else {
                    for (QuestionItem item : searchlist) {
                        if (item.getQuestion().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                }
                questionItems.clear();
                questionItems.addAll(filteredList);
                questionAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                List<QuestionItem> filteredList = new ArrayList<>();
                if (newText.isEmpty() || newText.equals(" ")) {
                    filteredList = searchlist;
                } else {
                    for (QuestionItem searchItem : searchlist) {
                        if (searchItem.getQuestion() != null) {
                            if (searchItem.getQuestion().toLowerCase().contains(newText.toLowerCase())) {
                                filteredList.add(searchItem);
                            }
                        }
                    }
                }
                questionItems.clear();
                if (filteredList.size() == 0) {
//                    fragmentFoodhelpBinding.noitem.setVisibility(View.VISIBLE);
                    new CustomMessage(getActivity(),"No question found");
                } else {
//                    fragmentFoodhelpBinding.noitem.setVisibility(View.GONE);
                }
                questionItems.addAll(filteredList);
                questionAdapter.notifyDataSetChanged();
                return true;
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

    @Override
    public void onYearSelected(String year) {

    }

    @Override
    public void onSubjectSelected(String subject) {

    }

    @Override
    public void onQuestionReport(String questionid, String questionNo) {
        showDialog(questionid, questionNo);
    }

    private Dialog dialog;

    private void showDialog(final String questionid, String questionNo) {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_report_question);
        dialog.setCanceledOnTouchOutside(false);


        final Button btn_ok = dialog.findViewById(R.id.btn_report_ok);
        final EditText edit_report_explain = dialog.findViewById(R.id.edit_report_exp);
        final SpinKitView spinkit_report = dialog.findViewById(R.id.spin_kit_report);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (!TextUtils.isEmpty(edit_report_explain.getText().toString())){
                   if (NetworkHelper.hasNetworAccess(getContext())){
                       spinkit_report.setVisibility(View.VISIBLE);
                       String reportid=AppController.getFirebaseHelper().getReportedQuestion().child(questionid).push().getKey();
                       HashMap<String, Object> reported=new HashMap<>();
                       reported.put("uid",AppController.getFirebaseHelper().getFirebaseAuth().getUid());
                       reported.put("explain", edit_report_explain.getText().toString());
                       reported.put("question_no", questionNo);
                       AppController.getFirebaseHelper().getReportedQuestion().child(questionid).child(reportid).updateChildren(reported).addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {
                               spinkit_report.setVisibility(View.GONE);
                               getQuestionList();
                               dialog.dismiss();
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               spinkit_report.setVisibility(View.GONE);
                               new CustomMessage(getActivity(),"Something wrong, please try again");
                               dialog.dismiss();
                           }
                       });
                   }else {
                       new CustomMessage(getActivity(),"No internet connection");
                   }
               }
               else {
                   edit_report_explain.setError("Please add your report explanation");
               }
            }
        });

        dialog.show();
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
}
