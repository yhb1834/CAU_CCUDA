package com.example.ccuda.ui_Recipe;

public class RegiItemsModel {
    private String convName;
    private String imageurl;
    private String itemname;
    private int itemid;

    public RegiItemsModel(String storename, String item_name) {
        this.convName = storename;
        this.itemname = item_name;
    }

    public String getConvName() {
        return convName;
    }

    public void setConvName(String convName) {
        this.convName = convName;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }


    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public RegiItemsModel(String convName, String imageurl, String itemname, int itemid) {
        this.convName = convName;
        this.imageurl = imageurl;
        this.itemname = itemname;
        this.itemid = itemid;
    }
    public RegiItemsModel(String convName, String imageurl, String itemname) {
        this.convName = convName;
        this.imageurl = imageurl;
        this.itemname = itemname;
    }

}
