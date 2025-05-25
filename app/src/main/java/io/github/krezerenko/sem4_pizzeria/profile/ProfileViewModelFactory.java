package io.github.krezerenko.sem4_pizzeria.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.github.krezerenko.sem4_pizzeria.api.ApiService;
import io.github.krezerenko.sem4_pizzeria.api.SecureStorageHelper;

public class ProfileViewModelFactory implements ViewModelProvider.Factory
{
    private final SecureStorageHelper storageHelper;
    private final ApiService apiService;

    public ProfileViewModelFactory(SecureStorageHelper storageHelper, ApiService apiService)
    {
        this.storageHelper = storageHelper;
        this.apiService = apiService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
    {
        return (T) new ProfileViewModel(storageHelper, apiService);
    }
}
