package com.interxis.onvifcamera.parsers

import com.interxis.onvifcamera.parseOnvifProbeResponse
import com.interxis.onvifcamera.readResourceFile
import kotlin.test.Test
import kotlin.test.assertEquals

class ProbeParserTest {
    @Test
    fun testProbeResponseParser() {
        val input = readResourceFile("probeResponse.xml")
        val result = parseOnvifProbeResponse(input)
        assertEquals(1, result.size)
    }

    @Test
    fun testProbeResponse2Parser() {
        val input = readResourceFile("probeResponse2.xml")
        val result = parseOnvifProbeResponse(input)
        assertEquals(1, result.size)
    }
}