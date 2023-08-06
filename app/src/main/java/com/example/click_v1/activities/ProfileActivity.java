package com.example.click_v1.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.click_v1.R;
import com.example.click_v1.adapters.ProfileAdapter;
import com.example.click_v1.databinding.ActivitiesProfileBinding;
import com.example.click_v1.models.Profile;
import com.example.click_v1.utilities.PreferenceManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends BaseActivity {

    private PreferenceManager preferenceManager;
    private ActivitiesProfileBinding binding;
    private FirebaseFirestore database;
    FusedLocationProviderClient fusedLocationProviderClient;


    private void setupProfileViewPager() {
        ViewPager2 profileViewPager = findViewById(R.id.profileViewPager);
        profileViewPager.setClipToPadding(false);
        profileViewPager.setClipChildren(false);
        profileViewPager.setOffscreenPageLimit(3);
        profileViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r + 0.15f);
        });
        profileViewPager.setPageTransformer(compositePageTransformer);
        profileViewPager.setAdapter(new ProfileAdapter(getProfiles()));
    }

    private List<Profile> getProfiles() {
        List<Profile> profiles = new ArrayList<>();
        Profile me = new Profile();
        me.name = "Chen";
        me.age = "25";
        me.description = "Hi, I am learning vietnamese";
        me.id = "1";
        me.imagePoster = R.drawable.profile;
        profiles.add(me);
        Profile you = new Profile();
        you.name = "Chen";
        you.age = "25";
        you.description = "Hi, I am learning vietnamese";
        you.id = "1";
        you.imagePoster = R.drawable.profile;
        profiles.add(you);

        return profiles;
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location == null) {
                        try {
                            // initialize geoCoder
                            Geocoder geocoder = new Geocoder(ProfileActivity.this, Locale.getDefault());
                            // initialize address
                            List<Address> addresses = geocoder.getFromLocation(
                                    location.getLatitude(), location.getLongitude(), 1
                            );
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitiesProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupProfileViewPager();
        preferenceManager = new PreferenceManager(getApplicationContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        init();
//        loadUserDetails();
//        getToken();
        setListeners();
//        listenConversations();
    }

    private void init() {
//        conversations = new ArrayList<>();
//        conversationsAdapter = new RecentConversationAdapter(conversations, this);
//        binding.conversationsRecyclerView.setAdapter(conversationsAdapter);
        database = FirebaseFirestore.getInstance();
        getLocation();
    }

    private void setListeners() {
//        binding.imageSignOut.setOnClickListener(v -> signOut());
//        binding.fabNewChat.setOnClickListener(v ->
//                startActivity(new Intent(getApplicationContext(), UsersActivity.class)));

//        bottomBar.onItemReselected = {
//                status.text = "Item $it re-selected"
//        }
    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
