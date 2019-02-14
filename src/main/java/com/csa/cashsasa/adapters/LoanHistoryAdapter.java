package com.csa.cashsasa.adapters;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.csa.cashsasa.R;
import com.csa.cashsasa.model.LoanModel;

import java.util.List;

public class LoanHistoryAdapter extends RecyclerView.Adapter<LoanHistoryAdapter.MyViewHolder>
{
    private List<LoanModel> loanModelList;
    private static Drawable not_paid, paid;

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView loan_status, loan_date, borrowed_amount_value, payable_amount_value, loan_balance_value, loan_int_value, insurance_int_value;

        public MyViewHolder(View view) {
            super(view);
            loan_status = view.findViewById(R.id.loan_status);
            loan_date = view.findViewById(R.id.loan_date);
            borrowed_amount_value = view.findViewById(R.id.borrowed_amount_value);
            payable_amount_value  = view.findViewById(R.id.payable_amount_value);
            loan_balance_value  = view.findViewById(R.id.loan_balance_value);
            loan_int_value = view.findViewById(R.id.loan_int_value);
            insurance_int_value = view.findViewById(R.id.insurance_int_value);

            not_paid = view.getResources().getDrawable(R.drawable.ic_clear_black_24dp);
            paid = view.getResources().getDrawable(R.drawable.ic_check_circle_black_24dp);
        }
    }

    public  LoanHistoryAdapter(List<LoanModel> loanModelList) {
        this.loanModelList = loanModelList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.loan_history_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LoanModel loanModel = loanModelList.get(position);
        holder.loan_balance_value.setText(loanModel.getBalance());
        holder.payable_amount_value.setText(loanModel.getAmount_payable());
        holder.borrowed_amount_value.setText(loanModel.getAmount_borrowed());
        holder.loan_date.setText(loanModel.getDate_borrowed());
        holder.loan_status.setText(loanModel.getStatus());
        holder.loan_int_value.setText(loanModel.getLoan_int());
        holder.insurance_int_value.setText(loanModel.getInsurance_int());

        int[][] states_not_paid = new int[][]{
                new int[]{},
        };

        int[] colors_not_paid = new int[]{
                Color.parseColor("#e2000b"),
        };

        int[][] states_paid = new int[][]{
                new int[]{},
        };

        int[] colors_paid = new int[]{
                Color.parseColor("#036e03"),
        };

        int[][] states_partial = new int[][]{
                new int[]{},
        };

        int[] colors_partial = new int[]{
                Color.parseColor("#f2be03"),
        };

        ColorStateList not_paid_color = new ColorStateList(states_not_paid, colors_not_paid);
        ColorStateList paid_color = new ColorStateList(states_paid, colors_paid);
        ColorStateList partial_color = new ColorStateList(states_partial, colors_partial);

        if(loanModel.getStatus() == R.string.pending_loan)
        {
            holder.loan_status.setTextColor(not_paid_color);
            holder.loan_status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear_black_24dp, 0, 0, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.loan_status.setCompoundDrawableTintList(not_paid_color);
            }
        }

        if(loanModel.getStatus() == R.string.paid_loan)
        {
            holder.loan_status.setTextColor(paid_color);
            holder.loan_status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_24dp, 0, 0, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.loan_status.setCompoundDrawableTintList(paid_color);
            }
        }

        if(loanModel.getStatus() == R.string.partial_loan)
        {
            holder.loan_status.setTextColor(partial_color);
            holder.loan_status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error_black_24dp, 0, 0, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.loan_status.setCompoundDrawableTintList(partial_color);
            }
        }
    }

    @Override
    public int getItemCount() {
        return loanModelList.size();
    }
}
