package com.example.smartrestaurant.db_handlers;

/**
 * Created by Dell 5521 on 10/20/2016.
 */
public interface DbTemplate {
    interface Kitchens{
        String TABLE_NAME = "Kitchens";
        String KTC_TRANS_ID = "ktc_trans_id";
        String KTC_MAIN_ID = "ktc_main_id";
        String REST_TRANS_ID = "rest_trans_id";
        String KTC_NAME = "ktc_name";
        String KTC_DESC = "ktc_desc";
        String KTC_STATUS = "ktc_status";
        String KTC_SYSTEM_DATE = "ktc_system_date";
        String KTC_SYNC_DATE = "ktc_sync_date";
    }
    interface Kitchen_Items{
        String TABLE_NAME = "Kitchen_Items";
        String ITEM_TRANS_ID = "item_trans_id";
        String ITEM_MAIN_ID = "item_main_id";
        String CAT_TRANS_ID = "cat_trans_id";
        String KTC_TRANS_ID = "ktc_trans_id";
        String ITEM_NAME = "item_name";
        String ITEM_PRICE = "item_price";
        String ITEM_SALE_PRICE = "item_sale_price";
        String ITEM_IMAGE = "item_image";
        String ITEM_UNIT = "item_unit";
        String ITEM_SYNC_DATE = "item_sync_date";
        String ITEM_AVAIL = "item_avail";
    }
    interface SpecificOrderItems{
        String TABLE_NAME = "SpecificOrderItems";
        String ITEM_TRANS_ID = "item_trans_id";
        String ITEM_MAIN_ID = "item_main_id";
        String CAT_TRANS_ID = "cat_trans_id";
        String KTC_TRANS_ID = "ktc_trans_id";
        String ITEM_NAME = "item_name";
        String ITEM_PRICE = "item_price";
        String ITEM_SALE_PRICE = "item_sale_price";
        String ITEM_IMAGE = "item_image";
        String ITEM_UNIT = "item_unit";
        String ITEM_SYNC_DATE = "item_sync_date";
        String ITEM_AVAIL = "item_avail";
        String IS_SELECTED = "is_selected";
        String SELECTED_COUNT = "selected_count";
        String TABLE_NUMBER = "table_number";
        String ON_HOLD = "on_hold";
        String ORDER_NUMBER = "order_number";
        String REMARKS = "remarks";
    }
    interface OrderOnHold{
        String TABLE_NAME = "OrderOnHold";
        String CUS_NAME = "cus_name";
        String CUS_CARD_NUM = "cus_card_num";
        String ORDER_NUMBER = "order_number";
        String SERVICE_CHARGE = "service_charge";
        String PAY_MODE = "pay_mode";
        String TABLE_NUMBER = "table_number";
        String ON_HOLD = "on_hold";
        String IS_PARENT_OR_CHILD = "is_parent_or_child";
        String CUST_TANS_ID = "cust_trans_id";
    }
    interface Notification{
        String TABLE_NAME = "Notification";
        String ITEM_NAME = "item_name";
        String ITEM_ID = "item_id";
        String ITEM_QTY = "item_qty";
        String KTC_NAME = "ktc_name";
        String KTC_ID = "ktc_id";
        String TABLE_NO = "table_no";
        String ORDER_NO = "order_no";
        String odMainId = "od_main_id";
        String IS_READ = "is_read";
    }
    interface PlacedOrderItems{
        String TABLE_NAME = "PlacedOrderItems";
        String ITEM_TRANS_ID = "item_trans_id";
        String ITEM_MAIN_ID = "item_main_id";
        String CAT_TRANS_ID = "cat_trans_id";
        String KTC_TRANS_ID = "ktc_trans_id";
        String ITEM_NAME = "item_name";
        String ITEM_PRICE = "item_price";
        String ITEM_SALE_PRICE = "item_sale_price";
        String ITEM_IMAGE = "item_image";
        String ITEM_UNIT = "item_unit";
        String ITEM_SYNC_DATE = "item_sync_date";
        String ITEM_AVAIL = "item_avail";
        String IS_SELECTED = "is_selected";
        String SELECTED_COUNT = "selected_count";
        String TABLE_NUMBER = "table_number";
        String ORDER_NUMBER = "order_number";
        String IS_NEW_ADDED = "is_new_added";
    }
}
