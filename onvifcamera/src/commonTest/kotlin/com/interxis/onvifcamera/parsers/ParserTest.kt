package com.interxis.onvifcamera.parsers

import com.interxis.onvifcamera.parseOnvifDeviceInformation
import com.interxis.onvifcamera.parseOnvifProfiles
import com.interxis.onvifcamera.parseOnvifSnapshotUri
import com.interxis.onvifcamera.parseOnvifStreamUri
import com.interxis.onvifcamera.readResourceFile
import kotlin.test.Test
import kotlin.test.assertEquals

class ParserTest {
    @Test
    fun testStreamUriResponseParser() {
        val input = readResourceFile("./stream.xml")
        val result = parseOnvifStreamUri(input)
        assertEquals("rtsp://192.168.0.209/onvif-media/media.amp?profile=profile_1_h264&sessiontimeout=60&streamtype=unicast", result)
    }

    @Test
    fun testSnapshotUriResponseParser() {
        val input = readResourceFile("snapshot.xml")
        val result = parseOnvifSnapshotUri(input)
        assertEquals("http://192.168.0.209/onvif-cgi/jpg/image.cgi?resolution=1920x1080&compression=30", result)
    }

    @Test
    fun testProfilesResponseParser() {
        val input = readResourceFile("profiles.xml")
        val result = parseOnvifProfiles(input)
        assertEquals(2, result.size)
    }

    @Test
    fun testDeviceInfoResponseParser() {
        val input = readResourceFile("deviceInfo.xml")
        val result = parseOnvifDeviceInformation(input)
        assertEquals("AXIS", result.manufacturer)
    }
}