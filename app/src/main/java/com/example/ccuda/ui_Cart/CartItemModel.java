package com.example.ccuda.ui_Cart;

//package com.nurisoft.recyclerview.models;

public class CartItemModel {

    private int imageResource;
    private String text1;
    private String text2;
    private int itemid;

    public CartItemModel(int imageResource, String text1, String text2, int itemid) {
        this.imageResource = imageResource;
        this.text1 = text1;
        this.text2 = text2;
        this.itemid = itemid;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public int getItemid(){return itemid;}
}

