package com.threelineng.freedomnetwork.transfer.domain

data class BankListUiState(
    val isLoading: Boolean = false,
    val banks: List<BankItemModel> = listOf(),
    val searchValue: String = ""
)