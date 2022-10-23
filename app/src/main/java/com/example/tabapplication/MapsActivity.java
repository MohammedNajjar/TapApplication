package com.example.tabapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String MAP_LAT_ENTITY_KEY = "lat";
    public static final String MAP_LNG_ENTITY_KEY = "lng";
    public static final String MAP_LAT_CUSTOMER_KEY = "latt";
    public static final String MAP_LNG_CUSTOMER_KEY = "lngg";
    private GoogleMap mMap;
    private double latitude = 31.5016943;
    private double longitude = 34.4755993;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        intent = getIntent();

//        if (intent != null  ) {
//            latitude = intent.getDoubleExtra(MainFragment.MAP_LAT_CUSTOMER_SHOW_KEY, 31.5016943);
//            longitude = intent.getDoubleExtra(MainFragment.MAP_LOG_CUSTOMER_SHOW_KEY, 34.4755993);
//        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Intent resultIntent = new Intent();
                intent.putExtra(MAP_LAT_ENTITY_KEY, latLng.latitude);
                intent.putExtra(MAP_LNG_ENTITY_KEY, latLng.longitude);
                intent.putExtra(MAP_LAT_CUSTOMER_KEY, latLng.latitude);
                intent.putExtra(MAP_LNG_CUSTOMER_KEY, latLng.longitude);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}