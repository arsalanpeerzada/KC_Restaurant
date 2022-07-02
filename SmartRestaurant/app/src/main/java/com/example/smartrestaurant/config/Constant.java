package com.example.smartrestaurant.config;

import android.app.Activity;

/**
 * Created by Dell 5521 on 12/26/2017.
 */

public class Constant {
    public static Activity activity;

    public static final int LIST_TYPE_ORDER_LIST = 1;
    public static final int LIST_TYPE_ORDER_SUMMARY_ITEMS = 2;
    public static final int LIST_TYPE_KITCHENS_ITEMS = 3;
    public static final int LIST_TYPE_ORDER_ON_HOLD = 4;
    public static final int LIST_TYPE_ORDER_PLACED = 5;
    public static final int LIST_TYPE_NOTIFICATION_LABEL = 6;
    public static final int LIST_TYPE_NOTIFICATION_ITEM = 7;

    public static final int KITCHEN_ORDER_DETAIL = 1;
    public static final int KITCHEN_ORDER_DETAIL_FOR_PLACED = 4;
    public static final int KITCHEN_INFLATE_ITEM_FRAGMENT = 2;
    public static final int KITCHEN_UPDATE_LIST_ITEM = 3;

    public static final int ORDER_DETAIL_HOLD = 1;
    public static final int ORDER_DETAIL_PLACE = 2;
    public static final int ORDER_DETAIL_CLOSE = 3;
    public static final int ORDER_DETAIL_ADD_NEW = 4;
    public static final int ORDER_DETAIL_SHOW_ORDER_LIST_AFTER_UPDATE_ITEMS = 5;

    public static final int PLACED_ORDER_ADD_NEW = 1;
    public static final int PLACED_ORDER_UPDATE = 2;

}
