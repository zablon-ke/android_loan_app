package com.lend.loanee.helpers;

import java.util.HashMap;

public interface RequestListener {

    public void ProcessRequest(int position, HashMap<String,String> data);
}
