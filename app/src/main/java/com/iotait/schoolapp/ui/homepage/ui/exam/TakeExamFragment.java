package com.iotait.schoolapp.ui.homepage.ui.exam;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.databinding.FragmentTakeExamBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.FragmentHelper;
import com.iotait.schoolapp.helper.NetworkHelper;
import com.iotait.schoolapp.session.SessionManager;
import com.iotait.schoolapp.ui.homepage.ui.exam.adapter.RecyclerExamSubjectAdapter;
import com.iotait.schoolapp.ui.homepage.ui.exam.models.SelectItem;
import com.iotait.schoolapp.ui.homepage.ui.exam.view.ExamSelectionView;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class TakeExamFragment extends Fragment implements View.OnClickListener, ExamSelectionView {
    
    private FragmentTakeExamBinding takeExamBinding;
    String PremiumType = "";
    boolean isPremium;
    String expireDate = "";
    Date date1 = null;
    Date date2 = null;
    private Dialog dialog;
    private RecyclerExamSubjectAdapter recyclerExamSubjectAdapter;
    private SpinKitView spinKitView;
    private List<String> subjectList;
    private List<SelectItem> selectList;
    private SessionManager sessionManager;


    public TakeExamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        takeExamBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_take_exam,container,false);


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
        return takeExamBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sessionManager = new SessionManager(getContext());
        takeExamBinding.includeToolbar.toolbarTitle.setText("TAKE AN EXAM");
        ((AppCompatActivity) getActivity()).setSupportActionBar(takeExamBinding.includeToolbar.toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        takeExamBinding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(takeExamBinding.getRoot(), R.id.action_takeExamFragment_to_nav_home, bundle);
            }
        });

        takeExamBinding.tvfull.setOnClickListener(this);
        takeExamBinding.tvcustom.setOnClickListener(this);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(takeExamBinding.getRoot(), R.id.action_takeExamFragment_to_nav_home, bundle);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
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

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.tvfull:
                if (PremiumType.equals("p0")){
                    if (sessionManager.getFullExamCount()<=2)
                        showDialog();
                    else
                        showUpgradeDialog();
                } else {
                    showDialog();
                }
                break;
            case R.id.tvcustom:
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(takeExamBinding.getRoot(), R.id.action_takeExamFragment_to_customExamFragment, bundle);
                break;
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

        final Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        final Button btn_ok = dialog.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(takeExamBinding.getRoot(), R.id.action_takeExamFragment_to_premiumProcessFragment, bundle);
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

    private void showDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = new Dialog(getContext());
        selectList=new ArrayList<>();
        subjectList=new ArrayList<>();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_exam_subject_view);
        dialog.setCanceledOnTouchOutside(false);

        RecyclerView recyclerView = dialog.findViewById(R.id.exam_subject_list);
        spinKitView = dialog.findViewById(R.id.spin_kit);

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

        //  getSubjectData();
        recyclerExamSubjectAdapter = new RecyclerExamSubjectAdapter(getContext(), subjectList, TakeExamFragment.this);
        recyclerExamSubjectAdapter.setHasStableIds(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerExamSubjectAdapter);

        final Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        final Button btn_ok = dialog.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectList!=null && selectList.size()==4){
                    dialog.dismiss();
                    String list = new Gson().toJson(selectList);
                    Bundle bundle = new Bundle();
                    bundle.putString("selectlist", list);
                    bundle.putLong("duration", 3600000);
                    bundle.putString("state", "full_exam");
                    FragmentHelper.changeFragmet(takeExamBinding.getRoot(), R.id.action_takeExamFragment_to_fullExamFragment, bundle);
                }
                else {
                    new CustomMessage(getActivity(),"Select [4] subjects");
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectList.clear();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getSubjectDataforNopremium() {
        if (NetworkHelper.hasNetworAccess(getActivity())) {
            if (spinKitView != null) {
                spinKitView.setVisibility(View.VISIBLE);
            }
            AppController.getFirebaseHelper().getSubject().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectList.clear();
                    if (spinKitView != null) {
                        spinKitView.setVisibility(View.GONE);
                    }
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String subject = dataSnapshot1.child("subject").getValue(String.class);
                            subjectList.add(subject);
                        }
                        recyclerExamSubjectAdapter.notifyDataSetChanged();
                    } else {
                        if (dialog != null) {
                            dialog.dismiss();
                            recyclerExamSubjectAdapter.notifyDataSetChanged();
                            new CustomMessage(getActivity(), "No subject available");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    if (spinKitView != null) {
                        spinKitView.setVisibility(View.GONE);
                    }
                    if (dialog != null) {
                        dialog.dismiss();
                        recyclerExamSubjectAdapter.notifyDataSetChanged();
                        new CustomMessage(getActivity(), "No subject available");
                    }

                }
            });
        } else {
            new CustomMessage(getActivity(), "No internet available");
        }
    }

    private void getSubjectDataforP2() {
        if (NetworkHelper.hasNetworAccess(getActivity())) {
            if (spinKitView != null) {
                spinKitView.setVisibility(View.VISIBLE);
            }
            AppController.getFirebaseHelper().getSubject().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectList.clear();
                    if (spinKitView != null) {
                        spinKitView.setVisibility(View.GONE);
                    }
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String subject = dataSnapshot1.child("subject").getValue(String.class);
                            subjectList.add(subject);
                        }
                        recyclerExamSubjectAdapter.notifyDataSetChanged();
                    } else {
                        if (dialog != null) {
                            dialog.dismiss();
                            recyclerExamSubjectAdapter.notifyDataSetChanged();
                            new CustomMessage(getActivity(), "No subject available");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    if (spinKitView != null) {
                        spinKitView.setVisibility(View.GONE);
                    }
                    if (dialog != null) {
                        dialog.dismiss();
                        recyclerExamSubjectAdapter.notifyDataSetChanged();
                        new CustomMessage(getActivity(), "No subject available");
                    }
                }
            });
        } else {
            AppController.getFirebaseHelper().getSubject().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectList.clear();
                    if (spinKitView != null) {
                        spinKitView.setVisibility(View.GONE);
                    }
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String subject = dataSnapshot1.child("subject").getValue(String.class);
                            subjectList.add(subject);
                        }
                        recyclerExamSubjectAdapter.notifyDataSetChanged();
                    } else {
                        if (dialog != null) {
                            dialog.dismiss();
                            recyclerExamSubjectAdapter.notifyDataSetChanged();
                            new CustomMessage(getActivity(), "No subject available");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    if (spinKitView != null) {
                        spinKitView.setVisibility(View.GONE);
                    }
                    if (dialog != null) {
                        dialog.dismiss();
                        recyclerExamSubjectAdapter.notifyDataSetChanged();
                        new CustomMessage(getActivity(), "No subject available");
                    }

                }
            });
        }
    }

    private void getSubjectDataforP0() {
        if (NetworkHelper.hasNetworAccess(getActivity())){
            if (spinKitView!=null){
                spinKitView.setVisibility(View.VISIBLE);
            }
            AppController.getFirebaseHelper().getSubject().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectList.clear();
                    if (spinKitView!=null){
                        spinKitView.setVisibility(View.GONE);
                    }
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String subject = dataSnapshot1.child("subject").getValue(String.class);
                            subjectList.add(subject);
                        }
                        recyclerExamSubjectAdapter.notifyDataSetChanged();
                    }
                    else {
                        if (dialog!=null){
                            dialog.dismiss();
                            recyclerExamSubjectAdapter.notifyDataSetChanged();
                            new CustomMessage(getActivity(),"No subject available");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    if (spinKitView!=null){
                        spinKitView.setVisibility(View.GONE);
                    }
                    if (dialog!=null){
                        dialog.dismiss();
                        recyclerExamSubjectAdapter.notifyDataSetChanged();
                        new CustomMessage(getActivity(),"No subject available");
                    }

                }
            });
        }
        else {
            new CustomMessage(getActivity(), "No internet available");
        }
    }

    private void getSubjectDataforP1() {
        if (NetworkHelper.hasNetworAccess(getActivity())) {
            if (spinKitView != null) {
                spinKitView.setVisibility(View.VISIBLE);
            }
            AppController.getFirebaseHelper().getSubject().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectList.clear();
                    if (spinKitView != null) {
                        spinKitView.setVisibility(View.GONE);
                    }
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String subject = dataSnapshot1.child("subject").getValue(String.class);
                            subjectList.add(subject);
                        }
                        recyclerExamSubjectAdapter.notifyDataSetChanged();
                    } else {
                        if (dialog != null) {
                            dialog.dismiss();
                            recyclerExamSubjectAdapter.notifyDataSetChanged();
                            new CustomMessage(getActivity(), "No subject available");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    if (spinKitView != null) {
                        spinKitView.setVisibility(View.GONE);
                    }
                    if (dialog != null) {
                        dialog.dismiss();
                        recyclerExamSubjectAdapter.notifyDataSetChanged();
                        new CustomMessage(getActivity(), "No subject available");
                    }

                }
            });
        } else {
            AppController.getFirebaseHelper().getSubject().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectList.clear();
                    if (spinKitView != null) {
                        spinKitView.setVisibility(View.GONE);
                    }
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String subject = dataSnapshot1.child("subject").getValue(String.class);
                            subjectList.add(subject);
                        }
                        recyclerExamSubjectAdapter.notifyDataSetChanged();
                    } else {
                        if (dialog != null) {
                            dialog.dismiss();
                            recyclerExamSubjectAdapter.notifyDataSetChanged();
                            new CustomMessage(getActivity(), "No subject available");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    if (spinKitView != null) {
                        spinKitView.setVisibility(View.GONE);
                    }
                    if (dialog != null) {
                        dialog.dismiss();
                        recyclerExamSubjectAdapter.notifyDataSetChanged();
                        new CustomMessage(getActivity(), "No subject available");
                    }

                }
            });
        }
    }

    @Override
    public void onSubjectSelect(int pos, String subject) {
        SelectItem selectItem=new SelectItem();
        selectItem.setId(pos);
        selectItem.setSubject(subject);
        selectList.add(selectItem);
    }

    @Override
    public void onSubjectDeselect(int pos, String subject) {
        for (int i=0;i<selectList.size();i++){
            SelectItem item=selectList.get(i);
            if (item.getId()==pos){
                selectList.remove(i);
            }
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
}
