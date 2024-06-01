package com.example.appbanlaptop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appbanlaptop.R;
import com.example.appbanlaptop.fragment.SearchFragment;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PaymentProductAdapter extends BaseAdapter {

    private Context context;
    private List<SearchFragment.LaptopProduct> productList;
    private LayoutInflater inflater;

    public PaymentProductAdapter(Context context, List<SearchFragment.LaptopProduct> productList) {
        this.context = context;
        this.productList = productList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_inpaid, parent, false);
            holder = new ViewHolder();
            holder.productImage = convertView.findViewById(R.id.productImageView);
            holder.productName = convertView.findViewById(R.id.productNameTextView);
            holder.productPrice = convertView.findViewById(R.id.productPriceTextView);
            holder.productQuantity = convertView.findViewById(R.id.productQuantityTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SearchFragment.LaptopProduct product = productList.get(position);
        Picasso.get().load(product.getImageUrl()).into(holder.productImage);
        holder.productName.setText(product.getName());
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
        String gia = numberFormat.format(product.getOldPrice()*(1+product.getDiscount()/100)) +" đ";
        holder.productPrice.setText(gia);
        holder.productQuantity.setText(String.valueOf(product.getQuantity()));

        return convertView;
    }

    private static class ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView productQuantity;
    }
}
