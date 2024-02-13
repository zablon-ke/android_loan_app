package com.lend.loanee.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.lend.loanee.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class LoanRequestAdapter extends RecyclerView.Adapter<LoanRequestAdapter.MyViewHolder> {


    ArrayList<LoanRequest> requests;
    Context context;

    RequestListener listener;

    public LoanRequestAdapter(ArrayList<LoanRequest> requests, Context context, RequestListener listener) {
        this.requests = requests;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LoanRequestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(context).inflate(R.layout.loan_single,parent,false);

        return new MyViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull LoanRequestAdapter.MyViewHolder holder, int position) {

        LoanRequest loanRequest=requests.get(position);
        holder.txt_name.setText(loanRequest.getFullname());
        holder.txt_amount.setText(String.format("Request a loan of Ksh %,.2f",loanRequest.getAmount()));
        holder.txt_period.setText(String.format("Repayment in %s days",loanRequest.getPeriod()));
        holder.txt_status.setText(String.format("Application %s",loanRequest.getStatus()));
        double interest=loanRequest.getAmount() * Double.parseDouble(loanRequest.getInterest()) /80;
        holder.txt_interest.setText(String.format("Interest to be earned %.0f",interest));

        if(Objects.equals(loanRequest.getStatus(),"pending")){
            holder.txt_status.setVisibility(View.GONE);
            holder.btnReapprove.setVisibility(View.GONE);
        }
        else{
            holder.linearAccept.setVisibility(View.GONE);
            if(loanRequest.getStatus().equals("Rejected")) holder.txt_status.setTextColor(Color.RED);
            else holder.txt_status.setTextColor(Color.GREEN);

            if(loanRequest.getStatus().equals("Approved")){
                holder.btnReapprove.setVisibility(View.GONE);
            }
        }
        holder.btnAccept.setOnClickListener(view ->{
            HashMap<String,String> data=new HashMap<>();
            data.put("status","Approved");
            data.put("app_ID",loanRequest.getApp_ID());
            data.put("amount", String.valueOf(loanRequest.getAmount()));
            listener.ProcessRequest(holder.getAdapterPosition(), data);
            notifyItemChanged(holder.getAdapterPosition());
        });
        holder.btnDecline.setOnClickListener(view ->{
            HashMap<String,String> data=new HashMap<>();
            data.put("status","Rejected");
            data.put("amount", String.valueOf(loanRequest.getAmount()));
            data.put("app_ID", loanRequest.getApp_ID());
            listener.ProcessRequest(holder.getAdapterPosition(), data);
            notifyItemChanged(holder.getAdapterPosition());
        });

        holder.btnReapprove.setOnClickListener(view ->{
            HashMap<String,String> data=new HashMap<>();
            data.put("status","Approved");
            data.put("app_ID",loanRequest.getApp_ID());
            data.put("amount", String.valueOf(loanRequest.getAmount()));
            listener.ProcessRequest(holder.getAdapterPosition(), data);
            notifyItemChanged(holder.getAdapterPosition());
        });

    }
    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {

        TextView txt_name,txt_amount,txt_period,txt_status,txt_interest;
        LinearLayout linearAccept;
        AppCompatButton btnAccept,btnDecline,btnReapprove;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_name=itemView.findViewById(R.id.txt_fullname);
            txt_amount=itemView.findViewById(R.id.txt_amount);
            txt_period=itemView.findViewById(R.id.txt_period);
            txt_status=itemView.findViewById(R.id.txt_status);
            txt_interest=itemView.findViewById(R.id.txt_interest);


//            Buttons
            btnAccept=itemView.findViewById(R.id.btn_accept);
            btnDecline=itemView.findViewById(R.id.btn_decline);
            linearAccept=itemView.findViewById(R.id.linear_accept);
            btnReapprove=itemView.findViewById(R.id.btn_reapprove);
        }
    }
}
