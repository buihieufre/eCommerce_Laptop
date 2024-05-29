package com.example.appbanlaptop.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.appbanlaptop.manager.CartManager;
import com.example.appbanlaptop.manager.WishListManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class SearchFragment extends Fragment {

    private ListView listView;
    private LaptopProductAdapter adapter;
    private RequestQueue requestQueue;
    private List<LaptopProduct> originalList = new ArrayList<>();
    private List<LaptopProduct> filteredList = new ArrayList<>();
    private EditText searchEditText;
    private List<LaptopProduct> wishList = new ArrayList<>();
    private List<LaptopProduct> cartList = new ArrayList<>();
    static SharedPreferences sharedPreferences;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        listView = view.findViewById(R.id.listViewSearch);
        searchEditText = view.findViewById(R.id.searchEditText);
        requestQueue = Volley.newRequestQueue(requireContext());

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                searchProducts(query);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });

        fetchSearch();

        return view;
    }

    private void fetchSearch() {
        String url = "https://raw.githubusercontent.com/hieumai1507/api/main/product.json";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        originalList = parseJSON(response);
                        if (originalList != null) {
                            filteredList.clear();
                            filteredList.addAll(originalList);
                            adapter = new LaptopProductAdapter(requireContext(), filteredList);
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

        // Add the request to the RequestQueue.
        requestQueue.add(request);
    }

    private List<LaptopProduct> parseJSON(JSONArray jsonArray) {
        List<LaptopProduct> products = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject productJSON = jsonArray.getJSONObject(i);
                int id = productJSON.getInt("id");
                String name = productJSON.getString("tensp");
                String imageUrl = productJSON.getString("anhsp");
                String ssd = productJSON.getString("ssd");
                String ram = productJSON.getString("ram");
                double oldPrice = productJSON.getDouble("giacu");
                double discount = productJSON.getDouble("discount");

                LaptopProduct product = new LaptopProduct(id, name, imageUrl, ssd, ram, oldPrice, discount);
                products.add(product);
            }
            return products;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void searchProducts(String query) {
        query = query.toLowerCase(Locale.getDefault());
        filteredList.clear();
        for (LaptopProduct product : originalList) {
            if (product.getName().toLowerCase(Locale.getDefault()).contains(query)) {
                filteredList.add(product);
            }
        }
        if (adapter != null) {
            adapter.updateList(filteredList);
        } else {
            adapter = new LaptopProductAdapter(getContext(), filteredList);
            listView.setAdapter(adapter);
        }
    }

    public static class LaptopProduct implements Parcelable {
        private int id;
        private String name;
        private String imageUrl;
        private String ssd;
        private String ram;
        private double oldPrice;
        private double discount;
        private int quantity;

        public LaptopProduct(int id, String name, String imageUrl, String ssd, String ram, double oldPrice, double discount) {
            this.id = id;
            this.name = name;
            this.imageUrl = imageUrl;
            this.ssd = ssd;
            this.ram = ram;
            this.oldPrice = oldPrice;
            this.discount = discount;
            this.quantity = 1; // Mặc định số lượng là 1
        }

        public LaptopProduct() {}
        protected LaptopProduct(Parcel in) {
            id = in.readInt();
            name = in.readString();
            imageUrl = in.readString();
            ssd = in.readString();
            ram = in.readString();
            oldPrice = in.readDouble();
            discount = in.readDouble();
            quantity = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeString(imageUrl);
            dest.writeString(ssd);
            dest.writeString(ram);
            dest.writeDouble(oldPrice);
            dest.writeDouble(discount);
            dest.writeInt(quantity);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<LaptopProduct> CREATOR = new Creator<LaptopProduct>() {
            @Override
            public LaptopProduct createFromParcel(Parcel in) {
                return new LaptopProduct(in);
            }

            @Override
            public LaptopProduct[] newArray(int size) {
                return new LaptopProduct[size];
            }
        };

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
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
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LaptopProduct that = (LaptopProduct) o;
            return id == that.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public void setSsd(String ssd) {
            this.ssd = ssd;
        }

        public void setRam(String ram) {
            this.ram = ram;
        }

        public void setOldPrice(double oldPrice) {
            this.oldPrice = oldPrice;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }
    }

    public class LaptopProductAdapter extends ArrayAdapter<LaptopProduct> {
        private List<LaptopProduct> filteredList;

        public LaptopProductAdapter(@NonNull Context context, @NonNull List<LaptopProduct> products) {
            super(context, 0, products);
            this.filteredList = new ArrayList<>(products);
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
            TextView oldPriceTextView = convertView.findViewById(R.id.oldPriceTextView);
            TextView discountTextView = convertView.findViewById(R.id.discountTextView);
            TextView ramTextView = convertView.findViewById(R.id.ramTextView);
            TextView ssdTextView = convertView.findViewById(R.id.ssdTextView);
            ImageButton addToCartButton = convertView.findViewById(R.id.addToCartButton);
            ImageButton wishListButton = convertView.findViewById(R.id.wishListButton);

            if (product != null) {
                Glide.with(getContext()).load(product.getImageUrl()).into(imageView);
                nameTextView.setText(product.getName());
                NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
                String giacu = numberFormat.format(product.getOldPrice()) + " đ";
                String giamoi = numberFormat.format(product.getOldPrice()*(1+product.getDiscount()/100)) + " đ";
                oldPriceTextView.setText(giacu);
                oldPriceTextView.setPaintFlags(oldPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                discountTextView.setText(giamoi);
                ramTextView.setText(getContext().getString(R.string.ram, product.getRam()));
                ssdTextView.setText(getContext().getString(R.string.ssd, product.getSsd()));
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = product.getId();
                    }
                });
                addToCartButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CartManager.getInstance(getContext().getApplicationContext()).addToCart(product);
                        Toast.makeText(getContext(), product.getName() + " đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                        try {
                            CartManager.getInstance(getContext().getApplicationContext()).addToWishList(product);
                            SharedPreferences sharedPreferences1 = getContext().getSharedPreferences(CartManager.getProductList(), Context.MODE_PRIVATE);
                            String json = sharedPreferences1.getString("product_cart_list", "");
                            Type type = new TypeToken<List<LaptopProduct>>(){}.getType();
                            List<SearchFragment.LaptopProduct> productList = new Gson().fromJson(json, type);
                            for(SearchFragment.LaptopProduct laptopProduct: productList){
                                Log.d("ITEM CART", laptopProduct.getName() );
                            }

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });

                wishListButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WishListManager wishListManager = WishListManager.getInstance(getContext().getApplicationContext());
                        List<LaptopProduct> wishListItems = wishListManager.getWishListItems();
                        if (!wishListItems.contains(product)) {
                            wishListManager.addToWishList(product);
                            Toast.makeText(getContext(), "Đã được thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Đã có trong danh sách yêu thích", Toast.LENGTH_SHORT).show();
                        }
                        try {
                            CartManager.getInstance(getContext().getApplicationContext()).addToWishList(product);
                            SharedPreferences sharedPreferences1 = getContext().getSharedPreferences(WishListManager.getProductListName(), Context.MODE_PRIVATE);
                            Set<String> jsonSet = sharedPreferences1.getStringSet("product_set", new HashSet<>());

                            Set<SearchFragment.LaptopProduct> productSet = new HashSet<>();
                            Gson gson = new Gson();
                            for (String json : jsonSet) {
                                SearchFragment.LaptopProduct product = gson.fromJson(json, SearchFragment.LaptopProduct.class);
                                productSet.add(product);
                            }
                            List<SearchFragment.LaptopProduct> productList = new ArrayList<>(productSet);
                            for(SearchFragment.LaptopProduct laptopProduct: productList){
                                Log.d("ITEM LOG", laptopProduct.getName() );
                            }

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return convertView;
        }

        public void updateList(List<LaptopProduct> newList) {
            filteredList.clear();
            filteredList.addAll(newList);
            notifyDataSetChanged();
        }
    }
}

