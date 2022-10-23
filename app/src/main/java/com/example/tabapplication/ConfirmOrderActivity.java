package com.example.tabapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.tabapplication.Adapter.AdapterCitySp;
import com.example.tabapplication.databinding.ActivityConfirmOrderBinding;
import com.example.tabapplication.model.City;
import com.example.tabapplication.model.Order;
import com.example.tabapplication.model.OrderItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ConfirmOrderActivity extends AppCompatActivity {
    private static final String ORDERITEM_COLLECTION = "Orders";
    private static final String CITY_COLLECTION = "Cities";
    ActivityConfirmOrderBinding binding;
    FirebaseFirestore db;
    ArrayList<OrderItem> orderItems;
    public static final int MAPS_CUSTOMER_REQ_CODE = 3;

    double lat, log;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.setTitle("Confirm Order");
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        binding.confirmEtDate.setText(currentDate);
        binding.confirmEtTime.setText(currentTime);

        // GET city from firebase to spinner
//        getDataFromFirebaseCITY_CCOLLECTION();

//        binding.confirmSpCity.setSelection(1);
//        City selectedCity = (City) binding.confirmSpCity.getSelectedItem();
//        DetermineDeliveryPrice(selectedCity);

        getfromfirebaseorderitemcart();

//        binding.confirmTvImageLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getBaseContext(), MapsActivity.class);
//                startActivityForResult(intent,MAPS_CUSTOMER_REQ_CODE);
//            }
//        });

        binding.confirmBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmInformationOrder();
            }
        });

    }

    // Determine the delivery price .
//    private void DetermineDeliveryPrice(City selectedCity) {
//        switch (selectedCity.getId()) {
//            case 2:
//                binding.confirmTvDeliveryCost.setText(String.valueOf(selectedCity.getDeliveryPrice()));
//                break;
//            default:
//                binding.confirmTvDeliveryCost.setText(String.valueOf(10));
//                break;
//        }
//    }


    // GET city from firebase to spinner
//    private void getDataFromFirebaseCITY_CCOLLECTION() {
//        db.collection(CITY_COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                ArrayList<City> cities = new ArrayList<>();
//                for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
//                    City city = q.toObject(City.class);
//                    cities.add(city);
//                }
////                populateCitysInSpinner(cities);
//            }
//        });
//    }
//    private void populateCitysInSpinner(ArrayList<City> cities) {
//        AdapterCitySp adapterCitySp = new AdapterCitySp(cities);
//        binding.confirmSpCity.setAdapter(adapterCitySp);
//    }


    //
    private void getfromfirebaseorderitemcart() {
        //orderItems = CartActivity.orderItems;
        db.collection(CategoriesActivity.ORDERITEM_CART_COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                orderItems = new ArrayList<>();
                for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                    OrderItem oi = q.toObject(OrderItem.class);
                    orderItems.add(oi);
                }
            }
        });

    }

    private void confirmInformationOrder() {
//        String name = binding.confirmEtNameOwnerOrder.getText().toString();
//        String phone = binding.confirmEtPhone.getText().toString();
//        String address = binding.confirmEtAddress.getText().toString();
        String Total = binding.confirmTvTotalAmount.getText().toString();
//        if (TextUtils.isEmpty(name)) {
//            binding.confirmEtNameOwnerOrder.setError("Please Enter Name");
//            binding.confirmEtNameOwnerOrder.requestFocus();
//            return;
//        } else if (TextUtils.isEmpty(phone)) {
//            binding.confirmEtPhone.setError("Please Enter Phone");
//            binding.confirmEtPhone.requestFocus();
//            return;
//        } else if (phone.length() != 10) {
//            binding.confirmEtPhone.setError("Please Your Number must be 10 Number's");
//            return;
//        } else if (TextUtils.isEmpty(address)) {
//            binding.confirmEtAddress.setError("Please Enter address");
//            binding.confirmEtAddress.requestFocus();
//            return;
//        } else {
            String docID = db.collection(ORDERITEM_COLLECTION).document().getId();
            // lat log ???   status ??  address spiner
            Order order = new Order(lat, log, docID, 1, orderItems, "Individually","WIFI","Parking", "", "", "",0,null,Total,"");

            db.collection(ORDERITEM_COLLECTION).document(docID).set(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getBaseContext(), "Added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ConfirmOrderActivity.this, "Added Failure", Toast.LENGTH_SHORT).show();
                }
            });
        }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAPS_CUSTOMER_REQ_CODE && resultCode == RESULT_OK && data != null) {
            lat = data.getDoubleExtra(MapsActivity.MAP_LAT_CUSTOMER_KEY, 0);
            log = data.getDoubleExtra(MapsActivity.MAP_LNG_CUSTOMER_KEY, 0);
        }
    }

}