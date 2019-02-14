package com.csa.cashsasa.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.csa.cashsasa.controller.MainActivity;

import java.util.HashMap;

@SuppressWarnings("deprecation")
public class UserExists
{
    SharedPreferences pref;

    Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREFER_NAME = "userExists";

    private static final String IS_USER_LOGIN = "existingUser";

    public static final String KEY_STATUS = "status";




    @SuppressLint("CommitPrefEdits")
    public UserExists(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createUserLoginSession(String status)
    {
        editor.putBoolean(IS_USER_LOGIN, true);

        editor.putString(KEY_STATUS, status);

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