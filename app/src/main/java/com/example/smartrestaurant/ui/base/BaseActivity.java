package com.example.smartrestaurant.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.smartrestaurant.util.AppLog;
import com.example.smartrestaurant.util.Util;

/**
 * Created by Dell 5521 on 12/26/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected Activity context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
    }

    protected void setupComponents(){
        initializeViews();
        initializeListeners();
    }

    protected void showInternetConnectionMsg(){
        AppLog.toast("Please check your internet connection.");
    }

    protected void showDialog(){
        Util.ProgressDialog.show(context);
    }

    protected void popStack(){
        getSupportFragmentManager().popBackStack();
    }

    protected void popAllStack(){
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    protected abstract void initializeViews();
    protected abstract void initializeListeners();
}
