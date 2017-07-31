package com.igor.vetrov.photogallery;


import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlickrFetchr2 {

    private static final String TAG = "FlickrFetchr";
    private static final String API_KEY = "d906a0a1c11d014f5e124352d8f926b7";
    private final String API_URL = "https://api.flickr.com/services/rest/";

    public PhotoGalleryClient getClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build()).build();

        return retrofit.create(PhotoGalleryClient.class);
    }

    public Call<List<GalleryItem>> fetchItems() {
        Map<String, String> params = new HashMap<>();
        params.put("method", "flickr.photos.getRecent");
        params.put("api_key", API_KEY);
        params.put("format", "json");
        params.put("nojsoncallback", "1");
        params.put("extras", "url_s");

        return getClient().fetchItems(params);
    }
}
