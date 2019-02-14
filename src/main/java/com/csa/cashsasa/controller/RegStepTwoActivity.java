package com.csa.cashsasa.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.csa.cashsasa.R;
import com.csa.cashsasa.controller.RegStepOneActivity;
import com.csa.cashsasa.controller.RegStepThreeActivity;
import com.csa.cashsasa.model.*;
import com.csa.cashsasa.utility.StepTwoPersist;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegStepTwoActivity extends AppCompatActivity
{

    private EditText user_email, user_national_id;
    private String email, national_id, emil_value, n_id_value;
    private int onStartCount = 0;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_reg_step_two);

        user_email = findViewById(R.id.user_email);
        user_national_id = findViewById(R.id.user_national_id);

        builder = new AlertDialog.Builder(this);

        StepTwoPersist stepTwoPersist = new StepTwoPersist(this);

        if(stepTwoPersist.valuesExist())
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        }
        else
        {
            this.overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_right);

            HashMap<String, String> steptwoValues = stepTwoPersist.getInputValues();

            user_email.setText(steptwoValues.get(StepTwoPersist.KEY_EMAIL));
            user_national_id.setText(steptwoValues.get(StepTwoPersist.KEY_NATIONAL_ID));
        }

    }


    public void nextStep(View view)
    {
        UserModel userModel = new UserModel();

        emil_value = user_email.getText().toString();
        n_id_value = user_national_id.getText().toString();

        if(emil_value.trim().length() > 0 && n_id_value.trim().length() > 0)
        {
            userModel.setEmail(emil_value);
            userModel.setNational_id(n_id_value);

            email = userModel.getEmail();
            national_id = userModel.getNational_id();

            StepTwoPersist stepTwoPersist = new StepTwoPersist(this);

            stepTwoPersist.createInputSession(email, national_id);

            startActivity(new Intent(this, RegStepThreeActivity.class));
        }
        else
        {
            Toast.makeText(this, "Fill all fields to continue", Toast.LENGTH_SHORT).show();
        }
    }

    public void previousStep(View view)
    {
        startActivity(new Intent(this, RegStepOneActivity.class));
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
