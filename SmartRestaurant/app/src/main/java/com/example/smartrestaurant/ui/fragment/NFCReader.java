package com.example.smartrestaurant.ui.fragment;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartrestaurant.AppClass;
import com.example.smartrestaurant.R;
import com.example.smartrestaurant.config.API;
import com.example.smartrestaurant.config.BodyParams;
import com.example.smartrestaurant.databinding.FrNfcBinding;
import com.example.smartrestaurant.db_handlers.DbScript;
import com.example.smartrestaurant.listener.Callback;
import com.example.smartrestaurant.model.AuthResponseModel;
import com.example.smartrestaurant.model.TransactionKitchenItemsModel;
import com.example.smartrestaurant.model.kitchen.KitchenDataModel;
import com.example.smartrestaurant.model.kitchen.KitchenNameModel;
import com.example.smartrestaurant.model.nfc.NFCDataModel;
import com.example.smartrestaurant.model.nfc.NFCFamilyMember;
import com.example.smartrestaurant.network.InternetConnection;
import com.example.smartrestaurant.network.RequestHandler;
import com.example.smartrestaurant.ui.activity.Authentication;
import com.example.smartrestaurant.ui.base.BaseActivity;
import com.example.smartrestaurant.ui.base.BaseFragment;
import com.example.smartrestaurant.util.AppLog;
import com.example.smartrestaurant.util.SharedPrefMgr;
import com.example.smartrestaurant.util.Util;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dell 5521 on 12/26/2017.
 */

public class NFCReader extends BaseFragment {

