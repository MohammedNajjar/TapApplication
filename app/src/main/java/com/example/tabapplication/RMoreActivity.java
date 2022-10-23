package com.example.tabapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.tabapplication.Adapter.AdapterItemSp;
import com.example.tabapplication.Adapter.AdapterOrderItem;
import com.example.tabapplication.databinding.ActivityMainBinding;
import com.example.tabapplication.databinding.ActivityRmoreBinding;
import com.example.tabapplication.model.Item;
import com.example.tabapplication.model.Order;
import com.example.tabapplication.model.OrderItem;
import com.example.tabapplication.model.UseRestaurant;
import com.example.tabapplication.ui.ROrderFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RMoreActivity extends AppCompatActivity {
    Order currentOrder;
    public static final int MAPS_CUSTOMER_REQ_CODE = 3;
    private static String Item_COLLECTION = "Items";
    public static final String ORDERS_COLLECTION = "Orders";
    private static final String CATEGORY_COLLECTION = "Categories";

    private String orderId;
    FirebaseFirestore db;
    ArrayList<OrderItem> orderItems = new ArrayList<>();
    double lat, log;
    private SharedPreferences sp;
    public static final int RESULT_OK = -1;
  String email;

ActivityRmoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRmoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.setTitle("Add New Order");
        db = FirebaseFirestore.getInstance();
//        sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp = RMoreActivity.this.getSharedPreferences(ULoginActivity.SP_NAME, Context.MODE_PRIVATE);
        email = sp.getString(ULoginActivity.EMAILUSERCURRENT, "");


        if (sp.getInt("userOrAdmin", 0) == 0) {
            // binding.addNewOrderRadioGroup.setVisibility(View.GONE);
            binding.addNewOrderRadioGroup.setVisibility(View.VISIBLE);
        } else if (sp.getInt("userOrAdmin", 0) == 1) {
            binding.addNewOrderRadioGroup.setVisibility(View.VISIBLE);
        }
        setSupportActionBar(binding.detailsToolbar);
        getDataFromFirebaseITEM_CCOLLECTION();   // Method --> Fill the spinner through the firebase
        Intent intent = getIntent();   // Receive from click at recycler view in main
        orderId = intent.getStringExtra("oid");
        if (orderId == null) {    // new Order
            enableFields();
            clearFields();
        } else {   // show Order
            disableFields();
            db.collection(ORDERS_COLLECTION).whereEqualTo("id", orderId).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                        Order oi = queryDocumentSnapshots.getDocuments().get(0).toObject(Order.class);
                        if (oi != null) {
                            fillOrderItemToFields(oi);
                        }
                    }
                }
            });
        }

        binding.addNewOrderTvImageLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                startActivityForResult(intent, MAPS_CUSTOMER_REQ_CODE);
            }
        });

        binding.addNewOrderBtnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = (binding.customAddNewItemEtNumItem.getText().toString());
                if (TextUtils.isEmpty(number)) {
                    binding.customAddNewItemEtNumItem.setError("please Enter Number Item");
                    return;
                }
                Item selectedItem = (Item) binding.addNewOrderSpinnerItem.getSelectedItem();
                OrderItem oi = new OrderItem(1, selectedItem, Integer.parseInt(number));
                orderItems.add(oi);

                populateOrderItemsIntoRV();
            }
        });
        // The save button is canceled (Cancel)
        // Replaced with a button in the menu

    }


    private void populateOrderItemsIntoRV() {
        AdapterOrderItem adapterOrderItem;
        if (currentOrder == null)
            adapterOrderItem = new AdapterOrderItem(orderItems);
        else
            adapterOrderItem = new AdapterOrderItem(currentOrder.getOrderItems());
        binding.addNewOrderRv.setAdapter(adapterOrderItem);
        binding.addNewOrderRv.setHasFixedSize(true);
        binding.addNewOrderRv.setLayoutManager(new LinearLayoutManager(getBaseContext()));
    }

    private void getDataFromFirebaseITEM_CCOLLECTION() {
        db.collection(Item_COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<Item> items = new ArrayList<>();
                for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                    Item item = q.toObject(Item.class);
                    items.add(item);
                }
                populateItemsInList(items);
            }
        });
    }

    private void populateItemsInList(ArrayList<Item> items) {
        AdapterItemSp adapterItemSp = new AdapterItemSp(items);
        binding.addNewOrderSpinnerItem.setAdapter(adapterItemSp);
    }

    private void fillOrderItemToFields(Order order) {
        currentOrder = order;
        binding.addNewOrderEtNameOwnerOrder.setText(order.getName_owner_order());
        binding.addNewOrderEtPhone.setText(order.getNumber());
        binding.addNewOrderEtNumch.setText(order.getNumOfChairs());
        binding.addNewOrderEtAddress.setText(order.getAddress());
        populateOrderItemsIntoRV();
        if (order.getStatus().equals("Individually")) {
            binding.addNewOrderRbIndividually.setChecked(true);
        } else if (order.getStatus().equals("Famile")) {
            binding.addNewOrderRbFamile.setChecked(true);
        }


        if (order.getStatusWIFI().equals("WIFI")) {
            binding.addNewOrderRbWifi.setChecked(true);
        } else if (order.getStatusWIFI().equals("NONWifi")) {
            binding.addNewOrderRbNonWifi.setChecked(true);
        }

        if (order.getStatusPARKING().equals("Parking")) {
            binding.addNewOrderRbParking.setChecked(true);
        } else if (order.getStatusPARKING().equals("NONParking")) {
            binding.addNewOrderRbNonParking.setChecked(true);
        }
        orderItems =currentOrder.getOrderItems();
        populateOrderItemsIntoRV();
    }

    private void disableFields() {
        binding.detailsIv.setEnabled(false);
        binding.addNewOrderEtNameOwnerOrder.setEnabled(false);
        binding.addNewOrderEtPhone.setEnabled(false);
        binding.addNewOrderEtNumch.setEnabled(false);
        binding.addNewOrderEtAddress.setEnabled(false);
        binding.addNewOrderSpinnerItem.setEnabled(false);
        binding.customAddNewItemEtNumItem.setEnabled(false);
        binding.addNewOrderBtnAddItem.setEnabled(false);
        binding.addNewOrderBtnSave.setEnabled(false);
        binding.addNewOrderRbFamile.setEnabled(false);
        binding.addNewOrderRbIndividually.setEnabled(false);
        binding.addNewOrderRbWifi.setEnabled(false);
        binding.addNewOrderRbNonWifi.setEnabled(false);
        binding.addNewOrderRbParking.setEnabled(false);
        binding.addNewOrderRbNonParking.setEnabled(false);
        binding.addNewOrderTvImageLocation.setEnabled(false);
    }
    private void enableFields() {
        binding.detailsIv.setEnabled(true);
        binding.addNewOrderEtNameOwnerOrder.setEnabled(true);
        binding.addNewOrderEtPhone.setEnabled(true);
        binding.addNewOrderEtNumch.setEnabled(true);
        binding.addNewOrderEtAddress.setEnabled(true);
        binding.addNewOrderSpinnerItem.setEnabled(true);
        binding.customAddNewItemEtNumItem.setEnabled(true);
        binding.addNewOrderBtnAddItem.setEnabled(true);
        binding.addNewOrderBtnSave.setEnabled(true);
        binding.addNewOrderRbFamile.setEnabled(true);
        binding.addNewOrderRbIndividually.setEnabled(true);
        binding.addNewOrderRbWifi.setEnabled(true);
        binding.addNewOrderRbNonWifi.setEnabled(true);
        binding.addNewOrderRbParking.setEnabled(true);
        binding.addNewOrderRbNonParking.setEnabled(true);
        binding.addNewOrderTvImageLocation.setEnabled(true);
    }
    private void clearFields() {
        binding.detailsIv.setImageURI(null);
        binding.addNewOrderEtNameOwnerOrder.setText("");
        binding.addNewOrderEtPhone.setText("");
        binding.addNewOrderEtNumch.setText("");
        binding.addNewOrderEtAddress.setText("");
        binding.customAddNewItemEtNumItem.setText("");
        binding.addNewOrderRbIndividually.setChecked(false);
        binding.addNewOrderRbFamile.setChecked(false);
        binding.addNewOrderRbWifi.setChecked(false);
        binding.addNewOrderRbNonWifi.setChecked(false);
        binding.addNewOrderRbParking.setChecked(false);
        binding.addNewOrderRbNonParking.setChecked(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_details_menu, menu);
        MenuItem save = menu.findItem(R.id.details_menu_save);
        MenuItem edit = menu.findItem(R.id.details_menu_edit);
        MenuItem delete = menu.findItem(R.id.details_menu_delete);
        if (orderId == null) {  // add
            save.setVisible(true);
            edit.setVisible(false);
            delete.setVisible(false);
        } else {                 // show
            save.setVisible(false);
            edit.setVisible(true);
            delete.setVisible(true);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.details_menu_save:
                String nameOwnerOrder = binding.addNewOrderEtNameOwnerOrder.getText().toString();
                if (TextUtils.isEmpty(nameOwnerOrder)) {
                    binding.addNewOrderEtNameOwnerOrder.setError("please Enter Name Owner Order");
                    break;
                }
                String phone = binding.addNewOrderEtPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    binding.addNewOrderEtPhone.setError("please Enter Phone");
                    break;
                }
                String numch = binding.addNewOrderEtNumch.getText().toString();
                if (TextUtils.isEmpty(numch)) {
                    binding.addNewOrderEtPhone.setError("please Enter number Of Chairs");
                    break;
                }
                String address = binding.addNewOrderEtAddress.getText().toString();
                if (TextUtils.isEmpty(address)) {
                    binding.addNewOrderEtAddress.setError("please Enter Address");
                    break;
                }
                String statusRb_stu = "";
                // int statusRb = 0;
                if (binding.addNewOrderRbIndividually.isChecked()) {
                    statusRb_stu = "Individually";
                } else if (binding.addNewOrderRbFamile.isChecked()) {
                    statusRb_stu = "Famile";
                }

                String statusRb_wifi = "";
                // int statusRb = 0;
                if (binding.addNewOrderRbWifi.isChecked()) {
                    statusRb_wifi = "WIFI";
                } else if (binding.addNewOrderRbNonWifi.isChecked()) {
                    statusRb_wifi = "NONWifi";
                }

                String statusRb_parking = "";
                // int statusRb = 0;
                if (binding.addNewOrderRbParking.isChecked()) {
                    statusRb_parking = "Parking";
                } else if (binding.addNewOrderRbNonParking.isChecked()) {
                    statusRb_parking = "NONParking";
                }



                if (orderId == null) {
                    String docID = db.collection("Orders").document().getId();

                    Order order = new Order(lat, log, docID, 1, orderItems, statusRb_stu,statusRb_wifi,statusRb_parking, nameOwnerOrder, phone, address,0,null,numch,email);
                    db.collection(ORDERS_COLLECTION).document(docID).set(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getBaseContext(), "Added successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getBaseContext(), "Added Failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    currentOrder = new Order(lat, log, currentOrder.getId(), 1, orderItems, statusRb_stu,statusRb_wifi,statusRb_parking, nameOwnerOrder, phone, address,0,null,numch,email);
                    db.collection(ORDERS_COLLECTION).document(currentOrder.getId()).set(currentOrder).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getBaseContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getBaseContext(), "Updated Failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return true;

            case R.id.details_menu_edit:
                enableFields();
                MenuItem save = binding.detailsToolbar.getMenu().findItem(R.id.details_menu_save);
                MenuItem edit = binding.detailsToolbar.getMenu().findItem(R.id.details_menu_edit);
                MenuItem delete = binding.detailsToolbar.getMenu().findItem(R.id.details_menu_delete);
                delete.setVisible(false);
                edit.setVisible(false);
                save.setVisible(true);
                return true;

            case R.id.details_menu_delete:
                if (currentOrder != null) {
                    db.collection(ORDERS_COLLECTION).document(currentOrder.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getBaseContext(), "Delete Success ", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getBaseContext(), "Delete Failure ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                finish();
                return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAPS_CUSTOMER_REQ_CODE && resultCode == RESULT_OK && data != null) {
            lat = data.getDoubleExtra(MapsActivity.MAP_LAT_CUSTOMER_KEY, 0);
            log = data.getDoubleExtra(MapsActivity.MAP_LNG_CUSTOMER_KEY, 0);
        }
    }

}

