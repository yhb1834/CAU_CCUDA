package com.example.ccuda.data;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeItem implements Parcelable {
    int resourceId;
    int like;
    String title;

    public RecipeItem(int resourceId, int like, String title) {
        this.resourceId = resourceId;
        this.like = like;
        this.title = title;
    }

    protected RecipeItem(Parcel in) {
        resourceId = in.readInt();
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

    public int getImage() {return resourceId;}
    public int getLike() { return like; }
    public String getTitle() { return title; }

    public void setImage(int resourceId){
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
        dest.writeInt(resourceId);
        dest.writeInt(like);
        dest.writeString(title);
    }
}
