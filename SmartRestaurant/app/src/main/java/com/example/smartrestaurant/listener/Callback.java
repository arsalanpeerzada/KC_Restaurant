package com.example.smartrestaurant.listener;

/**
 * Created by Dell 5521 on 12/26/2017.
 */

public interface Callback<T> {
    void invoke(T obj);
    void alreadyLogin();
    void _422(String msg);
}
