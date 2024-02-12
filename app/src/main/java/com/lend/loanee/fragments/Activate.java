package com.lend.loanee.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lend.loanee.R;
import com.lend.loanee.databinding.FragmentActivateBinding;
import com.lend.loanee.helpers.LoginData;
import com.lend.loanee.pages.Register;


public class Activate extends Fragment {

    FragmentActivateBinding binding;
    LoginData data;
    String role="";

    public Activate(LoginData data, String role) {
        this.data = data;
        this.role=role;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentActivateBinding.inflate(inflater,container,false);
        data=((Register) requireActivity()).getLoginData();
        binding.mobile1.setText(data.getMobile());
        binding.btnActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activate();
                getParentFragmentManager().beginTransaction().replace(R.id.frame_main,new CreateProfile(data,role)).commit();
                ((Register) requireActivity()).setToolbar("Create Profile");
            }
        });
        return binding.getRoot();
    }

    private void activate(){
        if(binding.code.getText().toString().equals("")){

        }
    }
    private LoginData getLoginData(){
        return data;
    }
}