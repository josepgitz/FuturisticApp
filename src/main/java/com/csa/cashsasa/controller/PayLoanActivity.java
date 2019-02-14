package com.csa.cashsasa.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csa.cashsasa.R;
import com.csa.cashsasa.dao.PayLoan;
import com.csa.cashsasa.utility.HasLoanPersist;
import com.csa.cashsasa.utility.NetworkUtil;
import com.csa.cashsasa.utility.Session;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PayLoanActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private TextView welcome_text, loan_limit_text;
    private String has_loan, loan_borrowed, loan_pay, user_id, loan_limit;
    private AlertDialog.Builder builder;
    private EditText pay_amount_text;
    private int limit_value, amount_to_pay;
    private CheckBox tlc;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_pay_loan);

        this.overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_left);

        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.pay_loan_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        welcome_text = findViewById(R.id.welcome_text);
        loan_limit_text = findViewById(R.id.loan_limit_text);
        pay_amount_text = findViewById(R.id.loan_amount_pay);
        tlc = findViewById(R.id.loan_tc_pay);

        has_loan = "no";

        Session session = new Session(this);
        HashMap<String, String> sessValues = session.getUserDetails();

        loan_limit_text.setText(sessValues.get(Session.KEY_LIMIT));
        user_id = sessValues.get(Session.KEY_U_ID);
        loan_limit = sessValues.get(Session.KEY_LIMIT_VALUE);

        HasLoanPersist hasLoanPersist = new HasLoanPersist(this);
        HashMap<String, String> hashMap = hasLoanPersist.getInputValues();

        if(hasLoanPersist.valuesExist())
        {}
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

    public void payLoanAction(View view)
    {
        if(!NetworkUtil.getConnectionStatus(this))
        {
            loan_pay = pay_amount_text.getText().toString();

            if (loan_pay.trim().length() == 0)
            {
                Toast.makeText(this, R.string.loan_empty, Toast.LENGTH_SHORT).show();
            }

            else if (!tlc.isChecked())
            {
                Toast.makeText(this, R.string.tc_empty, Toast.LENGTH_SHORT).show();
            }

            else
            {
                amount_to_pay = Integer.parseInt(loan_pay);
                limit_value = Integer.parseInt(loan_limit);

                if(amount_to_pay < 50)
                {
                    builder.setTitle(R.string.pay_loan_limit_head);
                    builder.setMessage(R.string.pay_loan_limit_text);
                    builder.setCancelable(false);

                    builder.setPositiveButton(
                            R.string.another_pay_text,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert_err1 = builder.create();
                    alert_err1.show();
                }

                else
                {
                    PayLoan payLoan = new PayLoan(this);
                    payLoan.loanPayment(loan_pay,user_id);
                }
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
