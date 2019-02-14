package com.csa.cashsasa.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;
import com.csa.cashsasa.R;
import com.csa.cashsasa.controller.LoginActivity;
import com.csa.cashsasa.dao.ResetPin;
import com.csa.cashsasa.utility.NetworkUtil;
import com.csa.cashsasa.utility.UserExists;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ForgotPinActivity extends AppCompatActivity
{

    private EditText id_no, user_exists_status;
    private String reset_id_no, exists_status, stored_status ,restore_account, status_no;
    private Button reset_btn;
    private ScrollView back_btn;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_forgot_pin);

        id_no = findViewById(R.id.reset_pin_text);
        user_exists_status = findViewById(R.id.user_exists_status);
        reset_btn = findViewById(R.id.reset_btn);
        back_btn = findViewById(R.id.scroll);
        restore_account = "Restore Account";
        status_no = "no";
        builder = new AlertDialog.Builder(this);

        UserExists userExists = new UserExists(this);

        if(userExists.valuesExist())
        {
            user_exists_status.setText(status_no);
        }

        else
        {
            HashMap<String, String> hashMap = userExists.getUserDetails();

            stored_status = hashMap.get(UserExists.KEY_STATUS);
            user_exists_status.setText(stored_status);
            back_btn.setVisibility(View.GONE);
            reset_btn.setText(restore_account);
        }


        this.overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }

    public void loginPage(View view)
    {
        if(!NetworkUtil.getConnectionStatus(this))
        {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    public void resetPin(View view)
    {
        if(!NetworkUtil.getConnectionStatus(this))
        {
            reset_id_no = id_no.getText().toString().trim();
            exists_status = user_exists_status.getText().toString().trim();

            if(reset_id_no.length() > 0)
            {
                ResetPin resetPin = new ResetPin(this);
                resetPin.changePin(reset_id_no, exists_status);
            }
            else
            {
                Toast.makeText(this, "Please enter ID number",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        builder.setTitle(R.string.close_app_head);
        builder.setMessage(R.string.close_app_text);
        builder.setCancelable(false);

        builder.setPositiveButton(
                "Yes Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        finish();
                        moveTaskToBack(true);

                    }
                });

        builder.setNegativeButton(
                "No Don't",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
