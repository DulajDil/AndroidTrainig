package com.igor.vetrov.photogallery.retrofit;


import com.igor.vetrov.photogallery.model.ResponsePhotogallery;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface PhotoGalleryClient {

    @GET("/services/rest")
    Call<ResponsePhotogallery> fetchItems(@QueryMap Map<String, String> params, @Query("page") String param1);

    @GET
    Call<ResponseBody> fetchImage(@Url String url);
}
