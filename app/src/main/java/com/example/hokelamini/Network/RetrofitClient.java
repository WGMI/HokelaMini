package com.example.hokelamini.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    static String BASE_URL = "http://192.168.43.53/";
    static String SUFFIX = "hokela_mini/public/api/";
    static String URL = BASE_URL + SUFFIX;

    public static Retrofit retrofit;

    public static Retrofit getRetrofitInstance(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
