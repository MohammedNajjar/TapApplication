package com.example.tabapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;


import com.example.tabapplication.Adapter.AdapterEntity;
import com.example.tabapplication.Adapter.CategoryTabsAdapter;
import com.example.tabapplication.databinding.ActivityCategoriesBinding;
import com.example.tabapplication.databinding.ActivityViewBinding;
import com.example.tabapplication.model.Categories;
import com.example.tabapplication.model.Order;
import com.example.tabapplication.model.UseRestaurant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {
    public static final String NUM ="NumberChair" ;
    public static final String STATUS ="Status" ;
    public static final String PARK ="Park" ;
    public static final String WIFI ="Wifi" ;

    ActivityViewBinding binding;

    FirebaseFirestore db;
    public static final String ORDERS_COLLECTION = "Orders";
    String num, status, wifi, park;
    String em;


    private static final String CATEGORY_COLLECTION = "Categories";
    private static final String DETAILS_COLLECTION = "Details";
    public static final String Entitys_COLLECTION = "Entitys";
    SharedPreferences.Editor editor;
    public final static String SP_NAME = "accountview";
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        sp= getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
//        sp = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit();

        db.collection(Entitys_COLLECTION).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()) {
                    ArrayList<UseRestaurant> wallPapers = new ArrayList<>();
                    for (QueryDocumentSnapshot q : value) {
                        UseRestaurant wallPaper = q.toObject(UseRestaurant.class);
                        wallPapers.add(wallPaper);
                    }
                    getDataFromFirebaseEntitys_COLLECTION();
                }
                binding.fragmentPb.setVisibility(View.GONE);
            }
        });


    }

    private void getDataFromFirebaseEntitys_COLLECTION() {
        db.collection(Entitys_COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<UseRestaurant> entities = new ArrayList<>();
                for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                    UseRestaurant ent = q.toObject(UseRestaurant.class);
                    entities.add(ent);
                }
                populateEntityInList(entities);
            }
        });
    }

    private void loadDataOrder() {
        db.collection(ORDERS_COLLECTION).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {
                    Order order = new Order();
                    for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                        Order orders = q.toObject(Order.class);
                        order = orders;
//                        num = order.getNumOfChairs();
//                        status = order.getStatus();
//                        wifi = order.getStatusWIFI();
//                        park = order.getStatusPARKING();


                    }


                }
            }
        });
    }

    private void populateEntityInList(ArrayList<UseRestaurant> entities) {
        AdapterEntity adapterEntity = new AdapterEntity(entities, new AdapterEntity.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int id) {
                db.collection(Entitys_COLLECTION).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                            Toast.makeText(getBaseContext(), "The details ", Toast.LENGTH_SHORT).show();
                            loadDataOrder();
                            Intent intent = new Intent(getBaseContext(), CategoriesActivity.class);
                            intent.putExtra("entTitle", entities.get(id).getTitle());
                            intent.putExtra("entEmail", entities.get(id).getEmail());
                            intent.putExtra("entimage", entities.get(id).getUrl_logo());
                            intent.putExtra("entid", entities.get(id).getId());
                            em = entities.get(id).getEmail();
                            test();
                            startActivity(intent);
                        } else {
                            Toast.makeText(getBaseContext(), "Error Other Shops", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });
//
        binding.fragmentRv.setAdapter(adapterEntity);
        binding.fragmentRv.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        binding.fragmentRv.setHasFixedSize(true);
    }

    private void test() {
        db.collection("Orders").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                ArrayList<Order> orders = new ArrayList<>();
                for (QueryDocumentSnapshot q : value) {
                    Order oi = q.toObject(Order.class);
                    num = oi.getNumOfChairs();
                    status = oi.getStatus();
                    park = oi.getStatusPARKING();
                    wifi = oi.getStatusWIFI();
                    editor.putString(NUM, num);
                    editor.putString(STATUS, status);
                    editor.putString(PARK, park);
                    editor.putString(WIFI, wifi);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shoping_menu, menu);
        MenuItem cart = menu.findItem(R.id.shopping_menu_cart);
        MenuItem setteng = menu.findItem(R.id.shopping_menu_setteng);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shopping_menu_cart:
                Intent intent = new Intent(getBaseContext(), CartActivity.class);
                startActivity(intent);
                break;
            case R.id.shopping_menu_setteng:
                intent = new Intent(getBaseContext(), HomeUserActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}