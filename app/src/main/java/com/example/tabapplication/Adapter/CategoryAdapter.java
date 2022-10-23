package com.example.tabapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tabapplication.R;
import com.example.tabapplication.model.Categories;

import java.util.ArrayList;


public class CategoryAdapter extends BaseAdapter {
    ArrayList<Categories> categories;
    Context context;

    public CategoryAdapter(ArrayList<Categories> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }



    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Categories getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public String getItemDocID(int position){
        return categories.get(position).getCategoriesID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            v = LayoutInflater.from(context).inflate(R.layout.custom_spinner_item,parent,false);
        }

        TextView tv = v.findViewById(R.id.custom_sp_item);
        tv.setText(getItem(position).getTitle());

        return v;
    }
}
