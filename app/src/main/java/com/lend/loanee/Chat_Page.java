
package com.lend.loanee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.lend.loanee.databinding.ActivityChatPageBinding;
import com.lend.loanee.helpers.SingleChatAdapter;
import com.lend.loanee.helpers.Single_Chat;

import java.util.ArrayList;
import java.util.Arrays;

public class Chat_Page extends AppCompatActivity {

    ArrayList<Single_Chat> chats;
    ActivityChatPageBinding binding;
    String sender="";
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityChatPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        chats=new ArrayList<>();

        toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        Intent intent= new Intent();
        Bundle bundle= intent.getExtras();
        if(bundle !=null){
          sender=bundle.getString("sender");
        }
          chats.addAll(Arrays.asList(new Single_Chat("1","2","Hello Annex","16:20","1"),
                  new Single_Chat("2","2","Good morning","16:21","1")));

         binding.singleChatRecy.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
         binding.singleChatRecy.setHasFixedSize(true);
         SingleChatAdapter adapter=new SingleChatAdapter(chats,getApplicationContext(),"1");

        binding.singleChatRecy.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_bar,menu);
        return true;
    }
}