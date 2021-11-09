package com.example.ccuda.data;

import android.graphics.Bitmap;

public class ItemData {
    private int id;
    private String name;
    private String category;
    private String plustype;
    private String storename;
    private int price;      // 원가
    private int price2;     // 개당 가격
    private Bitmap image;   // 물품 이미지


    public int getItemid() {
        return id;
    }
    public String getItemname() {
        return name;
    }
    public String getCategory() {
        return category;
    }
    public String getPlustype() {
        return plustype;
    }
    public String getStorename() {
        return storename;
    }
    public int getItemprice() {
        return price;
    }
    public int getItemprice2() {
        return price2;
    }
    public Bitmap getImage() {
        return image;
    }
    public void setItemid(int id) {
        this.id = id;
    }
    public void setItemname(String name) {
        this.name = name;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setPlustype(String plustype) {
        this.plustype = plustype;
    }
    public void setStorename(String storename) {
        this.storename = storename;
    }
    public void setItemprice(int price) {
        this.price = price;
    }
    public void setItemprice2(int price2) {
        this.price2 = price2;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }
}
