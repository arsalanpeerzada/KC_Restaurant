package com.example.smartrestaurant.util;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartrestaurant.AppClass;
import com.example.smartrestaurant.R;

/**
 * Created by Muhammad Faizan Khan on 20/06/2016.
 */
public class AppLog {

    private static final String CONSTANT_TAG = "APP:";


    public static int d(String msg) {
        return Log.d(CONSTANT_TAG, msg);
    }

    public static int d(String tag, String msg) {
        tag = CONSTANT_TAG + tag;
        return Log.d(tag, msg);
    }

    public static int d(String tag, String msg, Throwable tr) {
        tag = CONSTANT_TAG + tag;
        return Log.d(tag, msg, tr);
    }

    public static void toast(String msg) {
        Toast toast = Toast.makeText(AppClass.getCtx(),msg,Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void snakeToast(View view, String msg) {
        Snackbar.make(view,msg,Toast.LENGTH_SHORT).show();
    }
}
