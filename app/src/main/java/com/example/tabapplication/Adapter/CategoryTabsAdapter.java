package com.example.tabapplication.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import com.example.tabapplication.WallpapersFragment;
import com.example.tabapplication.model.Categories;

import java.util.ArrayList;

public class CategoryTabsAdapter extends FragmentStatePagerAdapter {
    ArrayList<Categories> categories;
    public CategoryTabsAdapter(@NonNull FragmentManager fm, ArrayList<Categories> categories) {
        super(fm);
        this.categories = categories;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return WallpapersFragment.newInstance(categories.get(position).getCategoriesID());
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categories.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return categories.size();
    }
}
