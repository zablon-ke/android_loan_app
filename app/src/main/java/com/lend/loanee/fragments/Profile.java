package com.lend.loanee.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.lend.loanee.R;
import com.lend.loanee.databinding.FragmentProfileBinding;
import com.lend.loanee.helpers.ApiService;
import com.lend.loanee.helpers.LoginData;
import com.lend.loanee.pages.Home;
import com.lend.loanee.pages.Login;
import com.lend.loanee.pages.UserProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Profile extends Fragment {


    FragmentProfileBinding binding;
    LoginData loginData;
    Retrofit retrofit;
    ApiService apiService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentProfileBinding.inflate(inflater,container,false);

        loginData=((Home) requireActivity()).getLoginData();
        initializer();

        binding.logout.setOnClickListener(view ->{
            startActivity(new Intent(requireContext(), Login.class));
        });

        return binding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        if(toolbar !=null){
            toolbar.setBackgroundColor(getResources().getColor(R.color.white));
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.left));
        }
    }

    private void initializer(){
        retrofit= new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.url).toString())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService =retrofit.create(ApiService.class);
         binding.profile.setOnClickListener(view ->{
             Intent intent=new Intent(requireContext(), UserProfile.class);
             intent.putExtra("token",loginData.getToken());
             intent.putExtra("profile",loginData.getDetails().toString());
             startActivity(intent);
         });
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
                            String name=loginData.getDetails().getJSONObject("profile").get("firstName").toString() +" "+loginData.getDetails().getJSONObject("profile").get("lastName").toString();
                            binding.userName.setText(name.substring(0,1).toUpperCase()+name.substring(1,name.length()));

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
}