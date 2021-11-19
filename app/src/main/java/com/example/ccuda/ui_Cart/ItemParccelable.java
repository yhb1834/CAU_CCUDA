package com.example.ccuda.ui_Cart;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemParccelable implements Parcelable {
    String prodName;
    String convName;
    String imgUrl;
    protected ItemParccelable(Parcel in) {
        prodName=in.readString();
        convName=in.readString();
        imgUrl=in.readString();
    }

    public static final Creator<ItemParccelable> CREATOR = new Creator<ItemParccelable>() {
        @Override
        public ItemParccelable createFromParcel(Parcel in) {
            return new ItemParccelable(in);
        }

        @Override
        public ItemParccelable[] newArray(int size) {
            return new ItemParccelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(prodName);
        dest.writeString(convName);
        dest.writeString(imgUrl);
    }

    public String getProdName(){
        return prodName;
    }

    public String getConvName() {
        return convName;
    }

    public String getImgUrl(){
        return imgUrl;
    }
}
