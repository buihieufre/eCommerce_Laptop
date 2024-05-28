package com.example.appbanlaptop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appbanlaptop.R;
import com.example.appbanlaptop.adapter.CartAdapter;
import com.example.appbanlaptop.manager.CartManager;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private ListView listView;
    private CartAdapter adapter;
    private Button checkoutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        listView = view.findViewById(R.id.listViewCart);
        checkoutButton = view.findViewById(R.id.checkoutButton);

        // Khởi tạo danh sách sản phẩm giỏ hàng và adapter
        List<SearchFragment.LaptopProduct> cart = CartManager.getInstance(getContext().getApplicationContext()).getCart();
        adapter = new CartAdapter(requireContext(), cart);
        listView.setAdapter(adapter);

        // Xử lý sự kiện khi ấn nút thanh toán
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Truyền dữ liệu sản phẩm qua PayFragment
                Intent intent = new Intent(getContext(), PayFragment.class);
                intent.putParcelableArrayListExtra("cartItems", (ArrayList<? extends Parcelable>) new ArrayList<>(cart));
                startActivity(intent);
            }
        });

        return view;
    }
}
