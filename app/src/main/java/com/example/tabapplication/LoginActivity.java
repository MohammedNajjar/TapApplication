package com.example.tabapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.tabapplication.DataBeas.DataB;
import com.example.tabapplication.databinding.ActivityLoginBinding;
import com.example.tabapplication.model.ServesUser;


public class LoginActivity extends AppCompatActivity {
    public static final String ADMINAME ="adminName" ;
    ActivityLoginBinding binding;

    int Result_login;
    DataB db;
    SharedPreferences sp ;
    SharedPreferences.Editor spEdit;

    public static final String REMEMBER_KEY="remember";
    public final static String USER_NAME_KEY = "user_name";
    public final static String PASS_KEY = "password";

    SharedPreferences sp_account;
    SharedPreferences sp_login;

    SharedPreferences.Editor editor_account;
    SharedPreferences.Editor editor_login;
    public final static String SP_NAM = "accountAdmin";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(binding.getRoot());
        db = new DataB(this);
        sp= getApplicationContext().getSharedPreferences(SP_NAM, Context.MODE_PRIVATE);


//        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        spEdit = sp.edit();

        if(sp.getBoolean("isFirstTime",true)){
            db.fillData();
            spEdit.putBoolean("isFirstTime",false);
        }

        binding.Login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AddCategoryActivity.class);
                String un = binding.email.getText().toString();
                String pas = binding.password.getText().toString();
                ServesUser s = db.getCHStudint(Integer.parseInt(un),pas);
                if (s != null) {

                    spEdit.putInt("id",Integer.parseInt(un));
                    spEdit.putString(ADMINAME,un);
                    spEdit.putString("pass",s.getPassword());
                    spEdit.commit();
                    Toast.makeText(LoginActivity.this, "succeesful login", Toast.LENGTH_SHORT).show();
                    startActivityForResult(intent, Result_login);
                } else {
                    Toast.makeText(LoginActivity.this, "faliled login", Toast.LENGTH_SHORT).show();
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(findViewById(R.id.edit_area2));

                }
            }

        });
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Result_login && data != null && resultCode == RESULT_OK) {
            String userName = data.getStringExtra("UserName");
            String password = data.getStringExtra("Password");
            binding.email.setText(userName);
            binding.password.setText(password);
        }

    }

}
