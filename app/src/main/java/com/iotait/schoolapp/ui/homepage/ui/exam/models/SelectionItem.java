package com.iotait.schoolapp.ui.homepage.ui.exam.models;

public class SelectionItem {
    int id;
    boolean isSelect;
    String subject;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
