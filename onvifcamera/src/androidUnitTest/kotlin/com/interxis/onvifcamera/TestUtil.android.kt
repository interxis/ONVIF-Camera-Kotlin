package com.interxis.onvifcamera

import java.io.File

actual fun readResourceFile(filename: String): String {
    return File("./src/commonTest/resources/$filename").readText()
}