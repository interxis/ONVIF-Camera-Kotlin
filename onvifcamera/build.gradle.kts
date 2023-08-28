import org.jetbrains.kotlin.util.suffixIfNot
plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
    id("com.vanniktech.maven.publish.base")
}

/**
 * Sets version inside the gradle.properties file
 * Usage: ./gradlew setVersion -P version=1.0.0
 */
tasks.register("setVersion") {
    doLast {
        tasks.create<JavaExec>("setVersionExec") {
            val backupFile = rootProject.file("gradle.properties.bak")
            backupFile.delete()
            val propsFile = rootProject.file("gradle.properties")
            propsFile.renameTo(backupFile)

            var version = rootProject.version as String
            version = version.removeSuffix("-SNAPSHOT")
            propsFile.printWriter().use {
                var versionApplied = false
                backupFile.readLines()
                    .forEach { line ->
                        if (line.matches(Regex("version\\s*=.*"))) {
                            versionApplied = true
                            it.println("version = $version")
                        } else {
                            it.println(line)
                        }
                    }
                if (!versionApplied) {
                    it.println("version = $version")
                }
            }

            println("Setting new version for ${rootProject.name} to $version")
        }
    }
}

tasks.register("setSnapshot") {
    doLast {
        tasks.create<JavaExec>("setSnapshotExec") {
            val backupFile = rootProject.file("gradle.properties.bak")
            backupFile.delete()
            val propsFile = rootProject.file("gradle.properties")
            propsFile.renameTo(backupFile)

            var version = rootProject.version as String
            version = version.suffixIfNot("-SNAPSHOT")
            propsFile.printWriter().use {
                var versionApplied = false
                backupFile.readLines()
                    .forEach { line ->
                        if (line.matches(Regex("version\\s*=.*"))) {
                            versionApplied = true
                            it.println("version = $version")
                        } else {
                            it.println(line)
                        }
                    }
                if (!versionApplied) {
                    it.println("version = $version")
                }
            }
            println("Setting new version for ${rootProject.name} to $version")
        }
    }
}

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

                implementation("com.benasher44:uuid:0.8.1")
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
    coordinates("io.github.interxis", "onvifcamera", "$version")
}
