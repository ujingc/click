package com.example.click_v1.fragement;

import static com.example.click_v1.utilities.Common.getBitmapFromEncodedString;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.click_v1.R;
import com.example.click_v1.activities.EditProfileActivity;
import com.example.click_v1.activities.SettingsActivity;
import com.example.click_v1.utilities.Constants;
import com.example.click_v1.utilities.PreferenceManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MeFragment extends Fragment {
    private View rootView;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;
    private AppCompatImageView editProfileBtn, editSettingsBtn;
    private ConstraintLayout profileLayout;
    private LinearLayout onlineStatusLayout;
    private TextView nameText, ageText, currentLocationText;
    private RoundedImageView meImage;
    FusedLocationProviderClient fusedLocationProviderClient;

    private TextView locationText;

    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_me, container, false);
        preferenceManager = new PreferenceManager(getApplicationContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(rootView.getContext());
        init();
        setListeners();
        return rootView;
    }

    private void init() {
        meImage = rootView.findViewById(R.id.meImage);
        nameText = rootView.findViewById(R.id.nameText);
        ageText = rootView.findViewById(R.id.ageText);
        locationText = rootView.findViewById(R.id.locationText);
        editProfileBtn = rootView.findViewById(R.id.editProfile);
        editSettingsBtn = rootView.findViewById(R.id.editSettings);
        profileLayout = rootView.findViewById(R.id.profileLayout);
        onlineStatusLayout = rootView.findViewById(R.id.onlineStatusLayout);
        currentLocationText = rootView.findViewById(R.id.currentLocationText);

        nameText.setText(preferenceManager.getString(Constants.KEY_NAME));
        ageText.setText(preferenceManager.getString(Constants.KEY_BIRTHDAY));
        locationText.setText(preferenceManager.getString(Constants.KEY_LOCATION));
        meImage.setImageBitmap(getBitmapFromEncodedString(preferenceManager.getString(Constants.KEY_IMAGE)));
        database = FirebaseFirestore.getInstance();
//        getLocation();
    }

    private void setListeners() {
        editProfileBtn.setOnClickListener(v -> editProfile());
        profileLayout.setOnClickListener(v-> editProfile());
        editSettingsBtn.setOnClickListener(v-> editSettings());
    }

    private void editProfile() {
        startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
    }

    private void editSettings() {
        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
    }



    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location == null) {
                        try {
                            // initialize geoCoder
                            Geocoder geocoder = new Geocoder(rootView.getContext(), Locale.getDefault());
                            // initialize address
                            List<Address> addresses = geocoder.getFromLocation(
                                    location.getLatitude(), location.getLongitude(), 1
                            );
                            currentLocationText.setText(addresses.get(0).getLocality());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }
    }
}
