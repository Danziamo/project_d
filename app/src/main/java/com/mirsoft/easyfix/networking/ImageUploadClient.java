package com.mirsoft.easyfix.networking;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;

/**
 * Created by mbt on 7/30/15.
 */
public class ImageUploadClient {

    public static final String UPLOAD_URL = "http://192.168.0.109/ut/u.php";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    public static Response upload(byte[] bytes) throws Exception{
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"image\""),
                        RequestBody.create(MEDIA_TYPE_PNG, bytes))
                .build();

        Request request = new Request.Builder()
//                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url(UPLOAD_URL)
                .post(requestBody)
                .build();

        return client.newCall(request).execute();
    }

    public static Response upload(File file) throws Exception{
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"image\""),
                        RequestBody.create(MEDIA_TYPE_PNG, file))
                .build();

        Request request = new Request.Builder()
//                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url(UPLOAD_URL)
                .post(requestBody)
                .build();

        return client.newCall(request).execute();
    }


}
