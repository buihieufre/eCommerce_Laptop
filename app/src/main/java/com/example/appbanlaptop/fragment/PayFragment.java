package com.example.appbanlaptop.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.appbanlaptop.R;
import com.example.appbanlaptop.adapter.PaymentProductAdapter;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PayFragment extends AppCompatActivity {

    PaymentSheet paymentSheet;
    String paymentIntentClientSecret;
    PaymentSheet.CustomerConfiguration configuration;
    private ArrayList<SearchFragment.LaptopProduct> cartItems;
    private ImageView productImageView;
    private TextView productNameTextView;
    private TextView productPriceTextView;
    private TextView productQuantityTextView;
    private EditText addressEditText;
    private EditText nameEditText;
    private EditText phoneEditText;

    private EditText additionalInfo;
    private RelativeLayout paypayButton;
    private RelativeLayout codButton;

    private Button buttonPaypal;
    private Button buttonCod;

    private CheckBox paypalCheckbox;
    private CheckBox codCheckbox;

    private TextView totalAmount;
    private TextView shippingCost;
    private TextView sumMoney;
    private ListView productList;
    private Button orderBtn;

    private EditText AdditionalInfo;

    String paymentMethod = "";

    String paymentStatus = "";

    private Double tongTienHang = 0.0;
    private Double tongTienGiaoHang = 0.0;
    private Double tongTatCaChiPhi = 0.0;


    private Object orderDetail;

    public double calculateShippingCost(double price) {
        if (price >= 0.0 && price < 30_000_000) {
            return 200_000;
        } else if (price >= 30_000_000 && price < 50_000_000) {
            return 150_000;
        } else if (price >= 50_000_000 && price < 70_000_000) {
            return 100_000;
        } else if (price >= 70_000_000 && price < 100_000_000) {
            return 80_000;
        } else if (price >= 100_000_000 && price < 130_000_000) {
            return 50_000;
        }
        return 0.0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pay);
        // Khởi tạo các thành phần giao diện
        addressEditText = findViewById(R.id.addressEditText);
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        additionalInfo = findViewById(R.id.additionalInfo);
        paypayButton = findViewById(R.id.paypalButton);
        codButton = findViewById(R.id.codButton);

        buttonPaypal = findViewById(R.id.btnCard);
        buttonCod = findViewById(R.id.buttonCod);
        codCheckbox = findViewById(R.id.codCheckbox);
        paypalCheckbox = findViewById(R.id.paypalCheck);
        productList = findViewById(R.id.boughtList);
        orderBtn = findViewById(R.id.Order);
        totalAmount = findViewById(R.id.totalAmount);
        shippingCost = findViewById(R.id.shippingCost);
        sumMoney = findViewById(R.id.sumMoney);
        AdditionalInfo = findViewById(R.id.additionalInfo);

        // Nhận dữ liệu sản phẩm từ Intent
        if (getIntent() != null && getIntent().hasExtra("cartItemsChecked")) {
            cartItems = getIntent().getParcelableArrayListExtra("cartItemsChecked");
            PaymentProductAdapter paymentProductAdapter = new PaymentProductAdapter(getApplicationContext(), cartItems);
            for (int i =0; i<cartItems.size();i++) {
                tongTienHang = tongTienHang + (1 + cartItems.get(i).getDiscount()/100) * cartItems.get(i).getOldPrice() * cartItems.get(i).getQuantity();
            }
            tongTienGiaoHang = calculateShippingCost(tongTienHang);
            tongTatCaChiPhi = tongTienGiaoHang + tongTienHang;
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
            totalAmount.setText("đ" + numberFormat.format(tongTienHang));
            shippingCost.setText("đ" +numberFormat.format(tongTienGiaoHang));
            sumMoney.setText("đ" +numberFormat.format(tongTatCaChiPhi));
            productList.setAdapter(paymentProductAdapter);
        }

        // Xử lý sự kiện khi nút "Thanh toán khi nhận hàng" được nhấn
        buttonPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchApi();
                paypalCheckbox.setChecked(true);
                codCheckbox.setChecked(false);
                paymentMethod = "Thẻ tín dụng";
                paypayButton.setBackground(getDrawable(R.drawable.border_radius_orange_bg));
                codButton.setBackground(getDrawable(R.drawable.border_radius_gray));
                if(paymentIntentClientSecret != null){
                    paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret,
                            new PaymentSheet.Configuration("Bui Hieu", configuration));
                }else{
                    Toast.makeText(PayFragment.this, "API is loading...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonCod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paypalCheckbox.setChecked(false);
                codCheckbox.setChecked(true);
                paymentMethod = "COD";
                paypayButton.setBackground(getDrawable(R.drawable.border_radius_gray));
                codButton.setBackground(getDrawable(R.drawable.border_radius_orange_bg));
            }
        });

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Lấy dữ liệu từ người dùng nhập
                String addInfo = additionalInfo.getText().toString();

                // Tạo Intent để chuyển qua BillFragment và đính kèm dữ liệu
                Intent intent = new Intent(PayFragment.this, BillFragment.class);

                // Tạo một Bundle để đóng gói thông tin sản phẩm và thông tin người nhập
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("cartItems", cartItems);
                bundle.putString("address", addressEditText.getText().toString());
                bundle.putString("name", nameEditText.getText().toString());
                bundle.putString("phone", phoneEditText.getText().toString());
                bundle.putString("paymentMethod", paymentMethod);
                NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
                bundle.putString("totalMoney", numberFormat.format(tongTatCaChiPhi)+ "đ");
                // Đính kèm Bundle vào Intent
                intent.putExtras(bundle);
                LocalDateTime currentDateTime = LocalDateTime.now();
                Log.d("Gio hien tai", currentDateTime.toString());
                String orderStatus = "success";
                Double tongTien = tongTienHang;
                int productId = cartItems.get(0).getId();
                int quantity = cartItems.get(0).getQuantity();
                String shippingAddress = addressEditText.getText().toString();
                String shippingMethod = "Express";
                String shippingCost1 = shippingCost.getText().toString();
                String estimatedDeliveryDate = "3 days";
                SharedPreferences sharedPreferences = getSharedPreferences("APPBANLAPTOP", Context.MODE_PRIVATE);
                String customerEmail = sharedPreferences.getString("email", "false");
                String customerPhone = phoneEditText.getText().toString();
                String notes = AdditionalInfo.getText().toString();
                // Payment method


                // Chèn dữ liệu lên database
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="https://buihieu204.000webhostapp.com/insertOrder.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String message = jsonObject.getString("message");
                                    String status = jsonObject.getString("status");
                                    if(status == "success"){
                                        Log.d("Message: ", message);
                                    }else{
                                        Log.d("Message: ", message);
                                    }
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
                    protected Map<String, String> getParams(){
                        Log.d("KET QUA HOM NAY", nameEditText.getText().toString() +" / " + LocalDateTime.now().toString()+" / "
                                + orderStatus +" / "
                                + tongTien.toString() + " / "
                                + String.valueOf(productId) + " / "
                                + quantity + " / "
                                + paymentMethod + " / "
                                + paymentStatus + " / "
                                + shippingAddress + " / "
                                + shippingCost1.replace("đ", "").replace(".", "") + " / "
                                +estimatedDeliveryDate + " / "
                                + customerEmail + " / "
                                +customerPhone + " / "
                                + notes + " "
                        );
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("customerName", nameEditText.getText().toString());
                        paramV.put("orderDate", LocalDateTime.now().toString());
                        paramV.put("orderStatus", orderStatus);
                        paramV.put("totalAmount", tongTien.toString());
                        paramV.put("productId", String.valueOf(productId));
                        paramV.put("quantity", String.valueOf(quantity));
                        paramV.put("paymentMethod", paymentMethod);
                        paramV.put("paymentStatus", paymentStatus);
                        paramV.put("shippingAddress", shippingAddress);
                        paramV.put("shippingMethod", shippingMethod);
                        paramV.put("shippingCost", shippingCost1.replace("đ", "").replace(".", ""));
                        paramV.put("estimatedDeliveryDate", estimatedDeliveryDate);
                        paramV.put("customerEmail", customerEmail);
                        paramV.put("customerPhone", customerPhone);
                        paramV.put("notes", notes);
                        return paramV;
                    }
                };
                queue.add(stringRequest);

                // Thay vì sử dụng startActivity(), bạn cần sử dụng startActivityForResult()
                startActivityForResult(intent, 1);

            }
        });
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

    }

    public void fetchApi(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="https://buihieu204.000webhostapp.com/stripe/index.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            configuration = new PaymentSheet.CustomerConfiguration(
                                    jsonObject.getString("customer"),
                                    jsonObject.getString("ephemeralKey")
                            );
                            paymentIntentClientSecret = jsonObject.getString("paymentIntent");
                            PaymentConfiguration.init(getApplicationContext(), jsonObject.getString("publishableKey"));
                            paymentStatus = "Thành công";
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("authKey", "abc");
                paramV.put("name", nameEditText.getText().toString());
                paramV.put("line1", addressEditText.getText().toString());
                paramV.put("postal_code", "100000");
                paramV.put("country", "Viet Nam ");
                paramV.put("amount", tongTienHang.toString());
                paramV.put("currency", "VND");
                paramV.put("description", AdditionalInfo.getText().toString());
                Log.d("THANH TOAN", nameEditText.getText().toString() + "/" +addressEditText.getText().toString() + "/" +"100000"+ "/"+"Viet Nam "+"/"+tongTienHang.toString()+"/"+"VND" );
                return paramV;
            }
        };
        queue.add(stringRequest);
    }

    private void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        if(paymentSheetResult instanceof  PaymentSheetResult.Canceled){
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
        }
        if(paymentSheetResult instanceof  PaymentSheetResult.Failed){
            Toast.makeText(this, ((PaymentSheetResult.Failed) paymentSheetResult).getError().getMessage(), Toast.LENGTH_SHORT).show();
        }
        if(paymentSheetResult instanceof  PaymentSheetResult.Completed){
            Toast.makeText(this, "Payment success", Toast.LENGTH_SHORT).show();
        }
    }


    private void displayProductInfo(SearchFragment.LaptopProduct product) {
        Glide.with(this).load(product.getImageUrl()).into(productImageView);
        productNameTextView.setText(product.getName());
        productPriceTextView.setText(getString(R.string.price_format, product.getOldPrice()));
        productQuantityTextView.setText(getString(R.string.quantity_format, product.getQuantity()));
    }

}
