plugins {
    id("com.android.application")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    androidTarget()
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":onvifcamera"))

                implementation("io.ktor:ktor-client-core:2.3.5")
                implementation("io.ktor:ktor-client-cio:2.3.4")
                implementation("io.ktor:ktor-client-auth:2.3.4")
                implementation("io.ktor:ktor-client-logging:2.3.4")

                // Compose dependencies
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)

                // MOKO ModelView
                api("dev.icerock.moko:mvvm-core:0.16.1")
                api("dev.icerock.moko:mvvm-flow:0.16.1")

                // Logging
                implementation("io.github.aakira:napier:2.6.1")

                // SSDP
                implementation("com.seanproctor:lighthouse:0.2.0")
            }
        }
        val androidMain by getting {
            dependencies {
                // Android presentation components
                implementation("androidx.activity:activity-compose:1.7.2")
                implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
                implementation("dev.icerock.moko:mvvm-flow-compose:0.16.1")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

android {
    namespace = "com.interxis.onvifdemo"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.interxis.onvifdemo"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(11)
    }
}

compose {
    desktop {
        application {
            mainClass = "com.interxis.onvifdemo.MainKt"
        }
    }
}
