package com.lend.loanee.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.lend.loanee.R;
import com.lend.loanee.databinding.ActivityHomeBinding;
import com.lend.loanee.databinding.ActivityUserProfileBinding;
import com.lend.loanee.helpers.ApiService;
import com.lend.loanee.helpers.LoginData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Retrofit;

public class UserProfile extends AppCompatActivity {

     ActivityUserProfileBinding binding;
    JSONObject profile;
    String token;
    Toolbar toolbar;

    Retrofit retrofit;
    ApiService apiService;


    boolean editable=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");

        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle !=null){
            token = bundle.getString("token");
            try {
                profile=(JSONObject) new JSONObject(Objects.requireNonNull(bundle.getString("profile")));
                setContents();
            } catch (JSONException e) {
                showError(e.getMessage(),true);
            }
        }

        binding.btnEdit.setOnClickListener(view ->{
            editable= !editable;
            if(editable){
                binding.firstName.setBackground(getResources().getDrawable(R.drawable.shape));
                binding.firstName.setEnabled(true);
            }
            else{
                binding.firstName.setBackgroundColor(getResources().getColor(R.color.light_gray));
                binding.firstName.setEnabled(false);
            }
        });
    }
    private void showError(String text,Boolean isError) {
        try{  Snackbar snackbar = Snackbar.make(binding.getRoot(), text, Snackbar.LENGTH_SHORT);
            if (isError) {
                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.red_2));
            } else {
                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.teal));
            }
            snackbar.show();
        }
        catch (Exception e){

        }
    }
    private  void setContents(){
        try {
            JSONObject json=profile.getJSONObject("profile");
            binding.firstName.setText(json.getString("firstName"));
            binding.middleName.setText(json.getString("middleName"));
            binding.surName.setText(json.getString("lastName"));
            binding.idNumber.setText(json.getString("ID"));
            binding.mobile.setText(json.getString("phone"));
            binding.dateOfBirth.setText(json.getString("DOB"));
            binding.email.setText(json.getString("Email"));
            String gender=json.getString("gender");
            if(Objects.equals(gender,"Female")){
                binding.imgFemale.setImageDrawable(getResources().getDrawable(R.drawable.c24));
                binding.imgMale.setImageDrawable(getResources().getDrawable(R.drawable.circle));
            }
            else{
                binding.imgMale.setImageDrawable(getResources().getDrawable(R.drawable.c24));
                binding.imgFemale.setImageDrawable(getResources().getDrawable(R.drawable.circle));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
}