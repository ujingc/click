package com.example.click_v1.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.click_v1.R;
import com.example.click_v1.databinding.ActiviesStartNewActivityBinding;
import com.example.click_v1.models.MarkerActivity;
import com.example.click_v1.utilities.Constants;
import com.example.click_v1.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;


public class StartNewActivity extends AppCompatActivity {

    private ActiviesStartNewActivityBinding binding;

    private PreferenceManager preferenceManager;

    private FirebaseFirestore database;

    private MarkerActivity myActivity;

    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActiviesStartNewActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        loadActivityDetails();
        init();
        setListeners();
    }

    private void init() {
        preferenceManager = new PreferenceManager(getApplicationContext());
        database = FirebaseFirestore.getInstance();
        loadMyActivity();
    }

//    private void loadActivityDetails() {
//        city = (String) getIntent().getSerializableExtra(Constants.KEY_CITY);
//        binding.cityInput.setText(city);
//    }

    private void createActivity() {
        if (binding.titleInput.getText().toString().trim().length() > 0) {
            HashMap<String, Object> activity = new HashMap<>();
            activity.put(Constants.KEY_CREATOR_ID, preferenceManager.getString(Constants.KEY_USER_ID));
            activity.put(Constants.KEY_CREATOR_NAME, preferenceManager.getString(Constants.KEY_NAME));
            activity.put(Constants.KEY_CREATOR_IMAGE, preferenceManager.getString(Constants.KEY_IMAGE));
            activity.put(Constants.KEY_SELF_INTRODUCTION, preferenceManager.getString(Constants.KEY_SELF_INTRODUCTION));
            activity.put(Constants.KEY_COUNTRY, preferenceManager.getString(Constants.KEY_COUNTRY));
            activity.put(Constants.KEY_GENDER, preferenceManager.getString(Constants.KEY_GENDER));
            activity.put(Constants.KEY_EMAIL, preferenceManager.getString(Constants.KEY_EMAIL));
            activity.put(Constants.KEY_LAT, preferenceManager.getString(Constants.KEY_LAT));
            activity.put(Constants.KEY_LNG, preferenceManager.getString(Constants.KEY_LNG));
            activity.put(Constants.KEY_TITLE, binding.titleInput.getText().toString());
            activity.put(Constants.KEY_TOPIC, binding.topicInput.getText().toString());
            activity.put(Constants.KEY_DESCRIPTION, binding.announcementInput.getText().toString());
            activity.put(Constants.KEY_LOCATION, city);
            activity.put(Constants.KEY_TIMESTAMP, new Date());
            activity.put(Constants.KEY_CITY, preferenceManager.getString(Constants.KEY_CITY));
            activity.put(Constants.KEY_ACTIVE, "true");

            if (myActivity == null) {
                database.collection(Constants.KEY_COLLECTION_ACTIVITY).add(activity).addOnSuccessListener(documentReference -> {
                    onBackPressed();
                });
            } else {
                DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_CONVERSATIONS).document(myActivity.getId());
                documentReference.update(
                                Constants.KEY_TITLE, binding.titleInput.getText().toString(),
                                Constants.KEY_TOPIC, binding.topicInput.getText().toString(),
                                Constants.KEY_DESCRIPTION, binding.announcementInput.getText().toString(),
                                Constants.KEY_DISTANCE, preferenceManager.getString(Constants.KEY_DISTANCE),
                                Constants.KEY_CITY, activity.get(Constants.KEY_CITY),
                                Constants.KEY_TIMESTAMP, new Date(),
                                Constants.KEY_ACTIVE, "true")
                        .addOnSuccessListener(ref -> {
                            onBackPressed();
                        });
            }
        } else {
            Toast.makeText(this, "Please fill in activity title", Toast.LENGTH_SHORT).show();
        }
    }

    private void setListeners() {
        binding.startBtn.setOnClickListener(v -> createActivity());
        binding.cancelBtn.setOnClickListener(v -> onBackPressed());
        binding.announcementInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @SuppressLint({"ResourceAsColor", "DefaultLocale"})
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                binding.announcementTextCount.setText(String.format("%2d/300", s.length()));
                if (s.length() == 300) {
                    Toast.makeText(getApplicationContext(), "Change to red", Toast.LENGTH_SHORT).show();
                    binding.announcementTextCount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.error));
                }
            }
        });
    }

    private void loadMyActivity() {
        myActivity = (MarkerActivity) getIntent().getSerializableExtra(Constants.KEY_COLLECTION_ACTIVITY);
        binding.topicInput.setText(myActivity.topic);
        binding.titleInput.setText(myActivity.title);
        binding.announcementInput.setText(myActivity.description);
        binding.cityInput.setText(preferenceManager.getString(Constants.KEY_CITY));
        binding.distanceInput.setText(preferenceManager.getString(Constants.KEY_DISTANCE));
    }
}