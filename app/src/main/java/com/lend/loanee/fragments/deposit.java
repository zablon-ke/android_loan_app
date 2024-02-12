package com.lend.loanee.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lend.loanee.R;
import com.lend.loanee.databinding.FragmentDepositBinding;

public class deposit extends Fragment {

    FragmentDepositBinding binding;

    public deposit() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentDepositBinding.inflate(inflater,container,false);
        return  binding.getRoot();
    }
}