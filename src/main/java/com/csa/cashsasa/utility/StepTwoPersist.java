package com.csa.cashsasa.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

@SuppressWarnings("deprecation")
public class StepTwoPersist
{
    SharedPreferences pref;

    Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREFER_NAME = "stepTwoPersist";

    private static final String IS_VALUES_SET_2 = "stepTwoExists";

    public static final String KEY_EMAIL = "email";

    public static final String KEY_NATIONAL_ID = "national_id";



    @SuppressLint("CommitPrefEdits")
    public StepTwoPersist(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createInputSession(String email, String national_id)
    {
        editor.putBoolean(IS_VALUES_SET_2, true);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NATIONAL_ID, national_id);

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

        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_NATIONAL_ID, pref.getString(KEY_NATIONAL_ID, null));
        return user;
    }

    public void clearInputValues()
    {

        editor.clear();
        editor.apply();

    }

    boolean ifValuesExist()
    {
        return pref.getBoolean(IS_VALUES_SET_2, false);
    }

}