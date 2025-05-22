package io.github.krezerenko.sem4_pizzeria;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        UserRegistrationDto user = new UserRegistrationDto("user", "password");

        Call<ResponseBody> call = apiService.registerUser(user);
        call.enqueue(new Callback<>()
        {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response)
            {
                if (response.isSuccessful())
                {
                    Log.d("API", "Registration successful!");
                }
                else
                {
                    Log.e("API", "Registration failed: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t)
            {
                Log.e("API", "Error: " + t.getMessage());
            }
        });
    }
}