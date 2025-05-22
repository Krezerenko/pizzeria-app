package io.github.krezerenko.sem4_pizzeria;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import io.github.krezerenko.sem4_pizzeria.databinding.FragmentMenuItemBinding;

import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder>
{

    private final List<MenuItem> mValues;

    public MenuItemAdapter(List<MenuItem> items)
    {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(FragmentMenuItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.mItem = mValues.get(position);
        holder.mTextName.setText(mValues.get(position).getName());
        holder.mTextDescription.setText(mValues.get(position).getDescription());
        holder.mTextPrice.setText(mValues.get(position).getPrice());
        Glide.with(holder.mImage.getContext())
                .load(mValues.get(position).getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.mImage);
    }

    @Override
    public int getItemCount()
    {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final TextView mTextName;
        public final TextView mTextDescription;
        public final TextView mTextPrice;
        public final ImageView mImage;
        public MenuItem mItem;

        public ViewHolder(FragmentMenuItemBinding binding)
        {
            super(binding.getRoot());
            mTextName = binding.textName;
            mTextDescription = binding.textDescription;
            mTextPrice = binding.textPrice;
            mImage = binding.image;
        }
    }
}