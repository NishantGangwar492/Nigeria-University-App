package com.iotait.schoolapp.ui.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.databinding.ActivityAuthenticationBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.Customlog;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.ui.homepage.HomeNavigationActivity;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class AuthenticationActivty extends AppCompatActivity {

    private ActivityAuthenticationBinding activityAuthenticationBinding;
    private String TAG = "AUTHENTICATION";
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private Bundle bundle;
    private HashMap<String, Object> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAuthenticationBinding = DataBindingUtil.setContentView(this, R.layout.activity_authentication);
        Intent in = getIntent();
        bundle = in.getExtras();
        hashMap = (HashMap<String, Object>) bundle.getSerializable("hasmap");
        Customlog.showlogD(TAG," " + hashMap);
        getIntentExtra();


        activityAuthenticationBinding.pinview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged() called with: s = [" + s + "], start = [" + start + "], before = [" + before + "], count = [" + count + "]");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 6) {
                    UIHelper.setupUI(AuthenticationActivty.this, activityAuthenticationBinding.getRoot());
                    verifyCode(s.toString());
                }
            }
        });
        activityAuthenticationBinding.txtResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendVerificationCode((String) hashMap.get("phone"), resendingToken);
                activityAuthenticationBinding.pinview.setText("");
            }
        });
        activityAuthenticationBinding.txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Verifying OTP
     */
    private void verifyCode(String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Verification Code is wrong", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Verifying with OTP
     */
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                /**When verification is completed*/
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String sentCode = phoneAuthCredential.getSmsCode();

                    if (sentCode != null) {
                        activityAuthenticationBinding.pinview.setText(sentCode);
                        Toast.makeText(AuthenticationActivty.this, "Successfully verified.", Toast.LENGTH_SHORT).show();
                        //session management
                        activityAuthenticationBinding.tvRemainingTime.setVisibility(View.GONE);


                    } else {
                        new CustomMessage(AuthenticationActivty.this, "Not verified");
                    }

                }

                /**When verification is failed*/
                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(AuthenticationActivty.this, "Invalid Request.", Toast.LENGTH_SHORT).show();
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        Toast.makeText(AuthenticationActivty.this, "Too many requests. Please try again later.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AuthenticationActivty.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                /**When code is sent*/
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    resendingToken = forceResendingToken;
                    verificationId = s;
                }
            };

    private void runTimer() {
        new CountDownTimer(30 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                activityAuthenticationBinding.tvRemainingTime.setVisibility(View.VISIBLE);
                activityAuthenticationBinding.txtResend.setVisibility(View.GONE);
                activityAuthenticationBinding.tvRemainingTime.setText(MessageFormat.format("Remaining Time : {0}", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                activityAuthenticationBinding.txtResend.setVisibility(View.VISIBLE);
                activityAuthenticationBinding.tvRemainingTime.setVisibility(View.GONE);

            }
        }.start();
    }

    /**
     * Getting passed data from intent
     */
    private void getIntentExtra() {
        if (getIntent() != null && getIntent().getExtras() != null) {

            String phoneNo = (String) hashMap.get("phone");
            if (phoneNo != null) {

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNo,
                        30,
                        TimeUnit.SECONDS,
                        this,
                        mCallbacks
                );
                runTimer();
            }

        }
    }

    /**
     * Signing in after successful OTP verification
     */
    private void signInWithCredential(PhoneAuthCredential credential) {
        AppController.getFirebaseHelper().getFirebaseAuth().createUserWithEmailAndPassword(String.valueOf(hashMap.get("email")), String.valueOf(hashMap.get("password")))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    AppController.getFirebaseHelper().getUsers().child(AppController.getFirebaseHelper().getFirebaseAuth().getUid()).updateChildren(hashMap);
                    UIHelper.changeActivty(AuthenticationActivty.this, HomeNavigationActivity.class, bundle);
                    finish();
                } else {
                    new CustomMessage(AuthenticationActivty.this, "Can't create account");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                new CustomMessage(AuthenticationActivty.this, e.getMessage());
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
        });
    }

    /**
     * For resending OTP
     */
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
        runTimer();
    }
}
