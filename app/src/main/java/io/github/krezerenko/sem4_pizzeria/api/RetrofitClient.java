package io.github.krezerenko.sem4_pizzeria.api;

import android.util.JsonReader;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Reader;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient
{
    public static final String GIST_URL = "https://api.github.com";
    private static volatile String BASE_URL = "https://192.168.1.69:8080";
    private static Retrofit retrofit = null;

    public static String getBaseUrl()
    {
        return BASE_URL;
    }

    public static void setBaseUrl(String url)
    {
        BASE_URL = url;
    }

    public static Retrofit getClient()
    {
        if (retrofit == null)
        {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(new HostSelectionInterceptor())
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        retrofit = retrofit.newBuilder().baseUrl(BASE_URL).build();
        return retrofit;
    }

    public static void tryGetGistTunnel(Runnable onSuccess)
    {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        GistService gistService = new Retrofit.Builder()
                .baseUrl(GIST_URL)
                .client(client)
                .build()
                .create(GistService.class);

        gistService.getTunnelGist(GistService.HEADER_ACCEPT, GistService.HEADER_VERSION).enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response)
            {
                try (ResponseBody body = response.body())
                {
                    if (response.isSuccessful() && body != null)
                    {
                        BASE_URL = extractTunnelFromGist(body.charStream());
                        Log.d("API", "onResponse: Got the tunnel: " + BASE_URL);
                        onSuccess.run();
                    }
                    else
                    {
                        Log.e("API", "onResponse: Failed to get the tunnel");
                    }
                }
                catch (Exception e)
                {
                    Log.e("API", "onResponse: Failed to get the tunnel", e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t)
            {
                Log.e("API", "onFailure: Failed to get the tunnel", t);
            }
        });
    }

    private static String extractTunnelFromGist(Reader bodyStream) throws IOException
    {
        JsonReader reader = new JsonReader(bodyStream);
        findKey("files", reader);
        findKey("link", reader);
        findKey("content", reader);
        return reader.nextString();
    }

    private static JsonReader findKey(String key, JsonReader reader) throws IOException
    {
        reader.beginObject();
        while (reader.hasNext())
        {
            if (key.equals(reader.nextName()))
            {
                return reader;
            }
            else reader.skipValue();
        }
        reader.endObject();
        return null;
    }
}
