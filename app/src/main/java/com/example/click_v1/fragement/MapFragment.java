package com.example.click_v1.fragement;


import static com.facebook.FacebookSdk.getApplicationContext;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.click_v1.R;
import com.example.click_v1.activities.StartNewActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.TriangleEdgeTreatment;

import java.util.Locale;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private View rootView;
    private View markerView;

    private CardView cardView, cardView2;

    private Button button;

    private FloatingActionButton fabAddActivity;

    private GoogleMap mMap;

    private TextView cityText, markerText;

    private FusedLocationProviderClient fusedLocationProviderClient;

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
        cityText = rootView.findViewById(R.id.city);
        cardView = rootView.findViewById(R.id.cardView);
        cardView2 = rootView.findViewById(R.id.cardView2);
        fabAddActivity = rootView.findViewById(R.id.fabAddActivity);
        markerText = markerView.findViewById(R.id.markerText);
        MaterialCardView markerCardView = markerView.findViewById(R.id.markerCardView);
        toTriangleEdgeCardView(markerCardView);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(rootView.getContext());
        markerCardView.measure(View.MeasureSpec.EXACTLY, View.MeasureSpec.EXACTLY);
        Toast.makeText(rootView.getContext(), "cardview width" + markerCardView.getWidth(), Toast.LENGTH_SHORT).show();
    }

    private void setListeners() {
//        button.setOnClickListener(v -> requestPermission());
        fabAddActivity.setOnClickListener(v -> addNewActivity());
    }

    private void addNewActivity() {
        Intent intent = new Intent(getApplicationContext(), StartNewActivity.class);
        startActivity(intent);
    }

//    private void requestPermission() {
//        if (ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED) {
//            fusedLocationProviderClient.getLastLocation()
//                    .addOnSuccessListener(location -> {
//                        if (location != null) {
//                            Toast.makeText(rootView.getContext(), "here:" + location.getLatitude(), Toast.LENGTH_SHORT).show();
//                            double lat = location.getLatitude();
//                            double alt = location.getAltitude();

//                            cityText.setText(String.format(Locale.getDefault(), "%s", lat));
//                        }
//                    });
//
//        } else {
//            Toast.makeText(rootView.getContext(), "requesting location", Toast.LENGTH_SHORT).show();
//            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 40);
//        }
//    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        Marker mMarker = mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .icon(getBitmapDescriptorFromView()));
        assert mMarker != null;
        mMarker.setTag("1234");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
        mMap.setOnMarkerClickListener(marker -> {
            cardView.setVisibility(View.VISIBLE);
            cardView2.setVisibility(View.GONE);
            fabAddActivity.setVisibility(View.GONE);
            Toast.makeText(rootView.getContext(), "click"+marker.getTag(), Toast.LENGTH_SHORT).show();
            return false;
        });
    }

    private void toTriangleEdgeCardView(MaterialCardView cardView) {
        float size = getResources().getDimension(com.intuit.sdp.R.dimen._5sdp); //16dp
        TriangleEdgeTreatment triangleEdgeTreatment = new TriangleEdgeTreatment(size,false);
        cardView.setShapeAppearanceModel(cardView.getShapeAppearanceModel()
                .toBuilder()
                .setBottomEdge(triangleEdgeTreatment)
                .build());
    }

    private BitmapDescriptor getBitmapDescriptorFromView() {
        Bitmap bitmap = view2Bitmap(markerView);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static Bitmap view2Bitmap(View view) {
        Bitmap bitmap = null;//  w w w . j a  v a  2s.c  om
        try {
            if (view != null) {
                view.setDrawingCacheEnabled(true);
                view.measure(View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                        .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                view.layout(0, 0, view.getMeasuredWidth(),
                        view.getMeasuredHeight());
                view.buildDrawingCache();
                bitmap = view.getDrawingCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}