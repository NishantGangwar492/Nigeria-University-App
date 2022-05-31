package com.iotait.schoolapp.ui.homepage.ui.unnfaq;

import android.app.Dialog;
import android.content.Context;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.databinding.FragmentUnnFaqBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.FragmentHelper;
import com.iotait.schoolapp.ui.homepage.ui.unnfaq.adapter.FaqAdapter;
import com.iotait.schoolapp.ui.homepage.ui.unnfaq.interfaceClass.FaqInterface;
import com.iotait.schoolapp.ui.homepage.ui.unnfaq.model.FaqData;
import com.iotait.schoolapp.ui.homepage.ui.unnfaq.model.SelectItemFaq;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnnFaqFragment extends Fragment implements FaqInterface {
    private FragmentUnnFaqBinding binding;
    private List<FaqData> faqDataList = new ArrayList<>();
    private FaqAdapter faqAdapter;
    Dialog dialog;
    private List<SelectItemFaq> selectList;

    public UnnFaqFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_unn_faq, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initFaqViews();
        getFaqData();

        selectList = new ArrayList<>();

        binding.shareBtn.setOnClickListener(v -> {

            if (selectList.size() >= 4) {
                OpenImageDialog();
            } else {
                new CustomMessage(getActivity(), "Please select Any [4] FAQ for Share!");
            }

        });

        binding.back.setOnClickListener(v -> FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_unnFaqFragment_to_navigation, new Bundle()));
    }

    private void OpenImageDialog() {

        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.faq_image_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        TextView textQuestion1 = (TextView) dialog.findViewById(R.id.faq_question_1);
        TextView textQuestion2 = (TextView) dialog.findViewById(R.id.faq_question_2);
        TextView textQuestion3 = (TextView) dialog.findViewById(R.id.faq_question_3);
        TextView textQuestion4 = (TextView) dialog.findViewById(R.id.faq_question_4);
        TextView textQuestionAnswer1 = (TextView) dialog.findViewById(R.id.faq_answer_1);
        TextView textQuestionAnswer2 = (TextView) dialog.findViewById(R.id.faq_answer_2);
        TextView textQuestionAnswer3 = (TextView) dialog.findViewById(R.id.faq_answer_3);
        TextView textQuestionAnswer4 = (TextView) dialog.findViewById(R.id.faq_answer_4);

        if (faqDataList != null) {
            textQuestion1.setText(selectList.get(0).getQuestion().toString());
            textQuestionAnswer1.setText(selectList.get(0).getAnswer());
            textQuestion2.setText(selectList.get(1).getQuestion());
            textQuestionAnswer2.setText(selectList.get(1).getAnswer());
            textQuestion3.setText(selectList.get(2).getQuestion());
            textQuestionAnswer3.setText(selectList.get(2).getAnswer());
            textQuestion4.setText(selectList.get(3).getQuestion());
            textQuestionAnswer4.setText(selectList.get(3).getAnswer());

        }

        Button ok = (Button) dialog.findViewById(R.id.ok_result_dialog_faq);

//        final ScrollView root = dialog.findViewById(R.id.scrollViewfaq);
//        ScrollView scrollView = (ScrollView) dialog.findViewById(R.id.scrollViewfaq);
        LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.lin2);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                int totalHeight = linearLayout.getChildAt(0).getHeight();
//                int totalWidth = linearLayout.getChildAt(0).getWidth();
                saveBitMap(getContext(), linearLayout);

            }
        });

        dialog.show();
    }

    private void saveBitMap(Context context, LinearLayout root) {
        Bitmap bitmap = getBitmapFromView(root);
        try {
            //pictureFile.createNewFile();
            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream oStream = new FileOutputStream(cachePath + "/image.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sharefile();
    }

    private void initFaqViews() {
        faqAdapter = new FaqAdapter(getContext(), faqDataList, this);
        binding.faqRcv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.faqRcv.setAdapter(faqAdapter);
    }

    private void getFaqData() {
        AppController.getFirebaseHelper().getUnnFaq().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    faqDataList.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        FaqData data = new FaqData();
                        data.setFaqId(dataSnapshot1.getKey());
                        data.setQuestion(dataSnapshot1.child("question").getValue(String.class));
                        data.setAnswer(dataSnapshot1.child("answer").getValue(String.class));
                        faqDataList.add(data);
                        Collections.reverse(faqDataList);
                    }
                    faqAdapter.notifyDataSetChanged();
                    binding.spinKit.setVisibility(View.GONE);
                    binding.shareBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void sharefile() {
        File imagePath = new File(getContext().getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(getContext(), "com.iotait.schoolapp.fileprovider", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, getContext().getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
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

    public Bitmap takeScreenshot() {
        View rootView = getActivity().findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    File imagePath;

    public void saveBitmap(Bitmap bitmap) {
        imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    private void shareIt() {
        Uri uri = Uri.fromFile(imagePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        String shareBody = "In Tweecher, My highest score with screen shot";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Tweecher score");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    public void onFaqSelect(int position, String question, String answer) {
        SelectItemFaq selectItemFaq = new SelectItemFaq();
        selectItemFaq.setId(position);
        selectItemFaq.setQuestion(question);
        selectItemFaq.setAnswer(answer);
        selectList.add(selectItemFaq);
    }

    @Override
    public void onSubjectDeselect(int position, String question, String answer) {
        for (int i = 0; i < selectList.size(); i++) {
            SelectItemFaq item = selectList.get(i);
            if (item.getId() == position) {
                selectList.remove(i);
            }
        }
    }
}