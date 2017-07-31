package com.igor.vetrov.photogallery;


import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface PhotoGalleryClient {

    @GET()
    Call<List<GalleryItem>> fetchItems(@QueryMap Map<String, String> params);
}
