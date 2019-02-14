package com.csa.cashsasa.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.csa.cashsasa.R;
import com.csa.cashsasa.adapters.LoanHistoryAdapter;
import com.csa.cashsasa.api.ApiData;
import com.csa.cashsasa.model.LoanModel;
import com.csa.cashsasa.utility.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoanHistoryActivity extends AppCompatActivity
{

    private TextView no_loan_history;
    private Toolbar toolbar;
    private List<LoanModel> loanModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LoanHistoryAdapter mAdapter;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;
    private String borrowed, loan_figure, loan_balance, loan_date, pay_status, user_id, loan_int, insurance_int;
    private int loan_p_status;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_loan_history);

        this.overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_left);

        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        no_loan_history = findViewById(R.id.no_loan_history);

        builder = new AlertDialog.Builder(this);
        progressDialog = new ProgressDialog(this);

        getSupportActionBar().setTitle(R.string.loan_history_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Session session = new Session(this);
        HashMap<String, String> sessValues = session.getUserDetails();

        user_id = sessValues.get(Session.KEY_U_ID);

        recyclerView = findViewById(R.id.recycler_view_history);

        myLoanHistory();

    }

    private void myLoanHistory()
    {

        String apiUrl = ApiData.loanHistory();

        progressDialog.setMessage("Processing Your Loan History ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AndroidNetworking.post(apiUrl)
                .addBodyParameter("user_id", user_id)
                .setTag("Loan History")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        progressDialog.dismiss();

                        for(int i = 0; i < response.length(); i++)
                        {
                            if(response.length() == 0)
                            {
                                no_loan_history.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                no_loan_history.setVisibility(View.GONE);

                                try
                                {
                                    JSONObject jsonObject = response.getJSONObject(i);

                                    borrowed = jsonObject.getString("actual");
                                    loan_figure = jsonObject.getString("amount");
                                    loan_balance = jsonObject.getString("balance");
                                    loan_date = jsonObject.getString("loan_date");
                                    pay_status = jsonObject.getString("pay_status");
                                    loan_int = jsonObject.getString("interest");
                                    insurance_int = jsonObject.getString("insurance");

                                    if(pay_status.equals("0"))
                                    {
                                        loan_p_status = R.string.pending_loan;
                                    }

                                    if(pay_status.equals("1"))
                                    {
                                        loan_p_status = R.string.partial_loan;
                                    }

                                    if(pay_status.equals("2"))
                                    {
                                        loan_p_status = R.string.paid_loan;
                                    }

                                    LoanModel loanModel = new LoanModel(borrowed,loan_figure,loan_balance, loan_date, loan_p_status,loan_int,insurance_int);
                                    loanModelList.add(loanModel);

                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        mAdapter = new LoanHistoryAdapter(loanModelList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onError(ANError error)
                    {
                        // handle error

                        progressDialog.dismiss();

                    }
                });
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
