package com.example.appbanlaptop.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.appbanlaptop.R;
import com.example.appbanlaptop.fragment.SearchFragment;
import com.example.appbanlaptop.manager.CartManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends ArrayAdapter<SearchFragment.LaptopProduct> {
    private SparseBooleanArray checkBoxStateArray;
    public CartAdapter(Context context, List<SearchFragment.LaptopProduct> products) {
        super(context, 0, products);
        checkBoxStateArray = new SparseBooleanArray();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CartManager cartManager = CartManager.getInstance(getContext().getApplicationContext());
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
        CheckBox checkBox = convertView.findViewById(R.id.checkToBuy);
        checkBox.setChecked(false);

        if (product != null) {
            Glide.with(getContext()).load(product.getImageUrl()).into(imageView);
            nameTextView.setText(product.getName());
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
            String giacu = numberFormat.format(product.getOldPrice())+ " đ";
            String giamoi = numberFormat.format(product.getOldPrice()*(1+product.getDiscount()/100)) +" đ";
            oldPriceTextView.setText(giacu);
            oldPriceTextView.setPaintFlags(oldPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            discountTextView.setText(giamoi);
            quantityTextView.setText(String.valueOf(product.getQuantity()));

            increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Thực hiện tăng số lượng sản phẩm
                    int newQuantity = product.getQuantity() + 1;
                    product.setQuantity(newQuantity);
                    notifyDataSetChanged(); // Cập nhật lại giao diện
                }
            });

            decreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Thực hiện giảm số lượng sản phẩm
                    int newQuantity = product.getQuantity() - 1;
                    if (newQuantity > 0) { // Đảm bảo số lượng không âm
                        product.setQuantity(newQuantity);
                    } else {
                        remove(product); // Xóa sản phẩm nếu số lượng bằng 0
                    }
                    notifyDataSetChanged(); // Cập nhật lại giao diện
                }
            });

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(product);
                    cartManager.removeFromCart(product);
                    notifyDataSetChanged();
                }
            });
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(product.isIschecked());
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                product.setIschecked(isChecked);
                checkBoxStateArray.put(position, isChecked);
            });
        }

        return convertView;
    }
    public List<SearchFragment.LaptopProduct> getCheckedProducts() {
        List<SearchFragment.LaptopProduct> checkedProducts = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            if (checkBoxStateArray.get(i, false)) {
                checkedProducts.add(getItem(i));
            }
        }
        return checkedProducts;
    }
}
