package com.csa.cashsasa.dao;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.csa.cashsasa.R;
import com.csa.cashsasa.api.ApiData;
import com.csa.cashsasa.controller.ForgotPinActivity;
import com.csa.cashsasa.controller.HomeActivity;
import com.csa.cashsasa.controller.LoginActivity;
import com.csa.cashsasa.controller.RegStepOneActivity;
import com.csa.cashsasa.controller.VerifyAccountActivity;
import com.csa.cashsasa.utility.HasLoanPersist;
import com.csa.cashsasa.utility.UserExists;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class ResetPin
{
    private Context ctx;
    private AlertDialog.Builder builder;
    private ProgressDialog progressDialog;
    private String reset_success, no_pin, reset_error, restore_success ,phone;
    private Bundle bundle;

    public ResetPin(Context ctx)
    {
        this.ctx = ctx;
        builder = new AlertDialog.Builder(ctx);
        progressDialog = new ProgressDialog(ctx);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                . writeTimeout(180, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.initialize(ctx,okHttpClient);

        restore_success = "restore";
        reset_success = "success";
        no_pin = "no_pin";
        reset_error = "error";

        UserExists userExists = new UserExists(ctx);

        if(userExists.valuesExist())
        {
            progressDialog.setMessage("Resetting your pin ...");
        }

        else
        {
            progressDialog.setMessage("Restoring your account ...");
        }

        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void changePin(String id_no, String status)
    {
        String url = ApiData.resetPin();

        AndroidNetworking.post(url)
                .addBodyParameter("id", id_no)
                .addBodyParameter("status", status)
                .setTag("Reset Pin")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            if (response.getString("status").equals(reset_success))
                            {
                                progressDialog.dismiss();

                                builder.setTitle(R.string.pin_reset_success);
                                builder.setMessage(response.getString("response"));
                                builder.setCancelable(false);

                                builder.setPositiveButton(
                                        "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                                Intent intent = new Intent(ctx, LoginActivity.class);

                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                ctx.startActivity(intent);
                                            }
                                        });

                                AlertDialog alert = builder.create();
                                alert.show();
                            }

                            if (response.getString("status").equals(no_pin))
                            {
                                progressDialog.dismiss();

                                builder.setTitle(R.string.pin_reset_none);
                                builder.setMessage(response.getString("response"));
                                builder.setCancelable(false);

                                builder.setPositiveButton(
                                        "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert = builder.create();
                                alert.show();
                            }

                            if (response.getString("status").equals(restore_success))
                            {
                                progressDialog.dismiss();

                                builder.setTitle(R.string.restore_head);
                                builder.setMessage(response.getString("response"));
                                builder.setCancelable(false);

                                phone = response.getString("phone");

                                builder.setPositiveButton(
                                        "OK",
                                        new DialogInterface.OnClickListener()
                                        {
                                            public void onClick(DialogInterface dialog, int id)
                                            {
                                                dialog.cancel();

                                                Intent intent = new Intent(ctx, VerifyAccountActivity.class);

                                                bundle = new Bundle();
                                                bundle.putString("phone", phone);

                                                intent.putExtras(bundle);

                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                ctx.startActivity(intent);
                                            }
                                        });

                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error

                        progressDialog.dismiss();

                        builder.setMessage(R.string.net_error);
                        builder.setCancelable(false);

                        builder.setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
    }

}
