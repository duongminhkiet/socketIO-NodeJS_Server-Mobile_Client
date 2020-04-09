/**
 * Created by: Kiet.Duong
 * Dec-29-2014
 **/
package com.zmk.nc.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class ShareReferenceConfig {
    private static ShareReferenceConfig instance = null;

    private String USERNAME = "USER_NAME";
    private String userName;

    private String PASSWORD = "PASS_WORD";
    private String passWord;

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    private ShareReferenceConfig(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(GlobalVariable.PREFERENCES, 4);
        userName = prefs.getString(USERNAME,"");
        passWord = prefs.getString(PASSWORD,"");
        settings = context.getSharedPreferences(GlobalVariable.PREFERENCES, 4);
        editor = settings.edit();

    }

    public static synchronized ShareReferenceConfig instance(Context context) {
        if (instance == null) {
            instance = new ShareReferenceConfig(context);
        }
        return instance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        editor.putString(USERNAME,userName);
        editor.commit();
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
        editor.putString(PASSWORD,passWord);
        editor.commit();
    }
}
