package com.example.tabapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.tabapplication.Adapter.CategoryAdapter;
import com.example.tabapplication.databinding.ActivityUserRegisterBinding;
import com.example.tabapplication.model.Categories;
import com.example.tabapplication.model.UseRestaurant;
import com.example.tabapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class UserRegisterActivity extends AppCompatActivity {
    public static final String EMAIL_KEY = "email";
    public static final String PASSWORD_KEY = "password";
    public static final String Entitys_COLLECTION = "User";
    private static final String CATEGORY_COLLECTION = "Categories";
    private static final int MAPS_ENTITY_REQ_CODE = 2;
    private static final int PIK_IMAGE_REQ_CODE = 5;
    private Uri images_uri;
    private ActivityUserRegisterBinding binding;
    private FirebaseFirestore db;
    private StorageReference storage;
    private String email = "", password = "", name = "", re_password = "", phone = "", address = "";
    double lat, log;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        this.setTitle("Register User Screen");
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth


        // photo from gallery
        binding.registerAdminIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                } else {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                }
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent, PIK_IMAGE_REQ_CODE);
            }
        });

        // Toast to enter location at map
        binding.registerAdminEtAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Toast.makeText(UserRegisterActivity.this, "Click on the location icon to locate the place on the map ", Toast.LENGTH_LONG).show();
            }
        });

        //-- location
        binding.registerAdimTvImageLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                startActivityForResult(intent, MAPS_ENTITY_REQ_CODE);
            }
        });

        // button save
        binding.registerAdminBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.mainProgressBar.setVisibility(View.VISIBLE);
                binding.registerAdminBtnSave.setEnabled(false);
                registerSave();

            }
        });
    }

    //----------------------------------------------------------------------------------------------

    private void registerSave() {
        if (images_uri != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            final StorageReference mountainImageRef = storageRef.child("image/" + images_uri.getLastPathSegment());
            mountainImageRef.putFile(images_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mountainImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    addEntityToFirebase(uri.toString());
                                }
                            });
                        }
                    });
        } else {
            Toast.makeText(this, "Pleas Enter The Logo", Toast.LENGTH_SHORT).show();
            binding.registerAdminBtnSave.setEnabled(true);
            binding.mainProgressBar.setVisibility(View.GONE);
        }
    }


    private void addEntityToFirebase(final String image) {
        name = binding.registerAdminEtName.getText().toString();
        email = binding.registerAdminEtEmail.getText().toString();
        password = binding.registerAdminEtPassword.getText().toString();
        re_password = binding.registerAdminEtRePassword.getText().toString();
        phone = binding.registerAdminEtPhone.getText().toString();
        address = binding.registerAdminEtAddress.getText().toString();


        if (TextUtils.isEmpty(name)) {
            binding.registerAdminEtName.setError("Please Enter Name");
            binding.registerAdminEtName.requestFocus();
            return;
        } else if (TextUtils.isEmpty(email)) {
            binding.registerAdminEtEmail.setError("Please Enter E-mail");
            binding.registerAdminEtEmail.requestFocus();
            return;
        } else if (TextUtils.isEmpty(password)) {
            binding.registerAdminEtPassword.setError("Please Enter Password");
            binding.registerAdminEtPassword.requestFocus();
            return;
        } else if (password.length() < 6) {
            binding.registerAdminEtPassword.setError("Password length at least 6 characters");
            binding.registerAdminEtPassword.setFocusable(true);
        } else if (TextUtils.isEmpty(re_password)) {
            binding.registerAdminEtRePassword.setError("Please Enter Re-Password");
            binding.registerAdminEtRePassword.requestFocus();
            return;
        } else if (!password.equals(re_password)) {
            binding.registerAdminEtRePassword.setError("Please Enter The Same Password");
            binding.registerAdminEtRePassword.requestFocus();
            return;
        } else if (TextUtils.isEmpty(phone)) {
            binding.registerAdminEtPhone.setError("Please Enter Phone");
            binding.registerAdminEtPhone.requestFocus();
            return;
        } else if (phone.length() != 10) {
            binding.registerAdminEtPhone.setError("Please Your Number must be 10 Number's");
            return;
        } else if (TextUtils.isEmpty(address)) {
            binding.registerAdminEtAddress.setError("Please Enter Address");
            binding.registerAdminEtAddress.requestFocus();
            return;
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(UserRegisterActivity.this, "Registered " + user.getEmail(), Toast.LENGTH_LONG).show();
                                User entity = new User(1, name, email, password, address, phone, image, lat, log);
                                db.collection(Entitys_COLLECTION).add(entity).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        binding.mainProgressBar.setVisibility(View.GONE);
                                        Toast.makeText(UserRegisterActivity.this, "Register Successfully", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getBaseContext(), UeLoginActivity.class);
                                        intent.putExtra(EMAIL_KEY, email);
                                        intent.putExtra(PASSWORD_KEY, password);
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UserRegisterActivity.this, "Register Failure", Toast.LENGTH_LONG).show();
                                    }
                                });
                                binding.mainProgressBar.setVisibility(View.GONE);
                                binding.registerAdminBtnSave.setEnabled(true);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(UserRegisterActivity.this, "Authentication failed.", Toast.LENGTH_LONG).show();
                                binding.mainProgressBar.setVisibility(View.GONE);
                                binding.registerAdminBtnSave.setEnabled(true);
                            }
                        }
                    });
        }

        //----------------------------------------------------------------------------------------



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PIK_IMAGE_REQ_CODE && resultCode == RESULT_OK && data != null) {
            images_uri = data.getData();
            binding.registerAdminIv.setImageURI(images_uri);
        }
        if (requestCode == MAPS_ENTITY_REQ_CODE && resultCode == RESULT_OK && data != null) {
            lat = data.getDoubleExtra(MapsActivity.MAP_LAT_ENTITY_KEY, 0);
            log = data.getDoubleExtra(MapsActivity.MAP_LNG_ENTITY_KEY, 0);
        }
    }

}