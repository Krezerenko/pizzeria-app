package io.github.krezerenko.sem4_pizzeria.menu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import io.github.krezerenko.sem4_pizzeria.R;
import io.github.krezerenko.sem4_pizzeria.cart.ButtonModifyCount;
import io.github.krezerenko.sem4_pizzeria.cart.ProductCounted;
import io.github.krezerenko.sem4_pizzeria.databinding.FragmentMenuItemBinding;

import java.util.ArrayList;
import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder>
{
    private final List<ProductCounted> mItems;
    List<ProductCounted> mItemsCopy;

    public MenuItemAdapter(List<ProductCounted> items)
    {
        mItems = items;
        mItemsCopy = new ArrayList<>();
        mItemsCopy.addAll(mItems);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        ViewHolder holder = new ViewHolder(FragmentMenuItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        holder.mButtonModify.setOnCountChangedListener(((oldCount, newCount) -> holder.mItem.setCount(newCount)));
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.mItem = mItems.get(position);
        holder.mTextName.setText(mItems.get(position).getProduct().getName());
        holder.mTextDescription.setText(mItems.get(position).getProduct().getDescription());
        String priceText = holder.mTextPrice.getContext().getString(R.string.price_menu_item);
        holder.mTextPrice.setText(String.format(priceText, mItems.get(position).getProduct().getPrice()));
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

        public ViewHolder(FragmentMenuItemBinding binding)
        {
            super(binding.getRoot());
            mTextName = binding.textMenuItemName;
            mTextDescription = binding.textMenuItemDescription;
            mTextPrice = binding.textMenuItemPrice;
            mImage = binding.imageMenuItem;
            mButtonModify = binding.buttonModifyMenuItem;
        }
    }

    public void filter(String text)
    {
        mItems.clear();
        if (text.isEmpty())
        {
            mItems.addAll(mItemsCopy);
        }
        else
        {
            text = text.toLowerCase();
            for (ProductCounted item : mItemsCopy)
            {
                if (item.getProduct().getName().toLowerCase().contains(text)
                        || item.getProduct().getDescription().toLowerCase().contains(text))
                {
                    mItems.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}