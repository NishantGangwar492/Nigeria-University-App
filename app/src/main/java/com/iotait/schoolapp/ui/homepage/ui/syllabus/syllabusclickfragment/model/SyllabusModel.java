package com.iotait.schoolapp.ui.homepage.ui.syllabus.syllabusclickfragment.model;

public class SyllabusModel {

    String Subject;
    String Syllabus;


    public SyllabusModel() {
    }

    public String getSubject() {
        return Subject;
    }

    public SyllabusModel(String subject, String syllabus) {
        Subject = subject;
        Syllabus = syllabus;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getSyllabus() {
        return Syllabus;
    }

    public void setSyllabus(String syllabus) {
        Syllabus = syllabus;
    }
}
