package com.csa.cashsasa.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.csa.cashsasa.R;
import com.csa.cashsasa.dao.ApplyLoan;
import com.csa.cashsasa.utility.HasLoanPersist;
import com.csa.cashsasa.utility.InstalledCheck;
import com.csa.cashsasa.utility.NetworkUtil;
import com.csa.cashsasa.utility.Session;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ApplyActivity extends AppCompatActivity
{

    private Toolbar toolbar;
    private EditText loan_amount;
    private CheckBox tlc;
    private String loan, has_loan, loan_borrowed, user_id, loan_limit, loan_info;
    private TextView welcome_text, loan_limit_text;
    private int loan_value, limit_value;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_apply);

        this.overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_left);

        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.apply_loan_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loan_amount = findViewById(R.id.loan_amount);
        tlc = findViewById(R.id.loan_tc);

        welcome_text = findViewById(R.id.welcome_text);
        loan_limit_text = findViewById(R.id.loan_limit_text);

        has_loan = "no";

        Session session = new Session(this);
        HashMap<String, String> sessValues = session.getUserDetails();

        loan_limit_text.setText(sessValues.get(Session.KEY_LIMIT));
        user_id = sessValues.get(Session.KEY_U_ID);
        loan_limit = sessValues.get(Session.KEY_LIMIT_VALUE);

        HasLoanPersist hasLoanPersist = new HasLoanPersist(this);
        HashMap<String, String> hashMap = hasLoanPersist.getInputValues();


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

        builder = new AlertDialog.Builder(this);

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

    public void loanApplication(final View view)
    {
        if(!NetworkUtil.getConnectionStatus(this))
        {
            loan = loan_amount.getText().toString();

            if(loan.trim().length() == 0)
            {
                Toast.makeText(this, R.string.loan_empty, Toast.LENGTH_SHORT).show();
            }

            else if (!tlc.isChecked())
            {
                Toast.makeText(this, R.string.tc_empty, Toast.LENGTH_SHORT).show();
            }

            else
            {
                loan_value = Integer.parseInt(loan);
                limit_value = Integer.parseInt(loan_limit);
                loan_info = "Your loan amount must be greater than 500 and less than " + limit_value;

                if(loan_value < 500)
                {
                    builder.setTitle(R.string.loan_amount_error);
                    builder.setMessage(loan_info);
                    builder.setCancelable(false);

                    builder.setPositiveButton(
                            R.string.another_loan_text,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert_err1 = builder.create();
                    alert_err1.show();
                }

                else if(loan_value > limit_value)
                {
                    builder.setTitle(R.string.loan_amount_error);
                    builder.setMessage(loan_info);
                    builder.setCancelable(false);

                    builder.setPositiveButton(
                            R.string.another_loan_text,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert_err2 = builder.create();
                    alert_err2.show();
                }

                else
                {
                    String borrowed_value = "Ksh. "+ loan;

                    String loan_details = "Amount Borrowed : "+ borrowed_value + "\n\n"

                                        + "Payable Period : 1 Month";


                    builder.setTitle(R.string.verify_loan);
                    builder.setMessage(loan_details);
                    builder.setCancelable(false);

                    builder.setPositiveButton(
                            R.string.accept_loan,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id)
                                {

                                    dialog.cancel();

                                    ApplyLoan applyLoan = new ApplyLoan(view.getContext());
                                    applyLoan.loanApplication(loan, user_id);
                                }
                            });

                    builder.setNegativeButton(
                            R.string.reject_loan,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert_done = builder.create();
                    alert_done.show();
                }

            }

        }
    }
}
