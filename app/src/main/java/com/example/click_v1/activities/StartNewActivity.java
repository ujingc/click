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

    private MarkerActivity ownActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActiviesStartNewActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListeners();
    }

    private void init() {
        preferenceManager = new PreferenceManager(getApplicationContext());
        database = FirebaseFirestore.getInstance();
        loadOwnActivity();
    }

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

            activity.put(Constants.KEY_LAT, "-34.01");
            activity.put(Constants.KEY_LNG, "151.01");
            activity.put(Constants.KEY_TITLE, binding.titleInput.getText().toString());
            activity.put(Constants.KEY_TOPIC, binding.topicInput.getText().toString());
            activity.put(Constants.KEY_DESCRIPTION, binding.announcementInput.getText().toString());
            activity.put(Constants.KEY_LOCATION, "Sydney");
            activity.put(Constants.KEY_TIMESTAMP, new Date());
            if (ownActivity == null) {
                database.collection(Constants.KEY_COLLECTION_ACTIVITY).add(activity).addOnSuccessListener(documentReference -> {
                    onBackPressed();
                });
            } else {
                DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_CONVERSATIONS).document(ownActivity.getId());
                documentReference.update(
                                Constants.KEY_TITLE, binding.titleInput.getText().toString(),
                                Constants.KEY_TOPIC, binding.topicInput.getText().toString(),
                                Constants.KEY_DESCRIPTION, binding.announcementInput.getText().toString(),
                                Constants.KEY_TIMESTAMP, new Date())
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

    private void loadOwnActivity() {
        ownActivity = (MarkerActivity) getIntent().getSerializableExtra(Constants.KEY_COLLECTION_ACTIVITY);
        binding.topicInput.setText(ownActivity.topic);
        binding.titleInput.setText(ownActivity.title);
        binding.announcementInput.setText(ownActivity.description);
        binding.distanceInput.setText(preferenceManager.getString(Constants.KEY_DISTANCE));
    }
}