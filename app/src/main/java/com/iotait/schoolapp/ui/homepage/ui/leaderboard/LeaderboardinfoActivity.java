package com.iotait.schoolapp.ui.homepage.ui.leaderboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.databinding.ActivityLeaderboardinfoBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.NetworkHelper;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.ui.homepage.ui.leaderboard.model.LeaderBoardModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardinfoActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityLeaderboardinfoBinding binding;
    private Bundle bundle;
    private LeaderBoardModel leaderBoardModel;
    ListView myList;
    ArrayAdapter<String> mHistory;
   // List<LeaderBoardModel> userLeaderBoardModelListTemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_leaderboardinfo);


        Intent in = getIntent();
        bundle = in.getExtras();

        List<String> array= new ArrayList<String>();



        //  userLeaderBoardModelListTemp = (List<LeaderBoardModel>) new Gson().fromJson(bundle.getString("leader_items"),LeaderBoardModel.class);
        Log.d("Error ",bundle.size()+"");
        for (int l=1; l<bundle.size(); l++){
            try {
                LeaderBoardModel leaderBoardModel = new Gson().fromJson(bundle.getString("q" + l, "NA"), LeaderBoardModel.class);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                String date = simpleDateFormat.format(leaderBoardModel.getTimeTake());
                array.add(leaderBoardModel.getScore() + " | " + "" + date + " min");
            }
            catch (Exception e){

            }
        }


        ArrayAdapter<String> mHistory = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
        binding.myList.setAdapter(mHistory);

        leaderBoardModel=new Gson().fromJson(bundle.getString("leader_item"),LeaderBoardModel.class);

        binding.userScore.setText(""+leaderBoardModel.getScore());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        String date = simpleDateFormat.format(leaderBoardModel.getTimeTake());
        binding.takeTimeuser.setText("" + date + " min");


        binding.sub1.setText(leaderBoardModel.getSubjectList().get(0).getSubject());
        binding.sub2.setText(leaderBoardModel.getSubjectList().get(1).getSubject());
        binding.sub3.setText(leaderBoardModel.getSubjectList().get(2).getSubject());
        binding.sub4.setText(leaderBoardModel.getSubjectList().get(3).getSubject());

        getUserInfo(leaderBoardModel.getUid());


        binding.backbutton.setOnClickListener(this);
    }

    private void getUserInfo(String uid) {
       if (NetworkHelper.hasNetworAccess(this)){
           binding.spinKit.setVisibility(View.VISIBLE);
           AppController.getFirebaseHelper().getUsers().child(uid).addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   binding.spinKit.setVisibility(View.GONE);
                   if (dataSnapshot.exists()){
                       String name=dataSnapshot.child("personname").getValue(String.class);
                       String photo=dataSnapshot.child("photo").getValue(String.class);
                       UIHelper.setCircleImage(binding.circleImageView2, photo);
                       binding.usernameleaderboard.setText(name);
                       binding.textView12.setText("Other Scores by "+ name +" :");
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {
                   binding.spinKit.setVisibility(View.GONE);
               }
           });
       }
       else {
           new CustomMessage(getParent(),"No internet connection");
       }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.backbutton:
                finish();
                break;
        }
    }
}
