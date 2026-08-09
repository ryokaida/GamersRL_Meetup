package com.example.gamersrl_meetup.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gamersrl_meetup.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class MapFragment extends Fragment
        implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(requireActivity());
        db = FirebaseFirestore.getInstance();

        View view = inflater.inflate(
                R.layout.fragment_map,
                container,
                false
        );

        SupportMapFragment mapFragment =
                (SupportMapFragment)
                        getChildFragmentManager()
                                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Push Google Maps controls below the app bar
        mMap.setPadding(0, 120, 0, 0);

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    100
            );

            return;
        }

        mMap.setMyLocationEnabled(true);
        moveCameraToCurrentLocation();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == 100 &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                mMap.setMyLocationEnabled(true);
                moveCameraToCurrentLocation();
            }
        }
    }

    private void moveCameraToCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        saveCurrentLocation(
                                location.getLatitude(),
                                location.getLongitude()
                        );
                        loadNearbyUsers();

                        LatLng currentLocation = new LatLng(
                                location.getLatitude(),
                                location.getLongitude()
                        );

                        mMap.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                        currentLocation,
                                        15f
                                )
                        );
                    }
                });
    }

    private void saveCurrentLocation(double latitude, double longitude) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return;
        }

        String uid = user.getUid();

        Map<String, Object> locationData = new HashMap<>();
        locationData.put("name", user.getEmail());
        locationData.put("latitude", latitude);
        locationData.put("longitude", longitude);
        locationData.put("sharingEnabled", true);

        db.collection("user_locations")
                .document(uid)
                .set(locationData);
    }

    private void loadNearbyUsers() {

        db.collection("user_locations")
                .get()
                .addOnSuccessListener(querySnapshot -> {

                    // Loop through every document
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {

                        // Read the data
                        Double latitude = document.getDouble("latitude");
                        Double longitude = document.getDouble("longitude");
                        Boolean sharing = document.getBoolean("sharingEnabled");
                        String name = document.getString("name");

                        // Only show users sharing location
                        if (sharing != null && sharing
                                && latitude != null
                                && longitude != null) {

                            // Create marker
                            LatLng userLocation =
                                    new LatLng(latitude, longitude);

                            mMap.addMarker(
                                    new MarkerOptions()
                                            .position(userLocation)
                                            .title(name)
                            );
                        }
                    }
                });
    }
}