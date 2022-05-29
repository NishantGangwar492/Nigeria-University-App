package com.iotait.schoolapp.ui.homepage.ui.home.view;

import com.iotait.schoolapp.ui.homepage.ui.home.model.Datum;

public interface HomeView {
    void onNewsLoading();
    void onNewsLoadFinish();
    void onInternetError();
    void onDataLoadError();
    void onNewsClick(Datum datum);
}
