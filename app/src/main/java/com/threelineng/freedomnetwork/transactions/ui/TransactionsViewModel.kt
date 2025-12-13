package com.threelineng.freedomnetwork.transactions.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.threelineng.freedomnetwork.transactions.domain.TransactionHistoryUiState
import com.threelineng.freedomnetwork.transactions.domain.TransactionItemModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.plus

class TransactionsViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionHistoryUiState())
    val uiState: StateFlow<TransactionHistoryUiState> get() = _uiState
    private val _errorState: MutableSharedFlow<String> = MutableSharedFlow()
    val errorState: SharedFlow<String> get() = _errorState

    init {
        loadTransactionHistory()
    }

    //    companion object {
//        const val UI_PAGE_SIZE = Pagination.DEFAULT_PAGE_SIZE
//    }
//
//    var pagination: Pagination = Pagination()
//        private set
    private var transactionList: List<TransactionItemModel> = listOf()

    fun loadTransactionHistory(isLoadMore: Boolean = false) {
        if (_uiState.value.isTransactionListLoading || _uiState.value.isEndReached && isLoadMore) {
            return
        }
//        pagination = if (isLoadMore) {
//            pagination.copy(currentPage = pagination.currentPage + 1)
//        } else {
//            reset()
////            resetSearch()
//            Pagination()
//        }
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        isTransactionListLoading = !isLoadMore,
                        isLoadingMoreTransactions = isLoadMore
                    )
                }
//                val data = transactionRepository.getTransactionHistory(
//                    terminalSerial,
//                    _uiState.value.filterModel.startDate,
//                    _uiState.value.filterModel.endDate,
//                    pagination.currentPage
//                )
                val data = listOf<TransactionItemModel>(
                    TransactionItemModel(
                        "Transfer", 500.0, "APPROVED", "2024-06-23T23:00:00"
                    ), TransactionItemModel(
                        "Cash In", 500.0, "FAILED", "2024-06-24T23:00:00"
                    ), TransactionItemModel(
                        "Airtime", 500.0, "APPROVED", "2024-06-25T23:00:00"
                    ), TransactionItemModel(
                        "Data", 500.0, "FAILED", "2024-06-26T23:00:00"
                    ), TransactionItemModel(
                        "Tv", 500.0, "pending", "2024-06-27T23:00:00"
                    ), TransactionItemModel(
                        "Electricity", 500.0, "FAILED", "2024-06-27T23:00:00"
                    )
                )

                _uiState.update {
                    it.copy(
                        transactionList = it.transactionList + data,
//                        isEndReached = !data.pagination.canLoadMore
                    )
                }
                transactionList = _uiState.value.transactionList
//                pagination = data.pagination

            } catch (e: Exception) {
                e.printStackTrace()
                _errorState.emit(e.message ?: "An Error has occurred")
            } finally {
                _uiState.update {
                    it.copy(
                        isTransactionListLoading = false,
                        isLoadingMoreTransactions = false,
                    )
                }
            }
        }
    }

    fun loadMoreTransaction() {
        loadTransactionHistory(true)
    }

    fun refreshTransactions() {
        reset()
        loadTransactionHistory()
    }


    private fun reset() {
//        pagination = Pagination()
        _uiState.update {
            it.copy(
                isEndReached = false, transactionList = listOf()
//                , filterModel = FilterModel()
            )
        }
    }

}