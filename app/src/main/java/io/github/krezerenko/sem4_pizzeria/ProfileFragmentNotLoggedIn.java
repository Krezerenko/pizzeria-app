package io.github.krezerenko.sem4_pizzeria;

import android.content.Context;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import io.github.krezerenko.sem4_pizzeria.api.AuthResponse;
import io.github.krezerenko.sem4_pizzeria.api.LoginRequest;
import io.github.krezerenko.sem4_pizzeria.api.UserRegistrationDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragmentNotLoggedIn extends Fragment
{
    private ProfileViewModel viewModel;

    public ProfileFragmentNotLoggedIn()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_profile_not_logged_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Context context = view.getContext();

        ProfileInputField inputUsername = view.findViewById(R.id.input_profile_username);
        ProfileInputField inputPassword = view.findViewById(R.id.input_profile_password);
        EditText editUsername = inputUsername.getEditFieldValue();
        EditText editPassword = inputPassword.getEditFieldValue();
        editPassword.setTransformationMethod(new PasswordTransformationMethod());

        Button buttonLogin = view.findViewById(R.id.button_profile_login);
        buttonLogin.setOnClickListener(v ->
                viewModel.getApi().loginUser(new LoginRequest(
                        editUsername.getText().toString(), editPassword.getText().toString()))
                        .enqueue(new AuthCallback(context)));

        Button buttonRegister = view.findViewById(R.id.button_profile_register);
        buttonRegister.setOnClickListener(v ->
                viewModel.getApi().registerUser(new UserRegistrationDto(
                        editUsername.getText().toString(), editPassword.getText().toString()))
                        .enqueue(new AuthCallback(context)));
    }

    private class AuthCallback implements Callback<AuthResponse>
    {
        private final Context context;

        public AuthCallback(Context context)
        {
            this.context = context;
        }

        @Override
        public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response)
        {
            if (response.isSuccessful() && response.body() != null)
            {
                viewModel.login(response.body().getAccessToken(), response.body().getRefreshToken());
                Toast.makeText(context, "Упешная авторизация", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context, "Ошибка авторизации", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<AuthResponse> call, Throwable t)
        {
            Toast.makeText(context, "Ошибка авторизации", Toast.LENGTH_SHORT).show();
        }
    }
}