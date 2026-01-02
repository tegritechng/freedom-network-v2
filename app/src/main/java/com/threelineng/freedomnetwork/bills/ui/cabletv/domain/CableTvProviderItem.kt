package com.threelineng.freedomnetwork.bills.ui.cabletv.domain

import android.os.Parcelable
import com.threelineng.freedomnetwork.bills.domain.BillerIconMapper
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class CableTvProviderItem(val name: String): Parcelable {
    @IgnoredOnParcel
    val icon: Int = BillerIconMapper.resolveIcon(name)

}