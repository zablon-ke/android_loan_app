package com.lend.loanee.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.lend.loanee.R;
import com.lend.loanee.databinding.FragmentEmergencyBinding;
import com.lend.loanee.pages.Home;

import java.util.ArrayList;
import java.util.Arrays;


public class Emergency extends Fragment {

    FragmentEmergencyBinding binding;


    public interface OnToolbarTextChangeListener {
        void updateToolbarText(String newText);
    }

    private OnToolbarTextChangeListener toolbarTextChangeListener;

    public void updateToolbarText(String newText) {
        if (toolbarTextChangeListener != null) {
            toolbarTextChangeListener.updateToolbarText(newText);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Ensure the activity implements the interface
        if (context instanceof OnToolbarTextChangeListener) {
            toolbarTextChangeListener = (OnToolbarTextChangeListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnToolbarTextChangeListener");
        }
    }

 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentEmergencyBinding.inflate(inflater,container,false);

        ArrayList<String> list=new ArrayList<>();
        String[] list2={"Father","Mother","Sister","Brother","Uncle","Others"};
        list.addAll(Arrays.asList(list2));
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerRelation1.setAdapter(adapter);
        binding.spinnerRelation2.setAdapter(adapter);

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),Home.class);
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }
}