package com.iotait.schoolapp.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseUser;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.ui.homepage.HomeNavigationActivity;
import com.iotait.schoolapp.ui.signup.SignUpActivty;

public class First extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }

    public void openLoginPage(View view) {
        Intent intent = new Intent(First.this, LoginActivity.class);
        startActivity(intent);
    }

    public void openSignUpPage(View view) {
        Intent intent = new Intent(First.this, SignUpActivty.class);
        startActivity(intent);
    }
}