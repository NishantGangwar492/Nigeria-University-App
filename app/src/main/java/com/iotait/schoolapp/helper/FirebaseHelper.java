package com.iotait.schoolapp.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference Users;
    private DatabaseReference Articles;
    private DatabaseReference Sliders;
    private DatabaseReference Syllabus;
    private DatabaseReference Years;
    private DatabaseReference Subject;
    private DatabaseReference Questions;
    private DatabaseReference ReportedQuestion;
    private DatabaseReference FullExam;
    private DatabaseReference CustomExam;
    private DatabaseReference Payment;
    private DatabaseReference Tokens;
    private DatabaseReference WeeklyContest;
    private DatabaseReference WeeklycontestQuestions;
    private DatabaseReference WeeklyQuestionList;
    private DatabaseReference weeklyContestLiveStatus;
    private DatabaseReference weeklyContestParticipateStatus;
    private DatabaseReference adState;
    private DatabaseReference unnFaq;

    public FirebaseHelper() {

        this.firebaseAuth = FirebaseAuth.getInstance();
        this.Users = FirebaseDatabase.getInstance().getReference("Users");

        this.Articles = FirebaseDatabase.getInstance().getReference("Articles");

        this.Sliders = FirebaseDatabase.getInstance().getReference("Sliders");
        this.Syllabus = FirebaseDatabase.getInstance().getReference("Syllabus");
        this.Years = FirebaseDatabase.getInstance().getReference("Years");

        this.Subject = FirebaseDatabase.getInstance().getReference("Subject");

        this.Questions = FirebaseDatabase.getInstance().getReference("Questions");
        this.ReportedQuestion = FirebaseDatabase.getInstance().getReference("ReportedQuestions");
        this.FullExam = FirebaseDatabase.getInstance().getReference("FullExamResult");
        this.CustomExam = FirebaseDatabase.getInstance().getReference("CustomExamResult");
        this.Payment = FirebaseDatabase.getInstance().getReference("Payment");
        this.Tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        this.WeeklyContest = FirebaseDatabase.getInstance().getReference("WeeklyContestResult");
        this.WeeklycontestQuestions = FirebaseDatabase.getInstance().getReference("WeeklycontestQuestions");
        this.WeeklyQuestionList = FirebaseDatabase.getInstance().getReference("WeeklyQuestionList");

        this.weeklyContestLiveStatus = FirebaseDatabase.getInstance().getReference("weeklyContestLiveStatus");
        this.weeklyContestParticipateStatus = FirebaseDatabase.getInstance().getReference("weeklyContestParticipateStatus");
        this.adState = FirebaseDatabase.getInstance().getReference("AdState");
        this.unnFaq = FirebaseDatabase.getInstance().getReference("UnnFaq");


    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public DatabaseReference getUsers() {
        return Users;
    }


    public DatabaseReference getArticles() {
        return Articles;
    }

    public DatabaseReference getSliders() {
        return Sliders;
    }

    public DatabaseReference getSyllabus() {
        return Syllabus;
    }

    public DatabaseReference getYears() {
        return Years;
    }


    public DatabaseReference getSubject() {
        return Subject;
    }

    public DatabaseReference getQuestions() {
        return Questions;
    }

    public DatabaseReference getReportedQuestion() {
        return ReportedQuestion;
    }

    public DatabaseReference getFullExam() {
        return FullExam;
    }

    public DatabaseReference getCustomExam() {
        return CustomExam;
    }

    public DatabaseReference getPayment() {
        return Payment;
    }

    public DatabaseReference getTokens() {
        return Tokens;
    }

    public DatabaseReference getWeeklyContest() {
        return WeeklyContest;
    }

    public DatabaseReference getWeeklycontestQuestions() {
        return WeeklycontestQuestions;
    }


    public DatabaseReference getWeeklyQuestionList() {
        return WeeklyQuestionList;
    }

    public DatabaseReference getWeeklyContestLiveStatus() {
        return weeklyContestLiveStatus;
    }

    public DatabaseReference getWeeklyContestParticipateStatus() {
        return weeklyContestParticipateStatus;
    }

    public DatabaseReference getAdState() {
        return adState;
    }

    public DatabaseReference getUnnFaq(){return unnFaq;}
}
