package com.example.appbanlaptop.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appbanlaptop.R;
import com.example.appbanlaptop.activity.AccountSettingActivity;
import com.example.appbanlaptop.activity.MainActivity;

import java.util.HashMap;
import java.util.Map;

public class UserFragment extends Fragment {

    public UserFragment() {
        // Required empty public constructor
    }
    TextView userName;
    static TextView email;
    TextView phone;
    Button btnOverview, btnAccount, btnSetting, btnLogout;
    SharedPreferences sharedPreferences;
    LoginFragment loginFragment;
    ImageView imageAvt;
    LinearLayout account_user;

    ImageButton btnDrop;
    @SuppressLint("MissingInflatedId")
    @Override


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user, container, false);
        btnLogout = v.findViewById(R.id.btnLogout);
        btnOverview = v.findViewById(R.id.btnOverview);
        btnAccount = v.findViewById(R.id.btnAccount);
        btnSetting = v.findViewById(R.id.btnSetting);
        btnDrop = v.findViewById(R.id.btnDrop);
        imageAvt = v.findViewById(R.id.avtImg);
        userName = v.findViewById(R.id.user_name);
        account_user = v.findViewById(R.id.accountUser);
        email = v.findViewById(R.id.user_email);
        account_user.setVisibility(View.GONE);
        loginFragment = new LoginFragment();
        sharedPreferences = getContext().getSharedPreferences("APPBANLAPTOP", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("logged", "false").equals("false")) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
            fm.setCustomAnimations(
                    R.anim.slide_in_left_login_register,
                    R.anim.slide_out_right_login_register
            ).replace(R.id.flFragement, loginFragment).commit();
        }
        userName.setText(sharedPreferences.getString("name",""));
        email.setText(sharedPreferences.getString("email", ""));
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                String url = "https://buihieu204.000webhostapp.com/logout.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("success")){
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("logged", "");
                                    editor.putString("name", "");
                                    editor.putString("email", "");
                                    editor.putString("apiKey", "");
                                    editor.apply();
                                    FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                                    fm.setCustomAnimations(
                                            R.anim.slide_in_left_login_register,
                                            R.anim.slide_out_right_login_register
                                    ).replace(R.id.flFragement, loginFragment).commit();
                                }
                                else{
                                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                                    Log.d("res",response);
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("email", sharedPreferences.getString("email", ""));
                        paramV.put("apiKey", sharedPreferences.getString("apiKey", ""));
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });

        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account_user.getVisibility() == View.GONE) {
                    account_user.setVisibility(View.VISIBLE);
                }
                else {
                    account_user.setVisibility(View.GONE);
                }
            }
        });

        btnDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account_user.getVisibility() == View.GONE) {
                    account_user.setVisibility(View.VISIBLE);
                }
                else {
                    account_user.setVisibility(View.GONE);
                }
            }
        });

        btnOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountSettingActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }
}