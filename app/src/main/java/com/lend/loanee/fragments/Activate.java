package com.lend.loanee.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lend.loanee.R;
import com.lend.loanee.databinding.FragmentActivateBinding;
import com.lend.loanee.pages.Register;


public class Activate extends Fragment {

    FragmentActivateBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentActivateBinding.inflate(inflater,container,false);
        binding.btnActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.frame_main,new CreateProfile()).commit();
                ((Register) requireActivity()).setToolbar("Create Profile");
            }
        });
        return binding.getRoot();
    }
}