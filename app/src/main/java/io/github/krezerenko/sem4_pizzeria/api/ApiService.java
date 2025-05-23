package io.github.krezerenko.sem4_pizzeria.api;

import java.util.List;

import io.github.krezerenko.sem4_pizzeria.MenuItem;
import io.github.krezerenko.sem4_pizzeria.UserRequestDto;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService
{
    @POST("api/auth/register")
    Call<AuthResponse> registerUser(@Body UserRegistrationDto user);

    @POST("api/auth/login")
    Call<AuthResponse> loginUser(@Body LoginRequest request);

    @POST("api/auth/refresh")
    Call<AuthResponse> refreshToken(@Body RefreshTokenRequest request);

    @GET("api/users/me")
    Call<UserResponseDto> getUserByToken(@Header("Authorization") String authHeader);

    @POST("api/users/me")
    Call<UserRequestDto> saveUserByToken(@Header("Authorization") String authHeader, @Body UserRequestDto user);

    @GET("api/public/products")
    Call<List<MenuItem>> getProducts();
}
