package com.lend.loanee.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lend.loanee.R;
import com.lend.loanee.databinding.FragmentEmergencyBinding;


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

        return inflater.inflate(R.layout.fragment_emergency, container, false);
    }
}