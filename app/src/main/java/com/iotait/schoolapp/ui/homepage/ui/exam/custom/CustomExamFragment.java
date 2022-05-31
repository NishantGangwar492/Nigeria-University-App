package com.iotait.schoolapp.ui.homepage.ui.exam.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.akexorcist.snaptimepicker.SnapTimePickerDialog;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.databinding.FragmentCustomExamBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.FragmentHelper;
import com.iotait.schoolapp.helper.NetworkHelper;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.ui.homepage.ui.exam.custom.adapters.RecyclerSubjectItemAdapter;
import com.iotait.schoolapp.ui.homepage.ui.question.adapter.RecyclerYearItemAdapter;
import com.iotait.schoolapp.ui.homepage.ui.question.view.AddQuestionView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomExamFragment extends Fragment implements AddQuestionView {

    private FragmentCustomExamBinding customExamBinding;
    private SnapTimePickerDialog snapTimePickerDialog;
    private long h,m,total=0;
    private boolean isYear, isSubject, isNumberOfQuestion, isDuration=false;


    String PremiumType = "";
    boolean isPremium;
    String expireDate = "";
    Date date1 = null;
    Date date2 = null;
    public CustomExamFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        customExamBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_custom_exam, container, false);


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
        return customExamBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customExamBinding.txtSelectedYear.setText("");
        customExamBinding.txtSelectedYear.setText("");
        yearList = new ArrayList<>();
        subjectList = new ArrayList<>();

        customExamBinding.includeToolbar.toolbarTitle.setText("CUSTOM EXAM");
        ((AppCompatActivity) getActivity()).setSupportActionBar(customExamBinding.includeToolbar.toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        customExamBinding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(customExamBinding.getRoot(), R.id.action_customExamFragment_to_takeExamFragment, bundle);
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(customExamBinding.getRoot(), R.id.action_customExamFragment_to_takeExamFragment, bundle);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        customExamBinding.txtSelectedYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showYearDailog();
            }
        });

        customExamBinding.txtSelectedSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSubjectDailog();
            }
        });

        customExamBinding.pikedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snapTimePickerDialog= new SnapTimePickerDialog.Builder().setTitle(R.string.select_time)
                        .setSuffix(R.string.sufex)
                        .setPrefix(R.string.prefex)
                        .setThemeColor(R.color.add_slider_text_color)
                        .build();
                snapTimePickerDialog.setListener(new SnapTimePickerDialog.Listener() {
                    @Override
                    public void onTimePicked(int i, int i1) {
                        h=i*3600000;
                        m=i1*60000;
                        total=h+m;
                        customExamBinding.pikedTime.setText(String.valueOf(i)+" hours : "+String.valueOf(i1)+" minutes");
                        customExamBinding.pikedTime.setError(null);
                    }
                });
                snapTimePickerDialog.show(getChildFragmentManager(), SnapTimePickerDialog.TAG);
            }
        });

        customExamBinding.btnStartExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.setupUI(getActivity(),customExamBinding.getRoot());
                startChecking();
            }
        });
    }

    private void startChecking() {
        if (TextUtils.isEmpty(customExamBinding.txtSelectedYear.getText().toString())){
            customExamBinding.txtSelectedYear.setError("Please select year");
            isYear=false;
        }
        else {
            isYear=true;
            customExamBinding.txtSelectedYear.setError(null);
        }

        if (TextUtils.isEmpty(customExamBinding.txtSelectedSubject.getText().toString())){
            customExamBinding.txtSelectedSubject.setError("Please select subject");
            isSubject=false;
        } else {
            isSubject=true;
            customExamBinding.txtSelectedSubject.setError(null);
        }

        if (total==0){
            customExamBinding.pikedTime.setError("Please select duration");
            isDuration=false;
        }
        else {
            isDuration=true;
            customExamBinding.pikedTime.setError(null);

        }
        if (isYear && isSubject && isDuration){
            Bundle bundle = new Bundle();
            bundle.putLong("duration", total);
            bundle.putString("state", "custom_exam");
            bundle.putString("year", customExamBinding.txtSelectedYear.getText().toString());
            bundle.putString("subject", customExamBinding.txtSelectedSubject.getText().toString());
            FragmentHelper.changeFragmet(customExamBinding.getRoot(), R.id.action_customExamFragment_to_customExamDetailsFragment, bundle);
        }

    }

    private void getYearData() {
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
            new CustomMessage(getActivity(), "No internet available");
        }
    }

    private Dialog dialog;
    private RecyclerYearItemAdapter recyclerYearItemAdapter;
    private SpinKitView spinKitViewYear;

    private List<String> yearList;
    private List<String> subjectList;

    private void showYearDailog() {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_year_view);
        dialog.setCanceledOnTouchOutside(true);

        RecyclerView recyclerView = dialog.findViewById(R.id.yearlist);
        TextView title = dialog.findViewById(R.id.txt_title);
        title.setText(getResources().getString(R.string.select_year));
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


        // getYearData();
        recyclerYearItemAdapter = new RecyclerYearItemAdapter(getContext(), yearList, CustomExamFragment.this);
        recyclerYearItemAdapter.setHasStableIds(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerYearItemAdapter);
        dialog.show();
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
        } else {
            new CustomMessage(getActivity(), "No internet available");
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
        } else {
            new CustomMessage(getActivity(), "No internet available");
        }
    }

    RecyclerSubjectItemAdapter recyclerSubjectItemAdapter;
    private SpinKitView spinKitViewSubject;

    private void showSubjectDailog() {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_year_view);
        dialog.setCanceledOnTouchOutside(true);


        RecyclerView recyclerView = dialog.findViewById(R.id.yearlist);
        spinKitViewSubject = dialog.findViewById(R.id.spin_kit_year);


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

        recyclerSubjectItemAdapter = new RecyclerSubjectItemAdapter(getContext(), subjectList, this);
        recyclerSubjectItemAdapter.setHasStableIds(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerSubjectItemAdapter);


        final TextView txt_title = dialog.findViewById(R.id.txt_title);
        txt_title.setText(getResources().getString(R.string.select_subject));
        dialog.show();
    }

    private void getSubjectDataforNopremium() {
        if (NetworkHelper.hasNetworAccess(getContext())) {
            if (spinKitViewSubject != null) {
                spinKitViewSubject.setVisibility(View.VISIBLE);
            }
            AppController.getFirebaseHelper().getSubject().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectList.clear();
                    if (spinKitViewSubject != null) {
                        spinKitViewSubject.setVisibility(View.GONE);
                    }
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String subject = dataSnapshot1.child("subject").getValue(String.class);
                            subjectList.add(subject);
                        }
                        if (recyclerSubjectItemAdapter != null)
                            recyclerSubjectItemAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    if (spinKitViewSubject != null) {
                        spinKitViewSubject.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            new CustomMessage(getActivity(), "No internet available");
        }
    }

    private void getSubjectDataforP2() {
        if (NetworkHelper.hasNetworAccess(getContext())) {
            if (spinKitViewSubject != null) {
                spinKitViewSubject.setVisibility(View.VISIBLE);
            }
            AppController.getFirebaseHelper().getSubject().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectList.clear();
                    if (spinKitViewSubject != null) {
                        spinKitViewSubject.setVisibility(View.GONE);
                    }
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String subject = dataSnapshot1.child("subject").getValue(String.class);
                            subjectList.add(subject);
                        }
                        if (recyclerSubjectItemAdapter != null)
                            recyclerSubjectItemAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    if (spinKitViewSubject != null) {
                        spinKitViewSubject.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            AppController.getFirebaseHelper().getSubject().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectList.clear();
                    if (spinKitViewSubject != null) {
                        spinKitViewSubject.setVisibility(View.GONE);
                    }
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String subject = dataSnapshot1.child("subject").getValue(String.class);
                            subjectList.add(subject);
                        }
                        if (recyclerSubjectItemAdapter != null)
                            recyclerSubjectItemAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    if (spinKitViewSubject != null) {
                        spinKitViewSubject.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private void getSubjectDataforP0() {
        if (NetworkHelper.hasNetworAccess(getContext())) {
            if (spinKitViewSubject != null) {
                spinKitViewSubject.setVisibility(View.VISIBLE);
            }
            AppController.getFirebaseHelper().getSubject().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectList.clear();
                    if (spinKitViewSubject != null) {
                        spinKitViewSubject.setVisibility(View.GONE);
                    }
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String subject = dataSnapshot1.child("subject").getValue(String.class);
                            subjectList.add(subject);
                        }
                        if (recyclerSubjectItemAdapter != null)
                            recyclerSubjectItemAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    if (spinKitViewSubject != null) {
                        spinKitViewSubject.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            new CustomMessage(getActivity(), "No internet available");
        }
    }

    private void getSubjectDataforP1() {
        if (NetworkHelper.hasNetworAccess(getContext())) {
            if (spinKitViewSubject != null) {
                spinKitViewSubject.setVisibility(View.VISIBLE);
            }
            AppController.getFirebaseHelper().getSubject().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectList.clear();
                    if (spinKitViewSubject != null) {
                        spinKitViewSubject.setVisibility(View.GONE);
                    }
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String subject = dataSnapshot1.child("subject").getValue(String.class);
                            subjectList.add(subject);
                        }
                        if (recyclerSubjectItemAdapter != null)
                            recyclerSubjectItemAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    if (spinKitViewSubject != null) {
                        spinKitViewSubject.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            AppController.getFirebaseHelper().getSubject().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectList.clear();
                    if (spinKitViewSubject != null) {
                        spinKitViewSubject.setVisibility(View.GONE);
                    }
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String subject = dataSnapshot1.child("subject").getValue(String.class);
                            subjectList.add(subject);
                        }
                        if (recyclerSubjectItemAdapter != null)
                            recyclerSubjectItemAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    if (spinKitViewSubject != null) {
                        spinKitViewSubject.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    @Override
    public void onYearSelected(String year) {
        if (isPremium){
            customExamBinding.txtSelectedYear.setText(year);
            if (dialog != null) {
                dialog.dismiss();
            }
        } else {
            switch (year) {
                case "2005/2006":
                case "2006/2007":
                case "2007/2008":
                case "2008/2009":
                    customExamBinding.txtSelectedYear.setText(year);
                    if (dialog != null) {
                        dialog.dismiss();
                    }
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
        customExamBinding.txtSelectedSubject.setText(subject);
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onQuestionReport(String questionid, String s) {

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

    private boolean printDifference(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            return date2.after(date1);
        } else {
            return false;
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
                FragmentHelper.changeFragmet(customExamBinding.getRoot(), R.id.action_customExamFragment_to_premiumProcessFragment, bundle);
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
}
