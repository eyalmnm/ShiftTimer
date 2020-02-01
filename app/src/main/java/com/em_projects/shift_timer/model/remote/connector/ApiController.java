package com.em_projects.shift_timer.model.remote.connector;

import android.util.Log;

import com.simpler.BuildConfig;
import com.simpler.application.SimplerApplication;
import com.simpler.utils.UrlUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


// Ref: https://stackoverflow.com/questions/47637456/android-okhttp-how-to-enable-gzip-for-post?rq=1
// Ref: https://stackoverflow.com/questions/50156589/how-to-write-retry-interceptor-for-data-stream-in-okhttp
// Ref: https://stackoverflow.com/a/50272497

public class ApiController {
    private static final String TAG = "ApiController";

    private static final long DISK_CACHE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String BASE_URL = UrlUtils.getBaseUrl() + "/"; // BuildConfig.FMC_SERVER_URL;

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass, boolean compress) {
        Log.d(TAG, "BASE_URL: " + BASE_URL);

        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder().cache(getChache()).build().newBuilder();
        httpClient.connectTimeout(30, TimeUnit.SECONDS);
        httpClient.readTimeout(30, TimeUnit.SECONDS);
        httpClient.addInterceptor(new RetriesInterceptor());
        // httpClient.retryOnConnectionFailure(true);
        // httpClient.callTimeout(30, TimeUnit.SECONDS);

        if (compress) {
            httpClient.addInterceptor(new GzipRequestInterceptor());
        }

        // OkHttpClient modifiedOkHttpClient = new OkHttpClient.Builder().addInterceptor(getHttpLoggingInterceptor()).build();

        OkHttpClient modifiedOkHttpClient = httpClient.addInterceptor(getHttpLoggingInterceptor()).build();
        builder.client(modifiedOkHttpClient);
        builder.baseUrl(BASE_URL);

        Retrofit retrofit = builder.build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass) {
        return ApiController.createService(serviceClass, false);
    }

    private static Cache getChache() {
        Cache cache = null;
        try {
            File cacheDir = new File(SimplerApplication.getCacheDirectory(), "http");
            cache = new Cache(cacheDir, DISK_CACHE_SIZE);
        } catch (Throwable tr) {
            Log.e(TAG, "getChache", tr);
        }
        return cache;
    }

    private static Interceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return httpLoggingInterceptor;
    }

    static class GzipRequestInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            Request originalRequest = chain.request();
            if (originalRequest.body() == null || originalRequest.header("Content-Encoding") != null) {
                return chain.proceed(originalRequest);
            }

            Request compressedRequest = originalRequest.newBuilder()
                    .header("Content-Encoding", "gzip")
                    .method(originalRequest.method(), gzip(originalRequest.body()))
                    .build();
            return chain.proceed(compressedRequest);
        }

        private RequestBody gzip(final RequestBody body) {
            return new RequestBody() {
                @Nullable
                @Override
                public MediaType contentType() {
                    return body.contentType();
                }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                    body.writeTo(gzipSink);
                    gzipSink.close();
                }
            };
        }
    }

    static class RetriesInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);

            int tryCount = 0;
            while (!response.isSuccessful() && tryCount < 3) {
                tryCount++;
                response = chain.proceed(request);
            }
            return response;
        }
    }

}
