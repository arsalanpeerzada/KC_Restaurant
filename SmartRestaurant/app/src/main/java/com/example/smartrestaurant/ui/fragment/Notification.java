package com.example.smartrestaurant.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.smartrestaurant.AppClass;
import com.example.smartrestaurant.R;
import com.example.smartrestaurant.config.BodyParams;
import com.example.smartrestaurant.config.Constant;
import com.example.smartrestaurant.databinding.FrNotificationBinding;
import com.example.smartrestaurant.databinding.LiNotificationLebelBinding;
import com.example.smartrestaurant.databinding.LiNotificationsListItemBinding;
import com.example.smartrestaurant.db_handlers.DbScript;
import com.example.smartrestaurant.listener.Callback;
import com.example.smartrestaurant.model.NotificationItemModel;
import com.example.smartrestaurant.model.NotificationLabelModel;
import com.example.smartrestaurant.model.NotificationResponseModel;
import com.example.smartrestaurant.model.kitchen.PlacedOrderGetResponse;
import com.example.smartrestaurant.model.kitchen.PlacedOrderModel;
import com.example.smartrestaurant.model.kitchen.PlacedOrderSingleItem;
import com.example.smartrestaurant.network.InternetConnection;
import com.example.smartrestaurant.network.RequestHandler;
import com.example.smartrestaurant.ui.activity.Authentication;
import com.example.smartrestaurant.ui.adapter.base.GeneralViewHolder;
import com.example.smartrestaurant.ui.adapter.base.RecyclerViewBaseAdapter;
import com.example.smartrestaurant.ui.adapter.base.RecyclerViewGeneralAdapter;
import com.example.smartrestaurant.ui.adapter.base.ViewHolderFactory;
import com.example.smartrestaurant.ui.adapter.viewholder.NotificationListLabelViewHolder;
import com.example.smartrestaurant.ui.adapter.viewholder.NotificationListViewHolder;
import com.example.smartrestaurant.ui.adapter.viewholder.OrderListPlacedViewHolder;
import com.example.smartrestaurant.ui.base.BaseFragment;
import com.example.smartrestaurant.util.AppLog;
import com.example.smartrestaurant.util.SharedPrefMgr;
import com.example.smartrestaurant.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Dell 5521 on 12/26/2017.
 */

public class Notification extends BaseFragment {

    public static final int SORT_BY_TABLE = 0;
    public static final int SORT_BY_ITEM = 1;
    public static final int SORT_BY_KITCHEN = 2;

