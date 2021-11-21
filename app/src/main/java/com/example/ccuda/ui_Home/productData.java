package com.example.ccuda.ui_Home;

public class productData {
    private String photo;
    private String productName;
    private String convenientStore;
    private int price;
    private String validity;
    private String coupon_id;
    private String seller_id;
    private String seller_nicname;
    private String seller_score;

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getSeller_nicname() {
        return seller_nicname;
    }

    public void setSeller_nicname(String seller_nicname) {
        this.seller_nicname = seller_nicname;
    }

    public String getSeller_score() {
        return seller_score;
    }

    public void setSeller_score(String seller_score) {
        this.seller_score = seller_score;
    }

    public String getCoupon_id() { return this.coupon_id;}
    public String getPhoto(){ return this.photo; }
    public String getProductName(){ return this.productName; }
    public String getConvenientStore(){ return this.convenientStore; }
    public int getPrice(){ return this.price; }
    public String getValidity(){ return this.validity; }

    public void setCoupon_id(String couponid){ coupon_id = couponid;}
    public void setPhoto(String icon){ photo=icon; }
    public void setProductName(String name){ productName=name; }
    public void setConvenientStore(String store){ convenientStore=store; }
    public void setPrice(int couponprice){ price=couponprice;}
    public void setValidity(String date){ validity=date;}
}
