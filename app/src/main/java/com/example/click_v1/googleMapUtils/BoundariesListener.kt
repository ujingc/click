package com.example.click_v1.googleMapUtils

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

internal class BoundariesListener(
        private val map: GoogleMap,
) : GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveStartedListener {

    private val _boundariesFlow = MutableSharedFlow<LatLngBounds>(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val boundariesFlow: Flow<LatLngBounds> = _boundariesFlow

    override fun onCameraIdle() {
        val boundaries = map.projection.visibleRegion.latLngBounds
        _boundariesFlow.tryEmit(boundaries)
    }

    override fun onCameraMoveStarted(p0: Int) {
        val boundaries = map.projection.visibleRegion.latLngBounds
        _boundariesFlow.tryEmit(boundaries)
    }
}