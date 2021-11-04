package com.example.ccuda.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    static final String PREF_USER_ID = "id";
    static final String PREF_USER_EMAIL = "email";
    static final String PREF_USER_PWD = "password";
    static final String PREF_USER_NICNAME = "nicname";
    static final String PREF_USER_SCORE = "score";

    static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences("test_preferences",Context.MODE_PRIVATE);
    }

    public static void setSession(Context context, long id, String email, String password, String nicname, double score){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putLong(PREF_USER_ID,id);
        editor.putString(PREF_USER_EMAIL,email);
        editor.putString(PREF_USER_PWD,password);
        editor.putString(PREF_USER_NICNAME,nicname);
        editor.putString(PREF_USER_SCORE,Double.toString(score));
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
    // 로그아웃 세션션 clear
    public static void clearSession(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }
}
