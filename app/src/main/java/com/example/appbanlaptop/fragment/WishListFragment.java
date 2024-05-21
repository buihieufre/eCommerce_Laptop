package com.example.appbanlaptop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.appbanlaptop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WishListFragment extends Fragment {

    private ListView listView;
    private LaptopProductAdapter adapter;
    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        listView = view.findViewById(R.id.listViewWishList);
        requestQueue = Volley.newRequestQueue(requireContext());

        fetchWishList();

        return view;
    }

    private void fetchWishList() {
        String url = "https://raw.githubusercontent.com/hieumai1507/api/main/product.json";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<LaptopProduct> wishList = parseJSON(response);
                        if (wishList != null) {
                            adapter = new LaptopProductAdapter(requireContext(), wishList);
                            listView.setAdapter(adapter);
                        } else {
                            Toast.makeText(requireContext(), "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                    return Response.success(new JSONArray(jsonString), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };

        // Thêm yêu cầu vào hàng đợi
        requestQueue.add(request);
    }

    private List<LaptopProduct> parseJSON(JSONArray jsonArray) {
        List<LaptopProduct> wishList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject productJSON = jsonArray.getJSONObject(i);
                int id = productJSON.getInt("id");
                String name = productJSON.getString("tensp");
                String imageUrl = productJSON.getString("anhsp"); // URL của ảnh thay vì ID tài nguyên drawable
                String ssd = productJSON.getString("ssd");
                String ram = productJSON.getString("ram");
                double oldPrice = productJSON.getDouble("giacu");
                double discount = productJSON.getDouble("discount");

                LaptopProduct product = new LaptopProduct(id, name, imageUrl, ssd, ram, oldPrice, discount);
                wishList.add(product);
            }
            return wishList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class LaptopProduct {
        private int id;
        private String name;
        private String imageUrl; // URL của ảnh thay vì ID tài nguyên drawable
        private String ssd;
        private String ram;
        private double oldPrice;
        private double discount;

        public LaptopProduct(int id, String name, String imageUrl, String ssd, String ram, double oldPrice, double discount) {
            this.id = id;
            this.name = name;
            this.imageUrl = imageUrl;
            this.ssd = ssd;
            this.ram = ram;
            this.oldPrice = oldPrice;
            this.discount = discount;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getSsd() {
            return ssd;
        }

        public String getRam() {
            return ram;
        }

        public double getOldPrice() {
            return oldPrice;
        }

        public double getDiscount() {
            return discount;
        }
    }

    public class LaptopProductAdapter extends ArrayAdapter<LaptopProduct> {
        public LaptopProductAdapter(@NonNull Context context, @NonNull List<LaptopProduct> products) {
            super(context, 0, products);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_laptop_product, parent, false);
            }

            LaptopProduct product = getItem(position);

            ImageView imageView = convertView.findViewById(R.id.imageView);
            TextView nameTextView = convertView.findViewById(R.id.nameTextView);
            TextView ssdTextView = convertView.findViewById(R.id.ssdTextView);
            TextView ramTextView = convertView.findViewById(R.id.ramTextView);
            TextView oldPriceTextView = convertView.findViewById(R.id.oldPriceTextView);
            TextView discountTextView = convertView.findViewById(R.id.discountTextView);

            if (product != null) {
                Glide.with(getContext()).load(product.getImageUrl()).into(imageView); // Sử dụng Glide để tải ảnh từ URL
                nameTextView.setText(product.getName());
                ssdTextView.setText("SSD: " + product.getSsd());
                ramTextView.setText("RAM: " + product.getRam());
                oldPriceTextView.setText("Giá cũ: " + String.format(Locale.getDefault(), "$%.2f", product.getOldPrice()));
                discountTextView.setText("Giảm giá: " + String.format(Locale.getDefault(), "%.0f%% off", product.getDiscount()));

                // Set layout params for the imageView to match parent width and wrap content height
                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                imageView.setLayoutParams(layoutParams);
                imageView.setAdjustViewBounds(true); // Maintain aspect ratio
            }

            return convertView;
        }
    }
}
