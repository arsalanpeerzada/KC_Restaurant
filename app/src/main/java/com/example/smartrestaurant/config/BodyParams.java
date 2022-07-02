package com.example.smartrestaurant.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dell 5521 on 1/6/2018.
 */

public class BodyParams {
    public static Map<String,String> login(String username, String password, String location){
        Map<String, String> map = new HashMap<>();
        map.put("username",username);
        map.put("password",password);
        map.put("location",location);
        return map;
    }
    public static Map<String,String> logoutEverywhere(String username, String password){
        Map<String, String> map = new HashMap<>();
        map.put("username",username);
        map.put("password",password);
        return map;
    }
    public static Map<String,String> nfcData(String customer_id,String type){
        Map<String, String> map = new HashMap<>();
        map.put("customer_id",customer_id);
        map.put("type",type);
        return map;
    }
    public static Map<String,String> placeOrder(String memberId, String tableNumber, String payType,
                                                String userId, String grandTotal, String orderList,
                                                String service_charges,String isParentOrChild,boolean isForceCreate){
        Map<String, String> map = new HashMap<>();
        map.put("member_id",memberId);
        map.put("table_no",tableNumber);
        map.put("pay_type",payType);
        map.put("userid",userId);
        map.put("grand_total",grandTotal);
        map.put("is_cust_type",isParentOrChild);
        map.put("service_charges",service_charges);
        map.put("order_list",orderList);
        map.put("place_fresh","0");
        if(isForceCreate)
            map.put("place_fresh","1");
        return map;
    }
    public static Map<String,String> updatePlaceOrder(String updateList, String newAddList, String orderNumber){
        Map<String, String> map = new HashMap<>();
        map.put("_method","UPDATE");
        map.put("order_id",orderNumber);
        map.put("order_update",updateList);
        map.put("order_add",newAddList);
        return map;
    }
    public static Map<String,String> closeOrder(String orderNumber){
        Map<String, String> map = new HashMap<>();
        map.put("order_id",orderNumber);
        return map;
    }
    public static Map<String,String> servedItem(String odMainId,String item_id,String odMainid){
        Map<String, String> map = new HashMap<>();
        map.put("order_id",odMainId);
        map.put("item_id",item_id);
        map.put("od_main_id",odMainid);
        return map;
    }

    public static Map<String,String> setRestautant(String res_id)
    {
        Map<String, String> map = new HashMap<>();
        map.put("rest_id",res_id);
        return map;
    }
}
