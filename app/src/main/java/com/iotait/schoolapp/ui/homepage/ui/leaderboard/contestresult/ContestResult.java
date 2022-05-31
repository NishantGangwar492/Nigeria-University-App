package com.iotait.schoolapp.ui.homepage.ui.leaderboard.contestresult;

import android.os.Bundle;
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
import com.iotait.schoolapp.databinding.FragmentContestResultBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.NetworkHelper;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.ui.homepage.ui.leaderboard.LeaderboardinfoActivity;
import com.iotait.schoolapp.ui.homepage.ui.leaderboard.contestresult.adapter.LeaderBoardAdapterForContest;
import com.iotait.schoolapp.ui.homepage.ui.leaderboard.model.LeaderBoardModel;
import com.iotait.schoolapp.ui.homepage.ui.leaderboard.model.SubjectListModel;
import com.iotait.schoolapp.ui.homepage.ui.leaderboard.view.LeaderBoardView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ContestResult extends Fragment implements View.OnClickListener, LeaderBoardView {


    FragmentContestResultBinding binding;

    List<LeaderBoardModel> leaderBoardModelList;
    List<LeaderBoardModel> leaderBoardModelListTemp;

    LeaderBoardAdapterForContest contestAdapter;

    Date date1 = null;
    Date date2 = null;
    int count = 0;
    int count2 = 0;

    public ContestResult() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contest_result, container, false);
        View root = binding.getRoot();
        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        leaderBoardModelListTemp = new ArrayList<>();
        leaderBoardModelList = new ArrayList<>();

        contestAdapter = new LeaderBoardAdapterForContest(getContext(), leaderBoardModelList, ContestResult.this);
        contestAdapter.setHasStableIds(true);
        binding.contestResultRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.contestResultRecyclerView.setHasFixedSize(true);
        binding.contestResultRecyclerView.setNestedScrollingEnabled(false);
        binding.contestResultRecyclerView.setAdapter(contestAdapter);

        getContestResultTemp();
        getContestResultLeaderBoard();

    }

    private void getContestResultTemp() {
        if (NetworkHelper.hasNetworAccess(getContext())) {

            AppController.getFirebaseHelper().getWeeklyContest().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    if (dataSnapshot.exists()) {

                        for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {

                            String key = postsnapshot.getKey();
                            AppController.getFirebaseHelper().getWeeklyContest().child(key).addValueEventListener(new ValueEventListener() {
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

    private void getContestResultLeaderBoard() {
        if (NetworkHelper.hasNetworAccess(getContext())) {
            binding.spinKit.setVisibility(View.VISIBLE);

            AppController.getFirebaseHelper().getWeeklyContest().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    leaderBoardModelListTemp.clear();
                    if (dataSnapshot.exists()) {

                        for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {

                            String key = postsnapshot.getKey();
                            AppController.getFirebaseHelper().getWeeklyContest().child(key).addValueEventListener(new ValueEventListener() {
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


                                            SimpleDateFormat simpleDateFormatfor = new SimpleDateFormat("E, dd MMM yyyy hh:mm a");
                                            String c_date = new SimpleDateFormat("E, dd MMM yyyy hh:mm a", Locale.getDefault()).format(new Date());

                                            Calendar date1Calendar = Calendar.getInstance();
                                            try {
                                                date1Calendar.setTime(new SimpleDateFormat("E, dd MMM yyyy hh:mm a").parse(c_date));
                                                if (date != null && !date.equals("")) {
                                                    try {
                                                        date1 = simpleDateFormatfor.parse(simpleDateFormatfor.format(date1Calendar.getTime()));
                                                        date2 = simpleDateFormatfor.parse(date);


                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            Calendar c = Calendar.getInstance();
                                            c.setFirstDayOfWeek(Calendar.SATURDAY);
                                            c.setTime(date1);
                                            int weekOfYear = c.get(Calendar.WEEK_OF_YEAR);

                                            Calendar cex = Calendar.getInstance();
                                            cex.setFirstDayOfWeek(Calendar.SATURDAY);
//                                            cex.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                                            cex.setTime(date2);
                                            int weekOfYearEx = cex.get(Calendar.WEEK_OF_YEAR);

                                            if (weekOfYear == weekOfYearEx) {

                                                leaderBoardModel.setDate(date);
                                                leaderBoardModel.setScore(scoring);
                                                leaderBoardModel.setTimeTake(d);
                                                leaderBoardModel.setUid(uid);
                                                leaderBoardModel.setSubjectList(subjectlist);


                                                leaderBoardModelListTemp.add(leaderBoardModel);
                                            }
//                                            else {
//                                                Calendar currentTime = Calendar.getInstance();
//                                                Calendar EndTIme = Calendar.getInstance();
//                                               String endTime= "20:00:00";
//                                                try {
//                                                    currentTime.setTime(new SimpleDateFormat("hh:mm a").parse(c_date));
//                                                    EndTIme.setTime(new SimpleDateFormat("hh:mm a").parse(endTime));
//                                                } catch (ParseException e) {
//                                                    e.printStackTrace();
//                                                }
//
//                                                if (weekOfYearEx==weekOfYear-1 && printDifference(currentTime.getTime(),EndTIme.getTime())){
//                                                    leaderBoardModel.setDate(date);
//                                                    leaderBoardModel.setScore(scoring);
//                                                    leaderBoardModel.setTimeTake(d);
//                                                    leaderBoardModel.setUid(uid);
//                                                    leaderBoardModel.setSubjectList(subjectlist);
//
//
//                                                    leaderBoardModelListTemp.add(leaderBoardModel);
//                                                }
//                                            }


                                        }
                                        Collections.sort(leaderBoardModelListTemp);

                                        List<LeaderBoardModel> shortlist = new ArrayList<>();
                                        if (leaderBoardModelListTemp.size() > 50) {
                                            for (int i = 0; i < 50; i++) {
                                                LeaderBoardModel leaderBoardModel = leaderBoardModelListTemp.get(i);
                                                shortlist.add(leaderBoardModel);
                                            }
                                        } else {
                                            shortlist.addAll(leaderBoardModelListTemp);
                                        }


                                        leaderBoardModelList.clear();
                                        leaderBoardModelList.addAll(shortlist);
                                        contestAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    binding.spinKit.setVisibility(View.GONE);
                                }
                            });

                        }

                    } else {
                        binding.spinKit.setVisibility(View.GONE);
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
    public void onClick(View v) {

    }

    @Override
    public void OnLeaderBoardItemClickAllTime(LeaderBoardModel leaderBoardModel) {

    }

    @Override
    public void OnLeaderBoardItemClickMonthlyTop(LeaderBoardModel leaderBoardModel) {

    }

    @Override
    public void OnLeaderBoardItemClickWeeklyTop(LeaderBoardModel leaderBoardModel) {

    }

    @Override
    public void OnLeaderBoardItemClickWeeklyContestTop(LeaderBoardModel leaderBoardModel) {
        Bundle bundle = new Bundle();
        String leaderboaditem = new Gson().toJson(leaderBoardModel);
        bundle.putString("leader_item", leaderboaditem);
        UIHelper.changeActivty(getContext(), LeaderboardinfoActivity.class, bundle);
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
}