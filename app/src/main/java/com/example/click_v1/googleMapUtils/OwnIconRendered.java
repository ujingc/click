package com.example.click_v1.googleMapUtils;

import android.content.Context;

import com.example.click_v1.models.MapClusterItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class OwnIconRendered extends DefaultClusterRenderer<MapClusterItem> {

    public OwnIconRendered(Context context, GoogleMap map,
                           ClusterManager<MapClusterItem> clusterManager) {
        super(context, map, clusterManager);
    }


    @Override
    protected void onBeforeClusterItemRendered(MapClusterItem item, MarkerOptions markerOptions) {
        markerOptions.icon(item.getIcon());
//        markerOptions.snippet(item.getSnippet());
//        markerOptions.title(item.getTitle());
        super.onBeforeClusterItemRendered(item, markerOptions);
    }

    @Override
    protected void onClusterItemRendered(MapClusterItem item, Marker marker) {
        marker.setTag(item.getId());
        super.onClusterItemRendered(item, marker);
    }
}