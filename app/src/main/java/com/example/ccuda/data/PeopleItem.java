package com.example.ccuda.data;

public class PeopleItem {
    String nicname;
    String star;
    String id;
    int resourceId; //프로필 사진
    String coupon_id;
    String roomnum;
    String seller_id;
    String buyer_id;

    public PeopleItem(int resourceId, String nicname, String star,String id, String coupon_id, String roomnum, String seller_id, String buyer_id) {
        this.resourceId = resourceId;
        this.nicname = nicname;
        this.star= star;
        this.id=id;
        this.coupon_id=coupon_id;
        this.roomnum=roomnum;
        this.seller_id = seller_id;
        this.buyer_id = buyer_id;
    }

    public String getNicname() { return nicname; }
    public String getUserId() { return id; }
    public String getStar() { return star; }
    public int getResourceId() {return resourceId;}
    public String getCoupon_id() {return coupon_id;}
    public String getRoomnum(){return roomnum;}

    public String getSeller_id() {
        return seller_id;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

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