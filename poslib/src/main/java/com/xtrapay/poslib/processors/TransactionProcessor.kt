package com.xtrapay.poslib.processors

import android.content.Context
import android.util.Log
import com.tegritech.poslib.comms.SocketRequest
import com.tegritech.poslib.extensions.*
import com.tegritech.poslib.utils.*
import com.solab.iso8583.IsoMessage
import com.solab.iso8583.IsoType
import com.solab.iso8583.IsoValue
import com.xtrapay.poslib.entities.CardData
import com.xtrapay.poslib.entities.ConfigData
import com.xtrapay.poslib.entities.HostConfig
import com.xtrapay.poslib.entities.TransactionRequestData
import com.xtrapay.poslib.entities.TransactionResponse
import com.xtrapay.poslib.entities.TransactionType
import com.xtrapay.poslib.entities.clearSessionKey
import com.xtrapay.poslib.utils.IsoAdapter
import java.util.*
import kotlin.math.absoluteValue


/**
 *
 * @property hostConfig
 */
class TransactionProcessor(private val hostConfig: HostConfig) {

    //Constants from Nibss specification
    private val posConditionCode = "00"
    private val pinCaptureMode = "12"
    private val amountTransactionFee = "D00000000"

    private val posDataCode = "51011151134C101"
    private val posDataCodeCTLS = "51010171134C101"
    private val requestHandler = SocketRequest(hostConfig.connectionData)

    private lateinit var requestIsoMessage: IsoMessage

    private var transactionTimeInMillis = 0L


    private fun setBaseFields(requestData: TransactionRequestData, cardData: CardData, configData: ConfigData) =
        IsoMessage().apply {
            val timeMgr = IsoTimeManager()
            transactionTimeInMillis = System.currentTimeMillis()

            val transmissionDateAndTime = timeMgr.longDate

            val timeLocalTransaction = timeMgr.time
            val dateLocalTransaction = timeMgr.shortDate

            logTrace("TransactionRequestData :: $requestData")

            val RRN = if(!requestData.passedRRN.isNullOrEmpty()){ requestData.passedRRN } else
                 (System.currentTimeMillis() + secureRandom.nextInt(999999)).absoluteValue.toString().padRight(12, '0').substring(0, 12)
            val sequenceNumber = secureRandom.nextInt(999999).toString()

            val processingCode =
                "${requestData.transactionType.code}${requestData.accountType.code}${IsoAccountType.DEFAULT_UNSPECIFIED.code}"

            type = requestData.transactionType.MTI
            setField(2, IsoValue(IsoType.LLVAR, cardData.pan))
            setField(3, IsoValue(IsoType.ALPHA, processingCode, 6))
            setField(4, IsoValue<String>(IsoType.ALPHA, (requestData.amount + requestData.otherAmount).toString().padLeft(12, '0'), 12))
            setField(7, IsoValue(IsoType.ALPHA, transmissionDateAndTime, 10))
            setField(11, IsoValue<String>(IsoType.NUMERIC, sequenceNumber, 6))
            setField(12, IsoValue(IsoType.ALPHA, timeLocalTransaction, 6))
            setField(13, IsoValue(IsoType.ALPHA, dateLocalTransaction, 4))
            setField(14, IsoValue(IsoType.ALPHA, cardData.expiryDate, 4))
            setField(18, IsoValue(IsoType.ALPHA, configData.merchantCategoryCode, 4))
            setField(22, IsoValue(IsoType.ALPHA, cardData.posEntryMode.padLeft(3, '0'), 3))

            if (cardData.panSequenceNumber.isNotBlank()) {
                setField(23, IsoValue(IsoType.ALPHA, cardData.panSequenceNumber.padLeft(3, '0'), 3))
            }

            setField(25, IsoValue(IsoType.ALPHA, posConditionCode, 2))
            setField(26, IsoValue(IsoType.ALPHA, pinCaptureMode, 2))
            setField(28, IsoValue(IsoType.ALPHA, amountTransactionFee, 9))
            setField(32, IsoValue(IsoType.LLVAR, cardData.acquiringInstitutionIdCode))
            setField(35, IsoValue(IsoType.LLVAR, cardData.track2Data.replace("F", "", true)))
            setField(37, IsoValue(IsoType.ALPHA, RRN, 12))
            setField(40, IsoValue(IsoType.ALPHA, cardData.serviceCode, 3))
            setField(41, IsoValue(IsoType.ALPHA, hostConfig.terminalId, 8))
            setField(42, IsoValue(IsoType.ALPHA, configData.cardAcceptorIdCode, 15))
            setField(43, IsoValue(IsoType.ALPHA, configData.merchantNameLocation, 40))
            setField(49, IsoValue(IsoType.ALPHA, configData.currencyCode, 3))

            cardData.pinBlock?.let { setField(52, IsoValue(IsoType.ALPHA,
                it.uppercase(Locale.ROOT), 16)) }

            if (cardData.nibssIccSubset.isNotBlank()) {
                setField(55, IsoValue(IsoType.LLLVAR, cardData.nibssIccSubset))
            }

            requestData.echoData?.let { setField(Constants.TRANSPORT_ECHO_DATA_59, IsoValue(IsoType.LLLVAR, it)) }

            setField(123, IsoValue(IsoType.LLLVAR, when (cardData.posEntryMode) {
                "071" -> posDataCodeCTLS
                else -> posDataCode
            } ))
            setField(128, IsoValue(IsoType.ALPHA, "", 64))
        }



