package com.example.click_v1.activities;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.click_v1.databinding.ActivitiesEditProfileBinding;
import com.example.click_v1.databinding.ActivityMainBinding;
import com.example.click_v1.fragement.ChatFragment;
import com.example.click_v1.utilities.PreferenceManager;
import com.google.android.gms.location.LocationServices;

public class EditProfile extends AppCompatActivity {
    private ActivitiesEditProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitiesEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        preferenceManager = new PreferenceManager(getApplicationContext());
//        init();
//        loadUserDetails();
//        getToken();
        setListeners();
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA},
                225);
    }

    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.nameLayout.setOnClickListener(v -> editName());
        binding.introductionLayout.setOnClickListener(v -> editIntroduction());
        binding.countrynLayout.setOnClickListener(v -> editCountry());
        binding.cityLayout.setOnClickListener(v -> editCity());
        binding.interestLayout.setOnClickListener(v -> editInterest());
        binding.genderLayout.setOnClickListener(v -> editGender());
    }

    private void editName() {
        Toast.makeText(this, "edit name", Toast.LENGTH_SHORT).show();
    }

    private void editCountry() {
        Toast.makeText(this, "edit country", Toast.LENGTH_SHORT).show();
    }

    private void editIntroduction() {
        Toast.makeText(this, "edit introduction", Toast.LENGTH_SHORT).show();
    }

    private void editCity() {
        Toast.makeText(this, "edit city", Toast.LENGTH_SHORT).show();
    }

    private void editInterest() {
        Toast.makeText(this, "edit interest", Toast.LENGTH_SHORT).show();
    }

    private void editGender() {
        Toast.makeText(this, "edit gender", Toast.LENGTH_SHORT).show();
    }

}
