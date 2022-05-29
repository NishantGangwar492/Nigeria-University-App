package com.iotait.schoolapp.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.common.Common;
import com.iotait.schoolapp.ui.homepage.ui.exam.fullexam.models.AnswerModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class UIHelper {
    public static void setCircleImage(CircleImageView circleImage, String personphoto) {
        Glide.with(AppController.getInstance())
                .load(personphoto)
                .apply(new RequestOptions().error(R.drawable.ic_person).placeholder(R.drawable.ic_person).circleCrop()
                        .format(DecodeFormat.PREFER_RGB_565))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(circleImage);
    }

    public static void changeActivty(Context context, Class terget, Bundle bundle){
        context.startActivity(new Intent(context,terget).putExtras(bundle));
    }

    public static void setupUI(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }


    public static void setArticleImage(Context context, CircleImageView articleprofile, String writerImage) {
        if (writerImage != null && !writerImage.equals("none")) {
            Glide.with(AppController.getInstance())
                    .load(writerImage)
                    .apply(new RequestOptions().error(R.drawable.def_person).placeholder(R.drawable.def_person).circleCrop()
                            .format(DecodeFormat.PREFER_RGB_565))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(articleprofile);
        } else {
            Glide.with(AppController.getInstance())
                    .load(R.drawable.profilepic)
                    .apply(new RequestOptions().error(R.drawable.def_person).placeholder(R.drawable.def_person).circleCrop()
                            .format(DecodeFormat.PREFER_RGB_565))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(articleprofile);
        }

    }

    public static void setPersonImage(ImageView imageView, String image_url) {
        Glide.with(AppController.getInstance())
                .load(image_url)
                .apply(new RequestOptions().error(R.drawable.ic_person).placeholder(R.drawable.ic_person)
                        .format(DecodeFormat.PREFER_RGB_565))
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageView);

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @BindingAdapter("changeFormat")
    public static void changeTimeFormat(TextView textview, String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "MMMM d, yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = "";

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        textview.setText(str);
    }

    @BindingAdapter("setQuestionImage")
    public static void setQuestionImage(ImageView imageView, String image) {
        Picasso.get()
                .load(image)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(image).into(imageView);
                    }
                });
    }

    @BindingAdapter("setImage")
    public static void setImage(ImageView imageView, String image) {


        if (NetworkHelper.hasNetworAccess(AppController.getInstance())) {


            Glide.with(AppController.getInstance())
                    .load(image)
                    .apply(new RequestOptions()
                            .format(DecodeFormat.PREFER_RGB_565))
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(imageView);
        } else {

            if (image != null) {
                String fileName1 = "";
                Pattern p = Pattern.compile("Question%2F(.+?)\\?alt");
                Matcher m = p.matcher(image);
                if (m.find()) {
                    String spaceReuce = m.group(1).replace("%20", " ");
                    fileName1 = spaceReuce;
                }
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myUNN_Post_Utme/images/", fileName1 + ".jpeg");

                Uri imageuri = Uri.fromFile(file);
                Glide.with(AppController.getInstance())
                        .load(imageuri)
                        .apply(new RequestOptions()
                                .format(DecodeFormat.PREFER_RGB_565))
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(imageView);
            }
        }
    }
    @BindingAdapter("checkReport")
    public static void checkReportedData(final TextView textview, String questionid) {
        AppController.getFirebaseHelper().getReportedQuestion().child(questionid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                   for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                       String id=snapshot.child("uid").getValue(String.class);
                       if (id.equals(AppController.getFirebaseHelper().getFirebaseAuth().getUid())){
                           textview.setText("Reported");
                       }
                       else {
                           textview.setText("Report");
                       }
                   }
                }
                else {
                    textview.setText("Report");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void checkAnswerData(String questionId, String answer,String current_answer,int pos){
        if (findPrevAnswee(questionId)!=null){
            AnswerModel updateModel = new AnswerModel();
            updateModel.setYour_answer(answer);
            updateModel.setCurrect_answer(current_answer);
            updateModel.setQuestion_position(pos);
            updateModel.setQuestioid(questionId);
            Common.answerList.add(updateModel);
            //  Common.answerList.remove(updateModel);
        }
        else {
            AnswerModel addModel=new AnswerModel();
            addModel.setYour_answer(answer);
            addModel.setCurrect_answer(current_answer);
            addModel.setQuestion_position(pos);
            addModel.setQuestioid(questionId);
            Common.answerList.add(addModel);
        }
    }
    public static AnswerModel findPrevAnswee(String questionId) {
        //    for(AnswerModel answerModel : Common.answerList) {

        for (int i = 0; i < Common.answerList.size(); i++) {
            AnswerModel ans = Common.answerList.get(i);
            if (ans.getQuestioid().equals(questionId)) {
                Common.answerList.remove(i);
                return ans;
            }
        }
        //     }
        return null;
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static void setImageInView(ImageView imageView, String image_url) {
        Glide.with(AppController.getInstance())
                .load(image_url)
                .apply(new RequestOptions()
                        .format(DecodeFormat.PREFER_RGB_565))
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageView);
    }

    public static void leaderName(final TextView textview, String uid) {
        AppController.getFirebaseHelper().getUsers().child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String name=dataSnapshot.child("personname").getValue(String.class);
                    textview.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
