package com.igor.vetrov.photogallery;


import retrofit2.Retrofit;

public class RetrofitClient {

    public static Retrofit sRetrofit = null;

    public static Retrofit getClient(String baseUrl) {
        if (sRetrofit == null) {
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .build();
        }
        return sRetrofit;
    }
}
