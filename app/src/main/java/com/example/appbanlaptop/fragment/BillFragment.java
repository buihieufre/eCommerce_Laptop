package com.example.appbanlaptop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.appbanlaptop.R;

import java.util.ArrayList;

public class BillFragment extends AppCompatActivity {


    private TextView billPayment;
    private TextView tongTien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fragment_bill);
        billPayment = findViewById(R.id.billPayment);
        tongTien = findViewById(R.id.totalMoney);

        Intent intent = getIntent();
        if (intent != null) {
            // Lấy danh sách sản phẩm từ Intent
            ArrayList<SearchFragment.LaptopProduct> cartItems = intent.getParcelableArrayListExtra("cartItems");
            // Hiển thị thông tin người mua hàng
            String name = intent.getStringExtra("name");
            String address = intent.getStringExtra("address");
            String phone = intent.getStringExtra("phone");

            TextView nameTextView = findViewById(R.id.nameTextView);
            nameTextView.setText("Họ và tên: " + name);

            TextView addressTextView = findViewById(R.id.addressTextView);
            addressTextView.setText("Địa chỉ: " + address);

            TextView phoneTextView = findViewById(R.id.phoneTextView);
            phoneTextView.setText("Số điện thoại: " + phone);

            billPayment.setText(intent.getStringExtra("paymentMethod"));
            tongTien.setText(intent.getStringExtra("totalMoney"));

            // Hiển thị thông tin sản phẩm
            LinearLayout productsLayout = findViewById(R.id.productsLayout);
            for (SearchFragment.LaptopProduct product : cartItems) {
                // Tạo các TextView mới để hiển thị thông tin sản phẩm
                TextView productNameTextView = new TextView(this);
                productNameTextView.setText("Tên sản phẩm: " + product.getName());
                productsLayout.addView(productNameTextView);

                // Hiển thị thông tin sản phẩm bằng Glide
                ImageView productImageView = new ImageView(this);
                Glide.with(this).load(product.getImageUrl()).into(productImageView);
                productsLayout.addView(productImageView);

                TextView productPriceTextView = new TextView(this);
                productPriceTextView.setText( getString(R.string.pricen_format, product.getOldPrice()));
                productsLayout.addView(productPriceTextView);

                TextView productQuantityTextView = new TextView(this);
                productQuantityTextView.setText( getString(R.string.quantity_format, product.getQuantity()));
                productsLayout.addView(productQuantityTextView);
            }
        }

    }



}