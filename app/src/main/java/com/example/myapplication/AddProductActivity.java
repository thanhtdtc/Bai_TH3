package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class AddProductActivity extends AppCompatActivity {

    private EditText edtName, edtPrice, edtQuantity, edtDescription;
    private Spinner spCategory;
    private SwitchCompat swActive;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        db = new DatabaseHelper(this);
        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtQuantity = findViewById(R.id.edtQuantity);
        edtDescription = findViewById(R.id.edtDescription);
        spCategory = findViewById(R.id.spCategory);
        swActive = findViewById(R.id.swActive);
        Button btnSave = findViewById(R.id.btnSave);

        String[] categories = {"Điện tử", "Thực phẩm", "Văn phòng phẩm", "Gia dụng", "Khác"};
        spCategory.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, categories));

        findViewById(R.id.btnHeaderBack).setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> save());
    }

    private void save() {
        String name = edtName.getText().toString().trim();
        String priceStr = edtPrice.getText().toString().trim();
        String qtyStr = edtQuantity.getText().toString().trim();

        if (name.isEmpty()) {
            edtName.setError("Tên sản phẩm không được để trống");
            edtName.requestFocus();
            return;
        }

        long price;
        try {
            price = Long.parseLong(priceStr);
        } catch (NumberFormatException e) {
            edtPrice.setError("Giá bán không hợp lệ");
            edtPrice.requestFocus();
            return;
        }
        if (price <= 0) {
            edtPrice.setError("Giá bán phải lớn hơn 0");
            edtPrice.requestFocus();
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(qtyStr);
        } catch (NumberFormatException e) {
            edtQuantity.setError("Số lượng không hợp lệ");
            edtQuantity.requestFocus();
            return;
        }
        if (quantity < 0) {
            edtQuantity.setError("Số lượng không được âm");
            edtQuantity.requestFocus();
            return;
        }

        String category = spCategory.getSelectedItem().toString();
        String description = edtDescription.getText().toString().trim();

        Product p = new Product(name, price, quantity, category, swActive.isChecked(), description);
        db.insertProduct(p);

        Toast.makeText(this, "Đã lưu sản phẩm", Toast.LENGTH_SHORT).show();
        finish(); // MainActivity.onResume() sẽ tải lại danh sách và thống kê
    }
}