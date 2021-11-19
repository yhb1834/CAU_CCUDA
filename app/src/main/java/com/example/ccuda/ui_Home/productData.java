package com.example.ccuda.ui_Home;

public class productData {
    private String photo;
    private String productName;
    private String convenientStore;
    private int price;
    private String sellerID;


    public String getPhoto(){ return this.photo; }
    public String getProductName(){ return this.productName; }
    public String getConvenientStore(){ return this.convenientStore; }
    public int getPrice(){ return this.price; }
    public String getSellerID(){ return this.sellerID; }
    public void setPhoto(String icon){ photo=icon; }
    public void setProductName(String name){ productName=name; }
    public void setConvenientStore(String store){ convenientStore=store; }
    public void setPrice(int couponprice){ price=couponprice;}
    public void setSellerID(String seller){ sellerID=seller;}
}
