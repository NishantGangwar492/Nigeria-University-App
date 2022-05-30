package com.iotait.schoolapp.ui.login;

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

import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityLoginBinding loginBinding;
    private ProgressHelper progressHelper;

    //declaring variables
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1;
    private CallbackManager mCallbackManager;
    private String TAG = "LOGIN_ACTIVTY";

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        progressHelper = new ProgressHelper(this);
        progressHelper.setProgres("Loading...");
        loginBinding.btnLogin.setOnClickListener(this);
        loginBinding.txtDontHaveAc.setOnClickListener(this);
        loginBinding.txtForgotPassword.setOnClickListener(this);
    //    loginBinding.btnCreateAc.setOnClickListener(this);
        loginBinding.havvingIssueText.setOnClickListener(this::onClick);

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


    private void handleFacebookAccessToken(AccessToken token, final LoginResult loginResult) {


        //progressHelper.showProgress();
       // loginBinding.spinKitForGoogleAndFacebook.setVisibility(View.VISIBLE);

        if (NetworkHelper.hasNetworAccess(this)) {
            if (token != null) {
                AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
                AppController.getFirebaseHelper().getFirebaseAuth().signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            loginBinding.spinKitForGoogleAndFacebook.setVisibility(View.GONE);
                            snack.dismiss();
                            new CustomMessage(LoginActivity.this, "Loged in");
                            FirebaseUser user = AppController.getFirebaseHelper().getFirebaseAuth().getCurrentUser();
                            updateUIWithFacebook(user, loginResult);
                            updateFullExamCount(user);

                        } else if (!task.isSuccessful()) {
                            snack.dismiss();
                            Snackbar snackbar = Snackbar.make(loginBinding.getRoot(), "" + task.getException().getMessage(), Snackbar.LENGTH_LONG);
                            View snackbarView = snackbar.getView();
                            TextView snackTextView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);

                            snackTextView.setMaxLines(4);
                            snackbar.show();

                        } else {
                            //       loginBinding.btnCreateAc.setClickable(true);
                            loginBinding.spinKitForGoogleAndFacebook.setVisibility(View.GONE);
                            snack.dismiss();
                            loginBinding.btnFacebook.setClickable(true);
                            loginBinding.btnGoogle.setClickable(true);
                            loginBinding.havvingIssueText.setClickable(true);
                            loginBinding.txtForgotPassword.setClickable(true);
                            loginBinding.btnLogin.setClickable(true);
                            loginBinding.editEmail.setEnabled(true);
                            loginBinding.editEmail.setClickable(true);
                            loginBinding.editPassword.setEnabled(true);
                            loginBinding.editPassword.setClickable(true);

                            // new CustomMessage(LoginActivity.this, "Something wrong, please try again");
                            Snackbar snackbar = Snackbar.make(loginBinding.getRoot(), "" + task.getException().getMessage(), Snackbar.LENGTH_LONG);
                            View snackbarView = snackbar.getView();
                            TextView snackTextView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);

                            snackTextView.setMaxLines(4);
                            snackbar.show();
//                        updateUIWithFacebook(null, null);
                            //  Log.d(TAG, "onComplete: " + task.getException().getMessage());
                            //  progressHelper.showProgress();

                        }
                    }
                });
            } else {
                loginBinding.spinKitForGoogleAndFacebook.setVisibility(View.GONE);
                snack.dismiss();
                // progressHelper.dismissProgress();

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

                new CustomMessage(LoginActivity.this, "Can't create account, please try again");
            }
        } else {
            loginBinding.spinKitForGoogleAndFacebook.setVisibility(View.GONE);
            snack.dismiss();

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

            new CustomMessage(LoginActivity.this, "No Internet! Please connect to the internet");
        }
    }

    //google sign in intent call
    private void googlesignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        // progressHelper.showProgress();
        loginBinding.spinKitForGoogleAndFacebook.setVisibility(View.VISIBLE);


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
                loginBinding.spinKitForGoogleAndFacebook.setVisibility(View.GONE);
                new CustomMessage(this, "Sign In Failed");
                Customlog.showlogD(TAG, e.getMessage());
                FirebaseGoogleAuth(null);
            }
        } else {
            loginBinding.spinKitForGoogleAndFacebook.setVisibility(View.GONE);
            new CustomMessage(LoginActivity.this, "No Internet! Please Connect to the Internet");
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
                                new CustomMessage(LoginActivity.this, "Signed In Successfully");
                                FirebaseGoogleAuth(acc);
                            } else if (deviceId.equals("")) {
                                new CustomMessage(LoginActivity.this, "Signed In Successfully");
                                FirebaseGoogleAuth(acc);
                            } else {
                                new CustomMessage(LoginActivity.this, "Account is associated with another device!");
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
                            new CustomMessage(LoginActivity.this, "Signed In Successfully");
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

    //if sign in complete or not updating ui
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

    private void CheckDeviceForFacebook(LoginResult loginResult) {
        @SuppressLint("HardwareIds")
        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                String email = "";
                if (response != null) {
                    try {
                        if (object.has("email"))
                            email = object.getString("email");

                        Query query = AppController.getFirebaseHelper().getUsers().orderByChild("email").equalTo(email);

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
                                        if (snapshot.child("device_id").getValue() != null) {
                                            deviceId = snapshot.child("device_id").getValue().toString();
                                        }
                                        boolean isPremium = (Boolean) snapshot.child("isPremimum").getValue();
                                        if (isPremium) {
                                            if (android_id.equals(deviceId)) {
                                                new CustomMessage(LoginActivity.this, "Signed In Successfully");
                                                handleFacebookAccessToken(loginResult.getAccessToken(), loginResult);
                                            } else if (deviceId.equals("")) {
                                                new CustomMessage(LoginActivity.this, "Signed In Successfully");
                                                handleFacebookAccessToken(loginResult.getAccessToken(), loginResult);

                                            } else {
                                                new CustomMessage(LoginActivity.this, "Account is associated with another device!");
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
                                            new CustomMessage(LoginActivity.this, "Signed In Successfully");
                                            handleFacebookAccessToken(loginResult.getAccessToken(), loginResult);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                        new CustomMessage(LoginActivity.this, "Can't create account, please try again");
                        // Log.d(TAG, "onCompleted: " + e.getMessage());
                    }
                }
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString("fields", "first_name,last_name,email,id");
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();
    }

    private void updateUIWithFacebook(final FirebaseUser fUser, LoginResult loginResult) {
       // loginBinding.spinKitForGoogleAndFacebook.setVisibility(View.VISIBLE);
        if (NetworkHelper.hasNetworAccess(this)) {
            GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    progressHelper.dismissProgress();
                    String email = "";
                    if (response != null) {
                        try {
                            String personlastname = object.getString("last_name");
                            String personfirstname = object.getString("first_name");
                            if (object.has("email"))
                                email = object.getString("email");
                            String id = object.getString("id");
                            String personPhoto = "https://graph.facebook.com/" + id + "/picture?type=normal";
                            String username = "@" + personlastname.toLowerCase().replaceAll(" ", "");

                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.dontAnimate();

                            HashMap<String, Object> user = new HashMap<>();
                            user.put("uid", AppController.getFirebaseHelper().getFirebaseAuth().getUid());
                            user.put("personname", personlastname);
                            user.put("username", username);
                            user.put("email", email);
                            // user.put("phone", "");
                            user.put("password", "");
                            //  user.put("date", Common.c_date);
                            //   user.put("isPremimum", false);
                            //   user.put("countrycode", "");
                            user.put("photo", personPhoto);
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
                                    new CustomMessage(LoginActivity.this, e.getMessage());
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            new CustomMessage(LoginActivity.this, "Can't create account, please try again");
                            // Log.d(TAG, "onCompleted: " + e.getMessage());
                        }
                    }
                }
            });
            Bundle bundle = new Bundle();
            bundle.putString("fields", "first_name,last_name,email,id");
            graphRequest.setParameters(bundle);
            graphRequest.executeAsync();
        } else {
            loginBinding.spinKitForGoogleAndFacebook.setVisibility(View.GONE);
            snack.dismiss();

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
            new CustomMessage(LoginActivity.this, "No Internet! Please Connect to the Internet");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppController.getFirebaseHelper().getFirebaseAuth().getCurrentUser() != null) {
            Bundle bundle = new Bundle();
            UIHelper.changeActivty(LoginActivity.this, HomeNavigationActivity.class, bundle);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_login:
                if (!TextUtils.isEmpty(loginBinding.editEmail.getText().toString()) && !TextUtils.isEmpty(loginBinding.editPassword.getText().toString())) {
                    if (checkEmailisValid(loginBinding.editEmail.getText().toString())) {
                        if (loginBinding.editPassword.getText().toString().length() > 5) {
                            loginBinding.btnFacebook.setClickable(false);
                            loginBinding.btnGoogle.setClickable(false);
                            loginBinding.havvingIssueText.setClickable(false);
                            loginBinding.txtForgotPassword.setClickable(false);
                            loginBinding.btnLogin.setClickable(false);
                            loginBinding.editEmail.setEnabled(false);
                            loginBinding.editEmail.setClickable(false);
                            loginBinding.editPassword.setEnabled(false);
                            loginBinding.editPassword.setClickable(false);

                            CheckDeviceForNormalSignIn(loginBinding.editEmail.getText().toString());
                            UIHelper.setupUI(this, loginBinding.getRoot());
                        } else {
                            new CustomMessage(this, "Password need atleast 6 characters");
                        }
                    } else {
                        new CustomMessage(this, "Your email is not valid");
                    }
                } else
                    new CustomMessage(this, "Please provide your email and password");
                break;
            case R.id.txt_dont_have_ac:

                UIHelper.changeActivty(LoginActivity.this, SignUpActivty.class, new Bundle());
                break;
            case R.id.txt_forgot_password:
                showCustomDialog();
                break;
            case R.id.havving_issue_text:
                contactUs();
                break;
            case R.id.btn_google:
                //     loginBinding.btnCreateAc.setClickable(false);
                loginBinding.btnFacebook.setClickable(false);
                loginBinding.btnGoogle.setClickable(false);
                loginBinding.havvingIssueText.setClickable(false);
                loginBinding.txtForgotPassword.setClickable(false);
                loginBinding.btnLogin.setClickable(false);
                loginBinding.editEmail.setEnabled(false);
                loginBinding.editEmail.setClickable(false);
                loginBinding.editPassword.setEnabled(false);
                loginBinding.editPassword.setClickable(false);
                googlesignIn();
                break;
        }
    }

    private void CheckDeviceForNormalSignIn(String email) {
        @SuppressLint("HardwareIds")
        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Query query = AppController.getFirebaseHelper().getUsers().orderByChild("email").equalTo(email);

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
                        if (snapshot.child("device_id").getValue() != null) {
                            deviceId = snapshot.child("device_id").getValue().toString();
                        }
                        boolean isPremium = (Boolean) snapshot.child("isPremimum").getValue();
                        if (isPremium) {
                            if (android_id.equals(deviceId)) {
                                new CustomMessage(LoginActivity.this, "Signed In Successfully");
                                login(loginBinding.editEmail.getText().toString(), loginBinding.editPassword.getText().toString());
                            } else if (deviceId.equals("")) {
                                new CustomMessage(LoginActivity.this, "Signed In Successfully");
                                login(loginBinding.editEmail.getText().toString(), loginBinding.editPassword.getText().toString());
                            } else {
                                new CustomMessage(LoginActivity.this, "Account is associated with another device!");
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
                            new CustomMessage(LoginActivity.this, "Signed In Successfully");
                            login(loginBinding.editEmail.getText().toString(), loginBinding.editPassword.getText().toString());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    private void contactUs() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.contact_us))));
    }

    private String user_nmail = "";

    private void login(final String email, final String password) {

        if (NetworkHelper.hasNetworAccess(LoginActivity.this)) {
            loginBinding.spinKit.setVisibility(View.VISIBLE);
            AppController.getFirebaseHelper().getUsers()
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String semail = dataSnapshot1.child("email").getValue(String.class);

                                    if (TextUtils.equals(semail, email)) {
                                        user_nmail = semail;
                                        break;
                                    }
                                }
                                if (!user_nmail.equals("")) {

                                    loginFirebase(user_nmail, password);
                                } else {
                                    loginBinding.spinKit.setVisibility(View.GONE);
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
                                    new CustomMessage(LoginActivity.this, "Email not matched");
                                }

                            } else {
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
                                new CustomMessage(LoginActivity.this, "Login failed please try again");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            //          loginBinding.btnCreateAc.setClickable(true);
                            loginBinding.btnFacebook.setClickable(true);
                            loginBinding.btnGoogle.setClickable(true);
                            loginBinding.havvingIssueText.setClickable(true);
                            loginBinding.txtForgotPassword.setClickable(true);
                            loginBinding.btnLogin.setClickable(true);
                            loginBinding.editEmail.setEnabled(true);
                            loginBinding.editEmail.setClickable(true);
                            loginBinding.editPassword.setEnabled(true);
                            loginBinding.editPassword.setClickable(true);
                            new CustomMessage(LoginActivity.this, databaseError.getMessage());
                        }
                    });
        } else {

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
            new CustomMessage(this, "No internet connection");
        }
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

    private void fullexamCount() {
        AppController.getFirebaseHelper().getUsers().child(AppController.getFirebaseHelper().getFirebaseAuth().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild("fullexamcount")) {
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

    public boolean checkEmailisValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private void showCustomDialog() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_forget_pass, viewGroup, false);
        Button btn_ok = dialogView.findViewById(R.id.buttonOk);
        final EditText edit_email = dialogView.findViewById(R.id.edit_forget_email);
        edit_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!Patterns.EMAIL_ADDRESS.matcher(editable.toString()).matches()) {
                    edit_email.setError("Invalid email");
                }
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEmailisValid(edit_email.getText().toString())) {
                    progressHelper.showProgress();
                    UIHelper.setupUI(LoginActivity.this, dialogView);
                    AppController.getFirebaseHelper().getFirebaseAuth().sendPasswordResetEmail(edit_email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressHelper.dismissProgress();
                            if (task.isSuccessful()) {
                                new CustomMessage(LoginActivity.this, "Please check your email");
                                alertDialog.dismiss();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Customlog.showlogD("FORGOT_PASS_FAILD", e.getMessage());
                            new CustomMessage(LoginActivity.this, "There is not user with this email.");
                            progressHelper.dismissProgress();
                        }
                    });
                } else {
                    edit_email.setError("Invalid email");
                }
            }
        });
        builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.show();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finishAffinity();
//    }

    public void openWhatsapp(View view) {
        String url = "https://api.whatsapp.com/send?phone=+2348093785476";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
