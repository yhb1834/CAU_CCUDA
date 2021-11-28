package com.example.ccuda.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class CouponData implements Parcelable {
    private int coupon_id;
    private long seller_id;
    private boolean isdeal;
    private String post_date;
    private String seller_name;
    private String seller_score;
    private String item_name;
    private String category;
    private String plustype;
    private String storename;
    private int price;
    private String expiration_date;
    private String content;
    private String imageurl;   // 물품 이미지
    private Bitmap couponimage; // 등록된 쿠폰 이미지 - 마이페이지 등에서 필요시 판매자 본인에게만 공개
    private boolean isClicked;

    public CouponData(){}

    protected CouponData(Parcel in) {
        coupon_id = in.readInt();
        seller_id = in.readLong();
        isdeal = in.readByte() != 0;
        post_date = in.readString();
        seller_name = in.readString();
        seller_score = in.readString();
        item_name = in.readString();
        category = in.readString();
        plustype = in.readString();
        storename = in.readString();
        price = in.readInt();
        expiration_date = in.readString();
        content = in.readString();
        imageurl = in.readString();
        couponimage = in.readParcelable(Bitmap.class.getClassLoader());
        isClicked = in.readByte() != 0;
    }

    public static final Creator<CouponData> CREATOR = new Creator<CouponData>() {
        @Override
        public CouponData createFromParcel(Parcel in) {
            return new CouponData(in);
        }

        @Override
        public CouponData[] newArray(int size) {
            return new CouponData[size];
        }
    };

    public String getCoupon_id(){
        return toString(coupon_id);
    }

    private String toString(int coupon_id) {
        return Integer.toString(coupon_id);
    }

    public long getSeller_id(){
        return seller_id;
    }
    public boolean isIsdeal(){
        return isdeal;
    }
    public String getPost_date(){
        return post_date;
    }
    public String getSeller_name(){
        return seller_name;
    }
    public String getSeller_score(){
        return seller_score;
    }
    public String getItem_name(){
        return item_name;
    }
    public String getCategory(){
        return category;
    }
    public String getPlustype(){
        return plustype;
    }
    public String getStorename(){
        return storename;
    }
    public int getPrice(){ return price; }
    public String getExpiration_date(){
        return expiration_date;
    }
    public String getpostingcontent(){
        return content;
    }
    public String getImageurl(){
        return imageurl;
    }
    public Bitmap getCouponimage(){
        return couponimage;
    }
    public boolean getIsClicked() { return isClicked; }

    public void setCoupon_id(int coupon_id){
        this.coupon_id = coupon_id;
    }
    public void setSeller_id(long seller_id){
        this.seller_id = seller_id;
    }
    public void setIsdeal(boolean isdeal){
        this.isdeal = isdeal;
    }
    public void setPost_date(String post_date){
        this.post_date = post_date;
    }
    public void setSeller_name(String seller_name){
        this.seller_name = seller_name;
    }
    public void setSeller_score(String seller_score){
        this.seller_score = seller_score;
    }
    public void setItem_name(String item_name){
        this.item_name = item_name;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public void setPlustype(String plustype){
        this.plustype = plustype;
    }
    public void setStorename(String storename){
        this.storename = storename;
    }
    public void setPrice(int price){
        this.price = price;
    }
    public void setExpiration_date(String expiration_date){
        this.expiration_date = expiration_date;
    }
    public void setContent(String content){
        this.content = content;
    }
    public void setImage(String imageurl){
        this.imageurl = imageurl;
    }
    public void setCouponimage(Bitmap couponimage) { this.couponimage = couponimage; }
    public void setIsClicked(boolean isClicked) { this.isClicked=isClicked; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(coupon_id);
        parcel.writeLong(seller_id);
        parcel.writeByte((byte) (isdeal ? 1 : 0));
        parcel.writeString(post_date);
        parcel.writeString(seller_name);
        parcel.writeString(seller_score);
        parcel.writeString(item_name);
        parcel.writeString(category);
        parcel.writeString(plustype);
        parcel.writeString(storename);
        parcel.writeInt(price);
        parcel.writeString(expiration_date);
        parcel.writeString(content);
        parcel.writeString(imageurl);
        parcel.writeParcelable(couponimage, i);
        parcel.writeByte((byte) (isClicked ? 1 : 0));
    }
}