    public FrNfcBinding binding;
    private OnNFCListener mListener;
    List<NFCFamilyMember> membersList = new ArrayList<>();
    NFCMemberAdapter adapter;
    NFCDataModel nfcDataModel;
    AuthResponseModel authResponseModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnNFCListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fr_nfc, container, false);
        setupComponents();
        return binding.getRoot();
    }

    @Override
    public void initializeComponents() {
        membersList.clear();
        if(InternetConnection.isOnline())getKitchenItems();
        else showConnectionErrorMsg();
        adapter = new NFCMemberAdapter(membersList);
        binding.dependentsLV.setAdapter(adapter);
    }

    @Override
    public void setupListeners() {
        binding.headSelectedRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nfcDataModel != null) {
                    nfcDataModel.setChecked(true);
                    binding.headSelectedRB.setChecked(nfcDataModel.isChecked());
                }
                for (int i = 0; i < membersList.size(); i++) {
                    membersList.get(i).setChecked(false);
                }
                adapter.notifyDataSetChanged();
            }
        });
        binding.nfcConfirmProceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onNFCListen(nfcDataModel);
            }
        });
        binding.nfcConfirmBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                popStack();
                binding.cardScanLL.setVisibility(View.VISIBLE);
                binding.familyDetailLL.setVisibility(View.GONE);
            }
        });

        binding.cardScanSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.nfcIdET1.getText().toString().isEmpty()) {
                    AppLog.toast("Please enter member id or scan card.");
                } else {
                    if (InternetConnection.isOnline())
                        getNCF_Information(binding.nfcIdET1.getText().toString());
                    else showConnectionErrorMsg();
                }
            }
        });
    }

    public void getNCF_Information(String memberId) {
        showDialog();
        Map<String, String> map = BodyParams.nfcData(memberId,"1");
        RequestHandler.nfcData(map, new Callback<NFCDataModel>() {
            @Override
            public void invoke(NFCDataModel obj) {
                if (obj != null) {
                    binding.nfcIdET1.setText(""+obj.getCustCardNo());
                    nfcDataModel = obj;
                    setData(nfcDataModel);
                } else {
                    showConnectionErrorMsg();
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

    public void getNCF_ID(String memberId) {
        showDialog();
        Map<String, String> map = BodyParams.nfcData(memberId,"0");
        RequestHandler.nfcData(map, new Callback<NFCDataModel>() {
            @Override
            public void invoke(NFCDataModel obj) {
                if (obj != null) {
                    binding.nfcIdET1.setText(""+obj.getCustCardNo());
                } else {
                    showConnectionErrorMsg();
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

    private void setData(NFCDataModel model) {
        Picasso.with(context).load(API.CONCAT_PARENT_IMAGE + model.getCustCardNo() + ".jpg")
                .resize(400, 400).centerCrop()
                .placeholder(R.mipmap.dummy_profile_image).into(binding.headProfilePic);

        Picasso.with(context).load(API.CONCAT_CUS_SIGNATURE_IMAGE + model.getCustCardNo() + ".jpg")
                .resize(400, 400).centerCrop()
                .into(binding.imgSign);
        binding.cardScanLL.setVisibility(View.GONE);
        model.setChecked(true);
        binding.headSelectedRB.setChecked(true);
        binding.familyDetailLL.setVisibility(View.VISIBLE);
        binding.bottomRL.setVisibility(View.VISIBLE);
        binding.nfcIdET.setText(binding.nfcIdET1.getText().toString());
        binding.headNameTV.setText(model.getCustName());
        binding.headNICNumberTV.setText(model.getCustCardNo());
        binding.dependentsTV.setText("" + model.getFamilyMember().size());
        membersList.addAll(model.getFamilyMember());
        adapter.notifyDataSetChanged();
    }

    public interface OnNFCListener {
        void onNFCListen(NFCDataModel nfcDataModel);
    }

    private void callListener(List<NFCFamilyMember> list) {
        if (nfcDataModel != null) {
            nfcDataModel.setChecked(false);
            binding.headSelectedRB.setChecked(nfcDataModel.isChecked());
            adapter.notifyDataSetChanged();
        }
    }

    public class NFCMemberAdapter extends BaseAdapter {

        List<NFCFamilyMember> memberList;

        public NFCMemberAdapter(List<NFCFamilyMember> memberList) {
            this.memberList = memberList;
        }

        @Override
        public int getCount() {
            return memberList.size();
        }

        @Override
        public Object getItem(int i) {
            return memberList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        class MemberViewHolder {
            TextView dependentTV, dependentNIC_NumberTV;
            RadioButton dependantRadioBtn;
            CircleImageView depProfileImage;
            ImageView imagSign;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            MemberViewHolder viewHolder;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(R.layout.li_dependents, viewGroup, false);
                viewHolder = new MemberViewHolder();
                viewHolder.dependentTV = (TextView) view.findViewById(R.id.dependentNameTV);
                viewHolder.dependentNIC_NumberTV = (TextView) view.findViewById(R.id.dependentNIC_NumberTV);
                viewHolder.dependantRadioBtn = (RadioButton) view.findViewById(R.id.dependantRadioBtn);
                viewHolder.depProfileImage = (CircleImageView) view.findViewById(R.id.dependentProfilePic);
                viewHolder.imagSign = (ImageView) view.findViewById(R.id.img_sign);
                view.setTag(viewHolder);
            } else {
                viewHolder = (MemberViewHolder) view.getTag();
            }

            Picasso.with(context).load(API.CONCAT_DEPENDANT_IMAGE + memberList.get(i).getCustChildCardNo() + ".jpg")
                    .resize(400, 400).centerCrop()
                    .placeholder(R.mipmap.dummy_profile_image).into(viewHolder.depProfileImage);

            Picasso.with(context).load(API.CONCAT_CUS__DEP_SIGN_IMAGE + memberList.get(i).getCustChildCardNo() + ".jpg")
                    .resize(400, 400).centerCrop()
                    .into(viewHolder.imagSign);
            viewHolder.dependantRadioBtn.setChecked(memberList.get(i).isChecked());
            viewHolder.dependentTV.setText(memberList.get(i).getCustChildName());
            viewHolder.dependentNIC_NumberTV.setText(memberList.get(i).getCustChildCardNo());

            viewHolder.dependantRadioBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int j = 0; j < memberList.size(); j++) {
                        memberList.get(j).setChecked(false);
                    }
                    memberList.get(i).setChecked(true);
                    callListener(memberList);
                }
            });
            return view;
        }
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
    }

}
