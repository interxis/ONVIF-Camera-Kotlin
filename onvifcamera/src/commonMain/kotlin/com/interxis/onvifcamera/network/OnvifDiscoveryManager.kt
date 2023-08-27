package com.interxis.onvifcamera.network

import com.interxis.onvifcamera.DiscoveredOnvifDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

public interface OnvifDiscoveryManager {
    public fun discoverDevices(
        retryCount: Int = 1,
        scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
    ): Flow<List<DiscoveredOnvifDevice>>
}