package com.lend.loanee.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.lend.loanee.R;
import com.lend.loanee.databinding.FragmentLenderLoanBinding;
import com.lend.loanee.helpers.ApiService;
import com.lend.loanee.helpers.LoanRequest;
import com.lend.loanee.helpers.LoanRequestAdapter;
import com.lend.loanee.helpers.LoginData;
import com.lend.loanee.helpers.RequestListener;
import com.lend.loanee.pages.Home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LenderLoan extends Fragment implements RequestListener {

    FragmentLenderLoanBinding binding;
    Retrofit retrofit;
    ApiService apiService;
    LoginData loginData;
    ArrayList<LoanRequest> requests;
    private double balance=0.0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentLenderLoanBinding.inflate(inflater,container,false);
        initializer();

        return binding.getRoot();
    }
    @Override
    public void ProcessRequest(int position, HashMap<String,String> data) {

        if(Double.parseDouble(data.get("amount")) < balance) {

            String auth = "Bearer " + loginData.getToken();
            Call<ResponseBody> call2 = apiService.updateLoanStatus(auth, data);
            call2.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String jsonresponse = response.body().string();
                            JSONObject json = new JSONObject(jsonresponse);
                            showError(json.getString("message"), false);

                            queryLoans();
                        } catch (JSONException | IOException e) {
                            showError(e.getMessage(), true);
                        }
                    } else {
                        try {
                            String jsonresponse = response.errorBody().string();
                            JSONObject json = new JSONObject(jsonresponse);
                            showError(json.getString("error"), true);

                        } catch (IOException | JSONException e) {

                            showError(e.getMessage(), true);
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    showError(t.getMessage(), true);
                }
            });
        }
        else if(Double.parseDouble(data.get("amount")) > balance   && Objects.equals(data.get("Status"), "Approved")){
            showError("Sorry::!! You can't approve a loan your balance is below requested amount", true);
        }
    }
    private void initializer(){
        retrofit= new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.url).toString())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService =retrofit.create(ApiService.class);
        loginData=((Home) requireActivity()).getLoginData();
        queryLoans();
        checkLenderBalance();
    }
    private void queryLoans(){
        String auth="Bearer "+loginData.getToken();
        Call<ResponseBody> call1=apiService.getRequests(auth);
        call1.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()){
                    try {
                        String jsonresponse=response.body().string();
                        JSONObject json=new JSONObject(jsonresponse);
                        JSONArray data=json.getJSONArray("data");
                        requests=new ArrayList<>();
                        for(int i=0; i<data.length(); i++){
                            JSONObject request=data.getJSONObject(i);
                            String fullname=String.format("%s %s %s",request.get("firstName"),request.get("middleName"),request.get("lastName") );
                            LoanRequest loan=new LoanRequest(Double.parseDouble(request.get("loanAmount").toString()),request.get("interestRate").toString(),request.get("period").toString(),request.get("Status").toString(),fullname,request.get("app_ID").toString());
                            requests.add(loan);
                        }
                        loadAdapterData(requests);
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

    private void loadAdapterData(ArrayList<LoanRequest> requests){

        LoanRequestAdapter adapter=new LoanRequestAdapter(requests,requireContext(), this);
        binding.recyRequests.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyRequests.setHasFixedSize(true);
        binding.recyRequests.setAdapter(adapter);
    }

    private void checkLenderBalance(){
        String auth="Bearer "+loginData.getToken();
        Call<ResponseBody> call3= apiService.getBalance(auth);
        call3.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    String jsonResponse= null;
                    try {
                        jsonResponse = response.body().string();
                        JSONObject json=new JSONObject(jsonResponse);
                        JSONObject data=json.getJSONObject("data");
                        balance=data.getDouble("balance");
                    } catch (IOException | JSONException  e) {
                        showError(e.getMessage(),true);
                    }
                }
                else{
                    try {
                        String jsonResponse=response.errorBody().string();
                        JSONObject json=new JSONObject(jsonResponse);
                        showError(json.getString("error"),true);

                    } catch (IOException | JSONException e) {

                        showError(e.getMessage(),true);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(Objects.equals(t.getMessage(), "timeout")){
                    showError("Failed to connect , Try again",true);
                }
                else{
                    showError(t.getMessage(),true);
                }

            }
        });
    }
}
