package com.example.click_v1.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.click_v1.databinding.ActiviesStartNewActivityBinding;
import com.example.click_v1.utilities.PreferenceManager;


public class StartNewActivity extends AppCompatActivity {

    ActiviesStartNewActivityBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActiviesStartNewActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        preferenceManager = new PreferenceManager(getApplicationContext());
        init();
//        loadUserDetails();
//        getToken();
        setListeners();
    }

    private void init() {

    }


    private void setListeners() {
//        binding.imageSignOut.setOnClickListener(v -> signOut());
//        binding.fabNewChat.setOnClickListener(v ->
//                startActivity(new Intent(getApplicationContext(), UsersActivity.class)));

        // listener for smoothBar
//        binding.smoothBottomBar.setOnItemSelectedListener(this::switchActivity);
        binding.cancelBtn.setOnClickListener(v->onBackPressed());
    }

}
