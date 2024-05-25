package com.example.appbanlaptop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appbanlaptop.R;
import com.example.appbanlaptop.activity.DetailActivity;
import com.example.appbanlaptop.modal.Product;
import com.example.appbanlaptop.retrofit.detailsLaptop;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAdapter extends BaseAdapter {

    private Context mContext;
    private List<Product> mProductList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.mContext = context;
        this.mProductList = productList;
    }

    @Override
    public int getCount() {
        return mProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //tao view
        ViewHolder holder;
        if (convertView == null) {
            //tao view moi
            //object generate layout
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_view, parent, false);
            holder = new ViewHolder();
            //anh xa tung truong
            holder.imageView=convertView.findViewById(R.id.item_product_imageView);
            holder.nameTv = convertView.findViewById(R.id.item_nameProduct_textview);
            holder.ramTv = convertView.findViewById(R.id.item_ramProduct_textview);
            holder.ssdTv = convertView.findViewById(R.id.item_ssdProduct_textview);
            holder.oldpriceTv = convertView.findViewById(R.id.item_oldpriceProduct_textview);
            holder.discountTv = convertView.findViewById(R.id.item_discountProduct_textview);
            //tao template for later
            convertView.setTag(holder);
        }
        else {
            //lay ve view cu
            holder=(ViewHolder) convertView.getTag();
        }
        //set data cho view
        Product product = mProductList.get(position);
        if (product!=null){
            //hien thi anh
            Picasso.get().load(product.getImageProduct()).placeholder(R.drawable.ic_launcher_background).into(holder.imageView);
            //hien thi thong tin
            holder.nameTv.setText(product.getNameProduct());
            holder.ramTv.setText(product.getRamProduct());
            holder.ssdTv.setText(product.getSsdProduct());
            holder.oldpriceTv.setText(product.getOldPriceProduct());
            holder.discountTv.setText(product.getDiscountProduct());
        }
        //xu ly su kien
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsLaptop detailproduct = new detailsLaptop();
                Product product = mProductList.get(position); //lay ve doi tuong da chon
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("PRODUCT", product); //dong goi product
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                RequestQueue requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
                // URL of the server endpoint
                String url = "https://buihieu204.000webhostapp.com/api/processorV2.json";

                // Create a new JsonObjectRequest
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                JSONArray jsonArray;
                                JSONObject productdetail;
                                // Handle the response
                                try {
                                    jsonArray = response.getJSONArray("data");
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                try {
                                    productdetail = jsonArray.getJSONObject(position);
//                                    detailproduct.setAnhsp(productdetail.getString("anhsp"));
//                                    detailproduct.setTensp(productdetail.getString("tensp"));
//                                    detailproduct.setRam(productdetail.getString("ram"));
//                                    detailproduct.setSsd(productdetail.getString("ssd"));
//                                    detailproduct.setGiacu(productdetail.getString("giacu"));
//                                    detailproduct.setDiscount(productdetail.getString("discount"));
                                    detailproduct.setCpu(productdetail.getString("cpu"));
                                    detailproduct.setSoNhan(productdetail.getString("soNhan"));
                                    detailproduct.setSoLuong(productdetail.getString("soLuong"));
                                    detailproduct.setTocDoCpu(productdetail.getString("tocDoCpu"));
                                    detailproduct.setTocDoToiDa(productdetail.getString("tocDoToiDa"));
                                    detailproduct.setBoNhoDem(productdetail.getString("boNhoDem"));
                                    detailproduct.setLoaiRam(productdetail.getString("loaiRam"));
                                    detailproduct.setTocDoBusRam(productdetail.getString("tocDoBusRam"));
                                    detailproduct.setHoTroRamToiDa(productdetail.getString("hoTroRamToiDa"));
                                    detailproduct.setoCung(productdetail.getString("oCung"));
                                    detailproduct.setManHinh(productdetail.getString("manHinh"));
                                    detailproduct.setDoPhanGiai(productdetail.getString("doPhanGiai"));
                                    detailproduct.setTanSoQuet(productdetail.getString("tanSoQuet"));
                                    detailproduct.setCongNgheManHinh(productdetail.getString("congNgheManHinh"));
                                    detailproduct.setCardManHinh(productdetail.getString("cardManHinh"));
                                    detailproduct.setCongGiaoTiep(productdetail.getString("congGiaoTiep"));
                                    detailproduct.setKetNoiKhongDay(productdetail.getString("ketNoiKhongDay"));
                                    detailproduct.setPin(productdetail.getString("pin"));
                                    detailproduct.setCongSuatSac(productdetail.getString("congSuatSac"));
                                    intent.putExtra("DETAILPRODUCT", detailproduct);
                                    try {
                                        mContext.startActivity(intent); //van chuyen product sang detail
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle the error response
                                if (error instanceof AuthFailureError) {
                                    // Handle authentication failure
                                    System.err.println("Authentication failed: " + error.getMessage());
                                } else {
                                    // Handle other types of errors
                                    System.err.println("Error: " + error.toString());
                                }
                            }
                        }
                ){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("User-Agent", "Mozilla/5.0");
                        return headers;
                    }
                };

                // Add the request to the RequestQueue
                requestQueue.add(jsonObjectRequest);

            }
        });
        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView idTv, nameTv, ramTv, ssdTv, oldpriceTv, discountTv;
    }
}
