package com.example.hokelamini.Network;

import com.example.hokelamini.Checkin;
import com.example.hokelamini.Models.Answer;
import com.example.hokelamini.Models.Campaign;
import com.example.hokelamini.Models.Question;
import com.example.hokelamini.Models.Responses.AuthResponse;
import com.example.hokelamini.Models.Project;
import com.example.hokelamini.Models.Responses.StandardResponse;
import com.example.hokelamini.Models.Survey;

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
    @POST("campaigns/joinbycode")
    @FormUrlEncoded
    Call<StandardResponse> joinCampaign(@Header("Authorization") String token,@Field("code") String code);

    @Headers({"Accept: application/json"})
    @GET("checkin/status/{id}")
    Call<StandardResponse> checkSessionStatus(@Header("Authorization") String token,@Path("id") long id);

    @Headers({"Accept: application/json"})
    @GET("projects/surveys/{id}")
    Call<List<Survey>> getSurveys(@Header("Authorization") String token, @Path("id") long id);

    @Headers({"Accept: application/json"})
    @GET("campaigns/reports/{id}")
    Call<List<Survey>> getCampaignSurveys(@Header("Authorization") String token, @Path("id") long id);

    @Headers({"Accept: application/json"})
    @GET("campaigns")
    Call<List<Campaign>> getCampaigns(@Header("Authorization") String token);

    @Headers({"Accept: application/json"})
    @GET("surveys/questions/{id}")
    Call<List<Question>> getQuestions(@Header("Authorization") String token, @Path("id") long id);

    @Headers({"Accept: application/json"})
    @POST("checkin")
    Call<StandardResponse> sendSession(@Header("Authorization") String token, @Body Checkin checkin);

    @Headers({"Accept: application/json"})
    @POST("checkout")
    @FormUrlEncoded
    Call<StandardResponse> endSession(@Header("Authorization") String token, @Field("campaignid") long id);

    @Headers({"Accept: application/json"})
    @POST("answers/storemultiple")
    Call<StandardResponse> storeAnswers(@Header("Authorization") String token, @Body List<Answer> answers);

}

