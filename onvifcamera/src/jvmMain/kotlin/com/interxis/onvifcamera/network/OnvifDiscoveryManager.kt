package com.interxis.onvifcamera.network

import org.slf4j.Logger

public fun OnvifDiscoveryManager(logger: Logger? = null): OnvifDiscoveryManager {
    val socketListener = JvmSocketListener(logger)
    return OnvifDiscoveryManagerImpl(socketListener)
}