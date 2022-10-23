package com.example.tabapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tabapplication.Adapter.CategoryAdapter;
import com.example.tabapplication.databinding.ActivityAddSubCategoryBinding;
import com.example.tabapplication.model.Categories;
import com.example.tabapplication.model.WallPaper;
import com.example.tabapplication.model.reserve;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class AddSubCategoryActivity extends AppCompatActivity {
    ActivityAddSubCategoryBinding binding;

    Spinner spinner;
    FirebaseFirestore db;
    private static final String CATEGORY_COLLECTION = "Categories";
    private static final String DETAILS_COLLECTION = "Details";

    String selectedCategory;
    private static final int PICK_IMAGE = 100;
    private int PIK_IMAGE_ITEM_REQ_CODE = 6;

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddSubCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

//        open = findViewById(R.id.open);
//        upload = findViewById(R.id.upload);
//        imageView = findViewById(R.id.image_view);
//        spinner = findViewById(R.id.spinner);
//        add_btn = findViewById(R.id.addCategoryButton);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = ((CategoryAdapter)parent.getAdapter()).getItemDocID(position);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        binding.addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


        db.collection(CATEGORY_COLLECTION).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null && !value.isEmpty()){
                    ArrayList<Categories> categories = new ArrayList<>();
                    for(QueryDocumentSnapshot q: value){
                        Categories cat = q.toObject(Categories.class);
                        cat.setCategoriesID(q.getId());
                        categories.add(cat);
                    }

                    CategoryAdapter adapter = new CategoryAdapter(categories,getBaseContext());
                    binding.spinner.setAdapter(adapter);

                }
            }
        });

        binding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
                binding.progressBar.setVisibility(View.VISIBLE);

            }
        });



/*

        binding.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openGallery();

            }
        });


        binding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();

            }
        });

        binding.addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getBaseContext(), AddCategoryActivity.class));

            }
        });*/


    }

    private void addItem() {
        if (imageUri != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            final StorageReference mountainImageRef = storageRef.child("imageItem/" + imageUri.getLastPathSegment());
            mountainImageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mountainImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    uploadDetails(uri.toString());
                                }
                            });
                        }
                    });
        }
        else {
            Toast.makeText(this, "Pleas Enter Image Item", Toast.LENGTH_SHORT).show();

        }
    }

    private void uploadDetails(String image){
        String addName = binding.etTitleID.getText().toString();


        if (TextUtils.isEmpty(addName)) {
            binding.etTitleID.setError("please Enter Name");
            return;
        }
        String docId = db.collection(DETAILS_COLLECTION).document().getId();
        String cat = ((Categories)binding.spinner.getSelectedItem()).getTitle();
        reserve d1 = new reserve(image,docId,addName,cat);

        db.collection(DETAILS_COLLECTION).document(docId).set(d1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddSubCategoryActivity.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PIK_IMAGE_ITEM_REQ_CODE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            binding.imageView.setImageURI(imageUri);
        }
    }
/*
    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");
        startActivityForResult(gallery, PICK_IMAGE);
    }

    private void uploadImage(){

        if(imageUri != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/"+ UUID.randomUUID().toString());
            ref.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    String cateogery = selectedCategory;
                                    ArrayList<String> tags = null;
                                    WallPaper wallPaper = new WallPaper(cateogery,imageUrl,tags);
                                    FirebaseFirestore.getInstance().collection("Wallpapers")
                                            .add(wallPaper).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(AddSubCategoryActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                            Toast.makeText(AddSubCategoryActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddSubCategoryActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }*/
}
