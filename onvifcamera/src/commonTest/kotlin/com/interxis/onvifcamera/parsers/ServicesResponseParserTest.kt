package com.interxis.onvifcamera.parsers

import com.interxis.onvifcamera.parseOnvifServices
import com.interxis.onvifcamera.readResourceFile
import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertNotNull

class ServicesResponseParserTest {
    @Test
    fun testServicesResponseParser() {
        val input = readResourceFile("services.xml")
        val result = parseOnvifServices(input)
        val namespace = result["http://www.onvif.org/ver10/search/wsdl"]
        assertNotNull(namespace)
        assertContains(namespace, "/onvif/services")
    }
}