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
    @FormUrlEncoded
    Call<RegisterResponse> register(
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone_number") String phone_number,
            @Field("country") String country,
            @Field("id_number") String id_number,
            @Field("dob") String dob,
            @Field("password") String password
    );

}
