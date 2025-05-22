package io.github.krezerenko.sem4_pizzeria;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuItemFragment extends Fragment
{

    public MenuItemFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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
            ApiService api = RetrofitClient.getClient().create(ApiService.class);
            api.getProducts().enqueue(new Callback<List<MenuItem>>()
            {
                @Override
                public void onResponse(@NonNull Call<List<MenuItem>> call, @NonNull Response<List<MenuItem>> response)
                {
                    if (response.isSuccessful())
                    {
                        recyclerView.setAdapter(new MenuItemAdapter(response.body()));
                    }
                    else
                    {
                        Log.e("API", "onResponse: Failed to receive products");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<MenuItem>> call, @NonNull Throwable t)
                {
                    Log.e("API", "onResponse: Failed to receive products", t);
                }
            });
        }
    }
}