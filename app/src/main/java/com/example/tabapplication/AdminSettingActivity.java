package com.example.tabapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tabapplication.databinding.ActivityAdminSettingBinding;
import com.example.tabapplication.databinding.ActivityHomeUserBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class AdminSettingActivity extends AppCompatActivity {
ActivityAdminSettingBinding binding;
    private SharedPreferences sp;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminSettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sp = getBaseContext().getSharedPreferences(ULoginActivity.SP_NAME, Context.MODE_PRIVATE);
        name= sp.getString(LoginActivity.ADMINAME, "");


        binding.adminSettingTvName.setText(name);



        binding.adminSettingTvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        binding.adminSettingRumh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        AdminSettingActivity.this, R.style.BottomSheetDialogThem);
                View bottomSheetView = LayoutInflater.from(AdminSettingActivity.this)
                        .inflate(R.layout.bottom_user_rumh_about_us,
                                (LinearLayout) view.findViewById(R.id.bottom_sheet_user));
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });
        binding.adminSettingShaghir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        AdminSettingActivity.this, R.style.BottomSheetDialogThem);
                View bottomSheetView = LayoutInflater.from(AdminSettingActivity.this)
                        .inflate(R.layout.bottom_user_shaghir_about_us,
                                (LinearLayout) view.findViewById(R.id.bottom_sheet_container));
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });
        binding.adminSettingCallMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              gotWatsapp();
            }
        });

    }

//    public void makeCall() {
//
//        PackageManager pm=getPackageManager();
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
//            Toast.makeText(getBaseContext(), "WhatsApp no esta instalado!", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void gotWatsapp() {
        boolean installed = isAppInstalled("com.whatsapp");

        if (installed) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+966 55 663 1223"  ));
            startActivity(intent);
        } else {
            Toast.makeText(AdminSettingActivity.this, "Watsapp is not installed !! ,please install the app first", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isAppInstalled(String s) {
        PackageManager packageManager = AdminSettingActivity.this.getPackageManager();
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
}