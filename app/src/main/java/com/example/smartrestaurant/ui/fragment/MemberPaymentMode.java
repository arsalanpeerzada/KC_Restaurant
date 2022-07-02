package com.example.smartrestaurant.ui.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartrestaurant.R;
import com.example.smartrestaurant.config.API;
import com.example.smartrestaurant.databinding.FrMemberPaymentModeBinding;
import com.example.smartrestaurant.databinding.FrNfcBinding;
import com.example.smartrestaurant.db_handlers.DbScript;
import com.example.smartrestaurant.model.kitchen.KitchenItemListModel;
import com.example.smartrestaurant.model.kitchen.KitchenNameModel;
import com.example.smartrestaurant.model.kitchen.OrderOnHoldModel;
import com.example.smartrestaurant.model.nfc.NFCDataModel;
import com.example.smartrestaurant.ui.base.BaseFragment;
import com.example.smartrestaurant.util.AppLog;
import com.example.smartrestaurant.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Dell 5521 on 12/26/2017.
 */

public class MemberPaymentMode extends BaseFragment {

    private FrMemberPaymentModeBinding binding;
    private OnMemberPaymentModeListener mListener;
    private NFCDataModel nfcDataModel;
    private int isParentOrChild = 0;

    public static MemberPaymentMode newInstance(NFCDataModel model){
        MemberPaymentMode mode = new MemberPaymentMode();
        mode.nfcDataModel = model;
        return mode;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnMemberPaymentModeListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fr_member_payment_mode, container, false);
        setupComponents();
        return binding.getRoot();
    }

    @Override
    public void initializeComponents() {
        binding.memberPaymentModeOrderNoTV.setText(Util.getUniqueId());
        binding.memberPaymentModeOrderNoTV.setVisibility(View.INVISIBLE);
        if(nfcDataModel != null){
            if(nfcDataModel.isChecked()){
                Picasso.with(context).load(API.CONCAT_PARENT_IMAGE+nfcDataModel.getCustCardNo()+".jpg")
                        .resize(250,250).centerCrop()
                        .placeholder(R.mipmap.dummy_profile_image).into(binding.headProfilePic);
                Picasso.with(context).load(API.CONCAT_CUS_SIGNATURE_IMAGE+nfcDataModel.getCustCardNo()+".jpg")
                        .resize(250,250).centerCrop()
                        .into(binding.imgSign);
                binding.headNameTV.setText(nfcDataModel.getCustName());
                binding.headNICNumberTV.setText(nfcDataModel.getCustCardNo());
                isParentOrChild = 0;// Parent
            }
            else {
                for (int i = 0; i < nfcDataModel.getFamilyMember().size(); i++) {
                    if(nfcDataModel.getFamilyMember().get(i).isChecked()){
                        Picasso.with(context).load(
                                API.CONCAT_DEPENDANT_IMAGE+nfcDataModel.getFamilyMember().get(i).getCustChildCardNo()+".jpg")
                                .resize(250,250).centerCrop()
                                .placeholder(R.mipmap.dummy_profile_image).into(binding.headProfilePic);
                        Picasso.with(context).load(
                                API.CONCAT_CUS__DEP_SIGN_IMAGE+nfcDataModel.getFamilyMember().get(i).getCustChildCardNo()+".jpg")
                                .resize(250,250).centerCrop()
                                .into(binding.imgSign);
                        binding.headNameTV.setText(nfcDataModel.getFamilyMember().get(i).getCustChildName());
                        binding.headNICNumberTV.setText(nfcDataModel.getFamilyMember().get(i).getCustChildCardNo());
                        isParentOrChild = 1; // Child
                        break;
                    }
                }
            }

            binding.statusTV.setText(nfcDataModel.getCustStatus());
            binding.statusTV.setTextColor(nfcDataModel.getCustStatus().equalsIgnoreCase("Available") ?
                    ContextCompat.getColor(context,R.color.colorGreen):
                    ContextCompat.getColor(context,R.color.colorRedDark));

            binding.creditAllowTV.setText(nfcDataModel.getCustCreditAllowed().equalsIgnoreCase("yes")?
                    "Allowed" : "Not Allowed");
            binding.creditAllowTV.setTextColor(nfcDataModel.getCustCreditAllowed().equalsIgnoreCase("yes")?
                    ContextCompat.getColor(context,R.color.colorGreen):
                    ContextCompat.getColor(context,R.color.colorRedDark));

            if (!nfcDataModel.getCustCreditAllowed().equalsIgnoreCase("yes")){
                binding.creditRB.setEnabled(false);
                binding.creditRB.setChecked(false);
                binding.cashRB.setEnabled(true);
                binding.cashRB.setChecked(true);
            }
        }
    }

    @Override
    public void setupListeners() {
        binding.memberPaymentModeConfirmProceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPreRequisite()) {
                    String tableNumber = "T-"+binding.memberPaymentModeTableNoTVET.getText().toString();
                    String orderNumber = binding.memberPaymentModeOrderNoTV.getText().toString();
                    List<KitchenNameModel> kitchens = DbScript.getInstance().getAllKitchens();
                    if (kitchens.size() > 0) {
                        List<KitchenItemListModel> itemsList1 = DbScript.getInstance().
                                getOrderByTableNumber(tableNumber,orderNumber,kitchens.get(0).getKtcTransId());
                        if (itemsList1.size() <= 0) {
                            for (int i = 0; i < kitchens.size(); i++) {
                                List<KitchenItemListModel> itemsList =
                                        DbScript.getInstance().getAllKitchenItems(kitchens.get(i).getKtcTransId());
                                for (int j = 0; j < itemsList.size(); j++) {
                                    KitchenItemListModel itemListModel = itemsList.get(j);
                                    itemListModel.setTable_number(tableNumber);
                                    itemListModel.setOrder_number(orderNumber);
                                    DbScript.getInstance().createNewOrder(itemListModel);
                                }
                            }
                        }
                        if(nfcDataModel != null){
                            nfcDataModel.setTableNumber(tableNumber);
                            nfcDataModel.setOrderNumber(orderNumber);
                            String payMode = "";
                            if(binding.paymentRadioGroup.getCheckedRadioButtonId() == R.id.cashRB) payMode = "CASH";
                            else payMode = "CREDIT";
                            nfcDataModel.setPayMode(payMode);
                            nfcDataModel.setServiceCharge(
                                    binding.memberPaymentModeServiceChangeET.getText().toString().isEmpty()? "0":
                                            binding.memberPaymentModeServiceChangeET.getText().toString());

                            //Requirement changes... Add order on hold as the button removed from order detail
                            OrderOnHoldModel orderOnHoldModel = new OrderOnHoldModel();
                            orderOnHoldModel.setCus_name(binding.headNameTV.getText().toString());
                            orderOnHoldModel.setCus_card_num(binding.headNICNumberTV.getText().toString());
                            orderOnHoldModel.setOrder_number(orderNumber);
                            orderOnHoldModel.setPay_mode(nfcDataModel.getPayMode());
                            orderOnHoldModel.setService_charge(nfcDataModel.getServiceCharge());
                            orderOnHoldModel.setOn_hold("yes");
                            orderOnHoldModel.setTable_number(tableNumber);
                            orderOnHoldModel.setIs_parent_or_child(String.valueOf(isParentOrChild));
                            orderOnHoldModel.setCust_trans_id(nfcDataModel.getCustMainId());
                            DbScript.getInstance().insertNewOrderToOnHoldTable(orderOnHoldModel);
                        }
//                        mListener.onMemberPaymentModeListen(tableNumber,nfcDataModel);
                        mListener.onMemberPaymentModeListen(nfcDataModel);
                    } else {
                        AppLog.toast("No Kitchens available.");
                    }
                }
            }
        });
        binding.memberPaymentModeConfirmBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popStack();
            }
        });
    }

    private boolean isPreRequisite() {
        if (binding.memberPaymentModeTableNoTVET.getText().toString().isEmpty()) {
            AppLog.toast("Please enter table number");
            return false;
        }
        /*if (binding.memberPaymentModeServiceChangeET.getText().toString().isEmpty()) {
            AppLog.toast("Please enter service charge");
            return false;
        }*/
        /*if (binding.memberPaymentModeServiceChangeET.getText().toString().equalsIgnoreCase("0")) {
            AppLog.toast("Service charge can not be 0%");
            return false;
        }*/
        return true;
    }

    public interface OnMemberPaymentModeListener {
        void onMemberPaymentModeListen( NFCDataModel nfcDataModel);
    }
}
