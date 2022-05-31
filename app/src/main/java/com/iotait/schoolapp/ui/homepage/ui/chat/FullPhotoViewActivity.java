package com.iotait.schoolapp.ui.homepage.ui.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.databinding.ActivityFullPhotoViewBinding;
//import com.iotait.schoolapp.databinding.ActivityFullPhotoViewBindingImpl;

public class FullPhotoViewActivity extends AppCompatActivity {

    ActivityFullPhotoViewBinding binding;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_photo_view);

        binding.includeToolbar.toolbarTitle.setText("");
        setSupportActionBar(binding.includeToolbar.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        url = getIntent().getStringExtra("URL_KEY");
        Glide.with(this).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.photoView);
    }
}
