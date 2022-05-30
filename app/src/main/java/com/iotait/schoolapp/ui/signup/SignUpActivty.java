package com.iotait.schoolapp.ui.signup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
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
import com.iotait.schoolapp.common.Common;
import com.iotait.schoolapp.databinding.ActivityLoginBinding;
import com.iotait.schoolapp.databinding.ActivitySignUpBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.NetworkHelper;
import com.iotait.schoolapp.helper.ProgressHelper;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.ui.authentication.AuthenticationActivty;
import com.iotait.schoolapp.ui.homepage.HomeNavigationActivity;
import com.iotait.schoolapp.ui.login.LoginActivity;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivty extends AppCompatActivity implements View.OnClickListener {

    private ActivitySignUpBinding signUpBinding;

    private String TAG = "SIGNUP_ACTIVTY";


    private ActivityLoginBinding loginBinding;
    private ProgressHelper progressHelper;

    //declaring variables
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1;
    private CallbackManager mCallbackManager;
    private String TAG = "LOGIN_ACTIVTY";

    private boolean isOkName = true, isOkEmail = true, isOkRetypePass = true, isOkPh = true, isOkPro = true;

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ViewPump.init(ViewPump.builder()
//                .addInterceptor(new CalligraphyInterceptor(
//                        new CalligraphyConfig.Builder()
//                                .setDefaultFontPath("fonts/Lato-Regular.ttf")
//                                .setFontAttrId(R.attr.fontPath)
//                                .build()))
//                .build());
        signUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);


        signUpBinding.ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
            }
        });





        //For google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //creating google sign in client
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        snack = Snackbar.make(
                (LoginActivity.this.findViewById(android.R.id.content)),
                "Please Wait!", Snackbar.LENGTH_INDEFINITE);

        //initializing on click listener
        loginBinding.btnGoogle.setOnClickListener(this);
        loginBinding.btnFacebook.setReadPermissions("email");
        mCallbackManager = CallbackManager.Factory.create();
        loginBinding.btnFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                //   loginBinding.btnCreateAc.setClickable(false);
                loginBinding.btnFacebook.setClickable(false);
                loginBinding.btnGoogle.setClickable(false);
                loginBinding.havvingIssueText.setClickable(false);
                loginBinding.txtForgotPassword.setClickable(false);
                loginBinding.btnLogin.setClickable(false);
                loginBinding.editEmail.setEnabled(false);
                loginBinding.editEmail.setClickable(false);
                loginBinding.editPassword.setEnabled(false);
                loginBinding.editPassword.setClickable(false);


                // loginResult.getAccessToken().

                loginBinding.spinKitForGoogleAndFacebook.setVisibility(View.VISIBLE);
                snack.show();
                CheckDeviceForFacebook(loginResult);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:cancel");
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]

                //     loginBinding.btnCreateAc.setClickable(true);
                loginBinding.btnFacebook.setClickable(true);
                loginBinding.btnGoogle.setClickable(true);
                loginBinding.havvingIssueText.setClickable(true);
                loginBinding.txtForgotPassword.setClickable(true);
                loginBinding.btnLogin.setClickable(true);
                loginBinding.editEmail.setEnabled(true);
                loginBinding.editEmail.setClickable(true);
                loginBinding.editPassword.setEnabled(true);
                loginBinding.editPassword.setClickable(true);
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]

                //  loginBinding.btnCreateAc.setClickable(true);
                loginBinding.btnFacebook.setClickable(true);
                loginBinding.btnGoogle.setClickable(true);
                loginBinding.havvingIssueText.setClickable(true);
                loginBinding.txtForgotPassword.setClickable(true);
                loginBinding.btnLogin.setClickable(true);
                loginBinding.editEmail.setEnabled(true);
                loginBinding.editEmail.setClickable(true);
                loginBinding.editPassword.setEnabled(true);
                loginBinding.editPassword.setClickable(true);
            }
        });

        loginBinding.btnGoogle.setOnClickListener(this::onClick);
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
                        //   progressHelper.showProgress();

                        // loginBinding.spinKitForGoogleAndFacebook.setVisibility(View.VISIBLE);
                        if (task.isSuccessful()) {
                            loginBinding.spinKitForGoogleAndFacebook.setVisibility(View.GONE);
                            snack.dismiss();
                            new CustomMessage(LoginActivity.this, "Logged in");
                            FirebaseUser user = AppController.getFirebaseHelper().getFirebaseAuth().getCurrentUser();
                            updateUI(user);
                            updateFullExamCount(user);
                        } else {
                            loginBinding.spinKitForGoogleAndFacebook.setVisibility(View.GONE);
                            snack.dismiss();
                            new CustomMessage(LoginActivity.this, "Something wrong, please try again");
                            updateUI(null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // progressHelper.showProgress();

                        loginBinding.spinKitForGoogleAndFacebook.setVisibility(View.GONE);
                        snack.dismiss();
                        new CustomMessage(LoginActivity.this, "Something wrong, please try again");
                        //           loginBinding.btnCreateAc.setClickable(true);
                        loginBinding.btnFacebook.setClickable(true);
                        loginBinding.btnGoogle.setClickable(true);
                        loginBinding.havvingIssueText.setClickable(true);
                        loginBinding.txtForgotPassword.setClickable(true);
                        loginBinding.btnLogin.setClickable(true);
                        loginBinding.editEmail.setEnabled(true);
                        loginBinding.editEmail.setClickable(true);
                        loginBinding.editPassword.setEnabled(true);
                        loginBinding.editPassword.setClickable(true);
                        // Customlog.showlogD(TAG, e.getMessage());
                    }
                });
            } else {
                // progressHelper.dismissProgress();
                loginBinding.spinKitForGoogleAndFacebook.setVisibility(View.GONE);
                snack.dismiss();
                new CustomMessage(LoginActivity.this, "Can't create account, please try again");

                //      loginBinding.btnCreateAc.setClickable(true);
                loginBinding.btnFacebook.setClickable(true);
                loginBinding.btnGoogle.setClickable(true);
                loginBinding.havvingIssueText.setClickable(true);
                loginBinding.txtForgotPassword.setClickable(true);
                loginBinding.btnLogin.setClickable(true);
                loginBinding.editEmail.setEnabled(true);
                loginBinding.editEmail.setClickable(true);
                loginBinding.editPassword.setEnabled(true);
                loginBinding.editPassword.setClickable(true);
            }
        } else {
            //  progressHelper.dismissProgress();
            loginBinding.spinKitForGoogleAndFacebook.setVisibility(View.GONE);
            new CustomMessage(LoginActivity.this, "No Internet! Please Connect to the Internet");

            //     loginBinding.btnCreateAc.setClickable(true);
            loginBinding.btnFacebook.setClickable(true);
            loginBinding.btnGoogle.setClickable(true);
            loginBinding.havvingIssueText.setClickable(true);
            loginBinding.txtForgotPassword.setClickable(true);
            loginBinding.btnLogin.setClickable(true);
            loginBinding.editEmail.setEnabled(true);
            loginBinding.editEmail.setClickable(true);
            loginBinding.editPassword.setEnabled(true);
            loginBinding.editPassword.setClickable(true);

        }
    }
    Snackbar snack;
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
    private void loginFirebase(String user_nmail, String password) {
        AppController.getFirebaseHelper().getFirebaseAuth().signInWithEmailAndPassword(user_nmail, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loginBinding.spinKit.setVisibility(View.GONE);
                        if (task.isSuccessful()) {

                            fullexamCount();
                            Bundle bundle = new Bundle();
                            UIHelper.changeActivty(LoginActivity.this, HomeNavigationActivity.class, bundle);
                            finish();
                        } else {
                            //        loginBinding.btnCreateAc.setClickable(true);
                            loginBinding.btnFacebook.setClickable(true);
                            loginBinding.btnGoogle.setClickable(true);
                            loginBinding.havvingIssueText.setClickable(true);
                            loginBinding.txtForgotPassword.setClickable(true);
                            loginBinding.btnLogin.setClickable(true);
                            loginBinding.editEmail.setEnabled(true);
                            loginBinding.editEmail.setClickable(true);
                            loginBinding.editPassword.setEnabled(true);
                            loginBinding.editPassword.setClickable(true);
                            new CustomMessage(LoginActivity.this, "Login failed please try again");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loginBinding.spinKit.setVisibility(View.GONE);
                        //    loginBinding.btnCreateAc.setClickable(true);
                        loginBinding.btnFacebook.setClickable(true);
                        loginBinding.btnGoogle.setClickable(true);
                        loginBinding.havvingIssueText.setClickable(true);
                        loginBinding.txtForgotPassword.setClickable(true);
                        loginBinding.btnLogin.setClickable(true);
                        loginBinding.editEmail.setEnabled(true);
                        loginBinding.editEmail.setClickable(true);
                        loginBinding.editPassword.setEnabled(true);
                        loginBinding.editPassword.setClickable(true);
                        new CustomMessage(LoginActivity.this, e.getMessage());
                    }
                });
    }






    private void updateUI(FirebaseUser fUser) {

        //progressHelper.showProgress();
        loginBinding.spinKitForGoogleAndFacebook.setVisibility(View.VISIBLE);
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
                        new CustomMessage(LoginActivity.this, "Successfully signup");
                        UIHelper.changeActivty(LoginActivity.this, HomeNavigationActivity.class, new Bundle());
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // progressHelper.dismissProgress();
                        loginBinding.spinKitForGoogleAndFacebook.setVisibility(View.GONE);
                        new CustomMessage(LoginActivity.this, e.getMessage());
                        //    loginBinding.btnCreateAc.setClickable(true);
                        loginBinding.btnFacebook.setClickable(true);
                        loginBinding.btnGoogle.setClickable(true);
                        loginBinding.havvingIssueText.setClickable(true);
                        loginBinding.txtForgotPassword.setClickable(true);
                        loginBinding.btnLogin.setClickable(true);
                        loginBinding.editEmail.setEnabled(true);
                        loginBinding.editEmail.setClickable(true);
                        loginBinding.editPassword.setEnabled(true);
                        loginBinding.editPassword.setClickable(true);
                    }
                });
            } else {
                loginBinding.spinKitForGoogleAndFacebook.setVisibility(View.GONE);
                //  progressHelper.dismissProgress();
                new CustomMessage(LoginActivity.this, "Can't create account, please try again");
                //       loginBinding.btnCreateAc.setClickable(true);
                loginBinding.btnFacebook.setClickable(true);
                loginBinding.btnGoogle.setClickable(true);
                loginBinding.havvingIssueText.setClickable(true);
                loginBinding.txtForgotPassword.setClickable(true);
                loginBinding.btnLogin.setClickable(true);
                loginBinding.editEmail.setEnabled(true);
                loginBinding.editEmail.setClickable(true);
                loginBinding.editPassword.setEnabled(true);
                loginBinding.editPassword.setClickable(true);
            }
        } else {
            loginBinding.spinKitForGoogleAndFacebook.setVisibility(View.GONE);
            //  progressHelper.dismissProgress();
            new CustomMessage(LoginActivity.this, "Please Connect to the Internet");
            //    loginBinding.btnCreateAc.setClickable(true);
            loginBinding.btnFacebook.setClickable(true);
            loginBinding.btnGoogle.setClickable(true);
            loginBinding.havvingIssueText.setClickable(true);
            loginBinding.txtForgotPassword.setClickable(true);
            loginBinding.btnLogin.setClickable(true);
            loginBinding.editEmail.setEnabled(true);
            loginBinding.editEmail.setClickable(true);
            loginBinding.editPassword.setEnabled(true);
            loginBinding.editPassword.setClickable(true);
        }

    }














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

