package com.example.click_v1.googleMapUtils;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;

import kotlinx.coroutines.channels.BufferOverflow;
import kotlinx.coroutines.flow.MutableSharedFlow;

public class BoundariesListenerJ implements GoogleMap.OnCameraIdleListener {
    private GoogleMap googleMap;
    private LatLngBounds boundaries;

    private MutableSharedFlow boundariesFlow;

    public BoundariesListenerJ(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public void onCameraIdle() {
        boundaries = googleMap.getProjection().getVisibleRegion().latLngBounds;
//        boundariesFlow = MutableSharedFlow<LatLngBounds>(
//                replay = 1,
//                onBufferOverflow = BufferOverflow.DROP_OLDEST,)


//        boundariesFlow.tryEmit(boundaries);
    }
}
