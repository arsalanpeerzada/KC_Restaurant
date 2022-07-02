package com.example.smartrestaurant.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.smartrestaurant.AppClass;

import org.json.JSONException;

import java.util.Set;

public class SharedPrefMgr {

    private static final String TAG = SharedPrefMgr.class.getSimpleName();

    private static SharedPreferences preferences;

    static {preferences = AppClass.getCtx().getSharedPreferences(SharedPreferencesConstants.SHARED_PREF_FILE, Context.MODE_PRIVATE);}

    private static SharedPrefMgr manager;

    public static SharedPrefMgr getInstance() {
        if (manager == null) {
            manager = new SharedPrefMgr();
        }
        return manager;
    }

    public static boolean clearPrefs() {
        return preferences.edit().clear().commit();
    }

    public void putBoolean(String field, boolean value) {
        preferences.edit().putBoolean(field, value).apply();
    }

    public boolean getBoolean(String field, boolean defaultValue) {
        return preferences.getBoolean(field, defaultValue);
    }

    public void putString(String field, String value) {
        preferences.edit().putString(field, value).apply();
    }

    public String getString(String field, String defaultValue) {
        return preferences.getString(field, defaultValue);
    }

    public void putStringSet(String field, Set<String> value) {
        preferences.edit().putStringSet(field, value).apply();
    }

    public Set<String> getStringSet(String field) {
        return preferences.getStringSet(field, null);
    }

    public void putInteger(String field, int value) {
        preferences.edit().putInt(field, value).apply();
    }

    public int getInteger(String field, int defaultValue) {
        return preferences.getInt(field, defaultValue);
    }

    public void putObject(String key, Object object) throws JSONException {
        String json = null;
        json = Util.GsonUtils.toJSON(object).toString();
        putString(key, json);
    }

    public <T> T getObject(String key, Class<T> classOfT) {
        try {
            String json = getString(key,"");
            return Util.GsonUtils.fromJSON(json, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final class SharedPreferencesConstants {
        static final String SHARED_PREF_FILE = "Smart_Pref_file";

        static final String IS_REGISTERED = "IS_REGISTERED";

        public static final String AUTH_TOKEN = "AUTH_TOKEN";
        static final String TOKEN_TYPE = "TOKEN_TYPE";
        static final String REFRESH_TOKEN = "REFRESH_TOKEN";
        static final String USER_DATA = "USER_DATA";

        public static final String UserRestID = "USER_REST_ID";

    }

}
