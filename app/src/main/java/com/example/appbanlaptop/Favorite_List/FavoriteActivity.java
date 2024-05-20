package com.example.appbanlaptop.Favorite_List;

// FavoriteActivity.java

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanlaptop.R;

import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    private ListView listView;
    private FavoriteAdapter adapter;
    private FavoriteManager favoriteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        listView = findViewById(R.id.listView);

        favoriteManager = new FavoriteManager(this);
        List<Product> favoriteList = favoriteManager.getFavorites();
        adapter = new FavoriteAdapter(this, favoriteList);
        listView.setAdapter(adapter);
    }
}
