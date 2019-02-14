package com.csa.cashsasa.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.csa.cashsasa.controller.MainActivity;

import java.util.HashMap;

@SuppressWarnings("deprecation")
public class InstalledCheck
{
    SharedPreferences pref;

    Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREFER_NAME = "installed";

    private static final String IS_USER_LOGIN = "appInstalled";

    public static final String KEY_STATUS = "status";

    public static final String KEY_PHONE = "phone";



    @SuppressLint("CommitPrefEdits")
    public InstalledCheck(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createUserLoginSession(String status, String phone)
    {
        editor.putBoolean(IS_USER_LOGIN, true);

        editor.putString(KEY_STATUS, status);

        editor.putString(KEY_PHONE, phone);


        editor.apply();
    }

    public boolean valuesExist()
    {

        if(!this.ifValueExists()){

            return true;
        }
        return false;
    }

    public boolean checkLogin()
    {

        if(!this.ifValueExists()){

            Intent i = new Intent(_context, MainActivity.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);

            return true;
        }
        return false;
    }


    public HashMap<String, String> getUserDetails()
    {

        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_STATUS, pref.getString(KEY_STATUS, null));

        user.put(KEY_PHONE, pref.getString(KEY_PHONE, null));

        return user;
    }

    public void clearInputValues()
    {

        editor.clear();
        editor.apply();

    }

    private boolean ifValueExists()
    {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

}