package com.iotait.schoolapp.ui.homepage.ui.premium.onlinepayment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

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
import com.iotait.schoolapp.databinding.FragmentOnlinePaymentBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.FragmentHelper;
import com.iotait.schoolapp.helper.NetworkHelper;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.notification.APIService;
import com.iotait.schoolapp.notification.Client;
import com.iotait.schoolapp.notification.Data;
import com.iotait.schoolapp.notification.MyResponse;
import com.iotait.schoolapp.notification.Sender;
import com.iotait.schoolapp.notification.Token;
import com.iotait.schoolapp.ui.homepage.HomeNavigationActivity;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.exceptions.ExpiredAccessCodeException;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnlinePaymentFragment extends Fragment {

    String backend_url = "https://infinite-peak-60063.herokuapp.com";
//    String paystack_public_key = "pk_live_335ec5fe3f067def605f0cdc72d3ff283c86ebf6";
    String paystack_public_key = "pk_live_335ec5fe3f067def605f0cdc72d3ff283c86ebf6";
    private Charge charge;
    private Transaction transaction;
    private ProgressDialog dialog;

    private Bundle bundle;
    private String packagename = "";
    private int total_amount = 0;
    private List<String> adminUiserList = new ArrayList<>();
    private APIService apiService;

    private FragmentOnlinePaymentBinding onlinePaymentBinding;

    public OnlinePaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        onlinePaymentBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_online_payment,container,false);
        return onlinePaymentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        PaystackSdk.setPublicKey(paystack_public_key);

        apiService = Client.getClient(getResources().getString(R.string.fcmlink)).create(APIService.class);
        Common.notification_state = true;
        bundle = getArguments();
        packagename = bundle.getString("state");
        total_amount = bundle.getInt("amount");
        onlinePaymentBinding.txtPaidAmount.setText("₦"+String.valueOf(total_amount));



        onlinePaymentBinding.includeToolbar.toolbarTitle.setText("ONLINE PAYMENT");
        ((AppCompatActivity) getActivity()).setSupportActionBar(onlinePaymentBinding.includeToolbar.toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        onlinePaymentBinding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(onlinePaymentBinding.getRoot(), R.id.action_onlinePaymentFragment_to_premiumProcessFragment, bundle);
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(onlinePaymentBinding.getRoot(), R.id.action_onlinePaymentFragment_to_premiumProcessFragment, bundle);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        //set click listener
        onlinePaymentBinding.buttonPerformLocalTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.setupUI(getActivity(),onlinePaymentBinding.getRoot());
                try {
                    startAFreshCharge(true);
                } catch (Exception e) {
                    new CustomMessage(getActivity(), "An error occurred while charging card");
//                    onlinePaymentBinding.textviewError.setText(String.format("An error occurred while charging card: %s %s", e.getClass().getSimpleName(), e.getMessage()));

                }
            }
        });
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
    private void startAFreshCharge(boolean local) {
        // initialize the charge
        charge = new Charge();
        charge.setCard(loadCardFromForm());

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Performing transaction... please wait");
        dialog.show();

        if (local) {
            // Set transaction params directly in app (note that these params
            // are only used if an access_code is not set. In debug mode,
            // setting them after setting an access code would throw an exception

            if (total_amount == 1000) {

                charge.setAmount(100000);
            } else if (total_amount == 2000) {
                charge.setAmount(200000);
            }
            //   charge.setAmount(total_amount);
            charge.setCurrency("NGN");
            charge.setEmail("contact@allschool.com.ng");
            charge.setReference("ChargedFromAndroid_" + Calendar.getInstance().getTimeInMillis());
            try {
                charge.putCustomField("Charged From", "Android SDK");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            chargeCard();
        } else {
            // Perform transaction/initialize on our server to get an access code
            // documentation: https://developers.paystack.co/reference#initialize-a-transaction
            new fetchAccessCodeFromServer().execute(backend_url + "/new-access-code");
        }
    }
    private Card loadCardFromForm() {
        //validate fields
        Card card;

        String cardNum =onlinePaymentBinding.editCardNumber.getText().toString().trim();

        //build card object with ONLY the number, update the other fields later
        card = new Card.Builder(cardNum, 0, 0, "").build();
        String cvc = onlinePaymentBinding.editCvc.getText().toString().trim();
        //update the cvc field of the card
        card.setCvc(cvc);

        //validate expiry month;
        String sMonth = onlinePaymentBinding.editExpiryMonth.getText().toString().trim();
        int month = 0;
        try {
            month = Integer.parseInt(sMonth);
        } catch (Exception ignored) {
        }

        card.setExpiryMonth(month);

        String sYear = onlinePaymentBinding.editExpiryYear.getText().toString().trim();
        int year = 0;
        try {
            year = Integer.parseInt(sYear);
        } catch (Exception ignored) {
        }
        card.setExpiryYear(year);

        return card;
    }
    @Override
    public void onPause() {
        super.onPause();

        if ((dialog != null) && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
    }
    private String c_date = new SimpleDateFormat("E, dd MMM yyyy hh:mm a", Locale.getDefault()).format(new Date());
    private void chargeCard() {
        transaction = null;
        PaystackSdk.chargeCard(getActivity(), charge, new Paystack.TransactionCallback() {
            // This is called only after transaction is successful
            @Override
            public void onSuccess(Transaction transaction) {
                dismissDialog();

                OnlinePaymentFragment.this.transaction = transaction;
//                onlinePaymentBinding.textviewError.setText(" ");
//                Toast.makeText(getContext(), transaction.getReference(), Toast.LENGTH_LONG).show();
                new CustomMessage(getActivity(),"Payment successful");
                updateTextViews();
                new verifyOnServer().execute(transaction.getReference());

                int addtime = 0;
                if (packagename.equals("p1")) {
                    addtime = 12;
                }
                if (packagename.equals("p2")) {
                    addtime = 12;
                }

                SimpleDateFormat df = new SimpleDateFormat("E, dd MMM yyyy hh:mm a");
                String c_date = new SimpleDateFormat("E, dd MMM yyyy hh:mm a", Locale.getDefault()).format(new Date());
                Calendar date1Calendar = Calendar.getInstance();
                try {
                    date1Calendar.setTime(new SimpleDateFormat("E, dd MMM yyyy hh:mm a").parse(c_date));
                    date1Calendar.add(Calendar.MONTH, addtime);

                    //          Toast.makeText(getContext(), ""+df.format(date1Calendar.getTime()), Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("isPremimum", true);
                hashMap.put("expire_date", df.format(date1Calendar.getTime()));
                hashMap.put("premium_type", packagename);
                if (NetworkHelper.hasNetworAccess(getContext())) {
                    final ProgressDialog mDialog = new ProgressDialog(getContext());
                    mDialog.setMessage("Submitting...");
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setIndeterminate(false);
                    mDialog.show();
                    AppController.getFirebaseHelper().getUsers().child(AppController.getFirebaseHelper().getFirebaseAuth().getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            new CustomMessage(getActivity(), "you are now premium user");
                            FragmentHelper.changeFragmet(onlinePaymentBinding.getRoot(), R.id.action_onlinePaymentFragment_to_nav_home, new Bundle());
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
            @Override
            public void beforeValidate(Transaction transaction) {
                OnlinePaymentFragment.this.transaction = transaction;
                Toast.makeText(getContext(), transaction.getReference(), Toast.LENGTH_LONG).show();
                updateTextViews();
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                OnlinePaymentFragment.this.transaction = transaction;
                if (error instanceof ExpiredAccessCodeException) {
                    startAFreshCharge(false);
                    chargeCard();
                    return;
                }

                dismissDialog();

                if (transaction.getReference() != null) {
                    new CustomMessage(getActivity(), error.getMessage());
//                    Toast.makeText(getContext(), transaction.getReference() + " concluded with error: " + error.getMessage(), Toast.LENGTH_LONG).show();
//                    onlinePaymentBinding.textviewError.setText(String.format("%s  concluded with error: %s %s", transaction.getReference(), error.getClass().getSimpleName(), error.getMessage()));
                    new verifyOnServer().execute(transaction.getReference());
                } else {
                    new CustomMessage(getActivity(), error.getMessage());
//                    onlinePaymentBinding.textviewError.setText(String.format("Error: %s %s", error.getClass().getSimpleName(), error.getMessage()));
                }
                updateTextViews();
            }

        });
    }

    private void dismissDialog() {
        if ((dialog != null) && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void updateTextViews() {
        if (transaction.getReference() != null) {
            onlinePaymentBinding.textviewReference.setText(String.format("Reference: %s", getContext().getResources().getString(R.string.app_name)));
        } else {
            onlinePaymentBinding.textviewReference.setText("No transaction");
        }
    }

    private boolean isEmpty(String s) {
        return s == null || s.length() < 1;
    }

    private class fetchAccessCodeFromServer extends AsyncTask<String, Void, String> {
        private String error;

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                charge.setAccessCode(result);
                chargeCard();
            } else {
                new CustomMessage(getActivity(),"There was a problem getting a new access code form the backend");
//                onlinePaymentBinding.textviewBackendMessage.setText(String.format("There was a problem getting a new access code form the backend: %s", error));
                dismissDialog();
            }
        }

        @Override
        protected String doInBackground(String... ac_url) {
            try {
                URL url = new URL(ac_url[0]);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                url.openStream()));

                String inputLine;
                inputLine = in.readLine();
                in.close();
                return inputLine;
            } catch (Exception e) {
                error = e.getClass().getSimpleName() + ": " + e.getMessage();
            }
            return null;
        }
    }

    private class verifyOnServer extends AsyncTask<String, Void, String> {
        private String reference;
        private String error;

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                new CustomMessage(getActivity(),"Gateway response error");
//                onlinePaymentBinding.textviewBackendMessage.setText(String.format("Gateway response: %s", result));

            } else {
                new CustomMessage(getActivity(),"There was a problem verifying %s on the backend");
//                onlinePaymentBinding.textviewBackendMessage.setText(String.format("There was a problem verifying %s on the backend: %s ", this.reference, error));
                dismissDialog();
            }
        }

        @Override
        protected String doInBackground(String... reference) {
            try {
                this.reference = reference[0];
                URL url = new URL(backend_url + "/verify/" + this.reference);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                url.openStream()));

                String inputLine;
                inputLine = in.readLine();
                in.close();
                return inputLine;
            } catch (Exception e) {
                error = e.getClass().getSimpleName() + ": " + e.getMessage();
            }
            return null;
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

    private String premium_type="";
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences pref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        premium_type = pref.getString("premiumType", "");
        if (TextUtils.equals(premium_type,"p0")){
            HomeNavigationActivity.getInstance().homeNavigationBinding.admobBanner.adView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(!isVisibleToUser){
            SharedPreferences pref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            premium_type = pref.getString("premiumType", "");
            if (TextUtils.equals(premium_type,"p0")){
                HomeNavigationActivity.getInstance().homeNavigationBinding.admobBanner.adView.setVisibility(View.VISIBLE);
            }
        }


    }
}
