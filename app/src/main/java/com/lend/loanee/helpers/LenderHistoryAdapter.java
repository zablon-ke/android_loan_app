package com.lend.loanee.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lend.loanee.R;
import com.lend.loanee.databinding.FragmentLenderHistoryBinding;

import java.util.ArrayList;

public class LenderHistoryAdapter extends RecyclerView.Adapter<LenderHistoryAdapter.MyViewHolder> {

    ArrayList<History> history;
    Context context;
    public LenderHistoryAdapter(ArrayList<History> history, Context context) {
        this.history = history;
        this.context = context;
    }
    @NonNull
    @Override
    public LenderHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);

       View view =inflater.inflate(R.layout.single_history,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LenderHistoryAdapter.MyViewHolder holder, int position) {

        History hist= history.get(position);
        holder.txt_title.setText(hist.getTitle());
        holder.txt_content.setText(hist.getContent());
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
       TextView txt_title;
       TextView txt_content;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_title=itemView.findViewById(R.id.text_title);
            txt_content=itemView.findViewById(R.id.text_content);

        }
    }
}
