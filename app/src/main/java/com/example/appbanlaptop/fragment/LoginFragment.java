package com.example.appbanlaptop.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appbanlaptop.R;

public class LoginFragment extends Fragment {
    EditText emailEdt;
    EditText passwordEdt;
    CheckBox rememberBtn;
    TextView goToRegister;
    Button btnLogin;
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        emailEdt = v.findViewById(R.id.emailLogin);
        passwordEdt = v.findViewById(R.id.passwordLogin);
        btnLogin = v.findViewById(R.id.loginBtn);
        // Handle when click login btn
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url ="http://172.16.35.194:8080/login_register/login.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity().getApplicationContext(), "Response: " + response,Toast.LENGTH_SHORT).show();
                        if(response.equals("success")){

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(stringRequest);

        // Handle on click to redirect register
        goToRegister = v.findViewById(R.id.redirectRegister);
        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment registerFragment = new RegisterFragment();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.setCustomAnimations(
                        R.anim.slide_in_left_login_register,
                        R.anim.slide_out_right_login_register
                ).replace(R.id.flFragement, registerFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

//      Active remember
        rememberBtn = v.findViewById(R.id.rememberAccount);
        float alphaCheckBox = rememberBtn.getAlpha();
        rememberBtn.setOnClickListener(v1 -> {
            if (!rememberBtn.isChecked()){
                rememberBtn.setAlpha(alphaCheckBox);
            }else {
                rememberBtn.setAlpha(1);
            }
        });
        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("email", emailEdt.getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            emailEdt.setText(savedInstanceState.getString("email"));
        }
    }
}