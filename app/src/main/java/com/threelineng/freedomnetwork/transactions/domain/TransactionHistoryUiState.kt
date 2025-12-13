package com.threelineng.freedomnetwork.transactions.domain

data class TransactionHistoryUiState(
    val transactionList: List<TransactionItemModel> = listOf(),
    val isTransactionListLoading: Boolean = false,
    val isLoadingMoreTransactions: Boolean = false,
    var isEndReached: Boolean = false,
//    var filterModel: FilterModel = FilterModel(),
)