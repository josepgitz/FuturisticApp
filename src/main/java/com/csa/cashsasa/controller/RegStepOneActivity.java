package com.csa.cashsasa.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.csa.cashsasa.R;
import com.csa.cashsasa.controller.MainActivity;
import com.csa.cashsasa.controller.RegStepTwoActivity;
import com.csa.cashsasa.model.*;
import com.csa.cashsasa.utility.StepOnePersist;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegStepOneActivity extends AppCompatActivity
{
    private MaterialBetterSpinner countries_spinner;
    private String country, country_model, fname_model, lname_model, fname_value,lname_value;
    private EditText last_name, first_name;
    private AlertDialog.Builder builder;
    private int country_position;
    private int onStartCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_reg_step_one);

        first_name = findViewById(R.id.user_fname);
        last_name = findViewById(R.id.user_lname);

        builder = new AlertDialog.Builder(this);

        countries_spinner = findViewById(R.id.countries_spinner);

        ArrayAdapter<CharSequence> countriesAdapter = ArrayAdapter.createFromResource(this, R.array.countries, android.R.layout.select_dialog_item);
        countries_spinner.setAdapter(countriesAdapter);

        countries_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                country = adapterView.getItemAtPosition(position).toString();
                country_position = position;
            }
        });

        StepOnePersist stepOnePersist = new StepOnePersist(this);

        if(stepOnePersist.valuesExist())
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        }
        else
        {
            this.overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_right);

            HashMap<String, String> stepOneValues = stepOnePersist.getInputValues();

            first_name.setText(stepOneValues.get(StepOnePersist.KEY_FIRST_NAME));
            last_name.setText(stepOneValues.get(StepOnePersist.KEY_LAST_NAME));
            country = stepOneValues.get(StepOnePersist.KEY_COUNTRY);
        }

    }

    public void nextStep(View view)
    {
        UserModel userModel = new UserModel();

        fname_value = first_name.getText().toString();
        lname_value = last_name.getText().toString();

        if(fname_value.trim().length() > 0 && lname_value.trim().length() > 0 && country.trim().length() > 0)
        {
            userModel.setCountry(country.trim());
            userModel.setFname(fname_value.trim());
            userModel.setLname(lname_value.trim());

            country_model = userModel.getCountry();
            fname_model = userModel.getFname();
            lname_model = userModel.getLname();

            StepOnePersist stepOnePersist = new StepOnePersist(this);

            stepOnePersist.createInputSession(fname_model,lname_model,country);

            startActivity(new Intent(this,RegStepTwoActivity.class));
        }
        else
        {
            Toast.makeText(this, "Fill in all fields to continue", Toast.LENGTH_SHORT).show();
        }

    }

    public void mainPageView(View view)
    {
        startActivity(new Intent(this, MainActivity.class));
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
