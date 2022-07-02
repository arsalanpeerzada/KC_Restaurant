package com.example.smartrestaurant.util;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.example.smartrestaurant.AppClass;

/**
 * Created by Dell 5521 on 8/8/2017.
 */

public class PermissionsUtil {
    public static boolean location(){
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                (ActivityCompat.checkSelfPermission(AppClass.getCtx(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean isLollipopOrHigher(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isStorage(){
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                (ActivityCompat.checkSelfPermission(AppClass.getCtx(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }
}
