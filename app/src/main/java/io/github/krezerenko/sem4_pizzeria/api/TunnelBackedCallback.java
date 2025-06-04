package io.github.krezerenko.sem4_pizzeria.api;

import android.util.Log;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class TunnelBackedCallback<T> implements Callback<T>
{
    private Runnable onTunnelAcquired;
    public TunnelBackedCallback(Runnable onTunnelAcquired)
    {
        this.onTunnelAcquired = onTunnelAcquired;
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t)
    {
        Log.e("API", "onFailure: Failed to establish connection, attempting to get the tunnel", t);
        RetrofitClient.tryGetGistTunnel(onTunnelAcquired);
    }

    public static <U> void enqueue(Call<U> call, Callback<U> callback)
    {
        call.enqueue(new TunnelBackedCallback<U>(() -> call.clone().enqueue(callback))
        {
            @Override
            public void onResponse(@NonNull Call<U> call, @NonNull Response<U> response)
            {
                callback.onResponse(call, response);
            }
        });
    }
}
