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
import com.csa.cashsasa.controller.ForgotPinActivity;
import com.csa.cashsasa.controller.HomeActivity;
import com.csa.cashsasa.controller.MainActivity;
import com.csa.cashsasa.controller.RegStepOneActivity;
import com.csa.cashsasa.utility.HasLoanPersist;
import com.csa.cashsasa.utility.InstalledCheck;
import com.csa.cashsasa.utility.Session;
import com.csa.cashsasa.utility.UserExists;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;

public class AccountVerification
{
    private Context ctx;
    private AlertDialog.Builder builder;z
    private ProgressDialog progressDialog;
    private String loan_success;

    public AccountVerification(Context ctx)
    {
        this.ctx = ctx;
        builder = new AlertDialog.Builder(ctx);
        progressDialog = new ProgressDialog(ctx);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .build();
        AndroidNetworking.initialize(ctx, okHttpClient);

        AndroidNetworking.setParserFactory(new JacksonParserFactory());

        loan_success = "success";

        progressDialog.setMessage("Finishing account verification ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void verifyAction(String message, final String phone)
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

                                builder.setTitle(R.string.verify_account_error);
                                builder.setMessage(R.string.verify_account_error_text);
                                builder.setCancelable(false);

                                builder.setPositiveButton(
                                        "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                                UserExists userExists = new UserExists(ctx);
                                                userExists.clearInputValues();

                                                Intent intent = new Intent(ctx, MainActivity.class);

                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                ctx.startActivity(intent);

                                            }
                                        });

                                AlertDialog alert = builder.create();
                                alert.show();
                            }

                            else
                            {
                                progressDialog.dismiss();

                                builder.setTitle(R.string.verify_account_success);
                                builder.setMessage(R.string.verify_account_success_text);
                                builder.setCancelable(false);

                                builder.setPositiveButton(
                                        "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                                UserExists userExists = new UserExists(ctx);
                                                userExists.clearInputValues();

                                                InstalledCheck installedCheck = new InstalledCheck(ctx);
                                                installedCheck.createUserLoginSession("1", phone);

                                                Intent intent = new Intent(ctx, ForgotPinActivity.class);

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
