package com.example.click_v1.activities;

import static com.example.click_v1.utilities.Common.getBitmapFromEncodedString;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.click_v1.databinding.ActivitiesEditProfileBinding;
import com.example.click_v1.utilities.Constants;
import com.example.click_v1.utilities.PreferenceManager;

public class EditProfileActivity extends AppCompatActivity {
    private ActivitiesEditProfileBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitiesEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        init();
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
        binding.editNameIcon.setOnClickListener(v -> editName());
        binding.nameText.setOnClickListener(v -> editName());

        binding.introductionLayout.setOnClickListener(v -> editIntroduction());
        binding.editIntroductionIcon.setOnClickListener(v -> editIntroduction());
        binding.selfIntroductionText.setOnClickListener(v -> editIntroduction());

        binding.countrynLayout.setOnClickListener(v -> editCountry());
        binding.editCountryIcon.setOnClickListener(v -> editCountry());
        binding.countryText.setOnClickListener(v -> editCountry());

        binding.locationLayout.setOnClickListener(v -> editLocation());
        binding.editLocationIcon.setOnClickListener(v -> editLocation());
        binding.locationText.setOnClickListener(v -> editLocation());

        binding.hobbiesLayout.setOnClickListener(v -> editHobbies());
        binding.editHobbiesIcon.setOnClickListener(v -> editHobbies());

        binding.birthdayLayout.setOnClickListener(v->editorBirthday());
        binding.editBirthdayIcon.setOnClickListener(v->editorBirthday());
        binding.birthdayText.setOnClickListener(v->editorBirthday());
    }

    private void init() {
        binding.meImage.setImageBitmap(getBitmapFromEncodedString(preferenceManager.getString(Constants.KEY_IMAGE)));
        binding.profileName.setText(preferenceManager.getString(Constants.KEY_NAME));
        binding.profileAge.setText("25");
        binding.nameText.setText(preferenceManager.getString(Constants.KEY_NAME));
        binding.selfIntroductionText.setText(preferenceManager.getString(Constants.KEY_SELF_INTRODUCTION));
        binding.countryText.setText(preferenceManager.getString(Constants.KEY_COUNTRY));
        binding.birthdayText.setText(preferenceManager.getString(Constants.KEY_BIRTHDAY));
        binding.locationText.setText(preferenceManager.getString(Constants.KEY_LOCATION));
    }

    private void editName() {
        Intent intent = new Intent(getApplicationContext(), EditNameActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void editCountry() {
        Toast.makeText(this, "Country is not able to be changed", Toast.LENGTH_SHORT).show();
    }

    private void editIntroduction() {
        Intent intent = new Intent(getApplicationContext(), EditSelfIntroductionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);    }

    private void editLocation() {
        Intent intent = new Intent(getApplicationContext(), EditLocationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void editHobbies() {
        Intent intent = new Intent(getApplicationContext(), EditHobbiesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void editGender() {
        Toast.makeText(this, "Gender is not able to be changed", Toast.LENGTH_SHORT).show();
    }

    private void editorBirthday() {
        Toast.makeText(this, "Birthday is not able to be changed", Toast.LENGTH_SHORT).show();
    }

}
