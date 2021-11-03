package com.example.ccuda.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    static final String PREF_USER_EMAIL = "email";
    static final String PREF_USER_PWD = "password";
    static final String PREF_USER_NICNAME = "nicname";

    static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences("test_preferences",Context.MODE_PRIVATE);
    }

    public static void setSession(Context context, String email, String password, String nicname){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_EMAIL,email);
        editor.putString(PREF_USER_PWD,password);
        editor.putString(PREF_USER_NICNAME,nicname);
        editor.commit();
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

    // 로그아웃 세션션 clear
    public static void clearSession(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }
}
