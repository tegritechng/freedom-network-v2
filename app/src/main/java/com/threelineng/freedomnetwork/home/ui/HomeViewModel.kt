package com.threelineng.freedomnetwork.home.ui

import androidx.lifecycle.ViewModel
import com.threelineng.freedomnetwork.R
import com.threelineng.freedomnetwork.home.domain.HomeItemModel
import com.threelineng.freedomnetwork.home.domain.HomeItemType
import com.threelineng.freedomnetwork.home.domain.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel() : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState> get() = _uiState

    init {
        load()
    }

    private fun load() {
        _uiState.update {
            it.copy(agentName = "Stromag Ventures")
        }
    }


    companion object {
        val homeList = listOf<HomeItemModel>(
            HomeItemModel(R.drawable.ic_withdrawal, HomeItemType.WITHDRAW),
            HomeItemModel(R.drawable.ic_transfer, HomeItemType.TRANSFER),
            HomeItemModel(R.drawable.ic_transactions, HomeItemType.END_OF_DAY),
            HomeItemModel(R.drawable.ic_airtime, HomeItemType.AIRTIME),
            HomeItemModel(R.drawable.ic_transactions, HomeItemType.TRANS_HISTORY),
            HomeItemModel(R.drawable.ic_bills, HomeItemType.BANK_NETWORK),
            HomeItemModel(R.drawable.ic_card_balance, HomeItemType.CHECK_CARD_BALANCE),
        )
    }

}