package io.github.krezerenko.sem4_pizzeria.profile;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import io.github.krezerenko.sem4_pizzeria.R;
import io.github.krezerenko.sem4_pizzeria.api.UserRequestDto;

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
        ProfileInputField inputPasswordSave = view.findViewById(R.id.input_profile_password_save);
        ProfileInputField inputPasswordDelete = view.findViewById(R.id.input_profile_password_delete);
        EditText editUsername = inputUsername.getEditFieldValue();
        EditText editEmail = inputEmail.getEditFieldValue();
        EditText editPhoneNumber = inputPhone.getEditFieldValue();
        EditText editPasswordSave = inputPasswordSave.getEditFieldValue();
        editPasswordSave.setTransformationMethod(new PasswordTransformationMethod());
        EditText editPasswordDelete = inputPasswordDelete.getEditFieldValue();
        editPasswordDelete.setTransformationMethod(new PasswordTransformationMethod());

        TextView textUsername = view.findViewById(R.id.text_profile_username);
        TextView textEmail = view.findViewById(R.id.text_profile_email);
        TextView textPhoneNumber = view.findViewById(R.id.text_profile_phone_number);
        textUsername.setText(viewModel.getUsername().getValue());
        textEmail.setText(blankOrElse(viewModel.getEmail().getValue(), "Электронная почта не указана"));
        textPhoneNumber.setText(blankOrElse(viewModel.getPhoneNumber().getValue(), "Номер телефона не указан"));
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
                new AlertDialog.Builder(context)
                        .setMessage("Имя пользователя не должно быть пустым")
                        .setTitle("Введите имя пользователя")
                        .setIcon(R.drawable.ic_launcher_background)
                        .setPositiveButton("Закрыть", (dialog, which) -> {})
                        .show();
                return;
            }
            viewModel.saveUser(new UserRequestDto(
                    editUsername.getText().toString(),
                    editEmail.getText().toString(),
                    editPhoneNumber.getText().toString(),
                    editPasswordSave.getText().toString()
            ));
        });
        Button buttonDelete = view.findViewById(R.id.button_profile_delete);
        buttonDelete.setOnClickListener(v ->
        {
            Handler handler = new Handler(Looper.getMainLooper());
            new Thread(() ->
            {
                try
                {
                    if (viewModel.confirmPassword(editPasswordDelete.getText().toString()))
                    {
                        handler.post(() ->
                                new AlertDialog.Builder(context)
                                        .setMessage("Вы точно хотите удалить свой аккаунт?")
                                        .setTitle("Внимание!")
                                        .setIcon(R.drawable.ic_dialog_danger)
                                        .setPositiveButton("Подтвердить", (dialog, which) ->
                                                viewModel.deleteUser())
                                        .setNegativeButton("Отменить", (dialog, which) -> {})
                                        .show());
                    }
                    else
                    {
                        handler.post(() ->
                                new AlertDialog.Builder(context)
                                        .setMessage("Неправильный пароль")
                                        .setTitle("Ошибка удаления")
                                        .setIcon(R.drawable.ic_bottom_menu)
                                        .setPositiveButton("Ок", (dialog, which) -> {})
                                        .show());
                    }
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }).start();
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