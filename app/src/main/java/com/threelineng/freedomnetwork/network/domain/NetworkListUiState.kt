package com.threelineng.freedomnetwork.network.domain

data class NetworkListUiState(
    val isLoading: Boolean = false,
    val networks: List<NetworkItemModel> = listOf(),
    val searchValue: String = ""
)