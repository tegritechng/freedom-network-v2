package com.xtrapay.poslib.utils


import android.content.Context
import android.util.Log
import com.tegritech.poslib.BuildConfig
import com.tegritech.poslib.R
import com.tegritech.poslib.extensions.hexString
import com.tegritech.poslib.extensions.logTrace
import com.solab.iso8583.IsoMessage
import com.solab.iso8583.MessageFactory
import com.solab.iso8583.parse.ConfigParser
import java.io.UnsupportedEncodingException
import java.text.ParseException



object IsoAdapter{

    fun processISOBitStreamWithJ8583(context: Context,data: ByteArray): IsoMessage {
        if (data.isEmpty()) {
            throw RuntimeException("Connection timeout")
        }
        try {
            return unpackWith8583(context, data.copyOfRange(2, data.size))
        } catch (e: Exception) {
            if (e is java.lang.IndexOutOfBoundsException) {
                throw RuntimeException("Invalid response received")
            }
            throw  e
        }
    }



    private fun unpackWith8583(context: Context, data: ByteArray): IsoMessage {
        val msgFactory = MessageFactory<IsoMessage>()
        msgFactory.ignoreLastMissingField = true

        if (BuildConfig.DEBUG) {
            Log.d("BREAK DOWN DATA: ", String(data))
        }

        try {
            val xmlReader = context.resources.openRawResource(R.raw.config).bufferedReader()
            xmlReader.use {
                ConfigParser.configureFromReader(msgFactory, it)
                val isoMessage =  msgFactory.parseMessage(data, 0)
                logIsoMessage(isoMessage)
                return isoMessage
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            when (e) {
                is IndexOutOfBoundsException, is ParseException ->
                    throw RuntimeException("Invalid response received")
                else ->  throw RuntimeException(e)
            }
        }

    }



    @Throws(Exception::class, UnsupportedEncodingException::class)
    fun prepareByteStream(isoMessage: IsoMessage): ByteArray {

        val isoStream = isoMessage.writeData()

        return prepareByteStream(isoStream)
    }


    @Throws(Exception::class)
    fun prepareByteStream(isoBytes: ByteArray): ByteArray {
        val len =  String(isoBytes).length
        val headerBytes = byteArrayOf( len.shr(8).toByte(), (len % 256).toByte()).also {
            logTrace("Header bytes: ${it.hexString()}")
        }

        val request = headerBytes + isoBytes
        logTrace("Request: ${String(request)}\n Hex: ${request.hexString()}")
        return request

    }


    fun logIsoMessage(msg: IsoMessage){
        if (BuildConfig.DEBUG) {
            logTrace( "----ISO MESSAGE-----")

            try {
                logTrace( " MTI : " + msg.type.toString(16))
                for (i in 1..128) {
                    if (msg.hasField(i)) {
                        logTrace( "    Field-" + i + " : " + getResponseDataFromIndex(msg, i))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                logTrace( "--------------------")
            }
        }
    }

    private fun getResponseDataFromIndex(isoMsg: IsoMessage, index: Int): String {
        val value =  isoMsg.getField<Any>(index).value

        return if (value is ByteArray) value.hexString() else value.toString()
    }

}
