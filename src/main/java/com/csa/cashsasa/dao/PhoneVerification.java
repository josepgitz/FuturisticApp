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
import com.csa.cashsasa.controller.AccountSetUpActivity;
import com.csa.cashsasa.controller.HomeActivity;
import com.csa.cashsasa.controller.RegStepOneActivity;
import com.csa.cashsasa.dao.RollBackChanges;
import com.csa.cashsasa.utility.HasLoanPersist;
import com.csa.cashsasa.utility.Session;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;

public class PhoneVerification
{
    private Context ctx;
    private AlertDialog.Builder builder;
    private ProgressDialog progressDialog;
    private String loan_success;

    public PhoneVerification(Context ctx)
    {
        this.ctx = ctx;
        builder = new AlertDialog.Builder(ctx);
        progressDialog = new ProgressDialog(ctx);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .build();
        AndroidNetworking.initialize(ctx, okHttpClient);

        AndroidNetworking.setParserFactory(new JacksonParserFactory());

        loan_success = "success";

        progressDialog.setMessage("Verifying Phone Number ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void verifyAction(String message, String phone)
    {
        String url = ApiData.VerifyUserPhone();

        AndroidNetworking.post(url)
                .addBodyParameter("phone", phone)
                .addBodyParameter("message", message)
                .setTag("Verification")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            if (response.getString("status").equals("no_code"))
                            {
                                progressDialog.dismiss();

                                final String phone = response.getString("phone");

                                builder.setTitle(R.string.phone_confirm);
                                builder.setMessage(R.string.phone_confirm_error);
                                builder.setCancelable(false);

                                builder.setPositiveButton(
                                        "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                                com.csa.cashsasa.dao.RollBackChanges rollBackChanges = new RollBackChanges(ctx);
                                                rollBackChanges.rollbackAction(phone);
                                            }
                                        });

                                AlertDialog alert = builder.create();
                                alert.show();
                            }

                            else
                            {
                                progressDialog.dismiss();

                                String loan_limit = response.getString("loan_limit");
                                String loan_limit_value = response.getString("loan_limit_value");
                                String phone = response.getString("phone");
                                String user_id = response.getString("user_id");
                                String name = response.getString("name");

                                Session session = new Session(ctx);
                                session.createUserLoginSession(loan_limit,loan_limit_value,phone,user_id,name);

                                Intent account_setup = new Intent(ctx, AccountSetUpActivity.class);

                                account_setup.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                account_setup.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                
                                 ctx.startActivity(account_setup);
                            }
                        }

                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error)
                    {
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
