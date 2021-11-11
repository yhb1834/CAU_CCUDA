package com.example.ccuda.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    static final String BASIC_IMAGE_URL = "https://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg";
    static final String PREF_USER_ID = "id";
    static final String PREF_USER_EMAIL = "email";
    static final String PREF_USER_PWD = "password";
    static final String PREF_USER_NICNAME = "nicname";
    static final String PREF_USER_SCORE = "score";
    static final String PREF_USER_PROFILEIMAGE = "profileimage";

    static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences("test_preferences",Context.MODE_PRIVATE);
    }

    public static void setSession(Context context, long id, String email, String password, String nicname, double score, String imageurl){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putLong(PREF_USER_ID,id);
        editor.putString(PREF_USER_EMAIL,email);
        editor.putString(PREF_USER_PWD,password);
        editor.putString(PREF_USER_NICNAME,nicname);
        editor.putString(PREF_USER_SCORE,Double.toString(score));
        if(imageurl == null){
            imageurl = BASIC_IMAGE_URL;
        }
        editor.putString(PREF_USER_PROFILEIMAGE,imageurl);
        editor.commit();
    }
    public static long getId(Context context){
        return getSharedPreferences(context).getLong(PREF_USER_ID,-1L);
    }

    public static String getEmail(Context context){
        return getSharedPreferences(context).getString(PREF_USER_EMAIL,"");
    }

    public static String getPassword(Context context){
        return getSharedPreferences(context).getString(PREF_USER_PWD,"");
    }
    public static String getNicname(Context context){
        return getSharedPreferences(context).getString(PREF_USER_NICNAME,"");
    }

    public static double getScore(Context context){
        return Double.parseDouble(getSharedPreferences(context).getString(PREF_USER_SCORE,""));
    }
    public static String getProfileimage(Context context){
        return getSharedPreferences(context).getString(PREF_USER_PROFILEIMAGE,"");
    }
    // 로그아웃 세션션 clear
    public static void clearSession(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }
}
