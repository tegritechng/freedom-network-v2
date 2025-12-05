package com.tegritech.poslib.utils

enum class ReceiptType {
    CUSTOMER_COPY, AGENT_COPY, REPRINT_COPY;

    override fun toString(): String {
        return name.replace('_', ' ')
    }
}

enum class RandomReceiptType {
    CUSTOMER_COPY, MERCHANT_COPY, SUMMARY;

    override fun toString(): String {
        return name.replace('_', ' ')
    }
}