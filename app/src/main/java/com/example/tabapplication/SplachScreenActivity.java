package com.example.tabapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tabapplication.databinding.ActivitySplachScreenBinding;


public class SplachScreenActivity extends AppCompatActivity {
    ActivitySplachScreenBinding binding;
    //private static final int SPLASH_DISPLAY_LENGTH = 2000;
    private Animation topAnim,bottonAnim;
    private static int SNAP=5000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplachScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_ainm);
        bottonAnim= AnimationUtils.loadAnimation(this,R.anim.botton_animation);

        binding.tvSnap.setAnimation(bottonAnim);
        binding.imgSnap.setAnimation(topAnim);






        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Thread Mythread=new Thread() {

            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent intent=new Intent(getApplicationContext(), IntroActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        };
        Mythread.start();

    }
}