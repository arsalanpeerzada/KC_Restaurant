package com.example.smartrestaurant;

import android.app.Application;

import com.example.smartrestaurant.db_handlers.DbHandler;
import com.example.smartrestaurant.model.ProfileModel;
import com.example.smartrestaurant.util.SharedPrefMgr;

/**
 * Created by Dell 5521 on 12/26/2017.
 */

public class AppClass extends Application {
    private static AppClass ctx;
    private static ProfileModel profileModel;
    @Override
    public void onCreate() {
        super.onCreate();
        ctx = this;
        DbHandler.getInstance();
    }
    public static AppClass getCtx(){
        return ctx;
    }

    public static void setUserRestId(String userRestId){
        SharedPrefMgr.getInstance().putString(SharedPrefMgr.SharedPreferencesConstants.UserRestID,userRestId);
    }

    public static String getUserRestId(){
        return SharedPrefMgr.getInstance().getString(SharedPrefMgr.SharedPreferencesConstants.UserRestID,"");
    }


    public static void setAuthToken(String token){
        SharedPrefMgr.getInstance().putString(SharedPrefMgr.SharedPreferencesConstants.AUTH_TOKEN,token);
    }

    public static String getAuthToken(){
        return SharedPrefMgr.getInstance().getString(SharedPrefMgr.SharedPreferencesConstants.AUTH_TOKEN,"");
    }

    public static void setProfile(ProfileModel profile){
        profileModel = profile;
    }

    public static ProfileModel getProfile(){
        return profileModel;
    }


}
