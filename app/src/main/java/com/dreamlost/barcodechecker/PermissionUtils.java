package com.dreamlost.barcodechecker;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by DreamLost on 2020/9/22 at 15:19
 * Description:
 */
public class PermissionUtils {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final int REQUEST_CAMERA = 2;
    private static String[] PERMISSIONS_CAMERA = {
            "android.permission.CAMERA",
            "android.permission.FLASHLIGHT"
    };
    public static void verifyCameraPermissions(Activity activity) {

        try {
            //检测是否有相机的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.CAMERA");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有相机的权限，去申请相机的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_CAMERA,REQUEST_CAMERA);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void requestLocationPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Toast.makeText(activity, "您已经拒绝过一次了", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }
}
