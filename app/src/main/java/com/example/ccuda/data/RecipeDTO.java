package com.example.ccuda.data;

import java.util.HashMap;
import java.util.Map;

public class RecipeDTO {
    private String title;
    private String storename;
    private String content;
    private String writer_id;
    private String itemname;
    private int like;
    private Map<String, Boolean> likes = new HashMap<>();
    private Map<String, Boolean> items = new HashMap<>();

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public Map<String, Boolean> getLikes() {
        return likes;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getWriter_id() {
        return writer_id;
    }

    public void setWriter_id(String writer_id) {
        this.writer_id = writer_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Boolean> getItems() {
        return items;
    }

    public void setItems(Map<String, Boolean> items) {
        this.items = items;
    }

    private String image1;
    private String filename1;
    private String image2;
    private String filename2;
    private String image3;
    private String filename3;
    private String image4;
    private String filename4;
    private String image5;
    private String filename5;
    private String image6;
    private String filename6;
    private String image7;
    private String filename7;
    private String image8;
    private String filename8;
    private String image9;
    private String filename9;
    private String image10;
    private String filename10;


    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1, String filename1) {
        this.image1 = image1;
        this.filename1 = filename1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2, String filename2) {
        this.image2 = image2;
        this.filename2 = filename2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3, String filename3) {
        this.image3 = image3;
        this.filename3 = filename3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4,String filename4) {
        this.image4 = image4;
        this.filename4 = filename4;
    }

    public String getImage5() {
        return image5;
    }

    public void setImage5(String image5, String filename5) {
        this.image5 = image5;
        this.filename5 = filename5;
    }

    public String getImage6() {
        return image6;
    }

    public void setImage6(String image6,String filename6) {
        this.image6 = image6;
        this.filename6=filename6;
    }

    public String getImage7() {
        return image7;
    }

    public void setImage7(String image7,String filename7) {
        this.image7 = image7;
        this.filename7=filename7;
    }

    public String getImage8() {
        return image8;
    }

    public void setImage8(String image8,String filename8) {
        this.image8 = image8;
        this.filename8 = filename8;
    }

    public String getImage9() {
        return image9;
    }

    public void setImage9(String image9,String filename9) {
        this.image9 = image9;
        this.filename9=filename9;
    }

    public String getImage10() {
        return image10;
    }

    public void setImage10(String image10,String filename10) {
        this.image10 = image10;
        this.filename10 = filename10;
    }

    public String getFilename1() {
        return filename1;
    }

    public String getFilename2() {
        return filename2;
    }

    public String getFilename3() {
        return filename3;
    }

    public String getFilename4() {
        return filename4;
    }

    public String getFilename5() {
        return filename5;
    }

    public String getFilename6() {
        return filename6;
    }

    public String getFilename7() {
        return filename7;
    }

    public String getFilename8() {
        return filename8;
    }

    public String getFilename9() {
        return filename9;
    }

    public String getFilename10() {
        return filename10;
    }
}
