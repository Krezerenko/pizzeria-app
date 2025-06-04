package io.github.krezerenko.sem4_pizzeria.api;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public final class HostSelectionInterceptor implements Interceptor
{
    @NonNull
    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException
    {
        Request request = chain.request();
        if (RetrofitClient.getBaseUrl() != null)
        {
            String[] parts = RetrofitClient.getBaseUrl().replace("//", "").split(":");
            String scheme = parts[0];
            String host = parts[1];
            HttpUrl.Builder builder = request.url().newBuilder()
                    .scheme(scheme)
                    .host(host)
                    .port(parts.length >= 3 ? Integer.parseInt(parts[2]) : 443);
//            if () builder.port(Integer.parseInt(parts[2]));
            HttpUrl newUrl = builder.build();

            request = request.newBuilder()
                    .url(newUrl)
                    .build();
        }
        return chain.proceed(request);
    }
}