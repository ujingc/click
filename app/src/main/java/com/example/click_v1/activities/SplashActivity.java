package com.example.click_v1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.click_v1.R;
import com.example.click_v1.utilities.Constants;
import com.example.click_v1.utilities.PreferenceManager;

public class SplashActivity extends AppCompatActivity {
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_splash);
//        Toast.makeText(this, "login: "+preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)
//                , Toast.LENGTH_SHORT).show();
        preferenceManager = new PreferenceManager(getApplicationContext());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(preferenceManager.getBoolean(Constants.KEY_USER_ID)) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                finish();
            }
        }, 2500);
    }
//    ((FragmentReplacerActivity) getActivity()).setFragment(new CreateAccountFragment())
}
