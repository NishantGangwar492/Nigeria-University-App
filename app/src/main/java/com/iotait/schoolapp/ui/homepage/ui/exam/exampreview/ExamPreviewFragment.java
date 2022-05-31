package com.iotait.schoolapp.ui.homepage.ui.exam.exampreview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.common.Common;
import com.iotait.schoolapp.databinding.FragmentExamPreviewBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.FragmentHelper;
import com.iotait.schoolapp.helper.NetworkHelper;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.listener.ImageClick;
import com.iotait.schoolapp.ui.homepage.ui.exam.exampreview.adapters.QuestionPreviewAdapter;
import com.iotait.schoolapp.ui.homepage.ui.exam.fullexam.models.AnswerModel;
import com.iotait.schoolapp.ui.homepage.ui.exam.models.SelectItem;
import com.iotait.schoolapp.ui.homepage.ui.question.details.models.QuestionItem;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
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
public class ExamPreviewFragment extends Fragment implements View.OnClickListener, ImageClick {

    private FragmentExamPreviewBinding examPreviewBinding;
    private Bundle bundle;

    private QuestionPreviewAdapter adapter;
    private List<QuestionItem> questionItems;
    private List<QuestionItem> questionItemsTemp;
    private List<SelectItem> selectItemListTemp;
    private List<SelectItem> selectItemList;
    private String pass_str = "";
    private String pass_str_subject = "";
    private String type = "";
    private String takeTime = "";
    private int markes = 0;
    private String c_date = new SimpleDateFormat("E, dd MMM yyyy hh:mm a", Locale.getDefault()).format(new Date());


    boolean processClick = true;

