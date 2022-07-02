package com.example.smartrestaurant.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.smartrestaurant.AppClass;
import com.example.smartrestaurant.R;
import com.example.smartrestaurant.ui.activity.Authentication;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Util {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static void showKeyboard(Activity context) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).
                toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
    public static void changeTypeFace(Context context, TextView textView, String textName) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), textName);
        textView.setTypeface(typeface);
    }

    public static String getUniqueId(){
        return "O-"+ ((int) (System.currentTimeMillis() & 0xff));
    }
    public static class GsonUtils {

        public static <T> JSONObject toJSON(T obj) throws JSONException {
            Gson gson = new Gson();
            return new JSONObject(gson.toJson(obj));
        }

        public static <T> T fromJSON(String json, Class<T> classOfT) {
            Gson gson = new Gson();
            return gson.fromJson(json, classOfT);
        }

        public static <T> T fromJSON(JSONObject json, Class<T> classOfT) {
            Gson gson = new Gson();
            return gson.fromJson(json.toString(), classOfT);
        }
    }

    public static class ProgressDialog {
        static Dialog progressDialog;
        public static void show(Context context){
            try {
                progressDialog = new Dialog(context);
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressDialog.setContentView(R.layout.custom_dialog_progress);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.setCancelable(false);
                if(progressDialog != null)
                    progressDialog.show();
            } catch (Exception e){e.printStackTrace();}
        }
        public static void dismiss(){
            if(progressDialog != null) progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public static void showAlreadyLoginDialog(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Session Expired");
        builder.setMessage("Your session has been expired, you must login to active.");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPrefMgr.clearPrefs();
                context.startActivity(new Intent(context, Authentication.class));
                ((AppCompatActivity)context).finish();
            }
        }).setNegativeButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }
}
