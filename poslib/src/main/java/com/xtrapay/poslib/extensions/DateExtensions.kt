package com.tegritech.poslib.extensions

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.convertDate(outputPattern: String): String {
    val sdf = SimpleDateFormat(outputPattern)
    return sdf.format(this)
}

fun String.convertDate(outputPattern: String): String {
    val sdf = SimpleDateFormat(outputPattern)
    val date = sdf.parse(this.toString())
    return sdf.format(date)
}

fun String.convertLongDate(outputPattern: String): String {
    val sdf = SimpleDateFormat(outputPattern)
    val timestamp = Timestamp(this.toLong())
    val date = Date(timestamp.time)
    return sdf.format(date)
}

fun String.formatReceiptDate(): String {
    val inputSdf = SimpleDateFormat("yyyy-MM-dd hh:mm aa")
    val date = inputSdf.parse(this)
    return date.formatReceiptDate()
}

fun String.formatReceiptDate(pattern: String): String {
    val date = Date(this.toLong())
    return date.formatReceiptDate()
}

fun Date.formatReceiptDate(): String {
    return java.text.SimpleDateFormat("MMM dd,  yyyy  hh:mm aa", Locale.ROOT).format(this)
}

fun Date.formateDate(outputPattern: String): String {
    val sdf = SimpleDateFormat(outputPattern)
    return sdf.format(this)
}
