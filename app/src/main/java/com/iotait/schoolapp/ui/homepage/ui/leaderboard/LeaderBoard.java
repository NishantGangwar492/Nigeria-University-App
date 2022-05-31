package com.iotait.schoolapp.ui.homepage.ui.leaderboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.iotait.schoolapp.R;
import com.iotait.schoolapp.databinding.FragmentLeaderBoardBinding;
import com.iotait.schoolapp.helper.FragmentHelper;
import com.iotait.schoolapp.ui.homepage.ui.adapters.SectionsPageAdapter;
import com.iotait.schoolapp.ui.homepage.ui.leaderboard.alltime.AllTimeFragment;
import com.iotait.schoolapp.ui.homepage.ui.leaderboard.contestresult.ContestResult;
import com.iotait.schoolapp.ui.homepage.ui.leaderboard.monthlytop.MonthlyTopFragment;
import com.iotait.schoolapp.ui.homepage.ui.leaderboard.weeklytop.WeeklyTopFragment;

public class LeaderBoard extends Fragment implements View.OnClickListener {

    FragmentLeaderBoardBinding binding;
    private SectionsPageAdapter mSectionsPageAdapter;


    private String type = "";
    private Bundle bundle;

    public LeaderBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_leader_board, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        bundle = getArguments();


        type = bundle.getString("type");


        mSectionsPageAdapter = new SectionsPageAdapter(getChildFragmentManager());

        binding.backbutton.setOnClickListener(this);
        setupViewPager(binding.viewpager);
        binding.tabs.setupWithViewPager(binding.viewpager);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_LeaderBoard_to_navigation, bundle);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

    }

    private void setupViewPager(ViewPager viewpager) {
        mSectionsPageAdapter.addFragment(new WeeklyTopFragment(), "Weekly");
        mSectionsPageAdapter.addFragment(new MonthlyTopFragment(), "Monthly");
        mSectionsPageAdapter.addFragment(new AllTimeFragment(), "All-Time");
        mSectionsPageAdapter.addFragment(new ContestResult(), "Contest");

        viewpager.setAdapter(mSectionsPageAdapter);
        if (type.equals("WeeklyContest")) {
            viewpager.setCurrentItem(3);
        } else {
            viewpager.setCurrentItem(0);
        }
        viewpager.setOffscreenPageLimit(4);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backbutton:
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(binding.getRoot(), R.id.action_LeaderBoard_to_navigation, bundle);
                break;
        }
    }


}
