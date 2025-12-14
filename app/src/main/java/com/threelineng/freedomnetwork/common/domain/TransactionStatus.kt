package com.threelineng.freedomnetwork.common.domain

enum class TransactionStatus(val typeName: String) {
    APPROVED("Approved"),
    FAILED("Failed"),
    PENDING("Pending");
}