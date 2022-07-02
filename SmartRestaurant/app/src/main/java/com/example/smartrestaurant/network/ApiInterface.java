package com.example.smartrestaurant.network;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by Dell 5521 on 11/6/2017.
 */

public interface ApiInterface {

    @GET
    Call<ResponseBody> get(@Url String endpoint);

    @GET
    Call<ResponseBody> get(@Url String endpoint,
                           @HeaderMap Map<String, String> headerMap);

    @GET
    Call<ResponseBody> get(@Url String endpoint,
                           @HeaderMap Map<String, String> headers,
                           @QueryMap Map<String, String> query);

    @POST
    Call<ResponseBody> post(@Url String endpoint);

    @POST
    Call<ResponseBody> post(@Url String endpoint,
                            @HeaderMap Map<String, String> headerMap);

    @Multipart
    @POST
    Call<ResponseBody> post(@Url String endpoint,
                            @HeaderMap Map<String, String> headerMap,
                            @PartMap Map<String, RequestBody> bodyMap);

    @Multipart
    @POST
    Call<ResponseBody> post(@Url String endpoint,
                            @HeaderMap Map<String, String> headerMap,
                            @PartMap Map<String, RequestBody> bodyMap,
                            @Part MultipartBody.Part file);

    @Multipart
    @POST
    Call<ResponseBody> post(@Url String endpoint,
                            @HeaderMap Map<String, String> headerMap,
                            @PartMap Map<String, RequestBody> bodyMap,
                            @Part List<MultipartBody.Part> files);

    @DELETE
    Call<ResponseBody> delete(@Url String endpoint,
                              @HeaderMap Map<String, String> headerMap);

}
