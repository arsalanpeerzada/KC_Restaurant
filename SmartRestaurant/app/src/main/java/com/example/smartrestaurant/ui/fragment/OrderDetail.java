package com.example.smartrestaurant.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smartrestaurant.AppClass;
import com.example.smartrestaurant.R;
import com.example.smartrestaurant.config.API;
import com.example.smartrestaurant.config.BodyParams;
import com.example.smartrestaurant.config.Constant;
import com.example.smartrestaurant.databinding.FrOrdercartDetailBinding;
import com.example.smartrestaurant.db_handlers.DbScript;
import com.example.smartrestaurant.listener.Callback;
import com.example.smartrestaurant.model.kitchen.KitchenItemListModel;
import com.example.smartrestaurant.model.kitchen.OrderOnHoldModel;
import com.example.smartrestaurant.model.kitchen.PlacedOrderModel;
import com.example.smartrestaurant.model.nfc.NFCDataModel;
import com.example.smartrestaurant.network.InternetConnection;
import com.example.smartrestaurant.network.RequestHandler;
import com.example.smartrestaurant.ui.base.BaseFragment;
import com.example.smartrestaurant.util.AppLog;
import com.example.smartrestaurant.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dell 5521 on 12/26/2017.
 */

public class OrderDetail extends BaseFragment {

    String TAG = "Error";

    private FrOrdercartDetailBinding binding;
    private OnOrderDetailListener mListener;
    private String tableNumber;
    private NFCDataModel nfcDataModel;
    private OrderOnHoldModel orderOnHoldModel;
    private boolean isFromOrderList;
    private float cost;
    private float serviceAmount = 1;
    private int isParentOfChild = 0;
    private String custMainId = "-1";
    private boolean isOrderPlaceBtnTapped;

    /*public static OrderDetail newInstance(String tableNumber, NFCDataModel nfcDataModel) {
        OrderDetail detail = new OrderDetail();
        detail.tableNumber = tableNumber;
        detail.nfcDataModel = nfcDataModel;
        return detail;
    }*/
    public static OrderDetail newInstance(boolean isFromOrderList, NFCDataModel nfcDataModel) {
        OrderDetail detail = new OrderDetail();
        detail.isFromOrderList = isFromOrderList;
        detail.nfcDataModel = nfcDataModel;
        return detail;
    }

    public static OrderDetail newInstance(boolean isFromOrderList, OrderOnHoldModel orderOnHoldModel) {
        OrderDetail detail = new OrderDetail();
        detail.isFromOrderList = isFromOrderList;
        detail.orderOnHoldModel = orderOnHoldModel;
        return detail;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnOrderDetailListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fr_ordercart_detail, container, false);
        setupComponents();
        return binding.getRoot();
    }

    @Override
    public void initializeComponents() {
        binding.orderCloseBtn.setVisibility(View.GONE);
        binding.orderHoldBtn.setVisibility(View.GONE);
        binding.orderCartOrderNo.setVisibility(View.INVISIBLE);
        if (isFromOrderList && orderOnHoldModel != null) {
            custMainId = orderOnHoldModel.getCust_trans_id();
            isParentOfChild = Integer.parseInt(orderOnHoldModel.getIs_parent_or_child());
            binding.orderCartCName.setText(orderOnHoldModel.getCus_name());
            binding.orderCartCNo.setText(orderOnHoldModel.getCus_card_num());
            binding.orderCartTableNo.setText(orderOnHoldModel.getTable_number());
            binding.orderCartOrderNo.setText(orderOnHoldModel.getOrder_number());
            binding.orderCartServiceCharge.setText(orderOnHoldModel.getService_charge() + "%");
            binding.orderCartPayMode.setText(orderOnHoldModel.getPay_mode());
            serviceAmount = Float.parseFloat(orderOnHoldModel.getService_charge());
            getAllItemsOfSpecificTable(orderOnHoldModel.getTable_number(), orderOnHoldModel.getOrder_number());
        } else if (!isFromOrderList && nfcDataModel != null) {
            //parent selected
            if (nfcDataModel.isChecked()) {
                custMainId = nfcDataModel.getCustMainId();
                binding.orderCartCName.setText(nfcDataModel.getCustName());
                binding.orderCartCNo.setText(nfcDataModel.getCustCardNo());
                isParentOfChild = 0; //Parent
            } else {
                for (int i = 0; i < nfcDataModel.getFamilyMember().size(); i++) {
                    if (nfcDataModel.getFamilyMember().get(i).isChecked()) {
                        custMainId = nfcDataModel.getFamilyMember().get(i).getId();
                        binding.orderCartCName.setText(nfcDataModel.getFamilyMember().get(i).getCustChildName());
                        binding.orderCartCNo.setText(nfcDataModel.getFamilyMember().get(i).getCustChildCardNo());
                        isParentOfChild = 1; //Child
                        break;
                    }
                }
            }
            binding.orderCartTableNo.setText(nfcDataModel.getTableNumber());
            binding.orderCartOrderNo.setText(nfcDataModel.getOrderNumber());
            binding.orderCartServiceCharge.setText(nfcDataModel.getServiceCharge() + "%");
            binding.orderCartPayMode.setText(nfcDataModel.getPayMode());
            serviceAmount = Float.parseFloat(nfcDataModel.getServiceCharge());
            getAllItemsOfSpecificTable(nfcDataModel.getTableNumber(), nfcDataModel.getOrderNumber());
        } else {
            AppLog.toast("Table Number or Customer Info not available.");
        }
//        setAdapter();
    }