    private FrNotificationBinding binding;
    private NotificationListener mListener;
    RecyclerViewGeneralAdapter<RecyclerViewBaseAdapter.AdapterDataType> adapter;
    List<RecyclerViewBaseAdapter.AdapterDataType> list = new ArrayList<>();
    List<NotificationItemModel> itemList;
    int displayType = SORT_BY_TABLE;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (NotificationListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fr_notification,container,false);
        setAdapter(list);
        setupComponents();
        return binding.getRoot();
    }

    @Override
    public void initializeComponents() {
        if(InternetConnection.isOnline()) getNotifications();
        else showConnectionErrorMsg();
    }

    private void setAdapter(List<RecyclerViewBaseAdapter.AdapterDataType> list) {
        createAdapter(list);
        binding.notificationLV.setAdapter(adapter);
        binding.notificationLV.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.notificationLV.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
    }

    private void createAdapter(List<RecyclerViewBaseAdapter.AdapterDataType> list){
        adapter = new RecyclerViewGeneralAdapter<>(list, new ViewHolderFactory<RecyclerViewBaseAdapter.AdapterDataType>() {
            @Override
            public int getItemViewType(int position, List<RecyclerViewBaseAdapter.AdapterDataType> list) {
                return list.get(position).getItemViewType();
            }

            @Override
            public GeneralViewHolder<RecyclerViewBaseAdapter.AdapterDataType> createViewHolder(ViewGroup parent, int viewType) {
                switch (viewType) {
                    case Constant.LIST_TYPE_NOTIFICATION_LABEL:
                        LiNotificationLebelBinding b1 = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                                R.layout.li_notification_lebel,
                                parent,
                                false
                        );

                        return new NotificationListLabelViewHolder(b1);

                    case Constant.LIST_TYPE_NOTIFICATION_ITEM:
                        LiNotificationsListItemBinding b = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                                R.layout.li_notifications_list_item,
                                parent,
                                false
                        );

                        return new NotificationListViewHolder(displayType, b, new RecyclerViewBaseAdapter.EventCallback<NotificationListViewHolder.Tap, NotificationItemModel>() {
                            @Override
                            public void onEvent(NotificationListViewHolder.Tap event, NotificationItemModel data) {
                                if(InternetConnection.isOnline())hitServed(data);
                                else showConnectionErrorMsg();
                            }
                        });
                }
                return null;
            }
        });
    }

    public void getNotifications(){
        showDialog();
        RequestHandler.getNotifications(new Callback<NotificationResponseModel>() {
            @Override
            public void invoke(NotificationResponseModel obj) {
                if(obj != null){
                    setupAdapter(obj);
                } else {
                    AppLog.toast("Request Failed.");
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

    private void setupAdapter(NotificationResponseModel placedOrderGetResponse) {
        if(itemList != null)
            itemList.clear();
        itemList = placedOrderGetResponse.getData();
        list.clear();
//        list.addAll(placedOrderGetResponse.getData());
        if (itemList.size() >= 0){
            switch (displayType){
                case SORT_BY_TABLE:
                    filterByTable();
                    break;
                case SORT_BY_KITCHEN:
                    filterByKitchen();
                    break;
                case SORT_BY_ITEM:
                    filterByItem();
                    break;
            }
            updateNotificationTable(itemList);
        }
        else
            AppLog.toast("No Notifications");
//            binding.noOrdersTakenTV.setVisibility(View.VISIBLE);
    }

    private void updateNotificationTable(List<NotificationItemModel> list){
        DbScript.getInstance().clearAllNotification();
        for (int i = 0; i < list.size(); i++) {
            DbScript.getInstance().saveNotification(list.get(i));
        }
    }

    @Override
    public void setupListeners() {
        binding.notiItemTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterByItem();
            }
        });
        binding.notiTableTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterByTable();
            }
        });
        binding.notiKitchenTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterByKitchen();
            }
        });
    }

    private void filterByItem(){
        displayType = SORT_BY_ITEM;
        binding.notiItemIndicator.setVisibility(View.VISIBLE);
        binding.notiTableIndicator.setVisibility(View.GONE);
        binding.notiKitchenIndicator.setVisibility(View.GONE);
        if(itemList != null && itemList.size() >= 0){
            list.clear();
            Collections.sort(itemList,new Comparator<NotificationItemModel>() {
                @Override
                public int compare(NotificationItemModel notificationItemModel, NotificationItemModel t1) {
                    return notificationItemModel.getItemName().compareTo(t1.getItemName());
                }
            });
            String previousItemName = "";
            for (int i = 0; i < itemList.size(); i++) {
                adapter.notifyDataSetChanged();
                if(!previousItemName.equalsIgnoreCase(itemList.get(i).getItemName())){
                    previousItemName = itemList.get(i).getItemName();
                    NotificationLabelModel labelModel = new NotificationLabelModel();
                    labelModel.setTitleText(itemList.get(i).getItemName());
                    list.add(labelModel);
                }
                list.add(itemList.get(i));
            }
        }
        adapter.notifyDataSetChanged();
        createAdapter(list);
        binding.notificationLV.setAdapter(adapter);
    }

    private void filterByTable(){
        displayType = SORT_BY_TABLE;
        binding.notiItemIndicator.setVisibility(View.GONE);
        binding.notiTableIndicator.setVisibility(View.VISIBLE);
        binding.notiKitchenIndicator.setVisibility(View.GONE);
        if(itemList != null && itemList.size() >= 0){
            list.clear();
            Collections.sort(itemList,new Comparator<NotificationItemModel>() {
                @Override
                public int compare(NotificationItemModel notificationItemModel, NotificationItemModel t1) {
                    return notificationItemModel.getTableNo().compareTo(t1.getTableNo());
                }
            });
            String previousTableNumber = "";
            for (int i = 0; i < itemList.size(); i++) {
                adapter.notifyDataSetChanged();
                if(!previousTableNumber.equalsIgnoreCase(itemList.get(i).getTableNo())){
                    previousTableNumber = itemList.get(i).getTableNo();
                    NotificationLabelModel labelModel = new NotificationLabelModel();
                    labelModel.setTitleText("Table # "+itemList.get(i).getTableNo());
                    list.add(labelModel);

                }
                list.add(itemList.get(i));
            }
        }

        /*if(itemList != null && itemList.size() > 0){
            list.clear();
            String previousTableNumber = "";
            for (int i = 0; i < itemList.size(); i++) {
                //order status not completed
                if(!itemList.get(i).getOmOrderStatus().equalsIgnoreCase("6")){
                    if(!previousTableNumber.equalsIgnoreCase(itemList.get(i).getTableNo())){
                        previousTableNumber = itemList.get(i).getTableNo();
                        NotificationLabelModel labelModel = new NotificationLabelModel();
                        labelModel.setTitleText("Table # "+itemList.get(i).getTableNo());
                        list.add(labelModel);
                        for (int j = 0; j < itemList.get(i).getOrderDetail().size(); j++) {
                            NotificationItemModel model = new NotificationItemModel();
                            model.setOdMainId(itemList.get(i).getOrderDetail().get(j).getOdMainId());
                            model.setTitle(itemList.get(i).getOrderDetail().get(j).getItemName());
                            model.setQty("Qty. "+itemList.get(i).getOrderDetail().get(j).getItemQty());
                            list.add(model);
                        }
                    }
                }
            }
        }*/
        adapter.notifyDataSetChanged();
        createAdapter(list);
        binding.notificationLV.setAdapter(adapter);
    }

    private void filterByKitchen(){
        displayType = SORT_BY_KITCHEN;
        binding.notiItemIndicator.setVisibility(View.GONE);
        binding.notiTableIndicator.setVisibility(View.GONE);
        binding.notiKitchenIndicator.setVisibility(View.VISIBLE);
        if(itemList != null && itemList.size() >= 0){
            list.clear();
            Collections.sort(itemList,new Comparator<NotificationItemModel>() {
                @Override
                public int compare(NotificationItemModel notificationItemModel, NotificationItemModel t1) {
                    return notificationItemModel.getKtcName().compareTo(t1.getKtcName());
                }
            });
            String previousKitchenName = "";
            for (int i = 0; i < itemList.size(); i++) {
                adapter.notifyDataSetChanged();
                if(!previousKitchenName.equalsIgnoreCase(itemList.get(i).getKtcName())){
                    previousKitchenName = itemList.get(i).getKtcName();
                    NotificationLabelModel labelModel = new NotificationLabelModel();
                    labelModel.setTitleText(itemList.get(i).getKtcName());
                    list.add(labelModel);
                }
                list.add(itemList.get(i));
            }
        }
        adapter.notifyDataSetChanged();
        createAdapter(list);
        binding.notificationLV.setAdapter(adapter);
    }

    private void hitServed(NotificationItemModel data){
        showDialog();
        Map<String, String> map = BodyParams.servedItem(data.getOrderNo(),data.getItemId(),data.getOdmainID());
        RequestHandler.servedItems(map, new Callback<PlacedOrderModel>() {
            @Override
            public void invoke(PlacedOrderModel obj) {
                if(obj != null){
                    AppLog.toast("Item has served successfully.");
                    getNotifications();
                } else{
                    AppLog.toast("Request Failed.");
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

    public interface NotificationListener{
        void onNotificationListen(int fragConst);
    }
}
