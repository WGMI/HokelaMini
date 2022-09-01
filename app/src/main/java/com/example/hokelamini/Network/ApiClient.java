package com.example.hokelamini.Network;

import com.example.hokelamini.Models.Responses.RegisterResponse;
import com.example.hokelamini.Models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiClient {

    @POST("register")
    Call<RegisterResponse> register(@Body User user);

}
