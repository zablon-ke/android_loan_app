package com.lend.loanee.pages;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.lend.loanee.R;
import com.lend.loanee.databinding.ActivityHomeBinding;
import com.lend.loanee.fragments.Home1;


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
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,new Home1()).commit();

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
public void setToolbar(String title){
        toolbar.setTitle(title);
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.topmenu,menu);
        return true;
    }
}