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

public class SignUpActivty extends AppCompatActivity implements View.OnClickListener {

    private ActivitySignUpBinding signUpBinding;

    private String TAG = "SIGNUP_ACTIVTY";



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

