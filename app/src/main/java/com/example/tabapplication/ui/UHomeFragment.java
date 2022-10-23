package com.example.tabapplication.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tabapplication.Adapter.AdapterEntity;
import com.example.tabapplication.CartActivity;
import com.example.tabapplication.CategoriesActivity;
import com.example.tabapplication.R;
import com.example.tabapplication.databinding.FragmentROrderBinding;
import com.example.tabapplication.databinding.FragmentUHomeBinding;
import com.example.tabapplication.model.UseRestaurant;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UHomeFragment extends Fragment {

FragmentUHomeBinding binding;

    FirebaseFirestore db;


    private static final String CATEGORY_COLLECTION = "Categories";
    private static final String DETAILS_COLLECTION = "Details";
    public static final String Entitys_COLLECTION = "Entitys";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUHomeBinding.inflate(inflater, container, false);

        db = FirebaseFirestore.getInstance();

        db.collection(Entitys_COLLECTION).addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(!value.isEmpty()){
                    ArrayList<UseRestaurant> wallPapers = new ArrayList<>();
                    for(QueryDocumentSnapshot q: value){
                        UseRestaurant wallPaper = q.toObject(UseRestaurant.class);
                        wallPapers.add(wallPaper);
                    }
                    getDataFromFirebaseEntitys_COLLECTION();
                }
                binding.fragmentPb.setVisibility(View.GONE);
            }
        });



        return binding.getRoot();

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
    private void populateEntityInList(ArrayList<UseRestaurant> entities) {
        AdapterEntity adapterEntity = new AdapterEntity(entities, new AdapterEntity.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int id) {
                db.collection(Entitys_COLLECTION).whereEqualTo("id", entities.get(id) ).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                            Toast.makeText(getActivity(), "Other Shops", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), CategoriesActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "Error Other Shops", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
        binding.fragmentRv.setAdapter(adapterEntity);
        binding.fragmentRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.fragmentRv.setHasFixedSize(true);
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.shoping_menu, menu);
        MenuItem cart = menu.findItem(R.id.shopping_menu_cart);
        cart.setVisible(true);

        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shopping_menu_cart:
                Intent intent = new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}