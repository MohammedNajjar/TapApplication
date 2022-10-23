package com.example.tabapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tabapplication.databinding.CustomViewOrdersItemBinding;
import com.example.tabapplication.model.Order;
import com.example.tabapplication.model.OrderItem;

import java.util.ArrayList;

public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.OrderHolder> {

    private ArrayList<Order> orders;
    private OnRecyclerViewItemClickListener listener;
    private OnClickLocationListener addresslistener;

    public AdapterOrder(ArrayList<Order> orders, OnRecyclerViewItemClickListener listener, OnClickLocationListener addresslistener) {
        this.orders = orders;
        this.listener = listener;
        this.addresslistener = addresslistener;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomViewOrdersItemBinding binding = CustomViewOrdersItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new OrderHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        Order order = orders.get(position);

        holder.binding.customViewOrdersTvNameOwnerOrder.setText(order.getName_owner_order());
        holder.binding.customViewOrdersTvStatusOrder.setText(order.getStatus());
        holder.or = order;
        //double number,price,totalTotalAmounts;

        double TotalAmount = 0;
        if(order.getOrderItems() != null){
            for(OrderItem o : order.getOrderItems()){
                TotalAmount += o.getNumber() * o.getItem().getPrice();
            }
        }
        /*
        for (int i = 0; i< order.getOrderItems().size() ; i++ ){
            TotalAmount += (  (order.getOrderItems().get(i).getNumber())  *  (order.getOrderItems().get(i).getItem().getPrice())  );
        }
*/
        holder.binding.customViewOrdersTvTotalPrice.setText("Total amount: "+String.valueOf(TotalAmount)+" $");

        // map
        holder.binding.customViewOrdersTvAddress.setText("Address: "+order.getAddress());
        //-----


    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderHolder extends RecyclerView.ViewHolder{
        CustomViewOrdersItemBinding binding;
       Order or;
        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomViewOrdersItemBinding.bind(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(or);
                }
            });

            binding.customViewOrdersTvAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addresslistener.OnClickLocation(or.getLat(),or.getLng());
                }
            });


        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(Order order);
    }
    public interface OnClickLocationListener {
        void OnClickLocation( double lat, double lng);
    }

}

