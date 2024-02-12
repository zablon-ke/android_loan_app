package com.lend.loanee.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.google.android.material.datepicker.MaterialStyledDatePickerDialog;
import com.google.android.material.snackbar.Snackbar;
import com.lend.loanee.R;
import com.lend.loanee.databinding.ActivityRegisterBinding;
import com.lend.loanee.databinding.FragmentCreateProfileBinding;
import com.lend.loanee.helpers.ApiService;
import com.lend.loanee.helpers.LoginData;
import com.lend.loanee.pages.Home;
import com.lend.loanee.pages.Register;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Calendar;
import java.util.Objects;

public class CreateProfile extends Fragment implements MaterialStyledDatePickerDialog.OnDateSetListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    String mobile;
    Retrofit retrofit;
    ApiService apiService;
    Toolbar toolbar;

    LoginData data;
    String gender="";
    String role="";

    FragmentCreateProfileBinding binding;

    public CreateProfile(LoginData data,String role) {
        this.role=role;
        this.data=data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentCreateProfileBinding.inflate(inflater,container,false);

        retrofit= new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.url).toString())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService =retrofit.create(ApiService.class);
        login();

        binding.imgMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.imgMale.setImageDrawable(getResources().getDrawable(R.drawable.c24));
                binding.imgFemale.setImageDrawable(getResources().getDrawable(R.drawable.circle));
                gender="Male";
            }
        });
        binding.imgFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.imgFemale.setImageDrawable(getResources().getDrawable(R.drawable.c24));
                binding.imgMale.setImageDrawable(getResources().getDrawable(R.drawable.circle));
                gender="Female";
            }
        });

        LoginData data =((Register) requireActivity()).getLoginData();
        String phone=data.getMobile();
        binding.mobile.setText(data.getMobile());
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (valateFields()){
                    String firstName=binding.firstName.getText().toString();
                    String middleName=binding.middleName.getText().toString();
                    String surName=binding.surName.getText().toString();
                    String email=binding.email.getText().toString();
                    String idNumber=binding.idNumber.getText().toString();
                    String dob=binding.dateOfBirth.getText().toString();
                    Register(firstName,middleName,surName,email,idNumber,dob,phone,gender);
                }
            }
        });
        binding.dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showDatePicker();
            }

        });
        return binding.getRoot();
    }
    private  void  Register(String fname,String mname,String sname, String email, String id,String dob,String mobile,String gender){
        HashMap<String,String> payload= new HashMap<>();
        payload.put("firstName",fname);
        payload.put("middleName",mname);
        payload.put("lastName",sname);
        payload.put("Email",email);
        payload.put("gender",gender);
        payload.put("ID",id);
        payload.put("DOB",dob);
        payload.put("phone",mobile);
        payload.put("rol",role);

        Call<ResponseBody> call2=apiService.addProfile(payload);
        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    if(response.isSuccessful()){
                        try {
                            String jsonresponse=response.body().string();
                            JSONObject jsonObject=new JSONObject(jsonresponse);
                            showError(jsonObject.get("message").toString(),false);
                            clear();
                            if(Objects.equals(role,"Lender")){
                                Intent intent=new Intent(requireContext(), Home.class);
                                intent.putExtra("logins",data.getToken());
                                startActivity(intent);

                            }
                            else{
                                getParentFragmentManager().beginTransaction().replace(R.id.frame_main,new Emergency()).commit();
                                ((Register) requireActivity()).setToolbar("Emergency Contacts");
                            }

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
                    clear();

                }
                else{
                    try {
                        String jsonresponse=response.errorBody().string();

                        JSONObject json=new JSONObject(jsonresponse);

                        showSnackBar(json.toString(),true);
                    } catch (IOException e) {
                        showSnackBar(e.toString(),true);
                    } catch (JSONException e) {
                        showSnackBar(e.getMessage(),true);
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showSnackBar(t.getMessage(),true);
            }
        });
    }

    private void showDatePicker(){

        Calendar now = Calendar.getInstance();
        @SuppressLint("RestrictedApi") MaterialStyledDatePickerDialog dpd = new MaterialStyledDatePickerDialog(requireActivity(),
                0,this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }
    private Boolean valateFields(){
        boolean validated=true;
        if(binding.firstName.getText().toString().equals("")){
            showSnackBar("First Name Required !!!",true);
            binding.firstName.requestFocus();
            validated=false;
        }
        else if(binding.middleName.getText().toString().equals("")){
            showSnackBar(" MiddleName Required !!!",true);
            binding.middleName.requestFocus();
            validated=false;
        }
        else if(binding.surName.getText().toString().equals("")){
            showSnackBar("SurName Required !!!",true);
            binding.surName.requestFocus();
            validated=false;
        }
        else if(binding.email.getText().toString().equals("")){
            showSnackBar("Email Required !!!",true);
            binding.email.requestFocus();
            validated=false;
        }
        else if(binding.idNumber.getText().toString().equals("")){
            showSnackBar("ID Number required!!!",true);
            binding.idNumber.requestFocus();
            validated=false;
        }
        else if(binding.dateOfBirth.getText().toString().equals("")){
            showSnackBar("Date of birth required!!!",true);
            validated=false;
        }
        else if(Objects.equals(gender,"")){
            showSnackBar("Select gender",true);
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
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        binding.dateOfBirth.setText(i+"-"+i1+"-"+i2);
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
    private void clear()
    {
        binding.firstName.setText("");
        binding.surName.setText("");
        binding.middleName.setText("");
    }

    private void login(){
        try {

            HashMap<String,String> hash=new HashMap<>();
            hash.put("mobile",data.getMobile());
            hash.put("password",data.getPassword());
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
                            data.setToken(token);
                            showError(jsonObject.getJSONObject("data").toString(),false);
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
}