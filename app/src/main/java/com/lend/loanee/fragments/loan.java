package com.lend.loanee.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.lend.loanee.R;
import com.lend.loanee.databinding.FragmentLoanBinding;
import com.lend.loanee.helpers.ApiService;
import com.lend.loanee.helpers.LoginData;
import com.lend.loanee.pages.Home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class loan extends Fragment {
    public loan() {
        // Required empty public constructor
    }
   FragmentLoanBinding binding;
    LoginData loginData;
    Retrofit retrofit;
    ApiService apiService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentLoanBinding.inflate(inflater,container,false);

        loginData=((Home) requireActivity()).getLoginData();
        initializer();
        return binding.getRoot();
    }
    private void fetchLoan(){
        String auth="Bearer "+loginData.getToken();
        Call<ResponseBody> call2= apiService.fetchLoan(auth);
        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                      String jsonresponse=response.body().string();
                          JSONObject jsonObject=new JSONObject(jsonresponse);
                         JSONArray data=jsonObject.getJSONArray("data");
                         if(data.length() >0){
                             double amount =Double.parseDouble(data.getJSONObject(0).get("loanAmount").toString());

                             String status=data.getJSONObject(0).get("State").toString();


                             double interest=Double.parseDouble( data.getJSONObject(0).get("interestRate").toString());
                             binding.amount.setText(String.format("%,.2f", amount));
                             binding.interest.setText(String.valueOf(interest * amount /100));
                             String period=data.getJSONObject(0).get("period").toString();
                             String dateString=data.getJSONObject(0).get("Date_applied").toString();

                             if(dateString !=null){
                                 Instant instant= Instant.parse(dateString);
                                 Instant paymentDate=instant.plus(Long.parseLong(period), ChronoUnit.DAYS);
                                 Instant now=Instant.now();
                                 long daysBetween = Duration.between(now,paymentDate).toDays();
                                 if (daysBetween <0) binding.status.setBackgroundColor(Color.RED);
                                 binding.status.setText(status);

                                 binding.remainingDays.setText(String.format("%s days",String.valueOf(daysBetween)));
                                 binding.repaymentDate.setText(String.format("%s ",Timestamp.from(paymentDate).toLocaleString()));

                                 showError(" Date applied "+Timestamp.from(instant).toLocaleString()+" Due Date:  "+Timestamp.from(paymentDate).toLocaleString(),false);
                             }
                         }
                         Log.d("LOG ME",jsonresponse.toString());
                    } catch (IOException |JSONException   e) {
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
    private void initializer(){
        retrofit= new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.url).toString())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService =retrofit.create(ApiService.class);
        fetchLoan();
    }
    private void showError(String text,Boolean isError){
       try {
           Snackbar snackbar=Snackbar.make(binding.amount,text,Snackbar.LENGTH_SHORT);
           if(isError){
               snackbar.getView().setBackgroundColor(requireActivity().getResources().getColor(R.color.red_2));
           }
           else{
               snackbar.getView().setBackgroundColor(requireActivity().getResources().getColor(R.color.teal));
           }
           snackbar.show();
       }
       catch (Exception e){

           showError(e.getMessage(),true);
       }
    }

}