package com.lend.loanee.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.material.snackbar.Snackbar;
import com.lend.loanee.R;
import com.lend.loanee.databinding.FragmentLoanApply1Binding;
import com.lend.loanee.helpers.ApiService;
import com.lend.loanee.helpers.LoginData;
import com.lend.loanee.pages.Home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoanApply1 extends Fragment {



    FragmentLoanApply1Binding binding;
    LoginData loginData;
    Retrofit retrofit;
    ApiService apiService;
    ArrayList<String> periods ;
    ArrayList<JSONObject> periodDetails;
    public LoanApply1() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentLoanApply1Binding.inflate(inflater,container,false);
        updateToolbar();

        String[] list2={"14 days","30 days","60 days","90 days"};
        ArrayList<String> list = new ArrayList<>(Arrays.asList(list2));
        ArrayAdapter<String> adapter=new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1,list);

        initializer();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.period.setAdapter(adapter);

        binding.btnApply.setOnClickListener(view ->{
             if(validateFields()){

                 try {
                     String amount=binding.amount.getText().toString().trim();
                     String purpose=binding.purpose.getSelectedItem().toString();

                     HashMap<String,String> payload=new HashMap<>();
                     payload.put("amount",amount);
                     payload.put("purpose",purpose);
                     payload.put("type_ID",periodDetails.get(binding.period.getSelectedItemPosition()-1).getString("ID"));
                     applyLoan(payload);

                 } catch (JSONException e) {
                    showError(e.getMessage(),true);
                 }
             }
        });

        loginData=((Home) requireContext()).getLoginData();
        binding.period.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if(!binding.amount.getText().toString().equals("") && position !=0){
                        double amount=Double.parseDouble(binding.amount.getText().toString());
                        Double interest=amount * Double.parseDouble(periodDetails.get(position-1).get("interestRate").toString()) /100 ;

                        binding.interest.setText(String.valueOf(interest));
                        binding.total.setText(String.valueOf(amount + interest));
                    }
                } catch (NumberFormatException | JSONException e) {
                    showError(e.getMessage(),true);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return binding.getRoot();
    }
private  void  updateToolbar(){
    Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
    if(toolbar !=null){
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.left));
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
    }
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
    private void applyLoan(HashMap<String,String> payload){
        try {

            String auth="Bearer "+loginData.getToken();
            Call<ResponseBody> call2= apiService.applyLoan(payload,auth);
            call2.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        try {
                            String jsonresponse=response.body().string();
                            JSONObject jsonObject=new JSONObject(jsonresponse);
                            showError(jsonObject.get("message").toString(),true);
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
       getCategories();

       ArrayList<String> list1=new ArrayList<>(Arrays.asList("Business","Education","Personal use","Food","Entertainment","Other"));

        ArrayAdapter<String> adapter=new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1,list1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.purpose.setAdapter(adapter);
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
    private Boolean validateFields(){

        Boolean validated=true;
        if(binding.amount.getText().equals("")){
            binding.amount.requestFocus();
            showError("Amount required",true);
            validated=false;
        }
        return  validated;
    }

    private  void  getCategories() {

        Call<ResponseBody> call1 = apiService.getCategories();
        call1.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        String jsonresponse=response.body().string();
                        JSONObject jsonObject=new JSONObject(jsonresponse);
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        periods=new ArrayList<>();
                        periodDetails=new ArrayList<>();
                        periods.add("Select period");
                       for(int i=0; i<jsonArray.length(); i++){
                            JSONObject loanType=jsonArray.getJSONObject(i);
                             periodDetails.add(loanType);
                            periods.add(String.format("%s days",loanType.get("period").toString()));
                        }
                        ArrayAdapter<String> adapter=new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1,periods);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.period.setAdapter(adapter);
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
}