package com.example.tabapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.tabapplication.R;
import com.example.tabapplication.model.CategoryMenu;

import java.util.List;

public class AdapterCategorySp extends BaseAdapter {

    private List<CategoryMenu> categoriesList;

    public AdapterCategorySp(List<CategoryMenu> categoriesList) {
        this.categoriesList = categoriesList;
    }

    @Override
    public int getCount() {
        return categoriesList.size();
    }

    @Override
    public CategoryMenu getItem(int i) {
        return categoriesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v =view;

        if(v == null){
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_sp_category,viewGroup,false);
        }
        TextView tv_title = v.findViewById(R.id.custom_categories_tv_category);

        CategoryMenu cat = categoriesList.get(i);
        tv_title.setText(cat.getName());

        return v;
    }
}
