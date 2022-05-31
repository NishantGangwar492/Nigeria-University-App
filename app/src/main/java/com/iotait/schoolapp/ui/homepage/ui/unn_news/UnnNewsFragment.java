package com.iotait.schoolapp.ui.homepage.ui.unn_news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iotait.schoolapp.R;
import com.iotait.schoolapp.databinding.UnnNewsFragmentBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.FragmentHelper;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.ui.homepage.ui.detailsnews.DetailsNewsWebView;
import com.iotait.schoolapp.ui.homepage.ui.home.model.Datum;
import com.iotait.schoolapp.ui.homepage.ui.home.model.LatestNewsResponse;
import com.iotait.schoolapp.ui.homepage.ui.home.view.HomeView;
import com.iotait.schoolapp.ui.homepage.ui.unn_news.adapter.UnnNewsAdapter;

import java.util.ArrayList;
import java.util.List;

public class UnnNewsFragment extends Fragment implements HomeView {

    UnnNewsFragmentBinding unnNewsFragmentBinding;
    String mState = "HIDE_MENU";
    UnnNewsAdapter adapter;
    private UnnNewsViewModel mViewModel;
    private List<Datum> dataList;

    private int pagenumber = 1;
    private int pageLimit = 15;
    private int totalPage = 0;


    public static UnnNewsFragment newInstance() {
        return new UnnNewsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        unnNewsFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.unn_news_fragment, container, false);
        View root = unnNewsFragmentBinding.getRoot();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UnnNewsViewModel.class);
        // TODO: Use the ViewModel

        dataList = new ArrayList<>();
        unnNewsFragmentBinding.toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(unnNewsFragmentBinding.toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        unnNewsFragmentBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(unnNewsFragmentBinding.getRoot(), R.id.action_unnNews_to_nav_home, bundle);
            }
        });

        adapter = new UnnNewsAdapter(getContext(), dataList, UnnNewsFragment.this);
        adapter.setHasStableIds(true);
        unnNewsFragmentBinding.latestnewsRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        unnNewsFragmentBinding.latestnewsRecyclerview.setAdapter(adapter);

        mViewModel.setUnnNews(getContext(), pagenumber, pageLimit, UnnNewsFragment.this);
        mViewModel.getUnnNews().observe(getActivity(), new Observer<LatestNewsResponse>() {
            @Override
            public void onChanged(LatestNewsResponse latestNewsResponse) {

                pagenumber = latestNewsResponse.getNextPage();
                totalPage = latestNewsResponse.getTotalPage();
                List<Datum> dataList = latestNewsResponse.getData();
                adapter.addData(dataList);
            }
        });

        unnNewsFragmentBinding.latestnewsRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItem = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                boolean endHasbeenReched = lastVisibleItem + 1 >= totalItem;
                if (totalItem > 0 && endHasbeenReched) {
                    if (pagenumber != -1 && pagenumber <= totalPage) {
                        mViewModel.setUnnNews(getContext(), pagenumber, pageLimit, UnnNewsFragment.this);
                    } else {
                        new CustomMessage(getActivity(), "No item available");
                    }
                }
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onNewsLoading() {
        unnNewsFragmentBinding.spinKit.setVisibility(View.VISIBLE);
    }


    @Override
    public void onNewsLoadFinish() {
        unnNewsFragmentBinding.spinKit.setVisibility(View.GONE);
    }

    @Override
    public void onInternetError() {
        new CustomMessage(getActivity(), getContext().getResources().getString(R.string.internet_error));
    }

    @Override
    public void onDataLoadError() {
        new CustomMessage(getActivity(), getContext().getResources().getString(R.string.server_error_messae));
    }

    @Override
    public void onNewsClick(Datum datum) {
        Bundle bundle = new Bundle();
        bundle.putString("link", datum.getGuid());
        UIHelper.changeActivty(getContext(), DetailsNewsWebView.class, bundle);
    }

}
