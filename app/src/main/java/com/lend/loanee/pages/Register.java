package com.lend.loanee.pages;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.lend.loanee.R;
import com.lend.loanee.databinding.ActivityRegisterBinding;
import com.lend.loanee.fragments.Activate;
import com.lend.loanee.fragments.CreateProfile;
import com.lend.loanee.fragments.Emergency;
import com.lend.loanee.helpers.ApiService;

import retrofit2.Retrofit;

public class Register extends AppCompatActivity implements Emergency.OnToolbarTextChangeListener {

    ActivityRegisterBinding binding;
    Retrofit retrofit;
    ApiService apiService;
    Toolbar toolbar;

    String gender="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar=findViewById(R.id.toolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main,new Activate()).commit();

        toolbar.setTitle("Activate");

    }

    public  void setToolbar(String title){
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
    }
    @Override
    public void updateToolbarText(String newText) {
        toolbar.setTitle(newText);
    }
}