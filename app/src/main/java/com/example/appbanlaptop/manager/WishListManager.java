package com.example.appbanlaptop.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.appbanlaptop.fragment.SearchFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class WishListManager {
    Context context ;
    private static WishListManager instance;
    private Set<SearchFragment.LaptopProduct> wishListItems;

    private WishListManager(Context context) {
        this.context = context.getApplicationContext(); // Sử dụng ApplicationContext để tránh rò rỉ bộ nhớ
        wishListItems = getProductWishList(context);
    }
    public static String getProductListName() {
        return productList;
    }

    public void loadWishListFromSharedPreferences() {
        wishListItems = getProductWishList(context);
    }

    public static void setProductListName(String productList) {
        WishListManager.productList = productList;
    }

    public static String productList = "PRODUCTWISHLIST";
    public void saveProductWishList(Context context, Set<SearchFragment.LaptopProduct> productSet) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(productList, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        Set<String> jsonSet = new HashSet<>();

        for (SearchFragment.LaptopProduct product : productSet) {
            String json = gson.toJson(product);
            jsonSet.add(json);
        }

        editor.putStringSet("product_set", jsonSet);
        editor.apply();
    }

    public Set<SearchFragment.LaptopProduct> getProductWishList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(productList, Context.MODE_PRIVATE);
        Set<String> jsonSet = sharedPreferences.getStringSet("product_set", new HashSet<>());

        Set<SearchFragment.LaptopProduct> productSet = new HashSet<>();
        Gson gson = new Gson();
        for (String json : jsonSet) {
            SearchFragment.LaptopProduct product = gson.fromJson(json, SearchFragment.LaptopProduct.class);
            productSet.add(product);
        }

        return productSet;
    }
    public SearchFragment.LaptopProduct getProductAtPosition(int position) {
        Set<SearchFragment.LaptopProduct> productSet = getProductWishList(context.getApplicationContext());
        if (productSet == null || productSet.isEmpty() || position < 0) {
            return null;
        }

        List<SearchFragment.LaptopProduct> productList = new ArrayList<>(productSet);
        if (position < productList.size()) {
            return productList.get(position);
        } else {
            return null; // Trả về null nếu vị trí không hợp lệ
        }
    }

    public void removeProductFromWishList(Context context, int productId) {
        Set<SearchFragment.LaptopProduct> productSet = getProductWishList(context);
        Iterator<SearchFragment.LaptopProduct> iterator = productSet.iterator();

        while (iterator.hasNext()) {
            SearchFragment.LaptopProduct product = iterator.next();
            if (product.getId() == productId) {
                iterator.remove();
                break;
            }
        }

        saveProductWishList(context, productSet);
    }

    public void addProductToWishList(Context context, SearchFragment.LaptopProduct product) {
        Set<SearchFragment.LaptopProduct> productSet = getProductWishList(context);
        productSet.add(product);
        saveProductWishList(context, productSet);
    }


    private WishListManager() {
        wishListItems = new HashSet<>();
    }

    public static synchronized WishListManager getInstance(Context context) {
        if (instance == null) {
            instance = new WishListManager(context);
        }
        return instance;
    }

    public void addToWishList(SearchFragment.LaptopProduct product) {
        wishListItems.add(product); // Set sẽ tự động loại bỏ các sản phẩm trùng lặp
        addProductToWishList(context.getApplicationContext(),product);

    }

    public void removeFromWishList(SearchFragment.LaptopProduct product) {
        wishListItems.remove(product);
        removeProductFromWishList(context.getApplicationContext(), product.getId());
    }

    public List<SearchFragment.LaptopProduct> getWishListItems() {
        return new ArrayList<>(wishListItems);
    }
}