    public ExamPreviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        examPreviewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_exam_preview, container, false);
        return examPreviewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        examPreviewBinding.includeToolbar.toolbarTitle.setText("EXAM PREVIEW");
        ((AppCompatActivity) getActivity()).setSupportActionBar(examPreviewBinding.includeToolbar.toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        examPreviewBinding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitDialog();
//                if (type.equals("WeeklyContest")) {
//                    Common.answerList = new ArrayList<>();
//                    Bundle bundle = new Bundle();
//                    FragmentHelper.changeFragmet(examPreviewBinding.getRoot(), R.id.action_examPreviewFragment_to_weekly_contest, bundle);
//                } else if (type.equals("full_exam")) {
//                    Common.answerList = new ArrayList<>();
//                    Bundle bundle = new Bundle();
//                    FragmentHelper.changeFragmet(examPreviewBinding.getRoot(), R.id.action_examPreviewFragment_to_takeExamFragment, bundle);
//
//                } else if (type.equals("custom_exam")) {
//                    Common.answerList = new ArrayList<>();
//                    Bundle bundle = new Bundle();
//                    FragmentHelper.changeFragmet(examPreviewBinding.getRoot(), R.id.action_examPreviewFragment_to_takeExamFragment, bundle);
//                }

            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                showExitDialog();

            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        bundle = getArguments();
        pass_str = bundle.getString("questionlist");

        type = bundle.getString("type");
        takeTime = bundle.getString("TakeTime");
        markes = bundle.getInt("marks");
        pass_str_subject = bundle.getString("selectlist");
        Gson gson = new Gson();
        Type type = new TypeToken<List<QuestionItem>>() {
        }.getType();
        Type type2 = new TypeToken<List<SelectItem>>() {
        }.getType();
        questionItems = gson.fromJson(pass_str, type);
        questionItemsTemp=new ArrayList<>();


        selectItemList = gson.fromJson(pass_str_subject, type2);
        selectItemListTemp = new ArrayList<>();
        selectItemListTemp.clear();

        for (SelectItem selectItem : selectItemList) {
            selectItemListTemp.add(selectItem);
        }
        for (QuestionItem questionItem: questionItems){
            for (AnswerModel answerModel:Common.answerList){
                if (questionItem.getQuestionId().equals(answerModel.getQuestioid())){
                    questionItemsTemp.add(questionItem);
                }
            }
        }
        adapter = new QuestionPreviewAdapter(getContext(), questionItemsTemp, this);
        adapter.setHasStableIds(true);
        examPreviewBinding.rvquestions.setLayoutManager(new LinearLayoutManager(getContext()));
        examPreviewBinding.rvquestions.setAdapter(adapter);

        examPreviewBinding.btnSubmit.setOnClickListener(this);
        if (Common.answerList.size() == 0) {
            new CustomMessage(getActivity(), "You didn't select any answer");
            examPreviewBinding.btnSubmit.setVisibility(View.GONE);
        }
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Dialogtheme);
        builder.setTitle("Submit answer");
        builder.setMessage("Please See Score Before Exit!!\n\nIf you don't see your 'Exam score', then your 'Exam score' can't be updated to the 'Leader Board'.");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (type.equals("WeeklyContest")) {
                    Common.answerList = new ArrayList<>();
                    Bundle bundle = new Bundle();
                    FragmentHelper.changeFragmet(examPreviewBinding.getRoot(), R.id.action_examPreviewFragment_to_nav_home, bundle);
                } else if (type.equals("full_exam")) {
                    Common.answerList = new ArrayList<>();
                    Bundle bundle = new Bundle();
                    FragmentHelper.changeFragmet(examPreviewBinding.getRoot(), R.id.action_examPreviewFragment_to_takeExamFragment, bundle);
                } else if (type.equals("custom_exam")) {
                    Common.answerList = new ArrayList<>();
                    Bundle bundle = new Bundle();
                    FragmentHelper.changeFragmet(examPreviewBinding.getRoot(), R.id.action_examPreviewFragment_to_takeExamFragment, bundle);
                }
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

    private Dialog dialog;
    private void processAnswer() {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_result);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        int width = (int) (getResources().getDisplayMetrics().widthPixels);
        int height = (int) (getResources().getDisplayMetrics().heightPixels);
        dialog.getWindow().setLayout(width, height);
        final TextView txt_your_score = dialog.findViewById(R.id.txt_score);
        final TextView txt_res_out_of = dialog.findViewById(R.id.res_out_of);
        txt_your_score.setText(String.valueOf(UIHelper.round(getTotalScore(), 0)));
        txt_res_out_of.setText(getResources().getString(R.string.out_of) + "\t" + String.valueOf(markes));
        final Button btn_ok = dialog.findViewById(R.id.ok_result_dialog);
        final Button btn_dismiss = dialog.findViewById(R.id.result_dialog_dismiss);
        final Button result_dialog_share = dialog.findViewById(R.id.result_dialog_share);
        final ConstraintLayout root = dialog.findViewById(R.id.root);
        final MaterialRippleLayout resultDismissLayout = dialog.findViewById(R.id.resultdismiss);

        final TextView examTitle = (TextView) dialog.findViewById(R.id.exam_title);
        final TextView subjectList = (TextView) dialog.findViewById(R.id.subjectlist);
        final TextView spentTime = (TextView) dialog.findViewById(R.id.spenttime);
        final TextView Score = (TextView) dialog.findViewById(R.id.score);
        final TextView getUnnAppContactTxt = dialog.findViewById(R.id.get_unn_app_txt);
        final ImageView frameImage = dialog.findViewById(R.id.frame_image);

        examTitle.setVisibility(View.INVISIBLE);
        subjectList.setVisibility(View.INVISIBLE);
        spentTime.setVisibility(View.INVISIBLE);
        Score.setVisibility(View.INVISIBLE);
        getUnnAppContactTxt.setVisibility(View.INVISIBLE);
        frameImage.setVisibility(View.INVISIBLE);

        if (selectItemListTemp.size()==1){
            examTitle.setText(R.string.took_exam_custom);
            subjectList.setText(String.format("I wrote: %s", selectItemListTemp.get(0).getSubject()));
        } else {
            examTitle.setText(R.string.took_exam_full);
            spentTime.setText(String.format("I spent %s min", takeTime));
            subjectList.setText(String.format("I wrote: %s,%s,%s,%s.", selectItemListTemp.get(0).getSubject(), selectItemListTemp.get(1).getSubject(), selectItemListTemp.get(2).getSubject(), selectItemListTemp.get(3).getSubject()));
        }
        Score.setText(String.format("I scored: %s", txt_your_score.getText().toString()));

        if (NetworkHelper.hasNetworAccess(requireContext())) {
            resultDismissLayout.setVisibility(View.GONE);
            btn_dismiss.setVisibility(View.GONE);

        } else {
            resultDismissLayout.setVisibility(View.VISIBLE);
            btn_dismiss.setVisibility(View.VISIBLE);
        }

        result_dialog_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                examTitle.setVisibility(View.VISIBLE);
                subjectList.setVisibility(View.VISIBLE);
                spentTime.setVisibility(View.VISIBLE);
                Score.setVisibility(View.VISIBLE);
                getUnnAppContactTxt.setVisibility(View.VISIBLE);
                frameImage.setVisibility(View.VISIBLE);
                btnOkClick(btn_ok, txt_your_score);
                saveBitMap(requireContext(), root, txt_your_score.getText().toString());
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnOkClick(btn_ok, txt_your_score);
            }
        });
        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                examPreviewBinding.btnSubmit.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void btnOkClick(Button btn_ok, TextView txt_your_score){
        if (processClick) {
            processClick = false;
            btn_ok.setEnabled(false);
            btn_ok.setClickable(false);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("uid", AppController.getFirebaseHelper().getFirebaseAuth().getUid());
            hashMap.put("date", c_date);
            hashMap.put("subjectList", selectItemListTemp);
            hashMap.put("TimeTake", takeTime);
            hashMap.put("score", txt_your_score.getText().toString());

            final ProgressDialog mDialog = new ProgressDialog(getContext());
            mDialog.setMessage("Result updating...");
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setIndeterminate(false);
            mDialog.show();

            if (type.equals("full_exam")) {
                if (NetworkHelper.hasNetworAccess(requireContext())) {
                    String id = AppController.getFirebaseHelper().getFullExam().child(AppController.getFirebaseHelper().getFirebaseAuth().getUid()).push().getKey();
                    AppController.getFirebaseHelper().getFullExam().child(AppController.getFirebaseHelper().getFirebaseAuth().getUid()).child(id).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Common.answerList = new ArrayList<>();
                            examPreviewBinding.btnSubmit.setVisibility(View.GONE);
                            dialog.dismiss();
                            mDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            new CustomMessage(getActivity(), "Try again");
                            dialog.dismiss();
                            mDialog.dismiss();
                        }
                    });
                } else {
                    new CustomMessage(getActivity(), "Please Connect internet to have your score on LeaderBoard");
                }
            } else if (type.equals("custom_exam")) {
                examPreviewBinding.btnSubmit.setVisibility(View.GONE);
                dialog.dismiss();
                mDialog.dismiss();
            } else if (type.equals("WeeklyContest")) {
                if (NetworkHelper.hasNetworAccess(requireContext())) {
                    String id = AppController.getFirebaseHelper().getWeeklyContest().child(AppController.getFirebaseHelper().getFirebaseAuth().getUid()).push().getKey();
                    AppController.getFirebaseHelper().getWeeklyContest().child(AppController.getFirebaseHelper().getFirebaseAuth().getUid()).child(id).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Common.answerList = new ArrayList<>();
                            Common.answerList = new ArrayList<>();
                            Bundle bundle = new Bundle();
                            FragmentHelper.changeFragmet(examPreviewBinding.getRoot(), R.id.action_examPreviewFragment_to_nav_home, bundle);
                            //examPreviewBinding.btnSubmit.setVisibility(View.GONE);
                            dialog.dismiss();
                            mDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            new CustomMessage(getActivity(), "Try again");
                            dialog.dismiss();
                            mDialog.dismiss();
                        }
                    });
                } else {
                    new CustomMessage(getActivity(), "Please Connect internet to have your score on LeaderBoard");
                }
            }
        }
    }

    private void saveBitMap(Context context, View drawView, String s) {

        Bitmap bitmap = getBitmapFromView(drawView);
        try {
            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream oStream = new FileOutputStream(cachePath + "/image.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.dismiss();
        sharefile(s);
    }

    private void sharefile(String s) {
        File imagePath = new File(getContext().getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(getContext(), "com.iotait.schoolapp.fileprovider", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, getContext().getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);

            if (selectItemListTemp.size()==1){
                shareIntent.putExtra(Intent.EXTRA_TEXT, "I just took a CUSTOM EXAM in MyUNN Post-utme App.\nI wrote: " + selectItemListTemp.get(0).getSubject() + "\n I scored:" + s + " min" + "\n\nGet My UNN Post-utme App on Playstore\n https://play.google.com/store/apps/details?id=com.iotait.schoolapp" + "\n\nor WhatsApp: 08093785476.");
            } else {
                shareIntent.putExtra(Intent.EXTRA_TEXT, "I just took a FULL EXAM in MyUNN Post-utme App.\nI wrote: " + selectItemListTemp.get(0).getSubject() + ", " + selectItemListTemp.get(1).getSubject()
                        + ", " + selectItemListTemp.get(2).getSubject() + ", " + selectItemListTemp.get(3).getSubject() + "\nI spent: " +
                        takeTime + "\n I scored:" + s + " min" + "\n\nGet My UNN Post-utme App on Playstore\n https://play.google.com/store/apps/details?id=com.iotait.schoolapp" + "\n\nor WhatsApp: 08093785476.");
            }
            startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }
    }

    //create bitmap from view and returns it
    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    private double getTotalScore() {
        double score = 0;
        for (int i = 0; i < Common.answerList.size(); i++) {
            AnswerModel answerModel = Common.answerList.get(i);
            if (type.equals("full_exam")) {
                if (answerModel.getCurrect_answer().equals(answerModel.getYour_answer())) {
                    score = score + 6.67;
                }
            }
            if (type.equals("custom_exam")) {
                if (answerModel.getCurrect_answer().equals(answerModel.getYour_answer())) {
                    score = score + 1;
                }
            }
            if (type.equals("WeeklyContest")) {
                if (answerModel.getCurrect_answer().equals(answerModel.getYour_answer())) {
                    score = score + 6.67;
                }
            }
        }
        return score;
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.btn_submit:
                processAnswer();
                break;
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

    @Override
    public void onResume() {
        super.onResume();
        processClick = true;
    }
}
