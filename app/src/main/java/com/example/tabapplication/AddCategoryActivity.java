package com.example.tabapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.tabapplication.databinding.ActivityAddCategoryBinding;
import com.example.tabapplication.model.Categories;
import com.example.tabapplication.model.CategoryMenu;
import com.example.tabapplication.model.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class AddCategoryActivity extends AppCompatActivity {
    ActivityAddCategoryBinding binding;
    FirebaseFirestore db;
    private int PIK_IMAGE_ITEM_REQ_CODE = 6;
    Uri images_uri;
    ArrayList<CategoryMenu> items = new ArrayList<>() ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        db = FirebaseFirestore.getInstance();

        binding.addNewItemIvItem.setOnClickListener(new View.OnClickListener() {
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
                startActivityForResult(intent, PIK_IMAGE_ITEM_REQ_CODE);
            }
        });



        binding.addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.progressBar2.setVisibility(View.VISIBLE);
                addItem();
            }
        });
    }
    private void addItem() {
        if (images_uri != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            final StorageReference mountainImageRef = storageRef.child("imageItem/" + images_uri.getLastPathSegment());
            mountainImageRef.putFile(images_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mountainImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    addItemToFirebase(uri.toString());
                                }
                            });
                        }
                    });
        }
        else {
            Toast.makeText(getBaseContext(), "Pleas Enter Image Item", Toast.LENGTH_SHORT).show();

            binding.progressBar2.setVisibility(View.GONE);
        }
    }
    private void addItemToFirebase(String image) {
        String nameItem = binding.etNameCategory.getText().toString();
        if (TextUtils.isEmpty(nameItem)) {
            binding.etNameCategory.setError("please Enter Name  Item");
            return;
        }


        //------------------
        String docID = db.collection("CategoryMenu").document().getId();
        CategoryMenu item = new CategoryMenu(1,docID,nameItem,image);
        items.add(item);
       // populateItemsIntoRV();
        db.collection("CategoryMenu").add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getBaseContext(), "Added Item Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(), "Added Item Failure", Toast.LENGTH_SHORT).show();
            }
        });

        binding.progressBar2.setVisibility(View.GONE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PIK_IMAGE_ITEM_REQ_CODE && resultCode == RESULT_OK && data != null) {
            images_uri = data.getData();
            binding.addNewItemIvItem.setImageURI(images_uri);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        MenuItem setteng = menu.findItem(R.id.admin_menu_setteng);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.admin_menu_setteng:
                Intent intent = new Intent(getBaseContext(),AdminSettingActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}