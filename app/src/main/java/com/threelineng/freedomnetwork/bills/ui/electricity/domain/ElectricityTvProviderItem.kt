package com.threelineng.freedomnetwork.bills.ui.electricity.domain

import android.os.Parcelable
import com.threelineng.freedomnetwork.bills.domain.BillerIconMapper
import com.threelineng.freedomnetwork.common.utils.toInitials
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class ElectricityTvProviderItem(val name: String): Parcelable {
    @IgnoredOnParcel
    val initials = name.toInitials()

    @IgnoredOnParcel
    val icon: Int = BillerIconMapper.resolveIcon(name)
}