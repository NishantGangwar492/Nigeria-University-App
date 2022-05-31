package com.iotait.schoolapp.ui.homepage.ui.question;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.databinding.FragmentQuestionBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.FragmentHelper;
import com.iotait.schoolapp.helper.NetworkHelper;
import com.iotait.schoolapp.ui.homepage.ui.question.adapter.RecyclerSubjectAdapter;
import com.iotait.schoolapp.ui.homepage.ui.question.adapter.RecyclerYearItemAdapter;
import com.iotait.schoolapp.ui.homepage.ui.question.view.AddQuestionView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment implements AddQuestionView, View.OnClickListener {

    private FragmentQuestionBinding questionBinding;
    private String Selected_year = "";
    private String selected_subject = "";
    private List<String> subjectList;
    private RecyclerSubjectAdapter subjectAdapter;
    String PremiumType = "";
    boolean isPremium;
    String expireDate = "";
    Date date1 = null;
    Date date2 = null;

    public QuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        questionBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_question, container, false);
        return questionBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Selected_year = "";
        selected_subject = "";


        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        PremiumType = pref.getString("premiumType", null);
        isPremium = pref.getBoolean("isPremium", false);
        expireDate = pref.getString("expire_date", null);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMM yyyy hh:mm a");
        String c_date = new SimpleDateFormat("E, dd MMM yyyy hh:mm a", Locale.getDefault()).format(new Date());

        if (expireDate != null && !expireDate.equals("")) {


            try {
                date1 = simpleDateFormat.parse(c_date);
                date2 = simpleDateFormat.parse(expireDate);


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        questionBinding.includeToolbar.toolbarTitle.setText("STUDY QUESTIONS");
        ((AppCompatActivity) getActivity()).setSupportActionBar(questionBinding.includeToolbar.toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        questionBinding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(questionBinding.getRoot(), R.id.action_questionFragment_to_nav_home, bundle);
            }
        });
        yearList = new ArrayList<>();
        subjectList = new ArrayList<>();
        subjectAdapter = new RecyclerSubjectAdapter(getContext(), subjectList, QuestionFragment.this);
        subjectAdapter.setHasStableIds(true);
        questionBinding.recyclerSubjectlist.setLayoutManager(new LinearLayoutManager(getContext()));
        questionBinding.recyclerSubjectlist.setHasFixedSize(true);
        questionBinding.recyclerSubjectlist.setNestedScrollingEnabled(false);
        questionBinding.recyclerSubjectlist.setAdapter(subjectAdapter);
        questionBinding.txtSelectedYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showYearDailog();
            }
        });

        if (isPremium && printDifference(date1, date2)) {
            if (PremiumType.equals("p1")) {

                AppController.getFirebaseHelper().getSubject().keepSynced(true);
                getSubjectDataforP1();
            }
            if (PremiumType.equals("p0")) {
                AppController.getFirebaseHelper().getSubject().keepSynced(false);
                getSubjectDataforP0();
            }
            if (PremiumType.equals("p2")) {
                AppController.getFirebaseHelper().getSubject().keepSynced(true);
                getSubjectDataforP2();
            }
        } else {
            AppController.getFirebaseHelper().getSubject().keepSynced(true);
            getSubjectDataforNopremium();
        }

        questionBinding.btnNext.setOnClickListener(this);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(questionBinding.getRoot(), R.id.action_questionFragment_to_nav_home, bundle);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private void getSubjectDataforNopremium() {
        if (NetworkHelper.hasNetworAccess(getActivity())) {
            questionBinding.spinKitSubject.setVisibility(View.VISIBLE);
            AppController.getFirebaseHelper().getSubject().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectList.clear();
                    questionBinding.spinKitSubject.setVisibility(View.GONE);
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String subject = dataSnapshot1.child("subject").getValue(String.class);
                            subjectList.add(subject);
                        }
                        subjectAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    questionBinding.spinKitSubject.setVisibility(View.GONE);

                }
            });
        }
    }

    private void getSubjectDataforP2() {
        if (NetworkHelper.hasNetworAccess(getActivity())) {
            questionBinding.spinKitSubject.setVisibility(View.VISIBLE);
            AppController.getFirebaseHelper().getSubject().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectList.clear();
                    questionBinding.spinKitSubject.setVisibility(View.GONE);
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String subject = dataSnapshot1.child("subject").getValue(String.class);
                            subjectList.add(subject);
                        }
                        subjectAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    questionBinding.spinKitSubject.setVisibility(View.GONE);

                }
            });
        } else {

            questionBinding.spinKitSubject.setVisibility(View.GONE);
            AppController.getFirebaseHelper().getSubject().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectList.clear();
                    questionBinding.spinKitSubject.setVisibility(View.GONE);
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String subject = dataSnapshot1.child("subject").getValue(String.class);
                            subjectList.add(subject);
                        }
                        subjectAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    questionBinding.spinKitSubject.setVisibility(View.GONE);

                }
            });
        }
    }

    private void getSubjectDataforP0() {
        if (NetworkHelper.hasNetworAccess(getActivity())) {
            questionBinding.spinKitSubject.setVisibility(View.VISIBLE);
            AppController.getFirebaseHelper().getSubject().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectList.clear();
                    questionBinding.spinKitSubject.setVisibility(View.GONE);
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String subject = dataSnapshot1.child("subject").getValue(String.class);
                            subjectList.add(subject);
                        }
                        subjectAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    questionBinding.spinKitSubject.setVisibility(View.GONE);

                }
            });
        }
    }

    private void getSubjectDataforP1() {
        if (NetworkHelper.hasNetworAccess(getActivity())) {
            questionBinding.spinKitSubject.setVisibility(View.VISIBLE);
            AppController.getFirebaseHelper().getSubject().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectList.clear();
                    questionBinding.spinKitSubject.setVisibility(View.GONE);
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String subject = dataSnapshot1.child("subject").getValue(String.class);
                            subjectList.add(subject);
                        }
                        subjectAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    questionBinding.spinKitSubject.setVisibility(View.GONE);

                }
            });
        } else {

            questionBinding.spinKitSubject.setVisibility(View.GONE);
            AppController.getFirebaseHelper().getSubject().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectList.clear();
                    questionBinding.spinKitSubject.setVisibility(View.GONE);
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String subject = dataSnapshot1.child("subject").getValue(String.class);
                            subjectList.add(subject);
                        }
                        subjectAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    questionBinding.spinKitSubject.setVisibility(View.GONE);

                }
            });
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

    private void getSubjectData() {

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

    private List<String> yearList;
//    private void getYearData() {
//        if (NetworkHelper.hasNetworAccess(getContext())){
//            if (spinKitViewYear!=null){
//                spinKitViewYear.setVisibility(View.VISIBLE);
//            }
//            AppController.getFirebaseHelper().getYears().addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    yearList.clear();
//                    if (spinKitViewYear!=null){
//                        spinKitViewYear.setVisibility(View.GONE);
//                    }
//                    if (dataSnapshot.exists()) {
//                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                            String year = dataSnapshot1.child("year").getValue(String.class);
//                            yearList.add(year);
//                        }
//                        if (recyclerYearItemAdapter!=null)
//                            recyclerYearItemAdapter.notifyDataSetChanged();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    if (spinKitViewYear!=null){
//                        spinKitViewYear.setVisibility(View.GONE);
//                    }
//                }
//            });
//        }
//        else {
//            if (spinKitViewYear != null) {
//                spinKitViewYear.setVisibility(View.VISIBLE);
//            }
//            AppController.getFirebaseHelper().getYears().addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    yearList.clear();
//                    if (spinKitViewYear != null) {
//                        spinKitViewYear.setVisibility(View.GONE);
//                    }
//                    if (dataSnapshot.exists()) {
//                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                            String year = dataSnapshot1.child("year").getValue(String.class);
//                            yearList.add(year);
//                        }
//                        if (recyclerYearItemAdapter != null)
//                            recyclerYearItemAdapter.notifyDataSetChanged();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    if (spinKitViewYear != null) {
//                        spinKitViewYear.setVisibility(View.GONE);
//                    }
//                }
//            });
//            new CustomMessage(getActivity(), "You are in OffLine");
//        }
//    }

    private Dialog dialog;
    private RecyclerYearItemAdapter recyclerYearItemAdapter;
    private SpinKitView spinKitViewYear;

    private void showYearDailog() {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_year_view);
        dialog.setCanceledOnTouchOutside(true);
        RecyclerView recyclerView = dialog.findViewById(R.id.yearlist);
        spinKitViewYear = dialog.findViewById(R.id.spin_kit_year);

        if (isPremium && printDifference(date1, date2)) {
            if (PremiumType.equals("p1")) {
                AppController.getFirebaseHelper().getYears().keepSynced(true);
                getYearDataP1();
            }
            if (PremiumType.equals("p0")) {
                AppController.getFirebaseHelper().getYears().keepSynced(false);
                getYearDataP0();
            }
            if (PremiumType.equals("p2")) {
                AppController.getFirebaseHelper().getYears().keepSynced(true);
                getYearDataP2();
            }
        } else {
            AppController.getFirebaseHelper().getYears().keepSynced(true);
            getYearDataforNopremium();
        }

        recyclerYearItemAdapter = new RecyclerYearItemAdapter(getContext(), yearList, QuestionFragment.this);
        recyclerYearItemAdapter.setHasStableIds(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerYearItemAdapter);

        final TextView txt_title = dialog.findViewById(R.id.txt_title);
        txt_title.setText(getResources().getString(R.string.select_year));
        dialog.show();
    }

    private void getYearDataP0() {
        if (NetworkHelper.hasNetworAccess(getContext())) {
            if (spinKitViewYear != null) {
                spinKitViewYear.setVisibility(View.VISIBLE);
            }
            AppController.getFirebaseHelper().getYears().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    yearList.clear();
                    if (spinKitViewYear != null) {
                        spinKitViewYear.setVisibility(View.GONE);
                    }
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String year = dataSnapshot1.child("year").getValue(String.class);
                            yearList.add(year);
                        }
                        if (recyclerYearItemAdapter != null)
                            recyclerYearItemAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    if (spinKitViewYear != null) {
                        spinKitViewYear.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private void getYearDataP2() {
        if (NetworkHelper.hasNetworAccess(getContext())) {
            if (spinKitViewYear != null) {
                spinKitViewYear.setVisibility(View.VISIBLE);
            }
            AppController.getFirebaseHelper().getYears().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    yearList.clear();
                    if (spinKitViewYear != null) {
                        spinKitViewYear.setVisibility(View.GONE);
                    }
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String year = dataSnapshot1.child("year").getValue(String.class);
                            yearList.add(year);
                        }
                        if (recyclerYearItemAdapter != null)
                            recyclerYearItemAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    if (spinKitViewYear != null) {
                        spinKitViewYear.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            if (spinKitViewYear != null) {
                spinKitViewYear.setVisibility(View.VISIBLE);
            }
            AppController.getFirebaseHelper().getYears().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    yearList.clear();
                    if (spinKitViewYear != null) {
                        spinKitViewYear.setVisibility(View.GONE);
                    }
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String year = dataSnapshot1.child("year").getValue(String.class);
                            yearList.add(year);
                        }
                        if (recyclerYearItemAdapter != null)
                            recyclerYearItemAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    if (spinKitViewYear != null) {
                        spinKitViewYear.setVisibility(View.GONE);
                    }
                }
            });

        }
    }

    private void getYearDataforNopremium() {
        if (NetworkHelper.hasNetworAccess(getContext())) {
            if (spinKitViewYear != null) {
                spinKitViewYear.setVisibility(View.VISIBLE);
            }
            AppController.getFirebaseHelper().getYears().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    yearList.clear();
                    if (spinKitViewYear != null) {
                        spinKitViewYear.setVisibility(View.GONE);
                    }
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String year = dataSnapshot1.child("year").getValue(String.class);
                            yearList.add(year);
                        }
                        if (recyclerYearItemAdapter != null)
                            recyclerYearItemAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    if (spinKitViewYear != null) {
                        spinKitViewYear.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private void getYearDataP1() {
        if (NetworkHelper.hasNetworAccess(getContext())) {
            if (spinKitViewYear != null) {
                spinKitViewYear.setVisibility(View.VISIBLE);
            }
            AppController.getFirebaseHelper().getYears().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    yearList.clear();
                    if (spinKitViewYear != null) {
                        spinKitViewYear.setVisibility(View.GONE);
                    }
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String year = dataSnapshot1.child("year").getValue(String.class);
                            yearList.add(year);
                        }
                        if (recyclerYearItemAdapter != null)
                            recyclerYearItemAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    if (spinKitViewYear != null) {
                        spinKitViewYear.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            if (spinKitViewYear != null) {
                spinKitViewYear.setVisibility(View.VISIBLE);
            }
            AppController.getFirebaseHelper().getYears().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    yearList.clear();
                    if (spinKitViewYear != null) {
                        spinKitViewYear.setVisibility(View.GONE);
                    }
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String year = dataSnapshot1.child("year").getValue(String.class);
                            yearList.add(year);
                        }
                        if (recyclerYearItemAdapter != null)
                            recyclerYearItemAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    if (spinKitViewYear != null) {
                        spinKitViewYear.setVisibility(View.GONE);
                    }
                }
            });

        }
    }

    private void showUpgradeDialog(){
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_full_exam_upgrade);
        dialog.setCanceledOnTouchOutside(false);

        final TextView textView = dialog.findViewById(R.id.benefit_title);
        final Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        final Button btn_ok = dialog.findViewById(R.id.btn_ok);
        textView.setText(getString(R.string.year_select_title));

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(questionBinding.getRoot(), R.id.action_questionFragment_to_premiumProcessFragment, bundle);
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onYearSelected(String year) {
        if (isPremium){
            questionBinding.txtSelectedYear.setText(year);
            questionBinding.txtSelectedYear.setError(null);
            Selected_year = year;
            if (dialog != null)
                dialog.dismiss();
        } else {
            switch (year) {
                case "2005/2006":
                case "2006/2007":
                case "2007/2008":
                case "2008/2009":
                    questionBinding.txtSelectedYear.setText(year);
                    questionBinding.txtSelectedYear.setError(null);
                    Selected_year = year;
                    if (dialog != null)
                        dialog.dismiss();
                    break;
                default:
                    if (dialog != null)
                        dialog.dismiss();
                    showUpgradeDialog();
            }
        }
    }

    @Override
    public void onSubjectSelected(String subject) {
        selected_subject = subject;
    }

    @Override
    public void onQuestionReport(String questionid, String s) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_next:
                if (!TextUtils.equals(Selected_year, "") && !TextUtils.equals(selected_subject, "")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("year", Selected_year);
                    bundle.putString("subject", selected_subject);
                    FragmentHelper.changeFragmet(questionBinding.getRoot(), R.id.action_questionFragment_to_questionDetailsFragment, bundle);
                } else {
                    new CustomMessage(getActivity(), "Select year and subject");
                }
                break;
        }
    }
}
