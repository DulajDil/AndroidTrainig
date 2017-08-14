package com.igor.vetrov.photogallery;


import com.igor.vetrov.photogallery.model.ResponsePhotogallery;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface PhotoGalleryClient {

//    @FormUrlEncoded
    @GET("/services/rest")
    Call<ResponsePhotogallery> fetchItems(@QueryMap Map<String, String> params, @Query("page") String param1);
}
