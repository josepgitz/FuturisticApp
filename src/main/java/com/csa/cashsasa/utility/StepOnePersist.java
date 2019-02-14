package com.csa.cashsasa.utility;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class StepOnePersist
{
    SharedPreferences pref;

    Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREFER_NAME = "stepOnePersist";

    private static final String IS_VALUES_SET_1 = "stepOneExists";

    public static final String KEY_FIRST_NAME = "fname";

    public static final String KEY_LAST_NAME = "lname";

    public static final String KEY_COUNTRY = "country";




    @SuppressLint("CommitPrefEdits")
    public StepOnePersist(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createInputSession(String fname, String lname, String country)
    {
        editor.putBoolean(IS_VALUES_SET_1, true);
        editor.putString(KEY_FIRST_NAME, fname);
        editor.putString(KEY_LAST_NAME, lname);
        editor.putString(KEY_COUNTRY, country);


        editor.apply();
    }

    public boolean valuesExist()
    {

        if(!this.ifValuesExist()){

            return true;
        }
        return false;
    }


    public HashMap<String, String> getInputValues()
    {

        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_FIRST_NAME, pref.getString(KEY_FIRST_NAME, null));
        user.put(KEY_LAST_NAME, pref.getString(KEY_LAST_NAME, null));
        user.put(KEY_COUNTRY, pref.getString(KEY_COUNTRY, null));

        return user;
    }

    public void clearInputValues()
    {

        editor.clear();
        editor.apply();

    }

     boolean ifValuesExist()
    {
        return pref.getBoolean(IS_VALUES_SET_1, false);
    }

}