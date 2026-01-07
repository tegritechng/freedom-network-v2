package com.threelineng.freedomnetwork.bills.ui.airtime.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.threelineng.freedomnetwork.bills.ui.airtime.domain.AirtimeDataUiState
import com.threelineng.freedomnetwork.bills.ui.cabletv.domain.CableTvProviderItem
import com.threelineng.freedomnetwork.bills.ui.cabletv.domain.CableTvUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AirtimeDataViewModel: ViewModel() {
    private val _uiState: MutableStateFlow<AirtimeDataUiState> = MutableStateFlow(AirtimeDataUiState())

    val uiState: StateFlow<AirtimeDataUiState> get() = _uiState
    private val _errorState: MutableSharedFlow<String> = MutableSharedFlow()
    val errorState: SharedFlow<String> get() = _errorState

    init {
        loadBillers()
    }
    private fun loadBillers() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                delay(2000L)
                _uiState.update {
                    it.copy(
                        providers = listOf()
                    )
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

    private fun selectBiller(provider: Any) {
        _uiState.update { it.copy(selectedProvider = provider) }
        if (!_uiState.value.isAirtime) {
            loadDataBundles()
        } else {
            _uiState.update { it.copy(selectedDataBundle = null) }
        }
    }

    fun toggleType() {
        _uiState.update { it.copy(isAirtime = !it.isAirtime) }
    }

    private fun loadDataBundles() {
        _uiState.update { it.copy(isLoadingDataBundles = true) }
        viewModelScope.launch {
            try {
                delay(2000L)
                _uiState.update {
                    it.copy(
                        databundles = listOf()
                    )
                }
            } catch (e: Exception) {
                _errorState.emit(e.message ?: "An error has occurred")
            } finally {
                _uiState.update {
                    it.copy(
                        isLoadingDataBundles = false,
                    )
                }
            }
        }
    }
}