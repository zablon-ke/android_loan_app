package com.lend.loanee.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;


import com.lend.loanee.R;
import com.lend.loanee.databinding.FragmentChatsBinding;
import com.lend.loanee.helpers.Chat;
import com.lend.loanee.helpers.ChatAdapter;

import java.util.ArrayList;
import java.util.Arrays;


public class Chats extends Fragment {
    FragmentChatsBinding binding;
    ArrayList<Chat> chats;
    public Chats() {

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentChatsBinding.inflate(inflater,container,false);
        chats=new ArrayList<>();
        binding.chatReyc.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.chatReyc.setHasFixedSize(true);
        chats.addAll(Arrays.asList(new Chat("Hello James","Elon musk","16:24","https://futureoflife.org/wp-content/uploads/2020/08/elon_musk_royal_society-1024x1024.jpg","0"),
                new Chat("Hello Jonnes","Kim kardashian","16:24","https://img.buzzfeed.com/buzzfeed-static/complex/images/bpighauq43c0yvyu5ptw/kim-kardashian-teases-met-look.jpg?downsize=920:*&output-format=auto&output-quality=auto","0")));
        binding.chatReyc.setAdapter(new ChatAdapter(chats,requireContext()));
        return binding.getRoot();
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.chats,menu);
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onResume() {
        super.onResume();

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        if(toolbar !=null){
            toolbar.setBackgroundColor(requireContext().getColor(R.color.light_gray));
            toolbar.setNavigationIcon(null);
            toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        }
    }
}