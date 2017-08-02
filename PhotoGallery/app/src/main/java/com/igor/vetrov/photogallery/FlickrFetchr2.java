package com.igor.vetrov.photogallery;


import com.igor.vetrov.photogallery.model.GalleryItem;
import com.igor.vetrov.photogallery.model.ResponsePhotogallery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlickrFetchr2 {

    public static final String API_KEY = "d906a0a1c11d014f5e124352d8f926b7";
    public static final String API_URL = "https://api.flickr.com";


    public static PhotoGalleryClient fetchItems() {
        return RetrofitClient.getClient(API_URL).create(PhotoGalleryClient.class);
    }
}
