package com.lend.loanee.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lend.loanee.R;
import com.lend.loanee.databinding.FragmentLoanApply2Binding;

public class LoanApply2 extends Fragment {



    FragmentLoanApply2Binding binding;

    public LoanApply2() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentLoanApply2Binding.inflate(inflater,container,false);

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
}