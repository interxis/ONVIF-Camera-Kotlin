package com.seanproctor.onvifcamera

import com.seanproctor.onvifcamera.OnvifCommands.deviceInformationCommand
import com.seanproctor.onvifcamera.OnvifCommands.getSnapshotURICommand
import com.seanproctor.onvifcamera.OnvifCommands.getStreamURICommand
import com.seanproctor.onvifcamera.OnvifCommands.profilesCommand
import com.seanproctor.onvifcamera.OnvifCommands.servicesCommand
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.core.*

/**
 * Informs us of what and where to send to the device
 */
internal enum class OnvifRequestType {

    GetServices,
    GetDeviceInformation,
    GetProfiles,
    GetStreamURI,
    GetSnapshotURI;

    fun namespace(): String =
        when (this) {
            GetServices, GetDeviceInformation -> "http://www.onvif.org/ver10/device/wsdl"
            GetProfiles, GetStreamURI, GetSnapshotURI -> "http://www.onvif.org/ver20/media/wsdl"
        }
}

/**
 * @author Remy Virin on 04/03/2018.
 * This class represents an ONVIF device and contains the methods to interact with it
 * (getDeviceInformation, getProfiles and getStreamURI).
 * @param hostname The IP address of the camera
 * @param username the username to login on the camera
 * @param password the password to login on the camera
 * @param namespaceMap a mapping of SOAP namespaces to URI paths
 */
public class OnvifDevice internal constructor(
    public val hostname: String,
    public val username: String?,
    public val password: String?,
    public val namespaceMap: Map<String, String>,
    public val debug: Boolean,
) {

    public suspend fun getDeviceInformation(): OnvifDeviceInformation {
        val path = pathForRequest(OnvifRequestType.GetDeviceInformation)
        val response = execute(hostname, path, deviceInformationCommand, username, password, debug)
        return parseOnvifDeviceInformation(response)
    }

    public suspend fun getProfiles(): List<MediaProfile> {
        val path = pathForRequest(OnvifRequestType.GetProfiles)
        val response = execute(hostname, path, profilesCommand, username, password, debug)
        return parseOnvifProfiles(response)
    }

    public suspend fun getStreamURI(
        profile: MediaProfile,
        addCredentials: Boolean = false
    ): String {
        val path = pathForRequest(OnvifRequestType.GetStreamURI)
        val response =
            execute(hostname, path, getStreamURICommand(profile), username, password, debug)
        val uri = parseOnvifStreamUri(response)
        return if (addCredentials) {
            appendCredentials(uri)
        } else {
            uri
        }
    }

    public suspend fun getSnapshotURI(profile: MediaProfile): String {
        val path = pathForRequest(OnvifRequestType.GetSnapshotURI)
        val response =
            execute(hostname, path, getSnapshotURICommand(profile), username, password, debug)
        return parseOnvifStreamUri(response)
    }

    private fun pathForRequest(requestType: OnvifRequestType): String {
        return namespaceMap[requestType.namespace()] ?: "/onvif/device_service"
    }

    /**
     * Util method to append the credentials to the rtsp URI
     * Working if the camera is behind a firewall.
     * @param original the URI to modify
     * @return the URI with the credentials
     */
    private fun appendCredentials(original: String): String {
        // Do nothing if we don't have a username and password
        if (username == null || password == null) {
            return original
        }

        val url = Url(original)

        val port = if (url.port > 0) {
            ":${url.port}"
        } else {
            ""
        }

        return url.protocol.toString() + "://" + username + ":" + password + "@" +
                url.host + port + url.encodedPathAndQuery
    }

    public companion object {
        public suspend fun requestDevice(
            hostname: String,
            username: String?,
            password: String?,
            debug: Boolean = false,
        ): OnvifDevice {
            val result =
                execute(
                    hostname,
                    "/onvif/device_service",
                    servicesCommand,
                    username,
                    password,
                    debug
                )
            val namespaceMap = parseOnvifServices(result)
            return OnvifDevice(hostname, username, password, namespaceMap, debug)
        }

        internal suspend fun execute(
            hostname: String,
            urlPath: String,
            command: String,
            username: String?,
            password: String?,
            debug: Boolean,
        ): String {
            HttpClient {
                if (username != null && password != null) {
                    install(Auth) {
                        basic {
                            credentials {
                                BasicAuthCredentials(username = username, password = password)
                            }
                        }
                        customDigest {
                            credentials {
                                DigestAuthCredentials(username = username, password = password)
                            }
                        }
                    }
                }
                if (debug) {
                    install(Logging) {
                        logger = Logger.DEFAULT
                        level = LogLevel.ALL
                    }
                }
            }.use { client ->
                val response = client.post {
                    url {
                        protocol = URLProtocol.HTTP
                        host = hostname
                        path(urlPath)
                    }
                    contentType(Soap)
                    setBody(command)
                }
                if (response.status.value in 200..299) {
                    return response.body()
                } else {
                    throw when (response.status.value) {
                        401 -> OnvifUnauthorized("Unauthorized")
                        403 -> OnvifForbidden("Forbidden")
                        else -> OnvifInvalidResponse("Invalid response from device: ${response.status}")
                    }
                }
            }
        }
    }
}

private val Soap: ContentType =
    ContentType(
        contentType = "application",
        contentSubtype = "soap+xml",
        parameters = listOf(HeaderValueParam("charset", "utf-8"))
    )