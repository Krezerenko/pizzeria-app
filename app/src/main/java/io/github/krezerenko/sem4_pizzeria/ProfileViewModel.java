package io.github.krezerenko.sem4_pizzeria;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import io.github.krezerenko.sem4_pizzeria.api.ApiService;
import io.github.krezerenko.sem4_pizzeria.api.AuthResponse;
import io.github.krezerenko.sem4_pizzeria.api.RefreshTokenRequest;
import io.github.krezerenko.sem4_pizzeria.api.SecureStorageHelper;
import io.github.krezerenko.sem4_pizzeria.api.TokenPair;
import io.github.krezerenko.sem4_pizzeria.api.UserResponseDto;
import retrofit2.Call;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel
{
    private final MutableLiveData<AuthState> authState = new MutableLiveData<AuthState>(AuthState.REFRESHING);
    private final SecureStorageHelper storageHelper;
    private final ApiService api;

    private final MutableLiveData<String> username = new MutableLiveData<>();
    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<String> phoneNumber = new MutableLiveData<>();

    public ProfileViewModel(SecureStorageHelper storageHelper, ApiService api)
    {
        this.storageHelper = storageHelper;
        this.api = api;
        checkInitialAuthState();
    }

    private void checkInitialAuthState()
    {
        new Thread(() ->
        {
            try
            {
                authState.postValue(AuthState.REFRESHING);
                Gson gson = new Gson();
                TokenPair tokens = gson.fromJson(storageHelper.getDecryptedData(), TokenPair.class);
                String refreshToken = tokens.getRefreshToken();
                String accessToken = tokens.getAccessToken();
                if (refreshToken == null)
                {
                    authState.postValue(AuthState.UNAUTHENTICATED);
                    return;
                }
                if (accessToken != null)
                {
                    Response<UserResponseDto> responseAuth = api.getUserByToken("Bearer " + accessToken).execute();
                    if (responseAuth.isSuccessful() && responseAuth.body() != null)
                    {
                        username.postValue(responseAuth.body().getName());
                        email.postValue(responseAuth.body().getEmail());
                        phoneNumber.postValue(responseAuth.body().getPhoneNumber());
                        authState.postValue(AuthState.AUTHENTICATED);
                        return;
                    }
                }
                if (tryRefresh(refreshToken)) return;

                authState.postValue(AuthState.UNAUTHENTICATED);
            }
            catch (Exception e)
            {
                authState.postValue(AuthState.UNAUTHENTICATED);
            }
        }).start();
    }

    private boolean tryRefresh(String refreshToken) throws Exception
    {
        Response<AuthResponse> responseRefresh = api
                .refreshToken(new RefreshTokenRequest(refreshToken))
                .execute();
        if (responseRefresh.isSuccessful() && responseRefresh.body() != null)
        {
            Gson gson = new Gson();
            storageHelper.saveEncryptedData(gson.toJson(
                    new TokenPair(responseRefresh.body().getAccessToken(), refreshToken)));
            authState.postValue(AuthState.AUTHENTICATED);
            return true;
        }
        return false;
    }

    public LiveData<AuthState> getAuthState()
    {
        return authState;
    }

    public ApiService getApi()
    {
        return api;
    }

    public LiveData<String> getUsername()
    {
        return username;
    }
    public LiveData<String> getEmail()
    {
        return email;
    }
    public LiveData<String> getPhoneNumber()
    {
        return phoneNumber;
    }

    public void login(String accessToken, String refreshToken)
    {
        new Thread(() ->
        {
            try
            {
                Response<UserResponseDto> responseAuth = api.getUserByToken(
                        "Bearer " + accessToken).execute();
                if (responseAuth.isSuccessful() && responseAuth.body() != null)
                {
                    Gson gson = new Gson();
                    storageHelper.saveEncryptedData(gson.toJson(
                            new TokenPair(accessToken, refreshToken)));
                    username.postValue(responseAuth.body().getName());
                    email.postValue(responseAuth.body().getEmail());
                    phoneNumber.postValue(responseAuth.body().getPhoneNumber());
                    authState.postValue(AuthState.AUTHENTICATED);
                }
                else
                {
                    storageHelper.clearData();
                    authState.postValue(AuthState.UNAUTHENTICATED);
                }
            }
            catch (Exception e)
            {
                authState.postValue(AuthState.UNAUTHENTICATED);
            }
        }).start();
    }

    public void logout()
    {
        new Thread(() ->
        {
            try
            {
                storageHelper.clearData();
                authState.postValue(AuthState.UNAUTHENTICATED);
            }
            catch (Exception e)
            {
                authState.postValue(AuthState.UNAUTHENTICATED);
            }
        }).start();
    }

    public void saveUser(UserRequestDto user)
    {
        new Thread(() ->
        {
            try
            {
                Gson gson = new Gson();
                TokenPair tokens = gson.fromJson(storageHelper.getDecryptedData(), TokenPair.class);
                String accessToken = tokens.getAccessToken();
                Response<UserRequestDto> response = api.saveUserByToken(
                        "Bearer " + accessToken, user).execute();
                if (response.isSuccessful() && response.body() != null)
                {
                    username.postValue(response.body().getName());
                    email.postValue(response.body().getEmail());
                    phoneNumber.postValue(response.body().getPhoneNumber());
                    return;
                }
                String refreshToken = tokens.getRefreshToken();
                if (tryRefresh(refreshToken)) return;

                Log.e("API", "saveUser: Error while saving user");
            }
            catch (Exception e)
            {
                Log.e("API", "saveUser: Error while saving user", e);
            }
        }).start();
    }

    public enum AuthState
    {
        UNAUTHENTICATED,
        AUTHENTICATED,
        REFRESHING
    }
}
