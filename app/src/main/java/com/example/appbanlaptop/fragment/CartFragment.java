package com.example.appbanlaptop.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

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

    private ImageButton removeBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        listView = view.findViewById(R.id.listViewCart);
        checkoutButton = view.findViewById(R.id.checkoutButton);
        removeBtn = listView.findViewById(R.id.removeButton);

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
                List<SearchFragment.LaptopProduct> isCheckedList = getCheckedProducts();
                intent.putParcelableArrayListExtra("cartItemsChecked", (ArrayList<? extends Parcelable>) new ArrayList<>(isCheckedList));
                if(cart.isEmpty()){
                    Toast.makeText(getContext().getApplicationContext(), "Vui lòng thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                }else {
                    if(isCheckedList.isEmpty()){
                        Toast.makeText(getContext().getApplicationContext(), "Hãy chọn sản phẩm bạn muốn mua", Toast.LENGTH_SHORT).show();
                    }else{
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("APPBANLAPTOP", Context.MODE_PRIVATE);
                        if(sharedPreferences.getString("logged", "false").equals("false")){
                            Toast.makeText(getContext().getApplicationContext(), "Đăng nhập trước khi thanh toán", Toast.LENGTH_SHORT).show();
                        }else{
                            startActivity(intent);
                        }
                    }
                }
            }
        });

        return view;
    }
    private List<SearchFragment.LaptopProduct> getCheckedProducts() {
        List<SearchFragment.LaptopProduct> checkedProducts = adapter.getCheckedProducts();
        return checkedProducts;
        // Do something with the checked products
    }
}