//    private void setAdapter(){
//        RecyclerViewGeneralAdapter<RecyclerViewBaseAdapter.AdapterDataType> adapter;
//        List<RecyclerViewBaseAdapter.AdapterDataType> list;
//        list = new ArrayList<>();
//        list.add(new OrderSummaryItemsModel());
//        list.add(new OrderSummaryItemsModel());
//        list.add(new OrderSummaryItemsModel());
//        list.add(new OrderSummaryItemsModel());
//        list.add(new OrderSummaryItemsModel());
//        list.add(new OrderSummaryItemsModel());
//        list.add(new OrderSummaryItemsModel());
//        list.add(new OrderSummaryItemsModel());
//        list.add(new OrderSummaryItemsModel());
//        adapter = new RecyclerViewGeneralAdapter<>(list, new ViewHolderFactory<RecyclerViewBaseAdapter.AdapterDataType>() {
//            @Override
//            public int getItemViewType(int position, List<RecyclerViewBaseAdapter.AdapterDataType> list) {
//                return list.get(position).getItemViewType();
//            }
//
//            @Override
//            public GeneralViewHolder<RecyclerViewBaseAdapter.AdapterDataType> createViewHolder(ViewGroup parent, int viewType) {
//                switch (viewType){
//                    case Constant.LIST_TYPE_ORDER_SUMMARY_ITEMS:
//                        LiItemOrderSummaryBinding b2 = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
//                                R.layout.li_item_order_summary,
//                                parent,
//                                false
//                        );
//
//                        return new OrderSummaryViewHolder(b2);
//                }
//                return null;
//            }
//        });
//        binding.orderSummaryItemsRV.setAdapter(adapter);
//        binding.orderSummaryItemsRV.setLayoutManager(new LinearLayoutManager(getContext()));
//        binding.orderSummaryItemsRV.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
//    }


    private void getAllItemsOfSpecificTable(String tableNumber, String orderNumber) {
        List<KitchenItemListModel> selectedList = DbScript.getInstance().getSelectedItemsByTableNumber(tableNumber, orderNumber);
        binding.liCart.setAdapter(new SelectedItemAdapter(selectedList));
        binding.selectedItemCountTV.setText("Items (" + selectedList.size() + ")");
        cost = 0;
        if (selectedList.size() == 0) {
            binding.grandTotalCostTV.setText(String.valueOf(cost));
            binding.orderHoldBtn.setEnabled(false);
            binding.orderPlaceBtn.setEnabled(false);
            binding.orderHoldBtn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGrayLight));
            binding.orderPlaceBtn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGrayLight));
            return;
        }
        for (int i = 0; i < selectedList.size(); i++) {
            float itemCount = selectedList.get(i).getSelected_count();
            float item = Float.valueOf(selectedList.get(i).getItemPrice());
            float totalCostPerItem = itemCount * item;
            cost += totalCostPerItem;
        }
        float serviceChargeAmount = ((serviceAmount / 100) * cost);
        cost = cost + serviceChargeAmount;
        binding.grandTotalCostTV.setText(String.valueOf(cost));
    }

    @Override
    public void setupListeners() {
        /*binding.backFromOrderSummaryIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popStack();
            }
        });*/
        binding.orderHoldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //avoid entering duplicate
                if (isFromOrderList) {
                    List<KitchenItemListModel> selectedList = DbScript.getInstance().
                            getSelectedItemsByTableNumber(orderOnHoldModel.getTable_number(), orderOnHoldModel.getOrder_number());
                    for (int i = 0; i < selectedList.size(); i++) {
                        KitchenItemListModel model = new KitchenItemListModel();
                        model.setIs_selected(selectedList.get(i).getIs_selected());
                        model.setSelected_count(selectedList.get(i).getSelected_count());
                        DbScript.getInstance().updateItem(model);
                    }
                    mListener.onOrderDetailListen(Constant.ORDER_DETAIL_HOLD, null, orderOnHoldModel);
                } else {
                    OrderOnHoldModel orderOnHoldModel = new OrderOnHoldModel();
                    orderOnHoldModel.setCus_name(binding.orderCartCName.getText().toString());
                    orderOnHoldModel.setCus_card_num(binding.orderCartCNo.getText().toString());
                    orderOnHoldModel.setOrder_number(nfcDataModel.getOrderNumber());
                    orderOnHoldModel.setPay_mode(nfcDataModel.getPayMode());
                    orderOnHoldModel.setService_charge(nfcDataModel.getServiceCharge());
                    orderOnHoldModel.setOn_hold("yes");
                    orderOnHoldModel.setTable_number(nfcDataModel.getTableNumber());
                    orderOnHoldModel.setIs_parent_or_child(String.valueOf(isParentOfChild));
                    if (DbScript.getInstance().insertNewOrderToOnHoldTable(orderOnHoldModel))
                        mListener.onOrderDetailListen(Constant.ORDER_DETAIL_HOLD, null, orderOnHoldModel);
                }
            }
        });
        binding.orderPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(isFromOrderList){
//                    placeNow();
//                } else {
//                    mListener.onOrderDetailListen(Constant.ORDER_DETAIL_PLACE, null, orderOnHoldModel);
//                }
                if (isOrderPlaceBtnTapped)
                    AppLog.toast("Current order has placed in the Kitchen.");
                else if (InternetConnection.isOnline())

                    placeNow(false);
                else
                    showConnectionErrorMsg();
            }
        });
        binding.orderAddNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFromOrderList) {
                    mListener.onOrderDetailListen(Constant.ORDER_DETAIL_ADD_NEW, null, orderOnHoldModel);
                } else {
                    mListener.onOrderDetailListen(Constant.ORDER_DETAIL_ADD_NEW, nfcDataModel, null);
                }
            }
        });
        binding.orderAddNewBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOrderPlaceBtnTapped) {
                    if (isFromOrderList) {
                        mListener.onOrderDetailListen(Constant.ORDER_DETAIL_ADD_NEW, null, orderOnHoldModel);
                    } else {
                        mListener.onOrderDetailListen(Constant.ORDER_DETAIL_ADD_NEW, nfcDataModel, null);
                    }
                } else {
                    AppLog.toast("Your current order has placed.");
                }
            }
        });
        binding.orderCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onOrderDetailListen(Constant.ORDER_DETAIL_CLOSE, null, orderOnHoldModel);
            }
        });
    }

    private void placeNow(boolean isForceCreate) {
        binding.orderPlaceBtn.setEnabled(false);
        String orderNumber = "", tableNumber = "", payMode = "", customerCardNo = "", service_charge = "1";
        if (orderOnHoldModel != null) {
            orderNumber = orderOnHoldModel.getOrder_number();
            tableNumber = orderOnHoldModel.getTable_number();
            payMode = orderOnHoldModel.getPay_mode();
//            customerCardNo = orderOnHoldModel.getCus_card_num();
            service_charge = orderOnHoldModel.getService_charge();
        } else {
            orderNumber = nfcDataModel.getOrderNumber();
            tableNumber = nfcDataModel.getTableNumber();
            payMode = nfcDataModel.getPayMode();
//            customerCardNo = nfcDataModel.getCustCardNo();
            service_charge = nfcDataModel.getServiceCharge();
        }

        /*String memberid = binding.orderCartCNo.getText().toString();
        if(memberid != null && !memberid.equalsIgnoreCase("N/A")){
            customerCardNo = memberid;
        }*/


        List<KitchenItemListModel> selectedList = DbScript.getInstance().getSelectedItemsByTableNumber(tableNumber, orderNumber);

        String orderListToSend = "[";
        for (int i = 0; i < selectedList.size(); i++) {
            KitchenItemListModel model = selectedList.get(i);
            if (model.getIs_selected() == 1) {
                String remark = model.getRemarks() == null ? "" : model.getRemarks();
                if ((selectedList.size() - 1) == i) {
                    orderListToSend += "[" + model.getItemTransId() + "," + model.getSelected_count() + ",\"" + remark + "\"]]";
                    break;
                } else {
                    orderListToSend += "[" + model.getItemTransId() + "," + model.getSelected_count() + ",\"" + remark + "\"],";
                }
            }
        }

        showDialog();
        Map<String, String> map = BodyParams.placeOrder(custMainId, tableNumber,
                payMode, AppClass.getProfile().getUserMainId(), String.valueOf(cost), orderListToSend,
                service_charge, String.valueOf(isParentOfChild), isForceCreate);
        final String finalOrderNumber = orderNumber;
        final String finalTableNumber = tableNumber;
        RequestHandler.placeOrder(map, new Callback<PlacedOrderModel>() {
            @Override
            public void invoke(PlacedOrderModel obj) {
                if (obj == null) {
                    AppLog.toast("Problem in placing order.");
                } else {
                    isOrderPlaceBtnTapped = true;
                    removeHoldOrder(finalTableNumber, finalOrderNumber);
                }
            }

            @Override
            public void alreadyLogin() {
                Util.showAlreadyLoginDialog(context);
            }

            @Override
            public void _422(String msg) {
//                AppLog.toast(msg);
                binding.orderPlaceBtn.setEnabled(true);
                if (msg.contains("this order is placed")) {
                    new AlertDialog.Builder(context)
                            .setTitle("Alert")
                            .setMessage("This card order is already taken at other location, do you wish to continue?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    placeNow(true);

                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            removeHoldOrder(finalTableNumber, finalOrderNumber);
                        }
                    }).show();
                } else {
                    new AlertDialog.Builder(context)
                            .setTitle("Alert")
                            .setMessage("This card order is already taken from this location.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    removeHoldOrder(finalTableNumber, finalOrderNumber);
                                }
                            }).setNegativeButton("", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            }
        });
        binding.orderPlaceBtn.setEnabled(true);
    }

    private void removeHoldOrder(String tableNumber, String orderNumber) {
        DbScript.getInstance().deleteHoldOrderFromDbAfterPlaced(tableNumber, orderNumber);
        mListener.onOrderDetailListen(Constant.ORDER_DETAIL_PLACE, null, null);
    }

    private void goBackToMenu() {
        if (isFromOrderList) {
            mListener.onOrderDetailListen(Constant.ORDER_DETAIL_ADD_NEW, null, orderOnHoldModel);
        } else {
            mListener.onOrderDetailListen(Constant.ORDER_DETAIL_ADD_NEW, nfcDataModel, null);
        }
    }

    public interface OnOrderDetailListener {
        void onOrderDetailListen(int fragConst, NFCDataModel nfcDataModel, OrderOnHoldModel orderOnHoldModel);
    }

    private void deleteConfirmationDialog(final KitchenItemListModel model1) {
        new AlertDialog.Builder(context)
                .setMessage("Are you sure you want to delete " + model1.getItemName() + "?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        model1.setIs_selected(0);
                        DbScript.getInstance().updateItem(model1);
                        getAllItemsOfSpecificTable(model1.getTable_number(), model1.getOrder_number());
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    private void showRemarksDialog(final KitchenItemListModel model) {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.remarks_dialog);
        final EditText remarkET = (EditText) dialog.findViewById(R.id.addRemarksET);
        Button remarkBtn = (Button) dialog.findViewById(R.id.addRemarksBtn);
        remarkET.setText(model.getRemarks() == null ? "" : model.getRemarks());
        remarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setRemarks(remarkET.getText().toString());
                DbScript.getInstance().updateRemarksOfSpecificItem(model);
                getAllItemsOfSpecificTable(model.getTable_number(), model.getOrder_number());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public class SelectedItemAdapter extends BaseAdapter {
        List<KitchenItemListModel> list;

        public SelectedItemAdapter(List<KitchenItemListModel> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        class SelectedViewHolder {
            CheckBox menuItemCheckBox;
            CircleImageView menuItemIV;
            TextView menuItemNameIV, menuItemPriceIV, sameItemCountTV, remarksTitleTV, remarksDescTV;
            ImageView addOneMoreIV, removeOneMoreIV, menuItemDeleteIV;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final SelectedViewHolder holder;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(R.layout.li_item_kitchen_items, viewGroup, false);
                holder = new SelectedViewHolder();
                holder.menuItemCheckBox = (CheckBox) view.findViewById(R.id.menuItemCheckBox);
                holder.menuItemNameIV = (TextView) view.findViewById(R.id.menuItemNameIV);
                holder.menuItemPriceIV = (TextView) view.findViewById(R.id.menuItemPriceIV);
                holder.sameItemCountTV = (TextView) view.findViewById(R.id.sameItemCountTV);
                holder.remarksTitleTV = (TextView) view.findViewById(R.id.remarksTitleIV);
                holder.remarksDescTV = (TextView) view.findViewById(R.id.remarksDescTV);
                holder.addOneMoreIV = (ImageView) view.findViewById(R.id.addOneMoreIV);
                holder.removeOneMoreIV = (ImageView) view.findViewById(R.id.removeOneMoreIV);
                holder.menuItemDeleteIV = (ImageView) view.findViewById(R.id.menuItemDeleteIV);
                holder.menuItemIV = (CircleImageView) view.findViewById(R.id.menuItemImageIV);
                view.setTag(holder);
            } else {
                holder = (SelectedViewHolder) view.getTag();
            }

            final KitchenItemListModel model = list.get(i);
            Picasso.with(context).load(API.CONCAT_ITEM_IMAGE + model.getItemTransId() + ".jpg")
                    .resize(250, 250).centerCrop().placeholder(R.mipmap.icon_item_placeholder).into(holder.menuItemIV);
            holder.menuItemNameIV.setText(model.getItemName());
            holder.menuItemPriceIV.setText("Rs. " + model.getItemPrice() + "    Qty. " + model.getSelected_count());
            holder.menuItemCheckBox.setVisibility(View.GONE);
            holder.removeOneMoreIV.setVisibility(View.VISIBLE);
            holder.addOneMoreIV.setVisibility(View.VISIBLE);
            holder.sameItemCountTV.setVisibility(View.VISIBLE);
            holder.menuItemDeleteIV.setVisibility(View.VISIBLE);
            holder.remarksTitleTV.setVisibility(View.VISIBLE);
            holder.remarksDescTV.setVisibility(View.VISIBLE);
            holder.remarksDescTV.setText(model.getRemarks() != null ? model.getRemarks() : "");
            holder.sameItemCountTV.setText("" + model.getSelected_count());

            holder.menuItemDeleteIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    KitchenItemListModel model1 = list.get(i);
                    deleteConfirmationDialog(model1);
                }
            });

            holder.remarksDescTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showRemarksDialog(model);
                }
            });


            holder.addOneMoreIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int addOne = model.getSelected_count();
                    addOne++;
                    model.setSelected_count(addOne);
                    holder.sameItemCountTV.setText("" + model.getSelected_count());
                    holder.menuItemPriceIV.setText("Rs. " + model.getItemPrice() + "    Qty. " + model.getSelected_count());
                    DbScript.getInstance().updateItem(model);
                    updateTotalCostAmount(model);
//                    goBackToMenu();
                }
            });
            holder.removeOneMoreIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int removeOne = model.getSelected_count();
                    if (removeOne > 1) {
                        removeOne--;
                        model.setSelected_count(removeOne);
                        holder.sameItemCountTV.setText("" + model.getSelected_count());
                        holder.menuItemPriceIV.setText("Rs. " + model.getItemPrice() + "    Qty. " + model.getSelected_count());
                        DbScript.getInstance().updateItem(model);
                        updateTotalCostAmount(model);
                    }
//                    goBackToMenu();
                }
            });
            return view;
        }

        private void updateTotalCostAmount(KitchenItemListModel model) {
            List<KitchenItemListModel> selectedList =
                    DbScript.getInstance().getSelectedItemsByTableNumber(model.getTable_number(), model.getOrder_number());
            cost = 0;
            for (KitchenItemListModel model1 : selectedList) {
                float itemCount = model1.getSelected_count();
                float totalCostPerItem = itemCount * Float.valueOf(model1.getItemPrice());
                cost += totalCostPerItem;
            }
            float serviceChargeAmount = ((serviceAmount / 100) * cost);
            cost = cost + serviceChargeAmount;
            binding.grandTotalCostTV.setText(String.valueOf(cost));
        }
    }
}
