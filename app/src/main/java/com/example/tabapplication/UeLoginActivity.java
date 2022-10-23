package com.example.tabapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.tabapplication.databinding.ActivityUeLoginBinding;
import com.example.tabapplication.databinding.ActivityUloginBinding;
import com.example.tabapplication.databinding.ActivityUserRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class UeLoginActivity extends AppCompatActivity {
    ActivityUeLoginBinding binding;

    public static final String ADMIN_COLLECTION = "Admin";
    public static final String EMAILUSERCURRENT = "emailUserCurrent";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private SharedPreferences sp;
    private String et_email;
    private String et_password;
    private int userOrAdmin = 0;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ue_login);
        binding = ActivityUeLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.setTitle("Login Admin Screen");
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit();
        //------------------------------------------------------------------------------------------
        Intent intent = getIntent();
        String name = intent.getStringExtra(UserRegisterActivity.EMAIL_KEY);
        binding.loginAdminEtEmail.setText(name);
        String password = intent.getStringExtra(UserRegisterActivity.PASSWORD_KEY);
        binding.loginAdminEtPassword.setText(password);

        // Get Email and Password From Register Screen

        // Go To Register Screen
        binding.loginAdminTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), UserRegisterActivity.class);
                startActivity(intent);
            }
        });


        //Login As User
        binding.loginAdminBtnLoginAsAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                et_email = binding.loginAdminEtEmail.getText().toString();
                if (TextUtils.isEmpty(et_email)) {
                    binding.loginAdminEtEmail.setError("Please Enter E-mail");
                    binding.loginAdminEtEmail.requestFocus();
                    return;
                }
                et_password = binding.loginAdminEtPassword.getText().toString();
                if (TextUtils.isEmpty(et_password)) {
                    binding.loginAdminEtPassword.setError("Please Enter Password");
                    binding.loginAdminEtPassword.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(et_email).matches()) {
                    binding.loginAdminEtEmail.setError("Invalid Email");
                    binding.loginAdminEtEmail.setFocusable(true);
                } else {
                    // progress bar
                    loginUser(et_email, et_password);
                }
                //----------------------------------------------------------------------------------


            }



        });
    }


    private void loginUser(final String et_email, String et_password) {

        mAuth.signInWithEmailAndPassword(et_email, et_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            startActivity(new Intent(UeLoginActivity.this, ViewActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(UeLoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UeLoginActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sp.getBoolean("chBox_save", false))
            checkUserStatus();
    }

    private void checkUserStatus() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(getBaseContext(), ViewActivity.class));
            finish();
        }
    }

}
