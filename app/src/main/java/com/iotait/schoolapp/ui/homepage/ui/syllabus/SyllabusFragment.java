package com.iotait.schoolapp.ui.homepage.ui.syllabus;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.databinding.FragmentSyllabusBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.FirebaseHelper;
import com.iotait.schoolapp.helper.FragmentHelper;
import com.iotait.schoolapp.helper.NetworkHelper;
import com.iotait.schoolapp.ui.homepage.ui.syllabus.adapter.SyllabusAdapter;
import com.iotait.schoolapp.ui.homepage.ui.syllabus.view.SyllabusClickView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SyllabusFragment extends Fragment implements SyllabusClickView, View.OnClickListener {

    FragmentSyllabusBinding binding;
    SyllabusAdapter adapter;

    FirebaseHelper firebaseHelper;

    List<String> subjectname;
    List<String> subjectnameTemp;
    String PremiumType = "";
    String expireDate = "";
    String ExpireTimeLeft = "";

    public SyllabusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_syllabus, container, false);
        View root = binding.getRoot();

        subjectname = new ArrayList<>();
        subjectnameTemp = new ArrayList<>();

        firebaseHelper = new FirebaseHelper();

        binding.back.setOnClickListener(this);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new SyllabusAdapter(getContext(), subjectname, this);
        binding.syllabusSubject.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.syllabusSubject.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getSubjectofSyllabus();
    }

    private void getSubjectofSyllabus() {


        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        PremiumType = pref.getString("premiumType", null);
        boolean isPremium = pref.getBoolean("isPremium", false);
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
                firebaseHelper.getSyllabus().keepSynced(true);
                getSubjectforP1();
            }
            if (PremiumType.equals("p0")) {
                firebaseHelper.getSyllabus().keepSynced(false);
                getSubjectforP0();
            }
            if (PremiumType.equals("p2")) {
                firebaseHelper.getSyllabus().keepSynced(true);
                getSubjectforP2();
            }
        } else {
            firebaseHelper.getSyllabus().keepSynced(true);
            getSubjectforNoPremium();
        }
    }

    private void getSubjectforNoPremium() {
        if (NetworkHelper.hasNetworAccess(getContext())) {
            binding.spinKit.setVisibility(View.VISIBLE);
            firebaseHelper.getSyllabus().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectnameTemp.clear();
                    if (dataSnapshot.exists()) {
                        binding.spinKit.setVisibility(View.GONE);
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String key = snapshot.getKey();
                            subjectnameTemp.add(key);
                        }

                        subjectname.clear();
                        subjectname.addAll(subjectnameTemp);
                        adapter.notifyDataSetChanged();

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
            binding.spinKit.setVisibility(View.GONE);
            new CustomMessage(getActivity(), "No internet is available");
        }
    }

    private void getSubjectforP2() {
        if (NetworkHelper.hasNetworAccess(getContext())) {
            binding.spinKit.setVisibility(View.VISIBLE);
            firebaseHelper.getSyllabus().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectnameTemp.clear();
                    if (dataSnapshot.exists()) {
                        binding.spinKit.setVisibility(View.GONE);
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String key = snapshot.getKey();
                            subjectnameTemp.add(key);
                        }

                        subjectname.clear();
                        subjectname.addAll(subjectnameTemp);
                        adapter.notifyDataSetChanged();

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
            binding.spinKit.setVisibility(View.VISIBLE);
            firebaseHelper.getSyllabus().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectnameTemp.clear();
                    if (dataSnapshot.exists()) {
                        binding.spinKit.setVisibility(View.GONE);
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String key = snapshot.getKey();
                            subjectnameTemp.add(key);
                        }

                        subjectname.clear();
                        subjectname.addAll(subjectnameTemp);
                        adapter.notifyDataSetChanged();

                    } else {
                        binding.spinKit.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    binding.spinKit.setVisibility(View.GONE);
                }
            });
        }
    }

    private void getSubjectforP0() {
        if (NetworkHelper.hasNetworAccess(getContext())) {
            binding.spinKit.setVisibility(View.VISIBLE);
            firebaseHelper.getSyllabus().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectnameTemp.clear();
                    if (dataSnapshot.exists()) {
                        binding.spinKit.setVisibility(View.GONE);
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String key = snapshot.getKey();
                            subjectnameTemp.add(key);
                        }

                        subjectname.clear();
                        subjectname.addAll(subjectnameTemp);
                        adapter.notifyDataSetChanged();

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
            binding.spinKit.setVisibility(View.GONE);
            new CustomMessage(getActivity(), "No internet is available");
        }
    }

    private void getSubjectforP1() {
        if (NetworkHelper.hasNetworAccess(getContext())) {
            binding.spinKit.setVisibility(View.VISIBLE);
            firebaseHelper.getSyllabus().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectnameTemp.clear();
                    if (dataSnapshot.exists()) {
                        binding.spinKit.setVisibility(View.GONE);
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String key = snapshot.getKey();
                            subjectnameTemp.add(key);
                        }

                        subjectname.clear();
                        subjectname.addAll(subjectnameTemp);
                        adapter.notifyDataSetChanged();

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
            binding.spinKit.setVisibility(View.VISIBLE);
            firebaseHelper.getSyllabus().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectnameTemp.clear();
                    if (dataSnapshot.exists()) {
                        binding.spinKit.setVisibility(View.GONE);
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String key = snapshot.getKey();
                            subjectnameTemp.add(key);
                        }

                        subjectname.clear();
                        subjectname.addAll(subjectnameTemp);
                        adapter.notifyDataSetChanged();

                    } else {
                        binding.spinKit.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    binding.spinKit.setVisibility(View.GONE);
                }
            });
        }

    }

    private boolean printDifference(Date startDate, Date endDate) {
        if (startDate != null && endDate != null) {
            if (endDate.after(startDate)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    @Override
    public void onsubjectClick(String subjectName) {
        Bundle bundle = new Bundle();
        bundle.putString("match_id", subjectName);
        FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_syllabus_to_SylaabusClickfragment, bundle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_syllabus_to_nav_home, bundle);
                break;
        }
    }
}
