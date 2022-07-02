package com.example.smartrestaurant.model.nfc;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Dell 5521 on 1/13/2018.
 */

public class NFCDataModel {

    @SerializedName("cust_trans_id")
    private String custTransId;

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

    @SerializedName("cust_total_credit_amount")
    private String custTotalCreditAmount;

    @SerializedName("family_member")
    private List<NFCFamilyMember> familyMember = null;

    private boolean isChecked;
    private String tableNumber,orderNumber,serviceCharge,payMode;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getCustTransId() {
        return custTransId;
    }

    public void setCustTransId(String custTransId) {
        this.custTransId = custTransId;
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

    public List<NFCFamilyMember> getFamilyMember() {
        return familyMember;
    }

    public void setFamilyMember(List<NFCFamilyMember> familyMember) {
        this.familyMember = familyMember;
    }

}
