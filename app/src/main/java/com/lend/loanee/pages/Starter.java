package com.lend.loanee.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lend.loanee.R;
import com.lend.loanee.databinding.ActivityStarterBinding;

public class Starter extends AppCompatActivity {

    ActivityStarterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityStarterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Starter.this,Login.class);
                startActivity(intent);
            }
        });
        binding.btnBorrower.setOnClickListener(view ->{
                Intent intent=new Intent(this,sign_up.class);
                intent.putExtra("role","Borrower");
                startActivity(intent);
        });

        binding.btnLender.setOnClickListener(view ->{
            Intent intent=new Intent(this,sign_up.class);
            intent.putExtra("role","Lender");
            startActivity(intent);
        });
    }
}