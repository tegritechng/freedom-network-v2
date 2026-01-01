package com.threelineng.freedomnetwork.bills.ui.electricity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.threelineng.freedomnetwork.bills.ui.electricity.domain.ElectricityTvProviderItem
import com.threelineng.freedomnetwork.bills.ui.electricity.domain.ElectricityUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ElectricityViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<ElectricityUiState> = MutableStateFlow(ElectricityUiState())

    val uiState: StateFlow<ElectricityUiState> get() = _uiState
    private val _errorState: MutableSharedFlow<String> = MutableSharedFlow()
    val errorState: SharedFlow<String> get() = _errorState

    init {
        loadProviders()
    }

    private fun loadProviders() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                delay(2000L)
                _uiState.update {
                    it.copy(
                        providers = listOf(
                            ElectricityTvProviderItem("Ibadan Electricity Dist. Company"),
                            ElectricityTvProviderItem(
                                "Eko Electricity Distribution Company"
                            )

                        )
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
}