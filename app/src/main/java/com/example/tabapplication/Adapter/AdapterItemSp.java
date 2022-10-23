package com.example.tabapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.tabapplication.R;
import com.example.tabapplication.model.Item;

import java.util.List;

public class AdapterItemSp extends BaseAdapter {

    private List<Item> items;

    public AdapterItemSp(List<Item> items) {
        this.items = items;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Item getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v =view;

        if(v == null){
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_sp_item,viewGroup,false);
        }
        TextView tv_title = v.findViewById(R.id.custom_sp_item_tv_title);
        TextView tv_price = v.findViewById(R.id.custom_sp_item_tv_price);

        Item ii = items.get(i);
        tv_title.setText(ii.getTitle());
        tv_price.setText(ii.getPrice()+" $");

        return v;
    }
}
