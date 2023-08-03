package com.example.click_v1.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.click_v1.databinding.ActivitySignInBinding;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class FacebookAuthActivity extends SignInActivity {

    CallbackManager callbackManager = CallbackManager.Factory.create();
    private ActivitySignInBinding binding;

    // shared instance of the FirebaseAuth object:
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        binding.buttonSignInFacebook.setPermissions("email", "public_profile");

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onError(@NonNull FacebookException e) {
                Toast.makeText(binding.getRoot().getContext(), "facebook login failed" + e, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(binding.getRoot().getContext(), "facebook login sucess" + loginResult, Toast.LENGTH_SHORT).show();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(binding.getRoot().getContext(), "facebook login cancel", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Toast.makeText(binding.getRoot().getContext(), "handling facebook access token", Toast.LENGTH_SHORT).show();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // If sign in success, update UI with the signed in user's information
                            Toast.makeText(binding.getRoot().getContext(), "sign in with credential success", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign  in Fails, display a message to users
                            Toast.makeText(binding.getRoot().getContext(), "Authentication:facebook failed", Toast.LENGTH_SHORT).show();
                            // TODO update UI
                        }

                    }
                });

    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(FacebookAuthActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}