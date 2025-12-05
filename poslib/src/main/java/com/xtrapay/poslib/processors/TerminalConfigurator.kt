package com.xtrapay.poslib.processors

import android.content.Context
import com.tegritech.poslib.comms.SocketRequest
import com.tegritech.poslib.extensions.generateHash256Value
import com.tegritech.poslib.utils.Constants
import com.tegritech.poslib.utils.IsoAccountType
import com.xtrapay.poslib.utils.IsoAdapter
import com.tegritech.poslib.utils.IsoTimeManager
import com.solab.iso8583.IsoMessage
import com.solab.iso8583.IsoType
import com.solab.iso8583.IsoValue
import com.xtrapay.poslib.entities.ConfigData
import com.xtrapay.poslib.entities.ConnectionData
import com.xtrapay.poslib.entities.KeyHolder
import com.xtrapay.poslib.entities.NibssAID
import com.xtrapay.poslib.entities.NibssCA
import com.xtrapay.poslib.entities.TransactionType
import java.util.*

class TerminalConfigurator(connectionData: ConnectionData) {
    private val requestHandler = SocketRequest(connectionData)

    private enum class KeyType {
        MASTER, SESSION, PIN
    }

    private fun parseField62(field62String: String): ConfigData {
        val configData = ConfigData()
        val responseDataCodes = arrayOf(
            ConfigData.TAG_LEN_CALL_HOME_TIME,
            ConfigData.TAG_LEN_CARD_ACCEPTOR_ID_CODE, ConfigData.TAG_LEN_COUNTRY_CODE,
            ConfigData.TAG_LEN_EPMS_DATE_TIME, ConfigData.TAG_LEN_CURRENCY_CODE,
            ConfigData.TAG_LEN_MERCHANT_CATEGORY_CODE,
            ConfigData.TAG_LEN_MERCHANT_NAME_LOCATION,
            ConfigData.TAG_LEN_TIMEOUT
        )


        for (dataCode in responseDataCodes) {
            val data = Constants.getDownloadParameterManagementData(dataCode, field62String)
            when (dataCode) {
                ConfigData.TAG_LEN_CALL_HOME_TIME -> configData.callHomeTime = data
                ConfigData.TAG_LEN_CARD_ACCEPTOR_ID_CODE -> configData.cardAcceptorIdCode = data
                ConfigData.TAG_LEN_COUNTRY_CODE -> configData.countryCode = data
                ConfigData.TAG_LEN_EPMS_DATE_TIME -> configData.epmsDateTime = data
                ConfigData.TAG_LEN_CURRENCY_CODE -> configData.currencyCode = data
                ConfigData.TAG_LEN_MERCHANT_CATEGORY_CODE -> configData.merchantCategoryCode = data
                ConfigData.TAG_LEN_MERCHANT_NAME_LOCATION -> configData.merchantNameLocation = data
                ConfigData.TAG_LEN_TIMEOUT -> configData.hostTimeOut = data
            }
        }

        return configData
    }

    private fun nibssKeyRequest(
        context: Context,
        type: KeyType,
        terminalID: String,
        isPayAttitude: Boolean
    ): String {

        val isoMessage = IsoMessage()
        val timeMgr = IsoTimeManager()

        val requestCode = when (type) {
            KeyType.MASTER -> Constants.IsoTransactionTypeCode.TERMINAL_MASTER_KEY
            KeyType.SESSION -> Constants.IsoTransactionTypeCode.TERMINAL_SESSION_KEY
            KeyType.PIN -> Constants.IsoTransactionTypeCode.TERMINAL_PIN_KEY
        }

        isoMessage.type = Constants.MTI.NETWORK_MGT_REQUEST_MTI.toInt(16)


        isoMessage.setField(
            Constants.PROCESSING_CODE_3,
            IsoValue(
                IsoType.ALPHA,
                requestCode + IsoAccountType.DEFAULT_UNSPECIFIED.code + IsoAccountType.DEFAULT_UNSPECIFIED.code,
                6
            )
        )

        isoMessage.setField(
            Constants.TRANSMISSION_DATE_TIME_7,
            IsoValue(IsoType.NUMERIC, timeMgr.longDate, 10)
        )
        isoMessage.setField(
            Constants.SYSTEMS_TRACE_AUDIT_NUMBER_11,
            IsoValue(IsoType.NUMERIC, timeMgr.time, 6)
        )
        isoMessage.setField(
            Constants.TIME_LOCAL_TRANSACTION_12,
            IsoValue(IsoType.NUMERIC, timeMgr.time, 6)
        )
        isoMessage.setField(
            Constants.DATE_LOCAL_TRANSACTION_13,
            IsoValue(IsoType.NUMERIC, timeMgr.shortDate, 4)
        )
        isoMessage.setField(
            Constants.CARD_ACCEPTOR_TERMINAL_ID_41,
            IsoValue(IsoType.ALPHA, terminalID, 8)
        )

        if (isPayAttitude) {
            isoMessage.setField(
                Constants.TRANSPORT_ECHO_DATA_59,
                IsoValue(IsoType.LLLVAR, "PAYATTITUDE", 11)
            )
        }

//        val terminalSerial = "1490478916"
//        val field62String = String.format("01%03d%s", terminalSerial.length, terminalSerial)
//
//        isoMessage.setField(
//            Constants.PRIVATE_FIELD_MGT_DATA1_62,
//            IsoValue(IsoType.LLLVAR, field62String)
//        )

        IsoAdapter.logIsoMessage(isoMessage)
        val messageBytes = IsoAdapter.prepareByteStream(isoMessage)

        val response = requestHandler.send(context, messageBytes)

        val parsedResponse = IsoAdapter.processISOBitStreamWithJ8583(context, response)

        val responseCode = parsedResponse.getField<String>(Constants.RESPONSE_CODE_39).value
        if (responseCode != "00") {
            throw Exception(Constants.getResponseMessageFromCode(responseCode))
        }

        return parsedResponse.getField<String>(Constants.SECURITY_RELATED_CONTROL_INFO_53).value
    }

