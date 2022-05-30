package com.iotait.schoolapp.ui.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.databinding.ActivityLoginBinding;
import com.iotait.schoolapp.databinding.ActivitySplashBinding;
import com.iotait.schoolapp.helper.ProgressHelper;
import com.iotait.schoolapp.ui.login.First;
import com.iotait.schoolapp.ui.login.LoginActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends AppCompatActivity {



    private ActivitySplashBinding splashBinding;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        splashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);


//        @SuppressLint("HardwareIds")
//        final TelephonyManager mTelephony = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//        if (mTelephony.getImei(1) != null)
//            Log.d("TAG", "onCreate: " + mTelephony.getImei());
//           // deviceId = mTelephony.getDeviceId();
        @SuppressLint("HardwareIds")
        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("TAG", "onCreate: " + android_id);


//        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//        @SuppressLint("HardwareIds")
//        String deviceId = telephonyManager.getDeviceId();
        //Toast.makeText(this, ""+android_id, Toast.LENGTH_SHORT).show();

        //generate keyhash for facebook login
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        splashBinding.progressBar.animateProgress(5000, 0, 100);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, First.class));
                finish();
            }
        },5000);
    }
}
