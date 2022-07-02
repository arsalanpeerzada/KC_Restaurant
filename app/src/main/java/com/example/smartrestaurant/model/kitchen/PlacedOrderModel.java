package com.example.smartrestaurant.model.kitchen;

import com.example.smartrestaurant.config.Constant;
import com.example.smartrestaurant.ui.adapter.base.RecyclerViewBaseAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Dell 5521 on 1/17/2018.
 */
/*{"om_main_id":"331","cust_trans_id":"A-000001-1","user_trans_id":"2","table_no":"T-10",
"om_order_status":"1","om_grand_total":"147","om_opening_time":"2018-02-20 07:22:42","om_closing_time":null,
"om_payment_method":"CREDIT","location_id":"2","session_id":"406","service_charges":"10","child_id":null,
"is_cust_type":"0","id":"1","customer_id":"1","cust_child_name":"Katrina_khaif",
"cust_child_card_no":"A-000001-1","cust_child_picture":"1","cust_child_signature":"1","credit_allowed":null,
"order_detail":[{"od_main_id":"984","master_id":"331","item_trans_id":"1","item_qty":"2",
"item_cooking_status":"1","item_taking_time":"2018-02-20 07:30:12","item_dispatch_time":null,
"item_serve_time":null,"remarks":"null","item_main_id":"6","cat_trans_id":"2","ktc_trans_id":"1",
"item_name":"Chicken Tikka","item_price":"147","item_sale_price":"190","item_image":null,"item_unit":"6",
"item_sync_date":"2018-01-03 11:46:08","item_avail":null}]}*/
public class PlacedOrderModel implements RecyclerViewBaseAdapter.AdapterDataType {

    @SerializedName("child_id")
    private String childId;

    @SerializedName("is_cust_type")
    private String is_cust_type;

    @SerializedName("id")
    private String id;

    @SerializedName("customer_id")
    private String customer_id;

    @SerializedName("cust_child_name")
    private String cust_child_name;

    @SerializedName("cust_child_card_no")
    private String cust_child_card_no;

    @SerializedName("cust_child_picture")
    private String cust_child_picture;

    @SerializedName("cust_child_signature")
    private String cust_child_signature;

    @SerializedName("credit_allowed")
    private String credit_allowed;

    @SerializedName("om_main_id")

    private String omMainId;
    @SerializedName("cust_trans_id")
    
    private String custTransId;
    @SerializedName("user_trans_id")
    
    private String userTransId;
    @SerializedName("table_no")
    
    private String tableNo;
    @SerializedName("om_order_status")
    
    private String omOrderStatus;
    @SerializedName("om_grand_total")
    
    private String omGrandTotal;
    @SerializedName("om_opening_time")
    
    private String omOpeningTime;
    @SerializedName("om_closing_time")
    
    private String omClosingTime;
    @SerializedName("om_payment_method")
    
    private String omPaymentMethod;
    @SerializedName("location_id")
    
    private String locationId;
    @SerializedName("session_id")
    
    private String sessionId;
    @SerializedName("cust_main_id")
    
    private String custMainId;
    @SerializedName("cust_name")
    
    private String custName;
    @SerializedName("cust_card_no")
    
    private String custCardNo;
    @SerializedName("cust_address")
    
    private String custAddress;
    @SerializedName("cust_status")
    
    private String custStatus;
    @SerializedName("cust_system_date")
    
    private String custSystemDate;
    @SerializedName("cust_sync_date")
    
    private String custSyncDate;

    @SerializedName("cust_credit_allowed")
    private String custCreditAllowed;

    @SerializedName("service_charges")
    private String serviceCharges;

    @SerializedName("cust_total_credit_amount")
    private String custTotalCreditAmount;

    @SerializedName("order_detail")
    private List<PlacedOrderSingleItem> orderDetail = null;

    public String getOmMainId() {
        return omMainId;
    }

    public void setOmMainId(String omMainId) {
        this.omMainId = omMainId;
    }

