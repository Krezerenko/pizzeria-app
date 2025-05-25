package io.github.krezerenko.sem4_pizzeria.cart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import io.github.krezerenko.sem4_pizzeria.R;

public class CartFragment extends Fragment
{
    public CartFragment()
    {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        FragmentContainerView listFragmentContainer = view.findViewById(R.id.fragment_container_cart_list);
        CartItemFragment listFragment = listFragmentContainer.getFragment();
        listFragment.setButtonClearCart(view.findViewById(R.id.image_cart_clear));
        listFragment.setViews(view.findViewById(R.id.text_cart_count_total),
                view.findViewById(R.id.text_cart_price_products),
                view.findViewById(R.id.text_cart_discount),
                view.findViewById(R.id.text_cart_price_delivery),
                view.findViewById(R.id.text_cart_points),
                view.findViewById(R.id.text_cart_price_total));
    }
}