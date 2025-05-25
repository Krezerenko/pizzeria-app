package io.github.krezerenko.sem4_pizzeria.cart;

import android.util.Log;
import android.util.MutableInt;

import androidx.annotation.NonNull;
import androidx.collection.MutableObjectList;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import io.github.krezerenko.sem4_pizzeria.api.ApiService;
import io.github.krezerenko.sem4_pizzeria.api.RetrofitClient;
import io.github.krezerenko.sem4_pizzeria.menu.MenuFragment;
import io.github.krezerenko.sem4_pizzeria.menu.MenuItemAdapter;
import io.github.krezerenko.sem4_pizzeria.menu.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartViewModel extends ViewModel
{
    private final List<ProductCounted> cartItems = new ArrayList<>();
    private final MutableLiveData<Boolean> isInitialized = new MutableLiveData<>(false);

    public CartViewModel()
    {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        api.getProducts().enqueue(new Callback<List<Product>>()
        {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    setItemsInitial(response.body());
                    isInitialized.postValue(true);
                }
                else
                {
                    Log.e("API", "onResponse: Failed to receive products");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t)
            {
                Log.e("API", "onResponse: Failed to receive products", t);
            }
        });

    }

    private void setItemsInitial(List<Product> items)
    {
        for (Product item : items)
        {
            cartItems.add(new ProductCounted(item, 0));
        }
    }

    public List<ProductCounted> getCartItems()
    {
        return cartItems;
    }

    public LiveData<Boolean> getInitializationState()
    {
        return isInitialized;
    }

//    public void incrementWithName(String name)
//    {
//        int position = cartItems.indexOfFirst(item -> item.getProduct().getName().equals(name));
//        if (position == -1) return;
//        cartItems
//    }
//
//    public void incrementOnPosition(int position)
//    {
//        ProductCounted item = cartItems.get(position);
//        cartItems.set(position, new ProductCounted(item.getProduct(), item.getCount() + 1));
//    }
//
//    public void decrementOnPosition(int position)
//    {
//        ProductCounted item = cartItems.get(position);
//        if (item.getCount() == 0) return;
//        cartItems.set(position, new ProductCounted(item.getProduct(), item.getCount() - 1));
//    }
}
