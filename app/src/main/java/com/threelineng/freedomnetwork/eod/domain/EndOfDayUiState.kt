package com.threelineng.freedomnetwork.eod.domain

data class EndOfDayUiState(
    val startDate: String? = null,
    val endDate: String? = null,
    val reportType: EndOfDayReportType? = null
)

enum class EndOfDayReportType {
    SUMMARY, FULL_REPORT
}