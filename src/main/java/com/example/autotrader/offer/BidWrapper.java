package com.example.autotrader.offer;

public class BidWrapper {
    private double price;
    private String itemName;

    public BidWrapper() {
        itemName = "";
    }

    public double getPrice() {
        return price;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
