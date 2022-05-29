package com.iotait.schoolapp.common;


import com.iotait.schoolapp.ui.homepage.ui.exam.fullexam.models.AnswerModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Common {

    //for Common class here all the data save and fetch from another class easily
    public static String c_date = new SimpleDateFormat("E, dd MMM yyyy hh:mm a", Locale.getDefault()).format(new Date());
    public static List<AnswerModel> answerList=new ArrayList<>();
    public static boolean notification_state = false;
    public static String User_Premium_Type = "";
    public static String interstitalType,baneerType,reawerType="";
}

