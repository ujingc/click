package com.example.click_v1.fragement;

import static com.example.click_v1.utilities.Common.getBitmapDescriptorFromView;
import static com.example.click_v1.utilities.Common.getBitmapFromEncodedString;
import static com.example.click_v1.utilities.Common.getDateDiff;
import static com.example.click_v1.utilities.Common.getReadableDateTime;
import static com.example.click_v1.utilities.Common.toTriangleEdgeCardView;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.click_v1.R;
import com.example.click_v1.activities.ChatActivity;
import com.example.click_v1.activities.StartNewActivity;
import com.example.click_v1.adapters.MarkerActivityAdapter;
import com.example.click_v1.listeners.UserListener;
import com.example.click_v1.models.MapClusterItem;
import com.example.click_v1.models.MarkerActivity;
import com.example.click_v1.models.User;
import com.example.click_v1.utilities.Constants;
import com.example.click_v1.utilities.PreferenceManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class MapFragment extends Fragment implements OnMapReadyCallback, UserListener {
    //    frame is not automatically bind
    private View rootView, markerView;
    private CardView clusterCardView;
    private RecyclerView activityCardRecyclerView;
    private FloatingActionButton fabAddActivityBtn;
    private ImageView imageView;
    private TextView markerText, markerTimeLeftText;
    private MaterialButton refreshMapBtn, exploreBtn;
    private ConstraintLayout selectedActivityCardView;

    private GoogleMap mMap;
    private ClusterManager<MapClusterItem> clusterManager;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;
    FusedLocationProviderClient fusedLocationProviderClient;

    private MarkerActivityAdapter markerActivityAdapter;
    private List<MarkerActivity> markerActivities;
    private List<MarkerActivity> selectedActivities;
    private MarkerActivity myActivity;
    private List<Address> addresses;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        markerView = inflater.inflate(R.layout.custom_marker, container, false);
        database = FirebaseFirestore.getInstance();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(rootView.getContext());
        init();
        getLocation();
        setListeners();
        setupMap();
        return rootView;
    }

    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
        getMarkerActivities();
        mMap.setOnMarkerClickListener(marker -> false);
    }

    // send message method
    @Override
    public void onUserClick(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
    }

    private void init() {
        preferenceManager = new PreferenceManager(getApplicationContext());
        markerActivities = new ArrayList<>();
        selectedActivities = new ArrayList<>();

        selectedActivityCardView = rootView.findViewById(R.id.selectedActivityCardView);
        clusterCardView = rootView.findViewById(R.id.clusterCardView);
        activityCardRecyclerView = rootView.findViewById(R.id.activityCardRecyclerView);
        exploreBtn = rootView.findViewById(R.id.exploreBtn);
        fabAddActivityBtn = rootView.findViewById(R.id.fabAddActivity);
        imageView = markerView.findViewById(R.id.markerImage);
        markerText = markerView.findViewById(R.id.markerText);
        markerTimeLeftText = markerView.findViewById(R.id.markerTimeLeftText);
        refreshMapBtn = rootView.findViewById(R.id.refreshMapBtn);
        MaterialCardView markerCardView = markerView.findViewById(R.id.markerCardView);
        markerCardView.measure(View.MeasureSpec.EXACTLY, View.MeasureSpec.EXACTLY);
        toTriangleEdgeCardView(markerCardView, getResources());

        markerActivityAdapter = new MarkerActivityAdapter(selectedActivities, this, preferenceManager.getString(Constants.KEY_USER_ID));
        activityCardRecyclerView.setAdapter(markerActivityAdapter);
    }

    private void setListeners() {
        fabAddActivityBtn.setOnClickListener(v -> addNewActivity());
        exploreBtn.setOnClickListener(v -> onExploreClick());
        refreshMapBtn.setOnClickListener(v -> getMarkerActivities());
    }

    // explore map method
    private void onExploreClick() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-34, 151), 12));
        clusterCardView.setVisibility(View.GONE);
        activityCardRecyclerView.setVisibility(View.GONE);
    }

    // get data
    private void getMarkerActivities() {
        loading(true);
        database.collection(Constants.KEY_COLLECTION_ACTIVITY)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<MarkerActivity> markerActivityList = new ArrayList<>();
                        // each queryDocumentSnapshot contains a user data
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            MarkerActivity markerActivity = new MarkerActivity();
                            markerActivity.creatorId = queryDocumentSnapshot.getString(Constants.KEY_CREATOR_ID);
                            markerActivity.name = queryDocumentSnapshot.getString(Constants.KEY_CREATOR_NAME);
                            markerActivity.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            markerActivity.creatorImage = queryDocumentSnapshot.getString(Constants.KEY_CREATOR_IMAGE);
                            markerActivity.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            markerActivity.selfIntroduction = queryDocumentSnapshot.getString(Constants.KEY_SELF_INTRODUCTION);
                            markerActivity.location = queryDocumentSnapshot.getString(Constants.KEY_LOCATION);
                            markerActivity.country = queryDocumentSnapshot.getString(Constants.KEY_COUNTRY);
                            markerActivity.gender = queryDocumentSnapshot.getString(Constants.KEY_GENDER);
                            markerActivity.id = queryDocumentSnapshot.getId();
                            markerActivity.topic = queryDocumentSnapshot.getString(Constants.KEY_TOPIC);
                            markerActivity.description = queryDocumentSnapshot.getString(Constants.KEY_DESCRIPTION);
                            markerActivity.lat = Double.parseDouble(Objects.requireNonNull(queryDocumentSnapshot.getString(Constants.KEY_LAT)));
                            markerActivity.lng = Double.parseDouble(Objects.requireNonNull(queryDocumentSnapshot.getString(Constants.KEY_LNG)));
                            markerActivity.dateTime = getReadableDateTime(queryDocumentSnapshot.getDate(Constants.KEY_TIMESTAMP));
                            markerActivity.dateObject = queryDocumentSnapshot.getDate(Constants.KEY_TIMESTAMP);
                            markerActivityList.add(markerActivity);
                        }
                        if (markerActivityList.size() > 0) {
//                            markerActivities = markerActivityList.stream().filter(activity -> distance(
//                                    Double.parseDouble(preferenceManager.getString(Constants.KEY_LAT)),
//                                    Double.parseDouble(preferenceManager.getString(Constants.KEY_LNG)),
//                                    activity.lat,
//                                    activity.lng,
//                                    0,
//                                    0
//                            ) <= Double.parseDouble(preferenceManager.getString(Constants.KEY_DISTANCE))).collect(Collectors.toList());
                            markerActivities = markerActivityList;
                            MarkerActivityAdapter markerActivityAdapter = new MarkerActivityAdapter(selectedActivities, this, preferenceManager.getString(Constants.KEY_USER_ID));
                            activityCardRecyclerView.setAdapter(markerActivityAdapter);
                            getMyActivity();
                            setUpCluster();
                        } else {
                            showErrorMessage();
                        }
                    } else {
                        showErrorMessage();
                    }
                });
    }

    // marker click method
    @SuppressLint("NotifyDataSetChanged")
    private void showSelectedMarkerCardView(String id) {
        for (MarkerActivity markerActivity : markerActivities) {
            if (markerActivity.getId().equals(id)) {
                selectedActivities = new ArrayList<>();
                selectedActivities.add(markerActivity);
                selectedActivities = markerActivities.stream()
                        .filter(m -> !Objects.equals(m.creatorId, preferenceManager.getString(Constants.KEY_USER_ID)))
                        .collect(Collectors.toList());
            }
        }
        markerActivityAdapter.notifyItemChanged(0);
        activityCardRecyclerView.setVisibility(View.VISIBLE);
        clusterCardView.setVisibility(View.GONE);
        fabAddActivityBtn.setVisibility(View.GONE);
    }

    // add map to fragment
    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    // set up map cluster and marker items
    @SuppressLint("PotentialBehaviorOverride")
    private void setUpCluster() {
        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-34, 151), 7));
        clusterManager = new ClusterManager<MapClusterItem>(rootView.getContext(), mMap) {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                //here will get the clicked marker
                return super.onMarkerClick(marker);
            }
        };
        // Point the map's listeners at the listeners implemented by the cluster manager.
        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);
        clusterManager.setOnClusterItemClickListener(item -> {
            String itemId = item.getId();
            showSelectedMarkerCardView(itemId);
            return false;
        });
        addItems();
        clusterManager.cluster();
    }

    // add cluster item
    private void addItems() {
        for (MarkerActivity markerActivity : markerActivities) {
            Date now = new Date();
            Long countDownLeftTime = getDateDiff(markerActivity.dateObject, now, TimeUnit.MILLISECONDS);
            CountDownTimer timer = getDownTimer(countDownLeftTime, markerActivity.id);
            timer.start();
            imageView.setImageBitmap(getBitmapFromEncodedString(markerActivity.creatorImage));
            markerText.setText(markerActivity.title);
            MapClusterItem offsetItem = new MapClusterItem(markerActivity.lat, markerActivity.lng, markerActivity.id, getBitmapDescriptorFromView(markerView));
            clusterManager.setRenderer(new CustomRendered<>(getApplicationContext(), mMap, clusterManager));
            clusterManager.addItem(offsetItem);
        }
    }

    // activity item timer
    private CountDownTimer getDownTimer(Long millionSecond, String id) {
        return new CountDownTimer(millionSecond, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long second = (millisUntilFinished / 1000) % 60;
//                long minutes = (millisUntilFinished / (1000 * 60)) % 60;
//                long hours = (millisUntilFinished / (1000 * 60 * 60)) % 60;
//                secondsText.setText(String.valueOf(second));
//                minutesText.setText(String.valueOf(minutes));
//                hoursText.setText(String.valueOf(hours));
                markerTimeLeftText.setText(String.valueOf(second));
            }

            @Override
            public void onFinish() {
                Collection<Marker> markerCollection = clusterManager.getMarkerCollection().getMarkers();
                for (Marker marker : markerCollection) {
                    if (marker.getId().equals(id)) {
                        marker.setVisible(false);
                    }
                }
                selectedActivityCardView.setVisibility(View.GONE);
            }
        };
    }

    // add activity method
    private void addNewActivity() {
//        if (myActivity == null) {
            Intent intent = new Intent(getApplicationContext(), StartNewActivity.class);
            intent.putExtra(Constants.KEY_CITY, addresses.get(0).getLocality());
            intent.putExtra(Constants.KEY_COLLECTION_ACTIVITY, myActivity);
            intent.putExtra(Constants.KEY_LAT, String.valueOf(addresses.get(0).getLatitude()));
            intent.putExtra(Constants.KEY_LNG, String.valueOf(addresses.get(0).getLongitude()));

        startActivity(intent);
//        } else {
//            Toast.makeText(getContext(), "You already have an existing activity", Toast.LENGTH_SHORT).show();
//        }
    }

    // edit activity method
    private void editActivity() {
        if (myActivity != null) {
            Intent intent = new Intent(getApplicationContext(), StartNewActivity.class);
            intent.putExtra(Constants.KEY_COLLECTION_ACTIVITY, myActivity);
        }
    }

    // get my activity
    private void getMyActivity() {
        for (MarkerActivity markerActivity : markerActivities) {
            if (markerActivity.getCreatorId().equals(preferenceManager.getString(Constants.KEY_USER_ID))) {
                myActivity = markerActivity;
            }
        }
    }

    private void getLocation() {
        Toast.makeText(getContext(), "getLocation", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

            LocationRequest mLocationRequest = LocationRequest.create();
            mLocationRequest.setInterval(60000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationCallback mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            try {
                                // initialize geoCoder
                                Geocoder geocoder = new Geocoder(rootView.getContext(), Locale.getDefault());
                                // initialize address
                                addresses = geocoder.getFromLocation(
                                        location.getLatitude(), location.getLongitude(), 1
                                );

                                if(addresses.size()>0) {
                                    preferenceManager.putString(Constants.KEY_CITY, addresses.get(0).getLocality());
                                    preferenceManager.putString(Constants.KEY_LAT, String.valueOf(addresses.get(0).getLatitude()));
                                    preferenceManager.putString(Constants.KEY_LNG, String.valueOf(addresses.get(0).getLongitude()));
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            };
            LocationServices.getFusedLocationProviderClient(getApplicationContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);


//            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
//                Location location = task.getResult();
//                Toast.makeText(getContext(), "complete", Toast.LENGTH_LONG).show();
//                if (location != null) {
//                    try {
//                        // initialize geoCoder
//                        Geocoder geocoder = new Geocoder(rootView.getContext(), Locale.getDefault());
//                        // initialize address
//                        addresses = geocoder.getFromLocation(
//                                location.getLatitude(), location.getLongitude(), 1
//                        );
//                        Toast.makeText(getContext(), "address"+addresses.size(), Toast.LENGTH_LONG).show();
//
//                        if(addresses.size()>0) {
//                            Toast.makeText(getContext(), ""+addresses.get(0).getLocality(), Toast.LENGTH_SHORT).show();
//                            preferenceManager.putString(Constants.KEY_CITY, addresses.get(0).getLocality());
//                        }
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            });
        }
    }

    private void showErrorMessage() {
        Toast.makeText(getContext(), "Error occur when loading activity", Toast.LENGTH_SHORT).show();
    }

    // show data loading message
    private void loading(Boolean isLoading) {
        if (isLoading) {
//            Toast.makeText(getContext(), "loading activity", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(getContext(), "activity loaded", Toast.LENGTH_SHORT).show();
        }
    }
}