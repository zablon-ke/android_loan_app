package com.lend.loanee.helpers;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/user/login")
    Call<ResponseBody> loginUser(@Body HashMap<String, String> payload);
    @POST("/user/sign_up")
    Call<ResponseBody> signUpUser( @Body HashMap<String,String> payload);
    @POST("/user/add")
    Call<ResponseBody> addProfile(@Body HashMap<String,String> payload);
    @POST("/user/emergency/add")

    Call<ResponseBody> addEmergency( @Body ArrayList<HashMap<String,String>> payload, @Header("Authorization") String auth);
    @GET("/user")
    Call<ResponseBody> getUser(@Header("Authorization") String auth);

  @GET("/app/duration")
    Call<ResponseBody> getDuration();

    @GET("/app/loan/type")
    Call<ResponseBody> getCategories();

    @POST("/app/loan")
    Call<ResponseBody> applyLoan(@Body HashMap<String,String> payload,@Header("Authorization") String auth);

    @GET("/app/vi/loan")
    Call<ResponseBody> fetchLoan(@Header("Authorization") String auth);

    @GET("/user/lender")
    Call<ResponseBody> getLender(@Header("Authorization") String auth);
}
