package com.example.appbanlaptop.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.appbanlaptop.R;
import com.example.appbanlaptop.adapter.ProductAdapter;
import com.example.appbanlaptop.fragment.CartFragment;
import com.example.appbanlaptop.fragment.HomeFragment;
import com.example.appbanlaptop.fragment.LoginFragment;
import com.example.appbanlaptop.fragment.RegisterFragment;
import com.example.appbanlaptop.fragment.SearchFragment;
import com.example.appbanlaptop.fragment.WishListFragment;
import com.example.appbanlaptop.manager.CartManager;
import com.example.appbanlaptop.manager.WishListManager;
import com.example.appbanlaptop.modal.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {
    LinearLayout linearLayout;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    SearchFragment searchFragment = new SearchFragment();
    WishListFragment WishListFragment = new WishListFragment();
    RegisterFragment registerFragment = new RegisterFragment();

    LoginFragment loginFragment  = new LoginFragment();

    CartFragment CartFragment = new CartFragment();

    private GridView gridView;
    private static ProductAdapter adapter;
    private static List<Product> productList;
    public int previousItemId = -1;

    private  FrameLayout loadingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);
        AnhXa();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        }
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);
        ActionBar();
        gridView = findViewById(R.id.mainGridView);
        productList=new ArrayList<>();
        // Set adpter
        adapter = new ProductAdapter(this, productList);
        gridView.setAdapter(adapter);
        //lay data tu API
        new FrechProductsTask().execute();
        // Kết nối internet check
        if(isConnected(this)){
        }else{
            Toast.makeText(getApplicationContext(), "No internet, please connect", Toast.LENGTH_LONG).show();
        }

        // Load dữ lieu vao wishmanager
        WishListManager wishListManager = WishListManager.getInstance(getApplicationContext());
        CartManager cartManager = CartManager.getInstance(getApplicationContext());
        wishListManager.loadWishListFromSharedPreferences();
        cartManager.loadCartListFromSharedPreferences();

    }
    @Override
    protected void onPause() {
        super.onPause();
        showLoading();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideLoading();
    }

    private void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
        ProgressBar progressBar=  loadingView.findViewById(R.id.progress_bar);
        progressBar.setProgress(75);
        loadingView.bringToFront();
    }

    private void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    private class FrechProductsTask extends AsyncTask<Void,Void,String> {
        //lay tu lieu tu api qua internet
        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder response = new StringBuilder(); //luu ket qua
            try {
                URL url = new URL("https://raw.githubusercontent.com/hieumai1507/api/main/data.json"); //url
                //open connection
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //set method for read data
                connection.setRequestMethod("GET");
                //tao bo dem de doc data
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                //doc du lieu
                String line="";
                while ((line=reader.readLine())!=null) {
                    //red until EOF
                    response.append(line);
                }
                reader.close();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return response.toString();
        }
        //tra ve du lieu
        @Override
        protected void onPostExecute(String s) {
            if (s!=null && !s.isEmpty()) {
                try {
                    JSONObject json = new JSONObject(s);
                    JSONArray productsArray = json.getJSONArray("product"); //get array for objects
                    for (int i=0; i<productsArray.length(); i++) {
                        JSONObject productObject=productsArray.getJSONObject(i);
                        String ImageId = productObject.getString("id");
                        String ImageProduct = productObject.getString("anhsp");
                        String NameProduct = productObject.getString("tensp");
                        String RamProduct = "RAM: " + productObject.getString("ram");
                        String SsdProduct = "SSD: " + productObject.getString("ssd");
                        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
                        String OldPriceProduct =numberFormat.format(Double.parseDouble(productObject.getString("giacu")))+ " đ";
                        Float giamoi  = Float.parseFloat(productObject.getString("giacu"))*(1 + Float.parseFloat(productObject.getString("discount"))/100);
                        String DiscountProduct = numberFormat.format(giamoi) + " đ";
                        Product product = new Product(ImageId,ImageProduct, NameProduct, RamProduct, SsdProduct, OldPriceProduct, DiscountProduct);
                        productList.add(product);
                    }
                    adapter.notifyDataSetChanged(); //update to adapter
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                Toast.makeText(MainActivity.this, "Faile to fetch products!", Toast.LENGTH_LONG).show();
            }
        }
    }
    private boolean isConnected(Context context){
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getActiveNetworkInfo();
        return mWifi != null && mWifi.isConnectedOrConnecting();
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        TextView homeHeader = (TextView) findViewById(R.id.homeHeader);
        homeHeader.setLeft(0);
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
//        navigationView = findViewById(R.id.navView);
        drawerLayout = findViewById(R.id.drawerLayoutHome);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        linearLayout = findViewById(R.id.mainLayout);
        loadingView = findViewById(R.id.loadingView);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(previousItemId != -1){
            MenuItem previousItem = bottomNavigationView.getMenu().findItem(previousItemId);
            if(previousItem.getItemId() == R.id.home){
                previousItem.setIcon(R.drawable.home);
            }else if(previousItem.getItemId() == R.id.search){
                previousItem.setIcon(R.drawable.search);
            }else if (previousItem.getItemId() == R.id.love){
                previousItem.setIcon(R.drawable.love1);
            }else if(previousItem.getItemId() == R.id.account){
                previousItem.setIcon(R.drawable.account);
            }
            else if(previousItem.getItemId() == R.id.Cart){
                previousItem.setIcon(R.drawable.cart_icon);
            }
        }
        previousItemId = item.getItemId();
        int id = item.getItemId();
        if(id == R.id.home){
            linearLayout.setVisibility(View.VISIBLE);
            item.setIcon(R.drawable.home_actived);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragement, homeFragment)
                    .commit();
            return true;
        }
        else if (id == R.id.search){
            linearLayout.setVisibility(View.GONE);
            item.setIcon(R.drawable.search_actived);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragement, searchFragment)
                    .commit();
            return true;
        }
        else if (id == R.id.love){
            linearLayout.setVisibility(View.GONE);
            item.setIcon(R.drawable.love_actived);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragement, WishListFragment)
                    .commit();
            return true;
        }
        else if (id == R.id.account){
            linearLayout.setVisibility(View.GONE);
            item.setIcon(R.drawable.account_actived);
            getSupportFragmentManager()
                    .beginTransaction()
                    ////////
                    .replace(R.id.flFragement, loginFragment)
                    .commit();
            return true;
        }
        else if (id == R.id.Cart){
            linearLayout.setVisibility(View.GONE);
            item.setIcon(R.drawable.cart_icon);
            getSupportFragmentManager()
                    .beginTransaction()
                    ////////
                    .replace(R.id.flFragement, CartFragment)
                    .commit();
            return true;
        }

        return false;
    }


}