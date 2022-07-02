package com.example.smartrestaurant.db_handlers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.smartrestaurant.model.NotificationItemModel;
import com.example.smartrestaurant.model.kitchen.KitchenItemListModel;
import com.example.smartrestaurant.model.kitchen.KitchenNameModel;
import com.example.smartrestaurant.model.kitchen.OrderOnHoldModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell 5521 on 9/26/2016.
 */
public class DbScript {
    private SQLiteDatabase database;
    private static DbScript instance;

    public static DbScript getInstance() {
        if (instance == null) instance = new DbScript();
        return instance;
    }

    private void getDatabase() {
        DbHandler dbHandler = DbHandler.getInstance();
        if (!dbHandler.openDatabase()) {
            dbHandler = DbHandler.newInstance();
        }
        database = dbHandler.getDatabase();
    }

    //save and get kitchen and items ( data coming from server)
    public boolean saveKitchens(KitchenNameModel model) {

        if (database == null || !database.isOpen())
            getDatabase();

        ContentValues values = new ContentValues();
        values.put(DbTemplate.Kitchens.KTC_TRANS_ID, model.getKtcMainId());
        values.put(DbTemplate.Kitchens.KTC_MAIN_ID, model.getKtcMainId());
        values.put(DbTemplate.Kitchens.REST_TRANS_ID, model.getRestTransId());
        values.put(DbTemplate.Kitchens.KTC_NAME, model.getKtcName());
        values.put(DbTemplate.Kitchens.KTC_DESC, model.getKtcDesc());
        values.put(DbTemplate.Kitchens.KTC_STATUS, model.getKtcStatus());
        values.put(DbTemplate.Kitchens.KTC_SYSTEM_DATE, model.getKtcSystemDate());
        values.put(DbTemplate.Kitchens.KTC_SYNC_DATE, model.getKtcSyncDate());


        long id;
        synchronized (database) {
            id = database.insert(DbTemplate.Kitchens.TABLE_NAME, null, values);
        }

        return id != -1;
    }

    public boolean saveKitchenItems(KitchenItemListModel model) {

        if (database == null || !database.isOpen())
            getDatabase();

        ContentValues values = new ContentValues();
        values.put(DbTemplate.Kitchen_Items.ITEM_TRANS_ID, model.getItemTransId());
        values.put(DbTemplate.Kitchen_Items.ITEM_MAIN_ID, model.getItemMainId());
        values.put(DbTemplate.Kitchen_Items.CAT_TRANS_ID, model.getCatTransId());
        values.put(DbTemplate.Kitchen_Items.KTC_TRANS_ID, model.getKtcTransId());
        values.put(DbTemplate.Kitchen_Items.ITEM_NAME, model.getItemName());
        values.put(DbTemplate.Kitchen_Items.ITEM_PRICE, model.getItemPrice());
        values.put(DbTemplate.Kitchen_Items.ITEM_SALE_PRICE, model.getItemSalePrice());
        values.put(DbTemplate.Kitchen_Items.ITEM_IMAGE, model.getItemImage());
        values.put(DbTemplate.Kitchen_Items.ITEM_UNIT, model.getItemUnit());
        values.put(DbTemplate.Kitchen_Items.ITEM_SYNC_DATE, model.getItemSyncDate());
        values.put(DbTemplate.Kitchen_Items.ITEM_AVAIL, model.getItemAvail());

        long id;
        synchronized (database) {
            id = database.insert(DbTemplate.Kitchen_Items.TABLE_NAME, null, values);
        }

        return id != -1;
    }

