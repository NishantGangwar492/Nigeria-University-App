package com.iotait.schoolapp.ui.homepage.ui.home.model;

public class OverViewModel {

    String overviewName;
    Integer overviewImage;

    public OverViewModel() {
    }

    public OverViewModel(String overviewName, Integer overviewImage) {
        this.overviewName = overviewName;
        this.overviewImage = overviewImage;
    }

    public int getOverviewImage() {
        return overviewImage;
    }

    public void setOverviewImage(int overviewImage) {
        this.overviewImage = overviewImage;
    }

    public String getOverviewName() {
        return overviewName;
    }

    public void setOverviewName(String overviewName) {
        this.overviewName = overviewName;
    }
}
