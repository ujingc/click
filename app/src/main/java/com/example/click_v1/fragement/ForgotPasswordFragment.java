package com.example.click_v1.fragement;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.click_v1.R;
import com.example.click_v1.activities.SignUpActivity;
import com.example.click_v1.databinding.FragmentForgotPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordFragment extends Fragment {
    private FragmentForgotPasswordBinding binding;
    private FirebaseAuth auth;

    public ForgotPasswordFragment() {
    }

    private void onForgotPasswordPressed() {
        // TODO reset password
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentForgotPasswordBinding.inflate(getLayoutInflater());
        setListeners();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);

    }

    private void setListeners() {
        binding.forgotPasswordBtn.setOnClickListener(v -> onForgotPasswordPressed()
//                startActivity(new Intent(null, SignUpActivity.class))
        );
    }

    private void init() {
        auth = FirebaseAuth.getInstance();

    }
}