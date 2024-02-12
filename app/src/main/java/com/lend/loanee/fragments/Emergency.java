package com.lend.loanee.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.google.android.material.snackbar.Snackbar;
import com.lend.loanee.R;
import com.lend.loanee.databinding.FragmentEmergencyBinding;
import com.lend.loanee.helpers.ApiService;
import com.lend.loanee.helpers.LoginData;
import com.lend.loanee.pages.Home;
import com.lend.loanee.pages.Register;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Emergency extends Fragment {

    FragmentEmergencyBinding binding;
    Retrofit retrofit;
    ApiService apiService;

    LoginData data;
    public interface OnToolbarTextChangeListener {
        void updateToolbarText(String newText);
    }
    private OnToolbarTextChangeListener toolbarTextChangeListener;

    public void updateToolbarText(String newText) {
        if (toolbarTextChangeListener != null) {
            toolbarTextChangeListener.updateToolbarText(newText);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Ensure the activity implements the interface
        if (context instanceof OnToolbarTextChangeListener) {
            toolbarTextChangeListener = (OnToolbarTextChangeListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnToolb+                                                                                                                                                                                                                                                                                                                                                                                     arTextChangeListener");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentEmergencyBinding.inflate(inflater,container,false);

        initializer();
        ArrayList<String> list=new ArrayList<>();
        String[] list2={"Father","Mother","Sister","Brother","Uncle","Others"};
        list.addAll(Arrays.asList(list2));
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerRelation1.setAdapter(adapter);
        binding.spinnerRelation2.setAdapter(adapter);

        data =((Register) requireActivity()).getLoginData();
        String phone=data.getMobile();

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(valateFields()){
                    String name1=binding.fullname1.getText().toString();
                    String name2=binding.fullName2.getText().toString();
                    String relation1=binding.spinnerRelation1.getSelectedItem().toString();
                    String relation2=binding.spinnerRelation2.getSelectedItem().toString();
                    String mobile1=binding.mobile.getText().toString();
                    String mobile2=binding.mobile2.getText().toString();

                    HashMap<String,String> map1=new HashMap<>();
                    map1.put("fullname",name1);
                    map1.put("mobile",mobile1);
                    map1.put("relation",relation1);
                    HashMap<String,String> map2=new HashMap<>();
                    map2.put("fullname",name2);
                    map2.put("mobile",mobile2);
                    map2.put("relation",relation2);

                    ArrayList<HashMap<String, String>> payload = new ArrayList<>(Arrays.asList(map1, map2));

                    addEmergency(payload);
                }
                else{

                }

            }
        });
        return binding.getRoot();
    }
    private void addEmergency(ArrayList<HashMap<String,String>> payload){
        try {
            String auth="Bearer "+data.getToken();
            Call<ResponseBody> call2=apiService.addEmergency(payload,auth);
            call2.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if(response.isSuccessful()){
                            try {
                                String jsonresponse=response.body().string();
                                JSONObject jsonObject=new JSONObject(jsonresponse);
                                showError(jsonObject.get("message").toString(),false);
                                Intent intent=new Intent(requireContext(),Home.class);
                                intent.putExtra("logins",data);
                                startActivity(intent);
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
    private void showError(String text,Boolean isError){
        Snackbar snackbar=Snackbar.make(binding.spinnerRelation1,text,Snackbar.LENGTH_SHORT);
        if(isError){
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.red_2));
        }
        else{
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.teal));
        }
        snackbar.show();
    }
    private void initializer(){
        retrofit= new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.url).toString())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService =retrofit.create(ApiService.class);
        login();
    }
    private Boolean valateFields(){
        boolean validated=true;
        if(binding.fullname1.getText().toString().equals("")){
            showError("First Name Required !!!",true);
            binding.fullname1.requestFocus();
            validated=false;
        }
        else if(binding.mobile.getText().toString().equals("") || binding.mobile.getText().toString().length() < 9){
            showError("Provide valid phone number!!!",true);
            binding.mobile.requestFocus();
            validated=false;
        }
        else if(binding.fullName2.getText().toString().equals("")){
            showError("Name  Required !!!",true);
            binding.fullName2.requestFocus();
            validated=false;
        }
        else if(binding.mobile2.getText().toString().equals("") || binding.mobile2.getText().toString().length() < 9){
            showError("Provide valid phone number!!!",true);
            binding.mobile2.requestFocus();
            validated=false;
        }
        return validated;
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
//                            data.setToken(token);
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