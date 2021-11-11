package com.example.ccuda.data;

public class ChatData {
    private String msg;
    private String nicname;
    private String time;
    private String profileUrl;

    public ChatData(){}

    public ChatData(String name, String message, String time, String pofileUrl) {
        this.nicname = name;
        this.msg = message;
        this.time = time;
        this.profileUrl = pofileUrl;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNicname() {
        return nicname;
    }

    public void setNicname(String nicname) {
        this.nicname = nicname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProfileUrl() {
        return profileUrl;
    }
    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
