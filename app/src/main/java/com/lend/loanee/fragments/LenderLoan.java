package com.lend.loanee.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lend.loanee.R;
import com.lend.loanee.databinding.FragmentLenderLoanBinding;
import com.lend.loanee.helpers.RequestListener;

public class LenderLoan extends Fragment implements RequestListener {

    FragmentLenderLoanBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentLenderLoanBinding.inflate(inflater,container,false);


        return binding.getRoot();
    }

    @Override
    public void ProcessRequest(int position, String status) {

    }


}