    private fun setOriginalTransactionData(isoMessage: IsoMessage, requestData: TransactionRequestData) {
        requestData.originalDataElements?.let {
            val originalDataElements = getOriginalDataElementField90(
                    it.originalTransactionType.MTI.toString(16),
                    it.originalAcquiringInstCode,
                    it.originalForwardingInstCode,
                    it.originalSTAN,
                    it.originalTransmissionTime
            )

            val replacementAmounts = getReplacementAmountField95(it.originalAmount, requestData.amount)

            isoMessage.setField(90, IsoValue(IsoType.ALPHA, originalDataElements, 42))
            isoMessage.setField(95, IsoValue(IsoType.ALPHA, replacementAmounts, 42))
        }
    }

    private fun getOriginalDataElementField90(
            originalMTI: String, acquiringInstCode: String,
            forwardingInstCode: String?, originalSTAN: String,
            originalTransmissionDateTime: String
    ): String {
        val acquiringInstitutionCode = acquiringInstCode.padLeft(11, '0')
        val originalForwardingInstitution = forwardingInstCode?.padLeft(11, '0') ?: "0".padLeft(11, '0')

        return originalMTI.padLeft(
                4,
                '0'
        ) + originalSTAN + originalTransmissionDateTime + acquiringInstitutionCode + originalForwardingInstitution
    }


    private fun getReplacementAmountField95(originalAmount: Long, newAmount: Long): String {
        val replacementAmount = originalAmount - newAmount

        return String.format("%012d%012dD00000000D00000000", replacementAmount, replacementAmount)
    }


