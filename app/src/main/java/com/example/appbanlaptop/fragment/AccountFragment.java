package com.example.appbanlaptop.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.appbanlaptop.R;

public class AccountFragment extends Fragment {
    CheckBox agreePrivacy;
    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, container, false);
        agreePrivacy = v.findViewById(R.id.cbAgreeTheTerms);
        float alphaCheckBox = agreePrivacy.getAlpha();
        agreePrivacy.setOnClickListener(v1 -> {
            if (!agreePrivacy.isChecked()){
                agreePrivacy.setAlpha(alphaCheckBox);
            }else {
                agreePrivacy.setAlpha(1);
            }
        });
        return v;
    }
}