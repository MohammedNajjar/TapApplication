package com.example.tabapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tabapplication.R;
import com.example.tabapplication.model.City;

import java.util.List;


public class AdapterCitySp extends BaseAdapter {

    private List<City> cities;

    public AdapterCitySp(List<City> cities) {
        this.cities = cities;
    }


    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public City getItem(int i) {
        return cities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v =view;

        if(v == null){
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_sp_city,viewGroup,false);
        }
        TextView tv_name = v.findViewById(R.id.custom_sp_city_tv_city);

        City cc = cities.get(i);
        tv_name.setText(cc.getName());

        return v;
    }
}
