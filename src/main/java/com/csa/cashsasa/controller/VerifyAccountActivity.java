package com.csa.cashsasa.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.csa.cashsasa.R;
import com.csa.cashsasa.dao.AccountVerification;
import com.csa.cashsasa.dao.PhoneVerification;
import com.csa.cashsasa.model.MessageModel;
import com.csa.cashsasa.utility.NetworkUtil;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class VerifyAccountActivity extends AppCompatActivity
{

    private String phone, message;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;
    private ProgressBar check_sms_progress;
    private int progressStatus = 0;
    private Handler handler;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_verify_account);

        check_sms_progress = findViewById(R.id.check_sms_progress);

        handler = new Handler();

        progressStatus = 0;

        this.overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);

        builder = new AlertDialog.Builder(this);
        progressDialog = new ProgressDialog(this);

        if(ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED)
        {}
        else
        {
            int REQUEST_CODE_ASK_PERMISSIONS = 500;
            ActivityCompat.requestPermissions(VerifyAccountActivity.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progressStatus < 100)
                {
                    progressStatus +=1;

                    try
                    {
                        Thread.sleep(400);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run()
                        {
                            check_sms_progress.setProgress(progressStatus);

                            if(progressStatus == 100)
                            {
                                finishAccountVerify();
                            }
                        }
                    });
                }
            }
        }).start();
    }

    public void finishAccountVerify()
    {
        if(!NetworkUtil.getConnectionStatus(this))
        {
            progressDialog.setMessage("Finishing account verification...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            Uri uri = Uri.parse("content://sms/inbox");

            grantUriPermission("com.csa.cashsasa.controller.VerifyPhoneActivity", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

            String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
            Cursor c= getContentResolver().query(uri, projection, "address='AFRICASTKNG'", null, "date desc");
            startManagingCursor(c);

            bundle = getIntent().getExtras();
            assert bundle != null;
            phone = bundle.getString("phone");

            assert c != null;

            if(c.moveToFirst())
            {
                MessageModel sms = new MessageModel();
                sms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                sms.setBody(c.getString(c.getColumnIndexOrThrow("body")));
                sms.setNumber(c.getString(c.getColumnIndexOrThrow("address")));

                do{
                    message = sms.getBody();

                }

                while (c.moveToNext());

                if (!c.isClosed()) {
                    c.close();
                }
            }

            progressDialog.dismiss();

            AccountVerification accountVerification = new AccountVerification(this);
            accountVerification.verifyAction(message, phone);

        }
    }

    @Override
    public void onBackPressed()
    {
        builder.setTitle(R.string.close_app_head);
        builder.setMessage(R.string.cannot_close_app);
        builder.setCancelable(false);

        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
