package com.example.ccuda;

public class productData {
    private int photo;
    private String productName;
    private String convenientStore;



    public int getPhoto(){ return this.photo; }
    public String getProductName(){ return this.productName; }
    public String getConvenientStore(){ return this.convenientStore; }
    public void setPhoto(int icon){ photo=icon; }
    public void setProductName(String name){ productName=name; }
    public void setConvenientStore(String store){ convenientStore=store; }
}
