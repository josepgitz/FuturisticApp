package com.csa.cashsasa.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csa.cashsasa.R;
import com.csa.cashsasa.controller.ApplyActivity;
import com.csa.cashsasa.controller.FaqsActivity;
import com.csa.cashsasa.controller.LoanHistoryActivity;
import com.csa.cashsasa.controller.PayLoanActivity;
import com.csa.cashsasa.utility.HasLoanPersist;
import com.csa.cashsasa.utility.NetworkUtil;
import com.csa.cashsasa.utility.Session;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeActivity extends AppCompatActivity
{

    private Toolbar toolbar;
    private TextView welcome_text, loan_limit_text;
    private String has_loan, loan_borrowed;
    private AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_home);

        this.overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);

        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setLogo(R.mipmap.ic_launcher_round);

        has_loan = "no";

        HasLoanPersist hasLoanPersist = new HasLoanPersist(this);
        HashMap<String, String> hashMap = hasLoanPersist.getInputValues();

        welcome_text = findViewById(R.id.welcome_text);
        loan_limit_text = findViewById(R.id.loan_limit_text);

        Session session = new Session(this);
        HashMap<String, String> sessValues = session.getUserDetails();

        loan_limit_text.setText(sessValues.get(Session.KEY_LIMIT));

        if(hasLoanPersist.valuesExist())
        {
        }
        else
        {
            loan_borrowed = hashMap.get(HasLoanPersist.KEY_LOAN_AMOUNT);
            has_loan = hashMap.get(HasLoanPersist.KEY_HAS_LOAN);

            if(has_loan.equals("yes"))
            {
                welcome_text.setText(R.string.outstanding_loan);
                loan_limit_text.setText(loan_borrowed);
            }
        }



        RelativeLayout.LayoutParams wel = (RelativeLayout.LayoutParams) welcome_text.getLayoutParams();
        RelativeLayout.LayoutParams lm = (RelativeLayout.LayoutParams) loan_limit_text.getLayoutParams();

        builder = new AlertDialog.Builder(this);

    }

    public void applyLoan(View view)
    {
        if(!NetworkUtil.getConnectionStatus(this))
        {
            if(has_loan.equals("yes"))
            {
                builder.setTitle(R.string.you_have_loan_title);
                builder.setMessage(R.string.you_have_loan_text);
                builder.setCancelable(false);

                builder.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
            else
            {
                startActivity(new Intent(this, ApplyActivity.class));
            }
        }
    }

    public void loanHistory(View view)
    {
        if(!NetworkUtil.getConnectionStatus(this))
        {
            startActivity(new Intent(this, LoanHistoryActivity.class));
        }
    }

    public void payLoan(View view)
    {
        if(!NetworkUtil.getConnectionStatus(this))
        {
            if(has_loan.equals("no"))
            {
                builder.setTitle(R.string.no_loan_header);
                builder.setMessage(R.string.no_loan_text);
                builder.setCancelable(false);

                builder.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
            else
            {
                startActivity(new Intent(this, PayLoanActivity.class));
            }
        }
    }

    public void faqsPage(View view)
    {
        if(!NetworkUtil.getConnectionStatus(this))
        {
            startActivity(new Intent(this, FaqsActivity.class));
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

