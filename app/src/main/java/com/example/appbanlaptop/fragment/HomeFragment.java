package com.example.appbanlaptop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.appbanlaptop.R;
import com.example.appbanlaptop.adapter.LoaiSpAdapter;
import com.example.appbanlaptop.modal.LoaiSp;
import com.example.appbanlaptop.retrofit.ApiBanHang;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class HomeFragment extends Fragment {
    ViewFlipper viewFlipper ;
    ListView listViewHome;
    List<LoaiSp> mangloaisp;
    LoaiSpAdapter loaiSpAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    private void ActionViewFlipper() {
        List<String> advertisesArr = new ArrayList<>();
        advertisesArr.add("https://laptop88.vn/media/banner/01_Feb6813d87d91bb2e8de4c30674952329d1.jpg");
        advertisesArr.add("https://laptop88.vn/media/banner/11_Aprc2daeb6ca338a0bc1899b44ba4cee05c.jpg");
        advertisesArr.add("https://laptop88.vn/media/banner/15_Feb8f2461585e7a0f87b2769e5a8d65c67e.jpg");
        for(int i = 0; i<advertisesArr.size(); i++){
            ImageView imageView = new ImageView(getContext());
            Glide.with(getContext()).load(advertisesArr.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);

    }

    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        viewFlipper = v.findViewById(R.id.viewFlipHome);
        ActionViewFlipper();
        listViewHome =v.findViewById(R.id.listViewHome);
        return v;
    }
}