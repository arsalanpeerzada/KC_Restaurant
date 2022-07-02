package com.example.smartrestaurant.model;

import com.example.smartrestaurant.config.API;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell 5521 on 1/6/2018.
 */

public class ProfileModel {

    @SerializedName("user_trans_id")
    private String userTransId;

    @SerializedName("user_main_id")
    private String userMainId;

    @SerializedName("user_username")
    private String userUsername;

    @SerializedName("user_full_name")
    private String userFullName;

    @SerializedName("user_email")
    private String userEmail;

    @SerializedName("user_password")
    private String userPassword;

    @SerializedName("user_dob")
    private String userDob;

    @SerializedName("user_date_of_joining")
    private String userDateOfJoining;

    @SerializedName("ut_trans_id")
    private String utTransId;

    @SerializedName("user_is_login")
    private String userIsLogin;

    @SerializedName("user_last_login_date")
    private String userLastLoginDate;

    @SerializedName("user_shift_start_time")
    private String userShiftStartTime;

    @SerializedName("user_shift_end_time")
    private String userShiftEndTime;

    @SerializedName("user_status")
    private String userStatus;

    @SerializedName("user_sync_date")
    private String userSyncDate;

    @SerializedName("user_system_date")
    private String userSystemDate;

    @SerializedName("is_kitchen")
    private String isKitchen;

    @SerializedName("is_captain")
    private String isCaptain;

    @SerializedName("is_admin")
    private String isAdmin;

    @SerializedName("user_type")
    private String userType;

    @SerializedName("location")
    private String location;

    @SerializedName("name")
    private String locationName;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    private String image;

    public String getImage() {
        return API.PROFILE+"/"+getUserMainId();
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserTransId() {
        return userTransId;
    }

    public void setUserTransId(String userTransId) {
        this.userTransId = userTransId;
    }

    public String getUserMainId() {
        return userMainId;
    }

    public void setUserMainId(String userMainId) {
        this.userMainId = userMainId;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserDob() {
        return userDob;
    }

    public void setUserDob(String userDob) {
        this.userDob = userDob;
    }

    public String getUserDateOfJoining() {
        return userDateOfJoining;
    }

    public void setUserDateOfJoining(String userDateOfJoining) {
        this.userDateOfJoining = userDateOfJoining;
    }

    public String getUtTransId() {
        return utTransId;
    }

    public void setUtTransId(String utTransId) {
        this.utTransId = utTransId;
    }

    public String getUserIsLogin() {
        return userIsLogin;
    }

    public void setUserIsLogin(String userIsLogin) {
        this.userIsLogin = userIsLogin;
    }

    public String getUserLastLoginDate() {
        return userLastLoginDate;
    }

    public void setUserLastLoginDate(String userLastLoginDate) {
        this.userLastLoginDate = userLastLoginDate;
    }

    public String getUserShiftStartTime() {
        return userShiftStartTime;
    }

    public void setUserShiftStartTime(String userShiftStartTime) {
        this.userShiftStartTime = userShiftStartTime;
    }

    public String getUserShiftEndTime() {
        return userShiftEndTime;
    }

    public void setUserShiftEndTime(String userShiftEndTime) {
        this.userShiftEndTime = userShiftEndTime;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserSyncDate() {
        return userSyncDate;
    }

    public void setUserSyncDate(String userSyncDate) {
        this.userSyncDate = userSyncDate;
    }

    public String getUserSystemDate() {
        return userSystemDate;
    }

    public void setUserSystemDate(String userSystemDate) {
        this.userSystemDate = userSystemDate;
    }

    public String getIsKitchen() {
        return isKitchen;
    }

    public void setIsKitchen(String isKitchen) {
        this.isKitchen = isKitchen;
    }

    public String getIsCaptain() {
        return isCaptain;
    }

    public void setIsCaptain(String isCaptain) {
        this.isCaptain = isCaptain;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
