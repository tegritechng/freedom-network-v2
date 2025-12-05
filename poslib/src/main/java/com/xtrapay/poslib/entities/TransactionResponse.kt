package com.xtrapay.poslib.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.tegritech.poslib.utils.Constants
import com.tegritech.poslib.utils.IsoAccountType
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class TransactionResponse(
    var terminalId: String,
    var merchantId: String,
    var transactionType: TransactionType,
    var maskedPan: String,
    var amount: Long,

    var transmissionDateTime: String,
    var STAN: String,
    @PrimaryKey var RRN: String,
    var localTime_12: String,
    var localDate_13: String,
    var otherAmount: Long = 0,
    var acquiringInstCode: String = "",
    var originalForwardingInstCode: String = "",
    var authCode: String = "",
    var responseCode: String = "",
    var additionalAmount_54: String = "",
    var echoData: String? = null,

    var cardLabel: String = "",
    var cardExpiry: String = "",
    var cardHolder: String = "",
    var TVR: String = "",
    var TSI: String = "",
    var AID: String = "",
    var appCryptogram: String = "",
    var transactionTimeInMillis: Long = 0L,
    var accountType: IsoAccountType = IsoAccountType.DEFAULT_UNSPECIFIED,

    var metaData: String? = null,
    var serviceType: String? = null,

    @Ignore var responseDE55: String? = null
) : Parcelable {
    var errorMessage: String? = null
    var isNotified = false
    var isPosBridge = false
    @Ignore var iccData = ""

    constructor() : this("",  "", TransactionType.PURCHASE, "", 0L, "", "", "", "", "")
}

val TransactionResponse.isApproved: Boolean
    get() = responseCode == "00"

val TransactionResponse.responseMessage: String
    get() =   errorMessage ?: "${Constants.getResponseMessageFromCode(responseCode)}($responseCode)"


data class AccountBalance(val accountType: IsoAccountType,
                          val amountType: String,
                          val currencyCode: String,
                          val amountSign: Char,
                          val amount: Long)


private fun parseAdditionalAmountString(inputString: String): AccountBalance {
    if (inputString.length < 20) error("Invalid string")

    val accountType = IsoAccountType.parseIntAccountType(inputString.substring(0, 2).toInt())
    val amountType = inputString.substring(2, 4)
    val currencyCode = inputString.substring(4, 7)
    val amountSign = inputString[7]
    val amount = inputString.substring(8, 20).toLong()

    return AccountBalance(accountType, amountType, currencyCode, amountSign, amount)
}

fun parseField54AdditionalAmount(inputString: String): List<AccountBalance> {
    if (inputString.length < 20) return listOf()

    val list = ArrayList<AccountBalance>()
    var count = 0
    do {
        list.add(parseAdditionalAmountString(inputString.substring(count, count + 20)))
        count += 20
    } while (count + 20 <= inputString.length)

    return list
}

fun currencySymbolFromCode(code: Int) =  when (code) {
    566 ->  "\u20A6 "
    840 ->  "\u0024 "
    978 ->  "\u20AC "
    826 ->  "\u00A3 "
    156 ->  "\u00A5 "
    36 ->  "AUD "
    952 ->  "XOF "
    950 ->  "XAF "
    else ->  "    "
}