    @Throws(Exception::class)
    private fun sendNetworkManagementRequest(
        context: Context,
        terminalID: String,
        sessionKey: String?,
        field62Data: String,
        transactionCode: String,
        responseDataIndex: Int = Constants.PRIVATE_FIELD_MGT_DATA1_62,
        isPayAttitude: Boolean
    ): String {
        val isoMessage = IsoMessage()
        val timeMgr = IsoTimeManager()

        isoMessage.type = Constants.MTI.NETWORK_MGT_REQUEST_MTI.toInt(16)


        isoMessage.setField(
            Constants.PROCESSING_CODE_3,
            IsoValue(
                IsoType.ALPHA,
                transactionCode + IsoAccountType.DEFAULT_UNSPECIFIED.code + IsoAccountType.DEFAULT_UNSPECIFIED.code,
                6
            )
        )

        isoMessage.setField(
            Constants.TRANSMISSION_DATE_TIME_7,
            IsoValue(IsoType.NUMERIC, timeMgr.longDate, 10)
        )
        isoMessage.setField(
            Constants.SYSTEMS_TRACE_AUDIT_NUMBER_11,
            IsoValue(IsoType.NUMERIC, timeMgr.time, 6)
        )
        isoMessage.setField(
            Constants.TIME_LOCAL_TRANSACTION_12,
            IsoValue(IsoType.NUMERIC, timeMgr.time, 6)
        )
        isoMessage.setField(
            Constants.DATE_LOCAL_TRANSACTION_13,
            IsoValue(IsoType.NUMERIC, timeMgr.shortDate, 4)
        )
        isoMessage.setField(
            Constants.CARD_ACCEPTOR_TERMINAL_ID_41,
            IsoValue(IsoType.ALPHA, terminalID, 8)
        )

        if (isPayAttitude) {
            isoMessage.setField(
                Constants.TRANSPORT_ECHO_DATA_59,
                IsoValue(IsoType.LLLVAR, "PAYATTITUDE", 11)
            )
        }

        isoMessage.setField(
            Constants.PRIVATE_FIELD_MGT_DATA1_62,
            IsoValue(IsoType.LLLVAR, field62Data)
        )

        IsoAdapter.logIsoMessage(isoMessage)
        val isoMsgByteArray = if (sessionKey != null) {
            isoMessage.setField(
                Constants.PRIMARY_MESSAGE_HASH_VALUE_64,
                IsoValue(IsoType.ALPHA, "", 64)
            )
            var messageString = String(isoMessage.writeData()).trim { it <= ' ' }
            val hash = messageString.generateHash256Value(sessionKey)
            messageString += hash.uppercase(Locale.getDefault())
//            println(messageString)
            IsoAdapter.prepareByteStream(messageString.toByteArray(charset("UTF-8")))
        } else {
            IsoAdapter.prepareByteStream(isoMessage)
        }

        IsoAdapter.logIsoMessage(isoMessage)
        val response = requestHandler.send(context, isoMsgByteArray)

        val parsedResponse = IsoAdapter.processISOBitStreamWithJ8583(context, response)

        val responseCode = parsedResponse.getField<String>(Constants.RESPONSE_CODE_39).value
        if (responseCode != "00") {
            throw Exception(Constants.getResponseMessageFromCode(responseCode))
        }

        return parsedResponse.getField<String>(responseDataIndex).value
    }

    /**
     * Send Network Management Request to Processor
     * On success it returns the content of the specified response index or if none is specified, the response code
     * On error throws Exception
     *
     * @param context
     * @param transactionType
     * @param terminalID
     * @param sessionKey
     * @param terminalSerial
     * @param responseDataIndex  expected ISO 8583 field index
     */
    fun doNetworkParamDownload(
        context: Context,
        transactionType: TransactionType,
        terminalID: String,
        sessionKey: String,
        terminalSerial: String,
        responseDataIndex: Int = Constants.RESPONSE_CODE_39,
        isPayAttitude: Boolean
    ): String {
        val field62String = String.format("01%03d%s", terminalSerial.length, terminalSerial)

        return sendNetworkManagementRequest(
            context = context,
            terminalID = terminalID,
            sessionKey = sessionKey,
            field62Data = field62String,
            transactionCode = transactionType.code,
            responseDataIndex = responseDataIndex,
            isPayAttitude
        )
    }

