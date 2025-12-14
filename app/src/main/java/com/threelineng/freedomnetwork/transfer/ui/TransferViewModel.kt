package com.threelineng.freedomnetwork.transfer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.threelineng.freedomnetwork.common.domain.ReceiptDetailsModel
import com.threelineng.freedomnetwork.common.domain.TransactionStatus
import com.threelineng.freedomnetwork.common.utils.generateUniqueSessionId
import com.threelineng.freedomnetwork.transfer.domain.BankItemModel
import com.threelineng.freedomnetwork.transfer.domain.TransferUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.text.isEmpty
import kotlin.to

class TransferViewModel() : ViewModel() {
    private val _uiState: MutableStateFlow<TransferUiState> = MutableStateFlow(TransferUiState())

    val uiState: StateFlow<TransferUiState> get() = _uiState

    private val _errorState: MutableSharedFlow<String> = MutableSharedFlow()
    val errorState: SharedFlow<String> get() = _errorState
    private val _paymentSuccess: MutableSharedFlow<ReceiptDetailsModel> = MutableSharedFlow()
    val paymentSuccess: SharedFlow<ReceiptDetailsModel> get() = _paymentSuccess

    fun setBankItemModel(value: BankItemModel) {
        _uiState.update {
            it.copy(
                selectedBank = value
            )
        }
        if (_uiState.value.accountNumber.length == 10) {
            getAccountName()
        }
    }

    fun accountNumberChanged(value: String) {
        _uiState.update {
            it.copy(
                accountNumber = value
            )
        }
        if (value.isEmpty()) {
            _uiState.update {
                it.copy(
                    accountName = ""
                )
            }
        }
        if (value.length == 10) {
            getAccountName()
        }
    }

    private fun getAccountName() {
        if (_uiState.value.selectedBank == null) {
            return
        }
        _uiState.update { it.copy(isAccountNameLoading = true) }
        viewModelScope.launch {
            try {
                val name = "Loren Ipsum"
                _uiState.update { it.copy(accountName = name) }
            } catch (e: Exception) {
                _errorState.emit(e.message ?: "An error has occurred")
            } finally {
                _uiState.update {
                    it.copy(
                        isAccountNameLoading = false,
                    )
                }
            }
        }
    }

    fun setWalletBalance(walletBalance: String) {
        _uiState.update {
            it.copy(walletBalance = walletBalance)
        }
    }

    fun deposit(
        pin: String, amount: String
    ) {
        if (_uiState.value.selectedBank == null) {
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isTransferring = true, amount = amount) }
            try {
                delay(2000L)
                val date = Date().time
                val model = with(_uiState.value) {
                    ReceiptDetailsModel(
                        status = TransactionStatus.APPROVED,
                        bodyMap = linkedMapOf(
                            "TERMINAL" to "207042EW",
                            "DATE & TIME" to SimpleDateFormat(
                                "dd/MM/yyyy hh:mm:ss a", Locale.ENGLISH
                            ).format(date),
                            "ACCOUNT NAME" to (_uiState.value.accountName ?: ""),
                            "ACCOUNT NUMBER" to (_uiState.value.accountNumber),
                            "BANK NAME" to (selectedBank?.name ?: ""),
                            "TRANS REF" to generateUniqueSessionId(),
                        ),
                        amount = amount,
                    )
                }
                _paymentSuccess.emit(model)
            } catch (e: Exception) {
                _errorState.emit(e.message ?: "An error has occurred")
            } finally {
                _uiState.update {
                    it.copy(
                        isTransferring = false
                    )
                }
            }
        }
    }
}