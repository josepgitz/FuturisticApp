package com.csa.cashsasa.utility;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class HasLoanPersist
{
    SharedPreferences pref;

    Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREFER_NAME = "hasLoanPersist";

    private static final String IS_VALUES_SET_1 = "hasLoanExists";

    public static final String KEY_LOAN_AMOUNT = "0";

    public static final String KEY_LOAN_VALUE = "loan_value";

    public static final String KEY_HAS_LOAN = "no";



    @SuppressLint("CommitPrefEdits")
    public HasLoanPersist(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createInputSession(String amount, String amount_value,  String has_loan)
    {
        editor.putBoolean(IS_VALUES_SET_1, true);
        editor.putString(KEY_LOAN_AMOUNT, amount);
        editor.putString(KEY_LOAN_VALUE, amount_value);
        editor.putString(KEY_HAS_LOAN, has_loan);

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

        user.put(KEY_LOAN_AMOUNT, pref.getString(KEY_LOAN_AMOUNT, null));
        user.put(KEY_LOAN_VALUE, pref.getString(KEY_LOAN_VALUE, null));
        user.put(KEY_HAS_LOAN, pref.getString(KEY_HAS_LOAN, null));

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