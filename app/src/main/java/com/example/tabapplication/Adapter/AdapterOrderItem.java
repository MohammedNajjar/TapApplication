package com.example.tabapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tabapplication.databinding.CustomDetailsItemBinding;
import com.example.tabapplication.model.OrderItem;

import java.util.List;


public class AdapterOrderItem extends RecyclerView.Adapter<AdapterOrderItem.OrderItemHolder> {

    private List<OrderItem> orderItems;

    public AdapterOrderItem(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public OrderItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomDetailsItemBinding binding = CustomDetailsItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new OrderItemHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemHolder holder, int position) {
        OrderItem orderItem = orderItems.get(position);
        holder.binding.customDetailsItemTvNameItem.setText(orderItem.getItem().getTitle());
        holder.binding.customDetailsItemTvNumberItem.setText(String.valueOf(orderItem.getNumber()));
        holder.binding.customDetailsItemTvPriceItem.setText(String.valueOf(orderItem.getItem().getPrice()));

        double num,price,total;
        num = orderItem.getNumber();
        price = orderItem.getItem().getPrice();
        total = num * price;
        holder.binding.customDetailsItemTvTotalPriceItem.setText(String.valueOf(total));

    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }


    class OrderItemHolder extends RecyclerView.ViewHolder{
        CustomDetailsItemBinding binding;
        public OrderItemHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomDetailsItemBinding.bind(itemView);

        }
    }
}
