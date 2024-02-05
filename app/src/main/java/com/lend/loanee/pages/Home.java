package com.lend.loanee.pages;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.lend.loanee.R;
import com.lend.loanee.databinding.ActivityHomeBinding;


public class Home extends AppCompatActivity {


    Toolbar toolbar;
    ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        if(getSupportActionBar() !=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public void onBackPressed() {


        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {

        super.onBackPressed();
        return true;
    }
}