package com.example.smartrestaurant.model.nfc;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell 5521 on 1/13/2018.
 */

public class NFCFamilyMember {
    @SerializedName("id")
    private String id;

    @SerializedName("customer_id")
    private String customerId;

    @SerializedName("cust_child_name")
    private String custChildName;

    @SerializedName("cust_child_card_no")
    private String custChildCardNo;

    @SerializedName("cust_child_picture")
    private String custChildPicture;

    @SerializedName("cust_child_signature")
    private String custChildSignature;

    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustChildName() {
        return custChildName;
    }

    public void setCustChildName(String custChildName) {
        this.custChildName = custChildName;
    }

    public String getCustChildCardNo() {
        return custChildCardNo;
    }

    public void setCustChildCardNo(String custChildCardNo) {
        this.custChildCardNo = custChildCardNo;
    }

    public String getCustChildPicture() {
        return custChildPicture;
    }

    public void setCustChildPicture(String custChildPicture) {
        this.custChildPicture = custChildPicture;
    }

    public String getCustChildSignature() {
        return custChildSignature;
    }

    public void setCustChildSignature(String custChildSignature) {
        this.custChildSignature = custChildSignature;
    }
}
