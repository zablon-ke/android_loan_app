package com.lend.loanee.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.lend.loanee.R;
import com.lend.loanee.databinding.ActivityHomeBinding;
import com.lend.loanee.fragments.Chats;
import com.lend.loanee.fragments.Home1;
import com.lend.loanee.fragments.HomeB;
import com.lend.loanee.fragments.LoanApply1;
import com.lend.loanee.fragments.LoanApply2;
import com.lend.loanee.fragments.Profile;
import com.lend.loanee.fragments.loan;
import com.lend.loanee.helpers.ApiService;
import com.lend.loanee.helpers.LoginData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Home extends AppCompatActivity {
    Toolbar toolbar;
    ActivityHomeBinding binding;
    LoginData loginData;
    Retrofit retrofit;
    ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);

        Intent intent=getIntent();

        Bundle bundle =intent.getExtras();
        if(bundle !=null){
            loginData=(LoginData) bundle.getSerializable("logins");
            if(Objects.equals(loginData.getRole(), "Lender")){
               getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,new Home1(loginData)).commit();
            }
            else{
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,new HomeB()).commit();
            }

        }
        try {
            initializer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

          binding.bottomNav.setOnNavigationItemSelectedListener(item -> {
              int id=item.getItemId();
              if(id ==R.id.home){
                  if(Objects.equals(loginData.getRole(),"Lender")){
                      toolbar.setTitle("");
                      getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,new Home1(loginData)).commit();
                  }
                  else{
                      toolbar.setTitle("");
                      getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,new HomeB()).commit();
                  }
              }
              else if(id ==R.id.profile){
                  toolbar.setTitle("Profile");
                  getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,new Profile()).commit();
              }
              else if(id ==R.id.loan){
                  toolbar.setTitle("Loan details");
                  try {
                      if (loginData.getDetails() != null) {
                          if (loginData.getDetails().getJSONArray("loan").length() > 0) {
                              getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new loan()).commit();
                          } else {
                              getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new LoanApply1()).commit();
                          }}
                      } catch(JSONException e){
                          throw new RuntimeException(e);
                      }
                  }

              else if(id ==R.id.chats){
                  toolbar.setTitle("Chats");
                  getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,new Chats()).commit();
              }
              return true;
          });

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

    public LoginData getLoginData() {
        return loginData;
    }

    private void getUser(){

        try {
            String auth="Bearer "+loginData.getToken();
            Call<ResponseBody> call1=apiService.getUser(auth);
            call1.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        try {
                            String jsonresponse=response.body().string();
                            JSONObject jsonObject=new JSONObject(jsonresponse);
                            loginData.setDetails(jsonObject.getJSONObject("data"));
                        } catch (IOException | JSONException e) {
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
    private void initializer(){
        retrofit= new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.url).toString())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService =retrofit.create(ApiService.class);

        getUser();
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

    @Override
    public boolean onNavigateUp() {


        return super.onNavigateUp();

    }
}