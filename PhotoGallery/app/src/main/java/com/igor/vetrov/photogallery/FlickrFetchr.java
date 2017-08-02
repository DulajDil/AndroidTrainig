package com.igor.vetrov.photogallery;


import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.igor.vetrov.photogallery.model.GalleryItem;
import com.igor.vetrov.photogallery.model.Photos;
import com.igor.vetrov.photogallery.model.ResponsePhotogallery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class FlickrFetchr {

    private static final String TAG = "FlickrFetchr";
    private static final String API_KEY = "d906a0a1c11d014f5e124352d8f926b7";
    private final String API_URL = "https://api.flickr.com/services/rest/";

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream input = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = input.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<GalleryItem> fetchItems() {
        List<GalleryItem> items = null;
        String url = Uri.parse(API_URL)
                .buildUpon()
                .appendQueryParameter("method", "flickr.photos.getRecent")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .appendQueryParameter("extras", "url_s")
                .build()
                .toString();
        try {
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            items = parseItems3(jsonBody);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        return items;
    }

    private void parseItems(List<GalleryItem> items, JSONObject jsonBody) throws JSONException {
        JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");

        for (int i = 0; i < photoJsonArray.length(); i++) {
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

            GalleryItem item = new GalleryItem();
            item.setId(photoJsonObject.getString("id"));
            item.setCaption(photoJsonObject.getString("title"));
            if (!photoJsonObject.has("url_s")) {
                continue;
            }
            item.setUrl(photoJsonObject.getString("url_s"));
            Log.i(TAG, "Received gallery item object: " + item);
            items.add(item);
        }
    }

    private List<GalleryItem> parseItems2(JSONObject jsonBody) throws JSONException {
        JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");

        Gson gson = new Gson();
        List<GalleryItem> items = gson.fromJson(photoJsonArray.toString(), new TypeToken<List<GalleryItem>>() {}.getType());
        Log.i(TAG, "Received gallery items list objects: " + items);
        return items;
    }

    private List<GalleryItem> parseItems3(JSONObject jsonBody) {
        Gson gson = new Gson();
        ResponsePhotogallery res = gson.fromJson(jsonBody.toString(), new TypeToken<ResponsePhotogallery>() {}.getType());

        Log.i(TAG, "Received response object: " + res);

        Photos photos = res.getPhotos();

        Log.i(TAG, "Received object photos: " + photos);

        List<GalleryItem> items = photos.getPhoto();

        Log.i(TAG, "Received gallery items list objects: " + items);

        return items;
    }
}
