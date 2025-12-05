package com.tegritech.poslib.comms

import android.content.Context
import com.tegritech.poslib.BuildConfig
import com.tegritech.poslib.comms.SSLManager.getSSLSocketFactory
import com.xtrapay.poslib.entities.ConnectionData
import com.tegritech.poslib.extensions.logTrace
import java.io.EOFException
import java.io.IOException
import java.net.*
import javax.net.ssl.SSLSocket

class SocketRequest
/**
 *
 * @param connectionData
 */
    (private val connectionData: ConnectionData) {

    /**
     *
     * @param isoStream byte[]
     * @param context Context
     * @return response String
     * @throws Exception
     */
    @Throws(Exception::class)
    fun send(context: Context, isoStream: ByteArray): ByteArray {

        if (connectionData.isSSL) {
            val sslsocket = getConnection(
                context,
                connectionData.ipAddress,
                connectionData.ipPort,
                -1//connectionData.certFileResId
            )
            sslsocket.soTimeout = 60 * 1000

            logConnection("isSSL: true, ipAddress: ${sslsocket.remoteSocketAddress}")
            return sendSocketRequest(isoStream, sslsocket)
        } else {
            val socket = Socket(connectionData.ipAddress, connectionData.ipPort)
            socket.soTimeout = 360 * 1000
            logConnection("isSSL: true, ipAddress: ${socket.remoteSocketAddress}")
            return sendSocketRequest(isoStream, socket)
        }
    }

    private fun logConnection(message: String){
        if(BuildConfig.DEBUG){
            logTrace(message)
        }
    }


    /**
     *
     * @param isoStream byte[]
     * @param socket Socket
     * @return response String
     * @throws Exception
     */

    @Throws(Exception::class)
    private fun sendSocketRequest(isoStream: ByteArray, socket: Socket): ByteArray {
        lateinit var responseBytes: ByteArray

        try {
            socket.getOutputStream().write(isoStream)

            socket.getInputStream().use {
                responseBytes = it.readBytes()
            }

        } catch (eof: EOFException) {
        } catch (e: SocketTimeoutException) {
            throw SocketTimeoutException("Connection timed out, failed to receive response from remote server")
        } catch (e: ConnectException) {
            throw RuntimeException("Could not connect to the internet, check your connection settings and try again")
        } catch (e: NoRouteToHostException) {
            throw RuntimeException("Could not connect with remote server, check your connection settings and try again")
        } catch (e: PortUnreachableException) {
            throw RuntimeException("Could not connect with remote server, port is unreachable, check your connection settings and try again")
        } catch (e: MalformedURLException) {
            throw RuntimeException("Malformed url, check your connection settings and try again")
        } catch (e: BindException) {
            throw RuntimeException("Could not bind socket to local address or port, check your connection settings and try again")
        } catch (e: SocketException) {
            throw RuntimeException("Could not create socket, check your connection settings and try again")
        } catch (e: UnknownHostException) {
            throw RuntimeException("Host address could not be recognized, check your connection settings and try again")
        } catch (e: UnknownServiceException) {
            throw RuntimeException("Unknown service, check your connection settings and try again")
        } catch (e: Exception) {
            throw RuntimeException(e)
        } finally {
            try {
                socket.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        return responseBytes
    }

    /**
     *
     * @param ip
     * @param port
     * @param context
     * @return SSLSocket
     * @throws IOException
     */
    @Throws(IOException::class)
    fun getConnection(context: Context, ip: String, port: Int, certFileResId: Int): SSLSocket {
        val sslFactory =  if (certFileResId > 0) {
            val trustFactory = SSLManager.getTrustManagerFactory(context, certFileResId)
            getSSLSocketFactory(trustManagerFactory = trustFactory)
        } else {
            SSLManager.getTrustySSLSocketFactory()
        }

        return SSLManager.createSocket(sslFactory, ip, port) as SSLSocket
    }
}