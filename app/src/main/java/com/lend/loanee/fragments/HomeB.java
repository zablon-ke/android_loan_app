package com.lend.loanee.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.lend.loanee.R;
import com.lend.loanee.databinding.FragmentHome1Binding;
import com.lend.loanee.databinding.FragmentHomeBBinding;
import com.lend.loanee.helpers.ApiService;
import com.lend.loanee.helpers.LoginData;
import com.lend.loanee.pages.Home;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeB extends Fragment {

    FragmentHomeBBinding binding;
    LoginData loginData;
    Retrofit retrofit;
    ApiService apiService;

    public HomeB(LoginData loginData) {
        this.loginData =loginData;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentHomeBBinding.inflate(inflater,container,false);
        ((Home) requireActivity()).setToolbar("");

        initializer();

        getChildFragmentManager().beginTransaction().replace(R.id.frm_main1,new BorrowerHistory()).commit();


        try {
            if(loginData.getDetails() !=null){
            if(loginData.getDetails().getJSONArray("loan").length() >0){
                if(loginData.getDetails().getJSONArray("loan").getJSONObject(0).get("State").equals("Completed")){
                    binding.btnApply.setVisibility(View.GONE);
                }
            }
            }
            binding.btnApply.setOnClickListener(view ->{
                getParentFragmentManager().beginTransaction().replace(R.id.main_frame,new LoanApply1()).commit();
            });
            binding.btnPayback.setOnClickListener(view ->{
                getParentFragmentManager().beginTransaction().replace(R.id.main_frame,new Payment()).commit();
                ((Home) requireActivity()).setToolbar("Pay Loan");

            });

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

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
        getDuration();
        getUser();
    }

    private  void getDuration(){
        try {
            Call<ResponseBody> call1 = apiService.getDuration();
            call1.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String jsonresponse = response.body().string();
                            JSONObject jsonObject = new JSONObject(jsonresponse);
                            JSONObject data=jsonObject.getJSONObject("data");
                            binding.txtDuration.setText(String.format("Repayment up to %s days",data.get("period").toString()));
                            binding.txtLimit.setText(data.get("loan_limit").toString());
                        } catch (IOException | JSONException e) {
                            showError(e.getMessage(), true);
                        }
                    } else {
                        try {
                            String jsonresponse = null;
                            jsonresponse = response.errorBody().string();
                            if (jsonresponse != null) {
                                JSONObject error = new JSONObject(jsonresponse);
                                showError(error.get("error").toString(), false);
                            }
                        } catch (IOException | JSONException e) {
                            showError(e.getMessage(), true);
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    showError(t.getMessage(), false);
                }
            });
        }
        catch (Exception e) {
            showError(e.getMessage(), false);
        }
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
                            if(loginData.getDetails().getJSONArray("loan").getJSONObject(0).get("State").equals("Completed")){
                                binding.btnApply.setVisibility(View.GONE);
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