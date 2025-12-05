package com.threelineng.freedomnetwork.home.domain

import com.threelineng.freedomnetwork.R

data class HomeItemModel(
    val icon: Int, val type: HomeItemType
)

enum class HomeItemType(val title: Int) {
    WITHDRAW(R.string.withdraw),
    TRANSFER(R.string.transfer),
    TRANS_HISTORY(R.string.transaction),
    AIRTIME(R.string.airtime),
    BILLS(R.string.bills),
    END_OF_DAY(R.string.eod),
    BANK_NETWORK(R.string.bank_network),
    CHECK_CARD_BALANCE(R.string.card_balance);
}