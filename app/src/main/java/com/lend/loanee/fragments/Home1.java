package com.lend.loanee.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lend.loanee.R;
import com.lend.loanee.databinding.FragmentHome1Binding;
import com.lend.loanee.pages.Home;


public class Home1 extends Fragment {


    FragmentHome1Binding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentHome1Binding.inflate(inflater,container,false);
        ((Home) requireActivity()).setToolbar("Friend Lend");
        return binding.getRoot();
    }
}