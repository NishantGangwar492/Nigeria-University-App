package com.iotait.schoolapp.utils;

import org.apache.commons.io.FilenameUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class PublicFunctions {
    public static String LTU() {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getDefault());
        String date = dateFormat.format(new Date());
        Date dt = new Date();
        try {
            dt = dateFormat.parse(date);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            dateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
            return dateFormat.format(dt);
        } catch (ParseException e) {
            return date;
        }
    }

    public static boolean isVideo(String url) {

        switch (FilenameUtils.getExtension(url).toLowerCase()) {

            case "3gp":
                return true;
            case "mp4":
                return true;
            case "mov":
                return true;
            case "m4a":
                return true;
            case "webm":
                return true;
            case "flv":
                return true;
            case "avi":
                return true;
            case "wmv":
                return true;
            case "mpg":
                return true;
            case "mpeg":
                return true;
            case "m4v":
                return true;
            default:
                return false;
        }
    }

    public static boolean isImage(String url) {

        switch (FilenameUtils.getExtension(url).toLowerCase()) {

            case "jpg":
                return true;
            case "JPEG":
                return true;
            case "png":
                return true;
            case "gif":
                return true;
            case "tif":
                return true;
            case "jpeg":
                return true;
            default:
                return false;
        }
    }

    public String getTimeandDate(String date) {

        String withdate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
        try {
            Date dat = dateFormat.parse(UTL(date));
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(dat);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            String time = "AM";
            if (hour - 12 > 0) {
                time = "PM";
                hour -= 12;
                if (hour == 0) {
                    hour = 12;
                }
            }
            dateFormat.applyPattern("dd MMM yyyy");
            withdate = dateFormat.format(dat);
            return String.valueOf(hour) + ":" + String.valueOf(min) + " " + time + "  " + withdate;

        } catch (ParseException e) {
            return getTime(date);
        }
    }

    public String getTime(String date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
        Date dat = null;
        Date current = new Date();
        try {
            dat = dateFormat.parse(UTL(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dat != null && current != null) {

            long seconds = (long) (current.getTime() - dat.getTime()) / 1000;
            String elapsed = "";

            if (seconds<=0){
                elapsed = "1s";
            }

            if (seconds > 0 && seconds <60) {
                elapsed = seconds + "s";
            } else if (seconds < 60 * 60) {
                long minutes = (seconds / 60);
                String minText = "m";
                elapsed = String.valueOf(minutes) + " " + minText;
            } else if (seconds < 24 * 60 * 60) {
                long hours = seconds / (60 * 60);
                String hourText = "hr";
                elapsed = String.valueOf(hours) + " " + hourText;
            } else if (seconds < ((24 * 60 * 60) * 2)) {
                elapsed = "1d";
            } else if (seconds < ((24 * 60 * 60) * 3)) {
                elapsed = "2d";
            } else if (seconds < ((24 * 60 * 60) * 4)) {
                elapsed = "3d";
            } else if (seconds < ((24 * 60 * 60) * 5)) {
                elapsed = "4d";
            } else if (seconds < ((24 * 60 * 60) * 6)) {
                elapsed = "5d";
            } else if (seconds < ((24 * 60 * 60) * 7)) {
                elapsed = "6d";
            } else if (seconds < ((24 * 60 * 60) * 8)) {
                elapsed = "7d";
            } else {
                dateFormat.applyPattern("dd MMM yy");
                elapsed = dateFormat.format(dat);
            }

            return elapsed;
        }
        dateFormat.applyPattern("dd MMM yy");
        return dateFormat.format(dat);
    }

    public String UTL(String date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date dt = new Date();
        String comingDate = date == null ? dateFormat.format(dt) : date;
        try {
            dt = dateFormat.parse(comingDate);
            dateFormat.setTimeZone(TimeZone.getDefault());
            dateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
            return dateFormat.format(dt);
        } catch (ParseException e) {
            return date;
        }
    }
}
