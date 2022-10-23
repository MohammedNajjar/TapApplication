package com.example.tabapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.tabapplication.databinding.ActivityUloginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ULoginActivity extends AppCompatActivity {
    ActivityUloginBinding binding;
    public static final String ADMIN_COLLECTION = "Admin";
    public static final String EMAILUSERCURRENT = "emailUserCurrent";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private SharedPreferences sp;
    private String et_email;
    private String et_password;
    private int userOrAdmin = 0;
    SharedPreferences.Editor editor;
    public final static String SP_NAME = "accountResturent";
    private static final String USERID = "userId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUloginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.setTitle("Login Admin Screen");
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth
        sp= getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
//        sp = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit();
        //------------------------------------------------------------------------------------------

        // Get Email and Password From Register Screen
        Intent intent = getIntent();
        String name = intent.getStringExtra(URegisterActivity.EMAIL_KEY);
        binding.loginAdminEtEmail.setText(name);
        String password = intent.getStringExtra(URegisterActivity.PASSWORD_KEY);
        binding.loginAdminEtPassword.setText(password);

        // Go To Register Screen
        binding.loginAdminTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), URegisterActivity.class);
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
                            // progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            userOrAdmin = 0;
                            editor.putInt("userOrAdmin", userOrAdmin);
                            editor.putString(EMAILUSERCURRENT, et_email);
                            editor.putString(USERID, mAuth.getUid());
                            boolean save =false;
                            if(binding.loginAdminChBoxSave.isChecked())
                                save=true;
                            editor.putBoolean("chBox_save", save);
                            editor.commit();

                            startActivity(new Intent(ULoginActivity.this, HomeActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(ULoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ULoginActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
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
            startActivity(new Intent(getBaseContext(), HomeActivity.class));
            finish();
        }
    }

}
