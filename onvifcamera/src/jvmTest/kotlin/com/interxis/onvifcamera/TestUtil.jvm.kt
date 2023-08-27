package com.interxis.onvifcamera

actual fun readResourceFile(filename: String): String {
    return ClassLoader
        .getSystemResourceAsStream(filename)!!
        .readBytes()
        .decodeToString()
}