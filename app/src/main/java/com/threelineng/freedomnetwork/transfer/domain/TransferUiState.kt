package com.threelineng.freedomnetwork.transfer.domain

data class TransferUiState(
    val isTransferring: Boolean = false,
    val selectedBank: BankItemModel? = null,
    val isAccountNameLoading: Boolean = false,
    val accountNumber: String = "",
    val accountName: String? = null,
    val amount: String = "0",
    val walletBalance: String = "0.0",
)