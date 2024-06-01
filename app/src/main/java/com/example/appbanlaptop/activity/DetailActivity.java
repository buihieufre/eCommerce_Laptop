package com.example.appbanlaptop.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appbanlaptop.R;
import com.example.appbanlaptop.fragment.SearchFragment;
import com.example.appbanlaptop.manager.CartManager;
import com.example.appbanlaptop.manager.WishListManager;
import com.example.appbanlaptop.modal.Product;
import com.example.appbanlaptop.retrofit.detailsLaptop;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class DetailActivity extends AppCompatActivity {
    ImageButton backToHome;

    boolean isLiked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        //nhan du lieu tu Listproduct chuyen sang
        Intent intent = getIntent();
        detailsLaptop detail = intent.getParcelableExtra("DETAILPRODUCT");
        Product product = intent.getParcelableExtra("PRODUCT");
        //anh xa cac thanh phan len giao dien
        ImageView imgPro = findViewById(R.id.detailImg);
        TextView tvNamePro = findViewById(R.id.detailName);
        TextView tvRamPro = findViewById(R.id.detailRam);
        TextView tvSsdPro = findViewById(R.id.detailSsd);
        TextView tvOldPricePro = findViewById(R.id.detailOldprice);
        TextView tvDiscountPro = findViewById(R.id.detailDiscount);
        TextView tvCPUPro = findViewById(R.id.detailCpu);
        TextView tvSoNhanPro = findViewById(R.id.detailSoNhan);
        TextView tvSoLuongPro = findViewById(R.id.detailSoLuong);
        TextView tvTocDoCPUPro = findViewById(R.id.detailTocDoCPU);
        TextView tvTocDoToiDaPro = findViewById(R.id.detailTocDoToiDa);
        TextView tvBoNhoDemPro = findViewById(R.id.detailBoNhoDem);
        TextView tvLoaiRAMPro = findViewById(R.id.detailLoaiRAM);
        TextView tvTocDoBusRAMPro = findViewById(R.id.detailTocDoBusRAM);
        TextView tvHoTroRAMToiDaPro = findViewById(R.id.detailHoTroRAMToiDa);
        TextView tvOCungPro = findViewById(R.id.detailOCung);
        TextView tvManHinhPro = findViewById(R.id.detailManHinh);
        TextView tvDoPhanGiaiPro = findViewById(R.id.detailDoPhanGiai);
        TextView tvTanSoQuetPro = findViewById(R.id.detailTanSoQuet);
        TextView tvCNManHinhPro = findViewById(R.id.detailCNManHinh);
        TextView tvCartManHinhPro = findViewById(R.id.detailCartManHinh);
        TextView tvCongGiaoTiepPro = findViewById(R.id.detailCongGiaoTiep);
        TextView tvKetNoiKoDayPro = findViewById(R.id.detailKetNoiKoDay);
        TextView tvPinPro = findViewById(R.id.detailPin);
        TextView tvCongSuatSacPro = findViewById(R.id.detailCongSuatSac);
        ImageButton like = findViewById(R.id.btnFavorite);
        Button addToCart = findViewById(R.id.btn_addCart);
        //hien thi thong tin chi tiet
        if (detail != null) {
            //hien thi anh
            Picasso.get().load(product.getImageProduct()).into(imgPro);
            //hien thi text
            tvNamePro.setText(product.getNameProduct());
            tvRamPro.setText(product.getRamProduct());
            tvSsdPro.setText(product.getSsdProduct());
            tvOldPricePro.setText(product.getOldPriceProduct());
            tvOldPricePro.setPaintFlags(tvOldPricePro.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvDiscountPro.setText(product.getDiscountProduct());
            tvCPUPro.setText(detail.getCpu());
            tvSoNhanPro.setText(detail.getSoNhan());
            tvSoLuongPro.setText(detail.getSoLuong());
            tvTocDoCPUPro.setText(detail.getTocDoCpu());
            tvTocDoToiDaPro.setText(detail.getTocDoToiDa());
            tvBoNhoDemPro.setText(detail.getBoNhoDem());
            tvLoaiRAMPro.setText(detail.getLoaiRam());
            tvTocDoBusRAMPro.setText(detail.getTocDoBusRam());
            tvHoTroRAMToiDaPro.setText(detail.getHoTroRamToiDa());
            tvOCungPro.setText(detail.getoCung());
            tvManHinhPro.setText(detail.getManHinh());
            tvDoPhanGiaiPro.setText(detail.getDoPhanGiai());
            tvTanSoQuetPro.setText(detail.getTanSoQuet());
            tvCNManHinhPro.setText(detail.getCongNgheManHinh());
            tvCartManHinhPro.setText(detail.getCardManHinh());
            tvCongGiaoTiepPro.setText(detail.getCongGiaoTiep());
            tvKetNoiKoDayPro.setText(detail.getKetNoiKhongDay());
            tvPinPro.setText(detail.getPin());
            tvCongSuatSacPro.setText(detail.getCongSuatSac());
        }

        SearchFragment.LaptopProduct productWish = new SearchFragment.LaptopProduct();
        WishListManager wishListManager = WishListManager.getInstance(getApplicationContext());
        List<SearchFragment.LaptopProduct> wishListItems = wishListManager.getWishListItems();
        productWish.setId(Integer.parseInt(product.getIdProduct()));
        productWish.setName(product.getNameProduct());
        productWish.setImageUrl(product.getImageProduct());
        productWish.setSsd(product.getSsdProduct());
        productWish.setRam(product.getRamProduct());
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
        try {
            Log.d("CHECKER", numberFormat.parse(product.getOldPriceProduct().replace("đ","")).toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        try {
            productWish.setOldPrice(Double.parseDouble(numberFormat.parse(product.getOldPriceProduct().replace("đ","")).toString()));
            productWish.setOldPrice(Double.parseDouble(numberFormat.parse(product.getDiscountProduct().replace("đ","")).toString()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if(!wishListManager.isProductInWishList(getApplicationContext(), productWish)){
            like.setImageResource(R.drawable.heart_outline);
        }else{
            like.setImageResource(R.drawable.heart_actived);
        }
        // Set OnClickListener for the like button
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!wishListManager.isProductInWishList(getApplicationContext(), productWish)) {
                    like.setImageResource(R.drawable.heart_actived);
                    isLiked = true;
                    if (!wishListItems.contains(productWish)) {
                        wishListManager.addToWishList(productWish);
                        Toast.makeText(getApplicationContext(), "Đã được thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Đã có trong danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("DISCOUNT", String.valueOf(productWish.getOldPrice()));
                } else {
                    like.setImageResource(R.drawable.heart_outline);
                    isLiked = false;
                    wishListManager.removeProductFromWishList(getApplicationContext(), productWish.getId());
                }
//                String productName = tvNamePro.getText().toString();
//                Log.d("Liked", "liked " + productName);

            }
        });

        // Set on add to cart

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartManager cartManager = CartManager.getInstance(getApplicationContext());
                List<SearchFragment.LaptopProduct> productListCart = cartManager.getCart();
                if(!cartManager.isProductInCart(productWish.getId())){
                    cartManager.addToCart(productWish);
                    Toast.makeText(getApplicationContext(), "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Đã có trong giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        backToHome = findViewById(R.id.backToHome);
        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean isProductLiked(int productId) {
        SharedPreferences sharedPreferences = getSharedPreferences("FavoriteProducts", Context.MODE_PRIVATE);
        Set<String> likedProductIds = sharedPreferences.getStringSet("likedProductIds", new HashSet<>());
        return  !likedProductIds.isEmpty() && likedProductIds.contains(String.valueOf(productId));
    }

    // Cập nhật trạng thái yêu thích của sản phẩm
    private void updateProductLikedState(int productId, boolean isLiked) {
        SharedPreferences sharedPreferences = getSharedPreferences("FavoriteProducts", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> likedProductIds = sharedPreferences.getStringSet("likedProductIds", new HashSet<>());

        if (isLiked) {
            likedProductIds.add(String.valueOf(productId));
        } else {
            likedProductIds.remove(String.valueOf(productId));
        }

        editor.putStringSet("likedProductIds", likedProductIds);
        editor.apply();
    }
}
