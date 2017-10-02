package com.igor.vetrov.photogallery.retrofit;


import android.util.Log;

import com.igor.vetrov.photogallery.model.GalleryItem;
import com.igor.vetrov.photogallery.model.Photos;
import com.igor.vetrov.photogallery.model.ResponsePhotogallery;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlickrFetchr2 {

    private static final String TAG = "FlickrFetchr2";
    public static final String API_KEY = "d906a0a1c11d014f5e124352d8f926b7";
    public static final String API_URL = "https://api.flickr.com";
    @Getter
    public static PhotoGalleryClient sClient;

    public static List<GalleryItem> sItems;

    public static void loadPhotos(PhotoGalleryFragment2.PhotoAdapter mAdapter, Integer currentPage) {
        Map<String, String> params = new HashMap<>();
        params.put("method", "flickr.photos.getRecent");
        params.put("api_key", FlickrFetchr2.API_KEY);
        params.put("format", "json");
        params.put("nojsoncallback", "1");
        params.put("extras", "url_s");
        params.put("page", String.valueOf(currentPage));

        Call<ResponsePhotogallery> call = sClient.fetchItems(params, String.valueOf(currentPage));
        call.enqueue(new Callback<ResponsePhotogallery>() {
            @Override
            public void onResponse(Call<ResponsePhotogallery> call, Response<ResponsePhotogallery> response) {

                Log.i(TAG, "URL to request: " + call.request().url());

                ResponsePhotogallery body = response.body();
                Log.i(TAG, "Received response object: " + body);

                Photos photos = body.getPhotos();
                Log.i(TAG, "Received object photos: " + photos);

                if (currentPage > 1) {
                    List<GalleryItem> items = photos.getPhoto();
                    for (int i = 0; i < items.size(); i++) {
                        sItems.add(items.get(i));
                    }
                } else {
                    sItems = photos.getPhoto();
                }
                Log.i(TAG, "Received gallery items list objects: " + sItems);

                Log.i(TAG, "Getting response: " + sItems);
                mAdapter.updatePhotoGallery(sItems);
                PhotoGalleryFragment2.loading.setRunning(true);
                Log.i(TAG, String.format("Load %s page", currentPage));
            }

            @Override
            public void onFailure(Call<ResponsePhotogallery> call, Throwable t) {
                Log.e(TAG, "Failed getting photogallery", t);
            }
        });
    }

    public static byte[] getUrlBytes(String urlSpec) throws IOException {
        Call<ResponseBody> call = sClient.fetchImage(urlSpec);
        Response<ResponseBody> response = call.execute();
        if (response.isSuccessful()) {
            ResponseBody body = response.body();
            InputStream input = body.byteStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = input.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } else {
            throw new IOException(response.code() + ": with " + urlSpec);
        }
    }


    public static void initClient() {
        sClient = RetrofitClient.getClient(API_URL).create(PhotoGalleryClient.class);
    }
}
