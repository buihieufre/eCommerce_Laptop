package com.example.appbanlaptop.modal;

public class ProductBoughtModal {
    private String image;
    private String name;
    private double price;
    private int quantity;

    public ProductBoughtModal(String image, String name, double price, int quantity) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public ProductBoughtModal() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
