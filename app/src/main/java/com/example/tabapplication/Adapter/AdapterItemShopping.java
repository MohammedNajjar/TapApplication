package com.example.tabapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tabapplication.R;
import com.example.tabapplication.databinding.CustomItemShoppingBasketBinding;
import com.example.tabapplication.model.Item;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AdapterItemShopping extends RecyclerView.Adapter<AdapterItemShopping.ItemShoppingHolder> {

    private List<Item> items;
    private OnRecyclerViewItemClickListener listener;

    public AdapterItemShopping(List<Item> items, OnRecyclerViewItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public AdapterItemShopping(List<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemShoppingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomItemShoppingBasketBinding binding = CustomItemShoppingBasketBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemShoppingHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ItemShoppingHolder holder, int position) {
        Item item = items.get(position);
        holder.binding.customAddNewItemTvNameItem.setText(item.getTitle());
        holder.binding.customAddNewItemTvPriceItem.setText(String.valueOf(item.getPrice())  );
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

        //------------ new

        holder.it = item;
        // holder.Num = String.valueOf(holder.binding.customAddNewItemEtNumItem);   // toast if edit text empty ????

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class ItemShoppingHolder extends RecyclerView.ViewHolder {
        CustomItemShoppingBasketBinding binding;
        Item it,ch;
        int Num = 0;
        int chair=0;

        public ItemShoppingHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomItemShoppingBasketBinding.bind(itemView);

            binding.customAddNewItemTvAddNumItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Num = Num + 1;
                    binding.customAddNewItemEtNumItem.setText(String.valueOf(Num));

                    int priceM = Integer.parseInt(binding.customAddNewItemTvPriceItem.getText().toString());
                    //total = priceM * Num;
                    int total = priceM * Num;
                    binding.customAddNewItemTvNameTotal.setText(String.valueOf( total));

                }
            });
            binding.customAddNewItemTvSubNumItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Num = Num - 1;
                    binding.customAddNewItemEtNumItem.setText(String.valueOf(Num));

                    int priceM = Integer.parseInt(binding.customAddNewItemTvPriceItem.getText().toString());
                    //total = priceM * Num;
                    int total = priceM * Num;
                    binding.customAddNewItemTvNameTotal.setText(String.valueOf( total));

                }
            });
//            binding.customAddNewChair.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                   chair = chair+ 1;
//                    binding.customNumChairs.setText(String.valueOf(chair));
//                }
//            });
//            binding.customAddNewChairSub.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    chair = chair - 1;
//                    binding.customNumChairs.setText(String.valueOf(chair));
//
//                }
//            });


            binding.customAddNewItemBtnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // ????????????????????????????
                    //??????????
                    String number = binding.customAddNewItemEtNumItem.getText().toString();
//                    String numChaairs = binding.customNumChairs.getText().toString();
                    if(number.equals(null)){
                        binding.customAddNewItemEtNumItem.setError("Enter Number");
                        return;
                    }
                    Num = Integer.parseInt(number);
//                    chair = Integer.parseInt(numChaairs);

                    listener.onBtnItemClick(it, Num,chair);

                }
            });
//                    if (Num==0) {
//                        binding.customAddNewItemEtNumItem.setError("Please Enter Number");
//                        binding.customAddNewItemEtNumItem.requestFocus();
//                        return;
//                    }


        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onBtnItemClick(Item item, int num,int chair);
    }
}
