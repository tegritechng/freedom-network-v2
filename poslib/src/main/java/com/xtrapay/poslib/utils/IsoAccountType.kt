package com.tegritech.poslib.utils

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
enum class IsoAccountType (val code: String): Parcelable {
    DEFAULT_UNSPECIFIED("00"), SAVINGS("10"), CURRENT("20"), CREDIT("30"),
    UNIVERSAL_ACCOUNT("40"), INVESTMENT_ACCOUNT("50"), BONUS_ACCOUNT("91");



    override fun toString(): String {
        return name.replace('_', ' ').lowercase(Locale.getDefault())
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

    companion object {
        fun parseStringAccountType(acctType: String) = when (acctType.lowercase(Locale.getDefault())) {
            "savings", "saving" -> SAVINGS
            "current" -> CURRENT
            "credit" -> CREDIT
            "universal" -> UNIVERSAL_ACCOUNT
            "investment" -> INVESTMENT_ACCOUNT
            "bonus" -> BONUS_ACCOUNT
            else -> DEFAULT_UNSPECIFIED
        }

        fun parseIntAccountType(type: Int) =  when (type) {
            1, 10 -> SAVINGS

            2, 20 -> CURRENT

            3, 30 -> CREDIT

            4, 40 -> UNIVERSAL_ACCOUNT

            5, 50 -> INVESTMENT_ACCOUNT
            9, 91 -> BONUS_ACCOUNT

            else -> DEFAULT_UNSPECIFIED
        }
    }
}