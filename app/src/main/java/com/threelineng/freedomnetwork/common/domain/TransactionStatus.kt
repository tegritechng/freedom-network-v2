package com.threelineng.freedomnetwork.common.domain

enum class TransactionStatus(val typeName: String) {
    SUCCESSFUL("Successful"),
    FAILED("Failed"),
    PENDING("Pending");
}