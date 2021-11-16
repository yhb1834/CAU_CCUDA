package com.example.ccuda.ui_Home;

public class productData {
    private String photo;
    private String productName;
    private String convenientStore;



    public String getPhoto(){ return this.photo; }
    public String getProductName(){ return this.productName; }
    public String getConvenientStore(){ return this.convenientStore; }
    public void setPhoto(String icon){ photo=icon; }
    public void setProductName(String name){ productName=name; }
    public void setConvenientStore(String store){ convenientStore=store; }
}
