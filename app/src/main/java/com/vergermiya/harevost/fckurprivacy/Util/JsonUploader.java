package com.vergermiya.harevost.fckurprivacy.Util;

import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by harevost on 18-7-17.
 */

public class JsonUploader {
    private static final MediaType MEDIA_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String BASE_URL = "http://10.8.205.87:8080";

    public static void postJson(String jsonStr) {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MEDIA_JSON, jsonStr);
        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(requestBody)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.d("NetWork", response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
