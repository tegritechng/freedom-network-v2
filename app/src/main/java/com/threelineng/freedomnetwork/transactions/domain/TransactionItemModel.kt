package com.threelineng.freedomnetwork.transactions.domain

import android.os.Parcelable
import com.threelineng.freedomnetwork.common.domain.TransactionStatus
import com.threelineng.freedomnetwork.common.utils.generateUniqueSessionId
import com.threelineng.freedomnetwork.common.utils.toDateTime
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.Locale

@Parcelize
data class TransactionItemModel(
    val title: String,
    val amount: Double,
    val status: String,
    val dateTime: String,
    val paymentBodyMap: LinkedHashMap<String, String> = linkedMapOf()
) : Parcelable {

    @IgnoredOnParcel
    val transactionStatus: TransactionStatus
        get() = when (status.lowercase(Locale.getDefault())) {
            "complete", "completed", "approved", "successful" -> {
                TransactionStatus.APPROVED
            }

            "failed", "declined" -> {
                TransactionStatus.FAILED
            }

            "pending" -> {
                TransactionStatus.PENDING
            }

            else -> {
                TransactionStatus.PENDING
            }
        }

    @IgnoredOnParcel
    val trxDateTime = dateTime.toDateTime()

//    @IgnoredOnParcel
//    val inputFormat get() = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH)

//    @IgnoredOnParcel
//
//    val parsedDateTime = try {
//        inputFormat.parse(trxDateTime)
//    } catch (e: Exception) {
//        null
//    }
//    val date = parsedDateTime?.let { SimpleDateFormat("yyyy:MM:dd", Locale.ENGLISH).format(parsedDateTime) } ?: ""
//    val time = parsedDateTime?.let { SimpleDateFormat("hh:mm:ss", Locale.ENGLISH).format(parsedDateTime) } ?: ""

    @IgnoredOnParcel
    val bodyMap = if (paymentBodyMap.isEmpty()) linkedMapOf<String, String>(
        "DATE & TIME" to trxDateTime,
        "TRANS REF" to generateUniqueSessionId(),
    ) else paymentBodyMap


}