    /**
     * Send a payment request to processor
     *
     * @param context
     * @param requestData
     * @param cardData
     */
    fun processTransaction(
        context: Context,
        requestData: TransactionRequestData,
        cardData: CardData,
        preProcessing: (request: TransactionResponse) -> Unit = { }
    ): TransactionResponse {

        requestIsoMessage = setBaseFields(requestData, cardData, hostConfig.configData)
        when (requestData.transactionType) {
            TransactionType.PURCHASE_WITH_CASH_BACK -> {
                val additionalAmounts = String.format(
                    "%s05%sD%012d",
                    requestData.accountType.code, hostConfig.configData.currencyCode,
                    requestData.otherAmount
                )
                requestIsoMessage.setField(Constants.ADDITIONAL_AMOUNTS_54, IsoValue(IsoType.LLLVAR, additionalAmounts))
            }

            TransactionType.REVERSAL -> {
                requestIsoMessage.setField(Constants.SYSTEMS_TRACE_AUDIT_NUMBER_11, IsoValue<String>(IsoType.ALPHA,
                        requestData.originalDataElements!!.originalSTAN, 6))

                requestIsoMessage.setField(Constants.TIME_LOCAL_TRANSACTION_12, IsoValue<String>(IsoType.ALPHA,
                        requestData.originalDataElements?.originalTransmissionTime?.substring(4), 6))

                requestIsoMessage.setField(Constants.DATE_LOCAL_TRANSACTION_13, IsoValue<String>(IsoType.ALPHA,
                        requestData.originalDataElements?.originalTransmissionTime?.substring(0, 4), 4))



                requestData.originalDataElements?.originalRRN.let {
                    requestIsoMessage.setField(37, IsoValue(IsoType.ALPHA, it, 12))
                }

                requestData.originalDataElements?.originalAuthorizationCode?.let {
                    requestIsoMessage.setField(
                            Constants.AUTHORIZATION_CODE_38,
                            IsoValue<String>(IsoType.ALPHA, it, 6)
                    )
                }

                requestIsoMessage.removeFields(Constants.INTEGRATED_CIRCUIT_CARD_SYSTEM_RELATED_DATA_55)
                requestIsoMessage.setField(
                        Constants.MESSAGE_REASON_CODE_56, IsoValue<String>(
                        IsoType.LLLVAR,
                        MessageReasonCode.UnSpecified.code )
                )

                setOriginalTransactionData(requestIsoMessage, requestData)
            }

            TransactionType.REFUND -> {
                requestData.originalDataElements?.originalAuthorizationCode?.let {
//                    requestIsoMessage.setField(
//                        Constants.AUTHORIZATION_CODE_38,
//                        IsoValue<String>(IsoType.ALPHA, it, 6)
//                    )
                }
                //TODO I removed field 90 and 95 from refund cos of Nibss certification pointers
                //setOriginalTransactionData(requestIsoMessage, requestData)
            }

            TransactionType.PRE_AUTHORIZATION_COMPLETION -> {
                requestData.originalDataElements?.originalAuthorizationCode?.let {
                    requestIsoMessage.setField(
                        Constants.AUTHORIZATION_CODE_38,
                        IsoValue<String>(IsoType.ALPHA, it, 6)
                    )
                    requestIsoMessage.removeFields(52)
                }

                requestData.originalDataElements?.let {
                    val originalDataElements = getOriginalDataElementField90(
                        it.originalTransactionType.MTI.toString(16),
                        it.originalAcquiringInstCode,
                        it.originalForwardingInstCode,
                        it.originalSTAN,
                        it.originalTransmissionTime
                    )
                    requestIsoMessage.setField(90, IsoValue(IsoType.ALPHA, originalDataElements, 42))
                }

            }

            else -> {}
        }
        IsoAdapter.logIsoMessage(requestIsoMessage)

        var messageString = String(requestIsoMessage.writeData()).trim { it <= ' ' }
        val hash = messageString.generateHash256Value(hostConfig.keyHolder.clearSessionKey)
        messageString += hash.uppercase(Locale.getDefault())
//        println(messageString)

        val isoMsgByteArray = IsoAdapter.prepareByteStream(messageString.toByteArray(charset("UTF-8")))

        try {
            preProcessing(requestIsoMessage.toTransactionResponse().apply {
                transactionTimeInMillis = this@TransactionProcessor.transactionTimeInMillis
            })
        } catch (e: Exception){
            Log.i(this.javaClass.simpleName, "processTransaction: Before processing")
            e.printStackTrace()
        }

        val transactionResponse: TransactionResponse = try {
            val response = requestHandler.send(context, isoMsgByteArray)
            IsoAdapter.processISOBitStreamWithJ8583(context, response)
                .toTransactionResponse()

        } catch (e: Exception) {
            e.printStackTrace()
            rollback(context)
        }

      return   transactionResponse.apply {
            transactionTimeInMillis = this@TransactionProcessor.transactionTimeInMillis
        }
    }


