package com.example.appbanlaptop.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.appbanlaptop.R;
import com.example.appbanlaptop.fragment.SearchFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends ArrayAdapter<SearchFragment.LaptopProduct> {
    SharedPreferences sharedPreferences;
    public CartAdapter(Context context, List<SearchFragment.LaptopProduct> products) {
        super(context, 0, products);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_laptop_product_cart, parent, false);
        }

        SearchFragment.LaptopProduct product = getItem(position);

        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView oldPriceTextView = convertView.findViewById(R.id.oldPriceTextView);
        TextView discountTextView = convertView.findViewById(R.id.discountTextView);
        TextView quantityTextView = convertView.findViewById(R.id.quantityTextView);
        ImageButton increaseQuantityButton = convertView.findViewById(R.id.increaseQuantityButton);
        ImageButton decreaseQuantityButton = convertView.findViewById(R.id.decreaseQuantityButton);
        ImageButton removeButton = convertView.findViewById(R.id.removeButton);

        if (product != null) {
            Glide.with(getContext()).load(product.getImageUrl()).into(imageView);
            nameTextView.setText(product.getName());
            oldPriceTextView.setText(getContext().getString(R.string.price_format, product.getOldPrice()));
            discountTextView.setText(getContext().getString(R.string.discount_format, product.getDiscount()));
            quantityTextView.setText(String.valueOf(product.getQuantity())); // Hiển thị số lượng

            increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Thực hiện tăng số lượng sản phẩm
                    int newQuantity = product.getQuantity() + 1;
                    product.setQuantity(newQuantity);
                    notifyDataSetChanged(); // Cập nhật lại giao diện
                    List<SearchFragment.LaptopProduct> orderList = new ArrayList<>();
                    Gson gson = new Gson();
                    String orderListJson = gson.toJson(orderList);

                    // Lưu trữ chuỗi JSON vào SharedPreferences
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("Orders", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("order_list", orderListJson);
                    editor.apply(); // Lưu thay đổi

                }
            });

            decreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Thực hiện giảm số lượng sản phẩm
                    int newQuantity = product.getQuantity() - 1;
                    if (newQuantity >= 0) { // Đảm bảo số lượng không âm
                        product.setQuantity(newQuantity);
                        notifyDataSetChanged(); // Cập nhật lại giao diện
                    }
                }
            });
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(product);
                    notifyDataSetChanged();
                }
            });
        }

        return convertView;
    }
}
