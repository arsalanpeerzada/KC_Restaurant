package com.example.smartrestaurant.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.smartrestaurant.AppClass;

/**
 * Created by Dell 5521 on 10/6/2017.
 */

public class InternetConnection {
    public static boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) AppClass.getCtx().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm != null ? cm.getActiveNetworkInfo() : null;
        return net != null && net.isAvailable() && net.isConnected();
    }
    public static boolean isDataPlanEnabled(){
        ConnectivityManager cm = (ConnectivityManager) AppClass.getCtx().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm != null ? cm.getActiveNetworkInfo() : null;
        return net != null && net.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    public static boolean isWifiEnabled(){
        ConnectivityManager cm = (ConnectivityManager) AppClass.getCtx().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm != null ? cm.getActiveNetworkInfo() : null;
        return net != null && net.getType() == ConnectivityManager.TYPE_WIFI;
    }


}
