package com.csa.cashsasa.dao;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.csa.cashsasa.R;
import com.csa.cashsasa.api.ApiData;
import com.csa.cashsasa.controller.ForgotPinActivity;
import com.csa.cashsasa.controller.LoginActivity;
import com.csa.cashsasa.controller.VerifyPhoneActivity;
import com.csa.cashsasa.model.UserModel;
import com.csa.cashsasa.utility.UserExists;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

public class RegisterAction {
    private Context _ctx;
    private AlertDialog.Builder builder;
    private ProgressDialog progressDialog;
    private String id_exists, phone_exists, email_exists, net_error, sent_res, invalid_phone, not_mpesa;

    public RegisterAction(Context ctx) {
        this._ctx = ctx;
        builder = new AlertDialog.Builder(ctx);
        progressDialog = new ProgressDialog(ctx);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                . writeTimeout(180, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.initialize(ctx,okHttpClient);

        id_exists = "id_exists";
        phone_exists = "phone_exists";
        email_exists = "email_exists";
        net_error = "error";
        sent_res = "saved";
        invalid_phone = "invalid_phone";
        not_mpesa = "not_mpesa";

        progressDialog.setMessage("Processing Credentials ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void addUserDetails(final UserModel userModel)
    {
        String regUrl = ApiData.ProcessUserData();

        AndroidNetworking.post(regUrl)
                .addBodyParameter("country", userModel.getCountry())
                .addBodyParameter("fname", userModel.getFname())
                .addBodyParameter("lname", userModel.getLname())
                .addBodyParameter("email", userModel.getEmail())
                .addBodyParameter("national_id", userModel.getNational_id())
                .addBodyParameter("phone", userModel.getPhone())
                .addBodyParameter("password", userModel.getPassword())
                .setTag("Registration")
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals(phone_exists)) {
                            progressDialog.dismiss();
                            builder.setTitle(R.string.reg_info);
                            builder.setMessage(R.string.phone_exists);
                            builder.setCancelable(true);

                            builder.setPositiveButton(
                                    "Yes, I have",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                            UserExists userExists = new UserExists(_ctx);
                                            userExists.createUserLoginSession("yes");

                                            Intent intent = new Intent(_ctx, ForgotPinActivity.class);

                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            _ctx.startActivity(intent);

                                        }
                                    });

                            builder.setNegativeButton(
                                    "No, I don't",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert = builder.create();
                            alert.show();
                        }

                        if (response.equals(id_exists)) {
                            progressDialog.dismiss();
                            builder.setTitle(R.string.reg_info);
                            builder.setMessage(R.string.id_exists);
                            builder.setCancelable(true);

                            builder.setPositiveButton(
                                    "Yes, I have",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                            UserExists userExists = new UserExists(_ctx);
                                            userExists.createUserLoginSession("yes");

                                            Intent intent = new Intent(_ctx, ForgotPinActivity.class);

                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            _ctx.startActivity(intent);
                                        }
                                    });

                            builder.setNegativeButton(
                                    "No, I don't",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert_id = builder.create();
                            alert_id.show();

                        }

                        if (response.equals(email_exists)) {
                            progressDialog.dismiss();
                            builder.setTitle(R.string.reg_info);
                            builder.setMessage(R.string.email_exists);
                            builder.setCancelable(true);

                            builder.setPositiveButton(
                                    "Yes, I have",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                            UserExists userExists = new UserExists(_ctx);
                                            userExists.createUserLoginSession("yes");

                                            Intent intent = new Intent(_ctx, ForgotPinActivity.class);

                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            _ctx.startActivity(intent);
                                        }
                                    });

                            builder.setNegativeButton(
                                    "No, I don't",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert_email = builder.create();
                            alert_email.show();
                        }

                        if (response.equals(invalid_phone))
                        {
                            progressDialog.dismiss();
                            builder.setTitle(R.string.reg_info);
                            builder.setMessage(R.string.invalid_phone_number);
                            builder.setCancelable(true);

                            builder.setPositiveButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert_invalid_phone = builder.create();
                            alert_invalid_phone.show();
                        }

                        if (response.equals(not_mpesa))
                        {
                            progressDialog.dismiss();
                            builder.setTitle(R.string.reg_info);
                            builder.setMessage(R.string.not_mpesa_number);
                            builder.setCancelable(true);

                            builder.setPositiveButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert_mpesa = builder.create();
                            alert_mpesa.show();
                        }

                        if (response.equals(sent_res)) {
                            progressDialog.dismiss();

                            Intent verify_phone = new Intent(_ctx, VerifyPhoneActivity.class);

                            verify_phone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            verify_phone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            _ctx.startActivity(verify_phone);
                        }

                        if (response.equals(net_error)) {
                            progressDialog.dismiss();
                            builder.setTitle(R.string.reg_info);
                            builder.setMessage(R.string.net_error);
                            builder.setCancelable(true);

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
                    }

                    @Override
                    public void onError(ANError error) {

                        progressDialog.dismiss();

                        builder.setTitle(R.string.reg_info);
                        builder.setMessage(R.string.net_error);
                        builder.setCancelable(true);

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
