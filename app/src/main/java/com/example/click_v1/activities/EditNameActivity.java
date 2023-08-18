package com.example.click_v1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.click_v1.databinding.ActivitiesEditNameBinding;
import com.example.click_v1.utilities.Constants;
import com.example.click_v1.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class EditNameActivity extends AppCompatActivity {
    private ActivitiesEditNameBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding = ActivitiesEditNameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListeners();
        database = FirebaseFirestore.getInstance();
    }

    private void init() {
        binding.nameInput.setText(preferenceManager.getString(Constants.KEY_NAME));
    }

    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.saveBtn.setOnClickListener(v -> onSavePressed());
    }

    private void onSavePressed() {
        loading(true);
        updateName(binding.nameInput.getText().toString());
    }

    private void updateName(String name) {
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
        Map<String, Object> data = new HashMap<>();
        data.put(Constants.KEY_NAME, name);
        documentReference.set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        preferenceManager.putString(Constants.KEY_NAME, binding.nameInput.getText().toString());
                        loading(false);
                        Toast.makeText(EditNameActivity.this, "ok", Toast.LENGTH_SHORT).show();
                        new CountDownTimer(500, 500) {
                            public void onTick(long millisUntilFinished) {

                            }

                            public void onFinish() {
                                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }.start();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loading(false);
                        Toast.makeText(EditNameActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
