package com.example.smartrestaurant.network;

import android.support.annotation.NonNull;

import android.text.PrecomputedText;
import android.widget.Toast;

import com.example.smartrestaurant.config.API;
import com.example.smartrestaurant.config.BodyParams;
import com.example.smartrestaurant.listener.Callback;
import com.example.smartrestaurant.model.AuthResponseModel;
import com.example.smartrestaurant.model.LocationModel;
import com.example.smartrestaurant.model.NotificationResponseModel;
import com.example.smartrestaurant.model.ProfileModel;
import com.example.smartrestaurant.model.kitchen.KitchenDataModel;
import com.example.smartrestaurant.model.kitchen.PlacedOrderGetResponse;
import com.example.smartrestaurant.model.kitchen.PlacedOrderModel;
import com.example.smartrestaurant.model.nfc.NFCDataModel;
import com.example.smartrestaurant.ui.activity.Authentication;
import com.example.smartrestaurant.util.AppLog;
import com.example.smartrestaurant.util.Util;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Dell 5521 on 11/6/2017.
 */



public class RequestHandler {

    //region ***** POST REQUEST ******



    //region Authentication
    public static void login(Map<String, String> bodyMap, final Callback<AuthResponseModel> callback) {
        NetworkHandler.postRequest(API.LOGIN, bodyMap, new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(call == null){
                        handle422Error(response,callback);
                    } else {
                        Util.ProgressDialog.dismiss();
                        AuthResponseModel model = Util.GsonUtils.fromJSON(response.body().string(), AuthResponseModel.class);
                        callback.invoke(model);
                    }
                } catch (Exception e) {
                    Util.ProgressDialog.dismiss();
                    e.printStackTrace();
                    showErrorMsg();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Util.ProgressDialog.dismiss();
//                if(call != null) //problem is requesting / internet connection problem
                    callback.invoke(null);
//                else //already login response from 401 handling
//                    callback.alreadyLogin();
            }
        });
    }

    public static void logoutEverywhere(Map<String, String> bodyMap, final Callback<String> callback) {
        NetworkHandler.postRequest(API.LOGOUT_EVERYWHERE, bodyMap, new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(call == null){
                        handle422Error(response,callback);
                    } else {
                        Util.ProgressDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String msg = jsonObject.getString("success_message");
                        callback.invoke(msg);
                    }
                } catch (Exception e) {
                    Util.ProgressDialog.dismiss();
                    e.printStackTrace();
                    showErrorMsg();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Util.ProgressDialog.dismiss();
                if(call != null) //problem is requesting / internet connection problem
                    callback.invoke(null);
                else //already login response from 401 handling
                    callback.alreadyLogin();
            }
        });
    }

    public static void logout(final Callback<String> callback) {
        NetworkHandler.postRequest(API.LOGOUT, null, new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Util.ProgressDialog.dismiss();
                    if(response.code() == 200)
                        callback.invoke("OK");
                    else
                        AppLog.toast("Problem in logging out, please try again");
                } catch (Exception e) {
                    Util.ProgressDialog.dismiss();
                    e.printStackTrace();
                    showErrorMsg();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Util.ProgressDialog.dismiss();
                if(call != null) //problem is requesting / internet connection problem
                    callback.invoke(null);
                else //already login response from 401 handling
                    callback.alreadyLogin();
            }
        });
    }
    //endregion

    public static void nfcData(Map<String, String> bodyMap, final Callback<NFCDataModel> callback) {
        NetworkHandler.postRequest(API.NFC, bodyMap, new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(call == null){
                        handle422Error(response,callback);
                    } else {
                        Util.ProgressDialog.dismiss();
                        NFCDataModel model = Util.GsonUtils.fromJSON(response.body().string(), NFCDataModel.class);
                        callback.invoke(model);
                    }
                } catch (Exception e) {
                    Util.ProgressDialog.dismiss();
                    e.printStackTrace();
                    showErrorMsg();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Util.ProgressDialog.dismiss();
                if(call != null) //problem is requesting / internet connection problem
                    callback.invoke(null);
                else //already login response from 401 handling
                    callback.alreadyLogin();
            }
        });
    }

    public static void placeOrder(Map<String, String> bodyMap, final Callback<PlacedOrderModel> callback) {
        NetworkHandler.postRequest(API.PLACE_ORDER, bodyMap, new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(call == null){
                        handle422Error(response,callback);
                    } else {
                        Util.ProgressDialog.dismiss();
                        PlacedOrderModel model = Util.GsonUtils.fromJSON(response.body().string(), PlacedOrderModel.class);
                        callback.invoke(model);
                    }
                } catch (Exception e) {
                    Util.ProgressDialog.dismiss();
                    e.printStackTrace();
                    showErrorMsg();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Util.ProgressDialog.dismiss();
                if(call != null) //problem is requesting / internet connection problem
                    callback.invoke(null);
                else //already login response from 401 handling
                    callback.alreadyLogin();
            }
        });
    }

    public static void updatePlaceOrder(Map<String, String> bodyMap, final Callback<PlacedOrderModel> callback) {
        NetworkHandler.postRequest(API.PLACE_ORDER, bodyMap, new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(call == null){
                        handle422Error(response,callback);
                    } else {
                        Util.ProgressDialog.dismiss();
                        PlacedOrderModel model = Util.GsonUtils.fromJSON(response.body().string(), PlacedOrderModel.class);
                        callback.invoke(model);
                    }
                } catch (Exception e) {
                    Util.ProgressDialog.dismiss();
                    e.printStackTrace();
                    showErrorMsg();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Util.ProgressDialog.dismiss();
                if(call != null) //problem is requesting / internet connection problem
                    callback.invoke(null);
                else //already login response from 401 handling
                    callback.alreadyLogin();
            }
        });
    }

    public static void closeOrder(Map<String, String> bodyMap, final Callback<PlacedOrderModel> callback) {
        NetworkHandler.postRequest(API.CLOSE_ORDER, bodyMap, new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(call == null){
                        handle422Error(response,callback);
                    } else {
                        Util.ProgressDialog.dismiss();
                        PlacedOrderModel model = Util.GsonUtils.fromJSON(response.body().string(), PlacedOrderModel.class);
                        callback.invoke(model);
                    }
                } catch (Exception e) {
                    Util.ProgressDialog.dismiss();
                    e.printStackTrace();
                    showErrorMsg();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Util.ProgressDialog.dismiss();
                if(call != null) //problem is requesting / internet connection problem
                    callback.invoke(null);
                else //already login response from 401 handling
                    callback.alreadyLogin();
            }
        });
    }

    public static void servedItems(Map<String, String> bodyMap, final Callback<PlacedOrderModel> callback) {
        NetworkHandler.postRequest(API.SERVED_ITEM, bodyMap, new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(call == null){
                        handle422Error(response,callback);
                    } else {
                        Util.ProgressDialog.dismiss();
                        PlacedOrderModel model = Util.GsonUtils.fromJSON(response.body().string(), PlacedOrderModel.class);
                        callback.invoke(model);
                    }
                } catch (Exception e) {
                    Util.ProgressDialog.dismiss();
                    e.printStackTrace();
                    showErrorMsg();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Util.ProgressDialog.dismiss();
                if(call != null) //problem is requesting / internet connection problem
                    callback.invoke(null);
                else //already login response from 401 handling
                    callback.alreadyLogin();
            }
        });
    }

    //endregion

    //region ***** GET REQUEST *******

    //region Profile
    public static void getProfile(@NonNull final Callback<ProfileModel> callback) {
        NetworkHandler.getRequest(API.PROFILE, null, new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(call == null){
                        handle422Error(response,callback);
                    } else {
                        Util.ProgressDialog.dismiss();
                        ProfileModel model = Util.GsonUtils.fromJSON(response.body().string(), ProfileModel.class);
                        callback.invoke(model);
                    }
                } catch (Exception e) {
                    Util.ProgressDialog.dismiss();
                    e.printStackTrace();
                    showErrorMsg();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Util.ProgressDialog.dismiss();
                if(call != null) //problem is requesting / internet connection problem
                    callback.invoke(null);
                else //already login response from 401 handling
                    callback.alreadyLogin();
            }
        });
    }

    public static void getLocations(@NonNull final Callback<LocationModel> callback) {
        NetworkHandler.getRequest(API.GET_LOCATION, null, new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(call == null){
                        handle422Error(response,callback);
                    } else {
                        Util.ProgressDialog.dismiss();
                        LocationModel model = Util.GsonUtils.fromJSON(response.body().string(), LocationModel.class);
                        callback.invoke(model);
                    }
                } catch (Exception e) {
                    Util.ProgressDialog.dismiss();
                    e.printStackTrace();
                    showErrorMsg();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Util.ProgressDialog.dismiss();
                if(call != null) //problem is requesting / internet connection problem
                    callback.invoke(null);
                else //already login response from 401 handling
                    callback.alreadyLogin();
            }
        });
    }
    //endregion

    public static void getKitchenItems(Map<String, String> bodyMap,@NonNull final Callback<KitchenDataModel> callback) {

        NetworkHandler.postRequest(API.MENU,bodyMap,new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(call == null){
                        handle422Error(response,callback);
                    } else {
                        Util.ProgressDialog.dismiss();
                        KitchenDataModel model = Util.GsonUtils.fromJSON(response.body().string(), KitchenDataModel.class);
                        callback.invoke(model);
                    }
                } catch (Exception e) {
                    Util.ProgressDialog.dismiss();
                    e.printStackTrace();
                    showErrorMsg();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Util.ProgressDialog.dismiss();
                if(call != null) //problem is requesting / internet connection problem
                    callback.invoke(null);
                else //already login response from 401 handling
                    callback.alreadyLogin();
            }
        });
    }

    //created this method for just dialog cancelling.. removed dialog dismissed becauese on
    //response of this call need to hit another request.
    public static void getKitchenItemsForPlacedOrder(Map<String, String> bodyMap,@NonNull final Callback<KitchenDataModel> callback) {
        NetworkHandler.postRequest(API.MENU, bodyMap, new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(call == null){
                        handle422Error(response,callback);
                    } else {
                        Util.ProgressDialog.dismiss();
                        KitchenDataModel model = Util.GsonUtils.fromJSON(response.body().string(), KitchenDataModel.class);
                        callback.invoke(model);
                    }
                } catch (Exception e) {
                    Util.ProgressDialog.dismiss();
                    e.printStackTrace();
                    showErrorMsg();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Util.ProgressDialog.dismiss();
                if(call != null) //problem is requesting / internet connection problem
                    callback.invoke(null);
                else //already login response from 401 handling
                    callback.alreadyLogin();
            }
        });
    }

    public static void getPlacedOrder(@NonNull final Callback<PlacedOrderGetResponse> callback) {
        NetworkHandler.getRequest(API.PLACE_ORDER, null, new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(call == null){
                        handle422Error(response,callback);
                    } else {
                        Util.ProgressDialog.dismiss();
                        PlacedOrderGetResponse model = Util.GsonUtils.fromJSON(response.body().string(), PlacedOrderGetResponse.class);
                        callback.invoke(model);
                    }
                } catch (Exception e) {
                    Util.ProgressDialog.dismiss();
                    e.printStackTrace();
                    showErrorMsg();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Util.ProgressDialog.dismiss();
                if(call != null) //problem is requesting / internet connection problem
                    callback.invoke(null);
                else //already login response from 401 handling
                    callback.alreadyLogin();
            }
        });
    }

    public static void getSpecificPlacedOrder(String orderId, @NonNull final Callback<PlacedOrderModel> callback) {
        NetworkHandler.getRequest(API.GET_SPECIFIC_ORDER + orderId, null, new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(call == null){
                        handle422Error(response,callback);
                    } else {
                        Util.ProgressDialog.dismiss();
                        PlacedOrderModel model = Util.GsonUtils.fromJSON(response.body().string(), PlacedOrderModel.class);
                        callback.invoke(model);
                    }
                } catch (Exception e) {
                    Util.ProgressDialog.dismiss();
                    e.printStackTrace();
                    showErrorMsg();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Util.ProgressDialog.dismiss();
                if(call != null) //problem is requesting / internet connection problem
                    callback.invoke(null);
                else //already login response from 401 handling
                    callback.alreadyLogin();
            }
        });
    }

    public static void getNotifications(@NonNull final Callback<NotificationResponseModel> callback) {
        NetworkHandler.getRequest(API.NOTIFICATION, null, new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(call == null){
                        handle422Error(response,callback);
                    } else {
                        Util.ProgressDialog.dismiss();
                        NotificationResponseModel model = Util.GsonUtils.fromJSON(response.body().string(), NotificationResponseModel.class);
                        callback.invoke(model);
                    }
                } catch (Exception e) {
                    Util.ProgressDialog.dismiss();
                    e.printStackTrace();
                    showErrorMsg();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Util.ProgressDialog.dismiss();
                if(call != null) //problem is requesting / internet connection problem
                    callback.invoke(null);
                else //already login response from 401 handling
                    callback.alreadyLogin();
            }
        });
    }

    //endregion

    private static void showErrorMsg() {
        Util.ProgressDialog.dismiss();
        AppLog.toast("Problem in requesting to server, please try again.");
    }

    //Request parameter error
    private static void handle422Error(Response response, Callback callback){
        Util.ProgressDialog.dismiss();
        try {
            JSONObject errorObject = new JSONObject(response.errorBody().string());
            Iterator<String> iterator = errorObject.keys();
            while (iterator.hasNext()){
                String key = iterator.next();
                if(key.equalsIgnoreCase("error_message")){
                    String errorMsg = errorObject.getString(key);
//                    AppLog.toast(errorMsg);
                    callback._422(errorMsg);
                    break;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            AppLog.toast("Unreachable 422 error");
        }
    }
}