    /**
     * Roll back a transaction. If initialIsoMessage is specified, that rollback request
     * is built using that transaction, else the last sent transaction is rolled-back.
     *
     * @param context
     * @param initialIsoMessage Optional
     * @param sessionKey Optional
     */
    fun rollback(
        context: Context, initialIsoMessage: IsoMessage = requestIsoMessage,
        sessionKey: String = hostConfig.keyHolder.clearSessionKey
    ): TransactionResponse {
        val timeMgr = IsoTimeManager()

        val originalTranType = initialIsoMessage.getTransactionType()

        val originalMTI = initialIsoMessage.type.toString(16)
        val originalSTAN = initialIsoMessage.getField<String>(Constants.SYSTEMS_TRACE_AUDIT_NUMBER_11).value
        val originalTransmissionDateTime = initialIsoMessage.getField<String>(Constants.TRANSMISSION_DATE_TIME_7).value
        val acquiringInstCode = initialIsoMessage.getField<String>(Constants.ACQUIRING_INSTITUTION_ID_CODE_32).value
        val forwardingInstCode: String? =
            if (initialIsoMessage.hasField(Constants.FORWARDING_INSTITUTION_IDENTIFICATION_33)) {
                initialIsoMessage.getField<String>(Constants.FORWARDING_INSTITUTION_IDENTIFICATION_33).value
            } else {
                null
            }

        val originalDataElements = getOriginalDataElementField90(
            originalMTI,
            acquiringInstCode,
            "",
            originalSTAN,
            originalTransmissionDateTime
        )
        val reversalReasonCode = MessageReasonCode.Timeout.code

        val processingCode =
            "${TransactionType.REVERSAL.code}${initialIsoMessage.getField<String>(Constants.PROCESSING_CODE_3).value?.substring(2..3)}${IsoAccountType.DEFAULT_UNSPECIFIED.code}"

        initialIsoMessage.removeFields(Constants.ADDITIONAL_AMOUNTS_54)
        initialIsoMessage.removeFields(Constants.INTEGRATED_CIRCUIT_CARD_SYSTEM_RELATED_DATA_55)

        initialIsoMessage.type = Constants.MTI.REVERSAL_ADVICE_MTI.toInt(16)
        initialIsoMessage.setField(3, IsoValue(IsoType.ALPHA, processingCode, 6))
        initialIsoMessage.setField(7, IsoValue(IsoType.ALPHA, timeMgr.longDate, 10))
        initialIsoMessage.setField(Constants.MESSAGE_REASON_CODE_56, IsoValue(IsoType.LLLVAR, reversalReasonCode))
        initialIsoMessage.setField(90, IsoValue(IsoType.ALPHA, originalDataElements, 42))
        initialIsoMessage.setField(95, IsoValue(IsoType.ALPHA, "000000000000000000000000D00000000D00000000", 42))

        IsoAdapter.logIsoMessage(initialIsoMessage)

        var messageString = String(initialIsoMessage.writeData()).trim { it <= ' ' }
        val hash = messageString.generateHash256Value(sessionKey)
        messageString += hash.uppercase(Locale.getDefault())

//        println(messageString)

        val isoMsgByteArray = IsoAdapter.prepareByteStream(messageString.toByteArray(charset("UTF-8")))
        try {
            val response = requestHandler.send(context, isoMsgByteArray)
            val parsedResponse = IsoAdapter.processISOBitStreamWithJ8583(context, response)

            val responseCode = parsedResponse.getField<String>(Constants.RESPONSE_CODE_39).value
            initialIsoMessage.setField(
                Constants.RESPONSE_CODE_39,
                IsoValue(IsoType.NUMERIC, if (responseCode == "00") "06" else responseCode, 2)
            )
        } catch (e: Exception) {
            initialIsoMessage.setField(Constants.RESPONSE_CODE_39, IsoValue(IsoType.NUMERIC, "20", 2))
        }

       return initialIsoMessage.toTransactionResponse().apply {
            transactionType = originalTranType
            transactionTimeInMillis = this@TransactionProcessor.transactionTimeInMillis
       }

    }


}