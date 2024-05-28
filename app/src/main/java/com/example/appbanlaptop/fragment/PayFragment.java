package com.example.appbanlaptop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.appbanlaptop.R;

import java.util.ArrayList;

public class PayFragment extends AppCompatActivity {

    private ArrayList<SearchFragment.LaptopProduct> cartItems;
    private ImageView productImageView;
    private TextView productNameTextView;
    private TextView productPriceTextView;
    private TextView productQuantityTextView;
    private EditText addressEditText;
    private EditText nameEditText;
    private EditText phoneEditText;
    private Button codButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pay);

        // Khởi tạo các thành phần giao diện
        productImageView = findViewById(R.id.productImageView);
        productNameTextView = findViewById(R.id.productNameTextView);
        productPriceTextView = findViewById(R.id.productPriceTextView);
        productQuantityTextView = findViewById(R.id.productQuantityTextView);
        addressEditText = findViewById(R.id.addressEditText);
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        codButton = findViewById(R.id.codButton);

        // Nhận dữ liệu sản phẩm từ Intent
        if (getIntent() != null && getIntent().hasExtra("cartItems")) {
            cartItems = getIntent().getParcelableArrayListExtra("cartItems");
            if (cartItems != null && !cartItems.isEmpty()) {
                SearchFragment.LaptopProduct product = cartItems.get(0);
                displayProductInfo(product);
            }
        }

        // Xử lý sự kiện khi nút "Thanh toán khi nhận hàng" được nhấn
        codButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển qua BillFragment và đính kèm dữ liệu
                Intent intent = new Intent(PayFragment.this, BillFragment.class);

                // Tạo một Bundle để đóng gói thông tin sản phẩm và thông tin người nhập
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("cartItems", cartItems);
                bundle.putString("address", addressEditText.getText().toString());
                bundle.putString("name", nameEditText.getText().toString());
                bundle.putString("phone", phoneEditText.getText().toString());

                // Đính kèm Bundle vào Intent
                intent.putExtras(bundle);

                // Thay vì sử dụng startActivity(), bạn cần sử dụng startActivityForResult()
                startActivityForResult(intent, 1);
            }
        });


    }


    private void displayProductInfo(SearchFragment.LaptopProduct product) {
        Glide.with(this).load(product.getImageUrl()).into(productImageView);
        productNameTextView.setText(product.getName());
        productPriceTextView.setText(getString(R.string.price_format, product.getOldPrice()));
        productQuantityTextView.setText(getString(R.string.quantity_format, product.getQuantity()));
    }
}
