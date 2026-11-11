package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private final ArrayList<Product> products = new ArrayList<>(); // chỉ chứa kết quả truy vấn
    private ProductAdapter adapter;
    private TextView tvCount, tvValue, tvLow, tvEmpty;
    private ListView lvProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        tvCount = findViewById(R.id.tvCount);
        tvValue = findViewById(R.id.tvValue);
        tvLow = findViewById(R.id.tvLow);
        tvEmpty = findViewById(R.id.tvEmpty);
        lvProducts = findViewById(R.id.lvProducts);
        Button btnAdd = findViewById(R.id.btnAdd);

        adapter = new ProductAdapter(this, products);
        lvProducts.setAdapter(adapter);

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddProductActivity.class)));

        lvProducts.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, ProductDetailActivity.class);
            intent.putExtra("product_id", products.get(position).getId());
            startActivity(intent);
        });
    }

    // Tải lại dữ liệu mỗi khi quay về màn hình này (sau thêm / bán / nhập / xóa)
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        products.clear();
        products.addAll(db.getAllProducts());
        adapter.notifyDataSetChanged();

        tvCount.setText(String.valueOf(products.size()));
        tvValue.setText(ProductAdapter.formatMoney(db.getTotalValue()));
        tvLow.setText(String.valueOf(db.countLowStock()));

        boolean empty = products.isEmpty();
        tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        lvProducts.setVisibility(empty ? View.GONE : View.VISIBLE);
    }
}