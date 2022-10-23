package com.example.tabapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import com.example.tabapplication.Adapter.IntroViewPagerAdapter;
import com.example.tabapplication.model.ScreenItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class IntroActivity extends AppCompatActivity {

    IntroViewPagerAdapter adapter;
    private ViewPager viewPager;
    TabLayout tabLayout;
    Button btn_next, btn_start;
    int position = 0;
    Animation btnAnim;
    TextView skip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // make the activity on full screen

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // when this activity is about to be launch we need to check if its openened before or not

        if (restorePrefData()) {

            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivity);
            finish();


        }


        setContentView(R.layout.activity_intro);

        String t1 = getString(R.string.Into_title1);
        String t2 = getString(R.string.Into_title2);
        String t3 = getString(R.string.Into_title3);
        String d1 = getString(R.string.Into_description1);
        String d2 = getString(R.string.Into_description2);
        String d3 = getString(R.string.Into_description3);

        List<ScreenItem> items = new ArrayList<>();
        items.add(new ScreenItem(t1, d1, R.drawable.logo));
        items.add(new ScreenItem(t2, d2, R.drawable.logo));
        items.add(new ScreenItem(t2, d2, R.drawable.logo));
      //  items.add(new ScreenItem(t3, d3, R.drawable.intro3));


        viewPager = findViewById(R.id.screen_viewpager);
        adapter = new IntroViewPagerAdapter(this, items);

        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.screen_tablayout);
        tabLayout.setupWithViewPager(viewPager);

        btn_next = findViewById(R.id.screen_btn_next);
        btn_start = findViewById(R.id.screen_btn_getstarted);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.btn_animation);
        skip = findViewById(R.id.screen_tv_skip);


        // skip button click listener

        skip.setOnClickListener(v -> viewPager.setCurrentItem(items.size()));


        btn_next.setOnClickListener(view -> {
            position = viewPager.getCurrentItem();
            if (position < items.size()) {
                position++;
                viewPager.setCurrentItem(position);
            }

            if (position == items.size() - 1) {
                loadLastScreen();
            }

        });


        btn_start.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);

            saveData();
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == items.size() - 1) {

                    loadLastScreen();

                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        return pref.getBoolean("isIntroOpenend", false);

    }

    private void saveData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpenend", true);
        editor.apply();
    }

    public void loadLastScreen() {
        btn_next.setVisibility(View.INVISIBLE);
        btn_start.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.INVISIBLE);
        btn_start.setAnimation(btnAnim);

    }
}