package com.example.smartrestaurant.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartrestaurant.AppClass;
import com.example.smartrestaurant.R;
import com.example.smartrestaurant.config.API;
import com.example.smartrestaurant.config.BodyParams;
import com.example.smartrestaurant.databinding.FrHomeBinding;
import com.example.smartrestaurant.db_handlers.DbScript;
import com.example.smartrestaurant.listener.Callback;
import com.example.smartrestaurant.model.AuthResponseModel;
import com.example.smartrestaurant.model.TransactionKitchenItemsModel;
import com.example.smartrestaurant.model.kitchen.KitchenDataModel;
import com.example.smartrestaurant.model.kitchen.KitchenNameModel;
import com.example.smartrestaurant.network.InternetConnection;
import com.example.smartrestaurant.network.RequestHandler;
import com.example.smartrestaurant.ui.activity.Authentication;
import com.example.smartrestaurant.ui.activity.MainActivity;
import com.example.smartrestaurant.ui.base.BaseFragment;
import com.example.smartrestaurant.util.AppLog;
import com.example.smartrestaurant.util.SharedPrefMgr;
import com.example.smartrestaurant.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

/**
 * Created by Dell 5521 on 12/26/2017.
 */

public class Home extends BaseFragment {

    AuthResponseModel authResponseModel;

    private FrHomeBinding binding;
    private OnHomeListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnHomeListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fr_home, container, false);
        setupComponents();
        return binding.getRoot();
    }

    @Override
    public void initializeComponents() {
        if(AppClass.getProfile() != null){
            Picasso.with(context).load(API.CONCAT_WAITER_IMAGE+AppClass.getProfile().getUserMainId()+".jpg")
                    .resize(400, 400).centerCrop()
                    .placeholder(R.mipmap.dummy_profile_image).into(binding.waiterImg);
            binding.homewaitername.setText(AppClass.getProfile().getUserFullName());
            binding.homeLocationTV.setText(AppClass.getProfile().getLocationName());
            /*switch (AppClass.getProfile().getLocation()){
                case "1":
                    binding.homeLocationTV.setText("Dine In");
                    break;
                case "2":
                    binding.homeLocationTV.setText("Swimming Pool");
                    break;
                case "3":
                    binding.homeLocationTV.setText("Library");
                    break;

            }*/
        }
//        if (InternetConnection.isOnline()) {
//            if(MainActivity.isNewKitchensAndItemsFetched)
//                getKitchenItems();
//        }
//        else showConnectionErrorMsg();
    }

    private void getKitchenItems() {
//        MainActivity.isNewKitchensAndItemsFetched = false;
//        showDialog();
        String rest_id = AppClass.getUserRestId();
        Map<String, String> map = BodyParams.setRestautant(rest_id);
        RequestHandler.getKitchenItems(map,new Callback<KitchenDataModel>() {
            @Override
            public void invoke(KitchenDataModel obj) {
                if (obj != null) {
                    TransactionKitchenItemsModel.getInstance().setKitchenDataModel(obj);
                    saveKitchensToLocalDB(obj.getData());
                } else {
                    AppLog.toast("Request Failed");
                }
            }

            @Override
            public void alreadyLogin() {
                Util.showAlreadyLoginDialog(context);
            }

            @Override
            public void _422(String msg) {
                AppLog.toast(msg);
            }
        });
    }

    private void saveKitchensToLocalDB(List<KitchenNameModel> data) {
//        if (DbScript.getInstance().getAllKitchens().size() <= 0 ||
//                DbScript.getInstance().getAllKitchens().size() < data.size()) {
            DbScript.getInstance().clearAllKitchen();
            for (int i = 0; i < data.size(); i++) {
                if (DbScript.getInstance().saveKitchens(data.get(i))) {
                    for (int j = 0; j < data.get(i).getItemList().size(); j++) {
                        DbScript.getInstance().saveKitchenItems(data.get(i).getItemList().get(j));
                    }
                }
            }
//        }
        /*else if(DbScript.getInstance().getAllKitchens().size() < data.size()) {
            DbScript.getInstance().clearAllKitchen();
            for (int i = 0; i < data.size(); i++) {
                if (DbScript.getInstance().saveKitchens(data.get(i))) {
                    for (int j = 0; j < data.get(i).getItemList().size(); j++) {
                        DbScript.getInstance().saveKitchenItems(data.get(i).getItemList().get(j));
                    }
                }
            }
        }*/
    }

    @Override
    public void setupListeners() {
        binding.newOrderRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onHomeListen(1);
            }
        });

        binding.myOrderRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onHomeListen(2);
            }
        });
        binding.rlNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onHomeListen(3);
            }
        });
    }

    public interface OnHomeListener {
        void onHomeListen(int fragConst);
    }

}
