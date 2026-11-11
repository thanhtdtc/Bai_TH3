package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private int productId;
    private Product product;

    private TextView tvName, tvCategory, tvStatus, tvPrice, tvQty, tvLow, tvValue, tvDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        db = new DatabaseHelper(this);
        productId = getIntent().getIntExtra("product_id", -1);

        tvName = findViewById(R.id.tvDetailName);
        tvCategory = findViewById(R.id.tvDetailCategory);
        tvStatus = findViewById(R.id.tvDetailStatus);
        tvPrice = findViewById(R.id.tvDetailPrice);
        tvQty = findViewById(R.id.tvDetailQty);
        tvLow = findViewById(R.id.tvDetailLow);
        tvValue = findViewById(R.id.tvDetailValue);
        tvDesc = findViewById(R.id.tvDetailDesc);

        findViewById(R.id.btnHeaderBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Nhập thêm 1
        findViewById(R.id.btnImport).setOnClickListener(v -> {
            if (product == null) return;
            db.updateQuantity(productId, product.getQuantity() + 1);
            loadProduct();
        });

        // Bán 1
        findViewById(R.id.btnSell).setOnClickListener(v -> {
            if (product == null) return;
            if (product.getQuantity() <= 0) {
                Toast.makeText(this, "Hết hàng", Toast.LENGTH_SHORT).show();
                return;
            }
            db.updateQuantity(productId, product.getQuantity() - 1);
            loadProduct();
        });

        // Xóa sản phẩm
        findViewById(R.id.btnDelete).setOnClickListener(v -> {
            db.deleteProduct(productId);
            Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
        });

        loadProduct();
    }

    // Truy vấn lại theo id rồi hiển thị
    private void loadProduct() {
        product = db.getProductById(productId);
        if (product == null) {
            finish();
            return;
        }
        tvName.setText(product.getName());
        tvCategory.setText(product.getCategory());
        tvStatus.setText(product.isActive() ? "Đang kinh doanh" : "Ngừng kinh doanh");
        tvPrice.setText(ProductAdapter.formatMoney(product.getPrice()));
        tvQty.setText(String.valueOf(product.getQuantity()));
        tvValue.setText(ProductAdapter.formatMoney(product.getStockValue()));
        tvLow.setVisibility(product.isLowStock() ? View.VISIBLE : View.GONE);
        tvDesc.setText(product.getDescription().isEmpty()
                ? "(không có)" : product.getDescription());
    }
}