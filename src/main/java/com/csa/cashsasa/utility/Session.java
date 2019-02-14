package com.csa.cashsasa.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.csa.cashsasa.controller.MainActivity;

import java.util.HashMap;

@SuppressWarnings("deprecation")
public class Session
{
    SharedPreferences pref;

    Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREFER_NAME = "SessionData";

    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    public static final String KEY_LIMIT = "limit";

    public static final String KEY_LIMIT_VALUE = "limit_value";

    public static final String KEY_PHONE = "phone";

    public static final String KEY_U_ID = "user_id";

    public static final String KEY_NAME = "name";


    @SuppressLint("CommitPrefEdits")
    public Session(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createUserLoginSession(String limit, String limit_value, String phone, String user_id, String name)
    {
        editor.putBoolean(IS_USER_LOGIN, true);

        editor.putString(KEY_LIMIT, limit);

        editor.putString(KEY_LIMIT_VALUE, limit_value);

        editor.putString(KEY_PHONE, phone);

        editor.putString(KEY_U_ID, user_id);

        editor.putString(KEY_NAME, name);

        editor.apply();
    }

    public boolean checkLogin()
    {

        if(!this.isUserLoggedIn()){

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

        user.put(KEY_LIMIT, pref.getString(KEY_LIMIT, null));

        user.put(KEY_LIMIT_VALUE, pref.getString(KEY_LIMIT_VALUE, null));

        user.put(KEY_PHONE, pref.getString(KEY_PHONE, null));

        user.put(KEY_U_ID, pref.getString(KEY_U_ID, null));

        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        return user;
    }

    public void logoutUser()
    {

        editor.clear();
        editor.apply();

        Intent i = new Intent(_context, MainActivity.class);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        _context.startActivity(i);
    }

    boolean isUserLoggedIn()
    {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

}