plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
    id("com.vanniktech.maven.publish.base")
}

group = "com.interxis"
version = "1.8.2"

kotlin {
    androidTarget {
        publishLibraryVariants("release")
    }

    jvm()

    explicitApi()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.github.pdvrieze.xmlutil:serialization:0.86.1")
                implementation("io.github.pdvrieze.xmlutil:serialutil:0.86.1")

                implementation("io.ktor:ktor-client-core:2.3.3")
                implementation("io.ktor:ktor-client-auth:2.3.3")
                implementation("io.ktor:ktor-client-logging:2.3.3")
                implementation("org.slf4j:slf4j-api:2.0.7")
                implementation("io.ktor:ktor-network:2.3.3")
                implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
                implementation("org.jetbrains.kotlinx:atomicfu:0.21.0")

                implementation("com.benasher44:uuid:0.7.0")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    compileSdk = 34

    namespace = "com.interxis.onvifcamera"

    defaultConfig {
        minSdk = 21

        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(11)
    }
}

configure<com.vanniktech.maven.publish.MavenPublishBaseExtension> {
    configure(
        com.vanniktech.maven.publish.KotlinMultiplatform(javadocJar = com.vanniktech.maven.publish.JavadocJar.Empty())
    )
}
