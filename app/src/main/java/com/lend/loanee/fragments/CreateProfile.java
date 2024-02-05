package com.lend.loanee.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
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
public class CreateProfile extends Fragment implements MaterialStyledDatePickerDialog.OnDateSetListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    Retrofit retrofit;
    ApiService apiService;
    Toolbar toolbar;

    String gender="";

    FragmentCreateProfileBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentCreateProfileBinding.inflate(inflater,container,false);


        retrofit= new Retrofit.Builder()
                .baseUrl("https://mawingu.cbaloop.com/cba/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService =retrofit.create(ApiService.class);

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
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager manager=getParentFragmentManager();
                manager.beginTransaction().replace(R.id.frame_main,new Emergency()).commit();

                ((Emergency.OnToolbarTextChangeListener) getActivity()).updateToolbarText("Emergency Contacts");

//                if (valateFields()){
//                    String name=binding.firstName.getText().toString();
//                    String email=binding.email.getText().toString();
//                    String password =binding.mobile.getText().toString();
//                    String mobile=binding.idNumber.getText().toString();
//                    Register(name,email,mobile,password);
//                }
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

    private  void  Register(String name, String email, String mobile,String password){

        HashMap<String,String> data= new HashMap<>();
        data.put("fullName",name);
        data.put("email",email);
        data.put("msisdn",mobile);
        data.put("credentials",password);
        Call<ResponseBody> call2=apiService.registerUser(data);

        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        String jsonresponse=response.body().string();

//                        JSONObject json=new JSONObject(jsonresponse);

                        Log.d("LOG ME",response.toString());
                        showSnackBar(response.toString(),false);
//                        Intent intent=new Intent(Register.this, Register.class);
//                        startActivity(intent);

                    } catch (IOException e) {
                        showSnackBar(e.getMessage(),true);
                    } catch (Exception e) {
                        showSnackBar(e.getMessage(),true);
                    }

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
            showSnackBar("Name Required !!!",true);
            binding.firstName.requestFocus();
            validated=false;
        }
        else if(binding.email.getText().toString().equals("")){
            showSnackBar("Email Required !!!",true);
            binding.email.requestFocus();
            validated=false;
        }
        else if(binding.idNumber.getText().toString().equals("")){
            showSnackBar("Mobile Number required!!!",true);
            binding.idNumber.requestFocus();
            validated=false;
        }
        else if(binding.idNumber.getText().toString().length() < 9 ){
            showSnackBar("Mobile number too short ",true);
            binding.idNumber.requestFocus();
            validated=false;
        }
        else if(binding.mobile.getText().toString().equals("")){
            showSnackBar("Password Required !!!",true);
            binding.mobile.requestFocus();
            validated=false;
        }

        else if(!binding.mobile.getText().toString().equals(binding.idNumber.getText().toString())){
            showSnackBar("Password do not match !!!",true);
            binding.email.requestFocus();
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

    }
}