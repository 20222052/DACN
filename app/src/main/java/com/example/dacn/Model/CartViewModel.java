package com.example.dacn.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class CartViewModel extends ViewModel {
    private MutableLiveData<List<Cart>> cartListLiveData = new MutableLiveData<>();

    public LiveData<List<Cart>> getCartList() {
        return cartListLiveData;
    }

    public void setCartList(List<Cart> cartList) {
        cartListLiveData.setValue(cartList);
    }
}
