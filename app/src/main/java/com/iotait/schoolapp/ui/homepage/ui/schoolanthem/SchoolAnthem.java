package com.iotait.schoolapp.ui.homepage.ui.schoolanthem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.iotait.schoolapp.R;
import com.iotait.schoolapp.databinding.FragmentSchoolAnthemBinding;
import com.iotait.schoolapp.helper.FragmentHelper;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.ui.homepage.ui.detailsnews.DetailsNewsWebView;


public class SchoolAnthem extends Fragment implements View.OnClickListener {

    FragmentSchoolAnthemBinding fragmentSchoolAnthemBinding;

    public SchoolAnthem() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        fragmentSchoolAnthemBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_school_anthem, container, false);

        View root = fragmentSchoolAnthemBinding.getRoot();

        fragmentSchoolAnthemBinding.schoolAnthemAudio.setOnClickListener(this);
        fragmentSchoolAnthemBinding.schoolAnthemVideo.setOnClickListener(this);

        fragmentSchoolAnthemBinding.includeToolbar.toolbarTitle.setText("SCHOOL ANTHEM");
        ((AppCompatActivity) getActivity()).setSupportActionBar(fragmentSchoolAnthemBinding.includeToolbar.toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        fragmentSchoolAnthemBinding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(fragmentSchoolAnthemBinding.getRoot(), R.id.action_schoolAnthem_to_nav_home, bundle);
            }
        });

        return root;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.schoolAnthemAudio:
                Bundle bundle = new Bundle();
                bundle.putString("link", "https://allschool.com.ng/unn-school-anthem-videolyrics-audio-lyrics/");
                UIHelper.changeActivty(getContext(), DetailsNewsWebView.class, bundle);
                break;
            case R.id.schoolAnthemVideo:
                Bundle bundl = new Bundle();
                bundl.putString("link", "https://allschool.com.ng/unn-school-anthem-videolyrics-audio-lyrics/");
                UIHelper.changeActivty(getContext(), DetailsNewsWebView.class, bundl);
                break;
        }
    }
}
