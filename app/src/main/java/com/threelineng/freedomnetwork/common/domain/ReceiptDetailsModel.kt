package com.threelineng.freedomnetwork.common.domain

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

@Parcelize

class ReceiptDetailsModel(
    val status: TransactionStatus,
    val amount: String,
    val bodyMap: LinkedHashMap<String, String>,
) : Parcelable {

}