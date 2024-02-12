package com.lend.loanee.pages;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.lend.loanee.R;
import com.lend.loanee.databinding.ActivityLoginBinding;
import com.lend.loanee.fragments.Emergency;
import com.lend.loanee.helpers.ApiService;
import com.lend.loanee.helpers.LoginData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Login extends AppCompatActivity {


    ApiService apiService;
    Retrofit retrofit;
    ActivityLoginBinding binding;
    LoginData data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Login.this, sign_up.class);
                startActivity(intent);
            }
        });

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(valateFields() ){
                    String phone=binding.mobile.getText().toString();
                    String password=binding.password.getText().toString();
                    login(phone,password);
                }
            }
        });
    }
    private void login(String mobile,String password){
        try {
            HashMap<String,String> hash=new HashMap<>();
            hash.put("mobile",mobile);
            hash.put("password",password);
            Call<ResponseBody> call1=apiService.loginUser(hash);
            call1.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        String jsonresponse= null;
                        try {
                            jsonresponse = response.body().string();
                            JSONObject jsonObject=new JSONObject(jsonresponse);
                            String token=jsonObject.getJSONObject("data").getString("token");
                            String role=jsonObject.getJSONObject("data").getJSONObject("results").getString("rol");
                            data=new LoginData(mobile,password,token,role);

                            Intent intent=new Intent(getApplicationContext(), Home.class);
                            intent.putExtra("logins",data);

                            Log.d("LOG ME",jsonObject.toString());
                            showError(jsonObject.get("message").toString(),false);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(intent);
                                }
                            },500);

                        } catch (IOException | JSONException e) {
                            showError(e.getMessage().toString(),true);
                        }
                    }
                    else{
                        try {
                            String jsonresponse= null;
                            jsonresponse = response.errorBody().string();
                            if(jsonresponse !=null){
                                JSONObject error=new JSONObject(jsonresponse);
                                showError(error.get("error").toString(),false);
                            }
                        } catch (IOException | JSONException e) {
                            showError(e.getMessage(),true);
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    showError(t.getMessage(),true);
                }
            });
        }
        catch (Exception e){

            showError(e.getMessage(),true);
        }
    }
    private Boolean valateFields(){
        boolean validated=true;
        if(binding.mobile.getText().toString().equals("")){
            showError("Name Required !!!",true);
            binding.mobile.requestFocus();
            validated=false;
        }
        else if(binding.password.getText().toString().equals("")){
            showError("Password Required !!!",true);
            binding.password.requestFocus();
            validated=false;
        }
        return validated;
    }

    private void showError(String text,Boolean isError){
        Snackbar snackbar=Snackbar.make(binding.getRoot(),text,Snackbar.LENGTH_SHORT);
        if(isError){
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.red_2));
        }
        else{
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.teal));
        }
        snackbar.show();
    }

}