package com.iotait.schoolapp.ui.homepage.ui.premium.learnmorepackage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.iotait.schoolapp.R;
import com.iotait.schoolapp.databinding.FragmentLearMoreForPremiumBinding;
import com.iotait.schoolapp.helper.FragmentHelper;


public class LearMoreForPremium extends Fragment implements View.OnClickListener {

    FragmentLearMoreForPremiumBinding binding;

    public LearMoreForPremium() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lear_more_for_premium, container, false);
        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.back:
                FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_learnmorePremium_to_premiumProcessFragment, bundle);
                break;

        }
    }
}
