package com.threelineng.freedomnetwork.common.utils

import kotlin.let
import kotlin.text.split

fun String.toDateTime(): String {
    return try {
        this.split("T").let {
            val datePart = it[0] // "2024-06-23"
            val timePart = it[1].split(".")[0] // "23:00:00"
            "$datePart $timePart"
        }
    } catch (e: Exception) {
        this
    }
}