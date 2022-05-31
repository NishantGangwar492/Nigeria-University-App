package com.iotait.schoolapp.ui.homepage.ui.home;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.iotait.schoolapp.data.network.RetrofitClient;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.NetworkHelper;
import com.iotait.schoolapp.ui.homepage.ui.home.model.LatestNewsResponse;
import com.iotait.schoolapp.ui.homepage.ui.home.view.HomeView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    MutableLiveData<LatestNewsResponse> latestNewsResponseMutableLiveData;

    public HomeViewModel() {
        this.latestNewsResponseMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<LatestNewsResponse> getLatestNewsResponseMutableLiveData() {
        return latestNewsResponseMutableLiveData;
    }

    public void setLatestNewsResponseMutableLiveData(Context context, int page, int limit, final HomeView homeView) {
        homeView.onNewsLoading();
        if (NetworkHelper.hasNetworAccess(context)){
            RetrofitClient.getInstance().getApi().getNews(page,limit).enqueue(new Callback<LatestNewsResponse>() {
                @Override
                public void onResponse(Call<LatestNewsResponse> call, Response<LatestNewsResponse> response) {
                    homeView.onNewsLoadFinish();
                    if (response.isSuccessful()){
                        latestNewsResponseMutableLiveData.setValue(response.body());
                    }
                    else {
                        homeView.onDataLoadError();
                    }
                }

                @Override
                public void onFailure(Call<LatestNewsResponse> call, Throwable t) {
                    homeView.onNewsLoadFinish();
                }
            });
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    homeView.onNewsLoadFinish();
                    homeView.onInternetError();
                }
            },100);
        }
    }
}