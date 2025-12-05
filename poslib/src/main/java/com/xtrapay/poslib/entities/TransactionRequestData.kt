package com.xtrapay.poslib.entities

import android.os.Parcelable
import com.tegritech.poslib.utils.IsoAccountType
import kotlinx.android.parcel.Parcelize

/**
 * Payment request data.
 * otherAmount property is required for PURCHASE WITH CASHBACK transactions
 * For SALES COMPLETION, REFUND and REVERSAL transactions, originalDataElements property is required
 *
 * @property transactionType
 * @property amount
 * @property otherAmount
 * @property accountType
 * @property echoData
 * @property originalDataElements
 */
@Parcelize
data class TransactionRequestData(var transactionType: TransactionType,
                                  var amount: Long,
                                  var otherAmount: Long = 0,
                                  var accountType: IsoAccountType = IsoAccountType.DEFAULT_UNSPECIFIED,
                                  var echoData: String? = null,
                                  var originalDataElements: OriginalDataElements? = null,
                                  var passedRRN: String?  = null
): Parcelable

/**
 *
 *
 * @property originalTransactionType
 * @property originalAmount
 * @property originalAuthorizationCode
 * @property originalTransmissionTime
 * @property originalSTAN
 * @property originalRRN
 * @property originalAcquiringInstCode
 * @property originalForwardingInstCode
 */
@Parcelize
data class OriginalDataElements(val originalTransactionType: TransactionType,
                                val originalAmount: Long = 0L,
                                val originalAuthorizationCode: String? = null,
                                val originalTransmissionTime: String = "",
                                val originalSTAN: String = "",
                                val originalRRN: String = "",
                                val originalAcquiringInstCode: String = "",
                                val originalForwardingInstCode: String? = null): Parcelable



fun TransactionResponse.toOriginalDataElements() = OriginalDataElements(
        originalTransactionType = transactionType,
        originalAmount =  amount,
        originalAuthorizationCode = authCode,
        originalTransmissionTime = transmissionDateTime,
        originalSTAN = STAN,
        originalRRN = RRN,
        originalAcquiringInstCode = acquiringInstCode,
        originalForwardingInstCode = originalForwardingInstCode
)