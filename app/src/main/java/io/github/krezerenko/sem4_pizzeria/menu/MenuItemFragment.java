package io.github.krezerenko.sem4_pizzeria.menu;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.github.krezerenko.sem4_pizzeria.R;
import io.github.krezerenko.sem4_pizzeria.api.ApiService;
import io.github.krezerenko.sem4_pizzeria.api.RetrofitClient;
import io.github.krezerenko.sem4_pizzeria.cart.CartViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuItemFragment extends Fragment
{
    private CartViewModel viewModel;

    public MenuItemFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_menu_item_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        if (view instanceof RecyclerView)
        {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            viewModel.getInitializationState().observe(getViewLifecycleOwner(), isInitialized ->
            {
                if (!isInitialized) return;
                MenuItemAdapter adapter = new MenuItemAdapter(List.copyOf(viewModel.getCartItems()));
                recyclerView.setAdapter(adapter);
                MenuFragment parent = (MenuFragment)getParentFragment();
                parent.initSearch(adapter);
            });
        }
    }
}