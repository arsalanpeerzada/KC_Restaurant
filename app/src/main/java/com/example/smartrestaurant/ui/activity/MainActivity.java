package com.example.smartrestaurant.ui.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcF;
import android.nfc.tech.TagTechnology;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.Toast;

import com.example.smartrestaurant.R;
import com.example.smartrestaurant.config.Constant;
import com.example.smartrestaurant.databinding.ActivityMainBinding;
import com.example.smartrestaurant.db_handlers.DbScript;
import com.example.smartrestaurant.handler.FragmentHandler;
import com.example.smartrestaurant.listener.Callback;
import com.example.smartrestaurant.model.TransactionKitchenItemsModel;
import com.example.smartrestaurant.model.kitchen.KitchenItemListModel;
import com.example.smartrestaurant.model.kitchen.OrderOnHoldModel;
import com.example.smartrestaurant.model.kitchen.PlacedOrderModel;
import com.example.smartrestaurant.model.nfc.NFCDataModel;
import com.example.smartrestaurant.network.InternetConnection;
import com.example.smartrestaurant.network.RequestHandler;
import com.example.smartrestaurant.service.NotificationService;
import com.example.smartrestaurant.ui.base.BaseActivity;
import com.example.smartrestaurant.ui.base.BaseFragment;
import com.example.smartrestaurant.ui.fragment.Home;
import com.example.smartrestaurant.ui.fragment.Kitchen1;
import com.example.smartrestaurant.ui.fragment.KitchenMenus;
import com.example.smartrestaurant.ui.fragment.Kitchens;
import com.example.smartrestaurant.ui.fragment.MemberPaymentMode;
import com.example.smartrestaurant.ui.fragment.NFCReader;
import com.example.smartrestaurant.ui.fragment.Notification;
import com.example.smartrestaurant.ui.fragment.OrderDetail;
import com.example.smartrestaurant.ui.fragment.OrderList;
import com.example.smartrestaurant.ui.fragment.PlacedOrderDetail;
import com.example.smartrestaurant.util.AppLog;
import com.example.smartrestaurant.util.SharedPrefMgr;
import com.example.smartrestaurant.util.Util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends BaseActivity implements
        BaseFragment.OnBaseFragmentInteractionListener,
        Home.OnHomeListener,
        NFCReader.OnNFCListener,
        MemberPaymentMode.OnMemberPaymentModeListener,
        Kitchens.OnKitchensListener,
        KitchenMenus.OnKitchenMenusListener,
        Kitchen1.OnKitchen1Listener,
        OrderList.OnOrderListListener,
        OrderDetail.OnOrderDetailListener,
        Notification.NotificationListener,
        PlacedOrderDetail.OnPlacedOrderDetailListener {

    private ActivityMainBinding binding;
    private FragmentHandler fragmentHandler;

    public static boolean isNewKitchensAndItemsFetched;
    public static MainActivity isMainActivityRef;
    private PendingIntent mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        fragmentHandler = new FragmentHandler(R.id.mainContainer, this);
        isNewKitchensAndItemsFetched = true;
        isMainActivityRef = this;
        Constant.activity = this;
        startService(new Intent(this, NotificationService.class));
        startNFCSetup();

        setupComponents();


//        just practice
//        binarySearch();
//        binarySearchString();
    }

    @Override
    protected void onPause() {
        isNewKitchensAndItemsFetched = false;
        DbScript.getInstance().releaseDatabase();
        if(mNfcAdapter != null) mNfcAdapter.disableForegroundDispatch(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        isMainActivityRef = null;
        super.onDestroy();
    }


    @Override
    protected void initializeViews() {
        fragmentHandler.addReplaceFragment(new Home(), false, false, 4, null);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, binding.DrawerLayout,
                binding.toolbarInclude.toolBar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                binding.DrawerLayout.setScrimColor(Color.TRANSPARENT);
                binding.mainContainer.setTranslationX(drawerView.getWidth() * slideOffset);
            }
        };
        binding.DrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    @Override
    protected void initializeListeners() {
        binding.toolbarInclude.notificationIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.toolbarInclude.notificationBubbleTV.setVisibility(View.GONE);
                fragmentHandler.addReplaceFragment(new Notification(), true, true, 4, null);
            }
        });
        binding.toolbarInclude.myOrdersFromMenuIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentHandler.addReplaceFragment(new OrderList(), true, true, 4, null);
            }
        });
        binding.toolbarInclude.newOrderMenuIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentHandler.addReplaceFragment(new NFCReader(), true, true, 4, null);
            }
        });
        binding.toolbarInclude.refreshOrderMenuIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderList l = (OrderList) getSupportFragmentManager().findFragmentByTag(OrderList.class.getName());
                if(l != null){
                    l.doRefresh(true);
                }
            }
        });
        binding.toolbarInclude.myOrdersFromMenuOnNotificationIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentHandler.addReplaceFragment(new OrderList(), true, true, 4, null);
            }
        });
        binding.toolbarInclude.newOrderMenuOnNotificationIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentHandler.addReplaceFragment(new NFCReader(), true, true, 4, null);
            }
        });
        binding.drawerItemInclude.drawerHomeLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAllStack();
                closeDrawer();
                fragmentHandler.addReplaceFragment(new Home(), false, true, 4, null);
            }
        });
        binding.drawerItemInclude.drawerNotificatiosLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer();
                fragmentHandler.addReplaceFragment(new Notification(), true, true, 4, null);
            }
        });
        binding.drawerItemInclude.drawerNewOrderLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer();
                fragmentHandler.addReplaceFragment(new NFCReader(), true, true, 4, null);
            }
        });
        binding.drawerItemInclude.drawerMyOrdersLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer();
                fragmentHandler.addReplaceFragment(new OrderList(), true, true, 4, null);
            }
        });
        binding.drawerItemInclude.drawerLogoutLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer();
                logoutConfirmationDialog();
            }
        });
    }

    @Override
    public void updateMenuForFragment(String fragmentConstant, BaseFragment fragment) {
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        binding.toolbarInclude.myOrdersFromMenuOnNotificationIV.setVisibility(View.GONE);
        binding.toolbarInclude.newOrderMenuOnNotificationIV.setVisibility(View.GONE);
        if (fragment.getClass().getName().equals(NFCReader.class.getName())) {
            binding.toolbarInclude.stepTitleTV.setText("Card Scan (Step 1)");
            binding.toolbarInclude.notificationRL.setVisibility(View.VISIBLE);
            binding.toolbarInclude.notificationIV.setVisibility(View.VISIBLE);
            binding.toolbarInclude.myOrdersFromMenuIV.setVisibility(View.VISIBLE);
            binding.toolbarInclude.newOrderMenuIV.setVisibility(View.GONE);
            binding.toolbarInclude.refreshOrderMenuIV.setVisibility(View.GONE);
        } else if (fragment.getClass().getName().equals(MemberPaymentMode.class.getName())) {
            binding.toolbarInclude.stepTitleTV.setText("Add Details (Step 2)");
            binding.toolbarInclude.notificationRL.setVisibility(View.VISIBLE);
            binding.toolbarInclude.notificationIV.setVisibility(View.VISIBLE);
            binding.toolbarInclude.myOrdersFromMenuIV.setVisibility(View.VISIBLE);
            binding.toolbarInclude.newOrderMenuIV.setVisibility(View.GONE);
            binding.toolbarInclude.refreshOrderMenuIV.setVisibility(View.GONE);
        } else if (fragment.getClass().getName().equals(KitchenMenus.class.getName())) {
            binding.toolbarInclude.stepTitleTV.setText("Select Items (Step 3)");
            binding.toolbarInclude.notificationRL.setVisibility(View.VISIBLE);
            binding.toolbarInclude.notificationIV.setVisibility(View.VISIBLE);
            binding.toolbarInclude.myOrdersFromMenuIV.setVisibility(View.VISIBLE);
            binding.toolbarInclude.newOrderMenuIV.setVisibility(View.VISIBLE);
            binding.toolbarInclude.refreshOrderMenuIV.setVisibility(View.GONE);
        } else if (fragment.getClass().getName().equals(OrderDetail.class.getName())) {
            binding.toolbarInclude.stepTitleTV.setText("Confirmation (Step 4)");
            binding.toolbarInclude.notificationIV.setVisibility(View.VISIBLE);
            binding.toolbarInclude.notificationRL.setVisibility(View.VISIBLE);
            binding.toolbarInclude.newOrderMenuIV.setVisibility(View.VISIBLE);
            binding.toolbarInclude.refreshOrderMenuIV.setVisibility(View.GONE);
            binding.toolbarInclude.myOrdersFromMenuIV.setVisibility(View.VISIBLE);
        } else if (fragment.getClass().getName().equals(PlacedOrderDetail.class.getName())) {
            binding.toolbarInclude.stepTitleTV.setText("Placed Order Detail");
            binding.toolbarInclude.notificationIV.setVisibility(View.VISIBLE);
            binding.toolbarInclude.notificationRL.setVisibility(View.VISIBLE);
            binding.toolbarInclude.newOrderMenuIV.setVisibility(View.VISIBLE);
            binding.toolbarInclude.refreshOrderMenuIV.setVisibility(View.GONE);
            binding.toolbarInclude.myOrdersFromMenuIV.setVisibility(View.VISIBLE);
        } else if (fragment.getClass().getName().equals(Notification.class.getName())) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            binding.toolbarInclude.stepTitleTV.setText("Notifications");
            binding.toolbarInclude.notificationRL.setVisibility(View.GONE);
            binding.toolbarInclude.notificationIV.setVisibility(View.GONE);
            binding.toolbarInclude.myOrdersFromMenuIV.setVisibility(View.GONE);
            binding.toolbarInclude.newOrderMenuIV.setVisibility(View.GONE);
            binding.toolbarInclude.refreshOrderMenuIV.setVisibility(View.GONE);
            binding.toolbarInclude.myOrdersFromMenuOnNotificationIV.setVisibility(View.VISIBLE);
            binding.toolbarInclude.newOrderMenuOnNotificationIV.setVisibility(View.VISIBLE);
        } else if (fragment.getClass().getName().equals(OrderList.class.getName())) {
            binding.toolbarInclude.stepTitleTV.setText("My Orders");
            binding.toolbarInclude.notificationRL.setVisibility(View.VISIBLE);
            binding.toolbarInclude.notificationIV.setVisibility(View.VISIBLE);
            binding.toolbarInclude.newOrderMenuIV.setVisibility(View.VISIBLE);
            binding.toolbarInclude.refreshOrderMenuIV.setVisibility(View.VISIBLE);
            binding.toolbarInclude.myOrdersFromMenuIV.setVisibility(View.GONE);
        } else if (fragment.getClass().getName().equals(Home.class.getName())) {
            binding.toolbarInclude.stepTitleTV.setText("Home");
            binding.toolbarInclude.notificationRL.setVisibility(View.GONE);
            binding.toolbarInclude.notificationIV.setVisibility(View.GONE);
            binding.toolbarInclude.myOrdersFromMenuIV.setVisibility(View.GONE);
            binding.toolbarInclude.newOrderMenuIV.setVisibility(View.GONE);
            binding.toolbarInclude.refreshOrderMenuIV.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
//        MemberPaymentMode memberPaymentMode = (MemberPaymentMode) getSupportFragmentManager().findFragmentByTag(MemberPaymentMode.class.getName());
//        if(memberPaymentMode != null){
//            popAllStack();
//            fragmentHandler.addReplaceFragment(new NFCReader(), true, true, 4, null);
//        } else {
//            super.onBackPressed();
//        }
    }

    @Override
    public void onHomeListen(int fragConst) {
        switch (fragConst) {
            case 1:
                fragmentHandler.addReplaceFragment(new NFCReader(), true, true, 4, null);
                break;
            case 2:
                fragmentHandler.addReplaceFragment(new OrderList(), true, true, 4, null);
                break;
            case 3:
                fragmentHandler.addReplaceFragment(new Notification(), true, true, 4, null);
                break;
        }
    }

    @Override
    public void onNFCListen(NFCDataModel nfcDataModel) {
        fragmentHandler.addReplaceFragment(MemberPaymentMode.newInstance(nfcDataModel), true, true, 4, null);
    }

    @Override
    public void onMemberPaymentModeListen(NFCDataModel nfcDataModel) {
//        popAllStack();
//        fragmentHandler.addReplaceFragment(KitchenMenus.newInstance(tableNumber,nfcDataModel), true, true, 4, null);
        fragmentHandler.addReplaceFragment(KitchenMenus.newInstance(nfcDataModel), true, true, 4, null);
    }

    @Override
    public void onKitchensListen(int fragConst, FragmentHandler fragmentHandler1) {
        switch (fragConst) {
            case Constant.KITCHEN_ORDER_DETAIL:
//                fragmentHandler.addReplaceFragment(new OrderDetail(), true, true, 4, null);
                break;
            case Constant.KITCHEN_INFLATE_ITEM_FRAGMENT:
                fragmentHandler1.addReplaceFragment(new Kitchen1(), false, true, 4, null);
                break;
            case Constant.KITCHEN_UPDATE_LIST_ITEM:
                Kitchen1 kitchen1 = (Kitchen1) getSupportFragmentManager().findFragmentByTag(Kitchen1.class.getName());
                if (kitchen1 != null) {
                    kitchen1.updateListItems();
                }
                break;
        }
    }


    @Override
    public void onKitchenMenusListen(int fragConst, NFCDataModel nfcDataModel,
                                     OrderOnHoldModel orderOnHoldModel, PlacedOrderModel placedOrderModel) {
        switch (fragConst) {
            case Constant.KITCHEN_ORDER_DETAIL:
                if (orderOnHoldModel != null)
                    fragmentHandler.addReplaceFragment(OrderDetail.newInstance(true, orderOnHoldModel), true, true, 4, null);
                else
                    fragmentHandler.addReplaceFragment(OrderDetail.newInstance(false, nfcDataModel), true, true, 4, null);
                break;
            case Constant.KITCHEN_ORDER_DETAIL_FOR_PLACED:
                if(placedOrderModel == null || placedOrderModel.getOrderDetail() == null)
                    AppLog.toast("There is an invalid order or some information is missing in this order.");
                else
                    fragmentHandler.addReplaceFragment(PlacedOrderDetail.newInstance(placedOrderModel), false, true, 4, null);
                break;
        }
    }

    @Override
    public void onKitchen1Listen(KitchenItemListModel data) {

    }

    @Override
    public void onOrderListListen(OrderOnHoldModel model, PlacedOrderModel placedOrderModel) {
        if (model != null) {
            if (model.getOn_hold().equalsIgnoreCase("yes")) {
                fragmentHandler.addReplaceFragment(OrderDetail.newInstance(true, model), true, true, 4, null);
            }
        } else if (placedOrderModel != null) {
            TransactionKitchenItemsModel.getInstance().setKitchenDataModelForPlaceOrder(
                    TransactionKitchenItemsModel.getInstance().getKitchenDataModel());
            if(placedOrderModel.getOrderDetail() != null){
                fragmentHandler.addReplaceFragment(PlacedOrderDetail.newInstance(placedOrderModel), true, true, 4, null);
            } else
                AppLog.toast("There is an invalid order or some information is missing in this order.");
        }
    }

    @Override
    public void onOrderDetailListen(int fragConst, NFCDataModel nfcDataModel, OrderOnHoldModel orderOnHoldModel) {
        switch (fragConst) {
            case Constant.ORDER_DETAIL_HOLD:
                Toast.makeText(context, "Order On Hold.", Toast.LENGTH_LONG).show();
                popAllStack();
                fragmentHandler.addReplaceFragment(OrderList.newInstance(orderOnHoldModel), true, true, 4, null);
                break;
            case Constant.ORDER_DETAIL_PLACE:
//                AppLog.toast("Order Placed in the Kitchen.");
                popAllStack();
                fragmentHandler.addReplaceFragment(new OrderList(), false, true, 4, null);
                break;
            case Constant.ORDER_DETAIL_ADD_NEW:
                popAllStack();
                if (orderOnHoldModel != null)
                    fragmentHandler.addReplaceFragment(KitchenMenus.newInstance(orderOnHoldModel), true, true, 4, null);
                else
                    fragmentHandler.addReplaceFragment(KitchenMenus.newInstance(nfcDataModel), true, true, 4, null);
                break;
            case Constant.ORDER_DETAIL_CLOSE:
                Toast.makeText(context, "Order will close.", Toast.LENGTH_LONG).show();
                popAllStack();
                break;
        }
    }

    @Override
    public void onPlacedOrderDetailListen(int fragConst, PlacedOrderModel placedOrderModel) {
        switch (fragConst){
            case Constant.PLACED_ORDER_ADD_NEW:
                fragmentHandler.addReplaceFragment(KitchenMenus.newInstance(true,placedOrderModel), false, true, 4, null);
                break;
            case Constant.PLACED_ORDER_UPDATE:
                //refreshing screen
                fragmentHandler.addReplaceFragment(PlacedOrderDetail.newInstance(placedOrderModel), false, true, 4, null);
                break;
        }
    }

    @Override
    public void onNotificationListen(int fragConst) {

    }

    private void closeDrawer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.DrawerLayout.closeDrawers();
            }
        }, 100);
    }

    private void logoutConfirmationDialog() {
        new AlertDialog.Builder(context)
                .setMessage("Are you sure you want to LOGOUT?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if(InternetConnection.isOnline())logoutCall();
                        else showInternetConnectionMsg();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    private void logoutCall(){
        showDialog();
        RequestHandler.logout(new Callback<String>() {
            @Override
            public void invoke(String obj) {
                if(obj != null && obj.equalsIgnoreCase("OK")){
                    if (SharedPrefMgr.clearPrefs()) {
//                            DbScript.getInstance().clearOrdersTable();
                        stopService(new Intent(MainActivity.this, NotificationService.class));
                        startActivity(new Intent(MainActivity.this, Authentication.class));
                        MainActivity.this.finish();
                    } else {
                        AppLog.toast("Problem while logging out.");
                    }
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

    public void setNotificationCount(int count){
        if(count > 0){
            binding.toolbarInclude.notificationBubbleTV.setVisibility(View.VISIBLE);
            binding.toolbarInclude.notificationBubbleTV.setText(String.valueOf(count));
        } else {
            binding.toolbarInclude.notificationBubbleTV.setVisibility(View.GONE);
        }
    }

    //region NFC work
    @Override
    public void onResume() {
        super.onResume();
        if(mNfcAdapter != null){
            mNfcAdapter.enableForegroundDispatch(this,mPendingIntent,mFilters,mTechList);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    NfcAdapter mNfcAdapter;
    String[][] mTechList = new String[][]{new String[]{NfcA.class.getName()}};
    IntentFilter[] mFilters = new IntentFilter[1];

    private void startNFCSetup() {

        mNfcAdapter = NfcAdapter.getDefaultAdapter(context);

        if (mNfcAdapter == null) {
            AppLog.toast("This device does not support NFC.");
            return;
        }

        if (!mNfcAdapter.isEnabled()) {
            AppLog.toast("Please enable your NFC.");
        }
        mPendingIntent = PendingIntent.getActivity(this,0,
                new Intent(this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);


        // Notice that this is the same filter as in our manifest.
        mFilters[0] = new IntentFilter();
        mFilters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        mFilters[0].addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        mFilters[0].addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        mFilters[0].addCategory(Intent.CATEGORY_DEFAULT);
        mFilters[0].addDataScheme("vnd.android.nfc");
        try {
            mFilters[0].addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

    }


    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if ("*/*".equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                AppLog.d("Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            byte[] idData = tag.getId();
            StringBuffer hexString = new StringBuffer("");
            for (int i : idData) {
                String byteString = Integer.toHexString(0xFF & i);
                hexString.append((byteString.length() == 1 ? "0" : "")).append(byteString);
            }

            if(!hexString.toString().equalsIgnoreCase("")){
                NFCReader nfcReader = (NFCReader) getSupportFragmentManager().findFragmentByTag(NFCReader.class.getName());
                if(nfcReader != null){
                    if(InternetConnection.isOnline()) nfcReader.getNCF_ID(hexString.toString());
                    else showInternetConnectionMsg();
                }
                AppLog.toast("TAG id: "+hexString);
            }

            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        } else if(NfcAdapter.ACTION_TAG_DISCOVERED.equalsIgnoreCase(action)){
            AppLog.toast("TAG: "+action);
        } else {
            AppLog.toast("Adapter state Changed");
        }
    }

    private static class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();
            Tag tag = params[0];

            /*NfcA nfcA = NfcA.get(tag);
            try {
                nfcA.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Short s = nfcA.getSak();
            byte[] a = nfcA.getAtqa();
            String atqa = new String(a, Charset.forName("US-ASCII"));*/


            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        AppLog.d("Unsupported Encoding: " + e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                AppLog.toast("Read content: " + result);
            }
        }
    }
    //endregion

    void binarySearch(){
        int[] sortedArr = {3,5,7,9,11,16,26,28,37,48,49,59,68,79,90,99};
        int[] unsortedArr = {13,45,27,19,611,516,226,228,367,448,429,569,-628,759,910,-919};
        Log.d("SEARCCCCCCCCCCCCCc","SORTED "+Arrays.toString(sortedArr));
        Log.d("SEARCCCCCCCCCCCCCc","UNSORTED "+Arrays.toString(unsortedArr));

        Arrays.sort(unsortedArr);
        Log.d("SEARCCCCCCCCCCCCCc","UNSORTED-SORT "+Arrays.toString(unsortedArr));

        int start = 0,end = unsortedArr.length - 1, toFind = -628;
        while (start <= end){
            int middle = (start + end) / 2;
            if(unsortedArr[start] == toFind) {
                Log.d("SEARCCCCCCCCCCCCCc","Found At index "+start +" is "+unsortedArr[start]);
                break;
            }

            if(unsortedArr[end] == toFind) {
                Log.d("SEARCCCCCCCCCCCCCc","Found At index "+end +" is "+unsortedArr[end]);
                break;
            }

            if(unsortedArr[middle] == toFind) {
                Log.d("SEARCCCCCCCCCCCCCc","Found At index "+middle +" is "+unsortedArr[middle]);
                break;
            }

            if(unsortedArr[middle] > toFind){
                end = middle - 1;
            } else {
                start = middle + 1;
            }
        }
    }

    void binarySearchString(){
        String[] unsortedArr = {"we","ewd","-1we","0ae","1ae","Aawe","Aewd","nEs","dEa","Zf","w","pl","90pl","EOD"};
        Log.d("SEARCH","UNSORTED "+Arrays.toString(unsortedArr));

        Arrays.sort(unsortedArr);
        Log.d("SEARCH","UNSORTED-SORT "+Arrays.toString(unsortedArr));

        int start = 0,end = unsortedArr.length - 1; String toFind = "EOD";
        while (start <= end){
            int middle = (start + end) / 2;
            if(unsortedArr[middle].compareTo(toFind) == 0) {
                Log.d("SEARCH","Found At index "+middle +" is "+unsortedArr[middle]);
                break;
            }

            if(unsortedArr[middle].compareTo(toFind) > 0){
                end = middle - 1;
            } else {
                start = middle + 1;
            }
        }
    }
}
