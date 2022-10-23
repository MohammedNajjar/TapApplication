package com.example.tabapplication.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tabapplication.LoginActivity;
import com.example.tabapplication.R;
import com.example.tabapplication.ULoginActivity;
import com.example.tabapplication.URegisterActivity;
import com.example.tabapplication.databinding.FragmentRHomeBinding;
import com.example.tabapplication.databinding.FragmentRMoreBinding;
import com.example.tabapplication.model.UseRestaurant;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class RMoreFragment extends Fragment {
    FragmentRMoreBinding binding;
    private FirebaseAuth mAuth;
    private SharedPreferences sp;// declare_auth
    private FirebaseFirestore db;
    private StorageReference storage;
    private static final int REQUEST_CALL = 1;
    String email;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRMoreBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth
        sp = getActivity().getSharedPreferences(ULoginActivity.SP_NAME, Context.MODE_PRIVATE);
        email = sp.getString(ULoginActivity.EMAILUSERCURRENT, "");
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();


        binding.moreTvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();


                Intent intent = new Intent(getActivity(), ULoginActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
        binding.callMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               gotWatsapp();
            }
        });


        binding.shaghir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getContext(), R.style.BottomSheetDialogThem);
                View bottomSheetView = LayoutInflater.from(getContext())
                        .inflate(R.layout.bottom_shaghir_about_us,
                                (LinearLayout) view.findViewById(R.id.bottom_sheet_container));
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        binding.rumh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        getContext(), R.style.BottomSheetDialogThem);
                View bottomSheetView = LayoutInflater.from(getContext())
                        .inflate(R.layout.bottom_rumh_about_us,
                                (LinearLayout) view.findViewById(R.id.bottom_sheet_container));
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

            db.collection(URegisterActivity.Entitys_COLLECTION).whereEqualTo("email", email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots != null) {
                        UseRestaurant entity1 = new UseRestaurant();
                        for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                            UseRestaurant entity = q.toObject(UseRestaurant.class);
                            entity1 = entity;
                        }
                        populateEntityInNavHeader(entity1);
                    }
                }
            });

        return binding.getRoot();


    }
    private void populateEntityInNavHeader(UseRestaurant entity1) {
        // if
        if (sp.getInt("userOrAdmin", 0) == 0) {  //USer
            binding.navHeaderTvName.setText(entity1.getTitle());
            binding.navHeaderTvEmail.setText(entity1.getEmail());
            Picasso.get()
                    .load(entity1.getUrl_logo())
                    .resize(96, 96)
                    .centerCrop()
                    .into(binding.navHeaderIvLogo);
        } else if (sp.getInt("userOrAdmin", 0) == 1) {
            binding.navHeaderTvName.setText("Your Name");
            binding.navHeaderTvEmail.setText("Your Eamil @gmail.com");
            Picasso.get()
                    .load(R.drawable.logo)
                    .resize(96, 96)
                    .centerCrop()
                    .into(binding.navHeaderIvLogo);
        }

    }

//    public void makeCall() {
//
//        PackageManager pm=getActivity().getPackageManager();
//        try {
//
//            String toNumber = "+966 55 663 1223"; // contains spaces.
//            toNumber = toNumber.replace("+", "").replace(" ", "");
//
//            Intent sendIntent = new Intent("android.intent.action.MAIN");
//            sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
//            sendIntent.putExtra(Intent.EXTRA_TEXT, "HOLA!");
//            sendIntent.setAction(Intent.ACTION_SEND);
//            sendIntent.setPackage("com.whatsapp");
//            sendIntent.setType("text/plain");
//            startActivity(sendIntent);
//
//            PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
//
//        } catch (PackageManager.NameNotFoundException e) {
//            Toast.makeText(getActivity(), "WhatsApp no esta instalado!", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void gotWatsapp() {
        boolean installed = isAppInstalled("com.whatsapp");

        if (installed) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+966 55 663 1223"  ));
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "Watsapp is not installed !! ,please install the app first", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isAppInstalled(String s) {
        PackageManager packageManager = getActivity().getPackageManager();
        boolean is_installed;

        try {
            packageManager.getPackageInfo(s, PackageManager.GET_ACTIVITIES);
            is_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            is_installed = false;
            e.printStackTrace();
        }
        return is_installed;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                gotWatsapp();
            } else {
                Toast.makeText(getActivity(), "غير مسموح ...!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}