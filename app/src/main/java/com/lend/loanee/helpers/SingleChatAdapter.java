package com.lend.loanee.helpers;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lend.loanee.R;

import java.util.ArrayList;
import java.util.Objects;

public class SingleChatAdapter extends RecyclerView.Adapter<SingleChatAdapter.MyViewHolder> {
    ArrayList<Single_Chat> chats;
    Context context;
    String sender;

    public SingleChatAdapter(ArrayList<Single_Chat> chats, Context context, String sender) {
        this.chats = chats;
        this.context = context;
        this.sender = sender;
    }

    @NonNull
    @Override
    public SingleChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.single_chat1,parent,false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleChatAdapter.MyViewHolder holder, int position) {

        Single_Chat chat=chats.get(position);
        holder.chat.setText(chat.getMessage());

        holder.linear_ch.setBackgroundColor(Color.TRANSPARENT);
        holder.cardView.setBackground(context.getResources().getDrawable(R.drawable.mine));
        if(!Objects.equals(chat.sender_id, sender)){
            LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,6,40,0);
            holder.cardView.setLayoutParams(params);
            holder.cardView.setBackground(context.getResources().getDrawable(R.drawable.them));
            holder.linear_ch.setGravity(Gravity.START);
        }
    }
    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView chat;
        LinearLayout linear_ch;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            chat=itemView.findViewById(R.id.chat);
            linear_ch=itemView.findViewById(R.id.linear_chat);
            cardView=itemView.findViewById(R.id.card);
        }


    }
}
