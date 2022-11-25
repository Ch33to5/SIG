package com.ahmed.model;

import java.util.ArrayList;


public class Invoice {
    private int num;
    private String date;
    private String cust;
    private ArrayList<Item> Items;

    public Invoice() {
    }

    public Invoice(int num, String date, String cust) {
        this.num = num;
        this.date = date;
        this.cust = cust;
    }
    
    public double getInvoiceTotal() {
        double total = 0.0;
        for (Item item : getItems()) {
            total += item.getItemTotal();
        }
        return total;
    }

    public ArrayList<Item> getItems() {
        if (Items == null) {
            Items = new ArrayList<>();
        }
        return Items;
    }


    public String getCust() {
        return cust;
    }

    public void setCust(String cust) {
        this.cust = cust;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Invoice{" + "num=" + num + ", date=" + date + ", cust=" + cust + '}';
    }


    public String getSaveFile() {
        return num + "," + date + "," + cust;
    }
    
    
}
