package com.iotait.schoolapp.ui.homepage.ui.question.details.models;

public class QuestionItem {
    String answer;
    String explaination;
    String optionA="";
    String optionB="";
    String optionC="";
    String optionD="";
    boolean prepareExam=false;
    String question="";
    String scenario="";
    boolean studyQuestion=false;
    String subject="";
    String year="";
    String questionId="";

    String explain_image="";
    String optiona_image="";
    String optionb_image="";
    String optionc_image="";
    String optiond_image="";
    String question_image="";
    String scenario_image="";

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getExplaination() {
        return explaination;
    }

    public void setExplaination(String explaination) {
        this.explaination = explaination;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public boolean isPrepareExam() {
        return prepareExam;
    }

    public void setPrepareExam(boolean prepareExam) {
        this.prepareExam = prepareExam;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public boolean isStudyQuestion() {
        return studyQuestion;
    }

    public void setStudyQuestion(boolean studyQuestion) {
        this.studyQuestion = studyQuestion;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getExplain_image() {
        return explain_image;
    }

    public void setExplain_image(String explain_image) {
        this.explain_image = explain_image;
    }

    public String getOptiona_image() {
        return optiona_image;
    }

    public void setOptiona_image(String optiona_image) {
        this.optiona_image = optiona_image;
    }

    public String getOptionb_image() {
        return optionb_image;
    }

    public void setOptionb_image(String optionb_image) {
        this.optionb_image = optionb_image;
    }

    public String getOptionc_image() {
        return optionc_image;
    }

    public void setOptionc_image(String optionc_image) {
        this.optionc_image = optionc_image;
    }

    public String getOptiond_image() {
        return optiond_image;
    }

    public void setOptiond_image(String optiond_image) {
        this.optiond_image = optiond_image;
    }

    public String getQuestion_image() {
        return question_image;
    }

    public void setQuestion_image(String question_image) {
        this.question_image = question_image;
    }

    public String getScenario_image() {
        return scenario_image;
    }

    public void setScenario_image(String scenario_image) {
        this.scenario_image = scenario_image;
    }
}
