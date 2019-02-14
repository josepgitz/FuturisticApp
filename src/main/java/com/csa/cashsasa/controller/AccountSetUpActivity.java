package com.csa.cashsasa.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.csa.cashsasa.R;
import com.csa.cashsasa.dao.AddUserMessage;
import com.csa.cashsasa.dao.PhoneVerification;
import com.csa.cashsasa.model.MessageModel;
import com.csa.cashsasa.utility.NetworkUtil;
import com.csa.cashsasa.utility.Session;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AccountSetUpActivity extends AppCompatActivity
{
    private String name,number, body, id, user_id, phone;
    private TextView account_welcome, account_phone;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;

    private ProgressBar simpleProgressBar;
    private int progressStatus = 0;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_account_set_up);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            this.overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_right);
        }

        Session session = new Session(this);
        HashMap<String, String> hashMap = session.getUserDetails();

        name = hashMap.get(Session.KEY_NAME);
        user_id = hashMap.get(Session.KEY_U_ID);
        phone = hashMap.get(Session.KEY_PHONE);

        account_welcome = findViewById(R.id.account_welcome);
        account_phone = findViewById(R.id.account_text_phone);

        account_welcome.setText(name);
        account_phone.setText(phone);

        simpleProgressBar = findViewById(R.id.progress_setup);

        handler = new Handler();

        progressStatus = 0;

        builder = new AlertDialog.Builder(this);
        progressDialog = new ProgressDialog(this);

        if(ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED)
        {}
        else
        {
            int REQUEST_CODE_ASK_PERMISSIONS = 500;
            ActivityCompat.requestPermissions(AccountSetUpActivity.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progressStatus < 100)
                {
                    progressStatus +=1;

                    try
                    {
                        Thread.sleep(250);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run()
                        {
                            simpleProgressBar.setProgress(progressStatus);

                            if(progressStatus == 100)
                            {
                                setUpAccount();
                            }
                        }
                    });
                }
            }
        }).start();

    }

    public void setUpAccount()
    {
        if(!NetworkUtil.getConnectionStatus(this))
        {
            progressDialog.setMessage("Setting up your account. Pleas wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            Uri uri = Uri.parse("content://sms/inbox");

            grantUriPermission("com.csa.cashsasa.controller.AccountSetUpActivity", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

            String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
            Cursor c= getContentResolver().query(uri, projection, "address='MPESA'", null, null);
            startManagingCursor(c);

            assert c != null;
            if(c.moveToFirst())
            {
                for(int i=0; i < c.getCount(); i++)
                {
                    MessageModel sms = new MessageModel();
                    sms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                    sms.setBody(c.getString(c.getColumnIndexOrThrow("body")));
                    sms.setNumber(c.getString(c.getColumnIndexOrThrow("address")));
                    sms.setDate(c.getString(c.getColumnIndexOrThrow("date")));

                    if(sms.getNumber().equals("MPESA"))
                    {
                        number = sms.getNumber();
                        body = sms.getBody();
                        id = sms.getId();

                        long milliSeconds = Long.parseLong(sms.getDate());
                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(milliSeconds);
                        String finalDateString = formatter.format(calendar.getTime());

                        AddUserMessage addUserMessage = new AddUserMessage(this);
                        addUserMessage.addAction(number,body,id,finalDateString,user_id);

                    }
                    c.moveToNext();
                }

                if (!c.isClosed()) {
                    c.close();
                }
            }

            AddUserMessage setUserDetails = new AddUserMessage(this);
            setUserDetails.setUserDetails(user_id);
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
