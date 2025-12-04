package com.bizzfilling.app.api;

import android.util.Log;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    
    // Primary: Local IP
    private static final String LOCAL_URL = "http://192.168.1.2:8081/";
    // Fallback: Public IP
    private static final String PUBLIC_URL = "http://115.97.59.230:8081/";
    
    private static Retrofit retrofit = null;
    private static String currentBaseUrl = LOCAL_URL; // Start with Local

    public static Retrofit getClient(android.content.Context context) {
        if (retrofit == null) {
            if (context == null) throw new IllegalArgumentException("Context cannot be null");
            android.content.Context appContext = context.getApplicationContext();
            
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Custom Interceptor for Failover
            Interceptor failoverInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    
                    // 1. Try with current Base URL (likely Local)
                    try {
                        return chain.proceed(request);
                    } catch (IOException e) {
                        // 2. If failed, and we are on Local, switch to Public
                        if (currentBaseUrl.equals(LOCAL_URL)) {
                            Log.w("ApiClient", "Local IP failed, switching to Public IP...");
                            currentBaseUrl = PUBLIC_URL;
                            
                            // Rebuild request with new URL
                            okhttp3.HttpUrl publicUrl = okhttp3.HttpUrl.parse(PUBLIC_URL);
                            if (publicUrl != null) {
                                okhttp3.HttpUrl newUrl = request.url().newBuilder()
                                    .host(publicUrl.host())
                                    .port(publicUrl.port())
                                    .scheme(publicUrl.scheme())
                                    .build();
                                
                                Request newRequest = request.newBuilder().url(newUrl).build();
                                return chain.proceed(newRequest);
                            }
                        }
                        throw e; // If already Public or other error, throw
                    }
                }
            };

            OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS) // Short timeout for faster failover
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .addInterceptor(failoverInterceptor) // Add failover logic
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    com.bizzfilling.app.utils.SessionManager sessionManager = new com.bizzfilling.app.utils.SessionManager(appContext);
                    String token = sessionManager.getToken();
                    
                    Request.Builder requestBuilder = original.newBuilder();
                    if (token != null) {
                        requestBuilder.header("Authorization", "Bearer " + token);
                    }
                    return chain.proceed(requestBuilder.build());
                })
                .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(currentBaseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
