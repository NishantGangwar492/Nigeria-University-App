package com.iotait.schoolapp.ui.homepage.ui.unn_news;

import android.content.Context;
import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.iotait.schoolapp.data.network.RetrofitClient;
import com.iotait.schoolapp.helper.NetworkHelper;
import com.iotait.schoolapp.ui.homepage.ui.home.model.LatestNewsResponse;
import com.iotait.schoolapp.ui.homepage.ui.home.view.HomeView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnnNewsViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    MutableLiveData<LatestNewsResponse> unnNewsMutableLiveData;

    public UnnNewsViewModel() {
        this.unnNewsMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<LatestNewsResponse> getUnnNews() {
        return unnNewsMutableLiveData;
    }

    public void setUnnNews(Context context, int page, int limit, final HomeView homeView) {
        homeView.onNewsLoading();
        if (NetworkHelper.hasNetworAccess(context)) {
            RetrofitClient.getInstance().getApi().getUNNNews(page, limit).enqueue(new Callback<LatestNewsResponse>() {
                @Override
                public void onResponse(Call<LatestNewsResponse> call, Response<LatestNewsResponse> response) {
                    homeView.onNewsLoadFinish();
                    if (response.isSuccessful()) {
                        unnNewsMutableLiveData.setValue(response.body());
                    } else {
                        homeView.onDataLoadError();
                    }
                }

                @Override
                public void onFailure(Call<LatestNewsResponse> call, Throwable t) {
                    homeView.onNewsLoadFinish();
                }
            });
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    homeView.onNewsLoadFinish();
                    homeView.onInternetError();
                }
            }, 100);
        }
    }
}