    public List<KitchenNameModel> getAllKitchens() {

        String query = "SELECT * FROM " + DbTemplate.Kitchens.TABLE_NAME;

        Cursor cursor;
        if (database == null || !database.isOpen())
            getDatabase();

        synchronized (database) {
            cursor = database.rawQuery(query, null);
        }

        ArrayList<KitchenNameModel> list = null;
        if (cursor != null && cursor.moveToFirst()) {
            list = new ArrayList<>();
            do {
                KitchenNameModel model = new KitchenNameModel();

                model.setKtcTransId(cursor.getString(cursor.getColumnIndex(DbTemplate.Kitchens.KTC_MAIN_ID)));
                model.setKtcMainId(cursor.getString(cursor.getColumnIndex(DbTemplate.Kitchens.KTC_MAIN_ID)));
                model.setRestTransId(cursor.getString(cursor.getColumnIndex(DbTemplate.Kitchens.REST_TRANS_ID)));
                model.setKtcName(cursor.getString(cursor.getColumnIndex(DbTemplate.Kitchens.KTC_NAME)));
                model.setKtcDesc(cursor.getString(cursor.getColumnIndex(DbTemplate.Kitchens.KTC_DESC)));
                model.setKtcStatus(cursor.getString(cursor.getColumnIndex(DbTemplate.Kitchens.KTC_STATUS)));
                model.setKtcSystemDate(cursor.getString(cursor.getColumnIndex(DbTemplate.Kitchens.KTC_SYSTEM_DATE)));
                model.setKtcSyncDate(cursor.getString(cursor.getColumnIndex(DbTemplate.Kitchens.KTC_SYNC_DATE)));

                list.add(model);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return list == null ? new ArrayList<KitchenNameModel>() : list;
    }

    public List<KitchenItemListModel> getAllKitchenItems(String ktc_trans_id) {

        String query = "SELECT * FROM " + DbTemplate.Kitchen_Items.TABLE_NAME + " WHERE " +
                DbTemplate.Kitchen_Items.KTC_TRANS_ID + "=" + ktc_trans_id;

        Cursor cursor;
        if (database == null || !database.isOpen())
            getDatabase();

        synchronized (database) {
            cursor = database.rawQuery(query, null);
        }

        ArrayList<KitchenItemListModel> list = null;
        if (cursor != null && cursor.moveToFirst()) {
            list = new ArrayList<>();
            do {
                KitchenItemListModel model = new KitchenItemListModel();

                model.setItemTransId(cursor.getString(cursor.getColumnIndex(DbTemplate.Kitchen_Items.ITEM_TRANS_ID)));
                model.setItemMainId(cursor.getString(cursor.getColumnIndex(DbTemplate.Kitchen_Items.ITEM_MAIN_ID)));
                model.setCatTransId(cursor.getString(cursor.getColumnIndex(DbTemplate.Kitchen_Items.CAT_TRANS_ID)));
                model.setKtcTransId(cursor.getString(cursor.getColumnIndex(DbTemplate.Kitchen_Items.KTC_TRANS_ID)));
                model.setItemName(cursor.getString(cursor.getColumnIndex(DbTemplate.Kitchen_Items.ITEM_NAME)));
                model.setItemPrice(cursor.getString(cursor.getColumnIndex(DbTemplate.Kitchen_Items.ITEM_PRICE)));
                model.setItemSalePrice(cursor.getString(cursor.getColumnIndex(DbTemplate.Kitchen_Items.ITEM_SALE_PRICE)));
                model.setItemImage(cursor.getString(cursor.getColumnIndex(DbTemplate.Kitchen_Items.ITEM_IMAGE)));
                model.setItemUnit(cursor.getString(cursor.getColumnIndex(DbTemplate.Kitchen_Items.ITEM_UNIT)));
                model.setItemSyncDate(cursor.getString(cursor.getColumnIndex(DbTemplate.Kitchen_Items.ITEM_SYNC_DATE)));
                model.setItemAvail(cursor.getString(cursor.getColumnIndex(DbTemplate.Kitchen_Items.ITEM_AVAIL)));

                list.add(model);
            } while (cursor.moveToNext());
        }

        return list == null ? new ArrayList<KitchenItemListModel>() : list;
    }

    public void clearAllKitchen() {
        String kitchenTable = "DELETE FROM " + DbTemplate.Kitchens.TABLE_NAME;
        String kitchenItemsTable = "DELETE FROM " + DbTemplate.Kitchen_Items.TABLE_NAME;

        if (database == null || !database.isOpen())
            getDatabase();

        synchronized (database) {
            database.execSQL(kitchenTable);
            database.execSQL(kitchenItemsTable);
        }
    }

    //save and get kitchen and items ( data coming from server)

    public boolean createNewOrder(KitchenItemListModel model) {

        if (database == null || !database.isOpen())
            getDatabase();

        ContentValues values = new ContentValues();
        values.put(DbTemplate.SpecificOrderItems.ITEM_TRANS_ID, model.getItemTransId());
        values.put(DbTemplate.SpecificOrderItems.ITEM_MAIN_ID, model.getItemMainId());
        values.put(DbTemplate.SpecificOrderItems.CAT_TRANS_ID, model.getCatTransId());
        values.put(DbTemplate.SpecificOrderItems.KTC_TRANS_ID, model.getKtcTransId());
        values.put(DbTemplate.SpecificOrderItems.ITEM_NAME, model.getItemName());
        values.put(DbTemplate.SpecificOrderItems.ITEM_PRICE, model.getItemPrice());
        values.put(DbTemplate.SpecificOrderItems.ITEM_SALE_PRICE, model.getItemSalePrice());
        values.put(DbTemplate.SpecificOrderItems.ITEM_IMAGE, model.getItemImage());
        values.put(DbTemplate.SpecificOrderItems.ITEM_UNIT, model.getItemUnit());
        values.put(DbTemplate.SpecificOrderItems.ITEM_SYNC_DATE, model.getItemSyncDate());
        values.put(DbTemplate.SpecificOrderItems.ITEM_AVAIL, model.getItemAvail());
        values.put(DbTemplate.SpecificOrderItems.TABLE_NUMBER, model.getTable_number());
        values.put(DbTemplate.SpecificOrderItems.SELECTED_COUNT, model.getSelected_count());
        values.put(DbTemplate.SpecificOrderItems.IS_SELECTED, model.getIs_selected());
        values.put(DbTemplate.SpecificOrderItems.ON_HOLD, model.getOn_hold());
        values.put(DbTemplate.SpecificOrderItems.ORDER_NUMBER, model.getOrder_number());

        long id;
        synchronized (database) {
            id = database.insert(DbTemplate.SpecificOrderItems.TABLE_NAME, null, values);
        }

        return id != -1;
    }

    public List<KitchenItemListModel> getOrderByTableNumber(String tableNumber, String orderNumber, String ktc_trans_id) {

        String query = "SELECT * FROM " + DbTemplate.SpecificOrderItems.TABLE_NAME + " WHERE " +
                DbTemplate.SpecificOrderItems.KTC_TRANS_ID + "=" + ktc_trans_id + " AND " +
                DbTemplate.SpecificOrderItems.TABLE_NUMBER + "='" + tableNumber + "' AND " +
                DbTemplate.SpecificOrderItems.ORDER_NUMBER + "='" + orderNumber + "'";

        Cursor cursor;
        if (database == null || !database.isOpen())
            getDatabase();

        synchronized (database) {
            cursor = database.rawQuery(query, null);
        }

        ArrayList<KitchenItemListModel> list = null;
        if (cursor != null && cursor.moveToFirst()) {
            list = new ArrayList<>();
            do {
                KitchenItemListModel model = new KitchenItemListModel();

                model.setItemTransId(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.ITEM_TRANS_ID)));
                model.setItemMainId(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.ITEM_MAIN_ID)));
                model.setCatTransId(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.CAT_TRANS_ID)));
                model.setKtcTransId(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.KTC_TRANS_ID)));
                model.setItemName(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.ITEM_NAME)));
                model.setItemPrice(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.ITEM_PRICE)));
                model.setItemSalePrice(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.ITEM_SALE_PRICE)));
                model.setItemImage(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.ITEM_IMAGE)));
                model.setItemUnit(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.ITEM_UNIT)));
                model.setItemSyncDate(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.ITEM_SYNC_DATE)));
                model.setItemAvail(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.ITEM_AVAIL)));
                model.setIs_selected(cursor.getInt(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.IS_SELECTED)));
                model.setSelected_count(cursor.getInt(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.SELECTED_COUNT)));
                model.setTable_number(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.TABLE_NUMBER)));
                model.setOn_hold(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.ON_HOLD)));
                model.setOrder_number(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.ORDER_NUMBER)));

                list.add(model);
            } while (cursor.moveToNext());
        }

        return list == null ? new ArrayList<KitchenItemListModel>() : list;
    }

    public List<KitchenItemListModel> getSelectedItemsByTableNumber(String tableNumber, String orderNumber) {

        String query = "SELECT * FROM " + DbTemplate.SpecificOrderItems.TABLE_NAME + " WHERE " +
                DbTemplate.SpecificOrderItems.TABLE_NUMBER + "='" + tableNumber + "' AND " +
                DbTemplate.SpecificOrderItems.ORDER_NUMBER + "='" + orderNumber + "' AND " +
                DbTemplate.SpecificOrderItems.IS_SELECTED + "=1";

        Cursor cursor;
        if (database == null || !database.isOpen())
            getDatabase();

        synchronized (database) {
            cursor = database.rawQuery(query, null);
        }

        ArrayList<KitchenItemListModel> list = null;
        if (cursor != null && cursor.moveToFirst()) {
            list = new ArrayList<>();
            do {
                KitchenItemListModel model = new KitchenItemListModel();

                model.setItemTransId(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.ITEM_TRANS_ID)));
                model.setItemMainId(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.ITEM_MAIN_ID)));
                model.setCatTransId(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.CAT_TRANS_ID)));
                model.setKtcTransId(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.KTC_TRANS_ID)));
                model.setItemName(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.ITEM_NAME)));
                model.setItemPrice(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.ITEM_PRICE)));
                model.setItemSalePrice(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.ITEM_SALE_PRICE)));
                model.setItemImage(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.ITEM_IMAGE)));
                model.setItemUnit(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.ITEM_UNIT)));
                model.setItemSyncDate(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.ITEM_SYNC_DATE)));
                model.setItemAvail(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.ITEM_AVAIL)));
                model.setIs_selected(cursor.getInt(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.IS_SELECTED)));
                model.setSelected_count(cursor.getInt(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.SELECTED_COUNT)));
                model.setTable_number(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.TABLE_NUMBER)));
                model.setOn_hold(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.ON_HOLD)));
                model.setOrder_number(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.ORDER_NUMBER)));
                model.setRemarks(cursor.getString(cursor.getColumnIndex(DbTemplate.SpecificOrderItems.REMARKS)));

                list.add(model);
            } while (cursor.moveToNext());
        }

        return list == null ? new ArrayList<KitchenItemListModel>() : list;
    }

    public void updateRemarksOfSpecificItem(KitchenItemListModel model) {
        String query = "UPDATE " + DbTemplate.SpecificOrderItems.TABLE_NAME + " SET " +
                DbTemplate.SpecificOrderItems.REMARKS + "='" + model.getRemarks() + "' WHERE " +
                DbTemplate.SpecificOrderItems.TABLE_NUMBER + "='" + model.getTable_number() + "' AND " +
                DbTemplate.SpecificOrderItems.ORDER_NUMBER + "='" + model.getOrder_number() + "' AND " +
                DbTemplate.SpecificOrderItems.ITEM_MAIN_ID + "=" + model.getItemMainId();

        if (database == null || !database.isOpen())
            getDatabase();

        synchronized (database) {
            database.execSQL(query);
        }

        return;
    }

    public boolean insertNewOrderToOnHoldTable(OrderOnHoldModel model) {

        if (database == null || !database.isOpen())
            getDatabase();

        ContentValues values = new ContentValues();
        values.put(DbTemplate.OrderOnHold.CUS_NAME, model.getCus_name());
        values.put(DbTemplate.OrderOnHold.CUS_CARD_NUM, model.getCus_card_num());
        values.put(DbTemplate.OrderOnHold.ORDER_NUMBER, model.getOrder_number());
        values.put(DbTemplate.OrderOnHold.PAY_MODE, model.getPay_mode());
        values.put(DbTemplate.OrderOnHold.SERVICE_CHARGE, model.getService_charge());
        values.put(DbTemplate.OrderOnHold.TABLE_NUMBER, model.getTable_number());
        values.put(DbTemplate.OrderOnHold.ON_HOLD, model.getOn_hold());
        values.put(DbTemplate.OrderOnHold.IS_PARENT_OR_CHILD, model.getIs_parent_or_child());
        values.put(DbTemplate.OrderOnHold.CUST_TANS_ID, model.getCust_trans_id());

        long id;
        synchronized (database) {
            id = database.insert(DbTemplate.OrderOnHold.TABLE_NAME, null, values);
        }

        return id != -1;
    }

    public List<OrderOnHoldModel> getAllOrders() {

        String query = "SELECT * FROM " + DbTemplate.OrderOnHold.TABLE_NAME;

        Cursor cursor;
        if (database == null || !database.isOpen())
            getDatabase();

        synchronized (database) {
            cursor = database.rawQuery(query, null);
        }

        ArrayList<OrderOnHoldModel> list = null;
        if (cursor != null && cursor.moveToFirst()) {
            list = new ArrayList<>();
            do {
                OrderOnHoldModel model = new OrderOnHoldModel();

                model.setCus_name(cursor.getString(cursor.getColumnIndex(DbTemplate.OrderOnHold.CUS_NAME)));
                model.setCus_card_num(cursor.getString(cursor.getColumnIndex(DbTemplate.OrderOnHold.CUS_CARD_NUM)));
                model.setService_charge(cursor.getString(cursor.getColumnIndex(DbTemplate.OrderOnHold.SERVICE_CHARGE)));
                model.setPay_mode(cursor.getString(cursor.getColumnIndex(DbTemplate.OrderOnHold.PAY_MODE)));
                model.setOrder_number(cursor.getString(cursor.getColumnIndex(DbTemplate.OrderOnHold.ORDER_NUMBER)));
                model.setTable_number(cursor.getString(cursor.getColumnIndex(DbTemplate.OrderOnHold.TABLE_NUMBER)));
                model.setOn_hold(cursor.getString(cursor.getColumnIndex(DbTemplate.OrderOnHold.ON_HOLD)));
                model.setIs_parent_or_child(cursor.getString(cursor.getColumnIndex(DbTemplate.OrderOnHold.IS_PARENT_OR_CHILD)));
                model.setCust_trans_id(cursor.getString(cursor.getColumnIndex(DbTemplate.OrderOnHold.CUST_TANS_ID)));

                list.add(model);
            } while (cursor.moveToNext());
        }

        return list == null ? new ArrayList<OrderOnHoldModel>() : list;
    }

    public void updateItem(KitchenItemListModel model) {
        String query = "UPDATE " + DbTemplate.SpecificOrderItems.TABLE_NAME + " SET " +
                DbTemplate.SpecificOrderItems.IS_SELECTED + "=" + model.getIs_selected() + "," +
                DbTemplate.SpecificOrderItems.SELECTED_COUNT + "=" + model.getSelected_count() + " WHERE " +
                DbTemplate.SpecificOrderItems.TABLE_NUMBER + "='" + model.getTable_number() + "' AND " +
                DbTemplate.SpecificOrderItems.ORDER_NUMBER + "='" + model.getOrder_number() + "' AND " +
                DbTemplate.SpecificOrderItems.ITEM_MAIN_ID + "=" + model.getItemMainId();

        if (database == null || !database.isOpen())
            getDatabase();

        synchronized (database) {
            database.execSQL(query);
        }

        return;
    }

    public void clearOrdersTable() {
        String clearHoldOrderTable = "DELETE FROM " + DbTemplate.OrderOnHold.TABLE_NAME;
        String clearSpecificOrderTable = "DELETE FROM " + DbTemplate.SpecificOrderItems.TABLE_NAME;

        if (database == null || !database.isOpen())
            getDatabase();

        synchronized (database) {
            database.execSQL(clearHoldOrderTable);
            database.execSQL(clearSpecificOrderTable);
        }
    }

    public void clearHoldOrders(String tableNumber,String orderNumber) {
        String clearHoldOrderTable = "DELETE FROM " + DbTemplate.OrderOnHold.TABLE_NAME + " WHERE " +
                DbTemplate.OrderOnHold.TABLE_NUMBER + "='" + tableNumber + "' AND " +
                DbTemplate.OrderOnHold.ORDER_NUMBER + "='" + orderNumber + "'";
        if (database == null || !database.isOpen())
            getDatabase();

        synchronized (database) {
            database.execSQL(clearHoldOrderTable);
        }
    }


    public void deleteHoldOrderFromDbAfterPlaced(String tableNumber, String orderNumber) {
        String querySpecificOrderItems = "DELETE FROM " + DbTemplate.SpecificOrderItems.TABLE_NAME + " WHERE " +
                DbTemplate.SpecificOrderItems.TABLE_NUMBER + "='" + tableNumber + "' AND " +
                DbTemplate.SpecificOrderItems.ORDER_NUMBER + "='" + orderNumber + "'";

        String queryOrderOnHold = "DELETE FROM " + DbTemplate.OrderOnHold.TABLE_NAME + " WHERE " +
                DbTemplate.OrderOnHold.TABLE_NUMBER + "='" + tableNumber + "' AND " +
                DbTemplate.OrderOnHold.ORDER_NUMBER + "='" + orderNumber + "'";

        if (database == null || !database.isOpen())
            getDatabase();

        synchronized (database) {
            database.execSQL(querySpecificOrderItems);
            database.execSQL(queryOrderOnHold);
        }
    }

    public List<NotificationItemModel> getAllNotifications() {

        String query = "SELECT * FROM " + DbTemplate.Notification.TABLE_NAME;

        Cursor cursor;
        if (database == null || !database.isOpen())
            getDatabase();

        synchronized (database) {
            cursor = database.rawQuery(query, null);
        }

        ArrayList<NotificationItemModel> list = null;
        if (cursor != null && cursor.moveToFirst()) {
            list = new ArrayList<>();
            do {
                NotificationItemModel model = new NotificationItemModel();

                model.setItemId(cursor.getString(cursor.getColumnIndex(DbTemplate.Notification.ITEM_ID)));
                model.setItemName(cursor.getString(cursor.getColumnIndex(DbTemplate.Notification.ITEM_NAME)));
                model.setItemQty(cursor.getString(cursor.getColumnIndex(DbTemplate.Notification.ITEM_QTY)));
                model.setKtcId(cursor.getString(cursor.getColumnIndex(DbTemplate.Notification.KTC_ID)));
                model.setKtcName(cursor.getString(cursor.getColumnIndex(DbTemplate.Notification.KTC_NAME)));
                model.setOrderNo(cursor.getString(cursor.getColumnIndex(DbTemplate.Notification.ORDER_NO)));
                model.setTableNo(cursor.getString(cursor.getColumnIndex(DbTemplate.Notification.TABLE_NO)));
                model.setOdmainID(cursor.getString(cursor.getColumnIndex(DbTemplate.Notification.odMainId)));

                list.add(model);
            } while (cursor.moveToNext());
        }

        return list == null ? new ArrayList<NotificationItemModel>() : list;
    }

    public void clearAllNotification() {
        String clearAll = "DELETE FROM " + DbTemplate.Notification.TABLE_NAME;

        if (database == null || !database.isOpen())
            getDatabase();

        synchronized (database) {
            database.execSQL(clearAll);
        }
    }

    public boolean saveNotification(NotificationItemModel model) {

        if (database == null || !database.isOpen())
            getDatabase();

        ContentValues values = new ContentValues();
        values.put(DbTemplate.Notification.ITEM_ID, model.getItemId());
        values.put(DbTemplate.Notification.ITEM_NAME, model.getItemName());
        values.put(DbTemplate.Notification.ITEM_QTY, model.getItemQty());
        values.put(DbTemplate.Notification.KTC_NAME, model.getKtcName());
        values.put(DbTemplate.Notification.KTC_ID, model.getKtcId());
        values.put(DbTemplate.Notification.ORDER_NO, model.getOrderNo());
        values.put(DbTemplate.Notification.TABLE_NO, model.getTableNo());
        values.put(DbTemplate.Notification.odMainId, model.getOdmainID());

        long id;
        synchronized (database) {
            id = database.insert(DbTemplate.Notification.TABLE_NAME, null, values);
        }

        return id != -1;
    }


    /*public boolean createFolder(FolderModel model){

        if(database == null || !database.isOpen())
            getDatabase();

        ContentValues values = new ContentValues();
        values.put(DbTemplate.Folder.NAME,model.name);
        values.put(DbTemplate.Folder.TYPE,model.type);
        values.put(DbTemplate.Folder.CREATED_AT,model.timestamp);

        long id;
        synchronized (database) {
            id = database.insert(DbTemplate.Folder.TABLE_NAME, null, values);
        }

        return id != -1;
    }

    public ArrayList<FolderModel> getAllFolders(){
        String query = Constants.FOLDER_SELECT_ALL_QUERY;

        Cursor cursor;
        if(database == null || !database.isOpen())
            getDatabase();

        synchronized (database){
            cursor = database.rawQuery(query,null);
        }

        ArrayList<FolderModel> list = null;
        if(cursor != null && cursor.moveToFirst()){
            list = new ArrayList<>();
             do {
                FolderModel folderModel = new FolderModel();

                folderModel.id = cursor.getString(cursor.getColumnIndex(DbTemplate.Folder._ID));
                folderModel.name = cursor.getString(cursor.getColumnIndex(DbTemplate.Folder.NAME));
                folderModel.timestamp = cursor.getString(cursor.getColumnIndex(DbTemplate.Folder.CREATED_AT));
                folderModel.type = cursor.getString(cursor.getColumnIndex(DbTemplate.Folder.TYPE));

                list.add(folderModel);
            } while (cursor.moveToNext());
        }

        return list==null?new ArrayList<FolderModel>():list;
    }

    public boolean createFile(FileModel model){

        if(database == null || !database.isOpen())
            getDatabase();

        ContentValues values = new ContentValues();
        values.put(DbTemplate.Links.NAME,model.name);
        values.put(DbTemplate.Links.HTTP_LINK,model.link);
        values.put(DbTemplate.Links.FOLDER_ID,model.folderId);
        values.put(DbTemplate.Links.TYPE,model.type);
        values.put(DbTemplate.Links.CREATED_AT,model.timestamp);

        long id;
        synchronized (database) {
            id = database.insert(DbTemplate.Links.TABLE_NAME, null, values);
        }

        return id != -1;
    }

    public ArrayList<FileModel> getAllFiles(String folderId){
        String query = "SELECT * FROM "+ DbTemplate.Links.TABLE_NAME+ " WHERE " +
                DbTemplate.Links.FOLDER_ID + "=" + folderId +" ORDER BY "+DbTemplate.Folder.CREATED_AT+" DESC";

        Cursor cursor;
        if(database == null || !database.isOpen())
            getDatabase();

        synchronized (database){
            cursor = database.rawQuery(query,null);
        }

        ArrayList<FileModel> list = null;
        if(cursor != null && cursor.moveToFirst()){
            list = new ArrayList<>();
            do {
                FileModel model = new FileModel();

                model.id = cursor.getString(cursor.getColumnIndex(DbTemplate.Links._ID));
                model.name = cursor.getString(cursor.getColumnIndex(DbTemplate.Links.NAME));
                model.timestamp = cursor.getString(cursor.getColumnIndex(DbTemplate.Links.CREATED_AT));
                model.type = cursor.getString(cursor.getColumnIndex(DbTemplate.Links.TYPE));
                model.link = cursor.getString(cursor.getColumnIndex(DbTemplate.Links.HTTP_LINK));
                model.folderId = cursor.getString(cursor.getColumnIndex(DbTemplate.Links.FOLDER_ID));

                list.add(model);
            } while (cursor.moveToNext());
        }

        return list==null?new ArrayList<FileModel>():list;
    }

    public boolean createNote(NoteModel model){

        if(database == null || !database.isOpen())
            getDatabase();

        ContentValues values = new ContentValues();
        values.put(DbTemplate.Notes.NAME,model.name);
        values.put(DbTemplate.Notes.TXT,model.txt);
        values.put(DbTemplate.Notes.CREATED_AT,model.created_at);

        long id;
        synchronized (database) {
            id = database.insert(DbTemplate.Notes.TABLE_NAME, null, values);
        }

        return id != -1;
    }

    public boolean updateNote(NoteModel model){

        if(database == null || !database.isOpen())
            getDatabase();

        ContentValues values = new ContentValues();
        values.put(DbTemplate.Notes.TXT,model.txt);
        values.put(DbTemplate.Notes.CREATED_AT, String.valueOf(System.currentTimeMillis()));

        long id;
        synchronized (database) {
            id = database.update(DbTemplate.Notes.TABLE_NAME, values, DbTemplate.Notes._ID+"=?",new String[]{model.id});
        }

        return id != -1;
    }

    public ArrayList<NoteModel> getAllNotes() {
        String query = Constants.NOTES_SELECT_ALL_QUERY;

        Cursor cursor;
        if(database == null || !database.isOpen())
            getDatabase();

        synchronized (database){
            cursor = database.rawQuery(query,null);
        }

        ArrayList<NoteModel> list = null;
        if(cursor != null && cursor.moveToFirst()){
            list = new ArrayList<>();
            do {
                NoteModel model = new NoteModel();

                model.id = cursor.getString(cursor.getColumnIndex(DbTemplate.Notes._ID));
                model.name = cursor.getString(cursor.getColumnIndex(DbTemplate.Notes.NAME));
                model.created_at = cursor.getString(cursor.getColumnIndex(DbTemplate.Notes.CREATED_AT));
                model.txt = cursor.getString(cursor.getColumnIndex(DbTemplate.Notes.TXT));

                list.add(model);
            } while (cursor.moveToNext());
        }

        return list==null?new ArrayList<NoteModel>():list;
    }

    public boolean deleteNote(String noteId){
        if(database == null || !database.isOpen())
            getDatabase();

        int i;
        synchronized (database){
            i = database.delete(DbTemplate.Notes.TABLE_NAME, DbTemplate.Notes._ID + " = ?", new String[] { noteId });
        }

        return i!=-1;
    }

    public boolean deleteFile(String fileId){
//        String query = "DELETE FROM "+ DbTemplate.Links.TABLE_NAME+ " WHERE " + DbTemplate.Links._ID + "='" + fileId +"'";

//        Cursor cursor;
        if(database == null || !database.isOpen())
            getDatabase();

        int i;
        synchronized (database){
            i = database.delete(DbTemplate.Links.TABLE_NAME, DbTemplate.Links._ID + " = ?", new String[] { fileId });
//            cursor = database.rawQuery(query,null);
        }

//        return cursor!=null;
        return i!=-1;
    }

    public boolean deleteFolder(String folderId){
        if(database == null || !database.isOpen())
            getDatabase();

        //delete folder item from Folder Table
        int i;
        synchronized (database){
            i = database.delete(DbTemplate.Folder.TABLE_NAME, DbTemplate.Folder._ID + " = ?", new String[] { folderId });
        }

        //if folder is deleted from Folder Table then remove all files related to this folder
        if(i != -1){
            synchronized (database){
                i = database.delete(DbTemplate.Links.TABLE_NAME, DbTemplate.Links.FOLDER_ID + " = ?", new String[] { folderId });
            }
        }

        return i!=-1;
    }*/

    public void releaseDatabase() {
        database = null;
    }
}
