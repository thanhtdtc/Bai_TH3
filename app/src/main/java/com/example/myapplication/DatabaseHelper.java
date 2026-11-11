package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "warehouse.db";
    public static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL UNIQUE, "
                + "password TEXT NOT NULL)");

        db.execSQL("CREATE TABLE products ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "price INTEGER NOT NULL, "
                + "quantity INTEGER NOT NULL, "
                + "category TEXT NOT NULL, "
                + "active INTEGER NOT NULL, "
                + "description TEXT)");

        // Tài khoản mẫu
        insertUser(db, "admin", "123456");

        // Sản phẩm mẫu
        insertProduct(db, new Product("Chuột không dây", 150000, 20, "Điện tử", true, ""));
        insertProduct(db, new Product("Bàn phím cơ", 450000, 3, "Điện tử", true,
                "Bàn phím cơ switch xanh, có đèn nền"));
        insertProduct(db, new Product("Sổ tay A5", 25000, 40, "Văn phòng phẩm", true, ""));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS products");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    // ---------- users ----------
    private long insertUser(SQLiteDatabase db, String username, String password) {
        ContentValues v = new ContentValues();
        v.put("username", username);
        v.put("password", password);
        return db.insert("users", null, v);
    }

    public long insertUser(String username, String password) {
        return insertUser(getWritableDatabase(), username, password);
    }

    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("users", new String[]{"id"},
                "username = ? AND password = ?",
                new String[]{username, password}, null, null, null);
        boolean ok = c.getCount() > 0;
        c.close();
        return ok;
    }

    // ---------- products ----------
    private long insertProduct(SQLiteDatabase db, Product p) {
        ContentValues v = new ContentValues();
        v.put("name", p.getName());
        v.put("price", p.getPrice());
        v.put("quantity", p.getQuantity());
        v.put("category", p.getCategory());
        v.put("active", p.isActive() ? 1 : 0);
        v.put("description", p.getDescription());
        return db.insert("products", null, v);
    }

    public long insertProduct(Product p) {
        return insertProduct(getWritableDatabase(), p);
    }

    private Product fromCursor(Cursor c) {
        return new Product(
                c.getInt(c.getColumnIndexOrThrow("id")),
                c.getString(c.getColumnIndexOrThrow("name")),
                c.getLong(c.getColumnIndexOrThrow("price")),
                c.getInt(c.getColumnIndexOrThrow("quantity")),
                c.getString(c.getColumnIndexOrThrow("category")),
                c.getInt(c.getColumnIndexOrThrow("active")) == 1,
                c.getString(c.getColumnIndexOrThrow("description")));
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("products", null, null, null, null, null, "id ASC");
        while (c.moveToNext()) {
            list.add(fromCursor(c));
        }
        c.close();
        return list;
    }

    public Product getProductById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("products", null, "id = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        Product p = null;
        if (c.moveToFirst()) {
            p = fromCursor(c);
        }
        c.close();
        return p;
    }

    public int updateQuantity(int id, int quantity) {
        ContentValues v = new ContentValues();
        v.put("quantity", quantity);
        return getWritableDatabase().update("products", v, "id = ?",
                new String[]{String.valueOf(id)});
    }

    public int deleteProduct(int id) {
        return getWritableDatabase().delete("products", "id = ?",
                new String[]{String.valueOf(id)});
    }

    public long getTotalValue() {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT SUM(price * quantity) FROM products", null);
        long total = 0;
        if (c.moveToFirst()) {
            total = c.getLong(0); // SUM rỗng -> null -> 0
        }
        c.close();
        return total;
    }

    public int countLowStock() {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT COUNT(*) FROM products WHERE quantity < ?",
                new String[]{String.valueOf(Product.LOW_STOCK_LIMIT)});
        int n = 0;
        if (c.moveToFirst()) {
            n = c.getInt(0);
        }
        c.close();
        return n;
    }
}