package com.threelineng.freedomnetwork.bills.ui.electricity.domain

data class ElectricityUiState(
    val isLoading: Boolean = false,
    val providers: List<ElectricityTvProviderItem> = listOf(),
)