package com.example.tabapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tabapplication.Adapter.AdapterCategories;
import com.example.tabapplication.Adapter.AdapterItemShopping;
import com.example.tabapplication.databinding.ActivityCategoriesBinding;
import com.example.tabapplication.model.Categories;
import com.example.tabapplication.model.CategoryMenu;
import com.example.tabapplication.model.Item;
import com.example.tabapplication.model.Order;
import com.example.tabapplication.model.OrderItem;
import com.example.tabapplication.model.UseRestaurant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoriesActivity extends AppCompatActivity {
    public static final String ORDERITEM_CART_COLLECTION = "OorderItem";
    ActivityCategoriesBinding binding;
    public static final String Categories_COLLECTION = "CategoryMenu";
    public static final String Entitys_COLLECTION = "Entitys";
    private static final String ITEMS_COLLECTION = "Items";
    private FirebaseFirestore db;
    private int entityId;
    public static ArrayList<OrderItem> orderItems ;
    private int orderId;
    String title,image;
    String numch,status,wifi,park;
    String email;
    public static final String ORDERS_COLLECTION = "Orders";
    SharedPreferences sp;
    int numchair=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.setTitle("Categories");
        db = FirebaseFirestore.getInstance();
        sp = CategoriesActivity.this.getSharedPreferences(ViewActivity.SP_NAME, Context.MODE_PRIVATE);
//       numch = sp.getString(ViewActivity.NUM, "");
//       wifi = sp.getString(ViewActivity.WIFI, "");
//       park = sp.getString(ViewActivity.PARK, "");
//       status = sp.getString(ViewActivity.STATUS, "");



        Intent intent = getIntent();
        entityId = intent.getIntExtra("entid", 0);
        title=intent.getStringExtra("entTitle");
        image=intent.getStringExtra("entimage");
        email=intent.getStringExtra("entEmail");

        binding.categoriesTvTitle.setText(title);
        Glide.with(this).load(image).into(binding.categoriesIvEntity);



        loadIbformationEntity(entityId);// get data Entity
        loadIbformationOrder();


        binding.categoryBtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.categryCard.setVisibility(View.VISIBLE);
                binding.categoriesRvItem.setVisibility(View.VISIBLE);
                getDataFromFirebaseCategories_COLLECTION();
            }
        });
        binding.categoryBtnChair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               binding.categoryLinear.setVisibility(View.VISIBLE);
               binding.categryCard.setVisibility(View.INVISIBLE);
               binding.categoriesRvItem.setVisibility(View.INVISIBLE);

            }
        });
        binding.categoryTvAddChair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numchair = numchair+ 1;
                binding.categoryTvNumChairs.setText(String.valueOf(numchair));
            }
        });
        binding.categoryTvSubChair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numchair =numchair - 1;
                binding.categoryTvNumChairs.setText(String.valueOf(numchair));

            }
        });



// get data Categories

    }
    // items
    private void getDataFromFirebaseITEMS_COLLECTION(String name) {
        db.collection(ITEMS_COLLECTION).whereEqualTo("catg_id", name).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {
                    ArrayList<Item> items = new ArrayList<>();
                    for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                        Item it = q.toObject(Item.class);
                        items.add(it);
                    }
                    populateItemInRV(items);
                }
            }
        });
    }
    private void loadIbformationOrder() {
        db.collection(ORDERS_COLLECTION).whereEqualTo("email",email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {
//                    Order order = new Order();
                    for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                        Order orders = q.toObject(Order.class);
//                        order = orders;
                       numch= orders.getNumOfChairs();
                       wifi=orders.getStatusWIFI();
                       park=orders.getStatusPARKING();
                       status=orders.getStatus();




                    }
                    populateOrderInHeader();


                }
            }
        });
    }

    private void populateOrderInHeader() {

        binding.tvNumch.setText(numch);
        binding.tvWifi.setText(wifi);
        binding.tvPar.setText(park);
        binding.tvFam.setText(status);
    }

    private void populateItemInRV(ArrayList<Item> items) {
        //----- test --
        AdapterItemShopping adapterItemShopping = new AdapterItemShopping(items, new AdapterItemShopping.OnRecyclerViewItemClickListener() {
            @Override
            public void onBtnItemClick(Item item, int num, int chair) {
                // put order item at firebase (collection = "orderItem")
                OrderItem orderItem = new OrderItem(1, item, num,numchair);
                // test -------
//                orderItems.add(orderItem);
//                Intent intent = new Intent(getBaseContext(),CartActivity.class);
//                intent.putExtra("orderItems",orderItems);
//                startActivity(intent);
                //----------------------------------------
                // orderItems.add(orderItem);
                //  String docID = db.collection(ORDERITEM_CART_COLLECTION).document().getId();
                db.collection(ORDERITEM_CART_COLLECTION).add(orderItem).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getBaseContext(), "Added To Cart", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getBaseContext(), "Added Failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        binding.categoriesRvItem.setAdapter(adapterItemShopping);
        binding.categoriesRvItem.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        binding.categoriesRvItem.setHasFixedSize(true);

    }





    // Categories
    private void getDataFromFirebaseCategories_COLLECTION() {
        db.collection(Categories_COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<CategoryMenu> categoriess = new ArrayList<>();
                for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                    CategoryMenu catt = q.toObject(CategoryMenu.class);
                    categoriess.add(catt);
                }
                populateCategoriesInList(categoriess);
            }
        });
    }
    private void populateCategoriesInList(ArrayList<CategoryMenu> categoriess) {
        AdapterCategories adapterCategories = new AdapterCategories(categoriess, new AdapterCategories.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(CategoryMenu categories) {
                getDataFromFirebaseITEMS_COLLECTION(categories.getName());
            }
        });
        binding.categoriesRvCategory.setAdapter(adapterCategories);
        binding.categoriesRvCategory.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.categoriesRvCategory.setHasFixedSize(true);
    }



    private void loadIbformationEntity(int entityId) {
        db.collection(Entitys_COLLECTION).whereEqualTo("id", entityId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {
                    UseRestaurant entity1 = new UseRestaurant();



                    for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                        UseRestaurant entity = q.toObject(UseRestaurant.class);
                        entity1 = entity;
                    }
//                    populateEntityInHeader(entity1);
                }
            }
        });
    }

//    private void populateEntityInHeader(UseRestaurant entity1) {
//
//        Picasso.get()
//                .load(entity1.getUrl_logo())
//                .resize(96, 96)
//                .centerCrop()
//                .into(binding.categoriesIvEntity);
//    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shoping_menu, menu);
        MenuItem cart = menu.findItem(R.id.shopping_menu_cart);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shopping_menu_cart:
                Intent intent = new Intent(getBaseContext(),CartActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}