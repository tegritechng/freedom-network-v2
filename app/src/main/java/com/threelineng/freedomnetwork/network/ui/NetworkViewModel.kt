package com.threelineng.freedomnetwork.network.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.threelineng.freedomnetwork.network.domain.NetworkListUiState
import com.threelineng.freedomnetwork.network.domain.NetworkItemModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NetworkViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<NetworkListUiState> = MutableStateFlow(NetworkListUiState())

    val uiState: StateFlow<NetworkListUiState> get() = _uiState
    private val _errorState: MutableSharedFlow<String> = MutableSharedFlow()
    val errorState: SharedFlow<String> get() = _errorState
    private var networkList: List<NetworkItemModel> = listOf()

    init {
        load()
    }

    private fun load() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                delay(2000L)
                val banks = listOf(
                    NetworkItemModel("GTB", 10),
                    NetworkItemModel("ACCESS", 50),
                    NetworkItemModel("First Bank", 60),
                    NetworkItemModel("Stanbic", 100)
                )
                networkList = banks
                _uiState.update {
                    it.copy(networks = banks)
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
                networks = if (value.isEmpty()) networkList else networkList.filter { item ->
                    item.name.lowercase().contains(value.lowercase())
                })
        }
    }
}