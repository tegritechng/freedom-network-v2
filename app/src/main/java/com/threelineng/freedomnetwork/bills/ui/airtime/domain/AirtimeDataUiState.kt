package com.threelineng.freedomnetwork.bills.ui.airtime.domain

data class AirtimeDataUiState(
    val isLoading: Boolean = false,
    val providers: List<Any> = listOf(),
    val selectedProvider: Any? = null,
    val isAirtime: Boolean = true,
    val isLoadingDataBundles: Boolean = false,
    val databundles: List<Any> = listOf(),
    val selectedDataBundle: Any? = null,
)