package com.example.appbanlaptop.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.appbanlaptop.fragment.SearchFragment;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CartManager {
    Context context;
    private static CartManager instance;
    private List<SearchFragment.LaptopProduct> cart;
    private List<SearchFragment.LaptopProduct> wishList;
    public static String producCartList = "PRODUCTCARTLIST";


    private CartManager(Context context) {
        cart = new ArrayList<>();
        wishList = new ArrayList<>();
        this.context = context.getApplicationContext();
        loadCartListFromSharedPreferences();
    }

    public void loadCartListFromSharedPreferences() {
        List<SearchFragment.LaptopProduct> loadedCart = getProductCartList(context);
        if (loadedCart != null) {
            cart = new ArrayList<>(loadedCart);
        } else {
            cart = new ArrayList<>();
        }
    }

    public static String getProductList() {
        return producCartList;
    }
    public void saveProductCartList(Context context, List<SearchFragment.LaptopProduct> productList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(producCartList, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(productList); // Serialize the list to JSON
        editor.putString("product_cart_list", json);
        editor.apply();
    }

    public List<SearchFragment.LaptopProduct> getProductCartList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(producCartList, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("product_cart_list", "");

        if (json.isEmpty()) {
            return new ArrayList<>(); // Return an empty list if the JSON string is empty
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<SearchFragment.LaptopProduct>>(){}.getType();
        try {
            return gson.fromJson(json, type); // Deserialize the JSON to a list
        } catch (JsonSyntaxException e) {
            // Handle the error appropriately
            e.printStackTrace();
            return new ArrayList<>();
        }

    }



    public SearchFragment.LaptopProduct getCartAtPosition(Context context, int position) {
        List<SearchFragment.LaptopProduct> productList = getProductCartList(context.getApplicationContext());
        if (productList == null || productList.isEmpty() || position < 0 || position >= productList.size()) {
            return null;
        }
        return productList.get(position); // Return the product at the specified position
    }



    public void addProductToCart(Context context, SearchFragment.LaptopProduct product) {
        List<SearchFragment.LaptopProduct> productList = getProductCartList(context);

        // Check if the product already exists in the list by its ID
        for (SearchFragment.LaptopProduct existingProduct : productList) {
            if (existingProduct.getId() == product.getId()) {
                // Product already exists in the cart
                return;
            }
        }

        // If the product does not exist, add it to the list
        productList.add(product);
        saveProductCartList(context, productList);
    }

    public void removeProductFromCart(Context context, int productId) {
        List<SearchFragment.LaptopProduct> productList = getProductCartList(context);
        Iterator<SearchFragment.LaptopProduct> iterator = productList.iterator();

        while (iterator.hasNext()) {
            SearchFragment.LaptopProduct product = iterator.next();
            if (product.getId() == productId) {
                iterator.remove();
                break;
            }
        }

        saveProductCartList(context, productList);
    }

    public boolean isProductInCart(int productId) {
        for (SearchFragment.LaptopProduct product : cart) {
            if (product.getId() == productId) {
                return true;
            }
        }
        return false;
    }
    public static synchronized CartManager getInstance(Context context) {
        if (instance == null) {
            instance = new CartManager(context);
        }
        return instance;
    }

    public void addToCart(SearchFragment.LaptopProduct product) {
        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        boolean found = false;
        for (SearchFragment.LaptopProduct p : cart) {
            if (p.getId() == product.getId()) {
                found = true;
                p.setQuantity(p.getQuantity() + 1); // Tăng số lượng
                break;
            }
        }
        if (!found) {
            // Nếu sản phẩm chưa có trong giỏ hàng, thêm mới
            product.setQuantity(1);
            cart.add(product);
            addProductToCart(context, product);
        }
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

    public void removeFromCart(SearchFragment.LaptopProduct product) {
        cart.remove(product);
        removeProductFromCart(context, product.getId());
    }
}
