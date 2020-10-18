package com.dreamlost.barcodechecker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
            if (subFileDay >= currentDay) {
                recentFile = files[i].getName();
                temp = i;
                break;
            }
        }

        if (temp != -1) {
            for (int i = temp; i < files.length; i++) {
                Long recentFileDay = convertTimeToLong(recentFile);
                Long subFileDay = convertTimeToLong(files[i].getName());
                if (subFileDay >= currentDay && subFileDay < recentFileDay) {
                    recentFile = files[i].getName();
                }
            }
        }
        Log.i(TAG, "getRecentFile: " + recentFile);
        return recentFile;
    }

    public static List<Double> getLocationInfo(Activity context) {
        String locationProvider;
        ArrayList<Double> list = new ArrayList<>();
//        private LocationManager locationManager;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            // 如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        }
//    else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
//       // 如果是Network
//       locationProvider = LocationManager.NETWORK_PROVIDER;
//    }
        else {
            Toast.makeText(context, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return null;
        }
        // 获取Location
        PermissionUtils.requestLocationPermission(context);
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return null ;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            // 不为空,显示地理位置经纬度
            double jd =location.getLongitude();
            double wd =location.getLatitude();
            Toast.makeText(context, "位置jd="+jd+",wd="+wd, Toast.LENGTH_SHORT).show();
            list.add(jd);
            list.add(wd);
            return list;
        } else {
            Toast.makeText(context, "GPS未定位到位置,请查看是否打开了GPS ", Toast.LENGTH_SHORT).show();
            return null;
//            System.out.println("GPS未定位到位置,请查看是否打开了GPS ？");
        }
        // 监视地理位置变化
//        locationManager.requestLocationUpdates(locationProvider, 2000, 1, locationListener);
    }

}
