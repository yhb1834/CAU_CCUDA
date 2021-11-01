package com.example.ccuda.data;

public class PeopleItem {
    String nicname;
    String star;
    int resourceId; //프로필 사진


    public PeopleItem(int resourceId, String nicname, String star) {
        this.resourceId = resourceId;
        this.nicname = nicname;
        this.star= star;

    }

    public String getNicname() { return nicname; }
    //public long getUserId() { return userid; }
    public String getStar() { return star; }
    public int getResourceId() {return resourceId;}


    public void setStar(String star) {
        this.star = star;
    }
    public void setNicname(String nicname) {
        this.nicname = nicname;
    }
    //public void setUserid(long userid) {
        //this.userid = userid;
    //}
    public void setResourceId(int resourceId){
        this.resourceId = resourceId;
    }
}