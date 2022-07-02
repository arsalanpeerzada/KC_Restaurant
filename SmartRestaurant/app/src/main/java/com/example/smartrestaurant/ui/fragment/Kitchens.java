package com.example.smartrestaurant.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartrestaurant.AppClass;
import com.example.smartrestaurant.R;
import com.example.smartrestaurant.config.BodyParams;
import com.example.smartrestaurant.config.Constant;
import com.example.smartrestaurant.databinding.FrKitchensBinding;
import com.example.smartrestaurant.databinding.FrNfcBinding;
import com.example.smartrestaurant.databinding.LiItemKitchenItemsBinding;
import com.example.smartrestaurant.databinding.LiItemKitchenMenuBinding;
import com.example.smartrestaurant.handler.FragmentHandler;
import com.example.smartrestaurant.listener.Callback;
import com.example.smartrestaurant.model.AuthResponseModel;
import com.example.smartrestaurant.model.ProfileModel;
import com.example.smartrestaurant.model.TransactionKitchenItemsModel;
import com.example.smartrestaurant.model.kitchen.KitchenDataModel;
import com.example.smartrestaurant.model.kitchen.KitchenItemListModel;
import com.example.smartrestaurant.model.kitchen.KitchenNameModel;
import com.example.smartrestaurant.network.InternetConnection;
import com.example.smartrestaurant.network.RequestHandler;
import com.example.smartrestaurant.ui.activity.Authentication;
import com.example.smartrestaurant.ui.adapter.KitchenAdapter;
import com.example.smartrestaurant.ui.base.BaseFragment;
import com.example.smartrestaurant.util.AppLog;
import com.example.smartrestaurant.util.SharedPrefMgr;
import com.example.smartrestaurant.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Dell 5521 on 12/26/2017.
 */

public class Kitchens extends BaseFragment {

    private FrKitchensBinding binding;
    private OnKitchensListener mListener;
    private static Kitchens kitchens;
    private FragmentHandler fragmentHandler;
    private String ktc_trans_id = "-1";
    AuthResponseModel authResponseModel;
    List<KitchenNameModel> menuitems = new ArrayList<>();
    public static Kitchens newInstance(String ktc_trans_id){
        Kitchens kitchens = new Kitchens();
        kitchens.ktc_trans_id = ktc_trans_id;
        return kitchens;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnKitchensListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fr_kitchens,container,false);
        fragmentHandler = new FragmentHandler(R.id.kitchenItemsContainerFL,getActivity());
        setupComponents();
        return binding.getRoot();
    }

    @Override
    public void initializeComponents() {
        mListener.onKitchensListen(Constant.KITCHEN_INFLATE_ITEM_FRAGMENT,fragmentHandler);
        if(InternetConnection.isOnline()) getKitchenItems();
        else showConnectionErrorMsg();
    }

    @Override
    public void setupListeners() {
        binding.kitchensDoneIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onKitchensListen(Constant.KITCHEN_ORDER_DETAIL,fragmentHandler);
            }
        });
        binding.kitchensBackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popStack();
            }
        });
    }

    public interface OnKitchensListener{
        void onKitchensListen(int fragConst, FragmentHandler fragmentHandler);
    }

    private void setupKitchen(List<KitchenNameModel> list) {
        menuitems.clear();
        menuitems.addAll(list);
        binding.kitchenRV.setAdapter(new KitchensAdapter(menuitems));
        binding.kitchenRV.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        TransactionKitchenItemsModel.getInstance().setListItems(list.get(0).getItemList());
        mListener.onKitchensListen(Constant.KITCHEN_UPDATE_LIST_ITEM,fragmentHandler);
    }

    private void callListener(List<KitchenItemListModel> listItems){
        TransactionKitchenItemsModel.getInstance().setListItems(listItems);
        mListener.onKitchensListen(Constant.KITCHEN_UPDATE_LIST_ITEM,null);
    }

    private void getKitchenItems(){
        showDialog();
        String rest_id = AppClass.getUserRestId();
        Map<String, String> map = BodyParams.setRestautant(rest_id);
        RequestHandler.getKitchenItems(map,new Callback<KitchenDataModel>() {
            @Override
            public void invoke(KitchenDataModel obj) {
                if(obj != null){
                    setupKitchen(obj.getData());
                } else {
                    AppLog.toast("Please check your internet connection.");
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

    public class KitchensAdapter extends RecyclerView.Adapter<KitchensAdapter.ViewHolderKitchen>{
        List<KitchenNameModel> list;
        public KitchensAdapter(List<KitchenNameModel> list){
            this.list = list;
        }
        @Override
        public ViewHolderKitchen onCreateViewHolder(ViewGroup parent, int viewType) {
            LiItemKitchenMenuBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(getContext()),R.layout.li_item_kitchen_menu,parent,false);
            return new ViewHolderKitchen(binding);
        }

        @Override
        public void onBindViewHolder(ViewHolderKitchen holder, int position) {
            holder.setData(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolderKitchen extends RecyclerView.ViewHolder{

            LiItemKitchenMenuBinding bind;
            public ViewHolderKitchen(LiItemKitchenMenuBinding binding) {
                super(binding.getRoot());
                this.bind = binding;
            }
            public void setData(final KitchenNameModel model){
                bind.menuItemTV.setText(model.getKtcName());
                bind.menuItemTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callListener(model.getItemList());
                    }
                });
            }
        }
    }
}
