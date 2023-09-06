package com.example.click_v1.fragement;

import android.content.Context;

import com.example.click_v1.models.MapClusterItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class CustomRendered<T extends ClusterItem>  extends DefaultClusterRenderer<MapClusterItem> {

    public CustomRendered(Context context, GoogleMap map,
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
    @Override
    protected boolean shouldRenderAsCluster(Cluster<MapClusterItem> cluster) {
        //start clustering if at least 2 items overlap
        return cluster.getSize() > 1;
    }
}