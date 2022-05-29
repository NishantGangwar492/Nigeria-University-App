package com.iotait.schoolapp.ui.homepage.ui.leaderboard.model;

import java.util.Date;
import java.util.List;

public class LeaderBoardModel implements Comparable<LeaderBoardModel> {
    Date TimeTake;
    String date;
    double score;
    String uid;

    List<SubjectListModel> subjectList;

    public LeaderBoardModel() {
    }

    public LeaderBoardModel(Date TimeTake, String date, double score, String uid, List<SubjectListModel> subjectList) {
        this.TimeTake = TimeTake;
        this.date = date;
        this.score = score;
        this.uid = uid;
        this.subjectList = subjectList;
    }


    public Date getTimeTake() {
        return TimeTake;
    }

    public void setTimeTake(Date timeTake) {
        TimeTake = timeTake;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<SubjectListModel> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<SubjectListModel> subjectList) {
        this.subjectList = subjectList;
    }

    @Override
    public int compareTo(LeaderBoardModel o) {
        if (score < o.getScore()) {
            return 1;
        } else if (score > o.getScore()) {
            return -1;
        }
        if (score == o.getScore()) {
            if (TimeTake.getTime() > o.TimeTake.getTime()) {
                return 1;
            } else if (TimeTake.getTime() < o.TimeTake.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return this.uid;
    }
}
