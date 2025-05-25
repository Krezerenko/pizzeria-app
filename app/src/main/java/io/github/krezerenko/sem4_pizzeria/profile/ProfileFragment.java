package io.github.krezerenko.sem4_pizzeria.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.krezerenko.sem4_pizzeria.R;
import io.github.krezerenko.sem4_pizzeria.api.ApiService;
import io.github.krezerenko.sem4_pizzeria.api.RetrofitClient;
import io.github.krezerenko.sem4_pizzeria.api.SecureStorageHelper;

public class ProfileFragment extends Fragment
{
    private ProfileViewModel viewModel;

    public ProfileFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        SecureStorageHelper storageHelper = new SecureStorageHelper(requireContext());
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        viewModel = new ViewModelProvider(requireActivity(),
                new ProfileViewModelFactory(storageHelper, api)).get(ProfileViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager fragmentManager = getChildFragmentManager();
        viewModel.getAuthState().observe(getViewLifecycleOwner(), state ->
        {
            switch (state)
            {
                case AUTHENTICATED:
                    changeFragment(new ProfileFragmentLoggedIn(), fragmentManager);
                    break;
                case UNAUTHENTICATED:
                    changeFragment(new ProfileFragmentNotLoggedIn(), fragmentManager);
                    break;
                case REFRESHING:
                    changeFragment(new Fragment(), fragmentManager);
                    break;
            }
        });
    }

    private void changeFragment(Fragment newFragment, FragmentManager manager)
    {
        manager.beginTransaction()
                .replace(R.id.fragment_container_profile, newFragment)
                .commit();
    }
}