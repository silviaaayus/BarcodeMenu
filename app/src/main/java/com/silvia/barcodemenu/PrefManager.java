package com.silvia.barcodemenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    HashMap <String,String> pesanan;

    int Private_mode=0;

    private static final String PREF_NAME = "data_app";

    public PrefManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Private_mode);
        editor = pref.edit();
    }

    public void setIdUser(String idUser){
        editor.putString("id_siswa", idUser);
        editor.apply();
    }

    public String getIdUser(){
        return pref.getString("id_siswa","");
    }

    public void setLoginStatus(boolean isLogin){
        editor.putBoolean("Login", isLogin);
        editor.apply();
    }
    public boolean getLoginStatus(){
        return pref.getBoolean("Login",false);
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();


    }
}
