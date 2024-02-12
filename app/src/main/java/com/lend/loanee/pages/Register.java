package com.lend.loanee.pages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.lend.loanee.R;
import com.lend.loanee.databinding.ActivityRegisterBinding;
import com.lend.loanee.fragments.Activate;
import com.lend.loanee.fragments.CreateProfile;
import com.lend.loanee.fragments.Emergency;
import com.lend.loanee.helpers.ApiService;
import com.lend.loanee.helpers.LoginData;

import retrofit2.Retrofit;

public class Register extends AppCompatActivity implements Emergency.OnToolbarTextChangeListener {

    ActivityRegisterBinding binding;
    Retrofit retrofit;
    ApiService apiService;
    Toolbar toolbar;
    String gender="";
    LoginData data;
    String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar=findViewById(R.id.toolbar);

        Intent intent =getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle !=null){
            data=(LoginData) bundle.getSerializable("logins");
            role=bundle.getString("role");
        }
        toolbar.setTitle("Activate");
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main,new Activate(data,role)).commit();

    }
    public LoginData getLoginData(){
        return data;
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