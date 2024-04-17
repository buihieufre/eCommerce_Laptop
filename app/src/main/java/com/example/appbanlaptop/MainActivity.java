package com.example.appbanlaptop;

import android.annotation.SuppressLint;
import android.app.FragmentBreadCrumbs;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import  androidx.appcompat.app.ActionBar;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    SearchFragment searchFragment = new SearchFragment();
    LoveFragment loveFragment = new LoveFragment();
    AccountFragment accountFragment = new AccountFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);
        AnhXa();
        ActionBar();

    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.menu_drawer);
        TextView homeHeader = (TextView) findViewById(R.id.homeHeader);
        homeHeader.setLeft(0);
        toolbar.setNavigationOnClickListener(v -> {drawerLayout.openDrawer(GravityCompat.START);});
        @SuppressLint("CutPasteId") TextView textView = (TextView) findViewById(R.id.homeHeader);

        TextPaint paint = textView.getPaint();
        float width = paint.measureText("LapUni");

        Shader textShader = new LinearGradient(0, 0, width, textView.getTextSize(),
                new int[]{
                        Color.parseColor("#F97C3C"),
                        Color.parseColor("#FDB54E"),
                        Color.parseColor("#64B678"),
                        Color.parseColor("#478AEA"),
                        Color.parseColor("#8446CC"),
                }, null, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(textShader);

    }


    private void AnhXa(){
        toolbar = findViewById(R.id.toolbar);
//        viewFlipper = findViewById(R.id.viewFlipHome);
        navigationView = findViewById(R.id.navView);
        drawerLayout = findViewById(R.id.drawerLayoutHome);
    }
//    @Override
//    public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
//
//    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if(id == R.id.home){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragement, homeFragment)
                        .commit();
                return true;
            }
            else if (id == R.id.search){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragement, searchFragment)
                        .commit();
                return true;
            }


            else if (id == R.id.love){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragement, loveFragment)
                        .commit();
                return true;
            }
            else if (id == R.id.account){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragement, accountFragment)
                        .commit();
                return true;

            }
            return false;
    }
}