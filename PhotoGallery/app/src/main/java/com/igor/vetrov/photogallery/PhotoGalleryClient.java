package com.igor.vetrov.photogallery;


import com.igor.vetrov.photogallery.model.GalleryItem;
import com.igor.vetrov.photogallery.model.ResponsePhotogallery;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface PhotoGalleryClient {

    @GET("/services/rest")
    Call<ResponsePhotogallery> fetchItems(@QueryMap Map<String, String> params);
}
