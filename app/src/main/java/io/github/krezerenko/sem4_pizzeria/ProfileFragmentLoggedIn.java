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

public class ProfileFragmentLoggedIn extends Fragment
{
    private ProfileViewModel viewModel;

    public ProfileFragmentLoggedIn()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_profile_logged_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Context context = view.getContext();

        ProfileInputField inputUsername = view.findViewById(R.id.input_profile_username);
        ProfileInputField inputEmail = view.findViewById(R.id.input_profile_email);
        ProfileInputField inputPhone = view.findViewById(R.id.input_profile_phone_number);
        ProfileInputField inputPassword = view.findViewById(R.id.input_profile_password);
        EditText editUsername = inputUsername.getEditFieldValue();
        EditText editEmail = inputEmail.getEditFieldValue();
        EditText editPhoneNumber = inputPhone.getEditFieldValue();
        EditText editPassword = inputPassword.getEditFieldValue();
        editPassword.setTransformationMethod(new PasswordTransformationMethod());

        TextView textUsername = view.findViewById(R.id.text_profile_username);
        TextView textEmail = view.findViewById(R.id.text_profile_email);
        TextView textPhoneNumber = view.findViewById(R.id.text_profile_phone_number);
        viewModel.getUsername().observe(getViewLifecycleOwner(), textUsername::setText);
        viewModel.getEmail().observe(getViewLifecycleOwner(), email ->
                textEmail.setText(blankOrElse(email, "Электронная почта не указана")));
        viewModel.getPhoneNumber().observe(getViewLifecycleOwner(), phoneNumber ->
                textPhoneNumber.setText(blankOrElse(phoneNumber, "Номер телефона не указан")));

        Button buttonLogout = view.findViewById(R.id.button_profile_logout);
        buttonLogout.setOnClickListener(v -> viewModel.logout());
        Button buttonSave = view.findViewById(R.id.button_profile_save);
        buttonSave.setOnClickListener(v ->
        {
            if (isNullOrBlank(editUsername.getText().toString()))
            {
                Toast.makeText(context, "Введите новое имя пользователя",
                        Toast.LENGTH_SHORT).show();
            }
            viewModel.saveUser(new UserRequestDto(
                    editUsername.getText().toString(),
                    editEmail.getText().toString(),
                    editPhoneNumber.getText().toString(),
                    editPassword.getText().toString()
            ));
        });
    }

    private boolean isNullOrBlank(String str)
    {
        return str == null || str.isBlank();
    }

    private String blankOrElse(String str, String def)
    {
        if (isNullOrBlank(str)) return def;
        else return str;
    }
}