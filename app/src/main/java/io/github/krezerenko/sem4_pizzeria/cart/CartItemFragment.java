package io.github.krezerenko.sem4_pizzeria.cart;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.krezerenko.sem4_pizzeria.R;

public class CartItemFragment extends Fragment
{
    private CartViewModel viewModel;
    private ImageView buttonClearCart;
    private CartItemAdapter adapter;

    private TextView textCountTotal;
    private TextView textPriceProducts;
    private TextView textDiscount;
    private TextView textPriceDelivery;
    private TextView textPoints;
    private TextView textPriceTotal;

    public CartItemFragment()
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
        return inflater.inflate(R.layout.fragment_cart_item_list, container, false);
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
                List<ProductCounted> allItems = viewModel.getCartItems();
                List<ProductCounted> itemsInCart = new ArrayList<>();
                for (ProductCounted item : allItems)
                {
                    if (item.getCount() == 0) continue;
                    itemsInCart.add(item);
                }

                adapter = new CartItemAdapter(itemsInCart,
                        textCountTotal,
                        textPriceProducts,
                        textDiscount,
                        textPriceDelivery,
                        textPoints,
                        textPriceTotal);
                buttonClearCart.setOnClickListener(v -> adapter.clearCart());
                recyclerView.setAdapter(adapter);
            });
        }
    }

    public void setButtonClearCart(ImageView buttonClearCart)
    {
        this.buttonClearCart = buttonClearCart;
    }

    public void setViews(TextView textCountTotal,
            TextView textPriceProducts,
            TextView textDiscount,
            TextView textPriceDelivery,
            TextView textPoints,
            TextView textPriceTotal)
    {
        this.textCountTotal = textCountTotal;
        this.textPriceProducts = textPriceProducts;
        this.textDiscount = textDiscount;
        this.textPriceDelivery = textPriceDelivery;
        this.textPoints = textPoints;
        this.textPriceTotal = textPriceTotal;
    }
}