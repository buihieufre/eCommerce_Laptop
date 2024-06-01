package com.example.appbanlaptop.activity;

import static com.example.appbanlaptop.modal.PreferenceHelper.sharedPreferences;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appbanlaptop.R;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AccountSettingActivity extends AppCompatActivity {
    EditText etUserName, etEmail, etPassword;
    Button btnChangeName, btnChangeEmail, btnChangePass, btnUpdateName, btnUpdateEmail, btnUpdatePass;
    ImageButton btnDropName, btnDropEmail, btnDropPass;
    LinearLayout newName, newEmail, newPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        etUserName = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnUpdateName = findViewById(R.id.btnUpdateName);
        btnUpdateEmail = findViewById(R.id.btnUpdateEmail);
        btnUpdatePass = findViewById(R.id.btnUpdatePass);
        btnChangeName = findViewById(R.id.btnChangeName);
        btnChangeEmail = findViewById(R.id.btnChangeEmail);
        btnChangePass = findViewById(R.id.btnChangePass);
        btnDropName = findViewById(R.id.btnDropName);
        btnDropEmail = findViewById(R.id.btnDropEmail);
        btnDropPass = findViewById(R.id.btnDropPass);
        newName = findViewById(R.id.newName);
        newEmail = findViewById(R.id.newEmail);
        newPass = findViewById(R.id.newPass);

        btnChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVisibility(newName);
            }
        });
        btnDropName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVisibility(newName);
            }
        });
        btnUpdateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = etUserName.getText().toString();
                sendUpdateRequest("name", newName);
            }
        });
        btnChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVisibility(newEmail);
            }
        });
        btnDropEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVisibility(newEmail);
            }
        });
        btnUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle update action
                String newName = etEmail.getText().toString();
                sendUpdateRequest("email", newName);
            }
        });
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVisibility(newPass);
            }
        });
        btnDropPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVisibility(newPass);
            }
        });
        btnUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle update action
                String newName = etPassword.getText().toString();
                sendUpdateRequest("password", newName);
            }
        });
    }
    private void toggleVisibility(LinearLayout layout) {
        if (layout.getVisibility() == View.GONE) {
            layout.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.GONE);
        }
    }

    private void sendUpdateRequest(final String key, final String value) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://your-server-url.com/update_account.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")) {
                            SharedPreferences sharedPreferences = null;
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(key, value);
                            editor.apply();
                            Toast.makeText(AccountSettingActivity.this, "Update successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AccountSettingActivity.this, "Update failed: " + response, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AccountSettingActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("AccountSetting", "Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(key, value);
                ResourceBundle sharedPreferences = null;
                params.put("apikey", sharedPreferences.getString("apikey"));
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
