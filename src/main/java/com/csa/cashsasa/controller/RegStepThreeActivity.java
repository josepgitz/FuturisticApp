package com.csa.cashsasa.controller;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.csa.cashsasa.R;
import com.csa.cashsasa.controller.RegStepTwoActivity;
import com.csa.cashsasa.dao.RegisterAction;
import com.csa.cashsasa.model.*;
import com.csa.cashsasa.utility.NetworkUtil;
import com.csa.cashsasa.utility.StepOnePersist;
import com.csa.cashsasa.utility.StepThreePersist;
import com.csa.cashsasa.utility.StepTwoPersist;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegStepThreeActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnKeyListener, TextWatcher {
    private EditText mPinFirstDigitEditText;
    private EditText mPinSecondDigitEditText;
    private EditText mPinThirdDigitEditText;
    private EditText mPinForthDigitEditText;
    private EditText mPinHiddenEditText;
    private EditText user_phone;
    private CheckBox accept_tlc;
    private String phone, password, phone_value, pass_value, selected_country;
    private String fname, lname, email, id_no;
    private String c_codes[] = {"+254"};
    private int onStartCount = 0;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_reg_step_three);

        init();
        setPINListeners();

        user_phone = findViewById(R.id.user_phone);
        accept_tlc = findViewById(R.id.tlc_checkbox);

        builder = new AlertDialog.Builder(this);

        StepOnePersist stepOnePersist = new StepOnePersist(this);

        HashMap<String, String> stepOneValues = stepOnePersist.getInputValues();

        selected_country = (stepOneValues.get(StepOnePersist.KEY_COUNTRY));

        if (selected_country.equals("Kenya")) {
            user_phone.setText(c_codes[0]);
        }

        StepThreePersist stepThreePersist = new StepThreePersist(this);

        if (stepThreePersist.valuesExist())
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        }
        else
        {
            this.overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_right);

            HashMap<String, String> stepThreeValues = stepThreePersist.getInputValues();

            user_phone.setText(stepThreeValues.get(StepThreePersist.KEY_PHONE));
        }

    }


    public void finishStep(View view)
    {
        if(!NetworkUtil.getConnectionStatus(this))
        {
            UserModel userModel = new UserModel();

            phone_value = user_phone.getText().toString();
            pass_value = mPinHiddenEditText.getText().toString();

            if (phone_value.trim().length() > 0 && pass_value.trim().length() == 4 && accept_tlc.isChecked())
            {
                StepThreePersist stepThreePersist = new StepThreePersist(this);
                stepThreePersist.createInputSession(phone_value.trim(), password);

                StepOnePersist stepOnePersist = new StepOnePersist(this);
                HashMap<String, String> stepOneInputs = stepOnePersist.getInputValues();

                fname = stepOneInputs.get(StepOnePersist.KEY_FIRST_NAME);
                lname = stepOneInputs.get(StepOnePersist.KEY_LAST_NAME);

                StepTwoPersist stepTwoPersist = new StepTwoPersist(this);
                HashMap<String, String> stepTwoInputs = stepTwoPersist.getInputValues();

                email = stepTwoInputs.get(StepTwoPersist.KEY_EMAIL);
                id_no = stepTwoInputs.get(StepTwoPersist.KEY_NATIONAL_ID);

                userModel.setCountry(selected_country.trim());
                userModel.setFname(fname.trim());
                userModel.setLname(lname.trim());
                userModel.setEmail(email.trim());
                userModel.setNational_id(id_no.trim());
                userModel.setPhone(phone_value.trim());
                userModel.setPassword(pass_value.trim());

                RegisterAction registerAction = new RegisterAction(this);
                registerAction.addUserDetails(userModel);


            } else {
                Toast.makeText(this, "Fill in all fields to continue", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void previousStep(View view) {
        startActivity(new Intent(this, RegStepTwoActivity.class));
    }

    /**
     * Hides soft keyboard.
     *
     * @param editText EditText which has focus
     */
    public void hideSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * Initialize EditText fields.
     */
    private void init()
    {
        mPinFirstDigitEditText = findViewById(R.id.pin_first_edittext);
        mPinSecondDigitEditText = findViewById(R.id.pin_second_edittext);
        mPinThirdDigitEditText = findViewById(R.id.pin_third_edittext);
        mPinForthDigitEditText = findViewById(R.id.pin_forth_edittext);
        mPinHiddenEditText = findViewById(R.id.pin_hidden_edittext);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        setDefaultPinBackground(mPinFirstDigitEditText);
        setDefaultPinBackground(mPinSecondDigitEditText);
        setDefaultPinBackground(mPinThirdDigitEditText);

        if (s.length() == 0) {
            setFocusedPinBackground(mPinFirstDigitEditText);
            mPinFirstDigitEditText.setText("");
        } else if (s.length() == 1) {
            setFocusedPinBackground(mPinSecondDigitEditText);
            mPinFirstDigitEditText.setText(s.charAt(0) + "");
            mPinSecondDigitEditText.setText("");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
        } else if (s.length() == 2) {
            setFocusedPinBackground(mPinThirdDigitEditText);
            mPinSecondDigitEditText.setText(s.charAt(1) + "");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
        } else if (s.length() == 3) {
            setFocusedPinBackground(mPinForthDigitEditText);
            mPinThirdDigitEditText.setText(s.charAt(2) + "");
        } else if (s.length() == 4) {
            mPinForthDigitEditText.setText(s.charAt(3) + "");

            hideSoftKeyboard(mPinForthDigitEditText);
        }
    }

    @Override
    public void afterTextChanged(Editable s)
    {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        final int id = v.getId();
        switch (id) {
            case R.id.pin_first_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pin_second_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pin_third_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pin_forth_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            final int id = v.getId();
            switch (id) {
                case R.id.pin_hidden_edittext:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (mPinHiddenEditText.getText().length() == 4)
                            mPinForthDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 3)
                            mPinThirdDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 2)
                            mPinSecondDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 1)
                            mPinFirstDigitEditText.setText("");

                        if (mPinHiddenEditText.length() > 0)
                            mPinHiddenEditText.setText(mPinHiddenEditText.getText().subSequence(0, mPinHiddenEditText.length() - 1));

                        return true;
                    }

                    break;

                default:
                    return false;
            }
        }

        return false;
    }

    private void setDefaultPinBackground(EditText editText) {

    }

    /**
     * Sets focus on a specific EditText field.
     *
     * @param editText EditText to set focus on
     */
    public static void setFocus(EditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    /**
     * Sets focused PIN field background.
     *
     * @param editText edit text to change
     */
    private void setFocusedPinBackground(EditText editText) {

    }

    /**
     * Sets listeners for EditText fields.
     */
    private void setPINListeners() {
        mPinHiddenEditText.addTextChangedListener(this);

        mPinFirstDigitEditText.setOnFocusChangeListener(this);
        mPinSecondDigitEditText.setOnFocusChangeListener(this);
        mPinThirdDigitEditText.setOnFocusChangeListener(this);
        mPinForthDigitEditText.setOnFocusChangeListener(this);

        mPinFirstDigitEditText.setOnKeyListener(this);
        mPinSecondDigitEditText.setOnKeyListener(this);
        mPinThirdDigitEditText.setOnKeyListener(this);
        mPinForthDigitEditText.setOnKeyListener(this);
        mPinHiddenEditText.setOnKeyListener(this);
    }

    /**
     * Sets background of the view.
     * This method varies in implementation depending on Android SDK version.
     *
     * @param view       View to which set background
     * @param background Background to set to view
     */
    @SuppressWarnings("deprecation")
    public void setViewBackground(View view, Drawable background) {
        if (view == null || background == null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    /**
     * Shows soft keyboard.
     *
     * @param editText EditText which has focus
     */
    public void showSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.showSoftInput(editText, 0);
    }

    /**
     * Custom LinearLayout with overridden onMeasure() method
     * for handling software keyboard show and hide events.
     */
    public class MainLayout extends LinearLayout {

        public MainLayout(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                inflater.inflate(R.layout.activity_reg_step_three, this);
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            final int proposedHeight = MeasureSpec.getSize(heightMeasureSpec);
            final int actualHeight = getHeight();

            Log.d("TAG", "proposed: " + proposedHeight + ", actual: " + actualHeight);

            if (actualHeight >= proposedHeight) {
                // Keyboard is shown
                if (mPinHiddenEditText.length() == 0)
                    setFocusedPinBackground(mPinFirstDigitEditText);
                else
                    setDefaultPinBackground(mPinFirstDigitEditText);
            }

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public void onBackPressed()
    {
        builder.setTitle(R.string.close_app_head);
        builder.setMessage(R.string.close_app_text);
        builder.setCancelable(false);

        builder.setPositiveButton(
                "Yes Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        finish();
                        moveTaskToBack(true);

                    }
                });

        builder.setNegativeButton(
                "No Don't",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
