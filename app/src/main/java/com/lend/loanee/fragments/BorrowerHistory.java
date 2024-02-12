package com.lend.loanee.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lend.loanee.R;
import com.lend.loanee.databinding.FragmentBorrowerHistoryBinding;
import com.lend.loanee.helpers.History;
import com.lend.loanee.helpers.LenderHistoryAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class BorrowerHistory extends Fragment {

   FragmentBorrowerHistoryBinding binding;
   ArrayList<History> history;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        history=new ArrayList<>();
        binding=FragmentBorrowerHistoryBinding.inflate(inflater,container,false);

        history.addAll(Arrays.asList(new History("Loan received","Loan of Ksh 30,000 was issued")
                ,new History("Loan repaid","Loan of Ksh 33,000 was issued"),new History("Loan issued","Loan of Ksh 30,000 was issued"),
                new History("Loan received","Loan of Ksh 30,000 received")));

        binding.recyHist.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyHist.setHasFixedSize(true);
        binding.recyHist.setAdapter(new LenderHistoryAdapter(history,getContext()));
        return binding.getRoot();
    }
}