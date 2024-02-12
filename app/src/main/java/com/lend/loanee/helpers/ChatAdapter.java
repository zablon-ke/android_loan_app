package com.lend.loanee.helpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lend.loanee.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    ArrayList<Chat> chats;
    Context context;

    public ChatAdapter(ArrayList<Chat> chats, Context context) {
        this.chats = chats;
        this.context = context;
    }
    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(context).inflate(R.layout.single_chat,parent,false);


        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {
        Chat chat=chats.get(position);
        holder.sender.setText(chat.getSender());
        holder.last_message.setText(chat.getLast_message());
        holder.last_time.setText(chat.getSend_time());
        if(!Objects.equals(chat.getImage_url(), "")){
            Picasso.get().load(Uri.parse(chat.getImage_url())).into(holder.src_img);
        }
        if(Objects.equals(chat.getStatus(), "1")){
            holder.status.setVisibility(View.GONE);
        }
        else{
            holder.status.setText("1");
        }
        holder.linear_ch.setOnClickListener(view ->{
            Intent intent=new Intent(context,com.lend.loanee.Chat_Page.class);
            intent.putExtra("id",chat.getSender());
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return chats.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView last_message,last_time,sender,status;
        CircleImageView src_img;
        LinearLayout linear_ch;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            last_message=itemView.findViewById(R.id.last_message);
            last_time=itemView.findViewById(R.id.message_time);
            sender=itemView.findViewById(R.id.user_name);
            src_img=itemView.findViewById(R.id.image_src);
            status=itemView.findViewById(R.id.unread);
            linear_ch=itemView.findViewById(R.id.linear_ch);
        }
    }
}
