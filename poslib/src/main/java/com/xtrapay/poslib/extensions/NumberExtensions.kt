package com.xtrapay.poslib.extensions

import java.text.DecimalFormat

const val MAX_PHONE_NUMBER_LENGTH = 11
const val NAIRA = '\u20A6'

fun Number.formatAmount(): String {
    return DecimalFormat("#,###.00").format(this)
}

fun Number.formatCurrencyAmount(currencySymbol: String = "\u20A6"): String {
    val format = DecimalFormat("#,###.00")
    return "$currencySymbol ${format.format(this)}"
}

fun Number.formatSingleExponentCurrency(currencyIndicator: String = NAIRA.toString()): String {
    return "$currencyIndicator ${DecimalFormat("#,##0.00").format(this.toDouble() / 100.0)}"
}