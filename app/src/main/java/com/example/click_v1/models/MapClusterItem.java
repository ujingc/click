package com.example.click_v1.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MapClusterItem implements ClusterItem {
    // represent a marker on the map
    private final LatLng position;
//    private final String title;
//    private final String snippet;

    private final String id;
    private final BitmapDescriptor icon;

    public MapClusterItem(double lat, double lng, String id, BitmapDescriptor icon) {
        position = new LatLng(lat, lng);
//        this.title = title;
//        this.snippet = snippet;
        this.id = id;
        this.icon = icon;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return position;
    }

    @Nullable
    @Override
    public String getTitle() {
        return null;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return null;
    }

//    @Override
//    public String getTitle() {
//        return title;
//    }
//
//    @Override
//    public String getSnippet() {
//        return snippet;
//    }

    @Nullable
    @Override
    public Float getZIndex() {
        return 0f;
    }

    public String getId(){ return id; }
    public BitmapDescriptor getIcon() {
        return icon;
    }
}
