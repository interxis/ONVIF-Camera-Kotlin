import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost

///////////////////////////////////////////////////////////////////////////////
//  GRADLE CONFIGURATION
///////////////////////////////////////////////////////////////////////////////
plugins {
    kotlin("jvm") apply false
    kotlin("multiplatform") apply false
    kotlin("android") apply false
    kotlin("plugin.serialization") apply false
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jetbrains.compose") apply false
    id("com.vanniktech.maven.publish.base") apply false
    id("org.sonarqube")
}

///////////////////////////////////////////////////////////////////////////////
//  APP CONFIGURATION
///////////////////////////////////////////////////////////////////////////////
allprojects {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://www.jitpack.io")
    }

    // Credentials must be added to ~/.gradle/gradle.properties per
    // https://vanniktech.github.io/gradle-maven-publish-plugin/central/#secrets
    plugins.withId("com.vanniktech.maven.publish.base") {
        configure<PublishingExtension> {
            repositories {
                maven {
                    name = "testMaven"
                    url = file("${rootProject.buildDir}/testMaven").toURI()
                }
            }
        }
        @Suppress("UnstableApiUsage")
        configure<MavenPublishBaseExtension> {
            publishToMavenCentral(SonatypeHost.S01)
            signAllPublications()
            pom {
                name.set("ONVIF Camera Kotlin")
                description.set("A Kotlin library to interact with ONVIF cameras.")
                url.set("https://github.com/interxis/ONVIF-Camera-Kotlin/")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://github.com/interxis/ONVIF-Camera-Kotlin/blob/master/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("interxis")
                        name.set("Alexis Martin")
                        email.set("interxis@gmail.com")
                    }
                }
                scm {
                    url.set("https://github.com/interxis/ONVIF-Camera-Kotlin/tree/master")
                    connection.set("scm:git:git://github.com/interxis/ONVIF-Camera-Kotlin.git")
                    developerConnection.set("scm:git:ssh://git@github.com/interxis/ONVIF-Camera-Kotlin.git")
                }
            }
        }
    }
}

///////////////////////////////////////////////////////////////////////////////
//  TASKS CONFIGURATION
///////////////////////////////////////////////////////////////////////////////
tasks {
    sonar {
        properties {
            property("sonar.projectKey", "interxis_ONVIF-Camera-Kotlin")
            property("sonar.organization", "interxis")
            property("sonar.host.url", "https://sonarcloud.io")
        }
    }
}

tasks.wrapper {
    gradleVersion = "8.2.1"
}
