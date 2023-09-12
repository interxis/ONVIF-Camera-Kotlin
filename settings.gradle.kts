rootProject.name = "onvif-camera-kotlin"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    kotlin("plugin.serialization") version "1.9.0" apply false
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("org.jetbrains.compose") version "1.4.3" apply false
    id("com.vanniktech.maven.publish.base") version "0.25.3" apply false
}

include(":onvifcamera")
include(":demo")