package io.github.krezerenko.sem4_pizzeria;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService
{
    @POST("api/auth/register")
    Call<ResponseBody> registerUser(@Body UserRegistrationDto user);

    @POST("api/auth/login")
    Call<AuthResponse> loginUser(@Body LoginRequest request);

    @POST("api/auth/refresh")
    Call<AuthResponse> refreshToken(@Body RefreshTokenRequest request);

    @GET("api/public/products")
    Call<List<MenuItem>> getProducts();
}
