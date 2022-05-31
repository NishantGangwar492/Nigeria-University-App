package com.iotait.schoolapp.ui.homepage.ui.leaderboard.monthlytop;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.databinding.FragmentMonthlyTopBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.Customlog;
import com.iotait.schoolapp.helper.NetworkHelper;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.ui.homepage.ui.leaderboard.LeaderboardinfoActivity;
import com.iotait.schoolapp.ui.homepage.ui.leaderboard.model.LeaderBoardModel;
import com.iotait.schoolapp.ui.homepage.ui.leaderboard.model.SubjectListModel;
import com.iotait.schoolapp.ui.homepage.ui.leaderboard.monthlytop.adapter.LeaderBoardAdapterForMonthlyTop;
import com.iotait.schoolapp.ui.homepage.ui.leaderboard.view.LeaderBoardView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MonthlyTopFragment extends Fragment implements LeaderBoardView {

    FragmentMonthlyTopBinding binding;
    List<LeaderBoardModel> leaderBoardModelList;
    List<LeaderBoardModel> leaderBoardModelListTemp;

    LeaderBoardAdapterForMonthlyTop adapterForAllTime;
    Date date1 = null;
    Date date2 = null;

    int count = 0;
    int count2 = 0;

    public MonthlyTopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_monthly_top, container, false);
        View root = binding.getRoot();
        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        leaderBoardModelListTemp = new ArrayList<>();
        leaderBoardModelList = new ArrayList<>();

        adapterForAllTime = new LeaderBoardAdapterForMonthlyTop(getContext(), leaderBoardModelList, MonthlyTopFragment.this);
        adapterForAllTime.setHasStableIds(true);
        binding.monthlyTimeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.monthlyTimeRecyclerView.setNestedScrollingEnabled(false);
        binding.monthlyTimeRecyclerView.setHasFixedSize(true);
        binding.monthlyTimeRecyclerView.setAdapter(adapterForAllTime);


        getAllTimeLeaderBoardTemp();
        getAllTimeLeaderBoard();
    }

    private void getAllTimeLeaderBoardTemp() {
        if (NetworkHelper.hasNetworAccess(getContext())) {

            AppController.getFirebaseHelper().getFullExam().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    if (dataSnapshot.exists()) {

                        for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {

                            String key = postsnapshot.getKey();
                            AppController.getFirebaseHelper().getFullExam().child(key).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists()) {


                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            count++;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }

                    } else {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    binding.spinKit.setVisibility(View.GONE);
                }
            });
        } else {
            new CustomMessage(getActivity(), "No internet connection");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getAllTimeLeaderBoard() {

        if (NetworkHelper.hasNetworAccess(getContext())) {
            binding.spinKit.setVisibility(View.VISIBLE);
            AppController.getFirebaseHelper().getFullExam().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    leaderBoardModelListTemp.clear();
                    if (dataSnapshot.exists()) {

                        for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {

                            String key = postsnapshot.getKey();

                            AppController.getFirebaseHelper().getFullExam().child(key).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists()) {
                                        String TimeTake = "";
                                        String date = "";
                                        String score = "";
                                        String uid = "";
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                            count2++;
                                            if (count == count2) {
                                                binding.spinKit.setVisibility(View.GONE);
                                            }

                                            if (snapshot.child("date").getValue(String.class) != null) {
                                                date = snapshot.child("date").getValue(String.class);
                                            }

                                            if (snapshot.child("TimeTake").getValue(String.class) != null) {
                                                TimeTake = snapshot.child("TimeTake").getValue(String.class);
                                            }

                                            if (snapshot.child("score").getValue(String.class) != null) {
                                                score = snapshot.child("score").getValue(String.class);
                                            }
                                            if (snapshot.child("uid").getValue(String.class) != null) {
                                                uid = snapshot.child("uid").getValue(String.class);
                                            }
                                            List<SubjectListModel> subjectlist = new ArrayList<>();
                                            if (snapshot.child("subjectList").getChildrenCount() > 0) {
                                                for (DataSnapshot subjectSnapshot : snapshot.child("subjectList").getChildren()) {
                                                    int id = 0;
                                                    String subject = "";
                                                    boolean isSelect = false;
                                                    if (subjectSnapshot.child("id").getValue(Integer.class) != null) {
                                                        id = subjectSnapshot.child("id").getValue(Integer.class);
                                                    }
                                                    if (subjectSnapshot.child("subject").getValue(String.class) != null) {
                                                        subject = subjectSnapshot.child("subject").getValue(String.class);
                                                    }
                                                    if ((Boolean) subjectSnapshot.child("isSelect").getValue() != null) {
                                                        isSelect = (Boolean) subjectSnapshot.child("isSelect").getValue();
                                                    }

                                                    SubjectListModel model = new SubjectListModel();

                                                    model.setId(id);
                                                    model.setSubject(subject);
                                                    model.setSelect(isSelect);

                                                    subjectlist.add(model);
                                                }


                                            }
                                            LeaderBoardModel leaderBoardModel = new LeaderBoardModel();

                                            double scoring;
                                            if (!score.equals("")) {
                                                scoring = Double.parseDouble(score);
                                            } else {
                                                scoring = Double.parseDouble("0.0");
                                            }

                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
                                            Date d = null;
                                            try {
                                                d = simpleDateFormat.parse(TimeTake);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }


                                            String c_date = new SimpleDateFormat("E, dd MMM yyyy hh:mm a", Locale.getDefault()).format(new Date());
//                                            Customlog.showlogD("USER_DETAILS",c_date.toLowerCase().substring(8,16)+" "+date.toLowerCase().substring(8,16));
                                            if (c_date.toLowerCase().substring(8,16).equals(date.toLowerCase().substring(8,16))) {

                                                leaderBoardModel.setDate(date);
                                                leaderBoardModel.setScore(scoring);
                                                leaderBoardModel.setTimeTake(d);
                                                leaderBoardModel.setUid(uid);
                                                leaderBoardModel.setSubjectList(subjectlist);


                                                leaderBoardModelListTemp.add(leaderBoardModel);
                                            }
                                        }
                                        Collections.sort(leaderBoardModelListTemp);
                                        Customlog.showlogD("USER_DETAILS",""+leaderBoardModelListTemp.size());
                                        List<LeaderBoardModel> shortlist = new ArrayList<>();

                                        List<String> UniqueIDs = new ArrayList<String>();

                                        for (int i=0;i<leaderBoardModelListTemp.size();i++) {
                                            LeaderBoardModel leaderBoardModel=leaderBoardModelListTemp.get(i);
                                            if (UniqueIDs.contains(leaderBoardModel.getUid()) == false){
                                                UniqueIDs.add(leaderBoardModel.getUid());
                                                shortlist.add(leaderBoardModel);
                                            }
                                        }

                                        List<LeaderBoardModel> shortlistFinal = new ArrayList<>();

                                        if (shortlist.size() < 500) {
                                            shortlistFinal = shortlist;
                                        } else {
                                            for (int k=0; k<500; k++){
                                                shortlistFinal.add(shortlist.get(k));
                                            }
                                        }

                                        leaderBoardModelList.clear();
                                        leaderBoardModelList.addAll(shortlistFinal);

                                        adapterForAllTime.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    binding.spinKit.setVisibility(View.GONE);
                                }
                            });


                        }

                    }
                    else {
                        binding.spinKit.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    binding.spinKit.setVisibility(View.GONE);
                }
            });
        }else {
            new CustomMessage(getActivity(),"No internet connection");
        }
    }

    private boolean printDifference(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            if (date1.before(date2)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void OnLeaderBoardItemClickAllTime(LeaderBoardModel leaderBoardModel) {

    }

    @Override
    public void OnLeaderBoardItemClickMonthlyTop(LeaderBoardModel leaderBoardModel) {
        List<LeaderBoardModel> userLeaderBoardModelListTemp =   new ArrayList<>();
        Bundle bundle = new Bundle();
        int question = 0;
        for (int l=0 ; l<leaderBoardModelListTemp.size(); l++){
            Log.d("size_",leaderBoardModelListTemp.get(l).getUid() + " -- "+l+" -- " + leaderBoardModel.getUid());
            if (leaderBoardModelListTemp.get(l).getUid().equals(leaderBoardModel.getUid())){
                //    Log.d("size_",leaderBoardModelListTemp.get(l).getUid() + " -- " + leaderBoardModel.getUid());
                //  userLeaderBoardModelListTemp.add(leaderBoardModelListTemp.get(l));

                String leaderboaditem = new Gson().toJson(leaderBoardModelListTemp.get(l));
                bundle.putString("q"+question, leaderboaditem);
                question++;

            }
        }

        String leaderboaditem = new Gson().toJson(leaderBoardModel);
        bundle.putString("leader_item", leaderboaditem);

        UIHelper.changeActivty(getContext(), LeaderboardinfoActivity.class, bundle);
    }

    @Override
    public void OnLeaderBoardItemClickWeeklyTop(LeaderBoardModel leaderBoardModel) {

    }

    @Override
    public void OnLeaderBoardItemClickWeeklyContestTop(LeaderBoardModel leaderBoardModel) {

    }
}
