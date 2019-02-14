package com.csa.cashsasa.controller;

import android.app.Application;

import com.csa.cashsasa.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class CustomFontApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/ubuntu/Ubuntu-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}