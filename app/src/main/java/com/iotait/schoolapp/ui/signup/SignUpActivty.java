package com.iotait.schoolapp.ui.signup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.facebook.CallbackManager;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.common.Common;
import com.iotait.schoolapp.databinding.ActivitySignUpBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.ui.authentication.AuthenticationActivty;
import com.iotait.schoolapp.ui.login.LoginActivity;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.databinding.ActivityLoginBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.Customlog;
import com.iotait.schoolapp.helper.NetworkHelper;
import com.iotait.schoolapp.helper.ProgressHelper;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.ui.homepage.HomeNavigationActivity;
import com.iotait.schoolapp.ui.signup.SignUpActivty;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class SignUpActivty extends AppCompatActivity implements View.OnClickListener {

    private ActivitySignUpBinding signUpBinding;

    private String TAG = "SIGNUP_ACTIVTY";

    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1;
    private CallbackManager mCallbackManager;
    Snackbar snack;


    private boolean isOkName = true, isOkEmail = true, isOkRetypePass = true, isOkPh = true, isOkPro = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        signUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        signUpBinding.btnGoogle.setOnClickListener(this);
        mCallbackManager = CallbackManager.Factory.create();


        //For google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //creating google sign in client
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        snack = Snackbar.make(
                (SignUpActivty.this.findViewById(android.R.id.content)),
                "Please Wait!", Snackbar.LENGTH_INDEFINITE);



        signUpBinding.ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
            }
        });


        signUpBinding.alreadyHaveAc.setOnClickListener(this);
        signUpBinding.btnSignup.setOnClickListener(this);

        signUpBinding.editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!Patterns.EMAIL_ADDRESS.matcher(editable.toString()).matches()) {
                    signUpBinding.editEmail.setError("Invalid email");
                }
            }
        });
        signUpBinding.editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() < 6) {
                    signUpBinding.editPassword.setError("password atleast 6 characters");
                }
            }
        });

        //signUpBinding.havvingIssueText.setOnClickListener(this);
    }

