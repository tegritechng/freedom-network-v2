package com.threelineng.freedomnetwork.bills.ui.electricity.domain

import com.threelineng.freedomnetwork.bills.domain.BillerIconMapper
import com.threelineng.freedomnetwork.common.utils.toInitials
import kotlinx.parcelize.IgnoredOnParcel

class ElectricityTvProviderItem(val name: String) {
    val initials = name.toInitials()

    @IgnoredOnParcel
    val icon: Int = BillerIconMapper.resolveIcon(name)
}