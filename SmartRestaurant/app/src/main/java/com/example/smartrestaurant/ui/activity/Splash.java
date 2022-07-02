package com.example.smartrestaurant.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.Window;
import android.view.WindowManager;

import com.example.smartrestaurant.AppClass;
import com.example.smartrestaurant.R;
import com.example.smartrestaurant.config.Constant;
import com.example.smartrestaurant.databinding.ActivitySplashBinding;
import com.example.smartrestaurant.listener.Callback;
import com.example.smartrestaurant.model.ProfileModel;
import com.example.smartrestaurant.network.InternetConnection;
import com.example.smartrestaurant.network.RequestHandler;
import com.example.smartrestaurant.ui.base.BaseActivity;
import com.example.smartrestaurant.util.AppLog;
import com.example.smartrestaurant.util.PermissionsUtil;
import com.example.smartrestaurant.util.SharedPrefMgr;
import com.example.smartrestaurant.util.Util;

/**
 * Created by Dell 5521 on 1/6/2018.
 */

public class Splash extends BaseActivity {

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        Constant.activity = this;
        if(PermissionsUtil.isStorage()){
            checkSession();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }

    }

    private void checkSession(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(InternetConnection.isOnline()){
                    if(AppClass.getAuthToken().equals("")){
                        Splash.this.finish();
                        startActivity(new Intent(Splash.this, Authentication.class));
                    } else {
                        getProfile();
                    }
                } else {
                    AppLog.toast("Please check your internet connection.");
                }
            }
        },2000);
    }

    private void getProfile(){
        RequestHandler.getProfile(new Callback<ProfileModel>() {
            @Override
            public void invoke(ProfileModel obj) {
                if(obj != null){
                    AppClass.setProfile(obj);
                    startActivity(new Intent(context, MainActivity.class));
                    Splash.this.finish();
                } else {
                    SharedPrefMgr.clearPrefs();
                    startActivity(new Intent(context, Authentication.class));
                    Splash.this.finish();
                }
            }

            @Override
            public void alreadyLogin() {
                Util.showAlreadyLoginDialog(context);
            }

            @Override
            public void _422(String msg) {
                AppLog.toast(msg);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 0:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    checkSession();
                } else {
                    AppLog.toast("Please approve this permission otherwise you will face problem while taking order.");
                    Splash.this.finish();
                }
                break;
        }
    }

    @Override
    protected void initializeViews() {

    }

    @Override
    protected void initializeListeners() {

    }
}
