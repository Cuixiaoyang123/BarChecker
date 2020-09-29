package com.dreamlost.barcodechecker;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by DreamLost on 2020/9/26 at 9:36
 * Description:
 */
public class Utils {
    private static final String TAG = "Utils";

    public static String getCurrentDay() {
        Calendar c = Calendar.getInstance();//
        int mYear = c.get(Calendar.YEAR); // 获取当前年份
        int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        int mWay = c.get(Calendar.DAY_OF_WEEK);// 获取当前日期的星期
        int mHour = c.get(Calendar.HOUR_OF_DAY);//时
        int mMinute = c.get(Calendar.MINUTE);//分
        return mYear + "-" + mMonth + "-" + mDay;
    }

    public static String getFileFromSD(String path) {
        StringBuilder result = new StringBuilder();

        try {
            FileInputStream f = new FileInputStream(path);
            BufferedReader bis = new BufferedReader(new InputStreamReader(f));
            String line = "";
            while ((line = bis.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();

    }

    public static String getCurrentSecond() {

        Calendar c = Calendar.getInstance();//
        int mYear = c.get(Calendar.YEAR); // 获取当前年份
        int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        int mWay = c.get(Calendar.DAY_OF_WEEK);// 获取当前日期的星期
        int mHour = c.get(Calendar.HOUR_OF_DAY);//时
        int mMinute = c.get(Calendar.MINUTE);//分
        int mSecond = c.get(Calendar.SECOND);
        return mYear + "-" + mMonth + "-" + mDay + " " + mHour + ":" + mMinute + ":" + mSecond;
    }

    public static String[] getAllSubFiles(String path) {
        File f = new File(path);
        if (!f.exists()) {
            return null;
        }
        File[] files = f.listFiles();
        if (files.length != 0) {
            String[] fileNames = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                fileNames[i] = files[i].getName();
            }
            return fileNames;
        }
        return null;
    }

    private static Long convertTimeToLong(String time) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public static String getRecentFile(String path) {
        File f = new File(path);
        File[] files = f.listFiles();
        String recentFile = "";
//        String currentDay = getCurrentDay();
        int temp = -1;
        Long currentDay = convertTimeToLong(getCurrentDay());
        for (int i = 0; i < files.length; i++) {
            Long subFileDay = convertTimeToLong(files[i].getName());
            if (subFileDay>=currentDay) {
                recentFile = files[i].getName();
                temp = i;
                break;
            }
        }

        if (temp != -1) {
            for (int i = temp; i < files.length; i++) {
                Long recentFileDay = convertTimeToLong(recentFile);
                Long subFileDay = convertTimeToLong(files[i].getName());
                if (subFileDay>=currentDay && subFileDay<recentFileDay) {
                    recentFile = files[i].getName();
                }
            }
        }
        Log.i(TAG, "getRecentFile: " + recentFile);
        return recentFile;
    }


}
