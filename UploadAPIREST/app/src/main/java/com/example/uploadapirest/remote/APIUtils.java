package com.example.uploadapirest.remote;

public class APIUtils {

    private static final String API_URL = "http://10.107.144.29:3000/";

    public  static ImageInterface uploadImage(){

        return RetrofitClient.getClient(API_URL)
                .create(ImageInterface.class);
    }
}
