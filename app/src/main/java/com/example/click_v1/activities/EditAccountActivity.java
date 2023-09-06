package com.example.click_v1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.click_v1.databinding.ActivitiesEditAccountBinding;
import com.example.click_v1.utilities.Constants;
import com.example.click_v1.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class EditAccountActivity extends AppCompatActivity {

    private ActivitiesEditAccountBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding = ActivitiesEditAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {
//        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                binding.distanceText.setText(String.format("0-%s km", i));
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(EditFiltersActivity.this, ""+binding.seekBar.getProgress(), Toast.LENGTH_SHORT).show();
//                preferenceManager.putString(Constants.KEY_DISTANCE, Integer.toString(binding.seekBar.getProgress()));
//            }
//        });
        binding.imageBack.setOnClickListener(v->onBackPressed());
        binding.logOutBtn.setOnClickListener(v -> logOut());
    }

    private void logOut() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        // get user data from database with collection name and user ID
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(
                preferenceManager.getString(Constants.KEY_USER_ID)
        );
        // create a map with key fcm token and empty value
        HashMap<String, Object> updates = new HashMap<>();
        // empty key fcm token in database
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates).addOnSuccessListener(unused -> {
            preferenceManager.clear();
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            finish();
        }).addOnFailureListener(e -> Toast.makeText(this, "Unable to sign out", Toast.LENGTH_SHORT).show());
    }
}
