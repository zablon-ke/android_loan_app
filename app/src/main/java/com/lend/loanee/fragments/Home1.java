package com.lend.loanee.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.lend.loanee.R;
import com.lend.loanee.databinding.FragmentHome1Binding;
import com.lend.loanee.helpers.ApiService;
import com.lend.loanee.helpers.LoginData;
import com.lend.loanee.pages.Home;

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


public class Home1 extends Fragment {

    FragmentHome1Binding binding;
    Retrofit retrofit;
    ApiService apiService;
    LoginData loginData;

    public Home1(LoginData loginData) {
        this.loginData=loginData;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentHome1Binding.inflate(inflater,container,false);
        ((Home) requireActivity()).setToolbar("");

        getChildFragmentManager().beginTransaction().replace(R.id.frm_main1,new LenderHistory()).commit();

        initializer();
        return binding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        if(toolbar !=null){
            toolbar.setBackgroundColor(getResources().getColor(R.color.white));
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.left));
            toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        }
    }
    private void initializer(){
        retrofit= new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.url).toString())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService =retrofit.create(ApiService.class);
        getLender();
    }
    private void getLender(){

        String auth=String.format("Bearer %s",loginData.getToken());
        Call<ResponseBody> call2= apiService.getLender(auth);
        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    String jsonresponse= null;
                    try {
                        jsonresponse = response.body().string();
                        JSONObject jsonObject=new JSONObject(jsonresponse);

                        JSONObject results=jsonObject.getJSONObject("data");
                        Object amount=results.get("amount");
                        if(Objects.equals(amount,null)){
                            binding.balance.setText("0.00");
                        }
                        else{
                            binding.balance.setText(amount+"");
                        }

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
                }}
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showError(t.getMessage(),true);
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
}