package com.lend.loanee.pages;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.lend.loanee.R;
import com.lend.loanee.databinding.ActivityLoginBinding;
import com.lend.loanee.fragments.Emergency;
import com.lend.loanee.helpers.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        retrofit = new Retrofit.Builder()
                .baseUrl("https://mawingu.cbaloop.com/cba/")
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
                    String email=binding.mobile.getText().toString();
                    String password=binding.password.getText().toString();
//                    login(email,password);
                }
            }
        });
    }
    private void login(String email,String password){
        try {
            HashMap<String,String> data=new HashMap<>();
            data.put("mobile",email);
            data.put("password",password);

            binding.progress.setVisibility(View.VISIBLE);
            Call<ResponseBody> call1=apiService.loginUser(data);

            call1.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        try {
                            if(response.body() !=null){
                                String jsonresponse=response.body().string();

                            JSONObject json=new JSONObject(jsonresponse);

//                            Intent intent=new Intent(Login.this,Home.class);
//                            startActivity(intent);
                            }
                            else{
                                showSnackBar(response.toString(),false);
                                Log.d("LOG ME",response.toString());

                            }

                        } catch (IOException e) {
                            showSnackBar(e.getMessage() +"   IO",true);
                        } catch (JSONException e) {
                            showSnackBar(e.getMessage()+"   JSON",true);
                        }

                    }
                    else{
                        try {
                            String jsonresponse=response.errorBody().string();

//                            JSONObject json=new JSONObject(jsonresponse);

                            showSnackBar(jsonresponse.toString() + response.code(),true);
                        } catch (IOException e) {
                            showSnackBar(e.toString() +"  IO2",true);
                        } catch (Exception e) {
                            showSnackBar(e.getMessage()+"  JSON2",true);
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    showSnackBar(t.getMessage(),true);
                }
            });
        }
        catch (Exception e){

            showSnackBar(e.getMessage(),true);
        }

        binding.progress.setVisibility(View.GONE);
    }
    private Boolean valateFields(){
        boolean validated=true;
        if(binding.mobile.getText().toString().equals("")){
            showSnackBar("Name Required !!!",true);
            binding.mobile.requestFocus();
            validated=false;
        }
        else if(binding.password.getText().toString().equals("")){
            showSnackBar("Password Required !!!",true);
            binding.password.requestFocus();
            validated=false;
        }
        return validated;
    }

    private void showSnackBar(String text, boolean isError){
        Snackbar snackbar=Snackbar.make(binding.getRoot(),text,Snackbar.LENGTH_SHORT);
        if(isError){
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.red_2));
        }
        else{
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.green));
        }

        snackbar.show();
    }
}