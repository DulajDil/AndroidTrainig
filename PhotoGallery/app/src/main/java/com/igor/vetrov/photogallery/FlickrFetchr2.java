package com.igor.vetrov.photogallery;


<<<<<<< HEAD


=======
>>>>>>> origin/master
public class FlickrFetchr2 {

    public static final String API_KEY = "d906a0a1c11d014f5e124352d8f926b7";
    public static final String API_URL = "https://api.flickr.com";


    public static PhotoGalleryClient fetchItems() {
        return RetrofitClient.getClient(API_URL).create(PhotoGalleryClient.class);
    }
}
