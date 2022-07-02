package com.example.smartrestaurant.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.smartrestaurant.AppClass;
import com.example.smartrestaurant.R;
import com.example.smartrestaurant.config.BodyParams;
import com.example.smartrestaurant.config.Constant;
import com.example.smartrestaurant.databinding.ActivityAuthBinding;
import com.example.smartrestaurant.listener.Callback;
import com.example.smartrestaurant.model.AuthResponseModel;
import com.example.smartrestaurant.model.LocationModel;
import com.example.smartrestaurant.model.ProfileModel;
import com.example.smartrestaurant.network.InternetConnection;
import com.example.smartrestaurant.network.RequestHandler;
import com.example.smartrestaurant.ui.base.BaseActivity;
import com.example.smartrestaurant.util.AppLog;
import com.example.smartrestaurant.util.SharedPrefMgr;
import com.example.smartrestaurant.util.Util;

import java.util.Map;

/**
 * Created by Dell 5521 on 12/26/2017.
 */

public class Authentication extends BaseActivity {

    private ActivityAuthBinding binding;
    private LocationModel.LocationData selectedLocation;
    public static String u_rest_Id;
    public String l_rest_id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        Constant.activity = this;
        setupComponents();
    }

    @Override
    protected void initializeViews() {

    }

    @Override
    protected void initializeListeners() {
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InternetConnection.isOnline())
                    if (isPreRequisite()) postLoginRequest();
                    else showInternetConnectionMsg();
            }
        });
        binding.locationET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocations();
            }
        });

    }

    private void postLoginRequest() {
        showDialog();
        final String username = binding.usernameET.getText().toString();
        final String password = binding.passwordET.getText().toString();

        if (selectedLocation == null) {
            AppLog.toast("Please select your location.");
            return;
        }
        String location = selectedLocation.getId();

        Map<String, String> map = BodyParams.login(username, password, location);
        RequestHandler.login(map, new Callback<AuthResponseModel>() {
            @Override
            public void invoke(AuthResponseModel obj) {
                if (obj != null) {

                    u_rest_Id = obj.getRest_id();
                    if (u_rest_Id.equals(l_rest_id)){
                        AppClass.setAuthToken(obj.getSessionId());
                        AppClass.setUserRestId(u_rest_Id);
                        getProfile();
                    }
                    else {AppLog.toast("Restaurant ID doesnt match");}
                } else {alreadyLoginDialog(username, password);}

//                if (u_rest_Id.equals(l_rest_id)) {
//
//                    if (obj != null) {
//
//                        AppClass.setAuthToken(obj.getSessionId());
//                        getProfile();
//                    } else {
//                        alreadyLoginDialog(username, password);
//                    }
//                } else {
//                    AppLog.toast("Restaurant ID doesnt match");
//                }


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

    private void alreadyLoginDialog(final String username, final String password) {
        new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage("Your are already logged in. Do you want to logout from everywhere?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if (InternetConnection.isOnline()) logoutEveryWhereNow(username, password);
                        else showInternetConnectionMsg();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    private void logoutEveryWhereNow(String username, String password) {
        showDialog();
        Map<String, String> map = BodyParams.logoutEverywhere(username, password);
        RequestHandler.logoutEverywhere(map, new Callback<String>() {
            @Override
            public void invoke(String obj) {
                if (SharedPrefMgr.clearPrefs()) {
                    AppLog.toast("Logged out successfully. you can login now.");
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

    private boolean isPreRequisite() {
        if (binding.usernameET.getText().toString().isEmpty()) {
            AppLog.toast("Please enter username.");
            return false;
        }
        if (binding.passwordET.getText().toString().isEmpty()) {
            AppLog.toast("Please enter password.");
            return false;
        }
        if (binding.locationET.getText().toString().isEmpty()) {
            AppLog.toast("Please select your location.");
            return false;
        }

        return true;
    }

    private void getProfile() {
        RequestHandler.getProfile(new Callback<ProfileModel>() {
            @Override
            public void invoke(ProfileModel obj) {
                if (obj != null) {
                    AppClass.setProfile(obj);
                    startActivity(new Intent(context, MainActivity.class));
                    context.finish();
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

    private void getLocations() {
        showDialog();
        RequestHandler.getLocations(new Callback<LocationModel>() {
            @Override
            public void invoke(LocationModel obj) {
                if (obj != null) {
                    showLocations(obj);

                } else {
                    showInternetConnectionMsg();
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

    private void showLocations(LocationModel model) {


        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.location_pick_dialog);
        ListView fileListView = (ListView) dialog.findViewById(R.id.locationPickLV);
        fileListView.setAdapter(new LocationListAdapter(model));
        fileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog.dismiss();
                selectedLocation = (LocationModel.LocationData) adapterView.getItemAtPosition(i);
                binding.locationET.setText(selectedLocation.getName());
                l_rest_id = selectedLocation.getRes_main_id();


            }
        });
        dialog.show();
    }

    class LocationListAdapter extends BaseAdapter {
        private LocationModel list;

        public LocationListAdapter(LocationModel list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.getData().size();
        }

        @Override
        public LocationModel.LocationData getItem(int i) {
            return list.getData().get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView textView;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(R.layout.li_location, viewGroup, false);
                textView = (TextView) view.findViewById(R.id.locationNameTV);
            } else {
                textView = (TextView) view.findViewById(R.id.locationNameTV);
            }
            textView.setGravity(Gravity.CENTER);
            textView.setText(list.getData().get(i).getName());
            return view;
        }
    }
}
