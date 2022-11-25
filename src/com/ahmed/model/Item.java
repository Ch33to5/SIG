package com.ahmed.model;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;


public class Item {
    private String item;
    private double price;
    private int count;
    private Invoice invoice;

    public Item() {
    }

    public Item(String item, double price, int count, Invoice invoice) {
        this.item = item;
        this.price = price;
        this.count = count;
        this.invoice = invoice;
    }
    
    public double getItemTotal() {
        return price * count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Item{" + "num=" + invoice.getNum() + ", item=" + item + ", price=" + price + ", count=" + count + '}';
    }

    public Invoice getInvoice() {
        return invoice;
    }
    
    public String getSaveFile() {
        return invoice.getNum() + "," + item + "," + price + "," + count;
    }
    
}
