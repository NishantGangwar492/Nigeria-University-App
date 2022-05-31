package com.iotait.schoolapp.ui.homepage.ui.leaderboard.model;

public class SubjectListModel {
    int id;
    String subject;
    boolean isSelect;

    public SubjectListModel() {
    }

    public SubjectListModel(int id, String subject, boolean isSelect) {
        this.id = id;
        this.subject = subject;
        this.isSelect = isSelect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
