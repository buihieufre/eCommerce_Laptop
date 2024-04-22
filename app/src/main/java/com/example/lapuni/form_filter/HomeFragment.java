package com.example.lapuni.form_filter;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lapuni.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_fragment);

        Spinner outerSpinner = findViewById(R.id.outerSpinner);
        Spinner innerSpinner = findViewById(R.id.innerSpinner);

// Tạo danh sách dữ liệu cho Spinner lớn (outerSpinner)
        List<String> outerList = new ArrayList<>();
        outerList.add("Default");
        outerList.add("Time");
        outerList.add("Price");
        outerList.add("Name");
        outerList.add("Rating");
// Tạo Adapter cho Spinner lớn và gắn dữ liệu vào nó
        ArrayAdapter<String> outerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, outerList);
        outerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        outerSpinner.setAdapter(outerAdapter);

// Xử lý sự kiện khi người dùng chọn một lựa chọn từ Spinner lớn
        outerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                if (selectedItem.equals("Time")) {
                    // Nếu người dùng chọn "Option 1", hiển thị Spinner nhỏ (innerSpinner) và danh sách dữ liệu cho nó
                    innerSpinner.setVisibility(View.VISIBLE);
                    List<String> innerList = new ArrayList<>();
                    innerList.add("Oldest");
                    innerList.add("Neweat");
                    ArrayAdapter<String> innerAdapter = new ArrayAdapter<>(HomeFragment.this,
                            android.R.layout.simple_spinner_item, innerList);
                    innerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    innerSpinner.setAdapter(innerAdapter);
                } else if (selectedItem.equals("Price")) {
                    innerSpinner.setVisibility(View.VISIBLE);
                    List<String> innerList = new ArrayList<>();
                    innerList.add("Low to high");
                    innerList.add("High to low");
                    ArrayAdapter<String> innerAdapter = new ArrayAdapter<>(HomeFragment.this,
                            android.R.layout.simple_spinner_item, innerList);
                    innerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    innerSpinner.setAdapter(innerAdapter);
                } else if (selectedItem.equals("Name")) {
                    innerSpinner.setVisibility(View.VISIBLE);
                    List<String> innerList = new ArrayList<>();
                    innerList.add("A-Z");
                    innerList.add("Z-A");
                    ArrayAdapter<String> innerAdapter = new ArrayAdapter<>(HomeFragment.this,
                            android.R.layout.simple_spinner_item, innerList);
                    innerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    innerSpinner.setAdapter(innerAdapter);
                } else if (selectedItem.equals("Rating")) {
                    innerSpinner.setVisibility(View.VISIBLE);
                    List<String> innerList = new ArrayList<>();
                    innerList.add("Low to high");
                    innerList.add("High to low");
                    ArrayAdapter<String> innerAdapter = new ArrayAdapter<>(HomeFragment.this,
                            android.R.layout.simple_spinner_item, innerList);
                    innerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    innerSpinner.setAdapter(innerAdapter);
                } else if (selectedItem.equals("Default")) {
                    innerSpinner.setVisibility(View.VISIBLE);
                    List<String> innerList = new ArrayList<>();
                    ArrayAdapter<String> innerAdapter = new ArrayAdapter<>(HomeFragment.this,
                            android.R.layout.simple_spinner_item, innerList);
                    innerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    innerSpinner.setAdapter(innerAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}