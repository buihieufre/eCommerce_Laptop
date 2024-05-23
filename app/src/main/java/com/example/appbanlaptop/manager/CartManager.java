package com.example.appbanlaptop.manager;

import com.example.appbanlaptop.fragment.SearchFragment;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<SearchFragment.LaptopProduct> cart;
    private List<SearchFragment.LaptopProduct> wishList;

    private CartManager() {
        cart = new ArrayList<>();
        wishList = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(SearchFragment.LaptopProduct product) {
        cart.add(product);
    }

    public List<SearchFragment.LaptopProduct> getCart() {
        return cart;
    }

    public void addToWishList(SearchFragment.LaptopProduct product) {
        wishList.add(product);
    }

    public List<SearchFragment.LaptopProduct> getWishList() {
        return wishList;
    }
}
