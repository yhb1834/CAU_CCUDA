package com.example.ccuda.data;

public class PeopleItem {
    String nicname;
    String star;
    String id;
    int resourceId; //프로필 사진
    String coupon_id;

    public PeopleItem(int resourceId, String nicname, String star,String id, String coupon_id) {
        this.resourceId = resourceId;
        this.nicname = nicname;
        this.star= star;
        this.id=id;
        this.coupon_id=coupon_id;
    }

    public String getNicname() { return nicname; }
    public String getUserId() { return id; }
    public String getStar() { return star; }
    public int getResourceId() {return resourceId;}
    public String getCoupon_id() {return coupon_id;}


    public void setStar(String star) {
        this.star = star;
    }
    public void setNicname(String nicname) {
        this.nicname = nicname;
    }
    //public void setUserid(long userid) {this.userid = userid;}
    public void setResourceId(int resourceId){
        this.resourceId = resourceId;
    }
}