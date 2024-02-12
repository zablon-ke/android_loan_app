package com.lend.loanee.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lend.loanee.R;
import com.lend.loanee.databinding.FragmentLenderHistoryBinding;
import com.lend.loanee.helpers.History;
import com.lend.loanee.helpers.LenderHistoryAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class LenderHistory extends Fragment {
    public LenderHistory() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters

    FragmentLenderHistoryBinding binding;
    ArrayList<History> history;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentLenderHistoryBinding.inflate(inflater,container,false);

        history=new ArrayList<>();

        history.addAll(Arrays.asList(new History[]{new History("Loan issued","Loan of Ksh 30,000 was issued")
        ,new History("Loan repaid","Loan of Ksh 33,000 was issued"),new History("Loan issued","Loan of Ksh 30,000 was issued"),
                new History("Loan issued","Loan of Ksh 30,000 was issued")}));


        binding.recyHist.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyHist.setHasFixedSize(true);
        binding.recyHist.setAdapter(new LenderHistoryAdapter(history,getContext()));
        return binding.getRoot();


    }
}