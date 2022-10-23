package com.example.tabapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tabapplication.databinding.ActivityCartBinding;
import com.example.tabapplication.model.OrderItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    ActivityCartBinding binding;
    FirebaseFirestore db;
    public static ArrayList<OrderItem> orderItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.setTitle("Cart list");
        db = FirebaseFirestore.getInstance();
        getDataFromFirebaseORDERITEM_CART_COLLECTION();
        // test -----------------------------------
//        Intent intent = getIntent();
//        orderItems =  intent.getParcelableArrayListExtra("orderItems");
//        AdapterItemCart adapterItemCart = new AdapterItemCart((List<OrderItem>) orderItems, new AdapterItemCart.OnButtonCancelItemClickListener() {
//            @Override
//            public void onBtnItemClick(OrderItem orderItem) {
//
//            }
//        });
//        binding.cartRvItemShoppint.setAdapter(adapterItemCart);
//        binding.cartRvItemShoppint.setHasFixedSize(true);
//        binding.cartRvItemShoppint.setLayoutManager(new LinearLayoutManager(this));
        //-----------------------------


        binding.cartBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ConfirmOrderActivity.class);
                startActivity(intent);
            }
        });

    }


    // show order item in rv and if you delete any order item .
    private void getDataFromFirebaseORDERITEM_CART_COLLECTION() {
        db.collection(CategoriesActivity.ORDERITEM_CART_COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                orderItems = new ArrayList<>();
                for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                    OrderItem oi = q.toObject(OrderItem.class);
                    orderItems.add(oi);
                }
                populateOrderItemCartInList(orderItems);
            }
        });
    }

    private void populateOrderItemCartInList(ArrayList<OrderItem> orderItems) {
        AdapterItemCart adapterItemCart = new AdapterItemCart(orderItems, new AdapterItemCart.OnButtonCancelItemClickListener() {
            @Override
            public void onBtnItemClick(OrderItem orderItem) {
                // deleted order item from cart
                // i want defend String id to delete document //hint you shoud insert string id in order item class and deleted order .....


                db.collection(CategoriesActivity.ORDERITEM_CART_COLLECTION).document(String.valueOf(orderItem.getId()))
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CartActivity.this);
                                builder.setTitle("Are you sure you want to delete the order?");
                                builder.setMessage("Deleted Order can't be undo")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                db.collection(CategoriesActivity.ORDERITEM_CART_COLLECTION).document(String.valueOf(orderItem.getId()))
                                                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(CartActivity.this, "Order deleted", Toast.LENGTH_LONG).show();
                                                            finish();

                                                        }
                                                    }
                                                });
                                            }
                                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CartActivity.this, "Delete Failure ", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        binding.cartRvItemShoppint.setAdapter(adapterItemCart);
        binding.cartRvItemShoppint.setHasFixedSize(true);
        binding.cartRvItemShoppint.setLayoutManager(new LinearLayoutManager(this));
    }


}