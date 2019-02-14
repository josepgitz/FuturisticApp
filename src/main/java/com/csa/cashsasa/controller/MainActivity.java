package com.csa.cashsasa.controller;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.csa.cashsasa.R;
import com.csa.cashsasa.controller.LoginActivity;
import com.csa.cashsasa.controller.RegStepOneActivity;
import com.csa.cashsasa.utility.InstalledCheck;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
{

    private int onStartCount = 0;
    private static int SPLASH_TIME_OUT = 2000;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        InstalledCheck installedCheck = new InstalledCheck(this);

        if(installedCheck.valuesExist())
        {
            handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    Intent regIntent = new Intent(MainActivity.this, RegStepOneActivity.class);

                    startActivity(regIntent);

                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
        else
        {
            handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    Intent homeIntent = new Intent(MainActivity.this, LoginActivity.class);

                    startActivity(homeIntent);

                    finish();
                }
            }, SPLASH_TIME_OUT);
        }


        onStartCount = 1;
        if (savedInstanceState == null)
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else
        {
            onStartCount = 2;
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStart() {

        super.onStart();
        if (onStartCount > 1) {
            this.overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_right);

        } else if (onStartCount == 1) {
            onStartCount++;
        }

    }

    public void registerStart(View view)
    {
        startActivity(new Intent(this, RegStepOneActivity.class));
    }

    public void loginStart(View view)
    {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