    public String getServiceCharges() {
        return serviceCharges;
    }

    public void setServiceCharges(String serviceCharges) {
        this.serviceCharges = serviceCharges;
    }

    public String getCustTransId() {
        return custTransId;
    }

    public void setCustTransId(String custTransId) {
        this.custTransId = custTransId;
    }

    public String getUserTransId() {
        return userTransId;
    }

    public void setUserTransId(String userTransId) {
        this.userTransId = userTransId;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String getOmOrderStatus() {
        return omOrderStatus;
    }

    public void setOmOrderStatus(String omOrderStatus) {
        this.omOrderStatus = omOrderStatus;
    }

    public String getOmGrandTotal() {
        return omGrandTotal;
    }

    public void setOmGrandTotal(String omGrandTotal) {
        this.omGrandTotal = omGrandTotal;
    }

    public String getOmOpeningTime() {
        return omOpeningTime;
    }

    public void setOmOpeningTime(String omOpeningTime) {
        this.omOpeningTime = omOpeningTime;
    }

    public String getOmClosingTime() {
        return omClosingTime;
    }

    public void setOmClosingTime(String omClosingTime) {
        this.omClosingTime = omClosingTime;
    }

    public String getOmPaymentMethod() {
        return omPaymentMethod;
    }

    public void setOmPaymentMethod(String omPaymentMethod) {
        this.omPaymentMethod = omPaymentMethod;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCustMainId() {
        return custMainId;
    }

    public void setCustMainId(String custMainId) {
        this.custMainId = custMainId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustCardNo() {
        return custCardNo;
    }

    public void setCustCardNo(String custCardNo) {
        this.custCardNo = custCardNo;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public String getCustStatus() {
        return custStatus;
    }

    public void setCustStatus(String custStatus) {
        this.custStatus = custStatus;
    }

    public String getCustSystemDate() {
        return custSystemDate;
    }

    public void setCustSystemDate(String custSystemDate) {
        this.custSystemDate = custSystemDate;
    }

    public String getCustSyncDate() {
        return custSyncDate;
    }

    public void setCustSyncDate(String custSyncDate) {
        this.custSyncDate = custSyncDate;
    }

    public String getCustCreditAllowed() {
        return custCreditAllowed;
    }

    public void setCustCreditAllowed(String custCreditAllowed) {
        this.custCreditAllowed = custCreditAllowed;
    }

    public String getCustTotalCreditAmount() {
        return custTotalCreditAmount;
    }

    public void setCustTotalCreditAmount(String custTotalCreditAmount) {
        this.custTotalCreditAmount = custTotalCreditAmount;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getIs_cust_type() {
        return is_cust_type;
    }

    public void setIs_cust_type(String is_cust_type) {
        this.is_cust_type = is_cust_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCust_child_name() {
        return cust_child_name;
    }

    public void setCust_child_name(String cust_child_name) {
        this.cust_child_name = cust_child_name;
    }

    public String getCust_child_card_no() {
        return cust_child_card_no;
    }

    public void setCust_child_card_no(String cust_child_card_no) {
        this.cust_child_card_no = cust_child_card_no;
    }

    public String getCust_child_picture() {
        return cust_child_picture;
    }

    public void setCust_child_picture(String cust_child_picture) {
        this.cust_child_picture = cust_child_picture;
    }

    public String getCust_child_signature() {
        return cust_child_signature;
    }

    public void setCust_child_signature(String cust_child_signature) {
        this.cust_child_signature = cust_child_signature;
    }

    public String getCredit_allowed() {
        return credit_allowed;
    }

    public void setCredit_allowed(String credit_allowed) {
        this.credit_allowed = credit_allowed;
    }

    public List<PlacedOrderSingleItem> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(List<PlacedOrderSingleItem> orderDetail) {
        this.orderDetail = orderDetail;
    }

    @Override
    public int getItemViewType() {
        return Constant.LIST_TYPE_ORDER_PLACED;
    }
}
