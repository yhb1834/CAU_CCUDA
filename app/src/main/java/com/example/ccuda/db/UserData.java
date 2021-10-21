package com.example.ccuda.db;

public class UserData {
    private long userid;
    private String nicname;
    private String email;
    private double score;
    private static UserData userData;

    public static UserData getInstance(){
        if(userData == null) userData = new UserData();
        return userData;
    }

    public long getUserid() {return userid;}
    public String getNicname() {return nicname;}
    public String getEmail() {return email;}
    public double getScore(){return score;}

    public void setUserid(long userid){this.userid=userid;}
    public void setNicname(String nicname){this.nicname=nicname;}
    public void setEmail(String email){this.email=email;}
    public void setScore(double score){this.score=score;}
}

