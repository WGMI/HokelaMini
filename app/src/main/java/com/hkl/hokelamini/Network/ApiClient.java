package com.hkl.hokelamini.Network;

import com.hkl.hokelamini.Models.Answer;
import com.hkl.hokelamini.Models.Question;
import com.hkl.hokelamini.Models.Responses.AuthResponse;
import com.hkl.hokelamini.Models.Responses.Project;
import com.hkl.hokelamini.Models.Responses.StandardResponse;
import com.hkl.hokelamini.Models.Survey;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiClient {

    @POST("register")
    @FormUrlEncoded
    Call<AuthResponse> register(
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone_number") String phone_number,
            @Field("country") String country,
            @Field("id_number") String id_number,
            @Field("dob") String dob,
            @Field("password") String password
    );

    @POST("login")
    @FormUrlEncoded
    Call<AuthResponse> login(@Field("email") String email,@Field("password") String password);

    @POST("logout")
    Call<StandardResponse> logout(@Header("Authorization") String token);

    //@POST("projects/join")
    //Call<SurveyResponse> joinSurvey(@Header("token") String token, @Field("survey_id") String code);

    @Headers({"Accept: application/json"})
    @GET("projects")
    Call<List<Project>> getProjects(@Header("Authorization") String token);

    @Headers({"Accept: application/json"})
    @POST("projects/joinbycode")
    @FormUrlEncoded
    Call<StandardResponse> joinProject(@Header("Authorization") String token,@Field("code") String code);

    @Headers({"Accept: application/json"})
    @GET("projects/surveys/{id}")
    Call<List<Survey>> getSurveys(@Header("Authorization") String token, @Path("id") long id);

    @Headers({"Accept: application/json"})
    @GET("surveys/questions/{id}")
    Call<List<Question>> getQuestions(@Header("Authorization") String token, @Path("id") long id);

    @Headers({"Accept: application/json"})
    @POST("answers/storemultiple")
    Call<StandardResponse> storeAnswers(@Header("Authorization") String token, @Body List<Answer> answers);

}

