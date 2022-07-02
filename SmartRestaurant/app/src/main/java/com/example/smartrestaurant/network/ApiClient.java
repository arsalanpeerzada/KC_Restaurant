package com.example.smartrestaurant.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.smartrestaurant.config.API.BASE_URL;

/**
 * Created by Dell 5521 on 11/6/2017.
 */

class ApiClient {
    private static Retrofit retrofit = null;
    private static ApiInterface apiInterface = null;

    private static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient.Builder()
                            .readTimeout(15, TimeUnit.SECONDS)
                            .writeTimeout(15, TimeUnit.SECONDS)
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .build())

                    .build();
        }
        return retrofit;
    }

    static ApiInterface getApiInterface() {
        return apiInterface == null ? apiInterface = getClient().create(ApiInterface.class) : apiInterface;
    }
}
