package com.lend.loanee.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.lend.loanee.R;
import com.lend.loanee.databinding.ActivitySignUpBinding;
import com.lend.loanee.helpers.ApiService;
import com.lend.loanee.helpers.LoginData;

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

public class sign_up extends AppCompatActivity {
    ActivitySignUpBinding binding;
    Retrofit retrofit;
    ApiService apiService;
    String role="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.url)).
                addConverterFactory(GsonConverterFactory.create())
        .build();

        apiService=retrofit.create(ApiService.class);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle !=null){

            role=bundle.getString("role");
        }

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validateFields()){
                    String mobile=binding.mobile.getText().toString();
                    String password =binding.password.getText().toString();
                    signup(mobile,password);
                };
            }
        });

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(sign_up.this,Login.class);
                startActivity(intent);
            }
        });
    }
    private void signup(String mobile,String password){
        try {

            HashMap<String,String> payload=new HashMap<>();
            payload.put("mobile",mobile);
            payload.put("password",password);
            Call<ResponseBody> call1=apiService.signUpUser(payload);
            call1.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        try {
                            String jsonresponse=response.body().string();
                            JSONObject jsonObject=new JSONObject(jsonresponse);
                            showError(jsonObject.get("message").toString(),false);
                            clear();
                            Intent intent=new Intent(getApplicationContext(), Register.class);
                            intent.putExtra("logins",new LoginData(mobile,password));
                            intent.putExtra("role",role);
                            startActivity(intent);
                        } catch (IOException e) {
                           showError(e.getMessage(),true);
                        } catch (JSONException e) {
                            showError(e.getMessage(),true);
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
                        } catch (IOException e) {
                            showError(e.getMessage(),true);
                        }
                        catch (JSONException e) {
                            showError(e.getMessage(),true);
                        }
                    }
                    clear();
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    showError(t.getMessage()+"  error",true);
                    clear();
                }
            });
        }
        catch (Exception e){
            showError(e.getMessage(),true);
        }
    }
    private void showError(String text,Boolean isError){
        Snackbar snackbar=Snackbar.make(binding.linear1,text,Snackbar.LENGTH_SHORT);
        if(isError){
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.red_2));
        }
        else{
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.teal));
        }
        snackbar.show();
    }
    private Boolean validateFields(){
        Boolean validated=true;

        if(binding.mobile.getText().toString().equals("")){
            showError("Mobile number required!!!",true);
            binding.mobile.requestFocus();
            validated=false;
        }
        if(binding.mobile.getText().toString().length() < 9){
            showError("Provide valid phone number!",true);
            binding.mobile.requestFocus();
            validated=false;
        }

        else if(binding.password.getText().toString().equals("")){
            showError("password required!!!",true);
            binding.password.requestFocus();
            validated=false;
        }
        else if(!binding.password.getText().toString().equals(binding.password2.getText().toString())){
            showError("password do not match!!!",true);
            binding.password.requestFocus();
            validated=false;
        }
  return  validated;
    }
    private void clear()
    {
        binding.password.setText("");
        binding.mobile.setText("");
        binding.password2.setText("");
    }
}