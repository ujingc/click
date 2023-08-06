package com.example.click_v1.fragement;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.click_v1.R;
import com.example.click_v1.databinding.ActivitiesProfileBinding;
import com.example.click_v1.utilities.PreferenceManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MeFragment extends Fragment {
    private View rootView;
    private PreferenceManager preferenceManager;
    private ActivitiesProfileBinding binding;
    private FirebaseFirestore database;
    FusedLocationProviderClient fusedLocationProviderClient;

    private TextView locationText;

    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_me, container, false);
        preferenceManager = new PreferenceManager(getApplicationContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(rootView.getContext());
        locationText = rootView.findViewById(R.id.location);
        init();
        return rootView;
    }

    private void init() {
//        conversations = new ArrayList<>();
//        conversationsAdapter = new RecentConversationAdapter(conversations, this);
//        binding.conversationsRecyclerView.setAdapter(conversationsAdapter);
        database = FirebaseFirestore.getInstance();
//        getLocation();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location == null) {
                        try {
                            // initialize geoCoder
                            Geocoder geocoder = new Geocoder(rootView.getContext(), Locale.getDefault());
                            // initialize address
                            List<Address> addresses = geocoder.getFromLocation(
                                    location.getLatitude(), location.getLongitude(), 1
                            );
                            locationText.setText(addresses.get(0).getLocality());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }
    }
}
