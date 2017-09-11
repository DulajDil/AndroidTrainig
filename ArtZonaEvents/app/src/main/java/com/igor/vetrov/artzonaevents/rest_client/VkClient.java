package com.igor.vetrov.artzonaevents.rest_client;



public class VkClient {

    public static final String API_URL = "https://api.vk.com/method/";

    public static RetrofitClient getClient() {
        return ClientRetrofit.getClient(API_URL).create(RetrofitClient.class);
    }
}
