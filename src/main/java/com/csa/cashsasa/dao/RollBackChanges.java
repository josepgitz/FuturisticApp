package com.csa.cashsasa.dao;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.csa.cashsasa.R;
import com.csa.cashsasa.api.ApiData;
import com.csa.cashsasa.controller.HomeActivity;
import com.csa.cashsasa.controller.RegStepOneActivity;
import com.csa.cashsasa.utility.HasLoanPersist;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;

public class RollBackChanges
{
    private Context ctx;
    private AlertDialog.Builder builder;
    private ProgressDialog progressDialog;
    private String loan_success;

    public RollBackChanges(Context ctx)
    {
        this.ctx = ctx;
        builder = new AlertDialog.Builder(ctx);
        progressDialog = new ProgressDialog(ctx);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .build();
        AndroidNetworking.initialize(ctx, okHttpClient);

        AndroidNetworking.setParserFactory(new JacksonParserFactory());

        loan_success = "success";

        progressDialog.setTitle(R.string.roll_back_changes);
        progressDialog.setMessage("Rolling back your registration process ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void rollbackAction(String phone)
    {
        String url = ApiData.rollBack();

        AndroidNetworking.post(url)
                .addBodyParameter("phone", phone)
                .setTag("Roll Back")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            if (response.getString("status").equals(loan_success))
                            {
                                progressDialog.dismiss();

                                builder.setTitle(R.string.roll_back_changes);
                                builder.setMessage(R.string.rolling_back_success);
                                builder.setCancelable(false);

                                builder.setPositiveButton(
                                        "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                                Intent reg_activity = new Intent(ctx, RegStepOneActivity.class);

                                                reg_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                reg_activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                                ctx.startActivity(reg_activity);
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
