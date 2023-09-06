package com.example.click_v1.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.click_v1.databinding.ActivitiesSettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    private ActivitiesSettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitiesSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListeners();
    }

    private void init() {
    }

    private void setListeners(){
        binding.accountLayout.setOnClickListener(v -> editAccount());
        binding.editAccountIcon.setOnClickListener(v -> editAccount());
        binding.accountText.setOnClickListener(v -> editAccount());

        binding.filtersLayout.setOnClickListener(v -> editFilters());
        binding.editFiltersIcon.setOnClickListener(v -> editFilters());
        binding.filtersLayoutText.setOnClickListener(v -> editFilters());

        binding.privacyLayout.setOnClickListener(v -> editPrivacy());
        binding.editPrivacyIcon.setOnClickListener(v -> editPrivacy());

        binding.notificationLayout.setOnClickListener(v -> editNotification());
        binding.notificationText.setOnClickListener(v -> editNotification());
        binding.editNotificationIcon.setOnClickListener(v -> editNotification());

        binding.aboutLayout.setOnClickListener(v->openAbout());
        binding.aboutText.setOnClickListener(v->openAbout());
        binding.editAboutIcon.setOnClickListener(v->openAbout());

        binding.imageBack.setOnClickListener(v->onBackPressed());
    }

    private void editAccount() {
        Intent intent = new Intent(getApplicationContext(), EditAccountActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void editPrivacy() {
        Intent intent = new Intent(getApplicationContext(), EditPrivacyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void editFilters() {
        Intent intent = new Intent(getApplicationContext(), EditFiltersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void editNotification() {
        Intent intent = new Intent(getApplicationContext(), EditNotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void openAbout() {
        Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
