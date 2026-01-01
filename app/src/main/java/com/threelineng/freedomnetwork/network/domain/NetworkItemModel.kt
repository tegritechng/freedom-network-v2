package com.threelineng.freedomnetwork.network.domain

import com.threelineng.freedomnetwork.R
import com.threelineng.freedomnetwork.common.utils.toInitials

data class NetworkItemModel(
    val name: String,
    val performanceRate: Int
) {
    val initials = name.toInitials()

    val rateColor = if (performanceRate > 99) {
        R.color.rate_green
    } else if (performanceRate > 50) {
        R.color.rate_green
    } else if (performanceRate == 50) {
        R.color.rate_yellow
    } else {
        R.color.rate_red
    }

    val rateIcon = when (performanceRate) {
        in 81..Int.MAX_VALUE ->
            R.drawable.ic_network_rate_success

        in 51..81 ->
            R.drawable.ic_network_rate_full

        50 ->
            R.drawable.ic_network_rate_medium

        else ->
            R.drawable.ic_network_rate_low
    }

}