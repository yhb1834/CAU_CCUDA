package com.example.ccuda.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecipeItem implements Parcelable {
    String resourceId;
    int like;
    Map<String, Boolean> likes = new HashMap<>();
    String title;
    String[] itemname;    // 고른 상품명이름
    ArrayList<String> imageurl;
    String content;     // 글 내용

    public RecipeItem(){}

    public RecipeItem(String resourceId, int like, String title, String[] itemname, ArrayList<String> imageurl, String content, Map<String,Boolean> likes) {
        this.resourceId = resourceId;
        this.like = like;
        this.title = title;
        this.itemname = itemname;
        this.imageurl = imageurl;
        this.content = content;
        this.likes = likes;
    }

    protected RecipeItem(Parcel in) {
        resourceId = in.readString();
        like = in.readInt();
        title = in.readString();
    }

    public static final Creator<RecipeItem> CREATOR = new Creator<RecipeItem>() {
        @Override
        public RecipeItem createFromParcel(Parcel in) {
            return new RecipeItem(in);
        }

        @Override
        public RecipeItem[] newArray(int size) {
            return new RecipeItem[size];
        }
    };

    public String[] getItemname() {
        return itemname;
    }

    public ArrayList<String> getImageurl() {
        return imageurl;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {return resourceId;}
    public int getLike() { return like; }
    public Map<String, Boolean> getLikes() {
        return likes;
    }
    public String getTitle() { return title; }

    public void setImage(String  resourceId){
        this.resourceId = resourceId;
    }
    public void setLike(int like) { this.like = like; }
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(resourceId);
        dest.writeInt(like);
        dest.writeString(title);
    }
}
