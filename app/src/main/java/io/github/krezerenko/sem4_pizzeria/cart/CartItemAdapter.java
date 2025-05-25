package io.github.krezerenko.sem4_pizzeria.cart;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import io.github.krezerenko.sem4_pizzeria.R;
import io.github.krezerenko.sem4_pizzeria.databinding.FragmentCartItemBinding;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder>
{
    private final List<ProductCounted> mItems;

    private int countTotal = 0;
    private double priceProducts = 0d;
    private final int DISCOUNT = 0;
    private final double PRICE_DELIVERY = 0;
    private int points = 0;
    private double priceTotal = 0d;

    private final TextView textCountTotal;
    private final TextView textPriceProducts;
    private final TextView textDiscount;
    private final TextView textPriceDelivery;
    private final TextView textPoints;
    private final TextView textPriceTotal;

    public CartItemAdapter(List<ProductCounted> items, TextView textCountTotal,
                           TextView textPriceProducts,
                           TextView textDiscount,
                           TextView textPriceDelivery,
                           TextView textPoints,
                           TextView textPriceTotal)
    {
        mItems = items;
        this.textCountTotal = textCountTotal;
        this.textPriceProducts = textPriceProducts;
        this.textDiscount = textDiscount;
        this.textPriceDelivery = textPriceDelivery;
        this.textPoints = textPoints;
        this.textPriceTotal = textPriceTotal;
        updateTotals();
    }

    @NonNull
    @Override
    public CartItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        ViewHolder holder = new CartItemAdapter.ViewHolder(FragmentCartItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        holder.mButtonModify.setOnCountChangedListener((oldCount, newCount) ->
        {
            holder.mItem.setCount(newCount);
            if (holder.mItem.getCount() == 0)
            {
                int latestPosition = mItems.indexOf(holder.mItem);
                mItems.remove(latestPosition);
                notifyItemRemoved(latestPosition);
            }
            updateTotals();
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final CartItemAdapter.ViewHolder holder, int position)
    {
        holder.mItem = mItems.get(position);
        holder.mTextName.setText(mItems.get(position).getProduct().getName());
        holder.mTextDescription.setText(mItems.get(position).getProduct().getDescription());
        String priceFormat = holder.mTextPrice.getContext().getString(R.string.price_cart);
        holder.mTextPrice.setText(String.format(priceFormat, mItems.get(position).getProduct().getPrice()));
        Glide.with(holder.mImage.getContext())
                .load(mItems.get(position).getProduct().getImageUrl())
                .placeholder(R.color.image_placeholder)
                .error(R.color.image_placeholder)
                .into(holder.mImage);
        holder.mButtonModify.setCount(mItems.get(position).getCount());
    }

    @Override
    public int getItemCount()
    {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final TextView mTextName;
        public final TextView mTextDescription;
        public final TextView mTextPrice;
        public final ImageView mImage;
        public final ButtonModifyCount mButtonModify;
        public ProductCounted mItem;

        public ViewHolder(FragmentCartItemBinding binding)
        {
            super(binding.getRoot());
            mTextName = binding.textCartItemName;
            mTextDescription = binding.textCartItemDescription;
            mTextPrice = binding.textCartItemPrice;
            mImage = binding.imageCartItemProduct;
            mButtonModify = binding.buttonModifyCartItem;
        }
    }

    public void clearCart()
    {
        int size = mItems.size();
        for (int i = 0; i < size; ++i)
        {
            mItems.get(i).setCount(0);
        }
        mItems.clear();
        notifyItemRangeRemoved(0, size);
        updateTotals();
    }

    private void updateTotals()
    {
        countTotal = 0;
        priceProducts = 0;
        points = 0;
        for (ProductCounted item : mItems)
        {
            countTotal += item.getCount();
            priceProducts += item.getProduct().getPrice() * item.getCount();
            points += item.getProduct().getPoints() * item.getCount();
        }
        priceTotal = priceProducts + PRICE_DELIVERY;
        String formatCount = textPriceProducts.getContext().getString(R.string.count_cart);
        String formatPrice = textPriceProducts.getContext().getString(R.string.price_cart);
        String formatDiscount = textPriceProducts.getContext().getString(R.string.discount_cart);
        String formatPoints = textPriceProducts.getContext().getString(R.string.points_cart);
        textCountTotal.setText(String.format(formatCount, countTotal, formatProduct(countTotal)));
        textPriceProducts.setText(String.format(formatPrice, priceProducts));
        textDiscount.setText(String.format(formatDiscount, DISCOUNT));
        textPriceDelivery.setText(String.format(formatPrice, PRICE_DELIVERY));
        textPoints.setText(String.format(formatPoints, points));
        textPriceTotal.setText(String.format(formatPrice, priceTotal));
    }

    public static String formatProduct(int number)
    {
        int absNumber = Math.abs(number);
        int nMod100 = absNumber % 100;
        int nMod10 = absNumber % 10;

        if (nMod100 >= 11 && nMod100 <= 14)
        {
            return "Товаров";
        }
        if (nMod10 == 1)
        {
            return "Товар";
        }
        if (nMod10 >= 2 && nMod10 <= 4)
        {
            return "Товара";
        }
        return "Товаров";
    }
}