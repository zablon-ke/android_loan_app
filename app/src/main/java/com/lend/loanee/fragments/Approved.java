package com.lend.loanee.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.lend.loanee.R;
import com.lend.loanee.databinding.FragmentApprovedBinding;
import com.lend.loanee.helpers.ApiService;
import com.lend.loanee.helpers.LoanRequest;
import com.lend.loanee.helpers.LoginData;
import com.lend.loanee.pages.Home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Approved extends Fragment {
    FragmentApprovedBinding binding;

    Retrofit retrofit;
    ApiService apiService;
    LoginData loginData;
    public Approved() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentApprovedBinding.inflate(inflater,container,false);
        initializer();

        binding.btnWithdraw.setOnClickListener(view ->{

        });
        return binding.getRoot();
    }

    private void queryLoan(){
        String auth="Bearer "+loginData.getToken();
        HashMap<String,String> data=new HashMap<>();
        data.put("status","Approved");
        Call<ResponseBody> call1=apiService.getLoan(auth,data.get("status"));
        call1.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()){
                    try {
                        String jsonresponse=response.body().string();
                        JSONObject json=new JSONObject(jsonresponse);
                        JSONArray data=json.getJSONArray("data");
                        JSONObject data1=data.getJSONObject(0);
                        double amount=Double.parseDouble(data1.get("loanAmount").toString());

                        binding.loanAmount.setText(getResources().getString(R.string.approved,String.valueOf(amount)));

                    } catch (IOException | JSONException e) {

                        showError(e.getMessage(),true);
                    }
                }
                else{
                    try {
                        String jsonresponse=response.errorBody().string();
                        JSONObject json=new JSONObject(jsonresponse);
                        showError(json.getString("error"),true);

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
    @SuppressLint("UseCompatLoadingForDrawables")
    private void showError(String text, Boolean isError){
        Snackbar snackbar=Snackbar.make(binding.getRoot(),text,Snackbar.LENGTH_SHORT);
        if(isError){
            snackbar.getView().setBackground(getResources().getDrawable(R.drawable.ligh_gray));

        }
        else{
            snackbar.getView().setBackground(getResources().getDrawable(R.drawable.bg_green));
        }
        snackbar.show();
    }

    private void initializer(){


        retrofit= new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.url).toString())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService =retrofit.create(ApiService.class);
        loginData=((Home) requireActivity()).getLoginData();
        queryLoan();
    }


}