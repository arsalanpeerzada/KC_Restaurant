package com.example.smartrestaurant.network;

import android.support.annotation.Nullable;


import com.example.smartrestaurant.util.AppLog;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.HeaderMap;

/**
 * Created by Dell 5521 on 9/28/2017.
 */

public class NetworkCall {
    static void post(String endpoint,
                     @Nullable Map<String, String> headerMap,
                     @Nullable Map<String, String> bodyMap,
                     Callback<ResponseBody> callback){

        AppLog.d("post: > "+endpoint);
        AppLog.d("headerMap > "+headerMap);
        AppLog.d("bodyMap > "+bodyMap);

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        if (bodyMap != null) {
            for (Map.Entry<String, String> entry : bodyMap.entrySet()) {
                requestBodyMap.put(entry.getKey(), RequestBody.create(MediaType.parse("text/plain"), entry.getValue()));
            }
        }

        Call<ResponseBody> call;

        if (headerMap == null && bodyMap == null) {
            call = ApiClient.getApiInterface().post(endpoint);
        } else if (bodyMap == null){
            call = ApiClient.getApiInterface().post(endpoint, headerMap);
        } else {
            if (headerMap == null) {
                headerMap = new HashMap<>();
            }
            call = ApiClient.getApiInterface().post(endpoint, headerMap, requestBodyMap);
        }
        call.enqueue(callback);
    }

    static void post(String endpoint,
                     Map<String, String> headerMap,
                     Map<String, String> bodyMap,
                     Map<String, File> fileMap,
                     String fileType,//image/audio/video
                     @Nullable ProgressRequestBody.UploadCallback fileUploadCallback,
                     Callback<ResponseBody> callback){

        AppLog.d("post: " + endpoint);
        AppLog.d("headerMap: " + headerMap);
        AppLog.d("bodyMap: " + bodyMap);
        AppLog.d("fileMap: " + fileMap);
        AppLog.d("fileType: " + fileType);

        //create requestBody of bodyMap
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        for (Map.Entry<String, String> entry : bodyMap.entrySet()) {
            requestBodyMap.put(entry.getKey(), RequestBody.create(MediaType.parse("text/plain"),  entry.getValue()));
        }

        MediaType mediaType = MediaType.parse(fileType + "/*");
        File file = new File("");
        String fileKey = "";
        for(Map.Entry<String, File> fileEntry : fileMap.entrySet()){
            fileKey = fileEntry.getKey();
            file = fileEntry.getValue();
        }

        RequestBody requestFile;
        if (fileUploadCallback == null) {
            requestFile = RequestBody.create(mediaType, file);
        } else {
            requestFile = new ProgressRequestBody(mediaType, file, fileUploadCallback);
        }

        MultipartBody.Part filePart = MultipartBody.Part.createFormData(fileKey, file.getName(), requestFile);

        if (headerMap == null) {
            headerMap = new HashMap<>();
        }

        Call<ResponseBody> call = ApiClient.getApiInterface().post(endpoint, headerMap, requestBodyMap, filePart);

        call.enqueue(callback);

    }


    static void get(String endpoint,
                    @Nullable Map<String, String> headerMap,
                    @Nullable Map<String, String> queryMap,
                    Callback<ResponseBody> callback){

        Call<ResponseBody> call;

        AppLog.d("get: " + endpoint);
        AppLog.d("headerMap: " + headerMap);
        AppLog.d("queryMap: " + queryMap);

        if (headerMap == null && queryMap == null) {
            call = ApiClient.getApiInterface().get(endpoint);
        } else if (queryMap == null){
            call = ApiClient.getApiInterface().get(endpoint, headerMap);
        } else {
            call = ApiClient.getApiInterface().get(endpoint, headerMap, queryMap);
        }

        call.enqueue(callback);
    }

}
