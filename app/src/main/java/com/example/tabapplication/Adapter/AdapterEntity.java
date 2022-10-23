package com.example.tabapplication.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tabapplication.R;
import com.example.tabapplication.databinding.CustomEntityBinding;
import com.example.tabapplication.model.UseRestaurant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterEntity extends RecyclerView.Adapter<AdapterEntity.EntityHolder> {

    private ArrayList<UseRestaurant> entities;
    private OnRecyclerViewItemClickListener listener;


    public AdapterEntity(ArrayList<UseRestaurant> entities, OnRecyclerViewItemClickListener listener) {
        this.entities = entities;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EntityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomEntityBinding binding = CustomEntityBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EntityHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull EntityHolder holder, int position) {
        UseRestaurant entity = entities.get(position);

        holder.binding.customEntityTvTitle.setText(entity.getTitle());
        holder.binding.customEntityTvAddress.setText(entity.getAddress());
        holder.binding.customEntityTvCategory.setText(entity.getCategory());
        if (entity.getUrl_logo() != null) {
            Picasso.get()
                    .load(entity.getUrl_logo())
                    .resize(130, 100)
                    .centerCrop()
                    .into(holder.binding.customEntityIv);
            //  holder.binding.customAddNewItemIvItem.setImageURI(Uri.parse(item.getImage()));
        } else {
            Picasso.get()
                    .load(String.valueOf(R.drawable.logo))
                    .resize(130, 100)
                    .centerCrop()
                    .into(holder.binding.customEntityIv);
            // holder.binding.customAddNewItemIvItem.setImageURI(Uri.parse(String.valueOf(R.drawable.ic_add_photo_item)));
        }
        holder.ent = entity;

    }

    @Override
    public int getItemCount() {
        return entities.size();
    }


    class EntityHolder extends RecyclerView.ViewHolder {
        CustomEntityBinding binding;
        UseRestaurant ent;

        public EntityHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomEntityBinding.bind(itemView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listener.onItemClick(getAdapterPosition());
                }
            });

        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(int id);
    }

}

