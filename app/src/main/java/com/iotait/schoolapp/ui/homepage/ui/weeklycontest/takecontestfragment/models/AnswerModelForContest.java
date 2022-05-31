package com.iotait.schoolapp.ui.homepage.ui.weeklycontest.takecontestfragment.models;

public class AnswerModelForContest {
    private String questioid;
    private String your_answer;
    private String currect_answer;
    private int question_position;

    public String getQuestioid() {
        return questioid;
    }

    public void setQuestioid(String questioid) {
        this.questioid = questioid;
    }

    public String getYour_answer() {
        return your_answer;
    }

    public void setYour_answer(String your_answer) {
        this.your_answer = your_answer;
    }

    public String getCurrect_answer() {
        return currect_answer;
    }

    public void setCurrect_answer(String currect_answer) {
        this.currect_answer = currect_answer;
    }

    public int getQuestion_position() {
        return question_position;
    }

    public void setQuestion_position(int question_position) {
        this.question_position = question_position;
    }
}
