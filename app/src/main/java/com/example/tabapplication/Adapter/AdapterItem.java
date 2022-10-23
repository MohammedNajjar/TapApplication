package com.example.tabapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tabapplication.R;
import com.example.tabapplication.databinding.CustomAddNewItemBinding;
import com.example.tabapplication.model.Item;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ItemHolder> {

    private List<Item> items;


    public AdapterItem(List<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomAddNewItemBinding binding = CustomAddNewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Item item = items.get(position);
        holder.binding.customAddNewItemTvNameItem.setText(item.getTitle());
        holder.binding.customAddNewItemTvPriceItem.setText(String.valueOf(item.getPrice()) + " ₪");
        holder.binding.customAddNewItemTvDescriptionItem.setText(item.getDescribe());
        if (item.getImage() != null) {
            Picasso.get()
                    .load(item.getImage())
                    .resize(120, 120)
                    .centerCrop()
                    .into(holder.binding.customAddNewItemIvItem);
            //  holder.binding.customAddNewItemIvItem.setImageURI(Uri.parse(item.getImage()));
        } else {
            Picasso.get()
                    .load(String.valueOf(R.drawable.logo))
                    .resize(120, 120)
                    .centerCrop()
                    .into(holder.binding.customAddNewItemIvItem);
           // holder.binding.customAddNewItemIvItem.setImageURI(Uri.parse(String.valueOf(R.drawable.ic_add_photo_item)));
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class ItemHolder extends RecyclerView.ViewHolder {
        CustomAddNewItemBinding binding;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomAddNewItemBinding.bind(itemView);

        }
    }
}
