package io.github.krezerenko.sem4_pizzeria.menu;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import io.github.krezerenko.sem4_pizzeria.R;

public class MenuFragment extends Fragment
{
    private SearchView searchMenu;

    public MenuFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        searchMenu = view.findViewById(R.id.search_menu);
//        EditText edit = searchMenu.findViewById(androidx.appcompat.R.id.search_src_text);
//        edit.setTextColor(Color.BLACK);
    }

    public void initSearch(MenuItemAdapter adapter)
    {
        searchMenu.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                adapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                adapter.filter(newText);
                return false;
            }
        });
    }
}