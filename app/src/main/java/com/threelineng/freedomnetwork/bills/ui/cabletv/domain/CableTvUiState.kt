package com.threelineng.freedomnetwork.bills.ui.cabletv.domain

data class CableTvUiState(
    val isLoading: Boolean = false,
    val providers: List<CableTvProviderItem> = listOf(),
)