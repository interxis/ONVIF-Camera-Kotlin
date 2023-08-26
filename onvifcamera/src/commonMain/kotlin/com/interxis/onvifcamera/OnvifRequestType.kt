package com.interxis.onvifcamera

/**
 * Informs us of what and where to send to the device
 */
internal enum class OnvifRequestType {

    GetServices,
    GetDeviceInformation,
    GetEvents,
    GetProfiles,
    GetStreamURI,
    GetSnapshotURI;

    fun namespace(): String =
        when (this) {
            GetServices, GetDeviceInformation -> "http://www.onvif.org/ver10/device/wsdl"
            GetEvents -> "http://www.onvif.org/ver10/events/wsdl"
            GetProfiles, GetStreamURI, GetSnapshotURI -> "http://www.onvif.org/ver20/media/wsdl"
        }
}
