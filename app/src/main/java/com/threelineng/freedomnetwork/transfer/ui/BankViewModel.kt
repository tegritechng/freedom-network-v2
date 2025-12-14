package com.threelineng.freedomnetwork.transfer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.threelineng.freedomnetwork.R
import com.threelineng.freedomnetwork.transfer.domain.BankItemModel
import com.threelineng.freedomnetwork.transfer.domain.BankListUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.filter
import kotlin.text.contains
import kotlin.text.isEmpty
import kotlin.text.lowercase

class BankViewModel() : ViewModel() {
    private val _uiState: MutableStateFlow<BankListUiState> = MutableStateFlow(BankListUiState())

    val uiState: StateFlow<BankListUiState> get() = _uiState
    private val _errorState: MutableSharedFlow<String> = MutableSharedFlow()
    val errorState: SharedFlow<String> get() = _errorState
    private var bankList: List<BankItemModel> = listOf()

    init {
        load()
    }

    private fun load() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                delay(2000L)
                val banks = listOf(
                    BankItemModel("GTB"),
                    BankItemModel("ACCESS")
                )
                bankList = banks
                _uiState.update {
                    it.copy(banks = banks)
                }
            } catch (e: Exception) {
                _errorState.emit(e.message ?: "An error has occurred")
            } finally {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                    )
                }
            }
        }
    }

    fun searchFilterChanged(value: String) {
        _uiState.update {
            it.copy(
                searchValue = value,
                banks = if (value.isEmpty()) bankList else bankList.filter { item ->
                    item.name.lowercase().contains(value.lowercase())
                })
        }
    }

}