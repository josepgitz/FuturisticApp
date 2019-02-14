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
import com.csa.cashsasa.utility.HasLoanPersist;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class ApplyLoan
{
    private Context ctx;
    private AlertDialog.Builder builder;
    private ProgressDialog progressDialog;
    private String loan_success, loan_error;
    private String loan_borrowed, borrowed_value, has_loan_status;

    public ApplyLoan(Context ctx)
    {
        this.ctx = ctx;
        builder = new AlertDialog.Builder(ctx);
        progressDialog = new ProgressDialog(ctx);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                . writeTimeout(120, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.initialize(ctx,okHttpClient);

        AndroidNetworking.setParserFactory(new JacksonParserFactory());

        loan_success = "loan_successful";
        loan_error = "loan_denied";

        progressDialog.setTitle(R.string.process_loan);
        progressDialog.setMessage("Processing Your Loan ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void loanApplication(String loan_amount, String user_id)
    {
        String regUrl = ApiData.applyLoan();

        AndroidNetworking.post(regUrl)
                .addBodyParameter("amount", loan_amount)
                .addBodyParameter("user_id", user_id)
                .setTag("Loan Application")
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
                                loan_borrowed = response.getString("loan");
                                borrowed_value = response.getString("loan_value");
                                has_loan_status = response.getString("has_loan");

                                progressDialog.dismiss();

                                builder.setTitle(R.string.loan_applied_success);
                                builder.setMessage(R.string.loan_applied_success_text);
                                builder.setCancelable(false);

                                builder.setPositiveButton(
                                        "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                                HasLoanPersist hasLoanPersist = new HasLoanPersist(ctx);
                                                hasLoanPersist.createInputSession(loan_borrowed,borrowed_value,has_loan_status);

                                                Intent home_activity = new Intent(ctx, HomeActivity.class);

                                                home_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                home_activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                                ctx.startActivity(home_activity);
                                            }
                                        });

                                AlertDialog alert = builder.create();
                                alert.show();
                            }

                            if (response.getString("status").equals(loan_error))
                            {
                                progressDialog.dismiss();
                                builder.setTitle(R.string.loan_applied_error);
                                builder.setMessage(R.string.loan_applied_error_text);
                                builder.setCancelable(true);

                                builder.setPositiveButton(
                                        "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id)
                                            {
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

                        AlertDialog alert2 = builder.create();
                        alert2.show();
                    }
                });
    }

}
