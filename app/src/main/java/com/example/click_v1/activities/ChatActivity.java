package com.example.click_v1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.click_v1.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
    }
}