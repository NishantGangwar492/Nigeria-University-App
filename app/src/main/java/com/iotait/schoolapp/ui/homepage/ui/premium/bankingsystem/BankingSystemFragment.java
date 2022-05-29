package com.iotait.schoolapp.ui.homepage.ui.premium.bankingsystem;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.common.Common;
import com.iotait.schoolapp.databinding.FragmentBankingSystemBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.FragmentHelper;
import com.iotait.schoolapp.helper.NetworkHelper;
import com.iotait.schoolapp.notification.APIService;
import com.iotait.schoolapp.notification.Client;
import com.iotait.schoolapp.notification.Data;
import com.iotait.schoolapp.notification.MyResponse;
import com.iotait.schoolapp.notification.Sender;
import com.iotait.schoolapp.notification.Token;

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
public class BankingSystemFragment extends Fragment implements View.OnClickListener {

    FragmentBankingSystemBinding binding;
    private String c_date = new SimpleDateFormat("E, dd MMM yyyy hh:mm a", Locale.getDefault()).format(new Date());
    private boolean isTotalAmount, isFullName, isEmail, isMtn, isPin3, isPin2 = false;
    private String packagename = "";
    private Bundle bundle;
    private List<String> adminUiserList = new ArrayList<>();
    private APIService apiService;

    Dialog dialog;


    public BankingSystemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_banking_system, container, false);


        bundle = getArguments();
        packagename = bundle.getString("state");
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        apiService = Client.getClient(getResources().getString(R.string.fcmlink)).create(APIService.class);
        Common.notification_state = true;


        if (packagename.equals("p1")) {
            binding.totalAmount.setText("₦1000");
        }
        if (packagename.equals("p2")) {
            binding.totalAmount.setText("₦2000");
        }



        binding.backbutton.setOnClickListener(this);
        binding.close.setOnClickListener(this);
        binding.submit.setOnClickListener(this);

        getAdminUswerList();

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_bankingSystemFragment_to_premiumProcessFragment, bundle);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backbutton:
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_bankingSystemFragment_to_premiumProcessFragment, bundle);
                break;

            case R.id.close:
                Bundle bund = new Bundle();
                FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_bankingSystemFragment_to_premiumProcessFragment, bund);
                break;

            case R.id.submit:
                ShowConformationBankingPAymentDialog();

                break;

        }
    }

    private void ShowConformationBankingPAymentDialog() {
        if (TextUtils.isEmpty(binding.totalAmount.getText().toString())) {
            binding.totalAmount.setError("Please input total amount");
            isTotalAmount = false;
        } else {
            isTotalAmount = true;
            binding.totalAmount.setError(null);

        }

        if (TextUtils.isEmpty(binding.fullName.getText().toString())) {
            binding.fullName.setError("Please input full name");
            isFullName = false;
        } else {
            isFullName = true;
            binding.fullName.setError(null);

        }

        if (TextUtils.isEmpty(binding.email.getText().toString())) {
            binding.email.setError("Please input recharge card type");
            isEmail = false;
        } else {
            isEmail = true;
            binding.email.setError(null);

        }

        if (isTotalAmount && isFullName && isEmail) {
            if (dialog != null) {
                dialog.dismiss();
            }


            dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.payment_request_dialog);
            dialog.setCanceledOnTouchOutside(true);


            final TextView userName = dialog.findViewById(R.id.userNamereq);
            TextView userEmail = dialog.findViewById(R.id.UserEmailreq);
            TextView amount = dialog.findViewById(R.id.amount);


            userName.setText(binding.fullName.getText().toString());
            userEmail.setText(binding.email.getText().toString());
            amount.setText(binding.totalAmount.getText().toString());


            Button yes = dialog.findViewById(R.id.accept);
            Button no = dialog.findViewById(R.id.decline);

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    submitting();
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
        } else {

        }
    }

    private void submitting() {

        if (TextUtils.isEmpty(binding.totalAmount.getText().toString())) {
            binding.totalAmount.setError("Please input total amount");
            isTotalAmount = false;
        } else {
            isTotalAmount = true;
            binding.totalAmount.setError(null);

        }

        if (TextUtils.isEmpty(binding.fullName.getText().toString())) {
            binding.fullName.setError("Please input full name");
            isFullName = false;
        } else {
            isFullName = true;
            binding.fullName.setError(null);

        }

        if (TextUtils.isEmpty(binding.email.getText().toString())) {
            binding.email.setError("Please input recharge card type");
            isEmail = false;
        } else {
            isEmail = true;
            binding.email.setError(null);

        }

        @SuppressLint("HardwareIds")
        String android_id = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if (isTotalAmount && isFullName && isEmail) {
            String id = AppController.getFirebaseHelper().getPayment().push().getKey();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("packagename", packagename);
            hashMap.put("paymentid", id);
            hashMap.put("paymentdate", c_date);
            hashMap.put("paymentype", "direct_bank");
            hashMap.put("payment_userid", AppController.getFirebaseHelper().getFirebaseAuth().getUid());
            hashMap.put("payment_amount", binding.totalAmount.getText().toString());
            hashMap.put("fullname", binding.fullName.getText().toString());
            hashMap.put("device_id", android_id);

            if (NetworkHelper.hasNetworAccess(getContext())) {
                binding.spinKit.setVisibility(View.VISIBLE);
                AppController.getFirebaseHelper().getPayment().child(id).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        binding.spinKit.setVisibility(View.GONE);
                        new CustomMessage(getActivity(), "Waiting for approval");
                        if (Common.notification_state) {
                            for (int i = 0; i < adminUiserList.size(); i++) {
                                sendNotification(AppController.getFirebaseHelper().getFirebaseAuth().getUid(), adminUiserList.get(i), binding.fullName.getText().toString(), "Request for access");
                            }
                        }
                        Common.notification_state = false;
                        FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_bankingSystemFragment_to_premiumProcessFragment, new Bundle());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.spinKit.setVisibility(View.GONE);
                    }
                });
            } else {
                new CustomMessage(getActivity(), "No internet connection");
            }
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
                                }
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {

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
    public void onResume() {
        super.onResume();
        getUserDetails();
    }

    private void getUserDetails() {
        AppController.getFirebaseHelper().getUsers().child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String email = "";
                            if (dataSnapshot.child("email") != null) {
                                email = dataSnapshot.child("email").getValue(String.class);
                            }

                            binding.email.setText(email);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
