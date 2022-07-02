package com.example.smartrestaurant.network;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.smartrestaurant.AppClass;
import com.example.smartrestaurant.config.Constant;
import com.example.smartrestaurant.model.network_error_model._400ErrorModel;
import com.example.smartrestaurant.model.network_error_model._401ErrorModel;
import com.example.smartrestaurant.ui.activity.ErrorMsgActivity;
import com.example.smartrestaurant.util.AppLog;
import com.example.smartrestaurant.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dell 5521 on 11/6/2017.
 */

public class NetworkHandler {
    private static Map<String,String> getHeader(){
        Map<String, String> map = new HashMap<>();
//        map.put("Accept","application/json");
        map.put("session_id", AppClass.getAuthToken()/*"91c0504e79f004aadc800ebfe5e4f588"*/);
        return map;
    }

    static void postRequest(String url, Map<String, String> bodyMap, final Callback callback){
        NetworkCall.post(url, getHeader(), bodyMap, new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                AppLog.d(""+response);
                handleOnResponse(call, response, callback);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                AppLog.d(""+call);
                handleOnFailResponse(call,t,callback);
            }
        });
    }

    static void getRequest(String url, Map<String, String> queryMap, final Callback callback){
        NetworkCall.get(url, getHeader(), queryMap, new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,@NonNull Response<ResponseBody> response) {
                AppLog.d(""+response);
                handleOnResponse(call, response, callback);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call,@NonNull Throwable t) {
                AppLog.d(""+call);
                handleOnFailResponse(call,t,callback);
            }
        });
    }

    static void postMultipartRequest(String url, Map<String, String> bodyMap, Map<String, File> fileMap, String fileType,
                                     @Nullable ProgressRequestBody.UploadCallback uploadCallback, final Callback callback){

        //if fileMap is null, hit simple post request otherwise Multipart
        if(fileMap == null){
            postRequest(url,bodyMap,callback);
        } else {
            NetworkCall.post(url, getHeader(), bodyMap, fileMap, fileType, uploadCallback, new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    AppLog.d(""+response);
                    handleOnResponse(call, response, callback);
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    AppLog.d(""+call);
                    handleOnFailResponse(call,t,callback);
                }
            });
        }
    }

    private static void handleOnResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response, Callback callback) {
        int code = response.code();
        switch (code){
            case 200:
                callback.onResponse(call,response);
                break;
            case 400:
                handle400Error(response);
                break;
            case 401:
                handle401Error(response, callback);
                break;
            case 404:
                handle404Error(response);
                break;
            case 422:
//                handle422Error(response);
                callback.onResponse(null,response);
                break;
            case 500:
                handle500Error(response);
                break;
            default:
                handleUnknownError(response);
                break;
        }
    }

    private static void handleOnFailResponse(Call call, Throwable t, Callback callback){
        Util.ProgressDialog.dismiss();
        callback.onFailure(call, t);
//        AppLog.toast("Request failed, please check your internect connection.");
    }

    //Bad Request error
    private static void handle400Error(Response response){
        Util.ProgressDialog.dismiss();
        try {
            startAct(response);
            _400ErrorModel errorModel = Util.GsonUtils.fromJSON(response.errorBody().string(), _400ErrorModel.class);
            String s = errorModel.getMessage() != null ? errorModel.getMessage() : errorModel.getError();
            AppLog.toast(s);
        } catch (Exception e) {
            AppLog.toast("Unreachable 400, Please try again.");
            e.printStackTrace();
        }
    }

    //Unauthorized error
    private static void handle401Error(Response response, Callback callback){
        Util.ProgressDialog.dismiss();
        try {
            _401ErrorModel errorModel = Util.GsonUtils.fromJSON(response.errorBody().string(), _401ErrorModel.class);
            String s = errorModel.getMessage() != null ? errorModel.getMessage() : errorModel.getError();
            if(s.contains("User is already") || s.contains("Session not"))
                callback.onFailure(null,null);
            else
                AppLog.toast(s);

            startAct(response);
        } catch (Exception e) {
            AppLog.toast("Unreachable 401, Please try again.");
            e.printStackTrace();
        }
    }

    //Page not found error
    private static void handle404Error(Response response){
        Util.ProgressDialog.dismiss();
        AppLog.toast("404 Error. Page not found");
    }

    //Request parameter error
    private static void handle422Error(Response response){
        Util.ProgressDialog.dismiss();
        try {
            startAct(response);
            JSONObject errorObject = new JSONObject(response.errorBody().string());
            Iterator<String> iterator = errorObject.keys();
            while (iterator.hasNext()){
                String key = iterator.next();
                if(key.equalsIgnoreCase("error_message")){
                    String errorMsg = errorObject.getString(key);
                    AppLog.toast(errorMsg);
                    break;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            AppLog.toast("Unreachable 422 error");
        }
    }

    //internal server error.. returning html page
    private static void handle500Error(Response response){
        Util.ProgressDialog.dismiss();
        AppLog.toast("Server Error.");
        startAct(response);
    }

    private static void handleUnknownError(Response response){
        Util.ProgressDialog.dismiss();
        AppLog.toast("Unknown Error.");
        startAct(response);
    }

    private static void startAct(Response response){
        /*try {
            Intent i = new Intent(Constant.activity, ErrorMsgActivity.class);
            i.putExtra("Ex",response.errorBody().string());
            Constant.activity.startActivity(i);
        } catch (Exception e){
            e.printStackTrace();
        }*/
    }
}
