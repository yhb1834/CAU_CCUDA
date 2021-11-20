package com.example.ccuda.ui_Home;

public class productData {
    private String photo;
    private String productName;
    private String convenientStore;
    private int price;
    private String validity;
    private String item_id;


    public String getItem_id() { return this.item_id;}
    public String getPhoto(){ return this.photo; }
    public String getProductName(){ return this.productName; }
    public String getConvenientStore(){ return this.convenientStore; }
    public int getPrice(){ return this.price; }
    public String getValidity(){ return this.validity; }

    public void setItem_id(String coupon_id){ item_id=coupon_id;}
    public void setPhoto(String icon){ photo=icon; }
    public void setProductName(String name){ productName=name; }
    public void setConvenientStore(String store){ convenientStore=store; }
    public void setPrice(int couponprice){ price=couponprice;}
    public void setValidity(String date){ validity=date;}
}
