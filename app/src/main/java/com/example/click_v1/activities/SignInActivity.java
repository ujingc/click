package com.example.click_v1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.click_v1.R;
import com.example.click_v1.databinding.ActivitySignInBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {
    // automatically generate binding class as configured in
    private ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {
        // contain direct references to all views that have an ID
        binding.textCreateNewAccount.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class))
        );
        binding.buttonSignIn.setOnClickListener(v -> addData());
    }

    private void addData() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("firstname", "Yu");
        data.put("lastname", "Che");
        database.collection("users").add(data).addOnSuccessListener(documentReference -> {
            Toast.makeText(getApplicationContext(), "User data is updated", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(exception -> {
            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}