package com.example.smartrestaurant.config;

/**
 * Created by Dell 5521 on 1/6/2018.
 */

public class API {



   //  public static final String BASE_URL = "http://kc.count-square.com/rest_login/";
    public static final String BASE_URL = "http://192.168.0.4/smart_app/rest_login/";
    // public static final String BASE_URL = "http://bohrazone.com/kc_smart_order/rest_login/";
    //public static final String BASE_URL = "http://192.168.0.18:81/kc_smart_order/rest_login/";
    //  public static final String BASE_URL = "http://192.168.0.12:80/kc_smart_order/rest_login/";
//    public static final String BASE_URL = "http://ezeebuying.com/order/rest_login/";
//    public static final String BASE_URL = "http://codbility.com/rest_order/rest_login/";

    //    public static final String BASE_URL_FOR_IMAGE = "http://ezeebuying.com/order/res/img";
  // public static final String BASE_URL_FOR_IMAGE = "http://appdeveloperkarachi.com/kc_smart_order/img";
    public static final String BASE_URL_FOR_IMAGE = "http://192.168.0.6:80/img";

    public static final String LOGIN = BASE_URL + "login_process";

    public static final String PROFILE = BASE_URL + "user_profile";
    public static final String GET_LOCATION = BASE_URL + "get_location";
    public static final String MENU = BASE_URL + "menu";
    public static final String NFC = BASE_URL + "nfc";
    public static final String PLACE_ORDER = BASE_URL + "order_place";
    public static final String CLOSE_ORDER = BASE_URL + "order_close";
    public static final String GET_SPECIFIC_ORDER = BASE_URL + "get_order/?order_id=";
    public static final String SERVED_ITEM = BASE_URL + "item_served";
    public static final String NOTIFICATION = BASE_URL + "notification";
    public static final String LOGOUT_EVERYWHERE = BASE_URL + "logout_everywhere";
    public static final String LOGOUT = BASE_URL + "logout";

    public static final String CONCAT_WAITER_IMAGE = BASE_URL_FOR_IMAGE + "/waiter/";//id
    public static final String CONCAT_PARENT_IMAGE = BASE_URL_FOR_IMAGE + "/customer/";//id
    public static final String CONCAT_DEPENDANT_IMAGE = BASE_URL_FOR_IMAGE + "/customer_depend/";//id
    public static final String CONCAT_ITEM_IMAGE = BASE_URL_FOR_IMAGE + "/items/";//id

    public static final String CONCAT_CUS_SIGNATURE_IMAGE = BASE_URL_FOR_IMAGE + "/customer_sign/";//id
    public static final String CONCAT_CUS__DEP_SIGN_IMAGE = BASE_URL_FOR_IMAGE + "/customer_depend_sign/";//id

}
