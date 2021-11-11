package com.example.ccuda.data;

public class RecipeItem {
    int resourceId; //프로필 사진
    int like;
    String title;

    public RecipeItem(int resourceId, int like, String title) {
        this.resourceId = resourceId;
        this.like = like;
        this.title = title;
    }

    public int getImage() {return resourceId;}
    public int getLike() { return like; }
    public String getTitle() { return title; }

    public void setResourceId(int resourceId){
        this.resourceId = resourceId;
    }
    public void setLike(int like) { this.like = like; }
    public void setTitle(String title) {
        this.title = title;
    }
}
