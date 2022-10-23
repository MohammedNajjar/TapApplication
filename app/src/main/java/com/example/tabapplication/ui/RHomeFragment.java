package com.example.tabapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.tabapplication.Adapter.AdapterOrder;
import com.example.tabapplication.MapsActivity;
import com.example.tabapplication.R;
import com.example.tabapplication.RMoreActivity;
import com.example.tabapplication.ULoginActivity;
import com.example.tabapplication.databinding.FragmentRHomeBinding;
import com.example.tabapplication.databinding.FragmentROrderBinding;
import com.example.tabapplication.model.Order;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class RHomeFragment extends Fragment {
    FragmentRHomeBinding binding;
    public static final String MAP_LAT_CUSTOMER_SHOW_KEY = "latshow";
    public static final String MAP_LOG_CUSTOMER_SHOW_KEY = "logshow";
    //private MainViewModel mainViewModel;
    public static final String ORDERS_COLLECTION = "Orders";
    FirebaseFirestore db;
    SharedPreferences sp;
    String email,s;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRHomeBinding.inflate(inflater, container, false);

        db= FirebaseFirestore.getInstance();
        getDataFromFirebaseORDERS_COLLECTION();         // get data



        return binding.getRoot();

    }
    private void getDataFromFirebaseORDERS_COLLECTION() {
//        db.collection(ORDERS_COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                ArrayList<Order> orders = new ArrayList<>();
//                for(QueryDocumentSnapshot q : queryDocumentSnapshots){
//                    Order oi =q.toObject(Order.class);
//                    orders.add(oi);
//                }
//                populateOrderInList(orders);
//            }
//        });
        sp = getActivity().getSharedPreferences(ULoginActivity.SP_NAME, Context.MODE_PRIVATE);
        email = sp.getString(ULoginActivity.EMAILUSERCURRENT, "");

        db.collection(ORDERS_COLLECTION).whereEqualTo("email", email).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                ArrayList<Order> orders = new ArrayList<>();
                for(QueryDocumentSnapshot q : queryDocumentSnapshots){
                    Order oi =q.toObject(Order.class);
                    orders.add(oi);
                }
                populateOrderInList(orders);

            }
        });
    }
    //????????????????????
    //????????????????????????????

    private void populateOrderInList(ArrayList<Order> orders) {
        AdapterOrder adapterOrder = new AdapterOrder(orders, new AdapterOrder.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(Order order) {



                Intent intent = new Intent(getActivity(),  RMoreActivity.class);
                intent.putExtra("oid", order.getId());
                startActivity(intent);
            }
        }, new AdapterOrder.OnClickLocationListener() {
            @Override
            public void OnClickLocation(double lat, double lng) {
                Intent intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra(MAP_LAT_CUSTOMER_SHOW_KEY,lat);
                intent.putExtra(MAP_LOG_CUSTOMER_SHOW_KEY,lng);
                startActivity(intent);
            }
        });

        binding.fragmentViewOrdersRv.setAdapter(adapterOrder);
        binding.fragmentViewOrdersRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.fragmentViewOrdersRv.setHasFixedSize(true);
    }

}