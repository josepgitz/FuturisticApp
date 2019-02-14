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
import com.androidnetworking.interfaces.StringRequestListener;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.csa.cashsasa.R;
import com.csa.cashsasa.api.ApiData;
import com.csa.cashsasa.controller.AccountSetUpActivity;
import com.csa.cashsasa.controller.HomeActivity;
import com.csa.cashsasa.controller.VerifyPhoneActivity;
import com.csa.cashsasa.model.UserModel;
import com.csa.cashsasa.utility.HasLoanPersist;
import com.csa.cashsasa.utility.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class LoginAction {
    private Context ctx;
    private AlertDialog.Builder builder;
    private ProgressDialog progressDialog;

    public LoginAction(Context ctx) {
        this.ctx = ctx;
        builder = new AlertDialog.Builder(ctx);
        progressDialog = new ProgressDialog(ctx);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                . writeTimeout(120, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.initialize(ctx,okHttpClient);

        progressDialog.setMessage("Verifying Credentials ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void userLogin(String phone, String password)
    {
        String url = ApiData.LoginUser();

        AndroidNetworking.post(url)
                .addBodyParameter("phone", phone)
                .addBodyParameter("password", password)
                .setTag("Login")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            if (response.getString("status").equals("wrong_details"))
                            {
                                progressDialog.dismiss();

                                builder.setMessage(R.string.login_info);
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

                            else if(response.getString("status").equals("above"))
                            {
                                final String loan_limit = response.getString("loan_limit");
                                final String loan_limit_value = response.getString("loan_limit_value");
                                final String phone = response.getString("phone");
                                final String user_id = response.getString("user_id");
                                final String name = response.getString("name");

                                Session session = new Session(ctx);
                                session.createUserLoginSession(loan_limit, loan_limit_value,phone,user_id,name);

                                progressDialog.dismiss();

                                Intent home_activity = new Intent(ctx, HomeActivity.class);
                                ctx.startActivity(home_activity);
                            }

                            else
                            {
                                final String loan_limit = response.getString("loan_limit");
                                final String loan_limit_value = response.getString("loan_limit_value");
                                final String phone = response.getString("phone");
                                final String user_id = response.getString("user_id");
                                final String name = response.getString("name");
                                final String has_loan = response.getString("has_loan");
                                final String loan_amount = response.getString("loan");
                                final String loan_amount_value = response.getString("loan_value");

                                Session session = new Session(ctx);
                                session.createUserLoginSession(loan_limit, loan_limit_value, phone,user_id,name);

                                HasLoanPersist hasLoanPersist = new HasLoanPersist(ctx);
                                hasLoanPersist.createInputSession(loan_amount,loan_amount_value , has_loan);

                                Intent home_activity = new Intent(ctx, HomeActivity.class);

                                home_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                home_activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                progressDialog.dismiss();

                                ctx.startActivity(home_activity);
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
