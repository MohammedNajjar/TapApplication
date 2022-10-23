package com.example.tabapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.tabapplication.databinding.ActivityHomeBinding;
import com.example.tabapplication.ui.RHomeFragment;
import com.example.tabapplication.ui.RItemFragment;
import com.example.tabapplication.ui.RMoreFragment;
import com.example.tabapplication.ui.ROrderFragment;


public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.homeBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_more));
        binding.homeBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic__add_resturant));
        binding.homeBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_order));
        binding.homeBottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_home));

        binding.homeBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;
                switch (item.getId()) {
                    case 1:
                        fragment = new RMoreFragment();
                        break;
                    case 2:
                        fragment = new RItemFragment();
                        break;
                    case 3:
                        fragment = new ROrderFragment();
                        break;
                    case 4:
                        fragment = new RHomeFragment();
                        break;


                }
                loadFragment(fragment);
            }
        });

        binding.homeBottomNavigation.setCount(3, "");
        binding.homeBottomNavigation.show(4, true);
        binding.homeBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                //Toast.makeText(HomeActivity.this, "You Clicked:"+item.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        binding.homeBottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                //Toast.makeText(HomeActivity.this, "You Reslected:"+item.getId(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.home_frame_layout, fragment)
                .commit();
    }
}