//        THE ONCREATE INTANCE ENDS HERE, REST CODE NEEDS TO BE BELOW IT





    private void googlesignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        snack.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    //handle sign in result
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {

        if (NetworkHelper.hasNetworAccess(this)) {
            // progressHelper.dismissProgress();
            //loginBinding.spinKitForGoogleAndFacebook.setVisibility(View.GONE);
            try {
                GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
                CheckDeviceForGoogle(acc);
            } catch (ApiException e) {

                new CustomMessage(this, "Sign In Failed");
                Customlog.showlogD(TAG, e.getMessage());
                FirebaseGoogleAuth(null);
            }
        } else {
            new CustomMessage(SignUpActivty.this, "No Internet! Please Connect to the Internet");
        }
    }

    private void CheckDeviceForGoogle(GoogleSignInAccount acc) {
        @SuppressLint("HardwareIds")
        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Query query = AppController.getFirebaseHelper().getUsers().orderByChild("email").equalTo(acc.getEmail());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String email = "";
                    String deviceId = "";
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.child("email").getValue() != null) {
                            email = snapshot.child("email").getValue().toString();
                        }
                        if (snapshot.child("device_id").getValue()!= null) {
                            deviceId = snapshot.child("device_id").getValue().toString();
                        }
                        boolean isPremium = (Boolean) snapshot.child("isPremimum").getValue();
                        if (isPremium) {
                            if (android_id.equals(deviceId)) {
                                new CustomMessage(SignUpActivty.this, "Signed In Successfully");
                                FirebaseGoogleAuth(acc);
                            } else if (deviceId.equals("")) {
                                new CustomMessage(SignUpActivty.this, "Signed In Successfully");
                                FirebaseGoogleAuth(acc);
                            } else {
                                new CustomMessage(SignUpActivty.this, "Account is associated with another device!");
                            }
                        } else {
                            new CustomMessage(SignUpActivty.this, "Signed In Successfully");
                            FirebaseGoogleAuth(acc);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    //fetch google firebase sign in auth
    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
        //  check if the account is null
        //  progressHelper.showProgress();
        // loginBinding.spinKitForGoogleAndFacebook.setVisibility(View.VISIBLE);
        if (NetworkHelper.hasNetworAccess(this)) {
            if (acct != null) {
                AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
                AppController.getFirebaseHelper().getFirebaseAuth().signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            snack.dismiss();
                            new CustomMessage(SignUpActivty.this, "Logged in");
                            FirebaseUser user = AppController.getFirebaseHelper().getFirebaseAuth().getCurrentUser();
                            updateUI(user);
                            updateFullExamCount(user);
                        } else {
                            snack.dismiss();
                            new CustomMessage(SignUpActivty.this, "Something wrong, please try again");
                            updateUI(null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        snack.dismiss();
                        new CustomMessage(SignUpActivty.this, "Something wrong, please try again");

                    }
                });
            } else {
                snack.dismiss();
                new CustomMessage(SignUpActivty.this, "Can't create account, please try again");
            }
        } else {
            new CustomMessage(SignUpActivty.this, "No Internet! Please Connect to the Internet");
        }
    }


    private void updateFullExamCount(FirebaseUser fUser) {
        /* Check user exam taken value */
        AppController.getFirebaseHelper().getUsers().child(AppController.getFirebaseHelper().getFirebaseAuth().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild("fullexamcount")){
                    HashMap<String, Object> user = new HashMap();
                    user.put("fullexamcount", 0);
                    AppController.getFirebaseHelper().getUsers().child(AppController.getFirebaseHelper().getFirebaseAuth().getUid()).updateChildren(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //if sign in complete or not updating ui
    private void updateUI(FirebaseUser fUser) {
        if (NetworkHelper.hasNetworAccess(this)) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
            if (account != null) {
                String personName = account.getDisplayName();
                String personGivenName = account.getGivenName();
                String personFamilyName = account.getFamilyName();
                String personEmail = account.getEmail();
                String personId = account.getId();
                Uri personPhoto = account.getPhotoUrl();
                String username = "@" + personName.toLowerCase().replaceAll(" ", "");
                HashMap<String, Object> user = new HashMap<>();
                user.put("uid", AppController.getFirebaseHelper().getFirebaseAuth().getUid());
                user.put("personname", personGivenName);
                user.put("username", username);
                user.put("email", personEmail);
                user.put("password", "");
                //  user.put("date", Common.c_date);
                user.put("photo", personPhoto.toString());

                AppController.getFirebaseHelper().getUsers().child(fUser.getUid()).updateChildren(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        new CustomMessage(SignUpActivty.this, "Successfully signup");
                        UIHelper.changeActivty(SignUpActivty.this, HomeNavigationActivity.class, new Bundle());
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // progressHelper.dismissProgress();
                        new CustomMessage(SignUpActivty.this, e.getMessage());
                    }
                });
            } else {
                //  progressHelper.dismissProgress();
                new CustomMessage(SignUpActivty.this, "Can't create account, please try again");
            }
        } else {
            new CustomMessage(SignUpActivty.this, "Please Connect to the Internet");
        }

    }













    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.already_have_ac:
                finish();
                break;
            case R.id.btn_signup:
                startSignup();
                break;
            case R.id.havving_issue_text:
                contactUs();
                break;
            case R.id.btn_google:
                //     loginBinding.btnCreateAc.setClickable(false);
                googlesignIn();
                break;
        }
    }

    private void contactUs() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.contact_us))));
    }

    private void startSignup() {
        if (!Patterns.EMAIL_ADDRESS.matcher(signUpBinding.editEmail.getText().toString()).matches() || signUpBinding.editEmail.getText().toString().equals("")) {
            signUpBinding.editEmail.setError("Please provide a valid email address");
            isOkEmail = false;
        } else
            isOkEmail = true;
        if (signUpBinding.editUsername.getText().toString().length() < 3) {
            signUpBinding.editUsername.setError("Name not valid");
            isOkName = false;
        } else
            isOkName = true;
        if (signUpBinding.editPassword.getText().toString().length() < 6) {
            signUpBinding.editPassword.setError("Password not strong");
            isOkRetypePass = false;
        } else
            isOkRetypePass = true;
        if (TextUtils.isEmpty(signUpBinding.editPhone.getText().toString()) || !Patterns.PHONE.matcher(signUpBinding.editPhone.getText().toString()).matches()) {
            signUpBinding.editPhone.setError("Provide your phone number");
            isOkPh = false;
        } else
            isOkPh = true;
        if (isOkName && isOkEmail && isOkPro && isOkPh && isOkRetypePass) {
            signup();
        }
    }

    private void signup() {
        final String phone = "+" + signUpBinding.ccp.getSelectedCountryCode() + signUpBinding.editPhone.getText().toString();
        if (isValidPhone(phone)) {
            HashMap<String, Object> user = new HashMap<>();
            user.put("uid", AppController.getFirebaseHelper().getFirebaseAuth().getUid());
            user.put("personname", signUpBinding.editUsername.getText().toString());
            user.put("username", "@" + signUpBinding.editUsername.getText().toString().toLowerCase().replace(" ", ""));
            user.put("email", signUpBinding.editEmail.getText().toString());
            user.put("phone", phone);
            user.put("password", signUpBinding.editPassword.getText().toString());
            user.put("date", Common.c_date);
            user.put("isPremimum", false);
            user.put("countrycode", "+" + signUpBinding.ccp.getSelectedCountryCode());
            user.put("photo", "");
            user.put("fullexamcount", 0);
            user.put("premium_type", "p0");
            user.put("point", "0");
            Bundle bundle = new Bundle();
            bundle.putSerializable("hasmap", user);
            UIHelper.changeActivty(SignUpActivty.this, AuthenticationActivty.class, bundle);
        } else {
            new CustomMessage(SignUpActivty.this, "please provide a valid phone number");
        }
    }

    public static boolean isValidPhone(String s) {
        Pattern p = Pattern.compile("^\\+(?:[0-9] ?){6,14}[0-9]$");
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

    public void openLoginPage(View view) {
        Intent intent = new Intent(SignUpActivty.this, LoginActivity.class);
        startActivity(intent);
    }
}

