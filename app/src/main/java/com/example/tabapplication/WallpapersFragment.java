package com.example.tabapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tabapplication.Adapter.AdapterEntity;
import com.example.tabapplication.Adapter.WallPaperAdapter;
import com.example.tabapplication.model.UseRestaurant;
import com.example.tabapplication.model.WallPaper;
import com.example.tabapplication.model.reserve;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WallpapersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WallpapersFragment extends Fragment {
    FirebaseFirestore db;
    ProgressBar progressBar;
    private static final String CATEGORY_COLLECTION = "Categories";
    private static final String DETAILS_COLLECTION = "Details";
    public static final String Entitys_COLLECTION = "Entitys";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CAT = "category";
    RecyclerView rv ;
    // TODO: Rename and change types of parameters
    private String category;

    public WallpapersFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static WallpapersFragment newInstance(String category) {
        WallpapersFragment fragment = new WallpapersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CAT, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString(ARG_CAT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_wallpapers, container, false);
        rv = v.findViewById(R.id.fragment_rv);
        progressBar = v.findViewById(R.id.fragment_pb);
        db = FirebaseFirestore.getInstance();
        Toast.makeText(getActivity(), category, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);

        return v;
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.shoping_menu, menu);
        MenuItem cart = menu.findItem(R.id.shopping_menu_cart);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shopping_menu_cart:
//                Intent intent = new Intent(getActivity(),CartActivity.class);
//                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}