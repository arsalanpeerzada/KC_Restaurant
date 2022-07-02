package com.example.smartrestaurant.model;

import com.example.smartrestaurant.db_handlers.DbScript;
import com.example.smartrestaurant.model.kitchen.KitchenDataModel;
import com.example.smartrestaurant.model.kitchen.KitchenItemListModel;
import com.example.smartrestaurant.model.kitchen.KitchenNameModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell 5521 on 1/13/2018.
 */

public class TransactionKitchenItemsModel {
    private static TransactionKitchenItemsModel instance;
    private List<KitchenItemListModel> listItems;
    private KitchenDataModel kitchenDataModel;
    private KitchenDataModel kitchenDataModelForPlaceOrder;

    public KitchenDataModel getKitchenDataModelForPlaceOrder() {
        return kitchenDataModelForPlaceOrder;
    }

    public void setKitchenDataModelForPlaceOrder(KitchenDataModel kitchenDataModelForPlaceOrder) {
        this.kitchenDataModelForPlaceOrder = kitchenDataModelForPlaceOrder;
    }
            /*String query = "UPDATE " + DbTemplate.SpecificOrderItems.TABLE_NAME + " SET " +
                DbTemplate.SpecificOrderItems.IS_SELECTED + "=" + model.getIs_selected() + "," +
                DbTemplate.SpecificOrderItems.SELECTED_COUNT + "=" + model.getSelected_count() + " WHERE " +
                DbTemplate.SpecificOrderItems.TABLE_NUMBER + "='" + model.getTable_number() + "' AND " +
                DbTemplate.SpecificOrderItems.ORDER_NUMBER + "='" + model.getOrder_number() + "' AND " +
                DbTemplate.SpecificOrderItems.ITEM_MAIN_ID + "=" + model.getItemMainId();*/
    public void updateKitchenItemForPlacedOrder(KitchenItemListModel model){
        for (int i = 0; i < kitchenDataModelForPlaceOrder.getData().size(); i++) {
            for (int j = 0; j < kitchenDataModelForPlaceOrder.getData().get(i).getItemList().size(); j++) {
                KitchenItemListModel model1 = kitchenDataModelForPlaceOrder.getData().get(i).getItemList().get(j);
                if(model1.getItemMainId().equalsIgnoreCase(model.getItemMainId())){
                    kitchenDataModelForPlaceOrder.getData().get(i).getItemList().set(j,model);
                    break;
                }
            }
        }
    }

    public static TransactionKitchenItemsModel getInstance(){
        return instance == null ? instance = new TransactionKitchenItemsModel() : instance ;
    }

    public KitchenDataModel getKitchenDataModel() {
        return kitchenDataModel;
    }

    public void setKitchenDataModel(KitchenDataModel kitchenDataModel) {
        this.kitchenDataModel = kitchenDataModel;
    }

    public List<KitchenItemListModel> getListItems(String ktc_trans_id) {
        if(kitchenDataModelForPlaceOrder.getData().size() <= 0)
            kitchenDataModelForPlaceOrder.setData(kitchenDataModel.getData());

        List<KitchenItemListModel> listModels = new ArrayList<>();
        for (int i = 0; i < kitchenDataModelForPlaceOrder.getData().size(); i++) {
            for (int j = 0; j < kitchenDataModelForPlaceOrder.getData().get(i).getItemList().size(); j++) {
                KitchenItemListModel model = kitchenDataModelForPlaceOrder.getData().get(i).getItemList().get(j);
                if(model.getKtcTransId().equalsIgnoreCase(ktc_trans_id)){
                    listModels.add(model);
                }
            }
        }
        return listModels;
    }

    public void setListItems(List<KitchenItemListModel> listItems) {
        this.listItems = listItems;
    }

}
