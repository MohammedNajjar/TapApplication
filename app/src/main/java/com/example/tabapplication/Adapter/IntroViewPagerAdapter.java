package com.example.tabapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import com.example.tabapplication.R;
import com.example.tabapplication.model.ScreenItem;

import java.util.List;


public class IntroViewPagerAdapter extends PagerAdapter {
    Context context;
    List<ScreenItem> screenItems;

    public IntroViewPagerAdapter(Context context, List<ScreenItem> screenItems) {
        this.context = context;
        this.screenItems = screenItems;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen=inflater.inflate(R.layout.layout_screen,null);
        ImageView screenImg=layoutScreen.findViewById(R.id.intro_iv);
        TextView screenTitle=layoutScreen.findViewById(R.id.intro_tx_title);
        TextView screenDescription=layoutScreen.findViewById(R.id.intro_tv_description);

        screenTitle.setText(screenItems.get(position).getTitle());
        screenDescription.setText(screenItems.get(position).getDescription());
        screenImg.setImageResource(screenItems.get(position).getScreenImg());

        container.addView(layoutScreen);
        return layoutScreen;

    }

    @Override
    public int getCount() {
        return screenItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
       // super.destroyItem(container, position, object);
    }
}