    /**
     * Send Network Management Request to Processor
     * On success it returns the content of the specified response index or if none is specified, the response code
     * On error throws Exception
     *
     * @param context
     * @param transactionType
     * @param terminalID
     * @param sessionKey
     * @param responseDataIndex  expected ISO 8583 field index
     */
    fun doNetworkParamDownloadExtended(
        context: Context,
        transactionType: TransactionType,
        terminalID: String,
        sessionKey: String?,
        field62TlvData: String,
        responseDataIndex: Int = Constants.RESPONSE_CODE_39
    ): String {
        return sendNetworkManagementRequest(
            context = context,
            terminalID = terminalID,
            sessionKey = sessionKey,
            field62Data = field62TlvData,
            transactionCode = transactionType.code,
            responseDataIndex = responseDataIndex,
            isPayAttitude = false
        )
    }


    /**
     * Download Key configuration from processor
     * on success: KeyHolder
     *
     * @param context
     * @param terminalID
     */
    fun downloadNibssKeys(context: Context, terminalID: String, isPayAttitude: Boolean): KeyHolder {

        val encryptedMasterKey =
            nibssKeyRequest(
                context,
                KeyType.MASTER,
                terminalID,
                isPayAttitude
            ).substring(0, 32)

        val encryptedSessionKey =
            nibssKeyRequest(
                context,
                KeyType.SESSION,
                terminalID,
                isPayAttitude
            ).substring(0, 32)

        val encryptedPinKey =
            nibssKeyRequest(
                context,
                KeyType.PIN,
                terminalID,
                isPayAttitude
            ).substring(0, 32)


        return KeyHolder(encryptedMasterKey, encryptedSessionKey, encryptedPinKey)
    }


    /**
     * Download terminal configurations from processor
     * on success: ConfigData
     *
     * @param context
     * @param terminalID
     * @param sessionKey
     * @param terminalSerial
     */
    fun downloadTerminalParameters(
        context: Context,
        terminalID: String,
        sessionKey: String,
        terminalSerial: String,
        isPayAttitude: Boolean
    ): ConfigData = parseField62(
        doNetworkParamDownload(
            context,
            TransactionType.TERMINAL_PARAMETER_DOWNLOAD,
            terminalID,
            sessionKey, terminalSerial,
            Constants.PRIVATE_FIELD_MGT_DATA1_62,
            isPayAttitude
        )
    )


    /**
     * Download dynamic CA Public Key from processor
     * on success: List<NibssCA>
     *
     * @param context
     * @param terminalID
     * @param sessionKey
     * @param terminalSerial
     */
    fun downloadNibssCAPK(
        context: Context,
        terminalID: String,
        sessionKey: String,
        terminalSerial: String
    ): List<NibssCA> = NibssCA.parseNibssResponse(
        doNetworkParamDownload(
            context,
            TransactionType.CA_PUBLIC_KEY_DOWNLOAD,
            terminalID, sessionKey, terminalSerial,
            Constants.PRIVATE_FIELD_MGT_DATA2_63,
            isPayAttitude = false
        )
    )


    /**
     * Download dynamic Application Identifiers (AID) from processor
     * on success: List<NibssAID>
     *
     * @param context
     * @param terminalID
     * @param sessionKey
     * @param terminalSerial
     */
    fun downloadNibssAID(
        context: Context,
        terminalID: String,
        sessionKey: String,
        terminalSerial: String
    ): List<NibssAID> = NibssAID.parseNibssResponse(
        doNetworkParamDownload(
            context,
            TransactionType.EMV_APPLICATION_AID_DOWNLOAD,
            terminalID,
            sessionKey,
            terminalSerial,
            Constants.PRIVATE_FIELD_MGT_DATA2_63,
            isPayAttitude = false
        )
    )

    /**
     * Send CallHome request to processor
     * on success: response code
     *
     * @param context
     * @param terminalID
     * @param sessionKey
     * @param terminalSerial
     */
    fun nibssCallHome(
        context: Context,
        terminalID: String,
        sessionKey: String,
        terminalSerial: String
    ): String = doNetworkParamDownload(
        context,
        TransactionType.CALL_HOME,
        terminalID,
        sessionKey,
        terminalSerial,
        isPayAttitude = false
    )

    /**
     * Send EOD request to processor
     * on success: response code
     *
     * @param context
     * @param terminalID
     * @param sessionKey
     * @param terminalSerial
     */
    fun nibssEOD(
        context: Context,
        terminalID: String,
        sessionKey: String,
        terminalSerial: String
    ): String = doNetworkParamDownload(
        context,
        TransactionType.DAILY_TRANSACTION_REPORT_DOWNLOAD,
        terminalID,
        sessionKey,
        terminalSerial,
        isPayAttitude = false
    )

}