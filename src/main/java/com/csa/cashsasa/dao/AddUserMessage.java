package com.csa.cashsasa.dao;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.csa.cashsasa.R;
import com.csa.cashsasa.api.ApiData;
import com.csa.cashsasa.api.MySingleton;
import com.csa.cashsasa.controller.AccountSetUpActivity;
import com.csa.cashsasa.controller.HomeActivity;
import com.csa.cashsasa.controller.RegStepOneActivity;
import com.csa.cashsasa.utility.HasLoanPersist;
import com.csa.cashsasa.utility.InstalledCheck;
import com.csa.cashsasa.utility.Session;
import com.csa.cashsasa.utility.StepOnePersist;
import com.csa.cashsasa.utility.StepThreePersist;
import com.csa.cashsasa.utility.StepTwoPersist;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class AddUserMessage
{
    private Context ctx;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;

    public AddUserMessage(Context ctx)
    {
        this.ctx = ctx;

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(360, TimeUnit.SECONDS)
                .readTimeout(360, TimeUnit.SECONDS)
                . writeTimeout(360, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.initialize(ctx,okHttpClient);
    }

    public void addAction(String number, String body, String id, String date, String user_id)
    {
        String url = ApiData.ProcessMessage();

        AndroidNetworking.post(url)
                .addBodyParameter("sender", number)
                .addBodyParameter("message", body)
                .addBodyParameter("message_id", id)
                .addBodyParameter("date", date)
                .addBodyParameter("u_id", user_id)
                .setTag("Adding Message")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            if (response.getString("status").equals("ok"))
                            {
                                Log.v("added", response.getString("status"));
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
                       error.printStackTrace();
                    }
                });

    }

    public void setUserDetails(final String user_id)
    {
        builder = new AlertDialog.Builder(ctx);
        progressDialog = new ProgressDialog(ctx);

        progressDialog.setMessage("Finishing set up ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = ApiData.loanDetails();

        AndroidNetworking.post(url)
                .addBodyParameter("user_id", user_id)
                .setTag("Set User Details")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener()
                {
                    @Override
                    public void onResponse(final JSONObject response)
                    {
                        try
                        {
                            if (response.getString("status").equals("above"))
                            {
                                final String loan_limit = response.getString("loan_limit");
                                final String loan_limit_value = response.getString("loan_limit_value");
                                final String phone = response.getString("phone");
                                final String user_id = response.getString("user_id");
                                final String name = response.getString("name");

                                progressDialog.dismiss();

                                builder.setTitle(R.string.account_setup_head);
                                builder.setMessage(R.string.account_setup_text);
                                builder.setCancelable(false);

                                builder.setPositiveButton(
                                        "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {


                                                HasLoanPersist hasLoanPersist = new HasLoanPersist(ctx);
                                                hasLoanPersist.clearInputValues();

                                                StepOnePersist stepOnePersist = new StepOnePersist(ctx);
                                                stepOnePersist.clearInputValues();

                                                StepTwoPersist stepTwoPersist = new StepTwoPersist(ctx);
                                                stepTwoPersist.clearInputValues();

                                                StepThreePersist stepThreePersist = new StepThreePersist(ctx);
                                                stepThreePersist.clearInputValues();

                                                Session session = new Session(ctx);
                                                session.createUserLoginSession(loan_limit,loan_limit_value,phone,user_id,name);

                                                InstalledCheck installedCheck = new InstalledCheck(ctx);
                                                installedCheck.createUserLoginSession("1", phone);

                                                dialog.cancel();

                                                Intent home_activity = new Intent(ctx, HomeActivity.class);

                                                home_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                home_activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                                ctx.startActivity(home_activity);

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
                        error.printStackTrace();

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
