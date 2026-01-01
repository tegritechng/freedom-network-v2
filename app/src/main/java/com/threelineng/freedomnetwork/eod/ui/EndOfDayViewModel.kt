package com.threelineng.freedomnetwork.eod.ui

import androidx.lifecycle.ViewModel
import com.threelineng.freedomnetwork.eod.domain.EndOfDayReportType
import com.threelineng.freedomnetwork.eod.domain.EndOfDayUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class EndOfDayViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<EndOfDayUiState> = MutableStateFlow(EndOfDayUiState())

    val uiState: StateFlow<EndOfDayUiState> get() = _uiState

    fun setDates(startDate: String, endDate: String? = null) {
        _uiState.update {
            it.copy(startDate = startDate, endDate = endDate)
        }
    }

    fun changeReportType(reportType: EndOfDayReportType) {
        _uiState.update {
            it.copy(reportType = reportType)
        }
    }

}