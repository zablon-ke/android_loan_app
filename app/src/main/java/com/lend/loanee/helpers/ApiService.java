package com.lend.loanee.helpers;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {


    @POST("api/v1/access/login")
    Call<ResponseBody> loginUser(@Body HashMap<String, String> payload);

    @POST("api/v1/user/register")
    Call<ResponseBody> registerUser( @Body HashMap<String,String> payload);

    @GET("")
    Call<ResponseBody> getServices();

    @POST("")
    Call<ResponseBody> subscribeService(@Body HashMap<String,String> data);

}
