package com.threelineng.freedomnetwork.bills.ui.cabletv.domain

import com.threelineng.freedomnetwork.bills.domain.BillerIconMapper
import kotlinx.parcelize.IgnoredOnParcel

class CableTvProviderItem(val name: String){
    @IgnoredOnParcel
    val icon: Int = BillerIconMapper.resolveIcon(name)

}