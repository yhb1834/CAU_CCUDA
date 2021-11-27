package com.example.ccuda.ui_Recipe;

public class RegiItemsModel {
    private String convName;
    private String imageurl;
    private int itemid;
    private String itemname;

    public RegiItemsModel(String storename, int item_id, String item_name) {
        this.convName = storename;
        this.itemid = item_id;
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

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public RegiItemsModel(String convName, String imageurl, int itemid, String itemname) {
        this.convName = convName;
        this.imageurl = imageurl;
        this.itemid = itemid;
        this.itemname = itemname;
    }

}
