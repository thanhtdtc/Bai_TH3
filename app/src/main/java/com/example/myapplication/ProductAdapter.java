package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductAdapter extends ArrayAdapter<Product> {

    public ProductAdapter(@NonNull Context context, @NonNull ArrayList<Product> list) {
        super(context, 0, list);
    }

    public static String formatMoney(long amount) {
        return NumberFormat.getInstance(new Locale("vi", "VN")).format(amount) + " đ";
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_product, parent, false);
        }
        Product p = getItem(position);

        TextView tvQty = convertView.findViewById(R.id.tvItemQty);
        TextView tvLow = convertView.findViewById(R.id.tvItemLow);

        ((TextView) convertView.findViewById(R.id.tvItemName)).setText(p.getName());
        ((TextView) convertView.findViewById(R.id.tvItemPrice))
                .setText("Giá bán: " + formatMoney(p.getPrice()));
        ((TextView) convertView.findViewById(R.id.tvItemCategory)).setText(p.getCategory());
        tvQty.setText("SL: " + p.getQuantity());

        if (p.isLowStock()) {
            tvQty.setTextColor(getContext().getColor(R.color.app_red));
            tvLow.setVisibility(View.VISIBLE);
        } else {
            tvQty.setTextColor(getContext().getColor(R.color.app_text));
            tvLow.setVisibility(View.GONE);
        }
        return convertView;
    }
}