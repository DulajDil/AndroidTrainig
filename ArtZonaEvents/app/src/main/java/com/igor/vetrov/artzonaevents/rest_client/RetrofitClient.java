package com.igor.vetrov.artzonaevents.rest_client;


import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface RetrofitClient {

    // https://api.vk.com/method/METHOD_NAME?PARAMETERS&access_token=ACCESS_TOKEN&v=V
    @GET("secure.checkToken")
    Call<ResponseBody> checkToken(@QueryMap Map<String, String> params);

}
