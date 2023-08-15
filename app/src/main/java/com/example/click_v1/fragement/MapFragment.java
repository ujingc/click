package com.example.click_v1.fragement;

import static com.example.click_v1.utilities.Common.getBitmapDescriptorFromView;
import static com.example.click_v1.utilities.Common.toTriangleEdgeCardView;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.click_v1.R;
import com.example.click_v1.activities.ChatActivity;
import com.example.click_v1.activities.StartNewActivity;
import com.example.click_v1.adapters.MarkerActivityAdapter;
import com.example.click_v1.listeners.ConversationListener;
import com.example.click_v1.listeners.UserListener;
import com.example.click_v1.models.MapClusterItem;
import com.example.click_v1.models.MarkerActivity;
import com.example.click_v1.models.User;
import com.example.click_v1.utilities.Constants;
import com.example.click_v1.utilities.PreferenceManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class MapFragment extends Fragment implements OnMapReadyCallback, ConversationListener, UserListener {
    private View rootView;
    private View markerView;
    private CardView activityCardView, clusterCardView;
    private RecyclerView activityCardRecyclerView;
    private LinearLayout exploreBtn, sendBtn;
    private FloatingActionButton fabAddActivityBtn;
    private GoogleMap mMap;
    private ClusterManager<MapClusterItem> clusterManager;

    private User receiverUser;
    private MarkerActivityAdapter markerActivityAdapter;
    private List<MarkerActivity> markerActivities;

    private PreferenceManager preferenceManager;


//    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        markerView = inflater.inflate(R.layout.marker_layout, container, false);
        init();
        setListeners();
        setupMap();
        return rootView;
    }

    private void init() {
        preferenceManager = new PreferenceManager(getApplicationContext());
        markerActivities = new ArrayList<>();
        activityCardView = rootView.findViewById(R.id.activityCardView);
        clusterCardView = rootView.findViewById(R.id.clusterCardView);
        activityCardRecyclerView = rootView.findViewById(R.id.activityCardRecyclerView);
        exploreBtn = rootView.findViewById(R.id.exploreBtn);
        sendBtn = rootView.findViewById(R.id.sendBtn);
        fabAddActivityBtn = rootView.findViewById(R.id.fabAddActivity);
        MaterialCardView markerCardView = markerView.findViewById(R.id.markerCardView);
        toTriangleEdgeCardView(markerCardView, getResources());
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(rootView.getContext());
        markerCardView.measure(View.MeasureSpec.EXACTLY, View.MeasureSpec.EXACTLY);
        Calendar cal = Calendar.getInstance();
        cal.set(1995, 7, 13, 11, 0);

        String creatorId = "7bzxYJzJn1gqM3AagXIk";
        String id = "1";
        String topic = "聚餐";
        String title = "一起吃中午饭";
        String description = "找人一起吃饭";
        int lat = -34;
        int lng = 151;
        String creatorName = "Chen";
        int imagePoster = 1;
        Date dateTime = cal.getTime();

        User user = new User();
        user.name = "avatar";
        user.email = "test1@email.com";
        user.image = "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";
        user.id = "7bzxYJzJn1gqM3AagXIk";

        MarkerActivity markerActivity1 = new MarkerActivity(creatorId, id, topic, title, description, lat, lng, dateTime, user);
        markerActivities.add(markerActivity1);
        markerActivityAdapter = new MarkerActivityAdapter(markerActivities, this);
        activityCardRecyclerView.setAdapter(markerActivityAdapter);
        activityCardRecyclerView.setVisibility(View.GONE);
    }

    private void setListeners() {
        fabAddActivityBtn.setOnClickListener(v -> addNewActivity());
        exploreBtn.setOnClickListener(v -> exploreActivity());

        User user = new User();
        user.id = "7bzxYJzJn1gqM3AagXIk";
        user.name = "Neytiri";
        user.image = "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    ";
//        sendBtn.setOnClickListener(v -> onConversationClicked(user));
    }

    private void addNewActivity() {
        Intent intent = new Intent(getApplicationContext(), StartNewActivity.class);
        startActivity(intent);
    }

    private void exploreActivity() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-34, 151), 12));
        clusterCardView.setVisibility(View.GONE);
        activityCardRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onConversationClicked(User user) {
        Toast.makeText(rootView.getContext(), "click send message", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @SuppressLint("PotentialBehaviorOverride")
    private void setUpCluster() {
        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-34, 151), 7));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
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
            activityCardRecyclerView.setVisibility(View.VISIBLE);
            clusterCardView.setVisibility(View.GONE);
            fabAddActivityBtn.setVisibility(View.GONE);
//            Toast.makeText(rootView.getContext(), "click" + marker.getTag(), Toast.LENGTH_SHORT).show();

            return false;
        });

        // Add cluster items (markers) to the cluster manager.
        addItems();
        //set info window
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(@NonNull Marker marker) {
                // Empty info window.
                return new TextView(getActivity());
            }

            @Override
            public View getInfoContents(@NonNull Marker marker) {
                return null;
            }
        });
    }

    private void addItems() {
        // Set some lat/lng coordinates to start with.
        double lat = -34.01;
        double lng = 151.01;
        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            MapClusterItem offsetItem = new MapClusterItem(lat, lng, "Title " + i, "Snippet " + i, Integer.toString(i), getBitmapDescriptorFromView(markerView));

            clusterManager.setRenderer(new OwnIconRendered(getApplicationContext(), mMap, clusterManager));
            clusterManager.addItem(offsetItem);
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
        setUpCluster();
        mMap.setOnMarkerClickListener(marker -> {
//            clusterCardView.setVisibility(View.GONE);
            return false;
        });
    }

    @Override
    public void onUserClick(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
    }
}