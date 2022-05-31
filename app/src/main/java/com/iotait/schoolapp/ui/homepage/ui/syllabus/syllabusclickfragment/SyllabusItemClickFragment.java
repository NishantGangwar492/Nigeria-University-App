package com.iotait.schoolapp.ui.homepage.ui.syllabus.syllabusclickfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.databinding.FragmentSyllabusItemClickBinding;
import com.iotait.schoolapp.helper.FragmentHelper;
import com.iotait.schoolapp.ui.homepage.ui.syllabus.syllabusclickfragment.model.SyllabusModel;
import com.iotait.schoolapp.ui.homepage.ui.syllabus.syllabusclickfragment.view.AddSyllabusView;

import java.util.ArrayList;
import java.util.List;


public class SyllabusItemClickFragment extends Fragment implements AddSyllabusView, View.OnClickListener {

    FragmentSyllabusItemClickBinding binding;
    String SubjectName = "";
    private List<String> yearList;

    public SyllabusItemClickFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_syllabus_item_click, container, false);

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            SubjectName = bundle.getString("match_id");
        }
        View root = binding.getRoot();

        yearList = new ArrayList<>();

        binding.toolbarSyllabus.setText(SubjectName + " Syllabus");
        binding.back.setOnClickListener(this);
        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        getSyllabus();
    }

    @Override
    public void onYearSelected(String year) {

    }

    private void getSyllabus() {
        binding.spinKit.setVisibility(View.VISIBLE);
        AppController.getFirebaseHelper().getSyllabus().child(SubjectName).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            binding.spinKit.setVisibility(View.GONE);

                            SyllabusModel model = dataSnapshot.getValue(SyllabusModel.class);
                            binding.syllabustext.setText(model.getSyllabus());
                        } else {
                            binding.spinKit.setVisibility(View.GONE);
                            binding.syllabustext.setText("No Syllabus Added Yet");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        binding.spinKit.setVisibility(View.GONE);
                    }
                }
        );
    }

    @Override
    public void onSubjectSelected(String subject) {

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.subjectview.setText(SubjectName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_SylaabusClickfragment_to_syllabus, bundle);
                break;
        }
    }
}
