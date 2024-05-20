package com.example.appbanlaptop.Favorite_List;

// FavoriteManager.java

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavoriteManager {
    private static final String PREFS_NAME = "favorites";
    private static final String FAVORITES_KEY = "favorite_list";
    private SharedPreferences preferences;

    public FavoriteManager(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveFavorites(List<Product> favorites) {
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(favorites);
        editor.putString(FAVORITES_KEY, json);
        editor.apply();
    }

    public List<Product> getFavorites() {
        String json = preferences.getString(FAVORITES_KEY, null);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Product>>() {}.getType();
        return json != null ? gson.fromJson(json, type) : new ArrayList<>();
    }

    public void addFavorite(Product product) {
        List<Product> favorites = getFavorites();
        favorites.add(product);
        saveFavorites(favorites);
    }

    public void removeFavorite(Product product) {
        List<Product> favorites = getFavorites();
        favorites.remove(product);
        saveFavorites(favorites);
    }
}
