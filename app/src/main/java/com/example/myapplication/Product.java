package com.example.myapplication;

public class Product {
    public static final int LOW_STOCK_LIMIT = 5;

    private int id;
    private String name;
    private long price;
    private int quantity;
    private String category;
    private boolean active;
    private String description;

    public Product(int id, String name, long price, int quantity,
                   String category, boolean active, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.active = active;
        this.description = description;
    }

    // Dùng khi thêm mới (chưa có id)
    public Product(String name, long price, int quantity,
                   String category, boolean active, String description) {
        this(0, name, price, quantity, category, active, description);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public long getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getCategory() { return category; }
    public boolean isActive() { return active; }
    public String getDescription() { return description; }

    public boolean isLowStock() { return quantity < LOW_STOCK_LIMIT; }
    public long getStockValue() { return price * quantity; }
}