package com.example.appbanlaptop.Favorite_List;

// FavoriteAdapter.java

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.example.appbanlaptop.R;

import java.util.List;

public class FavoriteAdapter extends ArrayAdapter<Product> {
    private Context context;
    private List<Product> favorites;

    public FavoriteAdapter(Context context, List<Product> favorites) {
        super(context, 0, favorites);
        this.context = context;
        this.favorites = favorites;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        }

        Product product = favorites.get(position);

        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        ImageView productImageView = convertView.findViewById(R.id.productImageView);

        nameTextView.setText(product.getName());
        Glide.with(context).load(product.getImageUrl()).into(productImageView);

        return convertView;
    }
}
