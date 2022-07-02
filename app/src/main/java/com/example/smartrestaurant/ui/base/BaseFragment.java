package com.example.smartrestaurant.ui.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartrestaurant.util.AppLog;
import com.example.smartrestaurant.util.Util;


public abstract class BaseFragment extends Fragment {

    private static final String TAG = BaseFragment.class.getSimpleName();
    private OnBaseFragmentInteractionListener mListener;
    protected FragmentActivity context;
    private boolean doLog = false;

    public BaseFragment() {
        // Required empty public constructor
    }

    /*
    * activity will forward back press events to the fragment
    * return true if event is consumed else return false
    * */
    public boolean onBackPressed() {

        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
        if (doLog)
            Log.d(TAG, "onCreate: " + this.getClass().getName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (doLog)
            Log.d(TAG, "onCreateView: " + this.getClass().getName());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (doLog) {
            Log.d(TAG, "onAttach: " + this.getClass().getName());
        }
        if (context instanceof OnBaseFragmentInteractionListener) {
            mListener = (BaseFragment.OnBaseFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " Fragment interaction listener must extend " + OnBaseFragmentInteractionListener.class.getName());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (doLog)
            Log.d(TAG, "onResume: " + this.getClass().getName());
        mListener.updateMenuForFragment(this.getClass().getName(),this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (doLog)
            Log.d(TAG, "onPause: " + this.getClass().getName());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (doLog)
            Log.d(TAG, "onDestroyView: " + this.getClass().getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (doLog)
            Log.d(TAG, "onDestroy: " + this.getClass().getName());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (doLog)
            Log.d(TAG, "onDetach: " + this.getClass().getName());
    }

    public void setupComponents() {
        initializeComponents();
        setupListeners();
    }

    protected void showDialog(){
        Util.ProgressDialog.show(context);
    }

    protected void dismissDialog(){
        Util.ProgressDialog.dismiss();
    }

    protected void showConnectionErrorMsg(){
        AppLog.toast("Please check your internet connection.");
    }

    protected void popStack(){
        context.getSupportFragmentManager().popBackStack();
    }

    protected void popAllStack(){
        context.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    public abstract void initializeComponents();

    public abstract void setupListeners();

    public interface OnBaseFragmentInteractionListener {
        void updateMenuForFragment(String fragmentConstant, BaseFragment fragment);
    }
}