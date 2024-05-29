package com.example.appbanlaptop.fragment;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.appbanlaptop.manager.CartManager;
import com.example.appbanlaptop.R;
import com.example.appbanlaptop.manager.WishListManager;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class WishListFragment extends Fragment {

    private ListView listView;
    private WishListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        listView = view.findViewById(R.id.listViewWishList);

        List<SearchFragment.LaptopProduct> wishList = WishListManager.getInstance(getContext().getApplicationContext()).getWishListItems();
        adapter = new WishListAdapter(requireContext(), wishList);
        listView.setAdapter((ListAdapter) adapter);

        return view;
    }

    private class WishListAdapter extends ArrayAdapter<SearchFragment.LaptopProduct> {
        public WishListAdapter(@NonNull Context context, @NonNull List<SearchFragment.LaptopProduct> products) {
            super(context, 0, products);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_laptop_product_wishlist, parent, false);
            }

            SearchFragment.LaptopProduct product = getItem(position);

            ImageView imageView = convertView.findViewById(R.id.imageView);
            TextView nameTextView = convertView.findViewById(R.id.nameTextView);
            TextView oldPriceTextView = convertView.findViewById(R.id.oldPriceTextView);
            TextView discountTextView = convertView.findViewById(R.id.discountTextView);
            ImageButton addToCartButton = convertView.findViewById(R.id.addToCartButton);
            ImageButton removeButton = convertView.findViewById(R.id.removeButton);

            if (product != null) {
                Glide.with(getContext()).load(product.getImageUrl()).into(imageView);
                nameTextView.setText(product.getName());
                NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
                String giacu = numberFormat.format(product.getOldPrice()) + " đ";
                String giamoi = numberFormat.format(product.getOldPrice()*(1+product.getDiscount()/100)) +" đ";
                oldPriceTextView.setText(giacu);
                oldPriceTextView.setPaintFlags(oldPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                discountTextView.setText(giamoi);

                addToCartButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CartManager.getInstance(getContext().getApplicationContext()).addToCart(product);
                        Toast.makeText(getContext(), product.getName() + " đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    }
                });

                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WishListManager.getInstance(getContext().getApplicationContext()).removeFromWishList(product);
                        adapter.remove(product);
                        notifyDataSetChanged();
                    }
                });
            }

            return convertView;
        }
    }
}