package com.example.smartrestaurant.model.kitchen;

import com.example.smartrestaurant.config.Constant;
import com.example.smartrestaurant.ui.adapter.base.RecyclerViewBaseAdapter;

/**
 * Created by Dell 5521 on 1/16/2018.
 */

public class OrderOnHoldModel implements RecyclerViewBaseAdapter.AdapterDataType {

    private String table_number, on_hold, cus_name, cus_card_num,order_number,service_charge,pay_mode,is_parent_or_child, cust_trans_id;

    public String getTable_number() {
        return table_number;
    }

    public void setTable_number(String table_number) {
        this.table_number = table_number;
    }

    public String getOn_hold() {
        return on_hold;
    }

    public void setOn_hold(String on_hold) {
        this.on_hold = on_hold;
    }

    public String getCus_name() {
        return cus_name;
    }

    public void setCus_name(String cus_name) {
        this.cus_name = cus_name;
    }

    public String getCus_card_num() {
        return cus_card_num;
    }

    public void setCus_card_num(String cus_card_num) {
        this.cus_card_num = cus_card_num;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getService_charge() {
        return service_charge;
    }

    public void setService_charge(String service_charge) {
        this.service_charge = service_charge;
    }

    public String getPay_mode() {
        return pay_mode;
    }

    public void setPay_mode(String pay_mode) {
        this.pay_mode = pay_mode;
    }

    public String getIs_parent_or_child() {
        return is_parent_or_child;
    }

    public void setIs_parent_or_child(String is_parent_or_child) {
        this.is_parent_or_child = is_parent_or_child;
    }

    public String getCust_trans_id() {
        return cust_trans_id;
    }

    public void setCust_trans_id(String cust_trans_id) {
        this.cust_trans_id = cust_trans_id;
    }

    @Override
    public int getItemViewType() {
        return Constant.LIST_TYPE_ORDER_ON_HOLD;
    }
}
