package com.example.tabapplication.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tabapplication.Adapter.AdapterCategorySp;
import com.example.tabapplication.Adapter.AdapterItem;
import com.example.tabapplication.R;

import com.example.tabapplication.databinding.FragmentRItemBinding;
import com.example.tabapplication.model.CategoryMenu;
import com.example.tabapplication.model.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
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


public class RItemFragment extends Fragment {
    FragmentRItemBinding binding;
    private static final String ITEMS_COLLECTION = "Items";
    private static final String Category_Menu_COLLECTION = "CategoryMenu";
    private int PIK_IMAGE_ITEM_REQ_CODE = 6;
    Uri images_uri;
    FirebaseFirestore db;
    StorageReference storage;
    public static final int RESULT_OK = -1;
    ArrayList<Item> items = new ArrayList<>() ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_r_item, container, false);

        binding = FragmentRItemBinding.inflate(inflater, container, false);
        //this.setTitle("Add New Item");
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        getDataFromFirebaseCATEGRY_CCOLLECTION();   // Method --> Fill the spinner through the firebase

        // photo from gallery
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

        // Button Show
        binding.addNewItemBtnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.addNewItemBtnShow.setEnabled(false);
                binding.addNewItemBtnAdd.setEnabled(false);
                binding.progressBar2.setVisibility(View.VISIBLE);
                getDataFromFirebaseITEMS_COLLECTION();
            }
        });

        // Button Add
        binding.addNewItemBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.addNewItemBtnShow.setEnabled(false);
                binding.addNewItemBtnAdd.setEnabled(false);
                binding.progressBar2.setVisibility(View.VISIBLE);
                addItem();
            }
        });

        return binding.getRoot();

    }

    private void getDataFromFirebaseCATEGRY_CCOLLECTION() {
        db.collection(Category_Menu_COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<CategoryMenu> categs = new ArrayList<>();
                for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                    CategoryMenu cat = q.toObject(CategoryMenu.class);
                    categs.add(cat);
                }
                populateItemsInList(categs);
            }
        });
    }
    private void populateItemsInList(ArrayList<CategoryMenu> categs) {
        AdapterCategorySp adapterCategorySp = new AdapterCategorySp(categs);
        binding.addNewItemSpinnerCategoryItem.setAdapter(adapterCategorySp);
    }

    private void getDataFromFirebaseITEMS_COLLECTION() {
        db.collection(ITEMS_COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots != null){
                    ArrayList<Item> items = new ArrayList<>();
                    for(QueryDocumentSnapshot q : queryDocumentSnapshots){
                        Item it =q.toObject(Item.class);
                        items.add(it);
                    }
                    populateItemInList(items);
                }
            }
        });
    }
    private void populateItemInList(ArrayList<Item> items) {
        AdapterItem adapterItem = new AdapterItem(items);
        binding.rvAllItem.setAdapter(adapterItem);
        binding.rvAllItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvAllItem.setHasFixedSize(true);
        binding.addNewItemBtnShow.setEnabled(true);
        binding.addNewItemBtnAdd.setEnabled(true);
        binding.progressBar2.setVisibility(View.GONE);
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
            Toast.makeText(getActivity(), "Pleas Enter Image Item", Toast.LENGTH_SHORT).show();
            binding.addNewItemBtnShow.setEnabled(true);
            binding.addNewItemBtnAdd.setEnabled(true);
            binding.progressBar2.setVisibility(View.GONE);
        }
    }
    private void addItemToFirebase(String image) {
        String nameItem = binding.addNewItemEtNameItem.getText().toString();
        if (TextUtils.isEmpty(nameItem)) {
            binding.addNewItemEtNameItem.setError("please Enter Name  Item");
            return;
        }
        String priceItem = binding.addNewItemEtPrice.getText().toString();
        if (TextUtils.isEmpty(priceItem)) {
            binding.addNewItemEtPrice.setError("please Enter Price  Item");
            return;
        }
        String descriptionItem = binding.addNewItemEtDescription.getText().toString();

        //------------------
        CategoryMenu selectedCategory = (CategoryMenu) binding.addNewItemSpinnerCategoryItem.getSelectedItem();

        Item item = new Item(1,nameItem,Integer.parseInt(priceItem),image,descriptionItem,selectedCategory.getName());
        items.add(item);
        populateItemsIntoRV();
        db.collection(ITEMS_COLLECTION).add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getActivity(), "Added Item Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Added Item Failure", Toast.LENGTH_SHORT).show();
            }
        });
        binding.addNewItemBtnShow.setEnabled(true);
        binding.addNewItemBtnAdd.setEnabled(true);
        binding.progressBar2.setVisibility(View.GONE);
    }
    private void populateItemsIntoRV() {
        AdapterItem adapterItem = new AdapterItem(items);
        binding.rvAllItem.setAdapter(adapterItem);
        binding.rvAllItem.setHasFixedSize(true);
        binding.rvAllItem.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PIK_IMAGE_ITEM_REQ_CODE && resultCode == RESULT_OK && data != null) {
            images_uri = data.getData();
            binding.addNewItemIvItem.setImageURI(images_uri);
        }
    }

}