package com.xtrapay.commons

import com.tegritech.commons.R

enum class Reader{
    CT , CTLS, MSR, MANUAL, CT_MSR, CT_CTLS , CT_CTLS_MSR, CT_CTLS_MANUAL, CT_CTLS_MSR_MANUAL;
}


fun Reader.displayMessageId() = when (this) {
    Reader.CT -> R.string.insert_card
    Reader.CTLS -> R.string.tap_card
    Reader.MSR -> R.string.swipe_card
    Reader.MANUAL -> R.string.key_in_card
    Reader.CT_MSR -> R.string.insert_or_swipe_card
    Reader.CT_CTLS -> R.string.insert_or_tap_card
    Reader.CT_CTLS_MANUAL -> R.string.insert_tap_key_card
    Reader.CT_CTLS_MSR -> R.string.insert_swipe_tap_card
    Reader.CT_CTLS_MSR_MANUAL -> R.string.insert_swipe_tap_key_card
}