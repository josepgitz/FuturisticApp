package com.csa.cashsasa.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

@SuppressWarnings("deprecation")
public class StepThreePersist
{
    SharedPreferences pref;

    Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREFER_NAME = "stepThreePersist";

    private static final String IS_VALUES_SET_3 = "stepThreeExists";

    public static final String KEY_PHONE = "phone";

    public static final String KEY_PASSWORD = "password";



    @SuppressLint("CommitPrefEdits")
    public StepThreePersist(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createInputSession(String phone, String password)
    {
        editor.putBoolean(IS_VALUES_SET_3, true);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_PASSWORD, password);

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

        user.put(KEY_PHONE, pref.getString(KEY_PHONE, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        return user;
    }

    public void clearInputValues()
    {

        editor.clear();
        editor.apply();

    }

    boolean ifValuesExist()
    {
        return pref.getBoolean(IS_VALUES_SET_3, false);
    }

}