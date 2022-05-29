package com.iotait.schoolapp.ui.homepage.ui.premium.mobileprecharge;

import android.app.Dialog;
import android.app.ProgressDialog;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.common.Common;
import com.iotait.schoolapp.databinding.FragmentMobileRechargeBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.FragmentHelper;
import com.iotait.schoolapp.helper.NetworkHelper;
import com.iotait.schoolapp.notification.APIService;
import com.iotait.schoolapp.notification.Client;
import com.iotait.schoolapp.notification.Data;
import com.iotait.schoolapp.notification.MyResponse;
import com.iotait.schoolapp.notification.Sender;
import com.iotait.schoolapp.notification.Token;
import com.iotait.schoolapp.ui.homepage.ui.question.adapter.RecyclerYearItemAdapter;
import com.iotait.schoolapp.ui.homepage.ui.question.view.AddQuestionView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MobileRechargeFragment extends Fragment implements View.OnClickListener, AddQuestionView {
    private FragmentMobileRechargeBinding mobileRechargeBinding;

    private boolean isTotalAmount, isFullName, isRechargeType, isMtn,  isPin = false;

    private Bundle bundle;
    private String packagename = "";
    private int total_amount = 0;
    private List<String> adminUiserList = new ArrayList<>();
    private APIService apiService;
    private List<String> cardList;

    public MobileRechargeFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mobileRechargeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_mobile_recharge, container, false);
        return mobileRechargeBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        apiService = Client.getClient(getResources().getString(R.string.fcmlink)).create(APIService.class);
        Common.notification_state = true;
        cardList=new ArrayList<>();
        bundle = getArguments();
        packagename = bundle.getString("state");
        total_amount = bundle.getInt("amount");
        mobileRechargeBinding.includeToolbar.toolbarTitle.setText("Recharge Card");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mobileRechargeBinding.includeToolbar.toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mobileRechargeBinding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(mobileRechargeBinding.getRoot(), R.id.action_mobileRechargeFragment_to_premiumProcessFragment, bundle);
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(mobileRechargeBinding.getRoot(), R.id.action_mobileRechargeFragment_to_premiumProcessFragment, bundle);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        mobileRechargeBinding.editTotalAmount.setText("₦"+String.valueOf(total_amount));
        mobileRechargeBinding.txtClose.setOnClickListener(this::onClick);
        mobileRechargeBinding.btnSubmit.setOnClickListener(this::onClick);
        mobileRechargeBinding.editRechargeCardType.setOnClickListener(this::onClick);
        getAdminUswerList();
    }

    private void getAdminUswerList() {
        if (NetworkHelper.hasNetworAccess(getContext())) {
            AppController.getFirebaseHelper().getTokens().child("Admins").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    adminUiserList.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            adminUiserList.add(snapshot.getKey());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            new CustomMessage(getActivity(),"No internet connection");
        }
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
        int id = view.getId();
        switch (id) {
            case R.id.txt_close:
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(mobileRechargeBinding.getRoot(), R.id.action_mobileRechargeFragment_to_premiumProcessFragment, bundle);
                break;
            case R.id.btn_submit:

                ShowRechargeconformationRequestDialog();
                break;
            case R.id.edit_recharge_card_type:
                showCardDialog();
                break;
        }
    }

    private void ShowRechargeconformationRequestDialog() {
        if (TextUtils.isEmpty(mobileRechargeBinding.editTotalAmount.getText().toString())) {
            mobileRechargeBinding.editTotalAmount.setError("Please input total amount");
            isTotalAmount = false;
        } else {
            isTotalAmount = true;
            mobileRechargeBinding.editTotalAmount.setError(null);

        }

        if (TextUtils.isEmpty(mobileRechargeBinding.editFullName.getText().toString())) {
            mobileRechargeBinding.editFullName.setError("Please input full name");
            isFullName = false;
        } else {
            isFullName = true;
            mobileRechargeBinding.editFullName.setError(null);

        }

        if (TextUtils.isEmpty(mobileRechargeBinding.editRechargeCardType.getText().toString())) {
            mobileRechargeBinding.editRechargeCardType.setError("Please input recharge card type");
            isRechargeType = false;
        } else {
            isRechargeType = true;
            mobileRechargeBinding.editRechargeCardType.setError(null);

        }

        if (TextUtils.isEmpty(mobileRechargeBinding.editMtn.getText().toString())) {
            mobileRechargeBinding.editMtn.setError("Please input mtn number");
            isMtn = false;
        } else {
            isMtn = true;
            mobileRechargeBinding.editMtn.setError(null);
        }

        if (total_amount == 2000) {
            if (mobileRechargeBinding.editRechargeCardType.getText().toString().equals("2000")) {
                isPin = getStatefor1();
            } else if (mobileRechargeBinding.editRechargeCardType.getText().toString().equals("500")) {
                isPin = getStatefor4();
            } else if (mobileRechargeBinding.editRechargeCardType.getText().toString().equals("1000")) {
                isPin = getStatefor2();
            } else if (mobileRechargeBinding.editRechargeCardType.getText().toString().equals("200")) {
                isPin = getStatefor10();
            }
        }
        if (total_amount == 1000) {
            if (mobileRechargeBinding.editRechargeCardType.getText().toString().equals("1000")) {
                isPin = getStatefor1();
            } else if (mobileRechargeBinding.editRechargeCardType.getText().toString().equals("500")) {
                isPin = getStatefor2();
            } else if (mobileRechargeBinding.editRechargeCardType.getText().toString().equals("200")) {
                isPin = getStatefor5();
            }
        }


        if (isTotalAmount && isFullName && isRechargeType && isMtn && isPin && isPin) {
            dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.recharge_payment_request_dialog);
            dialog.setCanceledOnTouchOutside(true);


            TextView userName = dialog.findViewById(R.id.userNamereq);
            TextView rechargecardType = dialog.findViewById(R.id.rechargecardtype);
            TextView amount = dialog.findViewById(R.id.amount);
            TextView CardPins = dialog.findViewById(R.id.cardPins);


            userName.setText(mobileRechargeBinding.editFullName.getText().toString());
            rechargecardType.setText(mobileRechargeBinding.editRechargeCardType.getText().toString());
            amount.setText(String.valueOf(total_amount));


            if (total_amount == 2000 && mobileRechargeBinding.editRechargeCardType.getText().toString().equals("2000")) {
                //  hashMap.put("/pins/" + "pin1" + "/",mobileRechargeBinding.editPin1.getText().toString());

                CardPins.setText("pin1: " + mobileRechargeBinding.editPin1.getText().toString());
            }
            if (total_amount == 1000 && mobileRechargeBinding.editRechargeCardType.getText().toString().equals("1000")) {
                //  hashMap.put("/pins/" + "pin1" + "/",mobileRechargeBinding.editPin1.getText().toString());
                CardPins.setText("pin1: " + mobileRechargeBinding.editPin1.getText().toString());
            } else if (total_amount == 2000 && mobileRechargeBinding.editRechargeCardType.getText().toString().equals("1000")) {
//                        hashMap.put("/pins/" + "pin1" + "/",mobileRechargeBinding.editPin1.getText().toString());
//                        hashMap.put("/pins/" + "pin2" + "/",mobileRechargeBinding.editPin2.getText().toString());

                CardPins.setText("pin1: " + mobileRechargeBinding.editPin1.getText().toString() + "pin2: " + mobileRechargeBinding.editPin2.getText().toString());
            } else if (total_amount == 1000 && mobileRechargeBinding.editRechargeCardType.getText().toString().equals("500")) {
//                        hashMap.put("/pins/" + "pin1" + "/",mobileRechargeBinding.editPin1.getText().toString());
//                        hashMap.put("/pins/" + "pin2" + "/",mobileRechargeBinding.editPin2.getText().toString());

                CardPins.setText("pin1: " + mobileRechargeBinding.editPin1.getText().toString() + ", pin2: " + mobileRechargeBinding.editPin2.getText().toString());

            } else if (total_amount == 2000 && mobileRechargeBinding.editRechargeCardType.getText().toString().equals("500")) {
//                        hashMap.put("/pins/" + "pin1" + "/",mobileRechargeBinding.editPin1.getText().toString());
//                        hashMap.put("/pins/" + "pin2" + "/",mobileRechargeBinding.editPin2.getText().toString());
//                        hashMap.put("/pins/" + "pin3" + "/",mobileRechargeBinding.editPin3.getText().toString());
//                        hashMap.put("/pins/" + "pin4" + "/",mobileRechargeBinding.editPin4.getText().toString());

                CardPins.setText("pin1: " + mobileRechargeBinding.editPin1.getText().toString() + ", pin2: " + mobileRechargeBinding.editPin2.getText().toString()
                        + ", pin3: " + mobileRechargeBinding.editPin3.getText().toString() + ", pin4: " + mobileRechargeBinding.editPin4.getText().toString());

            } else if (total_amount == 1000 && mobileRechargeBinding.editRechargeCardType.getText().toString().equals("200")) {
//                        hashMap.put("/pins/" + "pin1" + "/",mobileRechargeBinding.editPin1.getText().toString());
//                        hashMap.put("/pins/" + "pin2" + "/",mobileRechargeBinding.editPin2.getText().toString());
//                        hashMap.put("/pins/" + "pin3" + "/",mobileRechargeBinding.editPin3.getText().toString());
//                        hashMap.put("/pins/" + "pin4" + "/",mobileRechargeBinding.editPin4.getText().toString());
//                        hashMap.put("/pins/" + "pin5" + "/",mobileRechargeBinding.editPin5.getText().toString());
                CardPins.setText("pin1: " + mobileRechargeBinding.editPin1.getText().toString() + ", pin2: " + mobileRechargeBinding.editPin2.getText().toString()
                        + ", pin3: " + mobileRechargeBinding.editPin3.getText().toString() + ", pin4: " + mobileRechargeBinding.editPin4.getText().toString()
                        + ", pin5: " + mobileRechargeBinding.editPin5.getText().toString());
            } else if (total_amount == 2000 && mobileRechargeBinding.editRechargeCardType.getText().toString().equals("200")) {
//                        hashMap.put("/pins/" + "pin1" + "/",mobileRechargeBinding.editPin1.getText().toString());
//                        hashMap.put("/pins/" +
//                                "pin2" + "/",mobileRechargeBinding.editPin2.getText().toString());
//                        hashMap.put("/pins/" + "pin3" + "/",mobileRechargeBinding.editPin3.getText().toString());
//                        hashMap.put("/pins/" + "pin4" + "/",mobileRechargeBinding.editPin4.getText().toString());
//                        hashMap.put("/pins/" + "pin5" + "/",mobileRechargeBinding.editPin5.getText().toString());
//                        hashMap.put("/pins/" + "pin6" + "/",mobileRechargeBinding.editPin6.getText().toString());
//                        hashMap.put("/pins/" + "pin7" + "/",mobileRechargeBinding.editPin7.getText().toString());
//                        hashMap.put("/pins/" + "pin8" + "/",mobileRechargeBinding.editPin8.getText().toString());
//                        hashMap.put("/pins/" + "pin9" + "/",mobileRechargeBinding.editPin9.getText().toString());
//                        hashMap.put("/pins/" + "pin10" + "/",mobileRechargeBinding.editPin10.getText().toString());

                CardPins.setText("pin1: " + mobileRechargeBinding.editPin1.getText().toString() + ", pin2: " + mobileRechargeBinding.editPin2.getText().toString()
                        + ", pin3: " + mobileRechargeBinding.editPin3.getText().toString() + ", pin4: " + mobileRechargeBinding.editPin4.getText().toString()
                        + ", pin5: " + mobileRechargeBinding.editPin5.getText().toString() + ", pin6: " + mobileRechargeBinding.editPin6.getText().toString()
                        + ", pin7: " + mobileRechargeBinding.editPin7.getText().toString()
                        + ", pin8: " + mobileRechargeBinding.editPin8.getText().toString() + ", pin9: " + mobileRechargeBinding.editPin9.getText().toString()
                        + ", pin10: " + mobileRechargeBinding.editPin10.getText().toString());
            }


            Button yes = dialog.findViewById(R.id.accept);
            Button no = dialog.findViewById(R.id.decline);

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    startChecking();
                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });

            dialog.show();

        }
    }

    private Dialog dialog;
    private RecyclerYearItemAdapter recyclerYearItemAdapter;

    private void showCardDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_year_view);
        dialog.setCanceledOnTouchOutside(true);

        RecyclerView recyclerView = dialog.findViewById(R.id.yearlist);
        getCardList();
        recyclerYearItemAdapter = new RecyclerYearItemAdapter(getContext(), cardList, this);
        recyclerYearItemAdapter.setHasStableIds(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerYearItemAdapter);


        final TextView txt_title = dialog.findViewById(R.id.txt_title);
        txt_title.setText("Select card type");
        dialog.show();
    }

    private void getCardList() {
        cardList.clear();
        if (total_amount==2000){
            cardList.add(String.valueOf(200));
            cardList.add(String.valueOf(500));
            cardList.add(String.valueOf(1000));
            cardList.add(String.valueOf(2000));
        }
        else if (total_amount==1000){
            cardList.add(String.valueOf(200));
            cardList.add(String.valueOf(500));
            cardList.add(String.valueOf(1000));
        }
    }

    private String c_date = new SimpleDateFormat("E, dd MMM yyyy hh:mm a", Locale.getDefault()).format(new Date());

    private void startChecking() {
        if (TextUtils.isEmpty(mobileRechargeBinding.editTotalAmount.getText().toString())) {
            mobileRechargeBinding.editTotalAmount.setError("Please input total amount");
            isTotalAmount = false;
        } else {
            isTotalAmount = true;
            mobileRechargeBinding.editTotalAmount.setError(null);

        }

        if (TextUtils.isEmpty(mobileRechargeBinding.editFullName.getText().toString())) {
            mobileRechargeBinding.editFullName.setError("Please input full name");
            isFullName = false;
        } else {
            isFullName = true;
            mobileRechargeBinding.editFullName.setError(null);

        }

        if (TextUtils.isEmpty(mobileRechargeBinding.editRechargeCardType.getText().toString())) {
            mobileRechargeBinding.editRechargeCardType.setError("Please input recharge card type");
            isRechargeType = false;
        } else {
            isRechargeType = true;
            mobileRechargeBinding.editRechargeCardType.setError(null);

        }

        if (TextUtils.isEmpty(mobileRechargeBinding.editMtn.getText().toString())) {
            mobileRechargeBinding.editMtn.setError("Please input mtn number");
            isMtn = false;
        } else {
            isMtn = true;
            mobileRechargeBinding.editMtn.setError(null);
        }

        if (total_amount==2000){
            if (mobileRechargeBinding.editRechargeCardType.getText().toString().equals("2000")){
                isPin=getStatefor1();
            }
            else if (mobileRechargeBinding.editRechargeCardType.getText().toString().equals("500")){
                isPin=getStatefor4();
            }
            else if (mobileRechargeBinding.editRechargeCardType.getText().toString().equals("1000")){
                isPin=getStatefor2();
            }
            else if (mobileRechargeBinding.editRechargeCardType.getText().toString().equals("200")){
                isPin=getStatefor10();
            }
        }
        if (total_amount==1000){
            if (mobileRechargeBinding.editRechargeCardType.getText().toString().equals("1000")){
                isPin=getStatefor1();
            }
            else if (mobileRechargeBinding.editRechargeCardType.getText().toString().equals("500")){
                isPin=getStatefor2();
            }
            else if (mobileRechargeBinding.editRechargeCardType.getText().toString().equals("200")){
                isPin=getStatefor5();
            }
        }

        if (isTotalAmount && isFullName && isRechargeType && isMtn && isPin && isPin) {
            String id = AppController.getFirebaseHelper().getPayment().push().getKey();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("packagename", packagename);
            hashMap.put("paymentid", id);
            hashMap.put("paymentdate", c_date);
            hashMap.put("paymentype", "mobile_recharge");
            hashMap.put("payment_userid", AppController.getFirebaseHelper().getFirebaseAuth().getUid());
            hashMap.put("payment_amount", String.valueOf(total_amount));
            hashMap.put("fullname", mobileRechargeBinding.editFullName.getText().toString());
            if (total_amount==2000 && mobileRechargeBinding.editRechargeCardType.getText().toString().equals("2000")){
                hashMap.put("/pins/" + "pin1" + "/",mobileRechargeBinding.editPin1.getText().toString());
            }
            if (total_amount==1000 && mobileRechargeBinding.editRechargeCardType.getText().toString().equals("1000")){
                hashMap.put("/pins/" + "pin1" + "/",mobileRechargeBinding.editPin1.getText().toString());
            }
            else if (total_amount==2000 && mobileRechargeBinding.editRechargeCardType.getText().toString().equals("1000")){
                hashMap.put("/pins/" + "pin1" + "/",mobileRechargeBinding.editPin1.getText().toString());
                hashMap.put("/pins/" + "pin2" + "/",mobileRechargeBinding.editPin2.getText().toString());
            }
            else if (total_amount==1000 && mobileRechargeBinding.editRechargeCardType.getText().toString().equals("500")){
                hashMap.put("/pins/" + "pin1" + "/",mobileRechargeBinding.editPin1.getText().toString());
                hashMap.put("/pins/" + "pin2" + "/",mobileRechargeBinding.editPin2.getText().toString());
            }
            else if (total_amount==2000 && mobileRechargeBinding.editRechargeCardType.getText().toString().equals("500")){
                hashMap.put("/pins/" + "pin1" + "/",mobileRechargeBinding.editPin1.getText().toString());
                hashMap.put("/pins/" + "pin2" + "/",mobileRechargeBinding.editPin2.getText().toString());
                hashMap.put("/pins/" + "pin3" + "/",mobileRechargeBinding.editPin3.getText().toString());
                hashMap.put("/pins/" + "pin4" + "/",mobileRechargeBinding.editPin4.getText().toString());
            }
            else if (total_amount==1000 && mobileRechargeBinding.editRechargeCardType.getText().toString().equals("200")){
                hashMap.put("/pins/" + "pin1" + "/",mobileRechargeBinding.editPin1.getText().toString());
                hashMap.put("/pins/" + "pin2" + "/",mobileRechargeBinding.editPin2.getText().toString());
                hashMap.put("/pins/" + "pin3" + "/",mobileRechargeBinding.editPin3.getText().toString());
                hashMap.put("/pins/" + "pin4" + "/",mobileRechargeBinding.editPin4.getText().toString());
                hashMap.put("/pins/" + "pin5" + "/",mobileRechargeBinding.editPin5.getText().toString());
            }
            else if (total_amount==2000 && mobileRechargeBinding.editRechargeCardType.getText().toString().equals("200")){
                hashMap.put("/pins/" + "pin1" + "/",mobileRechargeBinding.editPin1.getText().toString());
                hashMap.put("/pins/" +
                        "pin2" + "/",mobileRechargeBinding.editPin2.getText().toString());
                hashMap.put("/pins/" + "pin3" + "/",mobileRechargeBinding.editPin3.getText().toString());
                hashMap.put("/pins/" + "pin4" + "/",mobileRechargeBinding.editPin4.getText().toString());
                hashMap.put("/pins/" + "pin5" + "/",mobileRechargeBinding.editPin5.getText().toString());
                hashMap.put("/pins/" + "pin6" + "/",mobileRechargeBinding.editPin6.getText().toString());
                hashMap.put("/pins/" + "pin7" + "/",mobileRechargeBinding.editPin7.getText().toString());
                hashMap.put("/pins/" + "pin8" + "/",mobileRechargeBinding.editPin8.getText().toString());
                hashMap.put("/pins/" + "pin9" + "/",mobileRechargeBinding.editPin9.getText().toString());
                hashMap.put("/pins/" + "pin10" + "/",mobileRechargeBinding.editPin10.getText().toString());
            }


            if (NetworkHelper.hasNetworAccess(getContext())) {
                final ProgressDialog mDialog = new ProgressDialog(getContext());
                mDialog.setMessage("Submitting...");
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.setIndeterminate(false);
                mDialog.show();
                AppController.getFirebaseHelper().getPayment().child(id).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mobileRechargeBinding.spinKit.setVisibility(View.GONE);
                        new CustomMessage(getActivity(), "Waiting for approval");
                        if (Common.notification_state) {
                            for (int i = 0; i < adminUiserList.size(); i++) {
                                sendNotification(AppController.getFirebaseHelper().getFirebaseAuth().getUid(), adminUiserList.get(i), mobileRechargeBinding.editFullName.getText().toString(), "Request for access");
                            }
                        }
                        Common.notification_state = false;
                        FragmentHelper.changeFragmet(mobileRechargeBinding.getRoot(), R.id.action_mobileRechargeFragment_to_premiumProcessFragment, new Bundle());
                        mDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mDialog.dismiss();
                    }
                });
            } else {
                new CustomMessage(getActivity(), "No internet connection");
            }

        }

    }

    private boolean getStatefor10() {
        if (!TextUtils.isEmpty( mobileRechargeBinding.editPin1.getText().toString()) &&
                !TextUtils.isEmpty( mobileRechargeBinding.editPin2.getText().toString()) &&
                !TextUtils.isEmpty( mobileRechargeBinding.editPin3.getText().toString()) &&
                !TextUtils.isEmpty( mobileRechargeBinding.editPin4.getText().toString()) &&
                !TextUtils.isEmpty( mobileRechargeBinding.editPin5.getText().toString()) &&
                !TextUtils.isEmpty( mobileRechargeBinding.editPin6.getText().toString()) &&
                !TextUtils.isEmpty( mobileRechargeBinding.editPin7.getText().toString()) &&
                !TextUtils.isEmpty( mobileRechargeBinding.editPin8.getText().toString()) &&
                !TextUtils.isEmpty( mobileRechargeBinding.editPin9.getText().toString()) &&
                !TextUtils.isEmpty( mobileRechargeBinding.editPin10.getText().toString())){
            mobileRechargeBinding.editPin1.setError(null);
            mobileRechargeBinding.editPin2.setError(null);
            mobileRechargeBinding.editPin3.setError(null);
            mobileRechargeBinding.editPin4.setError(null);
            mobileRechargeBinding.editPin5.setError(null);
            mobileRechargeBinding.editPin6.setError(null);
            mobileRechargeBinding.editPin7.setError(null);
            mobileRechargeBinding.editPin8.setError(null);
            mobileRechargeBinding.editPin9.setError(null);
            mobileRechargeBinding.editPin10.setError(null);
            return true;
        }
        else {
            if (mobileRechargeBinding.editPin1.getText().toString().equals("")){
                mobileRechargeBinding.editPin1.setError("Please add pin 1");
            }
            if (mobileRechargeBinding.editPin2.getText().toString().equals("")){
                mobileRechargeBinding.editPin2.setError("Please add pin 2");
            }
            if (mobileRechargeBinding.editPin3.getText().toString().equals("")){
                mobileRechargeBinding.editPin3.setError("Please add pin 3");
            }
            if (mobileRechargeBinding.editPin4.getText().toString().equals("")){
                mobileRechargeBinding.editPin4.setError("Please add pin 4");
            }
            if (mobileRechargeBinding.editPin5.getText().toString().equals("")){
                mobileRechargeBinding.editPin5.setError("Please add pin 5");
            }

            if (mobileRechargeBinding.editPin6.getText().toString().equals("")){
                mobileRechargeBinding.editPin6.setError("Please add pin 6");
            }
            if (mobileRechargeBinding.editPin7.getText().toString().equals("")){
                mobileRechargeBinding.editPin7.setError("Please add pin 7");
            }
            if (mobileRechargeBinding.editPin8.getText().toString().equals("")){
                mobileRechargeBinding.editPin8.setError("Please add pin 8");
            }
            if (mobileRechargeBinding.editPin9.getText().toString().equals("")){
                mobileRechargeBinding.editPin9.setError("Please add pin 9");
            }
            if (mobileRechargeBinding.editPin10.getText().toString().equals("")){
                mobileRechargeBinding.editPin10.setError("Please add pin 10");
            }
            return false;
        }
    }

    private boolean getStatefor5() {
        if (!TextUtils.isEmpty( mobileRechargeBinding.editPin1.getText().toString()) &&
                !TextUtils.isEmpty( mobileRechargeBinding.editPin2.getText().toString()) &&
                !TextUtils.isEmpty( mobileRechargeBinding.editPin3.getText().toString()) &&
                !TextUtils.isEmpty( mobileRechargeBinding.editPin4.getText().toString()) &&
                !TextUtils.isEmpty( mobileRechargeBinding.editPin5.getText().toString())){
            mobileRechargeBinding.editPin1.setError(null);
            mobileRechargeBinding.editPin2.setError(null);
            mobileRechargeBinding.editPin3.setError(null);
            mobileRechargeBinding.editPin4.setError(null);
            mobileRechargeBinding.editPin5.setError(null);
            return true;
        }
        else {
            if (mobileRechargeBinding.editPin1.getText().toString().equals("")){
                mobileRechargeBinding.editPin1.setError("Please add pin 1");
            }
            if (mobileRechargeBinding.editPin2.getText().toString().equals("")){
                mobileRechargeBinding.editPin2.setError("Please add pin 2");
            }
            if (mobileRechargeBinding.editPin3.getText().toString().equals("")){
                mobileRechargeBinding.editPin3.setError("Please add pin 3");
            }
            if (mobileRechargeBinding.editPin4.getText().toString().equals("")){
                mobileRechargeBinding.editPin4.setError("Please add pin 4");
            }
            if (mobileRechargeBinding.editPin5.getText().toString().equals("")){
                mobileRechargeBinding.editPin5.setError("Please add pin 5");
            }
            return false;
        }
    }

    private boolean getStatefor4() {
        if (!TextUtils.isEmpty( mobileRechargeBinding.editPin1.getText().toString()) &&
                !TextUtils.isEmpty( mobileRechargeBinding.editPin2.getText().toString()) &&
                !TextUtils.isEmpty( mobileRechargeBinding.editPin3.getText().toString()) &&
                !TextUtils.isEmpty( mobileRechargeBinding.editPin4.getText().toString())){
            mobileRechargeBinding.editPin1.setError(null);
            mobileRechargeBinding.editPin2.setError(null);
            mobileRechargeBinding.editPin3.setError(null);
            mobileRechargeBinding.editPin4.setError(null);
            return true;
        }
        else {
            if (mobileRechargeBinding.editPin1.getText().toString().equals("")){
                mobileRechargeBinding.editPin1.setError("Please add pin 1");
            }
            if (mobileRechargeBinding.editPin2.getText().toString().equals("")){
                mobileRechargeBinding.editPin2.setError("Please add pin 2");
            }
            if (mobileRechargeBinding.editPin3.getText().toString().equals("")){
                mobileRechargeBinding.editPin3.setError("Please add pin 3");
            }
            if (mobileRechargeBinding.editPin4.getText().toString().equals("")){
                mobileRechargeBinding.editPin4.setError("Please add pin 4");
            }
            return false;
        }
    }

    private boolean getStatefor2() {
        if (!TextUtils.isEmpty( mobileRechargeBinding.editPin1.getText().toString()) &&
                !TextUtils.isEmpty( mobileRechargeBinding.editPin2.getText().toString())){
            mobileRechargeBinding.editPin1.setError(null);
            mobileRechargeBinding.editPin2.setError(null);
            return true;
        }
        else {
            if (mobileRechargeBinding.editPin1.getText().toString().equals("")){
                mobileRechargeBinding.editPin1.setError("Please add pin 1");
            }
            if (mobileRechargeBinding.editPin2.getText().toString().equals("")){
                mobileRechargeBinding.editPin2.setError("Please add pin 2");
            }
            return false;
        }
    }

    private boolean getStatefor1(){
        if (!TextUtils.isEmpty( mobileRechargeBinding.editPin1.getText().toString())){
            mobileRechargeBinding.editPin1.setError(null);
            return true;
        }
        else {
            mobileRechargeBinding.editPin1.setError("Please add pin");
            return false;
        }
    }

    // send the notification to the user
    private void sendNotification(String sender, String receiver, String name, String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens").child("Admins");
        Query query = databaseReference.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Token token = snapshot.getValue(Token.class);
                        Data data = new Data(sender,receiver ,R.mipmap.ic_launcher_round, message, name , "user_payment_request");
                        Sender sender1 = new Sender(data, token.getToken());
                        apiService.sendNotification(sender1).enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                if (response.code() == 200) {
                                    if (response.body().success != 1) {
                                        Log.e("TAG", "onResponse: " + response.message());
                                    }
                                    else {
                                        Log.e("TAG", "onResponse: "+response.toString());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {
                                Log.e("TAG", "onResponse: "+t.getMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onYearSelected(String card) {
        mobileRechargeBinding.editRechargeCardType.setText(card);
        if (total_amount==2000){
            if (card.equals("200")){
                visible10();
            }
            else if (card.equals("500")){
                visible4();
            }
            else if (card.equals("1000")){
                visible2();
            }
            else if (card.equals("2000")){
                visible1();
            }
        }
        if (total_amount==1000){
            if (card.equals("500")){
                visible2();
            }
            else if (card.equals("1000")){
                visible1();
            }
            else if (card.equals("200")){
                visible5();
            }
        }
        if (dialog!=null){
            dialog.dismiss();
        }
    }

    private void visible5() {
        //view
        mobileRechargeBinding.editPin1.setVisibility(View.VISIBLE);
        mobileRechargeBinding.view1.setVisibility(View.VISIBLE);

        mobileRechargeBinding.editPin2.setVisibility(View.VISIBLE);
        mobileRechargeBinding.view2.setVisibility(View.VISIBLE);

        mobileRechargeBinding.editPin3.setVisibility(View.VISIBLE);
        mobileRechargeBinding.view3.setVisibility(View.VISIBLE);

        mobileRechargeBinding.editPin4.setVisibility(View.VISIBLE);
        mobileRechargeBinding.view4.setVisibility(View.VISIBLE);


        mobileRechargeBinding.editPin5.setVisibility(View.VISIBLE);
        mobileRechargeBinding.view5.setVisibility(View.VISIBLE);


        //gone


        mobileRechargeBinding.editPin6.setVisibility(View.GONE);
        mobileRechargeBinding.editPin6.setText("");
        mobileRechargeBinding.view6.setVisibility(View.GONE);

        mobileRechargeBinding.editPin7.setVisibility(View.GONE);
        mobileRechargeBinding.editPin7.setText("");
        mobileRechargeBinding.view7.setVisibility(View.GONE);

        mobileRechargeBinding.editPin8.setVisibility(View.GONE);
        mobileRechargeBinding.editPin8.setText("");
        mobileRechargeBinding.view8.setVisibility(View.GONE);

        mobileRechargeBinding.editPin9.setVisibility(View.GONE);
        mobileRechargeBinding.editPin9.setText("");
        mobileRechargeBinding.view9.setVisibility(View.GONE);

        mobileRechargeBinding.editPin10.setVisibility(View.GONE);
        mobileRechargeBinding.editPin10.setText("");
        mobileRechargeBinding.view10.setVisibility(View.GONE);
    }

    private void visible1() {
        //view
        mobileRechargeBinding.editPin1.setVisibility(View.VISIBLE);
        mobileRechargeBinding.view1.setVisibility(View.VISIBLE);

        //gone
        mobileRechargeBinding.editPin2.setVisibility(View.GONE);
        mobileRechargeBinding.editPin2.setText("");
        mobileRechargeBinding.view2.setVisibility(View.GONE);

        mobileRechargeBinding.editPin3.setVisibility(View.GONE);
        mobileRechargeBinding.editPin3.setText("");
        mobileRechargeBinding.view3.setVisibility(View.GONE);

        mobileRechargeBinding.editPin4.setVisibility(View.GONE);
        mobileRechargeBinding.editPin4.setText("");
        mobileRechargeBinding.view4.setVisibility(View.GONE);


        mobileRechargeBinding.editPin5.setVisibility(View.GONE);
        mobileRechargeBinding.editPin5.setText("");
        mobileRechargeBinding.view5.setVisibility(View.GONE);


        mobileRechargeBinding.editPin6.setVisibility(View.GONE);
        mobileRechargeBinding.editPin6.setText("");
        mobileRechargeBinding.view6.setVisibility(View.GONE);

        mobileRechargeBinding.editPin6.setVisibility(View.GONE);
        mobileRechargeBinding.editPin6.setText("");
        mobileRechargeBinding.view6.setVisibility(View.GONE);

        mobileRechargeBinding.editPin7.setVisibility(View.GONE);
        mobileRechargeBinding.editPin7.setText("");
        mobileRechargeBinding.view7.setVisibility(View.GONE);

        mobileRechargeBinding.editPin8.setVisibility(View.GONE);
        mobileRechargeBinding.editPin8.setText("");
        mobileRechargeBinding.view8.setVisibility(View.GONE);

        mobileRechargeBinding.editPin9.setVisibility(View.GONE);
        mobileRechargeBinding.editPin9.setText("");
        mobileRechargeBinding.view9.setVisibility(View.GONE);

        mobileRechargeBinding.editPin10.setVisibility(View.GONE);
        mobileRechargeBinding.editPin10.setText("");
        mobileRechargeBinding.view10.setVisibility(View.GONE);
    }

    private void visible2() {
        //view
        mobileRechargeBinding.editPin1.setVisibility(View.VISIBLE);
        mobileRechargeBinding.view1.setVisibility(View.VISIBLE);

        mobileRechargeBinding.editPin2.setVisibility(View.VISIBLE);
        mobileRechargeBinding.view2.setVisibility(View.VISIBLE);

        //gone

        mobileRechargeBinding.editPin3.setVisibility(View.GONE);
        mobileRechargeBinding.editPin3.setText("");
        mobileRechargeBinding.view3.setVisibility(View.GONE);

        mobileRechargeBinding.editPin4.setVisibility(View.GONE);
        mobileRechargeBinding.editPin4.setText("");
        mobileRechargeBinding.view4.setVisibility(View.GONE);


        mobileRechargeBinding.editPin5.setVisibility(View.GONE);
        mobileRechargeBinding.editPin5.setText("");
        mobileRechargeBinding.view5.setVisibility(View.GONE);


        mobileRechargeBinding.editPin6.setVisibility(View.GONE);
        mobileRechargeBinding.editPin6.setText("");
        mobileRechargeBinding.view6.setVisibility(View.GONE);

        mobileRechargeBinding.editPin7.setVisibility(View.GONE);
        mobileRechargeBinding.editPin7.setText("");
        mobileRechargeBinding.view7.setVisibility(View.GONE);

        mobileRechargeBinding.editPin8.setVisibility(View.GONE);
        mobileRechargeBinding.editPin8.setText("");
        mobileRechargeBinding.view8.setVisibility(View.GONE);

        mobileRechargeBinding.editPin9.setVisibility(View.GONE);
        mobileRechargeBinding.editPin9.setText("");
        mobileRechargeBinding.view9.setVisibility(View.GONE);

        mobileRechargeBinding.editPin10.setVisibility(View.GONE);
        mobileRechargeBinding.editPin10.setText("");
        mobileRechargeBinding.view10.setVisibility(View.GONE);
    }

    private void visible4() {

        //view
        mobileRechargeBinding.editPin1.setVisibility(View.VISIBLE);
        mobileRechargeBinding.view1.setVisibility(View.VISIBLE);

        mobileRechargeBinding.editPin2.setVisibility(View.VISIBLE);
        mobileRechargeBinding.view2.setVisibility(View.VISIBLE);

        mobileRechargeBinding.editPin3.setVisibility(View.VISIBLE);
        mobileRechargeBinding.view3.setVisibility(View.VISIBLE);

        mobileRechargeBinding.editPin4.setVisibility(View.VISIBLE);
        mobileRechargeBinding.view4.setVisibility(View.VISIBLE);


        //gone
        mobileRechargeBinding.editPin5.setVisibility(View.GONE);
        mobileRechargeBinding.editPin5.setText("");
        mobileRechargeBinding.view5.setVisibility(View.GONE);


        mobileRechargeBinding.editPin6.setVisibility(View.GONE);
        mobileRechargeBinding.editPin6.setText("");
        mobileRechargeBinding.view6.setVisibility(View.GONE);

        mobileRechargeBinding.editPin7.setVisibility(View.GONE);
        mobileRechargeBinding.editPin7.setText("");
        mobileRechargeBinding.view7.setVisibility(View.GONE);

        mobileRechargeBinding.editPin8.setVisibility(View.GONE);
        mobileRechargeBinding.editPin8.setText("");
        mobileRechargeBinding.view8.setVisibility(View.GONE);

        mobileRechargeBinding.editPin9.setVisibility(View.GONE);
        mobileRechargeBinding.editPin9.setText("");
        mobileRechargeBinding.view9.setVisibility(View.GONE);

        mobileRechargeBinding.editPin10.setVisibility(View.GONE);
        mobileRechargeBinding.editPin10.setText("");
        mobileRechargeBinding.view10.setVisibility(View.GONE);
    }

    private void visible10() {
        mobileRechargeBinding.editPin1.setVisibility(View.VISIBLE);
        mobileRechargeBinding.view1.setVisibility(View.VISIBLE);

        mobileRechargeBinding.editPin2.setVisibility(View.VISIBLE);
        mobileRechargeBinding.view2.setVisibility(View.VISIBLE);

        mobileRechargeBinding.editPin3.setVisibility(View.VISIBLE);
        mobileRechargeBinding.view3.setVisibility(View.VISIBLE);

        mobileRechargeBinding.editPin4.setVisibility(View.VISIBLE);
        mobileRechargeBinding.view4.setVisibility(View.VISIBLE);

        mobileRechargeBinding.editPin5.setVisibility(View.VISIBLE);
        mobileRechargeBinding.view5.setVisibility(View.VISIBLE);


        mobileRechargeBinding.editPin6.setVisibility(View.VISIBLE);
        mobileRechargeBinding.view6.setVisibility(View.VISIBLE);

        mobileRechargeBinding.editPin7.setVisibility(View.VISIBLE);
        mobileRechargeBinding.view7.setVisibility(View.VISIBLE);

        mobileRechargeBinding.editPin8.setVisibility(View.VISIBLE);
        mobileRechargeBinding.view8.setVisibility(View.VISIBLE);

        mobileRechargeBinding.editPin9.setVisibility(View.VISIBLE);
        mobileRechargeBinding.view9.setVisibility(View.VISIBLE);

        mobileRechargeBinding.editPin10.setVisibility(View.VISIBLE);
        mobileRechargeBinding.view10.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSubjectSelected(String subject) {

    }

    @Override
    public void onQuestionReport(String questionid, String s) {

    }
}
